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
import com.ubtechinc.alpha.ops.action.logic.IfLogic;
import com.ubtechinc.alpha.ops.action.utils.BlockHelper;

import timber.log.Timber;

/**
 * @desc :
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/5/8
 * @modifier:
 * @modify_time:
 */

class IfLogicPlay implements IPlayer, ILayerResult {
    private final IfLogic ifLogic;
    private final Block block;

    //输出
    private byte[] mOutData;

    IfLogicPlay(IfLogic ifLogic, Block mOwnedBlock) {
        this.ifLogic = ifLogic;
        this.block = mOwnedBlock;
    }

    @Override
    public void play(byte[] data, @ILayerResult.DataType int nType) {
       if (data == null || data.length == 0){
           // FIXME: 2017/5/8 数据在执行动作时生成，不考虑编码
           mOutData = "false".getBytes();
           return;
       }
        // FIXME: 2017/5/8 这些代码来自原主服务
       String compareConditions = ifLogic.getCompareConditions();
       if ("2".equals(compareConditions) || "3".equals(compareConditions)){
           String cmp = nType == ILayerResult.TYPE_INT ? String.valueOf(ConvertUtils.l_byte2Int(data)) : new String(data);
           if (cmp.contentEquals(ifLogic.getCompareValues())){
               mOutData = "2".contentEquals(compareConditions)?"true".getBytes():"false".getBytes();
           }else{
               mOutData = !"2".equals(compareConditions)?"true".getBytes():"false".getBytes();
           }
       }else {
           int cmp;
           if (nType == ILayerResult.TYPE_INT){
               cmp = ConvertUtils.l_byte2Int(data);
           }else{
               String s = new String(data);
               try{
                   cmp = Integer.parseInt(s);
               }catch(NumberFormatException e){
                   Timber.w(e.getMessage());
                   cmp = 0;
               }
           }
           int cmpValue;
           try{
               cmpValue = Integer.parseInt(ifLogic.getCompareValues());
           }catch(NumberFormatException e){
               cmpValue = 0;
           }
           if ("0".equals(compareConditions)){
               mOutData = cmp < cmpValue? "true".getBytes() : "false".getBytes();
           }else if ("1".equals(compareConditions)){
               mOutData = cmp <= cmpValue? "true".getBytes(): "false".getBytes();
           }else if ("4".equals(compareConditions)){
               mOutData = cmp > cmpValue ? "true".getBytes() : "false".getBytes();
           }else if ("5".equals(compareConditions)){
               mOutData = cmp >= cmpValue ? "true".getBytes(): "false".getBytes();
           }else{
               mOutData = "false".getBytes();
           }
       }
    }

    @Override
    public byte[] getOutData() {
        return mOutData;
    }

    @Override
    public @ILayerResult.DataType int getOutDataType() {
        return ILayerResult.TYPE_STR;
    }

    @Override
    public int getOutPortId() {
        String out = new String(mOutData);
        return out.equals("true")? BlockHelper.findPortIdByPortName(block, "True")
                :BlockHelper.findPortIdByPortName(block, "False");
    }
}
