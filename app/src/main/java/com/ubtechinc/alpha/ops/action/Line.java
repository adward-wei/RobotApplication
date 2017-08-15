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
 * @desc : 连接线model
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/28
 * @modifier:
 * @modify_time:
 */

public class Line implements IByteProcessor {

    private int mLength;
    private int mOutBlockId;
    private int mOutPortId;
    private int mInBlockId;
    private int mInPortId;

    @Override
    public boolean analysis(ByteBufferList bbl, int length) {
        byte[] bytes = new byte[4];
        bbl.get(bytes);
        mLength = ConvertUtils.l_byte2Int(bytes);
        if (mLength != length) return false;

        bbl.get(bytes);
        mOutBlockId = ConvertUtils.l_byte2Int(bytes);

        bbl.get(bytes);
        mOutPortId = ConvertUtils.l_byte2Int(bytes);

        bbl.get(bytes);
        mInBlockId = ConvertUtils.l_byte2Int(bytes);

        bbl.get(bytes);
        mInPortId = ConvertUtils.l_byte2Int(bytes);

        return true;
    }


    @Override
    public byte[] toBytes() {
        int totalLength = mLength + 4;//写了2次长度
        ByteBuffer bb = ByteBuffer.allocate(totalLength);
        bb.rewind();
        bb.limit(totalLength);

        bb.put(ConvertUtils.l_int2Byte(mLength));
        bb.put(ConvertUtils.l_int2Byte(mLength));
        bb.put(ConvertUtils.l_int2Byte(mOutBlockId));
        bb.put(ConvertUtils.l_int2Byte(mOutPortId));
        bb.put(ConvertUtils.l_int2Byte(mInBlockId));
        bb.put(ConvertUtils.l_int2Byte(mInPortId));
        bb.rewind();
        byte[] bytes = new byte[totalLength];
        bb.get(bytes);

        return bytes;
    }

    //////////////getter/////////////
    public int getOutBlockId() {
        return mOutBlockId;
    }

    public int getOutPortId() {
        return mOutPortId;
    }

    public int getInBlockId() {
        return mInBlockId;
    }

    public int getInPortId() {
        return mInPortId;
    }
}
