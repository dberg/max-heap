package com.cybergstudios.maxheap

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.collection.mutable.ArrayBuffer
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
    val buffer = new ArrayBuffer[Int]()
    for (i <- 0 until 1000) {
      val r = Random.nextInt
      maxHeap.add(r)
      buffer.append(r)
    }
    val values: List[Int] = buffer.toList.sorted.reverse
    values foreach { i: Int =>
      maxHeap.remove() shouldEqual i
    }
  }
}
