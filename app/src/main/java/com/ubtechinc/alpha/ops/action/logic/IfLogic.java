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

package com.ubtechinc.alpha.ops.action.logic;

import com.ubtech.utilcode.utils.ByteBufferList;
import com.ubtech.utilcode.utils.ConvertUtils;
import com.ubtechinc.alpha.ops.action.Constraints;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * @desc : if逻辑model
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/28
 * @modifier:
 * @modify_time:
 */

public class IfLogic implements ILogic {

    private int type;
    private int mCompareValueLength;
    private byte[] mCompareValueBytes = new byte[0];
    private String mCompareValues;
    private int mConditionValueLength;
    private byte[] mConditionsValueBytes = new byte[0];
    private String mCompareConditions;

    @Override
    public boolean analysis(ByteBufferList bbl, int length) {
        byte[] bytes = new byte[4];
        bbl.get(bytes);
        type = ConvertUtils.l_byte2Int(bytes);
        if (type != ILogic.IF_LOGIC) return false;

        bbl.get(bytes);
        mCompareValueLength = ConvertUtils.h_byte2Short(bytes);//比较值的长度
        if (mCompareValueLength > 0){
            mCompareValueBytes = new byte[mCompareValueLength];
            bbl.get(mCompareValueBytes);
            try {
                mCompareValues = new String(mCompareValueBytes, Constraints.CHARSET_NAME);
            } catch (UnsupportedEncodingException e) {
                return false;
            }
        }

        bbl.get(bytes);
        mConditionValueLength = ConvertUtils.l_byte2Int(bytes);
        if (mConditionValueLength > 0){
            mConditionsValueBytes = new byte[mConditionValueLength];
            bbl.get(mConditionsValueBytes);
            try {
                mCompareConditions = new String(mConditionsValueBytes, Constraints.CHARSET_NAME);
            } catch (UnsupportedEncodingException e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public byte[] toBytes() {
        int totalLength = 4 + 4 + mCompareValueLength + 4 + mConditionValueLength;
        ByteBuffer bb = ByteBuffer.allocate(totalLength);
        bb.limit(totalLength);
        bb.rewind();

        bb.put(ConvertUtils.l_int2Byte(type));
        bb.put(ConvertUtils.l_int2Byte(mCompareValueLength));
        bb.put(mCompareValueBytes);
        bb.put(ConvertUtils.l_int2Byte(mConditionValueLength));
        bb.put(mConditionsValueBytes);

        bb.rewind();
        byte[] bytes = new byte[totalLength];
        bb.get(bytes);
        return bytes;
    }

    ////////////////////getter////////////////////


    public String getCompareValues() {
        return mCompareValues;
    }

    public String getCompareConditions() {
        return mCompareConditions;
    }
}
