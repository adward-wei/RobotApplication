package com.ubtechinc.alpha.event;

/**
 * @desc : 事件设置事件
 * @author: wzt
 * @time : 2017/5/22
 * @modifier:
 * @modify_time:
 */

public class TimeSettingEvent extends BaseEvent {
    public byte[] rtcTime = new byte[7];
}
