package com.ubtechinc.alpha.cmds.header;

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

public class EyeLedBlinkCmd2 extends HeadCmds {
    private final int color;
    private final int bright;
    private final int reye;
    private final int leye;
    private final int on;
    private final int off;
    private final int total;
    public EyeLedBlinkCmd2(SerialCommandReceiver receiver, int color, int bright, int reye, int leye, int on, int off, int total) {
        super(receiver, null);
        this.color = color;
        this.bright = bright;
        this.reye = reye;
        this.leye = leye;
        this.on = on;
        this.off = off;
        this.total = total;
    }

    @Override
    public void execute() {
        if (SysUtils.is5Mic()){
            LedControl.open();
            LedControl.ledSetEye(color,bright,reye,leye ,on, off, total, 0);
            LedControl.close();
        }
        result = new SerialCmdResult(getCmdId(), SerialConstants.ERR_OK);
    }

    @Override
    public byte getCmdId() {
        return 0;
    }
}
