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

import android.text.TextUtils;

import com.ubtechinc.alpha.app.AlphaApplication;
import com.ubtechinc.alpha.ops.action.Block;
import com.ubtechinc.alpha.ops.action.Port;
import com.ubtechinc.alpha.ops.action.utils.BlockHelper;
import com.ubtechinc.alpha.ops.action.voice.VoiceRecognize;
import com.ubtechinc.alpha.speech.SpeechServiceProxy;
import com.ubtechinc.alpha.speech.recognizer.IRecognizerListener;

/**
 * @desc :
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/5/8
 * @modifier:
 * @modify_time:
 */

class RecognizePlay implements IPlayer,ILayerResult {
    private final VoiceRecognize recognize;
    private final Block block;

    //输出
    private String mRecognizeStr;

    RecognizePlay(VoiceRecognize recognize, Block block) {
        this.recognize = recognize;
        this.block = block;
    }

    @Override
    public byte[] getOutData() {
        return TextUtils.isEmpty(mRecognizeStr)? new byte[0] : mRecognizeStr.getBytes();
    }

    @Override
    public @ILayerResult.DataType int getOutDataType() {
        return ILayerResult.TYPE_STR;
    }

    @Override
    public int getOutPortId() {
        return BlockHelper.findPortIdByPortType(block, Port.TYPE_STOP);
    }

    @Override
    public void play(byte[] data, @ILayerResult.DataType int nType) {
        SpeechServiceProxy.getInstance().startSpeechAsr(AlphaApplication.getContext().getPackageName(), new IRecognizerListener(){

            @Override
            public void onBegin() {

            }

            @Override
            public void onEnd() {

            }

            @Override
            public void onResult(String result, boolean b) {
                mRecognizeStr = result;
            }

            @Override
            public void onError(int errCode) {

            }
        });
    }
}
