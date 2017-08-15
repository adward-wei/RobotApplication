package com.ubtechinc.alpha.ops.led;

import com.ubtech.utilcode.utils.ConvertUtils;
import com.ubtechinc.alpha.cmds.SerialCommand;
import com.ubtechinc.alpha.cmds.header.EyeLedCmd;
import com.ubtechinc.alpha.ops.OpResult;
import com.ubtechinc.alpha.serial.SerialCmdResult;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;

import java.nio.ByteBuffer;

/**
 * @desc : 眼睛灯设置:给动作文件用
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/5/2
 * @modifier:
 * @modify_time:
 */
public class SetEyeOp extends BaseLedOp {
    private final byte left;
    private final byte right;
    private final byte bright;
    private final byte color;
    private final short onTime;
    private final short offTime;
    private final short totalTime;

    public SetEyeOp(byte left, byte right, byte bright, byte color, short onTime, short offTime, short totalTime) {
        super(NOR_PRIORITY);
        this.left = left;
        this.right = right;
        this.bright = bright;
        this.color = color;
        this.onTime = onTime;
        this.offTime = offTime;
        this.totalTime = totalTime;
    }

    @Override
    protected int waitTime() {
        return 0;
    }

    @Override
    protected SerialCommand createCmd(SerialCommandReceiver receiver) {
        ByteBuffer bb = ByteBuffer.allocate(10);
        bb.rewind();
        bb.limit(10);
        bb.put(left);
        bb.put(right);
        bb.put(bright);
        bb.put(color);
        bb.put(ConvertUtils.h_short2Byte(onTime));
        bb.put(ConvertUtils.h_short2Byte(offTime));
        bb.put(ConvertUtils.h_short2Byte(totalTime));
        bb.rewind();
        byte[] bytes = new byte[10];
        bb.get(bytes);
        return new EyeLedCmd(receiver, bytes);
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