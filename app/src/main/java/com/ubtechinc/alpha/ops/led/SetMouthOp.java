package com.ubtechinc.alpha.ops.led;

import com.ubtech.utilcode.utils.ConvertUtils;
import com.ubtechinc.alpha.cmds.SerialCommand;
import com.ubtechinc.alpha.cmds.header.MouthLedCmd;
import com.ubtechinc.alpha.ops.OpResult;
import com.ubtechinc.alpha.serial.SerialCmdResult;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;

import java.nio.ByteBuffer;

/**
 * @desc : 设置嘴巴灯://给动作文件用
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/5/10
 * @modifier:
 * @modify_time:
 */
public class SetMouthOp extends BaseLedOp {

    private final byte bright;
    private final short onTime;
    private final short offTime;
    private final short runTime;
    private final int mode;

    public SetMouthOp(byte bright, short onTime, short offTime, short runTime, int mode) {
        super(NOR_PRIORITY);
        this.bright = bright;
        this.onTime = onTime;
        this.offTime = offTime;
        this.runTime = runTime;
        this.mode = mode;
    }

    @Override
    protected int waitTime() {
        return 0;
    }

    @Override
    protected SerialCommand createCmd(SerialCommandReceiver receiver) {
        ByteBuffer bb = ByteBuffer.allocate(11);
        bb.rewind();
        bb.put(bright);
        bb.put(ConvertUtils.h_short2Byte(onTime));
        bb.put(ConvertUtils.h_short2Byte(offTime));
        bb.put(ConvertUtils.h_short2Byte(runTime));
        bb.put(ConvertUtils.h_int2Byte(mode));
        bb.rewind();
        byte[] bytes = new byte[11];
        bb.get(bytes);
        return new MouthLedCmd(receiver, bytes);
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