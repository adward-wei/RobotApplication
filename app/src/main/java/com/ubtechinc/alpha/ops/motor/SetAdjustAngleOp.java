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
import com.ubtechinc.alpha.cmds.chest.SetAdjustAngleCmd;
import com.ubtechinc.alpha.ops.OpResult;
import com.ubtechinc.alpha.serial.SerialCmdResult;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;

import java.nio.ByteBuffer;

/**
 * @desc : 设置舵机校准角度
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/24
 * @modifier:
 * @modify_time:
 */

public class SetAdjustAngleOp extends BaseMotorOp {

    private final byte motorId;
    private final int angle;

    public SetAdjustAngleOp(byte motorId, int angle) {
        super(NOR_PRIORITY);
        this.motorId = motorId;
        this.angle = angle;
    }

    @Override
    protected int waitTime() {
        return 0;
    }

    @Override
    protected SerialCommand createCmd(SerialCommandReceiver receiver) {
        ByteBuffer bb = ByteBuffer.allocate(3);
        bb.put(motorId);
        bb.put(ConvertUtils.h_short2Byte((short)angle));
        bb.rewind();
        byte[] bytes = new byte[bb.remaining()];
        bb.get(bytes);
        return new SetAdjustAngleCmd(receiver, bytes);
    }

    @Override
    protected OpResult parseResult(SerialCmdResult cmdResult) {
        OpResult result = new OpResult<>();
        result.errorCode = cmdResult.getError();
        result.cmd = cmdResult.getCmd();
        result.data = null;
        return result;
    }
}
