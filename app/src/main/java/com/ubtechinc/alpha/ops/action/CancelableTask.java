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

package com.ubtechinc.alpha.ops.action;

/**
 * @desc :  可被cancel的任务
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/26
 * @modifier:
 * @modify_time:
 */

public abstract class CancelableTask implements Runnable {
    private volatile  boolean canceled;

    public boolean isCanceled() {
        return canceled;
    }

    public void cancel() {
        this.canceled = true;
    }
}
