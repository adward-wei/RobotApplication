package com.ubtechinc.alpha.speech.recognizer;

/**
 * @desc : 语言识别模块接口
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/29
 * @modifier:
 * @modify_time:
 */

public interface ISpeechRecognizer {
    void init();
    int buildGrammar(String grammarStr, IGrammarListener listener);
    void setEngineType(EngineType mode);
    EngineType getCurSpeechMode();
    int startListening(IRecognizerListener listener);
    int writeAudio(byte[] data, int start, int end);
    void stopListening();
    boolean isListening();
    void cancel();
    boolean destroy();
}
