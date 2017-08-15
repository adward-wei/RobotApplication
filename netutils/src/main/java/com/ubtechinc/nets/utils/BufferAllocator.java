package com.ubtechinc.nets.utils;

import com.ubtech.utilcode.utils.ByteBufferList;

import java.nio.ByteBuffer;

/**
 * @desc : ByteBuffer分配
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/5/16
 * @modifier:
 * @modify_time:
 */

public class BufferAllocator {
  final int maxAlloc;
  int currentAlloc = 0;
  int minAlloc = 2 << 11;

  public BufferAllocator(int maxAlloc) {
    this.maxAlloc = maxAlloc;
  }

  public BufferAllocator() {
    maxAlloc = ByteBufferList.MAX_ITEM_SIZE;
  }

  public ByteBuffer allocate() {
    return ByteBufferList.obtain(Math.min(Math.max(currentAlloc, minAlloc), maxAlloc));
  }

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

  public BufferAllocator setMinAlloc(int minAlloc) {
    this.minAlloc = minAlloc;
    return this;
  }
}

