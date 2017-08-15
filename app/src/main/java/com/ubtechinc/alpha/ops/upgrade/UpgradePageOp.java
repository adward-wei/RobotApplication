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

import com.ubtech.utilcode.utils.ByteBufferList;
import com.ubtech.utilcode.utils.ConvertUtils;
import com.ubtechinc.alpha.cmds.SerialCommand;
import com.ubtechinc.alpha.cmds.chest.ChestUpgradePageCmd;
import com.ubtechinc.alpha.cmds.header.HeadUpgradePageCmd;
import com.ubtechinc.alpha.ops.OpResult;
import com.ubtechinc.alpha.ops.SerialCmdOp;
import com.ubtechinc.alpha.serial.SerialCmdResult;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;
import com.ubtechinc.alpha.serial.SerialConstants;

import java.nio.ByteBuffer;

/**
 * @desc : 升级固件,分页升级，页最大为128
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/24
 * @modifier:
 * @modify_time:
 */

public class UpgradePageOp extends SerialCmdOp {
    private final byte[] page;
    private final @SerialConstants.SerialType int type;

    public UpgradePageOp(int id, @SerialConstants.SerialType int type,  byte[] data) {
        super(MAX_PRIORITY);
        this.page = data;
        this.type = type;
        this.opType = TYPE_UPGRADE;
    }

    @Override
    protected int waitTime() {
        return 0;
    }

    @Override
    protected SerialCommand createCmd(SerialCommandReceiver receiver) {
        ByteBuffer bb = ByteBufferList.obtain(page.length + 2);
        bb.clear();
        bb.put(ConvertUtils.l_short2Byte((short) page.length));
        bb.put(page);
        bb.rewind();
        bb.limit(page.length + 2);
        byte[] bytes =new byte[bb.remaining()];
        bb.get(bytes);
        ByteBufferList.reclaim(bb);
        if (type == SerialConstants.TYPE_CHEST){
            return new ChestUpgradePageCmd(receiver, bytes);
        }else {
            return new HeadUpgradePageCmd(receiver, bytes);
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
