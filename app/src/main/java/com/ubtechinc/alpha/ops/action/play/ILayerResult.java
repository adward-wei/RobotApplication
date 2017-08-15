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

package com.ubtechinc.alpha.ops.action.play;

import android.support.annotation.IntDef;

import com.ubtechinc.alpha.ops.action.Port;

/**
 * @desc :
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/5/8
 * @modifier:
 * @modify_time:
 */

public interface ILayerResult {
    public static final int TYPE_INT = 0;//整型
    public static final int TYPE_STR = 1;//字符串
    public static final int TYPE_MIX = 2;//混合
    @IntDef(value = {TYPE_INT, TYPE_STR, TYPE_MIX})
    public @interface DataType{}

    byte[] getOutData();

    @ILayerResult.DataType int getOutDataType();

    int getOutPortId();
}
