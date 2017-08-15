package com.ubtechinc.nets.http;

/**
 * @desc :
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/5/16
 * @modifier:
 * @modify_time:
 */

public interface RawCallback {

    void onError(ThrowableWrapper e);

    void onSuccess(byte[] rawBytes);
}
