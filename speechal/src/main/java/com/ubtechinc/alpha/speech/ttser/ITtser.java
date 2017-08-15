package com.ubtechinc.alpha.speech.ttser;

/**
 * @desc : 语音合成模块接口
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/29
 * @modifier:
 * @modify_time:
 */

public interface ITtser {
    void init();
    void startSpeaking(String text, ITTSListener listener);
    void stopSpeaking();
    boolean isSpeaking();
    boolean destroy();
    /**
     * 语速
     * @param speed
     */
    void setTtsSpeed(String speed);

    /**
     * 获取语速
     * @return
     */
    String getTtsSpeed();

    /**
     * 语调
     * @param pitch
     */
    void setTtsPitch(String pitch);

    /**
     * 获取语调值
     * @return
     */
    String getTtsPitch();

    /**
     * 音量
     * @param volume
     */
    void setTtsVolume(String volume);

    /**
     * 获取音量值
     * @return
     */
    String getTtsVolume();

    /**
     * 语言
     * @param language
     */
    void setTtsLanguage(String language);

    /**
     * 语言值
     * @return
     */
    String getTtsLanguage();

    /**
     * 发音人
     * @param voicer
     */
    void setTtsVoicer(String voicer);

    /**
     * 获取发音人
     * @return
     */
    String getTtsVoicer();
}
