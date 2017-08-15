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

package com.ubtechinc.alpha.ops.action.play;

import com.ubtech.utilcode.utils.ConvertUtils;
import com.ubtechinc.alpha.ops.action.Block;
import com.ubtechinc.alpha.ops.action.Port;
import com.ubtechinc.alpha.ops.action.logic.ForLogic;
import com.ubtechinc.alpha.ops.action.utils.BlockHelper;

/**
 * @desc :
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/5/8
 * @modifier:
 * @modify_time:
 */

class ForLogicPlay implements IPlayer,ILayerResult {
    private final ForLogic forLogic;
    private final Block block;

    //输出
    private int mOutPortId;
    private int mCurrentValue;

    ForLogicPlay(ForLogic forLogic, Block mOwnedBlock) {
        this.forLogic = forLogic;
        this.block = mOwnedBlock;
    }

    @Override
    public void play(byte[] data, int nType) {
        mCurrentValue = forLogic.getCurrentValue();
        if (mCurrentValue >= forLogic.getEndValue()){
            mOutPortId = BlockHelper.findPortIdByPortType(block, Port.TYPE_STOP);
        }else{
            mOutPortId = BlockHelper.findPortIdByPortName(block, "Loop");
            forLogic.stepCurrentValue();
        }
    }

    @Override
    public byte[] getOutData() {
        // FIXME: 2017/5/8 使用低位
        return ConvertUtils.l_int2Byte(mCurrentValue);
    }

    @Override
    public @ILayerResult.DataType int getOutDataType() {
        return ILayerResult.TYPE_INT;
    }

    @Override
    public int getOutPortId() {
        return mOutPortId;
    }
}
