package com.ubtechinc.alpha.speech.ttser;

/**
 * @desc : tts模块回调
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/29
 * @modifier:
 * @modify_time:
 */

public interface ITTSListener {
    void onTtsBegin();
    void onTtsCompleted(int errCode);
}
