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
import com.ubtechinc.alpha.cmds.Command;
import com.ubtechinc.alpha.cmds.SerialCommand;
import com.ubtechinc.alpha.cmds.chest.SetMotorAngleCmd;
import com.ubtechinc.alpha.ops.OpResult;
import com.ubtechinc.alpha.serial.SerialCmdResult;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;
import com.ubtechinc.alpha.utils.AngleCheckUtils;

import java.nio.ByteBuffer;

/**
 * @desc : 舵机移动绝对角度操作
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/17
 * @modifier:
 * @modify_time:
 */
public class MoveAbsAngleOp extends BaseMotorOp {

    private final byte motorId;//舵机id
    private final int angle;//
    private final short executeTime;//命令执行总时间

    public MoveAbsAngleOp( byte motorId, int angle, short executeTime){
        super(NOR_PRIORITY);
        this.motorId = motorId;
        this.angle = AngleCheckUtils.limitAngle(motorId, angle);
        this.executeTime = (short) Math.min(Math.max(Command.MIN_TIME, executeTime), Command.MAX_TIME);
    }

    @Override
    protected SerialCommand createCmd(SerialCommandReceiver receiver) {
        ByteBuffer bb = ByteBuffer.allocate(5);
        bb.put(motorId);
        bb.put(ConvertUtils.h_short2Byte((short) angle));
        bb.put(ConvertUtils.h_short2Byte(executeTime));
        bb.rewind();
        byte[] bytes = new byte[bb.remaining()];
        bb.get(bytes);
        return new SetMotorAngleCmd(receiver, bytes);
    }

    @Override
    protected OpResult parseResult(SerialCmdResult cmdResult) {
        OpResult result = new OpResult<>();
        result.errorCode = cmdResult.getError();
        result.data = null;
        result.cmd = cmdResult.getCmd();
        return result;
    }

    @Override
    protected int waitTime() {
        return executeTime;
    }
}
