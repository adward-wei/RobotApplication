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

package com.ubtechinc.alpha.ops.action.motion;

import android.support.annotation.IntDef;

import com.ubtechinc.alpha.ops.action.IByteProcessor;

/**
 * @desc : 动作控制层内的层根
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/28
 * @modifier:
 * @modify_time:
 */

public interface IMotionLayer extends IByteProcessor {
    public static final int TYPE_TIME = 0;
    public static final int TYPE_MOTOR = 1;
    public static final int TYPE_EYE = 2;
    public static final int TYPE_EAR = 3;
    public static final int TYPE_MUSIC = 4;
    public static final int TYPE_MOUTH = 5;
    @IntDef(value = {TYPE_TIME, TYPE_MOTOR, TYPE_EYE, TYPE_EAR, TYPE_MUSIC, TYPE_MOUTH})
    public @interface Type{}

}
