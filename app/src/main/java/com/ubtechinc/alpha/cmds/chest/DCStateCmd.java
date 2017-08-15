package com.ubtechinc.alpha.cmds.chest;

import com.ubtechinc.alpha.serial.SerialCommandReceiver;

/**
 * @desc : 充电状态命令
 * @author: wzt
 * @time : 2017/5/17
 * @modifier:
 * @modify_time:
 */

public class DCStateCmd extends ChestCmds{

    public DCStateCmd(SerialCommandReceiver receiver, byte[] param) {
        super(receiver, param);
    }

    @Override
    public byte getCmdId() {
        return ChestCmds.CHES_DC_STATE;
    }
}
