package com.ubtechinc.alpha.speech.recognizer;

/**
 * @desc : 语音模块代理层
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/30
 * @modifier:
 * @modify_time:
 */

public class RecongnizerProxy implements ISpeechRecognizer {
    private final ISpeechRecognizer mRecognizerInterface;
    public RecongnizerProxy(ISpeechRecognizer proxy) {
        this.mRecognizerInterface = proxy;
    }

    @Override
    public void init() {
       mRecognizerInterface.init();
    }

    @Override
    public int buildGrammar(String grammarStr, IGrammarListener listener) {
        return mRecognizerInterface.buildGrammar(grammarStr,listener);
    }

    public void setEngineType(EngineType mode) {
        mRecognizerInterface.setEngineType(mode);
    }

    @Override
    public EngineType getCurSpeechMode() {
        return mRecognizerInterface.getCurSpeechMode();
    }

    @Override
    public int startListening(IRecognizerListener listener) {
        return mRecognizerInterface.startListening(listener);
    }

    @Override
    public int writeAudio(byte[] data, int start, int end) {
        return mRecognizerInterface.writeAudio(data,start,end);
    }

    @Override
    public void stopListening() {
        mRecognizerInterface.stopListening();
    }

    public boolean isListening() {
        return mRecognizerInterface.isListening();
    }

    @Override
    public void cancel() {
        mRecognizerInterface.cancel();
    }

    @Override
    public boolean destroy() {
        return mRecognizerInterface.destroy();
    }
}
