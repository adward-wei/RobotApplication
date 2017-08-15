package com.ubtechinc.alpha.ops.led;

import com.ubtechinc.alpha.cmds.SerialCommand;
import com.ubtechinc.alpha.cmds.header.SetEyeCmd;
import com.ubtechinc.alpha.ops.OpResult;
import com.ubtechinc.alpha.sdk.led.LedColor;
import com.ubtechinc.alpha.serial.SerialCmdResult;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;

/**
 * @desc : 设置眼睛灯常亮
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/12
 * @modifier:
 * @modify_time:
 */

public final class SetEyeLightOp extends BaseLedOp {
    private final int color;
    public SetEyeLightOp(LedColor color) {
        super(NOR_PRIORITY);
        //5 ~ 11
        this.color = color.value + 4;
    }

    @Override
    protected int waitTime() {
        return 0;
    }

    @Override
    protected SerialCommand createCmd(SerialCommandReceiver receiver) {
        return new SetEyeCmd(receiver, color);
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
