package com.ubtechinc.alpha.ops.motor;

import com.ubtech.utilcode.utils.ConvertUtils;
import com.ubtechinc.alpha.cmds.Command;
import com.ubtechinc.alpha.cmds.SerialCommand;
import com.ubtechinc.alpha.cmds.chest.SetAllMotorAngleCmd;
import com.ubtechinc.alpha.ops.OpResult;
import com.ubtechinc.alpha.serial.SerialCmdResult;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;

import java.nio.ByteBuffer;

/**
 * @desc : 所有舵机移动绝对角度操作
 * @author: wzt
 * @time : 2017/5/24
 * @modifier: logic.peng
 * @modify_time:
 */

public class MoveAllAbsAngleOp extends BaseMotorOp {

    private byte[] angles = new byte[20];
    private short executeTime;

    public MoveAllAbsAngleOp(int[] angles, short executeTime) {
        super(NOR_PRIORITY);
        for (int i = 0; i < angles.length ; i ++){
            this.angles[i] = ConvertUtils.l_int2Byte(angles[i])[0];
        }
        this.executeTime = (short) Math.min(Math.max(Command.MIN_TIME, executeTime), Command.MAX_TIME);
    }

    @Override
    protected int waitTime() {
        return executeTime;
    }

    @Override
    protected SerialCommand createCmd(SerialCommandReceiver receiver) {
        ByteBuffer bb = ByteBuffer.allocate(22);
        bb.put(angles);
        bb.put(ConvertUtils.h_short2Byte(executeTime));
        bb.rewind();
        byte[] bytes = new byte[bb.remaining()];
        bb.get(bytes);
        return new SetAllMotorAngleCmd(receiver, bytes);
    }

    @Override
    protected OpResult parseResult(SerialCmdResult cmdResult) {
        OpResult result = new OpResult<>();
        result.errorCode = cmdResult.getError();
        result.data = null;
        result.cmd = cmdResult.getCmd();
        return result;
    }
}
