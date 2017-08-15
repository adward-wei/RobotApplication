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

package com.ubtechinc.alpha.ops.net;

import com.ubtechinc.alpha.cmds.SerialCommand;
import com.ubtechinc.alpha.cmds.header.HeadSetBtStatusCmd;
import com.ubtechinc.alpha.ops.OpResult;
import com.ubtechinc.alpha.ops.sys.BaseSysOp;
import com.ubtechinc.alpha.serial.SerialCmdResult;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;

/**
 * @desc : 设置蓝牙状态
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/24
 * @modifier:
 * @modify_time:
 */
public class SetBtStateOp extends BaseSysOp {
    private final boolean on;

    public SetBtStateOp(boolean on) {
        super(NOR_PRIORITY);
        this.on = on;
    }

    @Override
    protected int waitTime() {
        return 0;
    }

    @Override
    protected SerialCommand createCmd(SerialCommandReceiver receiver) {
        HeadSetBtStatusCmd cmd = new HeadSetBtStatusCmd(receiver, new byte[]{(byte) (on? 0x1 : 0x0)});
//        cmd.execute();
//        if (cmd.getResult().getError() != SerialConstants.ERR_OK){
//            return null;
//        }
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
