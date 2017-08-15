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

/**
 * @desc : 电量上报事件
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/25
 * @modifier:
 * @modify_time:
 */

public final class ElectricityEvent extends BaseEvent {
    public boolean isLowBattery;//是否低电
    public byte mPercentage;//电量百分比
    public boolean isCharging;//是否正在充电，在5mic中有
}
