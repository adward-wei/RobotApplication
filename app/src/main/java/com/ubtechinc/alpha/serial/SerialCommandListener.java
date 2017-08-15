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

/**
 * @desc : 串口命名回调接口
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/18
 * @modifier:
 * @modify_time:
 */
public interface SerialCommandListener {
     void onRcvSerialPortData(byte cmd, @SerialConstants.ErrorCode int resultCode, byte[] bytes);
}
