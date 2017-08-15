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
import com.ubtechinc.alpha.cmds.chest.AdjustPressZeroCmd;
import com.ubtechinc.alpha.ops.OpResult;
import com.ubtechinc.alpha.serial.SerialCmdResult;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;

/**
 * @desc : 校准压力0点值
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/24
 * @modifier:
 * @modify_time:
 */
@Deprecated
public class AdjustPressZeroOp extends BaseSysOp {
    private final boolean isRight;

    public AdjustPressZeroOp(boolean isRight) {
        super(NOR_PRIORITY);
        this.isRight = isRight;
    }

    @Override
    protected int waitTime() {
        return 0;
    }

    @Override
    protected SerialCommand createCmd(SerialCommandReceiver receiver) {
        return new AdjustPressZeroCmd(receiver, new byte[]{(byte) (isRight ? 0x1: 0x0)});
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
