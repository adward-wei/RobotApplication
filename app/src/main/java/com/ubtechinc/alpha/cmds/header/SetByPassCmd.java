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

package com.ubtechinc.alpha.cmds.header;

import com.ubtechinc.alpha.cmds.SerialCommand;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;
import com.ubtechinc.alpha.serial.SerialConstants;

/**
 * @desc : 设置bypass
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/26
 * @modifier:
 * @modify_time:
 */

public class SetByPassCmd extends SerialCommand {

    public SetByPassCmd(SerialCommandReceiver receiver, byte[] param) {
        super(receiver, param);
    }

    @Override
    public byte getCmdId() {
        return HeadCmds.CMD_CONTROL_BYPASS;
    }

    @Override
    protected int getSerialType() {
        return SerialConstants.TYPE_HEADER;
    }
}
