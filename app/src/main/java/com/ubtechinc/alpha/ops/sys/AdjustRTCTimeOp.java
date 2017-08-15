package com.ubtechinc.alpha.ops.sys;

import com.ubtechinc.alpha.cmds.SerialCommand;
import com.ubtechinc.alpha.cmds.chest.AdjustTimeCmd;
import com.ubtechinc.alpha.event.TimeSettingEvent;
import com.ubtechinc.alpha.ops.OpResult;
import com.ubtechinc.alpha.serial.SerialCmdResult;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;

import java.nio.ByteBuffer;

/**
 * @desc : 校准嵌入式RTC时间
 * @author: wzt
 * @time : 2017/5/22
 * @modifier:
 * @modify_time:
 */

public class AdjustRTCTimeOp extends BaseSysOp {
    private byte[] rtcTime = new byte[7];

    public AdjustRTCTimeOp(TimeSettingEvent event) {
        super(NOR_PRIORITY);
        System.arraycopy(event.rtcTime, 0, rtcTime, 0, 7);
    }

    @Override
    protected int waitTime() {
        return 0;
    }

    @Override
    protected SerialCommand createCmd(SerialCommandReceiver receiver) {
        ByteBuffer bb = ByteBuffer.allocate(7);
        bb.put(rtcTime[0]);
        bb.put(rtcTime[1]);
        bb.put(rtcTime[2]);
        bb.put(rtcTime[3]);
        bb.put(rtcTime[4]);
        bb.put(rtcTime[5]);
        bb.put(rtcTime[6]);
        bb.rewind();
        byte[] bytes = new byte[bb.remaining()];
        bb.get(bytes);
        return new AdjustTimeCmd(receiver, bytes);
    }

    @Override
    protected OpResult parseResult(SerialCmdResult cmdResult) {
        return null;
    }
}
