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
 * @desc : 写入序列号
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/20
 * @modifier:
 * @modify_time:
 */

public class WriteSidCmd extends ChestCmds {

    public WriteSidCmd(SerialCommandReceiver receiver, byte[] param) {
        super(receiver, param);
    }

    @Override
    public byte getCmdId() {
        return ChestCmds.CMD_WRITE_EEPROM;
    }
}
