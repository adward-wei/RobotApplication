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

package com.ubtechinc.alpha.serial;

import android.support.annotation.IntDef;

/**
 * @desc : 串口操作常量
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/19
 * @modifier:
 * @modify_time:
 */

public final class SerialConstants {

    public static final int ERR_OK = 0;
    public static final int ERR_BUSY = -1001;//串口繁忙
    public static final int ERR_WRITE_FAIL = -1002;//写失败
    public static final int ERR_IO = -1003; //IO错误
    public static final int ERR_UNKNOW = -1004;//未知错误
    public static final int ERR_UNDO = -1005; //撤销
    @IntDef(value = {ERR_OK, ERR_BUSY,ERR_WRITE_FAIL,ERR_IO, ERR_UNKNOW, ERR_UNDO})
    public @interface ErrorCode{}


    //串口服务类型
    public static final int TYPE_CHEST = 0;
    public static final int TYPE_HEADER = 1;
    @IntDef(value = {TYPE_CHEST, TYPE_HEADER})
    public @interface SerialType{}

    //串口服务状态
    public static final int SHUTDOWN = 0;//
    public static final int STARTUP = 1;
    @IntDef(value = {SHUTDOWN, STARTUP})
    public @interface State{}
}
