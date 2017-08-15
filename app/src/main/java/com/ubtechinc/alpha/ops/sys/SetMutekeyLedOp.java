package com.ubtechinc.alpha.ops.sys;

import com.ubtechinc.alpha.cmds.SerialCommand;
import com.ubtechinc.alpha.cmds.chest.SetMutekeyLedCmd;
import com.ubtechinc.alpha.ops.OpResult;
import com.ubtechinc.alpha.serial.SerialCmdResult;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;

/**
 * Created by Administrator on 2017/7/11 0011.
 */

public class SetMutekeyLedOp extends BaseSysOp {
    private byte ledState;
    public SetMutekeyLedOp(byte ledState){
        super(NOR_PRIORITY);
        this.ledState=ledState;
    }
    @Override
    protected int waitTime() {
        return 0;
    }

    @Override
    protected SerialCommand createCmd(SerialCommandReceiver receiver) {
        return new SetMutekeyLedCmd(receiver,new byte[]{ledState});
    }

    @Override
    protected OpResult parseResult(SerialCmdResult cmdResult) {
        return null;
    }
}
