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
import com.ubtechinc.alpha.ops.action.ControlLayer;

import java.nio.ByteBuffer;

import timber.log.Timber;

/**
 * @desc : 逻辑控制层
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/28
 * @modifier:
 * @modify_time:
 */

public class LogicControlLayer extends ControlLayer {
    private int type;//不参与序列化

    private ILogic logic;

    @Override
    public boolean analysis(ByteBufferList bbl, int length) {
        boolean ret = super.analysis(bbl, length);//已经读出长度：4个字节
        if (!ret) return false;

        byte[] bytes = bbl.peekBytes(4);
        type = ConvertUtils.l_byte2Int(bytes);

        if (type == ILogic.IF_LOGIC){
            logic = new IfLogic();
        } else if (type == ILogic.FOR_LOGIC){
            logic = new ForLogic();
        }else if (type == ILogic.SWITCH_LOGIC){
            logic = new SwitchLogic();
        }else {
            return false;
        }

        if(bbl.remaining() != (length -4)){
            Timber.d("parse error...");
            return false;
        }

        return logic.analysis(bbl, length - 4);
    }

    @Override
    public byte[] toBytes() {
        int totalLength = 4 + getLayerTotalLength();
        ByteBuffer bb = ByteBufferList.obtain(totalLength);
        bb.limit(totalLength);
        bb.rewind();

        bb.put(super.toBytes());
        bb.put(logic.toBytes());

        bb.rewind();
        byte[] bytes = new byte[totalLength];
        bb.get(bytes);
        ByteBufferList.reclaim(bb);
        return bytes;
    }

    ////////////getter////////
    public @ILogic.Type int getType() {
        return type;
    }

    public ILogic getLogic() {
        return logic;
    }
}
