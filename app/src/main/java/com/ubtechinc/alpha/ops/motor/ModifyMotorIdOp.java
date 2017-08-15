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
import com.ubtechinc.alpha.cmds.chest.ModifyMotorIdCmd;
import com.ubtechinc.alpha.ops.OpResult;
import com.ubtechinc.alpha.serial.SerialCmdResult;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;

import java.nio.ByteBuffer;

/**
 * @desc : 修改舵机id
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/24
 * @modifier:
 * @modify_time:
 */
@SuppressWarnings("该操作预留")
public class ModifyMotorIdOp extends BaseMotorOp{

    private final byte oldId;
    private final byte newId;

    public ModifyMotorIdOp(byte oldId, byte newId) {
        super(NOR_PRIORITY);
        this.oldId = oldId;
        this.newId = newId;
    }

    @Override
    protected int waitTime() {
        return 0;
    }

    @Override
    protected SerialCommand createCmd(SerialCommandReceiver receiver) {
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.put(oldId);
        bb.put(newId);
        bb.rewind();
        byte[] bytes = new byte[2];
        bb.get(bytes);
        ModifyMotorIdCmd cmd = new ModifyMotorIdCmd(receiver, bytes);
        return cmd;
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
