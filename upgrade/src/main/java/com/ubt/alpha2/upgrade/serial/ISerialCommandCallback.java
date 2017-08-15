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

package com.ubt.alpha2.upgrade.serial;

/**
 * @desc : 此接口只在串口包中使用，用户串口写入成功的回调
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/21
 * @modifier:
 * @modify_time:
 */

 interface ISerialCommandCallback {
    void onRcvSerialPortData(byte sessionId, byte cmd, byte index, @SerialConstants.ErrorCode int resultCode, byte[] bytes);
}
