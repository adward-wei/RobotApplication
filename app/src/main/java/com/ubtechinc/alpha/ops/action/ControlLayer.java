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

package com.ubtechinc.alpha.ops.action;

import com.ubtech.utilcode.utils.ByteBufferList;
import com.ubtech.utilcode.utils.ConvertUtils;

import java.nio.ByteBuffer;

/**
 * @desc : 控制层model:动作层，语音层，逻辑层，flowchart层，tool层
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/28
 * @modifier:
 * @modify_time:
 */

public abstract class ControlLayer implements IByteProcessor {

    private int mLength;

    @Override
    public boolean analysis(ByteBufferList bbl, int length) {
        byte[] bytes = new byte[4];
        bbl.get(bytes);
        mLength = ConvertUtils.l_byte2Int(bytes);
        if (mLength != length) return false;
        return true;
    }

    @Override
    public byte[] toBytes() {
        int totalLength = 4 + 4;
        ByteBuffer bb = ByteBuffer.allocate(totalLength);
        bb.rewind();
        bb.limit(totalLength);
        bb.put(ConvertUtils.l_int2Byte(mLength));
        bb.put(ConvertUtils.l_int2Byte(mLength));
        bb.rewind();
        byte[] bytes = new byte[8];
        bb.get(bytes);
        return bytes;
    }

    public int getLayerTotalLength(){
        return mLength;
    }

}
