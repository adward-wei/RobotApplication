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

package com.ubtechinc.alpha.ops;

import com.ubtechinc.alpha.cmds.SerialCommand;
import com.ubtechinc.alpha.cmds.chest.ChestTransparentCmd;
import com.ubtechinc.alpha.cmds.header.HeadTransparentCmd;
import com.ubtechinc.alpha.serial.SerialCmdResult;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;
import com.ubtechinc.alpha.serial.SerialConstants;

/**
 * @desc : 透传操作：1、从头部发来透传命令，需将数据写入胸部，反之依然
 *                  2、透传命令不满足问答模式
 *                  3、只在2mic中存在
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/25
 * @modifier:
 * @modify_time:
 */
public class TransparentOp extends SerialCmdOp {
    private @SerialConstants.SerialType int type;
    private final byte[] data;

    public TransparentOp(@SerialConstants.SerialType int type , byte[] data) {
        super(NOR_PRIORITY);
        this.type = type;
        this.data = data;
        this.opType = TYPE_SYS_SET;
    }

    @Override
    protected int waitTime() {
        return 0;
    }

    @Override
    protected SerialCommand createCmd(SerialCommandReceiver receiver) {
        if (type == SerialConstants.TYPE_CHEST) {
            return new ChestTransparentCmd(receiver, data);
        }else {
            return new HeadTransparentCmd(receiver, data);
        }
    }

    @Override
    protected OpResult parseResult(SerialCmdResult cmdResult) {
        OpResult result = new OpResult<>();
        result.errorCode = cmdResult.getError();
        result.data = null;
        result.cmd = cmdResult.getCmd();
        return result;
    }
}
