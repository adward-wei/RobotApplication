package com.ubtechinc.alpha.cmds.chest;

import com.ubtechinc.alpha.serial.SerialCommandReceiver;

/**
 * @desc : 设置所有舵机的角度
 * @author: wzt
 * @time : 2017/5/24
 * @modifier:
 * @modify_time:
 */

public class SetAllMotorAngleCmd extends ChestCmds {

    public SetAllMotorAngleCmd(SerialCommandReceiver receiver, byte[] param) {
        super(receiver, param);
    }

    @Override
    public byte getCmdId() {
        return ChestCmds.CMD_SET_ALL_ANGLE;
    }
}
