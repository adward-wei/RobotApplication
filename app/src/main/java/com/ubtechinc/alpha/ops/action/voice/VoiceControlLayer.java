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
import com.ubtechinc.alpha.ops.action.ControlLayer;

import java.nio.ByteBuffer;

import timber.log.Timber;

/**
 * @desc : 语音控制层model
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/28
 * @modifier:
 * @modify_time:
 */

public class VoiceControlLayer extends ControlLayer {
    private int type;//不参与序列化

    private IVoice mVoice;

    @Override
    public boolean analysis(ByteBufferList bbl, int length) {
        boolean ret = super.analysis(bbl, length);//读出长度：4个字节
        if (!ret) return false;

        byte[] bytes = bbl.peekBytes(4);
        type = ConvertUtils.l_byte2Int(bytes);

        if (type == IVoice.TYPE_TTS){
            mVoice = new VoiceTTS();
        } else if (type == IVoice.TYPE_RECOGNIZE){
            mVoice = new VoiceRecognize();
        }else {
            return false;
        }

        if(bbl.remaining() != (length -4)){
            Timber.d("parse error...");
            return false;
        }

        return mVoice.analysis(bbl, length - 4);
    }

    @Override
    public byte[] toBytes() {
        int totalLength = 4 + getLayerTotalLength();
        ByteBuffer bb = ByteBuffer.allocate(totalLength);
        bb.limit(totalLength);
        bb.rewind();

        bb.put(super.toBytes());
        bb.put(mVoice.toBytes());

        bb.rewind();
        byte[] bytes = new byte[totalLength];
        bb.get(bytes);
        return bytes;
    }

    //////////getter///////

    public @IVoice.Type int getType() {
        return type;
    }

    public IVoice getVoice() {
        return mVoice;
    }
}
