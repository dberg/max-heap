package com.cybergstudios.maxheap;

import java.util.ArrayList;
import java.util.List;

/** Mutable MaxHeap. Not thread-safe. */
public class MaxHeap {

  private List<Integer> a = new ArrayList<>();

  public void add(int i) {
    a.add(i);
    int idx = a.size() - 1;
    int parent;
    while (idx > 0 && a.get(idx) > a.get(parent = (idx - 1) / 2)) {
      swap(idx, parent);
      idx = parent;
    }
  }

  /** O(n) bulk insertion. */
  public static MaxHeap addAll(List<Integer> xs) {
    MaxHeap h = new MaxHeap();
    if (xs == null) return h;
    h.a = new ArrayList<>(xs);
    if (xs.size() == 1) return h;
    int log2 = (int) Util.log2(xs.size());
    int i = ((1 << log2) - 1);
    while (i > 0) h.heapify(--i, h.a.size());
    return h;
  }

  /** Remove the greatest value from the heap. */
  public int remove() {
    if (a.size() == 0) throw new IllegalStateException("MaxHeap is empty");
    if (a.size() == 1) return a.remove(0);
    swap(0, a.size() - 1);
    int pop = a.remove(a.size() - 1);
    heapify(0, a.size());
    return pop;
  }

  /** Returns a copy of the max-heap array representation. */
  public int[] array() {
    int[] r = new int[a.size()];
    int idx = 0;
    for (int v : a) r[idx++] = v;
    return r;
  }

  /**
   * Returns the sorted values.
   * This operation will clear the heap.
   *
   * O(n lg n) if addAll + sorted is performed.
   **/
  public List<Integer> sorted() {
    for (int i = a.size(); i > 1; i--) {
      swap(0, i - 1);
      heapify(0, i - 1);
    }
    List<Integer> b = a;
    a = new ArrayList<>();
    return b;
  }

  private void heapify(int idx, int size) {
    if (idx >= size - 1) return;
    int val = a.get(idx);

    // check left child if any
    int lidx = childLeft(idx);
    if (lidx >= size) return;
    int lval = a.get(lidx);
    int swapIdx = lval > val ? lidx : -1;

    // check right child if any.
    int ridx = childRight(idx);
    if (ridx < size) {
      int rval = a.get(ridx);
      if (rval > lval && rval > val) swapIdx = ridx;
    }

    if (swapIdx < 0) return;
    swap(idx, swapIdx);
    heapify(swapIdx, size);
  }

  private int childLeft(int idx) {
    return idx * 2 + 1;
  }

  private int childRight(int idx) {
    return idx * 2 + 2;
  }

  private void swap(int i, int j) {
    int tmp = a.get(i);
    a.set(i, a.get(j));
    a.set(j, tmp);
  }
}
