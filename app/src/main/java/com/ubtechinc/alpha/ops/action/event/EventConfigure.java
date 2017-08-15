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

package com.ubtechinc.alpha.ops.action.event;

import com.ubtech.utilcode.utils.ByteBufferList;
import com.ubtech.utilcode.utils.ConvertUtils;
import com.ubtechinc.alpha.ops.action.IByteProcessor;

import java.nio.ByteBuffer;

/**
 * @desc : 事件配置model
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/28
 * @modifier:
 * @modify_time:
 */

public class EventConfigure implements IByteProcessor {
    private final int type;//不参与序列化

    private int mLength;
    private int distance;

    public EventConfigure(int type) {
        this.type = type;
    }

    @Override
    public boolean analysis(ByteBufferList bbl, int length) {
        byte[] bytes = new byte[4];
        bbl.get(bytes);
        mLength = ConvertUtils.l_byte2Int(bytes);
        if (mLength != length) return false;

        bbl.get(bytes);
        distance = ConvertUtils.l_byte2Int(bytes);
        return true;
    }

    @Override
    public byte[] toBytes() {
        int totalLength = 4 + 4 + 4;
        ByteBuffer bb = ByteBuffer.allocate(totalLength);
        bb.rewind();
        bb.limit(totalLength);

        bb.put(ConvertUtils.l_int2Byte(mLength));
        bb.put(ConvertUtils.l_int2Byte(mLength));
        bb.put(ConvertUtils.l_int2Byte(distance));

        return new byte[0];
    }

    /**
     *
     * @return
     */
    public int getType() {
        return type;
    }
}
