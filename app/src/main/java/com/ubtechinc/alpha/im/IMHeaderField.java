package com.ubtechinc.alpha.im;

/**
 * @desc : IM响应包的包头关键字段
 * @author: wzt
 * @time : 2017/6/6
 * @modifier:
 * @modify_time:
 */

public class IMHeaderField {
    public long requestSerial;
    public String peer;
    public int responseCmdId;

    public int customCmd;
}
