package com.ubtechinc.alpha.speech.recognizer;

/**
 * @desc :
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/30
 * @modifier:
 * @modify_time:
 */

public interface IRecognizerListener {
    void onBegin();
    void onEnd();
    void onResult(String result, boolean b);
    void onError(int errCode);
}
