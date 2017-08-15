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
import com.ubtechinc.alpha.cmds.chest.ReadAlarmCmd;
import com.ubtechinc.alpha.ops.OpResult;
import com.ubtechinc.alpha.ops.sys.BaseSysOp;
import com.ubtechinc.alpha.serial.SerialCmdResult;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;

/**
 * @desc : 读取闹钟操作
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/24
 * @modifier:
 * @modify_time:
 */

public class ReadAlarmOp extends BaseSysOp {
    public ReadAlarmOp() {
        super(NOR_PRIORITY);
    }

    @Override
    protected int waitTime() {
        return 0;
    }

    @Override
    protected SerialCommand createCmd(SerialCommandReceiver receiver) {
        return new ReadAlarmCmd(receiver, null);
    }

    @Override
    protected OpResult<AlarmInfo> parseResult(SerialCmdResult cmdResult) {
        OpResult<AlarmInfo> result = new OpResult<>();
        result.errorCode = cmdResult.getError();
        result.data = parseData(cmdResult.getResult());
        result.cmd = cmdResult.getCmd();
        return result;
    }

    private AlarmInfo parseData(byte[] result) {
        AlarmInfo info = new AlarmInfo();
        info.state = result[0];
        if (info.state != 2) {
            info.acitonEndName = result[1] + "";
            info.repeat = result[2];
            info.yy = result[3];
            info.mo = result[4];
            info.day = result[5];
            info.date = result[6];
            info.hh = result[7];
            info.mm = result[8];
        }
        return info;
    }
}
