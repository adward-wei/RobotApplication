package com.ubtechinc.alpha.cmds.chest;

import com.ubtechinc.alpha.serial.SerialCommandReceiver;

/**
 * @desc : 关机模式设置，禁止还是不禁止
 * @author: wzt
 * @time : 2017/5/24
 * @modifier:
 * @modify_time:
 */

public class ShutdownSettingCmd extends ChestCmds {

    public ShutdownSettingCmd(SerialCommandReceiver receiver, byte[] param) {
        super(receiver, param);
    }

    @Override
    public byte getCmdId() {
        return ChestCmds.CHEST_SHUTDOWN_SETTING;
    }
}
