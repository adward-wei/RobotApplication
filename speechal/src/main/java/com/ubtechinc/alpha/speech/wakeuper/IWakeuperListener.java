package com.ubtechinc.alpha.speech.wakeuper;

/**
 * @desc : alpha 唤醒回调
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/29
 * @modifier:
 * @modify_time:
 */

public interface IWakeuperListener {
    void onWakeup(String resultStr, int soundAngle);

    void onError(int errCode);

    void onAudio(byte[] data, int dataLen, int param1, int param2);
}
