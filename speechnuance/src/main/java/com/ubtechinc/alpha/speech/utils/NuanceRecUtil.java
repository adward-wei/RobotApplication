package com.ubtechinc.alpha.speech.utils;

import com.ubtechinc.alpha.speech.AbstractSpeech;

/**
 * @desc : nuance语音识别处理工具
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/8/8
 * @modifier:
 * @modify_time:
 */

public final class NuanceRecUtil extends IRecUtil {

    public NuanceRecUtil(AbstractSpeech mSpeechContext) {
        super(mSpeechContext);
    }

    @Override
    protected boolean filterGrammarResult(String result) {
        return false;
    }

    @Override
    protected String parseIatResult(String speechResult) {
        return "";
    }
}
