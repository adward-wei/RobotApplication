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

import java.nio.ByteBuffer;

/**
 * @desc : 眼睛灯命令
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/20
 * @modifier:
 * @modify_time:
 */

public class EyeLedCmd extends HeadCmds {
    private boolean is5Mic;

    public EyeLedCmd(SerialCommandReceiver receiver, byte[] param) {
        super(receiver, param);
        is5Mic = SysUtils.is5Mic();
    }

    @Override
    public void execute() {
        if (is5Mic){
            ByteBuffer bb = ByteBuffer.wrap(param);
            bb.rewind();
            bb.limit(param.length);
            byte leftLed = bb.get();
            byte rightLed = bb.get();
            byte bright = bb.get();
            byte color = bb.get();
            short onTime = bb.getShort();
            short offTime = bb.getShort();
            short totalTime = bb.getShort();
            LedControl.open();
            boolean ret = LedControl.ledSetEye(color, bright, rightLed, leftLed, onTime, offTime, totalTime, 0);
            LedControl.close();
            result = new SerialCmdResult(getCmdId(), /*ret?*/ SerialConstants.ERR_OK/*: SerialConstants.ERR_IO*/);
        }else {
            super.execute();
        }
    }

    @Override
    public byte getCmdId() {
        return HeadCmds.CMD_OPEN_LED_EYE;
    }
}
