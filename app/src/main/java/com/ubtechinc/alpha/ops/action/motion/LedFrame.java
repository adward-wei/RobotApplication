/*
 *
 *  *
 *  *  *
 *  *  * Copyright (c) 2008-2017 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *  *
 *  *
 *
 */

package com.ubtechinc.alpha.ops.action.motion;

import com.ubtech.utilcode.utils.ByteBufferList;
import com.ubtech.utilcode.utils.ConvertUtils;

import java.nio.ByteBuffer;

/**
 * @desc : 灯帧model
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/28
 * @modifier:
 * @modify_time:
 */

public class LedFrame extends Frame {
    private final @IMotionLayer.Type
    int type;//不参与序列

    private byte leftLed;
    private byte rightLed;
    private byte bright;
    private byte color;//眼睛灯有
    private int  onTime;
    private int  offTime;

    private int mode;//嘴巴灯有

    public LedFrame(@IMotionLayer.Type int type) {
        super();
        this.type = type;
    }

    @Override
    public final boolean analysis(ByteBufferList bbl, int length) {
        boolean ret = super.analysis(bbl, length);
        if (!ret) return false;

        byte[] bytes = new byte[4];
        if (isMouthLed()){
            if (bbl.remaining() != 13) return false;
            bbl.get(bytes);
            mode = ConvertUtils.l_byte2Int(bytes);
            bright = bbl.get();
            onTime = ConvertUtils.l_byte2Int(bytes);
            offTime = ConvertUtils.l_byte2Int(bytes);
            return true;
        }

        if (isEyeLed()) {
            if (bbl.remaining() != 12) return false;
        }else {
            if (bbl.remaining() != 11) return false;
        }

        leftLed = bbl.get();
        rightLed = bbl.get();
        bright = bbl.get();

        if (isEyeLed()){
            color = bbl.get();
        }

        bbl.get(bytes);
        onTime = ConvertUtils.l_byte2Int(bytes);

        bbl.get(bytes);
        offTime = ConvertUtils.l_byte2Int(bytes);
        return true;
    }

    private boolean isEyeLed() {
        return type == IMotionLayer.TYPE_EYE;
    }

    private boolean isMouthLed() {
        return type == IMotionLayer.TYPE_MOUTH;
    }

    @Override
    protected final byte[] getExtra() {
        if (isMouthLed()){
            int extraLength = 4 + 1 + 4 * 2;
            ByteBuffer bb = ByteBuffer.allocate(extraLength);
            bb.limit(extraLength);
            bb.rewind();
            bb.put(ConvertUtils.l_int2Byte(mode));
            bb.put(bright);
            bb.put(ConvertUtils.l_int2Byte(onTime));
            bb.put(ConvertUtils.l_int2Byte(offTime));
            bb.rewind();
            byte[] bytes = new byte[extraLength];
            bb.get(bytes);
            return bytes;
        }

        int extraLength = 3 + (isEyeLed() ? 1: 0) + 4 * 2;
        ByteBuffer bb = ByteBuffer.allocate(extraLength);
        bb.limit(extraLength);
        bb.rewind();

        bb.put(leftLed);
        bb.put(rightLed);
        bb.put(bright);
        if (isEyeLed())
            bb.put(color);

        bb.put(ConvertUtils.l_int2Byte(onTime));
        bb.put(ConvertUtils.l_int2Byte(offTime));

        bb.rewind();
        byte[] bytes = new byte[extraLength];
        bb.get(bytes);
        return bytes;
    }


    @Override
    public Frame clone() throws CloneNotSupportedException {
        LedFrame newObj = (LedFrame) super.clone();
        return newObj;
    }

    ///////////////////getter///////////////////

    public byte getLeftLed() {
        return leftLed;
    }

    public byte getRightLed() {
        return rightLed;
    }

    public byte getBright() {
        return bright;
    }

    public byte getColor() {
        return color;
    }

    public int getOnTime() {
        return onTime;
    }

    public int getMode() {
        return mode;
    }

    public int getOffTime() {
        return offTime;
    }

    public @IMotionLayer.Type int getType() {
        return type;
    }
}
