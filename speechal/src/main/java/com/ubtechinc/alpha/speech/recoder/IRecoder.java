package com.ubtechinc.alpha.speech.recoder;

/**
 * @desc : 录音模块接口
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/29
 * @modifier:
 * @modify_time:
 */

public interface IRecoder {
    void setPcmListener(IPcmListener listener);

    int startRecording();

    boolean isRecording();

    void stopRecording();

    void destroy();

    void setBufferSize(int size);
}
