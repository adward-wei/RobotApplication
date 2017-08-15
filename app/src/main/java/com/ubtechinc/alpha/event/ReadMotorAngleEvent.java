package com.ubtechinc.alpha.event;

/**
 * @desc : 动作执行事件
 * @author: wzt
 * @time : 2017/5/22
 * @modifier:
 * @modify_time:
 */

public class ReadMotorAngleEvent extends BaseEvent {
    public long requestSerial;
    public String peer;

    public int responseCmdID;

    public int motorID;
    public boolean acdump;
}
