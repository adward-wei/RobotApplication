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
 * @desc : 串口服务状态事件
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/19
 * @modifier:
 * @modify_time:
 */
public final class SerialStateEvent extends BaseEvent {
    public final @SerialConstants.State int state;
    public SerialStateEvent(@SerialConstants.State int state){
        this.state = state;
    }
}
