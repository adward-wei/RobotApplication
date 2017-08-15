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

/**
 * @desc: 可被Cancel的IUnderstanderCallback
 * @author: Logic.peng
 * @email : 2091843903@qq.com
 * @time  : 2017/3/22
 * @modifier:
 * @modify_time:
 */

public abstract class CancelableUnderstanderCallback {

    private volatile boolean isCanceled = false;
    private volatile boolean finished = false;

    public boolean isCanceled() {
        return isCanceled;
    }

    public void canceled() {
        isCanceled = true;
    }

    public abstract IUnderstanderCallback getCallback();

    public boolean isFinished() {
        return finished;
    }

    public void finished() {
        this.finished = true;
    }
}
