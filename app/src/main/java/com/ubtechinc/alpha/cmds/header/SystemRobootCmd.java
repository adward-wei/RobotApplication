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

import com.ubtechinc.alpha.serial.SerialCommandReceiver;

/**
 * @desc : 系统更新？
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/20
 * @modifier:
 * @modify_time:
 */
@Deprecated
public class SystemRobootCmd extends HeadCmds {

    public SystemRobootCmd(SerialCommandReceiver receiver, byte[] param) {
        super(receiver, param);
    }

    @Override
    public byte getCmdId() {
        return HeadCmds.CMD_SYSTEM_REBOOT;
    }
}
