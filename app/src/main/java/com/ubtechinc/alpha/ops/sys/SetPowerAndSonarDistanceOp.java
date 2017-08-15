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

package com.ubtechinc.alpha.ops.sys;

import com.ubtechinc.alpha.cmds.SerialCommand;
import com.ubtechinc.alpha.cmds.chest.SetPowerSonarDistanceCmd;
import com.ubtechinc.alpha.ops.OpResult;
import com.ubtechinc.alpha.serial.SerialCmdResult;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;

import java.nio.ByteBuffer;

/**
 * @desc : 设置电量和声呐距离
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/24
 * @modifier:
 * @modify_time:
 */

public class SetPowerAndSonarDistanceOp extends BaseSysOp {

    private final byte power;
    private final byte distance;

    public SetPowerAndSonarDistanceOp(byte power, byte distance) {
        super(NOR_PRIORITY);
        this.power = power;
        this.distance = distance;
    }

    @Override
    protected int waitTime() {
        return 0;
    }

    @Override
    protected SerialCommand createCmd(SerialCommandReceiver receiver) {
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.put(power);
        bb.put(distance);
        bb.rewind();
        byte[] bytes = new byte[2];
        bb.get(bytes);
        return new SetPowerSonarDistanceCmd(receiver, bytes);
    }

    @Override
    protected OpResult<String> parseResult(SerialCmdResult cmdResult) {
        OpResult<String> result = new OpResult<>();
        result.errorCode = cmdResult.getError();
        result.cmd = cmdResult.getCmd();
        result.data = null;
        return result;
    }
}
