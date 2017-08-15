package com.ubtechinc.alpha.speech.wakeuper;

/**
 * @desc : 唤醒模块代理类
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/29
 * @modifier:
 * @modify_time:
 */

public class WakeuperProxy implements IWakeuper {
    private final IWakeuper mWakeuperInterface;

    public WakeuperProxy(IWakeuper mWakeuperInterface) {
        this.mWakeuperInterface = mWakeuperInterface;
    }

    @Override
    public void init() {
        mWakeuperInterface.init();
    }

    @Override
    public void writeAudio(byte[] pcmData, int dataLen) {
        mWakeuperInterface.writeAudio(pcmData, dataLen);
    }

    @Override
    public void setWakeupListener(IWakeuperListener listener) {
        mWakeuperInterface.setWakeupListener(listener);
    }

    @Override
    public void reset() {
        mWakeuperInterface.reset();
    }

    @Override
    public void destroy() {
        mWakeuperInterface.destroy();
    }

    @Override
    public boolean isWakeup() {
        return mWakeuperInterface.isWakeup();
    }
}
