/*
 *
 *  *
 *  *  *
 *  *  * Copyright (c) 2008-2017 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *  *
 *  *
 *
 */
package com.ubtechinc.nlu.understander;

import android.support.annotation.IntDef;

import com.ubtechinc.nlu.iflytekmix.MixSemantic;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @desc: 语义理解回调接口
 * @author: Logic.peng
 * @email : 2091843903@qq.com
 * @time  : 2017/3/22
 * @modifier:
 * @modify_time:
 */
public interface IUnderstanderCallback {
    int TIMEOUT = 1;
    int WAITMOMENT = 2;
    @Retention(RetentionPolicy.SOURCE)
    @IntDef(value = {TIMEOUT, WAITMOMENT})
    @interface WaitState{}
    void onRecognizeSuccess(MixSemantic result);
    void onRecognizeError(String errorMessage);
    void onWaitForReturn(@WaitState int state);
}
