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

package com.ubtechinc.alpha.ops.alarm;

import com.ubtechinc.alpha.cmds.SerialCommand;
import com.ubtechinc.alpha.cmds.chest.EndAlarmCmd;
import com.ubtechinc.alpha.ops.OpResult;
import com.ubtechinc.alpha.ops.sys.BaseSysOp;
import com.ubtechinc.alpha.serial.SerialCmdResult;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;

import java.nio.ByteBuffer;
import java.util.Calendar;

/**
 * @desc : 停止闹钟操作
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/24
 * @modifier:
 * @modify_time:
 */

public class EndAlarmOp extends BaseSysOp {
    private final Calendar date;

    public EndAlarmOp(Calendar date) {
        super(NOR_PRIORITY);
        this.date = date;
    }

    @Override
    protected int waitTime() {
        return 0;
    }

    @Override
    protected SerialCommand createCmd(SerialCommandReceiver receiver) {
        ByteBuffer bb = ByteBuffer.allocate(7);
        bb.put((byte) (date.get(Calendar.YEAR)%100));
        bb.put((byte) date.get(Calendar.MONTH));
        bb.put((byte) date.get(Calendar.DAY_OF_MONTH));
        bb.put((byte) date.get(Calendar.DAY_OF_WEEK));
        bb.put((byte) date.get(Calendar.HOUR_OF_DAY));
        bb.put((byte) date.get(Calendar.MINUTE));
        bb.put((byte) date.get(Calendar.SECOND));
        bb.rewind();
        byte[] bytes = new byte[7];
        bb.get(bytes);
        return new EndAlarmCmd(receiver, bytes);
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
