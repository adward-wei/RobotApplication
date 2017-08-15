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
 * @desc : 读取舵机角度:分掉电回读和不掉掉回读
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/19
 * @modifier:
 * @modify_time:
 */

public class ReadMotorAngleCmd extends ChestCmds {
    private final boolean acdump;
    public ReadMotorAngleCmd(SerialCommandReceiver receiver , byte[] param , boolean acdump) {
        super(receiver, param);
        this.acdump =acdump;
    }

    @Override
    public byte getCmdId() {
        if (acdump) {
            return ChestCmds.CMD_READ_MOTOR_ANGLE;
        }else {
            return ChestCmds.CMD_READ_BACK_MOTOR_ANGLE;
        }
    }

}
