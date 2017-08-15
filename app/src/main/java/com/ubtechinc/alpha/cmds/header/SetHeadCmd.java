package com.ubtechinc.alpha.cmds.header;

import android.util.Log;

import com.ubtechinc.alpha.cmds.SerialCommand;
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

public class SetHeadCmd extends HeadCmds {
    private final int color;
    private final int bright;
    private final int rled;
    private final int lled;
    private final int on;
    private final int off;
    private final int total;
    public SetHeadCmd(SerialCommandReceiver receiver, int color, int bright, int rled, int lled, int on, int off, int total) {
        super(receiver, null);
        this.color = color;
        this.bright = bright;
        this.rled = rled;
        this.lled = lled;
        this.on = on;
        this.off = off;
        this.total = total;
    }

    @Override
    public void execute() {
        if (SysUtils.is5Mic()){
            LedControl.open();
            boolean ret = LedControl.ledSetHead(color,bright,rled,lled ,on, off, total, 0);
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
