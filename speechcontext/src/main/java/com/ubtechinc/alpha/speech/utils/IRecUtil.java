package com.ubtechinc.alpha.speech.utils;

import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha.callback.message.speech.RecCbMessage;
import com.ubtechinc.alpha.speech.AbstractSpeech;
import com.ubtechinc.alpha.speech.recognizer.IRecognizerListener;

/**
 * @desc : 语音识别结果处理工具
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/8/8
 * @modifier:
 * @modify_time:
 */

public abstract class IRecUtil {
    public static final int LOCAL_GRAMMAR_SCORE_VALUE = 30;
    protected final AbstractSpeech mSpeechContext;
    protected int mScore = LOCAL_GRAMMAR_SCORE_VALUE;
    public volatile String mPackageName;
    public int appid;
    private IRecognizerListener mListener;

    public IRecUtil(AbstractSpeech mSpeechContext) {
        this.mSpeechContext = mSpeechContext;
    }

    private void handleCallback(String content,int state){
        if (mPackageName == null) return;
        RecCbMessage callback = new RecCbMessage(mSpeechContext, mPackageName, content, state, mListener);
        mSpeechContext.getDispatch().enqueue(callback);
    }

    public void setListener(IRecognizerListener listener) {
        this.mListener = listener;
    }

    public IRecognizerListener getListener(){
        return mProxyListener;
    }

    private IRecognizerListener mProxyListener = new IRecognizerListener() {
        @Override
        public void onBegin() {
            handleCallback("", RecCbMessage.CALLBACK_STATE_BEGIN_SPEECH);
        }

        @Override
        public void onEnd() {
            handleCallback("", RecCbMessage.CALLBACK_STATE_END_SPEECH);
        }

        @Override
        public void onResult(String result, boolean isLast) {
            if (mSpeechContext.getMode() == AbstractSpeech.SpeechMode.IAT){
                LogUtils.d("听写识别结果...");
                handleCallback(parseIatResult(result), RecCbMessage.CALLBACK_STATE_RESULT_SPEECH);
                return;
            }
            if (filterGrammarResult(result)) return;
            handleCallback(result, RecCbMessage.CALLBACK_STATE_RESULT_SPEECH);
        }

        @Override
        public void onError(int errCode) {
            LogUtils.W("识别错误，错误码:%d", errCode);
            handleCallback("", RecCbMessage.CALLBACK_STATE_ERROR_SPEECH);
        }
    };

    abstract protected boolean filterGrammarResult(String result);
    abstract protected String parseIatResult(String speechResult);
}
