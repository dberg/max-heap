package com.cybergstudios.maxheap;

import java.util.ArrayList;
import java.util.List;

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

  public int remove() {
    if (a.size() == 0) throw new IllegalStateException("MaxHeap is empty");
    if (a.size() == 1) return a.remove(0);
    swap(0, a.size() - 1);
    int pop = a.remove(a.size() - 1);
    heapify(0);
    return pop;
  }

  public int[] array() {
    int[] r = new int[a.size()];
    int idx = 0;
    for (int v : a) r[idx++] = v;
    return r;
  }

  private void heapify(int idx) {
    if (idx >= a.size() - 1) return;
    int val = a.get(idx);

    // check left child if any
    int lidx = childLeft(idx);
    if (lidx >= a.size()) return;
    int lval = a.get(lidx);
    int swapIdx = lval > val ? lidx : -1;

    // check right child if any.
    int ridx = childRight(idx);
    if (ridx < a.size()) {
      int rval = a.get(ridx);
      if (rval > lval && rval > val) swapIdx = ridx;
    }

    if (swapIdx < 0) return;
    swap(idx, swapIdx);
    heapify(swapIdx);
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
