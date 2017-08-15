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

import com.ubtechinc.alpha.serial.SerialCommandReceiver;

/**
 * @desc : 停止舵机
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/19
 * @modifier:
 * @modify_time:
 */
@Deprecated
public class StopMotorCmd extends ChestCmds {

    public StopMotorCmd(SerialCommandReceiver receiver) {
        super(receiver, null);
    }

    @Override
    public byte getCmdId() {
        return ChestCmds.CMD_STOP_MOTOR;
    }
}
