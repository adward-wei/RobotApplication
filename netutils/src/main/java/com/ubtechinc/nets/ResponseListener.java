package com.ubtechinc.nets;

import com.ubtechinc.nets.http.ThrowableWrapper;

/**
 * @desc :
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/5/16
 * @modifier:
 * @modify_time:
 */

public interface ResponseListener<T> {
    void onError(ThrowableWrapper e);
    void onSuccess(T t);
}
