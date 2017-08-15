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
 * @desc : 写入升级数据？
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/20
 * @modifier:
 * @modify_time:
 */

public class HeadUpgradePageCmd extends HeadCmds {

    public HeadUpgradePageCmd(SerialCommandReceiver receiver, byte[] param) {
        super(receiver, param);
    }

    @Override
    public byte getCmdId() {
        return HeadCmds.CMD_UPGRADE_PAGE;
    }
}
