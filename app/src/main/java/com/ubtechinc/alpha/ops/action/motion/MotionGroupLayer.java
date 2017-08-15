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
import java.util.ArrayList;

import timber.log.Timber;

/**
 * @desc : 动作组层
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/28
 * @modifier:
 * @modify_time:
 */

public class MotionGroupLayer implements IMotionLayer {
    private final @IMotionLayer.Type int type;
    protected int mLength;
    protected ArrayList<MotionGroup> groups = new ArrayList<>();//保存了所有组

    public MotionGroupLayer(@IMotionLayer.Type int type){
        this.type = type;
    }

    @Override
    public boolean analysis(ByteBufferList bbl, int length) {
        byte[] bytes = new byte[4];
        bbl.get(bytes);
        mLength = ConvertUtils.l_byte2Int(bytes);
        if (mLength != length) return false;

        bbl.get(bytes);
        int groupCount = ConvertUtils.l_byte2Int(bytes);//组
        for (int i = 0; i < groupCount; i++){
            bbl.get(bytes);
            int groupLength = ConvertUtils.l_byte2Int(bytes);
            ByteBufferList groupBBL = bbl.get(groupLength);
            MotionGroup group = new MotionGroup(type);
            boolean ret = group.analysis(groupBBL, groupLength);
            if (!ret) return false;
            groups.add(group);
        }

        return true;
    }

    @Override
    public byte[] toBytes() {
        //type字节长 + layerlength字节长 + layerlength
        int totalLength = 4 + 4 + mLength;
        ByteBuffer bb = ByteBufferList.obtain(totalLength);
        bb.rewind();
        bb.limit(totalLength);

        bb.put(ConvertUtils.l_int2Byte(type));
        bb.put(ConvertUtils.l_int2Byte(mLength));
        bb.put(ConvertUtils.l_int2Byte(mLength));
        bb.put(ConvertUtils.l_int2Byte(groups.size()));
        int sum = 4 + 4;
        for (int i =0 ; i < groups.size() ; i ++){
            bb.put(groups.get(i).toBytes());
            sum += groups.get(i).toBytes().length;
        }
        Timber.d("MotionGroupLayer:%d,%d",sum, mLength);

        bb.rewind();
        byte[] bytes = new byte[totalLength];
        bb.get(bytes);
        ByteBufferList.reclaim(bb);
        return bytes;
    }

    ///////////////getter//////////////
    public @IMotionLayer.Type int getType() {
        return type;
    }

    public ArrayList<MotionGroup> getGroups() {
        return groups;
    }
}
