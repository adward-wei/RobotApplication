package com.ubtechinc.nets.im.util.parser;

/**
 * 第三方消息通信的数据类型
 * Created by Liudongyang on 2017/4/14.
 */

public class UbtMessageData {

    private String appKey;
    private String message;


    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
