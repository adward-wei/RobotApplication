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
 * @desc : 耳朵灯跑马效果
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/20
 * @modifier:
 * @modify_time:
 */

public class EarLedHorseCmd extends HeadCmds {
    private boolean is5Mic;

    public EarLedHorseCmd(SerialCommandReceiver receiver, byte[] param) {
        super(receiver, param);
        is5Mic = SysUtils.is5Mic();
    }

    @Override
    public void execute() {
        if (is5Mic){

        }else {
            //super.execute();
        }
    }

    @Override
    public byte getCmdId() {
        return HeadCmds.CMD_EAR_LED_BREATH;
    }
}
