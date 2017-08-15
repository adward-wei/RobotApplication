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
import com.ubtechinc.alpha.cmds.chest.SetChargePlayingModeCmd;
import com.ubtechinc.alpha.ops.OpResult;
import com.ubtechinc.alpha.serial.SerialCmdResult;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;

/**
 * @desc : 设置边充边玩模式
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/24
 * @modifier:
 * @modify_time:
 */

public class SetChargePlayingModeOp extends BaseSysOp {
    private final boolean on;

    public SetChargePlayingModeOp(boolean on) {
        super(NOR_PRIORITY);
        this.on = on;
    }

    @Override
    protected int waitTime() {
        return 0;
    }

    @Override
    protected SerialCommand createCmd(SerialCommandReceiver receiver) {
        return new SetChargePlayingModeCmd(receiver,new byte[]{(byte) (on? 0x1: 0x0)});
    }

    @Override
    protected OpResult<Boolean> parseResult(SerialCmdResult cmdResult) {
        OpResult<Boolean> result = new OpResult();
        result.errorCode = cmdResult.getError();
        result.data = parseData(cmdResult.getResult());
        result.cmd = cmdResult.getCmd();
        return result;
    }

    private Boolean parseData(byte[] result) {
        if (result == null) return null;
        return result[0] == 1;//参考串口协议
    }
}
