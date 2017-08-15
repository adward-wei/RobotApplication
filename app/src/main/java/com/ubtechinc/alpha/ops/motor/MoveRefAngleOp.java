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
import com.ubtechinc.alpha.cmds.chest.ReadMotorAngleCmd;
import com.ubtechinc.alpha.cmds.chest.SetMotorAngleCmd;
import com.ubtechinc.alpha.ops.OpResult;
import com.ubtechinc.alpha.serial.SerialCmdResult;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;
import com.ubtechinc.alpha.serial.SerialConstants;
import com.ubtechinc.alpha.utils.AngleCheckUtils;

import java.nio.ByteBuffer;

/**
 * @desc : 移动相对角度操作
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/24
 * @modifier:
 * @modify_time:
 */

public class MoveRefAngleOp extends BaseMotorOp {

    private final byte motorId;//舵机id
    private final int offsetAngle;//
    private final short executeTime;//命令执行总时间

    public MoveRefAngleOp(byte motorId, int angle, short executeTime){
        super( NOR_PRIORITY);
        this.motorId = motorId;
        this.offsetAngle = angle;
        this.executeTime = (short) Math.min(Math.max(Command.MIN_TIME, executeTime), Command.MAX_TIME);
    }

    @Override
    protected int waitTime() {
        return executeTime;
    }

    @Override
    protected SerialCommand createCmd(SerialCommandReceiver receiver) {
        ReadMotorAngleCmd cmd = new ReadMotorAngleCmd(receiver, new byte[]{motorId}, true);
        cmd.execute();
        if (cmd.getResult().getError() != SerialConstants.ERR_OK)
            return null;
        byte[] data = cmd.getResult().getResult();
        int angle = ConvertUtils.h_byte2Short(new byte[]{data[2], data[3]});
        angle = AngleCheckUtils.limitAngle(motorId, angle + this.offsetAngle);
        //组装
        ByteBuffer bb = ByteBuffer.allocate(5);
        bb.put(motorId);
        bb.put(ConvertUtils.h_short2Byte((short)angle));
        bb.put(ConvertUtils.h_short2Byte(executeTime));
        bb.rewind();
        byte[] bytes = new byte[bb.remaining()];
        bb.get(bytes);
        //创建命令
        return new SetMotorAngleCmd(receiver, bytes);
    }

    @Override
    protected OpResult parseResult(SerialCmdResult cmdResult) {
        OpResult<String> result = new OpResult<>();
        result.errorCode = cmdResult == null ? SerialConstants.ERR_UNKNOW : cmdResult.getError();
        result.data = null;
        result.cmd = cmdResult == null ? -1: cmdResult.getCmd();
        return result;
    }
}
