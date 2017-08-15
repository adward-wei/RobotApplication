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
 * @desc : 时间层model
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/28
 * @modifier:
 * @modify_time:
 */

public class TimeLayer implements IMotionLayer {

    private final int type;
    private final int mLength;
    private int mTimeScale;
    private int mTime;

    public TimeLayer(int type, int length) {
        this.type = type;
        this.mLength = length;
    }

    @Override
    public boolean analysis(ByteBufferList bbl, int length) {
        if (bbl.remaining() != 8 || length != 8)
            return false;

        byte[] bytes = new byte[4];
        bbl.get(bytes);
        mTimeScale = ConvertUtils.l_byte2Int(bytes);

        bbl.get(bytes);
        mTime = ConvertUtils.l_byte2Int(bytes);

        return true;
    }

    @Override
    public byte[] toBytes() {
        int totalLength = 4 + 4 + 4 + 4;
        ByteBuffer bb = ByteBuffer.allocate(totalLength);
        bb.rewind();
        bb.limit(totalLength);

        bb.put(ConvertUtils.l_int2Byte(type));
        bb.put(ConvertUtils.l_int2Byte(mLength));
        bb.put(ConvertUtils.l_int2Byte(mTimeScale));
        bb.put(ConvertUtils.l_int2Byte(mTime));

        bb.rewind();
        byte[] bytes = new byte[totalLength];
        bb.get(bytes);
        return bytes;
    }

    //////////////////getter//////////////////
    public int getTimeScale() {
        return mTimeScale;
    }

    public int getTime() {
        return mTime;
    }
}
