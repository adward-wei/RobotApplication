/*
 *
 *  *
 *  *  *
 *  *  * Copyright (c) 2008-2017 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *  *
 *  *
 *
 */

package com.ubtechinc.alpha.cmds.chest;

import com.ubtechinc.alpha.serial.SerialCommandReceiver;

/**
 * @desc :  设置边充边玩模式
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/20
 * @modifier:
 * @modify_time:
 */

public class SetChargePlayingModeCmd extends ChestCmds {

    public SetChargePlayingModeCmd(SerialCommandReceiver receiver, byte[] param) {
        super(receiver, param);
    }

    @Override
    public byte getCmdId() {
        return ChestCmds.CMD_SET_CHARGE_PLAYING_MODE;
    }
}
