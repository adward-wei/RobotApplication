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

import com.ubtech.utilcode.utils.ByteBufferList;
import com.ubtech.utilcode.utils.ConvertUtils;

/**
 * @desc : 语音识别层model
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/29
 * @modifier:
 * @modify_time:
 */

public class VoiceRecognize implements IVoice {

    private int type;

    @Override
    public boolean analysis(ByteBufferList bbl, int length) {
        byte[] bytes = new byte[4];
        bbl.get(bytes);
        type = ConvertUtils.l_byte2Int(bytes);
        if (type != IVoice.TYPE_RECOGNIZE) return false;
        return true;
    }

    @Override
    public byte[] toBytes() {
        return ConvertUtils.l_int2Byte(type);
    }
}
