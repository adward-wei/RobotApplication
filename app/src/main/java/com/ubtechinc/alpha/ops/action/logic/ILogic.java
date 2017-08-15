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

package com.ubtechinc.alpha.ops.action.logic;

import android.support.annotation.IntDef;

import com.ubtechinc.alpha.ops.action.IByteProcessor;

/**
 * @desc : 逻辑层
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/28
 * @modifier:
 * @modify_time:
 */

public interface ILogic extends IByteProcessor {
    /**if 逻辑块*/
     static public final int IF_LOGIC = 1;
    /**for 逻辑块*/
    static public final int FOR_LOGIC = 2;
    /** switch 逻辑块*/
    static public final int SWITCH_LOGIC = 3;
    @IntDef(value = {IF_LOGIC, FOR_LOGIC, SWITCH_LOGIC})
    public @interface Type{}
}
