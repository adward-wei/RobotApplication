package com.ubtechinc.alpha.cmds.header;

import android.util.Log;

import com.ubtechinc.alpha.cmds.SerialCommand;
import com.ubtechinc.alpha.jni.LedControl;
import com.ubtechinc.alpha.sdk.led.LedBright;
import com.ubtechinc.alpha.sdk.led.LedColor;
import com.ubtechinc.alpha.serial.SerialCmdResult;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;
import com.ubtechinc.alpha.serial.SerialConstants;
import com.ubtechinc.alpha.utils.SysUtils;

/**
 * @desc :
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/27
 * @modifier:
 * @modify_time:
 */

public class StopHeadLightCmd extends HeadCmds {
    public StopHeadLightCmd(SerialCommandReceiver receiver) {
        super(receiver, null);
    }

    @Override
    public void execute() {
        if (SysUtils.is5Mic()){
            LedControl.open();
            boolean ret = LedControl.ledSetHead(LedColor.BLACK.value, LedBright.ZERO.value,0xff,0xff,0, 0, 0, 0x07);
            Log.d("led", "set head led: " + ret);
            LedControl.close();
        }
        result = new SerialCmdResult(getCmdId(), SerialConstants.ERR_OK);
    }

    @Override
    public byte getCmdId() {
        return 0;
    }
}
