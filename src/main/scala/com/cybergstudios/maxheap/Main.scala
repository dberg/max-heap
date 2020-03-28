package com.cybergstudios.maxheap

import scala.util.Random

object Main {
  def main(args: Array[String]): Unit = {
    val maxHeap = buildMaxheap
    val filename = "/tmp/maxheap.png"
    Render.render(maxHeap, filename)
  }

  def buildMaxheap: MaxHeap = {
    val maxHeap = new MaxHeap
    for (i <- 0 until 31) {
      maxHeap.add(Random.nextInt(10000))
    }
    maxHeap
  }
}
