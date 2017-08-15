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
import com.ubtechinc.alpha.ops.action.Page;

import java.nio.ByteBuffer;

import timber.log.Timber;

/**
 * @desc : 流程动作事件model
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/28
 * @modifier:
 * @modify_time:
 */

public class MotionEvent implements IByteProcessor {
    /**红外距离事件*/
    public static final int TYPE_INFRARE = 0;
    /**声纳事件*/
    public static final int TYPE_SONAR = 1;

    private final int type;
    private int mLength;

    private EventConfigure configure;
    private Page page;

    public MotionEvent(int eventType) {
        this.type = eventType;
    }

    @Override
    public boolean analysis(ByteBufferList bbl, int length) {
        byte[] bytes = new byte[4];
        bbl.get(bytes);
        mLength = ConvertUtils.l_byte2Int(bytes);
        if (mLength != length) return false;

        bbl.get(bytes);
        int configureDataLength = ConvertUtils.l_byte2Int(bytes);
        if (configureDataLength > 0){
            ByteBufferList configsBBL = bbl.get(configureDataLength);
            configure = new EventConfigure(type);
            boolean ret = configure.analysis(configsBBL, configureDataLength);
            if (!ret) return false;
        }

        bbl.get(bytes);
        int pageDataLength = ConvertUtils.l_byte2Int(bytes);
        if (pageDataLength > 0){
            ByteBufferList pageBBL = bbl.get(pageDataLength);
            page = new Page();
            boolean ret = page.analysis(pageBBL, pageDataLength);
            if (!ret) return false;
        }

        return true;
    }

    @Override
    public byte[] toBytes() {
        //type字节数 + lenght字节数 + mLength
        int totalLenght = 4 + 4 + mLength;
        ByteBuffer bb = ByteBufferList.obtain(totalLenght);
        bb.rewind();
        bb.limit(totalLenght);

        bb.put(ConvertUtils.l_int2Byte(type));
        bb.put(ConvertUtils.l_int2Byte(mLength));
        bb.put(ConvertUtils.l_int2Byte(mLength));
        bb.put(configure.toBytes());
        bb.put(page.toBytes());

        Timber.d("%d = %d", configure.toBytes().length + page.toBytes().length + 4, mLength);

        bb.rewind();
        byte[] bytes = new byte[totalLenght];
        bb.get(bytes);
        ByteBufferList.reclaim(bb);
        return bytes;
    }
}
