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

package com.ubtechinc.alpha.ops.sys;

import com.ubtechinc.alpha.ops.SerialCmdOp;

/**
 * @desc : 系统设置操作父类
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/26
 * @modifier:
 * @modify_time:
 */

public abstract class BaseSysOp extends SerialCmdOp {
    public BaseSysOp(@Priority int priority) {
        super(priority);
        this.opType = TYPE_SYS_SET;
    }
}
