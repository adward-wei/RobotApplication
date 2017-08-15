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
import com.ubtechinc.alpha.cmds.chest.MotorSendDataCmd;
import com.ubtechinc.alpha.ops.OpResult;
import com.ubtechinc.alpha.serial.SerialCmdResult;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;

import java.nio.ByteBuffer;

/**
 * @desc : 向所有舵机发送数据操作
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/24
 * @modifier:
 * @modify_time:
 */

public class SendMotorDataOp extends BaseMotorOp {
    private final byte[] angles;
    private final short executeTime;

    public SendMotorDataOp(byte[] angles, short executeTime){
        super(NOR_PRIORITY);
        this.angles = angles;
        this.executeTime = (short) Math.min(Math.max(Command.MIN_TIME, executeTime), Command.MAX_TIME);;
    }

    @Override
    protected int waitTime() {
        return executeTime;
    }

    @Override
    protected SerialCommand createCmd(SerialCommandReceiver receiver) {
        ByteBuffer bb = ByteBuffer.allocate(angles.length + 2);
        bb.put(angles);
        bb.put(ConvertUtils.h_short2Byte(executeTime));
        bb.rewind();
        byte[] bytes = new byte[bb.remaining()];
        bb.get(bytes);
        return new MotorSendDataCmd(receiver, bytes);
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
