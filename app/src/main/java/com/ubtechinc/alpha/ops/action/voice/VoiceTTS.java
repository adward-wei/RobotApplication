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
import com.ubtechinc.alpha.ops.action.Constraints;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import timber.log.Timber;

/**
 * @desc : TTS语音层model
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/29
 * @modifier:
 * @modify_time:
 */

public class VoiceTTS implements IVoice {

    private int type;
    private int mTtsTextByteLength;
    private byte[] mTtsTextBytes = new byte[0];
    private String mTtsText;

    @Override
    public boolean analysis(ByteBufferList bbl, int length) {
        byte[] bytes = new byte[4];
        bbl.get(bytes);
        type = ConvertUtils.l_byte2Int(bytes);
        if (type != IVoice.TYPE_TTS) return false;

        bbl.get(bytes);
        mTtsTextByteLength = ConvertUtils.l_byte2Int(bytes);
        if (mTtsTextByteLength > 0) {
            mTtsTextBytes = new byte[mTtsTextByteLength];
            try {
                mTtsText = new String(mTtsTextBytes, Constraints.CHARSET_NAME);
            } catch (UnsupportedEncodingException e) {
                Timber.d(e.getMessage());
                return false;
            }
        }
        return true;
    }

    @Override
    public byte[] toBytes() {
        int totalLength = 4 + 4 + mTtsTextByteLength;
        ByteBuffer bb = ByteBuffer.allocate(totalLength);
        bb.limit(totalLength);
        bb.rewind();

        bb.put(ConvertUtils.l_int2Byte(type));
        bb.put(ConvertUtils.l_int2Byte(mTtsTextByteLength));
        bb.put(mTtsTextBytes);


        bb.rewind();
        byte[] bytes = new byte[totalLength];
        bb.get(bytes);
        return bytes;
    }

    //////////getter/////////

    public String getTtsText() {
        return mTtsText;
    }
}
