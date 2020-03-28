package com.cybergstudios.maxheap

import java.awt.image.RenderedImage
import java.io.File
import java.util.concurrent.CountDownLatch

import com.cybergstudios.maxheap.Util.log2
import javafx.application.Platform
import javafx.embed.swing.{JFXPanel, SwingFXUtils}
import javafx.scene.canvas.{Canvas, GraphicsContext}
import javafx.scene.image.WritableImage
import javafx.scene.paint.Color
import javafx.scene.text.{Font, Text}
import javax.imageio.ImageIO
import javax.swing.SwingUtilities

case class Cell(row: Int, col: Int)

case class Elem(v: Int, cell: Cell)

case class Point(x: Double, y: Double)

case class ElemPoint(element: Elem, point: Point)

case class ElemPointRelationShip(
  elemPoint: ElemPoint,
  leftChild: Option[ElemPoint],
  rightChild: Option[ElemPoint]
)

object Render  {

  private val elemWidth = 32
  private val elemHeight = 24
  private val paddingWidth = 2
  private val paddingHeight = 16

  def render(maxHeap: MaxHeap, filename: String): Unit = {
    initializeToolkit
    renderImage(maxHeap, filename)
  }

  private def initializeToolkit(): Unit = {
    // Starting with JavaFx9 we should be able to run
    // Platform.startup(() => ... )
    SwingUtilities.invokeAndWait(() => new JFXPanel())
  }

  private def renderImage(maxHeap: MaxHeap, filename: String): Unit = {
    // A few operations have to run the JavaFx thread
    val latch = new CountDownLatch(1)
    Platform.runLater(() => {
      renderImageHelper(maxHeap, filename)
      latch.countDown()
    })
    latch.await()
  }


  private def renderImageHelper(heap: MaxHeap, filename: String): Unit = {
    val elems = buildElems(heap)
    val (width, height) = calculateDimensions(elems)
    renderAndSaveFile(elems, width, height, filename)
  }

  private def calculateDimensions(elems: List[Elem]): (Int, Int) = {
    val elem = elems.last
    val rows = elem.cell.row + 1
    val height = rows * elemHeight + (rows * paddingHeight)
    val cols = 1 << rows // max col in largest row
    val width = cols * elemWidth + (cols * paddingWidth)
    (width, height)
  }

  /** Returns the Elems in ascending order (row, col). */
  private def buildElems(heap: MaxHeap): List[Elem] = {
    val a: Array[Int] = heap.array()
    val rows = log2(a.length).toInt + 1
    val cols = (1 << rows) - 2
    a.toList.zipWithIndex.foldLeft(Map[Int, List[Elem]]()) { case (acc, (v, i)) =>
      val row = log2(i + 1).toInt
      val colIdx = (i - (1 << row) + 1) % cols // idx in the row without padding.
      val pad = (1 << (rows - row)) - 1 // padding between cols. the first padding is half.
      val col = colIdx + (colIdx * pad) + pad / 2 // col is the idx with padding added.
      val elem = Elem(v, Cell(row, col))
      val entries = elem :: acc.getOrElse(row, List())
      acc + (row -> entries)
    }.values.toList.flatten.sortWith { case (Elem(_, Cell(r1, c1)), Elem(_, Cell(r2, c2))) =>
      if (r1 == r2) c1 < c2 else r1 < r2
    }
  }

  private def renderAndSaveFile(elems: List[Elem], width: Int, height: Int, filename: String): Unit = {
    val canvas: Canvas = new Canvas(width, height)

    val elemPoints = buildElemPoints(elems)
    val relationship = buildRelationShip(elemPoints)

    // draw
    val gc: GraphicsContext = canvas.getGraphicsContext2D
    elemPoints.foreach { case elemPoint @ ElemPoint(Elem(value, _), Point(x, y)) =>
      // rectangle
      gc.setFill(Color.AQUAMARINE)
      gc.fillRect(x, y, elemWidth, elemHeight)

      // node value
      val text = new Text(value.toString)
      val xTextPad = (elemWidth - text.getLayoutBounds.getWidth) / 2
      val yTextPad = text.getLayoutBounds.getHeight
      gc.setFont(Font.font(10))
      gc.setFill(Color.BLACK)
      gc.fillText(value.toString, x + xTextPad, y + yTextPad)

      // connect to children if any
      val (xFrom, yFrom) = lineFrom(x, y)
      def link(target: ElemPoint): Unit = {
        val (xTo, yTo) = lineTo(target.point.x, target.point.y)
        gc.strokeLine(xFrom, yFrom, xTo, yTo)
      }
      relationship.get(elemPoint).foreach { r =>
        r.leftChild.foreach(link)
        r.rightChild.foreach(link)
      }
    }

    // save
    val writableImage = new WritableImage(width, height)
    canvas.snapshot(null, writableImage)
    val renderedImage: RenderedImage = SwingFXUtils.fromFXImage(writableImage, null)
    ImageIO.write(renderedImage, "png", new File(filename))
  }

  private def lineFrom(x: Double, y: Double): (Double, Double) = (x + (elemWidth / 2), y + elemHeight)

  private def lineTo(x: Double, y: Double): (Double, Double) = (x + elemWidth / 2, y)

  private def buildElemPoints(elems: List[Elem]): List[ElemPoint] = {
    elems map { case elem @ Elem(_, Cell(row, col)) =>
      val x = col * paddingWidth + (col * elemWidth)
      val y = row * paddingHeight + (row * elemHeight)
      ElemPoint(elem, Point(x, y))
    }
  }

  private def buildRelationShip(elemPoints: List[ElemPoint]): Map[ElemPoint, ElemPointRelationShip] = {
    val elemPointsMap: Map[Int, ElemPoint] = elemPoints.zipWithIndex
      .map { case (e, idx) => idx -> e }
      .toMap

    elemPointsMap.foldLeft(Map[ElemPoint, ElemPointRelationShip]()) { case (acc, (idx, elemPoint)) =>
      val leftChild: Option[ElemPoint] = elemPointsMap.get(idx * 2 + 1)
      val rightChild: Option[ElemPoint] = elemPointsMap.get(idx * 2 + 2)
      val relationShip = ElemPointRelationShip(elemPoint, leftChild, rightChild)
      acc + (elemPoint -> relationShip)
    }
  }
}
