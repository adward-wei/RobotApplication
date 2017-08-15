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
import com.ubtechinc.alpha.ops.action.logic.SwitchLogic;
import com.ubtechinc.alpha.ops.action.utils.BlockHelper;

/**
 * @desc :
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/5/8
 * @modifier:
 * @modify_time:
 */

class SwitchLogicPlay implements IPlayer,ILayerResult {
    private final SwitchLogic switchLogic;
    private final Block block;
    //输出
    private String mOutStr;

    SwitchLogicPlay(SwitchLogic logic, Block mOwnedBlock) {
        this.switchLogic = logic;
        this.block = mOwnedBlock;
    }

    @Override
    public void play(byte[] data, @ILayerResult.DataType int nType) {
        String strToCmp = (nType == ILayerResult.TYPE_INT) ?
                String.valueOf(ConvertUtils.l_byte2Int(data)) : new String(data);

        for(String sCmp : switchLogic.getListSwitchValue()){
            if (strToCmp.equals(sCmp)){
                mOutStr = sCmp;
                break;
            }
        }
        if (mOutStr == null){
            mOutStr = "Default";
        }
    }

    @Override
    public byte[] getOutData() {
        return mOutStr.getBytes();
    }

    @Override
    public @ILayerResult.DataType int getOutDataType() {
        return ILayerResult.TYPE_STR;
    }

    @Override
    public int getOutPortId() {
        return BlockHelper.findPortIdByPortName(block, mOutStr);
    }
}
