package com.ubtechinc.alpha.event;

/**
 * @desc : 应用管理事件
 * @author: wzt
 * @time : 2017/5/22
 * @modifier:
 * @modify_time:
 */

public class AppManageEvent extends BaseEvent {
    public long requestSerial;
    public String peer;

    public int responseCmdID;

    public String event;
    public String packageName;
    public String name;
    public String clientIP;
    public String srcApp;
    public byte angle;
}
