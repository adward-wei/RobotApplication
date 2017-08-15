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
import com.ubtechinc.alpha.cmds.chest.ChestShutDownCmd;
import com.ubtechinc.alpha.cmds.header.HeadShutdownCmd;
import com.ubtechinc.alpha.ops.OpResult;
import com.ubtechinc.alpha.serial.SerialCmdResult;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;
import com.ubtechinc.alpha.serial.SerialConstants;

/**
 * @desc : 关机操作:胸部和头部
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/24
 * @modifier:
 * @modify_time:
 */

public class ShutdownOps extends BaseSysOp {

    public ShutdownOps(){
        super(MAX_PRIORITY);
    }

    @Override
    protected int waitTime() {
        return 0;
    }

    @Override
    protected SerialCommand createCmd(SerialCommandReceiver receiver) {
        ChestShutDownCmd cmd = new ChestShutDownCmd(receiver, new byte[]{0x0});
        cmd.execute();
        if (cmd.getResult().getError() != SerialConstants.ERR_OK)
            return null;
        return new HeadShutdownCmd(receiver, null);
    }

    @Override
    protected OpResult parseResult(SerialCmdResult cmdResult) {
        OpResult result = new OpResult<>();
        result.errorCode = cmdResult== null? SerialConstants.ERR_UNKNOW :cmdResult.getError();
        result.data = null;
        result.cmd = cmdResult == null ? -1 : cmdResult.getCmd();
        return result;
    }
}
