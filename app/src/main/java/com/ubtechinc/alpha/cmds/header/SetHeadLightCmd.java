package com.ubtechinc.alpha.cmds.header;

import android.util.Log;

import com.ubtechinc.alpha.jni.LedControl;
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

public class SetHeadLightCmd extends HeadCmds {
    private final int bright;
    private final int color;

    public SetHeadLightCmd(SerialCommandReceiver receiver, int color, int bright) {
        super(receiver, null);
        this.color = color;
        this.bright = bright;
    }

    @Override
    public void execute() {
        if (SysUtils.is5Mic()){
            LedControl.open();
            boolean ret = LedControl.ledSetHead(color,bright,0xff,0xff,Integer.MAX_VALUE, 0, Integer.MAX_VALUE, 0x07);
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
