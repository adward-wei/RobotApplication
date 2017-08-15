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

import android.support.annotation.IntDef;

import com.ubtech.utilcode.utils.ByteBufferList;
import com.ubtech.utilcode.utils.ConvertUtils;
import com.ubtechinc.alpha.ops.action.logic.LogicControlLayer;
import com.ubtechinc.alpha.ops.action.motion.MotionControlLayer;
import com.ubtechinc.alpha.ops.action.voice.VoiceControlLayer;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import timber.log.Timber;

/**
 * @desc :块model
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/28
 * @modifier:
 * @modify_time:
 */

public class Block implements IByteProcessor {
    //////////////////////////////////////
    /**动作功能块*/
    public static final int BLOCK_ACTION = 0;
    /**流程图块*/
    public static final int BLOCK_FLOWCHART = 1;
    /**逻辑块*/
    public static final int BLOCK_LOGIC = 2;
    /**工具块*/
    public static final int BLOCK_TOOL = 3;
    /**语音块*/
    public static final int BLOCK_VOICE = 4;
    @IntDef(value = {BLOCK_ACTION, BLOCK_FLOWCHART, BLOCK_LOGIC, BLOCK_TOOL, BLOCK_VOICE})
    public @interface Type{}

    private int mLength;
    private int portTotalLength;
    private ArrayList<Port> ports = new ArrayList<>();
    private int x;
    private int y;
    private byte[] titleBytes;//100B
    private String title;
    private byte[] descBytes;//100B
    private String desc;
    private int id;
    private int type;
    private int linkToId;
    private ControlLayer mLayer;//层

    @Override
    public boolean analysis(ByteBufferList bbl, int length) {
        byte[] bytes = new byte[4];
        bbl.get(bytes);
        mLength = ConvertUtils.l_byte2Int(bytes);
        if (mLength != length) return false;

        bbl.get(bytes);//端口
        portTotalLength = ConvertUtils.l_byte2Int(bytes);
        if (portTotalLength > 0){
            bbl.get(4);
            bbl.get(bytes);
            int portCount = ConvertUtils.l_byte2Int(bytes);
            for (int i = 0; i< portCount; i++){
                bbl.get(bytes);
                int portLength = ConvertUtils.l_byte2Int(bytes);
                ByteBufferList portBBL = bbl.get(portLength);
                Port port = new Port();
                boolean ret = port.analysis(portBBL, portLength);
                if (!ret) return false;
                ports.add(port);
            }
        }

        bbl.get(bytes);
        x = ConvertUtils.l_byte2Int(bytes);

        bbl.get(bytes);
        y = ConvertUtils.l_byte2Int(bytes);

        titleBytes = new byte[100];
        bbl.get(titleBytes);
        try {
            title = new String(titleBytes, Constraints.CHARSET_NAME);
        } catch (UnsupportedEncodingException e) {
            Timber.d(e.getMessage());
        }

        descBytes = new byte[100];
        bbl.get(descBytes);
        try {
            desc = new String(descBytes, Constraints.CHARSET_NAME);
        } catch (UnsupportedEncodingException e) {
            Timber.d(e.getMessage());
        }

        bbl.get(bytes);
        id = ConvertUtils.l_byte2Int(bytes);

        bbl.get(bytes);
        type = ConvertUtils.l_byte2Int(bytes);

        bbl.get(bytes);
        linkToId = ConvertUtils.l_byte2Int(bytes);

        bbl.get(bytes);
        int layerTotalLength = ConvertUtils.l_byte2Int(bytes);

        if (layerTotalLength > 0) {
            ByteBufferList layerBBL = bbl.get(layerTotalLength);
            //层解析
            if (type == BLOCK_ACTION) {
                mLayer = new MotionControlLayer();
                return mLayer.analysis(layerBBL, layerTotalLength);
            } else if (type == BLOCK_FLOWCHART) {
                mLayer = null;//流程图块没有数据layerTotalLength == 0
            } else if (type == BLOCK_LOGIC) {
                mLayer = new LogicControlLayer();
                return mLayer.analysis(layerBBL, layerTotalLength);
            } else if (type == BLOCK_TOOL) {
                mLayer = new ToolControlLayer();
                return mLayer.analysis(layerBBL, layerTotalLength);
            } else if (type == BLOCK_VOICE) {
                mLayer = new VoiceControlLayer();
               return mLayer.analysis(layerBBL, layerTotalLength);
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public byte[] toBytes(){
        int totalLength = mLength + 4;
        ByteBuffer bb = ByteBufferList.obtain(totalLength);
        bb.rewind();
        bb.limit(totalLength);

        bb.put(ConvertUtils.l_int2Byte(mLength));
        bb.put(ConvertUtils.l_int2Byte(mLength));
        bb.put(ConvertUtils.l_int2Byte(portTotalLength));
        bb.put(ConvertUtils.l_int2Byte(portTotalLength));
        bb.put(ConvertUtils.l_int2Byte(ports.size()));
        int sum = 0;
        for (int i=0; i< ports.size(); i++){
            bb.put(ports.get(i).toBytes());
            sum += ports.get(i).toBytes().length;
        }
        Timber.d("port:%d,%d", sum, portTotalLength);

        bb.put(ConvertUtils.l_int2Byte(x));
        bb.put(ConvertUtils.l_int2Byte(y));
        bb.put(titleBytes);
        bb.put(descBytes);
        bb.put(ConvertUtils.l_int2Byte(id));
        bb.put(ConvertUtils.l_int2Byte(type));
        bb.put(ConvertUtils.l_int2Byte(linkToId));

        if (mLayer != null){
            bb.put(mLayer.toBytes());
        }

        bb.rewind();
        byte[] bytes = new byte[totalLength];
        bb.get(bytes);
        ByteBufferList.reclaim(bb);
        return bytes;
    }

    ///////////getter///////////

    public int getId() {
        return id;
    }

    public @Type int getType() {
        return type;
    }

    public int getLinkToId() {
        return linkToId;
    }

    public ControlLayer getLayer() {
        return mLayer;
    }

    public ArrayList<Port> getPorts() {
        return ports;
    }


}
