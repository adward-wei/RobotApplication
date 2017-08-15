package com.ubtechinc.alpha.cmds.chest;

import com.ubtechinc.alpha.serial.SerialCommandReceiver;

/**
 * Created by Administrator on 2017/7/11 0011.
 */

public class SetMutekeyLedCmd extends ChestCmds {

    public SetMutekeyLedCmd(SerialCommandReceiver receiver, byte[] param){
        super(receiver,param);
    }

    @Override
    public byte getCmdId() {
        return ChestCmds.CMD_SET_MUTE_KEY;
    }
}
