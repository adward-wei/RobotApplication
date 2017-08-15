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

package com.ubtechinc.alpha.serverlibutil.interfaces;

import com.ubtechinc.alpha.sdk.SdkConstants;

/**
 * @desc : 动作回调接口
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/14
 * @modifier:
 * @modify_time:
 */
public interface ActionResultListener {

    /**
     * @Description 执行一个动作回调方法（异步）
     * @param nOpId playAction返回的opid, 代表对应的一次playAction
     * @param nErr {@link SdkConstants.OpResult}
     * @return
     * @throws
     */
    void onPlayActionResult(int nOpId, int nErr);
}
