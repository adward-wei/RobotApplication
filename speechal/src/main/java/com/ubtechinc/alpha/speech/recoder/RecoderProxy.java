package com.ubtechinc.alpha.speech.recoder;

/**
 * @desc : 录音代理
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/29
 * @modifier:
 * @modify_time:
 */

public class RecoderProxy implements IRecoder {
    private IRecoder mRecoderInterface;

    public RecoderProxy(IRecoder mRecoderInterface) {
        this.mRecoderInterface = mRecoderInterface;
    }

    @Override
    public int startRecording() {
        return mRecoderInterface.startRecording();
    }

    @Override
    public void setPcmListener(IPcmListener listener) {
        mRecoderInterface.setPcmListener(listener);
    }

    @Override
    public boolean isRecording() {
        return mRecoderInterface.isRecording();
    }

    @Override
    public void stopRecording() {
        mRecoderInterface.stopRecording();
    }

    @Override
    public void destroy() {
        mRecoderInterface.destroy();
    }

    @Override
    public void setBufferSize(int size) {
        mRecoderInterface.setBufferSize(size);
    }
}
