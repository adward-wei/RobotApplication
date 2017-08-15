package com.ubtechinc.alpha.cmds.header;

import android.util.Log;

import com.ubtechinc.alpha.jni.LedControl;
import com.ubtechinc.alpha.sdk.led.LedColor;
import com.ubtechinc.alpha.serial.SerialCmdResult;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;
import com.ubtechinc.alpha.serial.SerialConstants;
import com.ubtechinc.alpha.utils.SysUtils;

/**
 * @desc :
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/11
 * @modifier:
 * @modify_time:
 */

public class SetEyeFlashCmd extends HeadCmds {
    private final int color;
    private final int bright;
    private final int reye;
    private final int leye;
    private final int on;
    private final int total;
    public SetEyeFlashCmd(SerialCommandReceiver receiver, int color, int bright, int reye, int leye, int on, int total) {
        super(receiver, null);
        this.color = color;
        this.bright = bright;
        this.reye = reye;
        this.leye = leye;
        this.on = on;
        this.total = total;
    }

    @Override
    public void execute() {
        if (SysUtils.is5Mic()){
            LedControl.open();
            boolean ret = LedControl.ledSetEye(color,bright,reye,leye,on,(color > LedColor.BLUE.value?  0: 1000),total, (color > LedColor.BLUE.value? 0x03: 0x00));
            Log.d("led", "set eye led: " + ret);
            LedControl.close();
        }
        result = new SerialCmdResult(getCmdId(), SerialConstants.ERR_OK);
    }

    @Override
    public byte getCmdId() {
        return 0;
    }
}
