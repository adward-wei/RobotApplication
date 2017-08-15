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

import com.ubtechinc.alpha.ops.action.Block;
import com.ubtechinc.alpha.ops.action.Port;
import com.ubtechinc.alpha.ops.action.utils.BlockHelper;
import com.ubtechinc.alpha.ops.action.voice.VoiceTTS;
import com.ubtechinc.alpha.sdk.speech.SpeechRobotApi;
import com.ubtechinc.alpha.speech.SpeechServiceProxy;

/**
 * @desc :
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/5/8
 * @modifier:
 * @modify_time:
 */

class TTSPlay implements IPlayer,ILayerResult {
    private final VoiceTTS tts;
    private final Block block;

    TTSPlay(VoiceTTS tts, Block block) {
        this.tts = tts;
        this.block = block;
    }

    @Override
    public byte[] getOutData() {
        return TextUtils.isEmpty(tts.getTtsText())? new byte[0] : tts.getTtsText().getBytes();
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
        if (TextUtils.isEmpty(tts.getTtsText())){
            return;
        }
        SpeechServiceProxy.getInstance().speechStartTTS(tts.getTtsText(),null);
    }
}
