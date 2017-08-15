package com.ubtechinc.alpha.speech.ttser;

/**
 * @desc : tts代理
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/29
 * @modifier:
 * @modify_time:
 */

public class TtserProxy implements ITtser {
    // TODO: 2017/7/30 注入
    private final ITtser mTtserInterface;

    public TtserProxy(ITtser mTtserInterface) {
        this.mTtserInterface = mTtserInterface;
    }

    @Override
    public void init() {
        mTtserInterface.init();
    }

    @Override
    public void startSpeaking(String text, ITTSListener listener) {
        mTtserInterface.startSpeaking(text, listener);
    }

    @Override
    public void stopSpeaking() {
        mTtserInterface.stopSpeaking();
    }

    @Override
    public boolean isSpeaking() {
        return mTtserInterface.isSpeaking();
    }

    @Override
    public boolean destroy() {
        return mTtserInterface.destroy();
    }

    @Override
    public void setTtsSpeed(String speed) {
        mTtserInterface.setTtsSpeed(speed);
    }

    @Override
    public String getTtsSpeed() {
        return mTtserInterface.getTtsSpeed();
    }

    @Override
    public void setTtsPitch(String pitch) {
        mTtserInterface.setTtsPitch(pitch);
    }

    @Override
    public String getTtsPitch() {
        return mTtserInterface.getTtsPitch();
    }

    @Override
    public void setTtsVolume(String volume) {
        mTtserInterface.setTtsVolume(volume);
    }

    @Override
    public String getTtsVolume() {
        return mTtserInterface.getTtsVolume();
    }

    @Override
    public void setTtsLanguage(String language) {
        mTtserInterface.setTtsLanguage(language);
    }

    @Override
    public String getTtsLanguage() {
        return mTtserInterface.getTtsLanguage();
    }

    @Override
    public void setTtsVoicer(String voicer) {
        mTtserInterface.setTtsVoicer(voicer);
    }

    @Override
    public String getTtsVoicer() {
        return mTtserInterface.getTtsVoicer();
    }
}
