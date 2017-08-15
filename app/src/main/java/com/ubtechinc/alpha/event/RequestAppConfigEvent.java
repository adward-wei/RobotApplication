package com.ubtechinc.alpha.event;

/**
 * @desc : 获取第三方app配置信息的事件
 * @author: wzt
 * @time : 2017/6/5
 * @modifier:
 * @modify_time:
 */

public class RequestAppConfigEvent {
    public long requestSerial;
    public String peer;

    public int responseCmdID;
}
