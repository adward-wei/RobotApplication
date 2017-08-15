package com.ubtechinc.alpha.ops.sys;


import com.ubtechinc.alpha.cmds.SerialCommand;
import com.ubtechinc.alpha.cmds.chest.ShutdownSettingCmd;
import com.ubtechinc.alpha.ops.OpResult;
import com.ubtechinc.alpha.serial.SerialCmdResult;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;

/**
 * @desc : 关机模式设置
 * @author: wzt
 * @time : 2017/5/24
 * @modifier:
 * @modify_time:
 */

public class ShutdownSettingOp extends BaseSysOp {
    private byte[] param = new byte[1];

    public ShutdownSettingOp(byte param) {
        super(NOR_PRIORITY);
        this.param[0] = param;
    }

    @Override
    protected int waitTime() {
        return 0;
    }

    @Override
    protected SerialCommand createCmd(SerialCommandReceiver receiver) {
        return new ShutdownSettingCmd(receiver, param);
    }

    @Override
    protected OpResult parseResult(SerialCmdResult cmdResult) {
        OpResult<byte[]> result = new OpResult<>();
        result.errorCode = cmdResult.getError();
        result.cmd = cmdResult.getCmd();
        result.data = null;
        return result;
    }
}
