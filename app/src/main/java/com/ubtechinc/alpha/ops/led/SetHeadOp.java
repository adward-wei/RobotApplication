package com.ubtechinc.alpha.ops.led;

import com.ubtechinc.alpha.cmds.SerialCommand;
import com.ubtechinc.alpha.cmds.header.SetHeadCmd;
import com.ubtechinc.alpha.ops.OpResult;
import com.ubtechinc.alpha.sdk.led.LedBright;
import com.ubtechinc.alpha.sdk.led.LedColor;
import com.ubtechinc.alpha.serial.SerialCmdResult;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;

/**
 * @desc : 设置头灯
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/11
 * @modifier:
 * @modify_time:
 */

public class SetHeadOp extends BaseLedOp {
    private final int color;
    private final int bright;
    private final int lled;
    private final int rled;
    private final int on;
    private final int off;
    private final int total;
    public SetHeadOp(LedColor color, LedBright bright, int number, int on, int off, int total) {
        super(NOR_PRIORITY);
        this.color = color.value;
        this.bright = bright.value;
        this.lled = number;
        this.rled = number;
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
        return new SetHeadCmd(receiver, color, bright, rled,lled, on, off, total);
    }

    @Override
    protected OpResult parseResult(SerialCmdResult cmdResult) {
        return null;
    }
}
