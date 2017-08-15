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
import com.ubtechinc.alpha.cmds.chest.SaveAlarmCmd;
import com.ubtechinc.alpha.ops.OpResult;
import com.ubtechinc.alpha.ops.sys.BaseSysOp;
import com.ubtechinc.alpha.serial.SerialCmdResult;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;

import java.nio.ByteBuffer;
import java.util.Calendar;

/**
 * @desc : 设置闹钟
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/24
 * @modifier:
 * @modify_time:
 */

public class SetAlarmOp extends BaseSysOp {

    private final boolean end;
    private final boolean isHold;//是否保持开机
    private final byte mode;//0--不重复， 2--天重复 3--周重复
    private final Calendar date;

    public SetAlarmOp(boolean end, boolean isHold, byte mode, Calendar date) {
        super(NOR_PRIORITY);
        this.end = end;
        this.isHold = isHold;
        this.mode = mode;
        this.date = date;
    }

    @Override
    protected int waitTime() {
        return 0;
    }

    @Override
    protected SerialCommand createCmd(SerialCommandReceiver receiver) {
        ByteBuffer bb = ByteBuffer.allocate(10);
        bb.put((byte) (end? 0x1 : 0x0));
        bb.put((byte) (isHold ? 0x01: 0x0));
        bb.put(mode);
        bb.put((byte) (date.get(Calendar.YEAR)%100));
        bb.put((byte) date.get(Calendar.MONTH));
        bb.put((byte) date.get(Calendar.DAY_OF_MONTH));
        bb.put((byte) date.get(Calendar.DAY_OF_WEEK));
        bb.put((byte) date.get(Calendar.HOUR_OF_DAY));
        bb.put((byte) date.get(Calendar.MINUTE));
        bb.put((byte) date.get(Calendar.SECOND));
        bb.rewind();
        byte[] bytes = new byte[bb.remaining()];
        bb.get(bytes);
        return new SaveAlarmCmd(receiver, bytes);
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
