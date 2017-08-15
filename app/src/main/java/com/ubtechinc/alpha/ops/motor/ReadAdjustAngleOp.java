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
import com.ubtechinc.alpha.cmds.chest.ReadMotorAdjustAngleCmd;
import com.ubtechinc.alpha.ops.OpResult;
import com.ubtechinc.alpha.serial.SerialCmdResult;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;

/**
 * @desc : 读取舵机校准角度
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/24
 * @modifier:
 * @modify_time:
 */

public class ReadAdjustAngleOp extends BaseMotorOp {
    private final byte motorId;

    public ReadAdjustAngleOp(byte motorId) {
        super(NOR_PRIORITY);
        this.motorId = motorId;
    }

    @Override
    protected int waitTime() {
        return 0;
    }

    @Override
    protected SerialCommand createCmd(SerialCommandReceiver receiver) {
        return new ReadMotorAdjustAngleCmd(receiver, new byte[]{motorId});
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
        if (result == null || result.length != 3) return null;
        byte[] anglebytes = {result[1], result[2]};
        return ConvertUtils.h_byte2Short(anglebytes);
    }
}
