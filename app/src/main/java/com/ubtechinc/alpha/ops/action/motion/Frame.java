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
import com.ubtechinc.alpha.ops.action.Constraints;
import com.ubtechinc.alpha.ops.action.IByteProcessor;

import java.nio.ByteBuffer;

/**
 * @desc : 帧
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/28
 * @modifier:
 * @modify_time:
 */

public abstract class Frame implements IByteProcessor, Cloneable {
    protected int mLength;
    protected int id;
    protected int runTime;
    protected int haltTime;
    protected byte[] descBytes = new byte[Constraints.DESC_SIZE];
    protected int extrasLength;

    @Override
    public boolean analysis(ByteBufferList bbl, int length) {
        byte[] bytes = new byte[4];
        bbl.get(bytes);
        mLength = ConvertUtils.l_byte2Int(bytes);
        if (mLength != length) return false;

        bbl.get(bytes);
        id = ConvertUtils.l_byte2Int(bytes);

        bbl.get(bytes);
        runTime = ConvertUtils.l_byte2Int(bytes);

        bbl.get(bytes);
        haltTime = ConvertUtils.l_byte2Int(bytes);

        bbl.get(descBytes);

        bbl.get(bytes);
        extrasLength = ConvertUtils.l_byte2Int(bytes);
        bbl.get(4);

        return true;
    }

    @Override
    public final byte[] toBytes() {
        int totalLength = 4 + mLength;
        ByteBuffer bb = ByteBufferList.obtain(totalLength);
        bb.limit(totalLength);
        bb.rewind();

        bb.put(ConvertUtils.l_int2Byte(mLength));
        bb.put(ConvertUtils.l_int2Byte(mLength));
        bb.put(ConvertUtils.l_int2Byte(id));
        bb.put(ConvertUtils.l_int2Byte(runTime));
        bb.put(ConvertUtils.l_int2Byte(haltTime));
        bb.put(descBytes);
        bb.put(ConvertUtils.l_int2Byte(extrasLength));
        bb.put(ConvertUtils.l_int2Byte(extrasLength));
        bb.put(getExtra());

        bb.rewind();
        byte[] bytes = new byte[totalLength];
        bb.get(bytes);
        ByteBufferList.reclaim(bb);
        return bytes;
    }

    protected abstract byte[] getExtra();

    //////////getter////////////////////
    public int getRunTime() {
        return runTime;
    }

    public int getId(){
        return id;
    }

    public int getHaltTime() {
        return haltTime;
    }

    @Override
    public Frame clone() throws CloneNotSupportedException {
        Frame obj = (Frame) super.clone();
        obj.descBytes = new byte[descBytes.length];
        System.arraycopy(descBytes, 0, obj.descBytes, 0, descBytes.length);
        return obj;
    }
}
