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
 * @desc : 设置舵机校准角度
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/24
 * @modifier:
 * @modify_time:
 */

public class SetAdjustAngleCmd extends SerialCommand {
    public SetAdjustAngleCmd(SerialCommandReceiver mCmdReceiver, byte[] params) {
        super(mCmdReceiver, params);
    }

    @Override
    public byte getCmdId() {
        return ChestCmds.CMD_SET_ADJUST_ANGLE;
    }

    @Override
    protected int getSerialType() {
        return SerialConstants.TYPE_CHEST;
    }
}
