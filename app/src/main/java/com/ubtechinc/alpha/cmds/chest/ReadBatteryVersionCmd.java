package com.ubtechinc.alpha.cmds.chest;

import com.ubtechinc.alpha.serial.SerialCommandReceiver;

/**
 * @desc : 读取电池包版本号
 * @author: wzt
 * @time : 2017/5/24
 * @modifier:
 * @modify_time:
 */


public class ReadBatteryVersionCmd extends ChestCmds {

    public ReadBatteryVersionCmd(SerialCommandReceiver receiver, byte[] param) {
        super(receiver, param);
    }

    @Override
    public byte getCmdId() {
        return ChestCmds.CMD_POWER_INFO;
    }
}
