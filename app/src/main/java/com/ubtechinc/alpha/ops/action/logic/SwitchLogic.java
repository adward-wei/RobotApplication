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
import java.util.ArrayList;
import java.util.List;

/**
 * @desc :  switch逻辑model
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/28
 * @modifier:
 * @modify_time:
 */

public class SwitchLogic implements ILogic {

    private int type;
    private List<byte[]> mListSwitchValueBytes = new ArrayList<>();
    private List<String> mListSwitchValue = new ArrayList<>();

    @Override
    public boolean analysis(ByteBufferList bbl, int length) {

        byte[] bytes = new byte[4];
        bbl.get(bytes);
        type = ConvertUtils.l_byte2Int(bytes);
        if (type != ILogic.SWITCH_LOGIC) return false;

        while (bbl.hasRemaining()){
            bbl.get(bytes);
            int switchValueLength = ConvertUtils.l_byte2Int(bytes);
            if (switchValueLength > 0){
                byte[] values = new byte[switchValueLength];
                try {
                    String s = new String(values, Constraints.CHARSET_NAME);
                    mListSwitchValue.add(s);
                    mListSwitchValueBytes.add(values);
                } catch (UnsupportedEncodingException e) {
                    return false;
                }
            }else {
                break;
            }
        }
        return true;
    }


    @Override
    public byte[] toBytes() {
        int totalLength = 4 + 4* mListSwitchValueBytes.size();
        for (byte[] data:mListSwitchValueBytes) {
            totalLength += data.length;
        }
        ByteBuffer bb = ByteBufferList.obtain(totalLength);
        bb.rewind();
        bb.limit(totalLength);

        bb.put(ConvertUtils.l_int2Byte(type));
        for (int i = 0; i < mListSwitchValueBytes.size() ; i ++){
            bb.put(ConvertUtils.l_int2Byte(mListSwitchValueBytes.get(i).length));
            bb.put(mListSwitchValueBytes.get(i));
        }

        bb.rewind();
        byte[] bytes = new byte[totalLength];
        bb.get(bytes);
        ByteBufferList.reclaim(bb);
        return bytes;
    }

    ////////getter///////////

    public List<String> getListSwitchValue() {
        return mListSwitchValue;
    }
}
