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

package com.ubtechinc.alpha.ops;

import android.support.annotation.IntDef;

/**
 * @desc : 串口命令op策略类
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/17
 * @modifier:
 * @modify_time:
 */
public final class SerialOpStrategy {
    /**
     * 等待正在运行的op执行完再执行
     */
    public static final int WAIT = 1;
    /**
     * 若有op正在执行，此次op忽略
     */
    public static final int IGNORE = 2;
    /**
     * 和正在运行的op并发执行
     */
    public static final int CONCURRENCY = 3;
    /**
     * 中断当前正在运行的op
     */
    public static final int INTERUPT = 4;

    @IntDef(value = {WAIT, IGNORE, CONCURRENCY, INTERUPT})
    public @interface Strategy{}

}
