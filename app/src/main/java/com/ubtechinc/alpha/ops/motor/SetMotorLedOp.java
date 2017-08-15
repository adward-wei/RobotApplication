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
import com.ubtechinc.alpha.cmds.chest.SetMotorLedStateCmd;
import com.ubtechinc.alpha.ops.OpResult;
import com.ubtechinc.alpha.ops.SerialCmdOp;
import com.ubtechinc.alpha.serial.SerialCmdResult;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;
import com.ubtechinc.alpha.serial.SerialConstants;

import java.nio.ByteBuffer;

/**
 * @desc : 设置舵机上led状态
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/24
 * @modifier:
 * @modify_time:
 */
@Deprecated
public class SetMotorLedOp extends SerialCmdOp {

    private final byte motorId;
    private final boolean on;

    public SetMotorLedOp(byte motorId, boolean isOn) {
        super(NOR_PRIORITY);
        this.motorId = motorId;
        this.on = isOn;
    }

    @Override
    protected int waitTime() {
        return 0;
    }

    @Override
    protected SerialCommand createCmd(SerialCommandReceiver receiver) {
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.put(motorId);
        bb.put((byte) (on? 0x1 : 0x0));
        bb.rewind();
        byte[] bytes = new byte[bb.remaining()];
        bb.get(bytes);
        return new SetMotorLedStateCmd(receiver, bytes);
    }

    @Override
    protected OpResult<Boolean> parseResult(SerialCmdResult cmdResult) {
        OpResult result = new OpResult<>();
        result.errorCode = cmdResult.getError();
        result.data = result.errorCode == SerialConstants.ERR_OK ;
        result.cmd = cmdResult.getCmd();
        return result;
    }
}
