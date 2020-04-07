package com.cybergstudios.maxheap

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.collection.mutable.ArrayBuffer
import scala.jdk.CollectionConverters._
import scala.util.Random

class MaxHeapSpec extends AnyFlatSpec with Matchers {
  "The MaxHeap class" should "return the greatest elements" in {
    val maxHeap = new MaxHeap()
    maxHeap.add(10)
    maxHeap.add(20)
    maxHeap.add(1)
    maxHeap.add(30)
    maxHeap.add(40)
    maxHeap.add(25)

    maxHeap.remove() shouldEqual 40
    maxHeap.remove() shouldEqual 30
    maxHeap.remove() shouldEqual 25
    maxHeap.remove() shouldEqual 20
    maxHeap.remove() shouldEqual 10
    maxHeap.remove() shouldEqual 1
  }

  "The MaxHeap class" should "return the greatest elements randomly generated" in {
    val maxHeap = new MaxHeap()
    generateRandomList(1000) foreach { maxHeap.add }
    validate(maxHeap)
  }

  "The MaxHeap class" should "bulk insert randomly generated list of values" in {
    val xs = generateRandomList(1000)
    val maxHeap = MaxHeap.addAll(xs.asJava.asInstanceOf[java.util.List[java.lang.Integer]])
    validate(maxHeap)
  }

  "The MaxHeap class" should "sort its content" in {
    val xs = generateRandomList(1000)
    val maxHeap = MaxHeap.addAll(xs.asJava.asInstanceOf[java.util.List[java.lang.Integer]])
    xs.sorted.zip(maxHeap.sorted().asScala) foreach { case (x, y) =>
      x shouldEqual y
    }
  }

  private def generateRandomList(n: Int): List[Int] = {
    val buffer = new ArrayBuffer[Int]()
    for (_ <- 0 until n) {
      val r = Random.nextInt
      buffer.append(r)
    }
    buffer.toList
  }

  private def validate(maxHeap: MaxHeap): Unit = {
    maxHeap.array().sorted.reverse.foreach { i: Int =>
      maxHeap.remove() shouldEqual i
    }
  }
}
