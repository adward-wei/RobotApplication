package com.ubtechinc.alpha.ops.led;

import com.ubtechinc.alpha.cmds.SerialCommand;
import com.ubtechinc.alpha.cmds.header.EyeLedBlinkCmd;
import com.ubtechinc.alpha.ops.OpResult;
import com.ubtechinc.alpha.serial.SerialCmdResult;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;

/**
 * @desc : 不可配置眨眼: enable 表示开始/结束眨眼
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/6/9
 * @modifier:
 * @modify_time:
 */

public class SetEyeBlinkOp extends BaseLedOp {
    private final boolean blink;
    public SetEyeBlinkOp(boolean enable) {
        super(NOR_PRIORITY);
        this.blink = enable;
    }

    @Override
    protected int waitTime() {
        return 0;
    }

    @Override
    protected SerialCommand createCmd(SerialCommandReceiver receiver) {
        return new EyeLedBlinkCmd(blink,receiver, new byte[]{blink?(byte)1:0, 0});
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
