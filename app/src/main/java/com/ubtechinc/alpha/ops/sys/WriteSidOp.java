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

package com.ubtechinc.alpha.ops.sys;

import com.ubtechinc.alpha.cmds.SerialCommand;
import com.ubtechinc.alpha.cmds.chest.WriteSidCmd;
import com.ubtechinc.alpha.ops.OpResult;
import com.ubtechinc.alpha.serial.SerialCmdResult;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;

import java.nio.ByteBuffer;

/**
 * @desc : 写入eeprom
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/24
 * @modifier:
 * @modify_time:
 */

public class WriteSidOp extends BaseSysOp {
    private final byte[] data;

    public WriteSidOp(byte[] data) {
        super(NOR_PRIORITY);
        this.data = data;
    }

    @Override
    protected int waitTime() {
        return 0;
    }

    @Override
    protected SerialCommand createCmd(SerialCommandReceiver receiver) {
        ByteBuffer bb = ByteBuffer.allocate(data.length + 2);
        bb.put((byte) data.length);
        bb.put(data);
        bb.put("y".getBytes()[0]);
        bb.rewind();
        byte[] bytes = new byte[bb.remaining()];
        bb.get(bytes);
        return new WriteSidCmd(receiver, bytes);
    }

    @Override
    protected OpResult parseResult(SerialCmdResult cmdResult) {
        OpResult result = new OpResult();
        result.errorCode = cmdResult.getError();
        result.data = null;
        result.cmd = cmdResult.getCmd();
        return result;
    }
}
