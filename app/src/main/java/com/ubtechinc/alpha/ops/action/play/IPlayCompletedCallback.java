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

/**
 * @desc : 播放结果回调
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/5/9
 * @modifier:
 * @modify_time:
 */

public interface IPlayCompletedCallback {
    void onPlayCompleted(CompletedState state);
}
