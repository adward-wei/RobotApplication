package com.ubtechinc.alpha.ops.motor;

import com.ubtech.utilcode.utils.ConvertUtils;
import com.ubtechinc.alpha.cmds.SerialCommand;
import com.ubtechinc.alpha.cmds.chest.MotorUpgradePageCmd;
import com.ubtechinc.alpha.ops.OpResult;
import com.ubtechinc.alpha.serial.SerialCmdResult;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;
import com.ubtechinc.alpha.serial.SerialConstants;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * @desc : 升级舵机数据: 分片升级，每个片大小是64字节
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/8/11
 * @modifier:
 * @modify_time:
 */

public final class UpgradeMotorDataOp extends BaseMotorOp {

    private final byte[] page;
    private final short number;
    private final boolean isLast;
    private final byte motorId;

    /**
     * 构造函数
     * @param motorId 舵机id
     * @param isLast 是否最后一个分片
     * @param number  分片序号
     * @param page 当前片数据
     */
    public UpgradeMotorDataOp(byte motorId, boolean isLast, short number, byte[] page) {
        super(NOR_PRIORITY);
        assert page!= null && page.length <= 64;
        this.motorId = motorId;
        this.isLast = isLast;
        this.number = number;
        this.page = page;
    }

    public UpgradeMotorDataOp(byte motorId, short number, byte[] page) {
        this(motorId,false,number,page);
    }

    @Override
    protected int waitTime() {
        return 0;
    }

    @Override
    protected SerialCommand createCmd(SerialCommandReceiver receiver) {
        final int length = 64 + 4;
        ByteBuffer bb = ByteBuffer.allocate(length);
        bb.put(motorId);
        bb.put((byte) (isLast ? 0x02 : 0x01));
        bb.put(ConvertUtils.h_short2Byte(number));
        bb.put(page);
        if (page.length < 64){
            byte[] zeros = new byte[length - page.length - 4];
            Arrays.fill(zeros,(byte)0x0);
            bb.put(zeros);
        }
        bb.rewind();
        byte[] bytes = new byte[bb.remaining()];
        bb.get(bytes);
        return new MotorUpgradePageCmd(receiver, bytes);
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
