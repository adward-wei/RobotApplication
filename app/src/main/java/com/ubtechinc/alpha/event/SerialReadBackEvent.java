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

package com.ubtechinc.alpha.event;

import com.ubtechinc.alpha.serial.SerialConstants;


/**
 * @desc : 串口回读事件: 硬件主动发送过来的
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/18
 * @modifier:
 * @modify_time:
 */
public class SerialReadBackEvent extends BaseEvent {
    public int error = SerialConstants.ERR_OK;
    public byte[] data;
    public byte cmd;
    public final @SerialConstants.SerialType int serialType;

    public SerialReadBackEvent(@SerialConstants.SerialType int serialType) {
        this.serialType = serialType;
    }
}
