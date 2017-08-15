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

package com.ubtechinc.alpha.ops.action.voice;

import android.support.annotation.IntDef;

import com.ubtechinc.alpha.ops.action.IByteProcessor;

/**
 * @desc :
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/29
 * @modifier:
 * @modify_time:
 */

public interface IVoice extends IByteProcessor {

    /**语音播报*/
    static public final int TYPE_TTS = 1;
    /**语音识别*/
    static public final int TYPE_RECOGNIZE = 2;

    @IntDef(value = {TYPE_TTS,TYPE_RECOGNIZE})
    public @interface Type{}
}
