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

import com.ubtech.utilcode.utils.ConvertUtils;
import com.ubtechinc.alpha.cmds.SerialCommand;
import com.ubtechinc.alpha.cmds.chest.ReadMotorAngleCmd;
import com.ubtechinc.alpha.ops.OpResult;
import com.ubtechinc.alpha.serial.SerialCmdResult;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;

/**
 * @desc : 读取舵机角度
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/24
 * @modifier:
 * @modify_time:
 */

public class ReadAngleOp extends BaseMotorOp {

    private final byte motorId;
    private final boolean acdump;//是否掉电读取舵机调度
    public ReadAngleOp(byte motorId, boolean acdump){
        super(NOR_PRIORITY);
        this.motorId = motorId;
        this.acdump = acdump;
    }

    @Override
    protected int waitTime() {
        return 0;
    }

    @Override
    protected SerialCommand createCmd(SerialCommandReceiver receiver) {
        return new ReadMotorAngleCmd(receiver, new byte[]{motorId}, acdump);
    }

    @Override
    protected OpResult<Short> parseResult(SerialCmdResult cmdResult) {
        OpResult<Short> result = new OpResult<>();
        result.errorCode = cmdResult.getError();
        result.data = parseData(cmdResult.getResult());
        result.cmd = cmdResult.getCmd();
        return result;
    }

    private Short parseData(byte[] result) {
        if (result == null || result.length != 4) return null;
        byte[] anglebytes = {result[2], result[3]};
        return ConvertUtils.h_byte2Short(anglebytes);
    }

}
