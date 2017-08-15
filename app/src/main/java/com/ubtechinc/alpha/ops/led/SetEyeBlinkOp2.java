package com.ubtechinc.alpha.ops.led;

import com.ubtechinc.alpha.cmds.SerialCommand;
import com.ubtechinc.alpha.cmds.header.EyeLedBlinkCmd2;
import com.ubtechinc.alpha.ops.OpResult;
import com.ubtechinc.alpha.sdk.led.LedBright;
import com.ubtechinc.alpha.sdk.led.LedColor;
import com.ubtechinc.alpha.serial.SerialCmdResult;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;

/**
 * @desc : 可配置的眨眼
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/12
 * @modifier:
 * @modify_time:
 */

public class SetEyeBlinkOp2 extends BaseLedOp {
    private final int color;
    private final int bright;
    private final int reye;
    private final int leye;
    private final int on;
    private final int off;
    private final int total;
    public SetEyeBlinkOp2(LedColor color, LedBright bright, int number, int on, int off, int total) {
        super(NOR_PRIORITY);
        this.color = color.value;
        this.bright = bright.value;
        this.reye = number;
        this.leye = number;
        this.on = on;
        this.off = off;
        this.total = total;
    }

    @Override
    protected int waitTime() {
        return 0;
    }

    @Override
    protected SerialCommand createCmd(SerialCommandReceiver receiver) {
        return new EyeLedBlinkCmd2(receiver, color, bright, reye, leye, on, off, total);
    }

    @Override
    protected OpResult parseResult(SerialCmdResult cmdResult) {
        OpResult result = new OpResult<>();
        result.errorCode = cmdResult.getError();
        result.data = null;
        result.cmd = cmdResult.getCmd();
        return result;
    }
}
