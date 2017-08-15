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
import com.ubtechinc.alpha.utils.SysUtils;

/**
 * @desc : 恢复被暂停的灯控指令
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/20
 * @modifier:
 * @modify_time:
 */
@Deprecated
public class ResumeLedCmd extends HeadCmds {

    public ResumeLedCmd(SerialCommandReceiver receiver, byte[] param) {
        super(receiver, param);
    }

    @Override
    public void execute() {
        if (SysUtils.is5Mic()){

        }else {
            super.execute();
        }
    }

    @Override
    public byte getCmdId() {
        return HeadCmds.CMD_RESUME;
    }
}
