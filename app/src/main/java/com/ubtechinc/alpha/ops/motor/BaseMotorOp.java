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

package com.ubtechinc.alpha.ops.motor;

import com.ubtechinc.alpha.ops.SerialCmdOp;

/**
 * @desc : 舵机操作父类
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/26
 * @modifier:
 * @modify_time:
 */

public abstract class BaseMotorOp extends SerialCmdOp {
    public BaseMotorOp(@Priority int priority) {
        super(priority);
        this.opType = TYPE_MOTOR;
    }
}
