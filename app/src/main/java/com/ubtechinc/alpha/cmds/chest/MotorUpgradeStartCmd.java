package com.ubtechinc.alpha.cmds.chest;

import com.ubtechinc.alpha.serial.SerialCommandReceiver;

/**
 * @desc : 单个舵机升级开始
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/8/11
 * @modifier:
 * @modify_time:
 */

public final class MotorUpgradeStartCmd extends ChestCmds {

    public MotorUpgradeStartCmd(SerialCommandReceiver receiver, byte[] param) {
        super(receiver, param);
    }

    @Override
    public byte getCmdId() {
        return ChestCmds.CMD_UPGRADE_MOTOR_START;
    }
}
