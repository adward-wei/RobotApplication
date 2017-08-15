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
import com.ubtechinc.alpha.serial.SerialConstants;

/**
 * @desc : 向头部透传数据
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/25
 * @modifier:
 * @modify_time:
 */

public class HeadTransparentCmd extends HeadCmds {
    public HeadTransparentCmd(SerialCommandReceiver receiver, byte[] data) {
        super(receiver,data);
    }

    @Override
    public byte getCmdId() {
        return HeadCmds.HEADER_SEND_TRANSFORM;
    }

    @Override
    protected int getSerialType() {
        return SerialConstants.TYPE_HEADER;
    }
}
