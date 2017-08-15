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
 * @desc : 停止动作的客户端回调
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/15
 * @modifier:
 * @modify_time:
 */
public interface StopActonResultListener {

    /**
     * @Description 停止一个动作回调方法（异步）
     * @param nErr {@link SdkConstants.OpResult}
     * @return
     * @throws
     */
    void onStopActionResult(int nErr);
}
