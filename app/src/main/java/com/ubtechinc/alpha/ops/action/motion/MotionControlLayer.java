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
import com.ubtechinc.alpha.ops.action.ControlLayer;
import com.ubtechinc.alpha.ops.action.IByteProcessor;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import timber.log.Timber;

/**
 * @desc : 动作控制层model: 舵机，led, music,time
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/28
 * @modifier:
 * @modify_time:
 */

public class MotionControlLayer extends ControlLayer implements IByteProcessor {

    private TimeLayer mTimeLayer;
    private ArrayList<MotionGroupLayer> layers = new ArrayList<>();

    @Override
    public boolean analysis(ByteBufferList bbl, int length) {
        boolean ret =super.analysis(bbl, length);
        if (!ret) return false;

        byte[] bytes = new byte[4];
        bbl.get(bytes);
        int totalLayerCount = ConvertUtils.l_byte2Int(bytes);
        for (int i =0 ; i < totalLayerCount; i++){
            bbl.get(bytes);//type
            @IMotionLayer.Type int type = ConvertUtils.l_byte2Int(bytes);
            MotionGroupLayer layer;
            bbl.get(bytes);
            int layerLength = ConvertUtils.l_byte2Int(bytes);
            ByteBufferList subBBL = bbl.get(layerLength);
            if (type == IMotionLayer.TYPE_TIME){
                mTimeLayer = new TimeLayer(type, layerLength);
                ret = mTimeLayer.analysis(subBBL, layerLength);
                if (!ret) return false;
                continue;
            }else if (isMotionLayer(type)){
                layer = new MotionGroupLayer(type);
                ret = layer.analysis(subBBL, layerLength);
                if (!ret) return false;
            }else {
                return false;
            }
            layers.add(layer);
        }
        return true;
    }

    @Override
    public byte[] toBytes() {
        int totalLength = getLayerTotalLength() + 4;
        ByteBuffer bb = ByteBufferList.obtain(totalLength);
        bb.rewind();
        bb.limit(totalLength);

        bb.put(super.toBytes());
        bb.put(ConvertUtils.l_int2Byte(layers.size() + 1));//加上时间层
        int sum = 4 + 4;//层数字节+总长度字节
        bb.put(mTimeLayer.toBytes());
        sum += mTimeLayer.toBytes().length;
        for (int i = 0; i < layers.size(); i++){
            bb.put(layers.get(i).toBytes());
            sum += layers.get(i).toBytes().length;
        }
        Timber.d("layers:%d,%d", sum, getLayerTotalLength());

        bb.rewind();
        byte[] bytes = new byte[totalLength];
        bb.get(bytes);
        ByteBufferList.reclaim(bb);
        return bytes;
    }

    private boolean isMotionLayer(@IMotionLayer.Type int type){
        return type == IMotionLayer.TYPE_MOTOR ||
                type == IMotionLayer.TYPE_EAR ||
                type == IMotionLayer.TYPE_EYE ||
                type == IMotionLayer.TYPE_MOUTH ||
                type == IMotionLayer.TYPE_MUSIC;
    }

    ///////////////getter////////////////////
    public TimeLayer getTimeLayer() {
        return mTimeLayer;
    }

    public ArrayList<MotionGroupLayer> getLayers() {
        return layers;
    }
}
