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

package com.ubtechinc.alpha.ops.action;

import com.ubtech.utilcode.utils.ByteBufferList;

/**
 * @desc : 解析接口
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/28
 * @modifier:
 * @modify_time:
 */

public interface IByteAnalysisor {
    /**
     * 
     * @param bb 所有数据
     * @param length 要分析的数据长度
     * @return
     */
    boolean analysis(ByteBufferList bb, int length);
}
