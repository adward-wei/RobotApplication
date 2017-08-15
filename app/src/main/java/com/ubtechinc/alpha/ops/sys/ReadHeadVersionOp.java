package com.ubtechinc.alpha.ops.sys;

import com.ubtechinc.alpha.cmds.SerialCommand;
import com.ubtechinc.alpha.cmds.header.ReadVersionCmd;
import com.ubtechinc.alpha.ops.OpResult;
import com.ubtechinc.alpha.serial.SerialCmdResult;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;

/**
 * @desc : 读取头板版本号
 * @author: wzt
 * @time : 2017/5/24
 * @modifier:
 * @modify_time:
 */

public class ReadHeadVersionOp extends BaseSysOp {

    public ReadHeadVersionOp() {
        super(NOR_PRIORITY);
    }

    @Override
    protected int waitTime() {
        return 0;
    }

    @Override
    protected SerialCommand createCmd(SerialCommandReceiver receiver) {
        return new ReadVersionCmd(receiver, new byte[]{0x0});
    }

    @Override
    protected OpResult parseResult(SerialCmdResult cmdResult) {
        OpResult<byte[]> result = new OpResult<>();
        result.errorCode = cmdResult.getError();
        result.cmd = cmdResult.getCmd();
        result.data = cmdResult.getResult();
        return result;
    }
}
