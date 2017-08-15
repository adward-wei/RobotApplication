package com.ubtechinc.alpha.ops.motor;

import com.ubtech.utilcode.utils.ConvertUtils;
import com.ubtechinc.alpha.cmds.SerialCommand;
import com.ubtechinc.alpha.cmds.chest.MotorUpgradeStartCmd;
import com.ubtechinc.alpha.ops.OpResult;
import com.ubtechinc.alpha.serial.SerialCmdResult;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;
import com.ubtechinc.alpha.serial.SerialConstants;

import java.nio.ByteBuffer;

/**
 * @desc : 升级舵机请求操作
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/8/11
 * @modifier:
 * @modify_time:
 */

public final class UpgradeMotorStartOp extends BaseMotorOp {
    private final byte mMotorId;
    private final int mDataSize;

    public UpgradeMotorStartOp(byte motorId, int dataSize) {
        super(NOR_PRIORITY);
        this.mMotorId = motorId;
        this.mDataSize = dataSize;
    }

    @Override
    protected int waitTime() {
        return 0;
    }

    @Override
    protected SerialCommand createCmd(SerialCommandReceiver receiver) {
        ByteBuffer bb = ByteBuffer.allocate(5);
        bb.put(mMotorId);
        bb.put(ConvertUtils.h_int2Byte(mDataSize));
        bb.rewind();
        byte[] bytes = new byte[bb.remaining()];
        bb.get(bytes);
        return new MotorUpgradeStartCmd(receiver, bytes);
    }

    @Override
    protected OpResult parseResult(SerialCmdResult cmdResult) {
        OpResult<String> result = new OpResult<>();
        result.errorCode = cmdResult == null ? SerialConstants.ERR_UNKNOW : cmdResult.getError();
        result.data = null;
        result.cmd = cmdResult == null ? -1: cmdResult.getCmd();
        return result;
    }
}
