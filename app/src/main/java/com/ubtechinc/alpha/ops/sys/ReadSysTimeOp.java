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
import com.ubtechinc.alpha.cmds.chest.ReadTimeCmd;
import com.ubtechinc.alpha.ops.OpResult;
import com.ubtechinc.alpha.serial.SerialCmdResult;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;

import java.util.Calendar;

/**
 * @desc : 读取系统时间
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/24
 * @modifier:
 * @modify_time:
 */

public class ReadSysTimeOp extends BaseSysOp {

    public ReadSysTimeOp() {
        super(NOR_PRIORITY);
    }

    @Override
    protected int waitTime() {
        return 0;
    }

    @Override
    protected SerialCommand createCmd(SerialCommandReceiver receiver) {
        return new ReadTimeCmd(receiver, null);
    }

    @Override
    protected OpResult<Calendar> parseResult(SerialCmdResult cmdResult) {
        OpResult<Calendar> result = new OpResult<>();
        result.errorCode = cmdResult.getError();
        result.data = parseData(cmdResult.getResult());
        result.cmd = cmdResult.getCmd();
        return result;
    }

    private Calendar parseData(byte[] result) {
        if (result== null || result.length != 7) return null;
        // FIXME: 2017/4/25 代码参考原主服务
        int year = 2000 + result[0];
        int month = result[1];
        int day = result[2];
        int week = result[3];
        int hour = result[4];
        int minute = result[5];
        int second = result[6];
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.WEEK_OF_MONTH, week);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        return calendar;
    }
}
