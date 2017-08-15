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
import com.ubtechinc.alpha.cmds.chest.ChestStartupCmd;
import com.ubtechinc.alpha.cmds.header.HeadStartupCmd;
import com.ubtechinc.alpha.ops.OpResult;
import com.ubtechinc.alpha.serial.SerialCmdResult;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;
import com.ubtechinc.alpha.serial.SerialConstants;

/**
 * @desc : 开机操作：胸部开机、头部开机
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/24
 * @modifier:
 * @modify_time:
 */

public class StartUpOps extends BaseSysOp {
    public StartUpOps() {
        super(MAX_PRIORITY);
    }

    @Override
    protected int waitTime() {
        return 0;
    }

    @Override
    protected SerialCommand createCmd(SerialCommandReceiver receiver) {
        ChestStartupCmd cmd = new ChestStartupCmd(receiver, null);
        cmd.execute();
        if (cmd.getResult().getError() != SerialConstants.ERR_OK)
            return null;
        return new HeadStartupCmd(receiver, null);
    }

    @Override
    protected OpResult parseResult(SerialCmdResult cmdResult) {
        OpResult result = new OpResult<>();
        result.errorCode = cmdResult == null ? SerialConstants.ERR_UNKNOW : cmdResult.getError();
        result.data = null;
        result.cmd = cmdResult == null ? -1 : cmdResult.getCmd();
        return result;
    }
}
