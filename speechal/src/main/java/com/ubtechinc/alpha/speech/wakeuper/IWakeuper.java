package com.ubtechinc.alpha.speech.wakeuper;

/**
 * @desc : 唤醒模块接口
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/29
 * @modifier:
 * @modify_time:
 */

public interface IWakeuper {
    void init();
    void writeAudio(byte[] pcmData, int dataLen);
    void setWakeupListener(IWakeuperListener listener);
    void reset();
    void destroy();
    /**
     * @description: 是否可唤醒
     * @return
     */
    boolean isWakeup();
}
