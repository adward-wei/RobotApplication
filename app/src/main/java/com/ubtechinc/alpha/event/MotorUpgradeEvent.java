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
 * @desc : 舵机升级事件
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/25
 * @modifier:
 * @modify_time:
 */

public final class MotorUpgradeEvent {
    public byte motorId;//舵机id
    public boolean success;//舵机升级操作是否成功
}
