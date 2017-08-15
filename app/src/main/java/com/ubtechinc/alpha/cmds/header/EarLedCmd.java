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
 * @desc : 耳朵／头灯命令： 在这里处理5mic/2mic逻辑
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/20
 * @modifier:
 * @modify_time:
 */
public class EarLedCmd extends HeadCmds {

    private boolean is5mic;

    public EarLedCmd(SerialCommandReceiver receiver, byte[] param) {
        super(receiver, param);
        is5mic = SysUtils.is5Mic();
    }

    @Override
    public void execute() {
        if (is5mic){
            ByteBuffer bb = ByteBuffer.wrap(param);
            bb.rewind();
            bb.limit(param.length);
            byte leftLed = bb.get();
            byte rightLed = bb.get();
            byte bright = bb.get();
            short onTime = bb.getShort();
            short offTime = bb.getShort();
            short nTotalTime = bb.getShort();
            LedControl.open();
            //在5mic上是设置头灯
            boolean ret = LedControl.ledSetHead(3, bright, rightLed, leftLed, onTime, offTime, nTotalTime, 0);
            LedControl.close();
            result = new SerialCmdResult(getCmdId(), ret? SerialConstants.ERR_OK: SerialConstants.ERR_IO);
        }else {
            super.execute();
        }
    }

    @Override
    public byte getCmdId() {
        return HeadCmds.CMD_OPEN_LED_EAR;
    }
}
