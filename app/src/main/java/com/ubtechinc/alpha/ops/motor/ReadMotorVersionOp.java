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
import com.ubtechinc.alpha.cmds.chest.ReadMotorVersionCmd;
import com.ubtechinc.alpha.ops.OpResult;
import com.ubtechinc.alpha.ops.sys.BaseSysOp;
import com.ubtechinc.alpha.serial.SerialCmdResult;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;
import com.ubtechinc.alpha.serial.SerialConstants;

/**
 * @desc : 读取舵机版本
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/24
 * @modifier:
 * @modify_time:
 */

public class ReadMotorVersionOp extends BaseSysOp {
    private final byte motorId;

    public ReadMotorVersionOp(byte motorId) {
        super(NOR_PRIORITY);
        this.motorId = motorId;
    }

    @Override
    protected int waitTime() {
        return 0;
    }

    @Override
    protected SerialCommand createCmd(SerialCommandReceiver receiver) {
        return new ReadMotorVersionCmd(receiver, new byte[]{motorId});
    }

    @Override
    protected OpResult<String> parseResult(SerialCmdResult cmdResult) {
        OpResult<String> result = new OpResult<>();
        result.errorCode = cmdResult == null ? SerialConstants.ERR_UNKNOW :cmdResult.getError();
        result.cmd = cmdResult == null ? -1: cmdResult.getCmd();
        result.data = result.errorCode == SerialConstants.ERR_OK? parseVersion(cmdResult.getResult()): "";
        return result;
    }

    private final String parseVersion(byte[] param){
        //param包含:错误码，参数长度，参数数据
        if (param.length < 2 || param[1] > param.length) return "";
        byte[] versionData = new byte[param.length - 2];
        System.arraycopy(param, 2, versionData, 0, param.length - 2);
        return new String(versionData);
    }
}
