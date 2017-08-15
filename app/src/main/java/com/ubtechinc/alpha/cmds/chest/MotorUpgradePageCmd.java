package com.ubtechinc.alpha.cmds.chest;

import com.ubtechinc.alpha.serial.SerialCommandReceiver;

/**
 * @desc : 升级舵机数据
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/8/11
 * @modifier:
 * @modify_time:
 */

public final class MotorUpgradePageCmd extends ChestCmds {

    public MotorUpgradePageCmd(SerialCommandReceiver receiver, byte[] param) {
        super(receiver, param);
    }

    @Override
    public byte getCmdId() {
        return ChestCmds.CMD_UPGRADE_MOTOR_PAGE;
    }
}
