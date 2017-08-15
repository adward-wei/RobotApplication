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
 * @time : 2017/7/12
 * @modifier:
 * @modify_time:
 */

public class SetEyeCmd extends HeadCmds {
    private final int color;
    public SetEyeCmd(SerialCommandReceiver receiver, int color) {
        super(receiver, null);
        this.color = color;
    }

    @Override
    public void execute() {
        if (SysUtils.is5Mic()){
            LedControl.open();
            boolean ret = LedControl.ledSetOn(color);
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
