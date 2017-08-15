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
 * @desc : 读取舵机版本
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/24
 * @modifier:
 * @modify_time:
 */

public class ReadMotorVersionCmd extends SerialCommand {

    public ReadMotorVersionCmd(SerialCommandReceiver receiver, byte[] params) {
        super(receiver, params);
    }

    @Override
    public byte getCmdId() {
        return ChestCmds.CMD_READ_MOTOR_VERSION;
    }

    @Override
    protected @SerialConstants.SerialType int getSerialType() {
        return SerialConstants.TYPE_CHEST;
    }
}
