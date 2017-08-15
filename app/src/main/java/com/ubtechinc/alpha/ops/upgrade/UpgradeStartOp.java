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

import com.ubtech.utilcode.utils.ConvertUtils;
import com.ubtechinc.alpha.cmds.SerialCommand;
import com.ubtechinc.alpha.cmds.chest.ChestUpgradeStartCmd;
import com.ubtechinc.alpha.cmds.header.HeadUpgradeStartCmd;
import com.ubtechinc.alpha.ops.OpResult;
import com.ubtechinc.alpha.ops.SerialCmdOp;
import com.ubtechinc.alpha.serial.SerialCmdResult;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;
import com.ubtechinc.alpha.serial.SerialConstants;

/**
 * @desc : 升级开始
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/24
 * @modifier:
 * @modify_time:
 */

public class UpgradeStartOp extends SerialCmdOp {
    private final int fileSize;
    private final @SerialConstants.SerialType int type;

    public UpgradeStartOp(@SerialConstants.SerialType int type, int fileSize) {
        super(MAX_PRIORITY);
        this.fileSize = fileSize;
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
            return new ChestUpgradeStartCmd(receiver, ConvertUtils.l_int2Byte(fileSize));
        }else {
            return new HeadUpgradeStartCmd(receiver, ConvertUtils.l_int2Byte(fileSize));
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
