package com.ubtechinc.alpha.speech.recoder;

/**
 * @desc : alpha pcm 回调接口
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/29
 * @modifier:
 * @modify_time:
 */

public interface IPcmListener {
    void onPcmData(byte[] data, int length);
}
