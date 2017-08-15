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

import com.ubtechinc.alpha.serial.SerialConstants;

/**
 * @desc : 操作结果
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/25
 * @modifier:
 * @modify_time:
 */

public final class OpResult<T> {
    public @SerialConstants.ErrorCode int errorCode = SerialConstants.ERR_UNKNOW;
    public T data;
    public byte cmd;

    @Override
    public String toString() {
        return "OpResult[cmd="+cmd + ", errorCode="+errorCode+", data="+ (data== null? "":data.toString())+"]";
    }
}
