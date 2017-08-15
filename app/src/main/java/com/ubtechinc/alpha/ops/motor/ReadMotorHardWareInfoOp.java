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

package com.ubtechinc.alpha.ops.motor;

import com.ubtechinc.alpha.cmds.SerialCommand;
import com.ubtechinc.alpha.cmds.chest.ReadMotorHardWareInfoCmd;
import com.ubtechinc.alpha.ops.OpResult;
import com.ubtechinc.alpha.ops.SerialCmdOp;
import com.ubtechinc.alpha.serial.SerialCmdResult;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;

/**
 * @desc : 读取舵机硬件信息
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/24
 * @modifier:
 * @modify_time:
 */
@Deprecated
public class ReadMotorHardWareInfoOp extends SerialCmdOp {
    private final byte motorId;

    public ReadMotorHardWareInfoOp(byte motorId) {
        super(NOR_PRIORITY);
        this.motorId = motorId;
    }

    @Override
    protected int waitTime() {
        return 0;
    }

    @Override
    protected SerialCommand createCmd(SerialCommandReceiver receiver) {
        return new ReadMotorHardWareInfoCmd(receiver, new byte[]{motorId});
    }

    @Override
    protected OpResult parseResult(SerialCmdResult cmdResult) {
        OpResult result = new OpResult<>();
        result.errorCode = cmdResult.getError();
        result.data = null;
        result.cmd = cmdResult.getCmd();
        // TODO: 2017/4/25 添加舵机硬件信息实体
        return result;
    }
}
