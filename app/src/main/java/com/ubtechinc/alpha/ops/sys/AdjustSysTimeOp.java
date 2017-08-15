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
import com.ubtechinc.alpha.cmds.chest.AdjustTimeCmd;
import com.ubtechinc.alpha.ops.OpResult;
import com.ubtechinc.alpha.serial.SerialCmdResult;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;

import java.nio.ByteBuffer;
import java.util.Calendar;

/**
 * @desc : 校准系统时间
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/24
 * @modifier:
 * @modify_time:
 */

public class AdjustSysTimeOp extends BaseSysOp {
    private final Calendar time;

    public AdjustSysTimeOp(Calendar time) {
        super(NOR_PRIORITY);
        this.time = time;
    }

    @Override
    protected int waitTime() {
        return 0;
    }

    @Override
    protected SerialCommand createCmd(SerialCommandReceiver receiver) {
        ByteBuffer bb = ByteBuffer.allocate(7);
        bb.put((byte) (time.get(Calendar.YEAR)%100));
        bb.put((byte) time.get(Calendar.MONTH));
        bb.put((byte) time.get(Calendar.DAY_OF_MONTH));
        bb.put((byte) time.get(Calendar.DAY_OF_WEEK));
        bb.put((byte) time.get(Calendar.HOUR_OF_DAY));
        bb.put((byte) time.get(Calendar.MINUTE));
        bb.put((byte) time.get(Calendar.SECOND));
        bb.rewind();
        byte[] bytes = new byte[bb.remaining()];
        bb.get(bytes);
        return new AdjustTimeCmd(receiver, bytes);
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
