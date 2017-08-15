package com.ubtechinc.alpha.ops.led;

import com.ubtechinc.alpha.cmds.SerialCommand;
import com.ubtechinc.alpha.cmds.header.SetHeadBreathCmd;
import com.ubtechinc.alpha.ops.OpResult;
import com.ubtechinc.alpha.sdk.led.LedBright;
import com.ubtechinc.alpha.sdk.led.LedColor;
import com.ubtechinc.alpha.serial.SerialCmdResult;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;

/**
 * @desc : 设置头灯呼吸
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/11
 * @modifier:
 * @modify_time:
 */

public class SetHeadBreathOp extends BaseLedOp {
    private final int color;
    private final int bright;
    private final int rled;
    private final int lled;
    private final int on;
    private final int total;

    public SetHeadBreathOp(LedColor color, LedBright bright, int number, int on, int total) {
        super(NOR_PRIORITY);
        this.color = color.value;
        this.bright = bright.value;
        this.rled = number;
        this.lled = number;
        this.on = on;
        this.total = total;
    }

    public SetHeadBreathOp(LedColor color1, LedColor color2, LedBright bright, int number, int on, int total){
        super(NOR_PRIORITY);
        this.color = (color1.value << 8)| color2.value;
        this.bright = bright.value;
        this.rled = number;
        this.lled = number;
        this.on = on;
        this.total = total;
    }

    @Override
    protected int waitTime() {
        return 0;
    }

    @Override
    protected SerialCommand createCmd(SerialCommandReceiver receiver) {
        return new SetHeadBreathCmd(receiver, color, bright, rled, lled, on, total);
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
