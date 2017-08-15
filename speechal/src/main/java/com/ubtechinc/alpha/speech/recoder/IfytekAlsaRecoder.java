package com.ubtechinc.alpha.speech.recoder;

import com.iflytek.alsa.AlsaRecorder;
import com.iflytek.alsa.jni.AlsaJni;

/**
 * @desc : 科大讯飞recoder包装类
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/29
 * @modifier:
 * @modify_time:
 */

public final class IfytekAlsaRecoder implements IRecoder{
    private IPcmListener listener;
    private AlsaRecorder proxy;

    public IfytekAlsaRecoder(int sampleRate) {
        proxy = AlsaRecorder.createInstance(0, sampleRate, 1536);
        AlsaJni.showJniLog(false);
    }

    public IfytekAlsaRecoder() {
        //讯飞默认96000
        proxy = AlsaRecorder.createInstance(0, 96000, 1536);
        AlsaJni.showJniLog(false);
    }

    @Override
    public void setPcmListener(IPcmListener listener) {
        this.listener = listener;
    }

    public int startRecording(){
        return proxy.startRecording(new AlsaRecorder.PcmListener() {
            @Override
            public void onPcmData(byte[] bytes, int i) {
                //非主线程调用
                if (listener != null)
                    listener.onPcmData(bytes, i);
            }
        });
    }

    public boolean isRecording(){
        return proxy.isRecording();
    }

    public void stopRecording(){
         proxy.stopRecording();
    }

    public void destroy(){
        proxy.destroy();
    }

    public void setBufferSize(int size){
        proxy.setBufferSize(size);
    }

    public IPcmListener getListener(){
        return listener;
    }
}
