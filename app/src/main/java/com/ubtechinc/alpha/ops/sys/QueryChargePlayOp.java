package com.ubtechinc.alpha.ops.sys;

import com.ubtechinc.alpha.cmds.SerialCommand;
import com.ubtechinc.alpha.cmds.chest.SetChargePlayingModeCmd;
import com.ubtechinc.alpha.ops.OpResult;
import com.ubtechinc.alpha.serial.SerialCmdResult;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;

/**
 * @desc : 设置边充边玩模式
 * @author: wzt
 * @time : 2017/6/6
 * @modifier:
 * @modify_time:
 */

public class QueryChargePlayOp extends BaseSysOp {
    public QueryChargePlayOp() {
        super(NOR_PRIORITY);
    }

    @Override
    protected int waitTime() {
        return 0;
    }

    @Override
    protected SerialCommand createCmd(SerialCommandReceiver receiver) {
        return new SetChargePlayingModeCmd(receiver,new byte[]{(byte)(0x02)});
    }

    @Override
    protected OpResult<Boolean> parseResult(SerialCmdResult cmdResult) {
        OpResult<Boolean> result = new OpResult();
        result.errorCode = cmdResult.getError();
        result.data = parseData(cmdResult.getResult());
        result.cmd = cmdResult.getCmd();
        return result;
    }

    private Boolean parseData(byte[] result) {
        if (result == null) return null;
        return result[1] == 1;//参考串口协议
    }
}
