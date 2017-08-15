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

package com.ubtechinc.alpha.ops.upgrade;

import com.ubtechinc.alpha.cmds.SerialCommand;
import com.ubtechinc.alpha.cmds.chest.ChestUpgradeEndCmd;
import com.ubtechinc.alpha.cmds.header.HeadUpgradeEndCmd;
import com.ubtechinc.alpha.ops.OpResult;
import com.ubtechinc.alpha.ops.SerialCmdOp;
import com.ubtechinc.alpha.serial.SerialCmdResult;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;
import com.ubtechinc.alpha.serial.SerialConstants;

/**
 * @desc : 升级结束
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/24
 * @modifier:
 * @modify_time:
 */

public class UpgradeEndOp extends SerialCmdOp {
    private final byte[] md5;
    private final @SerialConstants.SerialType int type;
    public UpgradeEndOp(@SerialConstants.SerialType int type, byte[] md5) {
        super(MAX_PRIORITY);
        this.md5 = md5;
        this.type = type;
        this.opType = TYPE_UPGRADE;
    }

    @Override
    protected int waitTime() {
        return 0;
    }

    @Override
    protected SerialCommand createCmd(SerialCommandReceiver receiver) {
        if (type == SerialConstants.TYPE_CHEST) {
            return new ChestUpgradeEndCmd(receiver, md5);
        }else {
            return new HeadUpgradeEndCmd(receiver, md5);
        }
    }

    @Override
    protected OpResult parseResult(SerialCmdResult cmdResult) {
        OpResult result = new OpResult();
        result.errorCode = cmdResult.getError();
        result.cmd = cmdResult.getCmd();
        result.data = null;
        return result;
    }
}
