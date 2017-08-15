package com.ubtechinc.alpha.im;

/**
 * @desc : 对于异步处理的消息，增加回调接口，方便发送响应消息
 * @author: wzt
 * @time : 2017/6/6
 * @modifier:
 * @modify_time:
 */

public interface IMsgResponseCallback {
    void onResult(IMHeaderField headerField, Object obj);
}
