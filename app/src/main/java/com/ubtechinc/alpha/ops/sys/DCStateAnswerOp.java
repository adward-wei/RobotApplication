package com.ubtechinc.alpha.ops.sys;

import com.ubtechinc.alpha.cmds.SerialCommand;
import com.ubtechinc.alpha.cmds.chest.DCStateCmd;
import com.ubtechinc.alpha.ops.OpResult;
import com.ubtechinc.alpha.serial.SerialCmdResult;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;

/**
 * @desc : 收到机器人充电状态的信息后给出应答
 * @author: wzt
 * @time : 2017/5/17
 * @modifier:
 * @modify_time:
 */

public class DCStateAnswerOp extends BaseSysOp {

    public DCStateAnswerOp() {
        super(NOR_PRIORITY);
    }

    @Override
    protected int waitTime() {
        return 0;
    }

    @Override
    protected SerialCommand createCmd(SerialCommandReceiver receiver) {
        return new DCStateCmd(receiver, new byte[]{0x0});
    }

    @Override
    protected OpResult parseResult(SerialCmdResult cmdResult) {
        OpResult<String> result = new OpResult<>();
        result.errorCode = cmdResult.getError();
        result.cmd = cmdResult.getCmd();
        result.data = null;
        return result;
    }
}
