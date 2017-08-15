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
import com.ubtechinc.alpha.ops.action.play.ILayerResult;

import java.nio.ByteBuffer;

/**
 * @desc : 端口model
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/28
 * @modifier:
 * @modify_time:
 */

public class Port implements IByteProcessor {

    public static final int	TYPE_START = 0;	// OnStart
    public static final int TYPE_CANCEL = 1;	// OnCancel
    public static final int TYPE_STOP = 2;		// OnStop
    public static final int	TYPE_GENERAL = 3;	// GENERAL
    @IntDef(value = {TYPE_START,TYPE_CANCEL,TYPE_STOP,TYPE_GENERAL})
    public @interface PortType{}

    private int mLength;
    private int id;
    private byte direction;
    private int dataType;
    private int type;
    private byte[] data = new byte[0];
    private byte  flag;
    private byte[] nameBytes = new byte[Constraints.PORT_NAME_LENGTH];
    private short[] name = new short[Constraints.PORT_NAME_LENGTH / 2];

    @Override
    public boolean analysis(ByteBufferList bbl, int length) {
        byte[] bytes = new byte[4];
        bbl.get(bytes);
        mLength = ConvertUtils.l_byte2Int(bytes);
        if (length != mLength) return false;

        bbl.get(bytes);
        id = ConvertUtils.l_byte2Int(bytes);

        direction = bbl.get();

        bbl.get(bytes);
        dataType = ConvertUtils.l_byte2Int(bytes);

        bbl.get(bytes);
        type = ConvertUtils.l_byte2Int(bytes);
        if (type < TYPE_START || type > TYPE_GENERAL) return false;

        bbl.get(bytes);
        int dataLength = ConvertUtils.l_byte2Int(bytes);
        data = new byte[dataLength];
        bbl.get(data);


        flag = bbl.get();

        nameBytes = new byte[Constraints.PORT_NAME_LENGTH];
        bbl.get(nameBytes);
        for(int i=0; i < name.length; i+=2){
            byte[] s = new byte[]{nameBytes[i], nameBytes[1+i]};
            name[i] = ConvertUtils.l_byte2Short(s);
        }
        return true;
    }

    @Override
    public byte[] toBytes()  {
        int totalLength = mLength + 4;//写了2次长度
        ByteBuffer bb = ByteBufferList.obtain(totalLength);
        bb.rewind();
        bb.limit(totalLength);

        bb.put(ConvertUtils.l_int2Byte(mLength));
        bb.put(ConvertUtils.l_int2Byte(mLength));
        bb.put(ConvertUtils.l_int2Byte(id));
        bb.put(direction);
        bb.put(ConvertUtils.l_int2Byte(dataType));
        bb.put(ConvertUtils.l_int2Byte(type));
        bb.put(ConvertUtils.l_int2Byte(data.length));
        bb.put(data);
        bb.put(flag);
        bb.put(nameBytes);

        byte[] bytes = new byte[totalLength];
        bb.rewind();
        bb.get(bytes);
        ByteBufferList.reclaim(bb);
        return bytes;
    }

    @Override
    public String toString() {
        return "Port[name="+ new String(nameBytes)+"]";
    }

    ///////////////getter//////////////////////////

    public int getId() {
        return id;
    }

    public byte getDirection() {
        return direction;
    }

    public @ILayerResult.DataType int getDataType() {
        return dataType;
    }

    public @PortType int getType() {
        return type;
    }

    public byte[] getData() {
        return data;
    }

    public byte getFlag() {
        return flag;
    }

    public short[] getName() {
        return name;
    }


    /////////////////端口传数据//////////////////

    /**
     * @param data
     * @param nType
     */
    public void writePortData(byte[] data, int nType){
        if (data.length != this.data.length){
            this.mLength += (this.data.length - data.length);
        }
        this.data = new byte[data.length];
        System.arraycopy(data, 0, this.data, 0, data.length);
        this.dataType = nType;
    }
}


