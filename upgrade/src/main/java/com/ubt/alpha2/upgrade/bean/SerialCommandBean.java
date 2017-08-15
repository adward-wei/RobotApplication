package com.ubt.alpha2.upgrade.bean;

import java.nio.ByteBuffer;

/**
 * Created by ubt on 2017/7/10.
 */

public class SerialCommandBean {
    public boolean hasHeader;
    public boolean hasTail;
    public ByteBuffer buffer;
    public int length;
    public int currentLength;
}
