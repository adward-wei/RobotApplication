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

package com.ubtechinc.alpha.cmds.chest;

import com.ubtechinc.alpha.cmds.SerialCommand;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;
import com.ubtechinc.alpha.serial.SerialConstants;

/**
 * @desc : 校准压力0点值
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/24
 * @modifier:
 * @modify_time:
 */
@Deprecated
public class AdjustPressZeroCmd extends SerialCommand {
    public AdjustPressZeroCmd(SerialCommandReceiver receiver, byte[] params) {
        super(receiver, params);
    }

    @Override
    public byte getCmdId() {
        return ChestCmds.CMD_ADJUST_PRESSZERO_VALUE;
    }

    @Override
    protected int getSerialType() {
        return SerialConstants.TYPE_CHEST;
    }
}
