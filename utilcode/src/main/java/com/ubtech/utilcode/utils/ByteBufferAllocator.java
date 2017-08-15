package com.ubtech.utilcode.utils;

import java.nio.ByteBuffer;
/**
 *
 *     @author: Logic
 *     @time  : 2017/3/16
 *     desc  : ByteBuffer 分配器
 *
 *
 */
public class ByteBufferAllocator {
  final int maxAlloc;
  int currentAlloc = 0;
  int minAlloc = 2 << 11;

  public ByteBufferAllocator(int maxAlloc) {
    this.maxAlloc = maxAlloc;
  }

  public ByteBufferAllocator() {
    maxAlloc = ByteBufferList.MAX_ITEM_SIZE;
  }

  public ByteBuffer allocate() {
    return ByteBufferList.obtain(Math.min(Math.max(currentAlloc, minAlloc), maxAlloc));
  }

  /**
   * 根据read，扩容分配器
   */
  public void track(long read) {
    currentAlloc = (int) read * 2;
  }

  public int getMaxAlloc() {
    return maxAlloc;
  }

  public void setCurrentAlloc(int currentAlloc) {
    this.currentAlloc = currentAlloc;
  }

  public int getMinAlloc() {
    return minAlloc;
  }

  public ByteBufferAllocator setMinAlloc(int minAlloc) {
    this.minAlloc = minAlloc;
    return this;
  }
}

