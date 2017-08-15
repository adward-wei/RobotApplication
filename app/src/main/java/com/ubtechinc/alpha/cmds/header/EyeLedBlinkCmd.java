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

import com.ubtechinc.alpha.jni.LedControl;
import com.ubtechinc.alpha.serial.SerialCmdResult;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;
import com.ubtechinc.alpha.serial.SerialConstants;
import com.ubtechinc.alpha.utils.SysUtils;

/**
 * @desc : 眼睛灯：眨眼效果
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/20
 * @modifier:
 * @modify_time:
 */

public class EyeLedBlinkCmd extends HeadCmds {
    private boolean is5Mic;
    private final boolean blink;
    public EyeLedBlinkCmd(boolean enable, SerialCommandReceiver receiver, byte[] param) {
        super(receiver, param);
        is5Mic = SysUtils.is5Mic();
        this.blink = enable;
    }

    @Override
    public void execute() {
        if (is5Mic){
            LedControl.open();
            LedControl.ledSetOFF(0);
            final boolean ret = LedControl.ledSetOn(blink? 1 : 0);
            LedControl.close();
            result = new SerialCmdResult(getCmdId(), /*ret?*/ SerialConstants.ERR_OK/*: SerialConstants.ERR_IO*/);
        }else {
            super.execute();
        }
    }

    @Override
    public byte getCmdId() {
        return HeadCmds.CMD_EYE_LED_BLINK;
    }
}
