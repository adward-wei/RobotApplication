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
 * @desc : 蹲下命令
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/20
 * @modifier:
 * @modify_time:
 */
public class SquatCmd extends ChestCmds {
    public SquatCmd(SerialCommandReceiver receiver, byte[] param) {
        super(receiver, param);
    }

    @Override
    public byte getCmdId() {
        return ChestCmds.CMD_SQUAT;
    }
}
