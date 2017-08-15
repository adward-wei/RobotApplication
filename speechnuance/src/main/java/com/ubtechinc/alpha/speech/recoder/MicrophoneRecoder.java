package com.ubtechinc.alpha.speech.recoder;

import com.nuance.dragon.toolkit.audio.sources.MicrophoneRecorderSource;

/**
 * @desc : 利用麦克风 audiosource录音
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/30
 * @modifier:
 * @modify_time:
 */

public final class MicrophoneRecoder implements IRecoder {

    private IPcmListener listener;
    private final MicrophoneRecorderSource proxy;

    public MicrophoneRecoder() {
        proxy = new MicrophoneAudioSource(new IPcmListener() {
            @Override
            public void onPcmData(byte[] data, int length) {
                if (listener != null){
                    listener.onPcmData(data, length);
                }
            }
        });
    }

    @Override
    public void setPcmListener(IPcmListener listener) {
        this.listener = listener;
    }

    @Override
    public int startRecording() {
        proxy.startRecording();
        return 0;
    }

    @Override
    public boolean isRecording() {
        return proxy.isActive();
    }

    @Override
    public void stopRecording() {
        proxy.stopRecording();
    }

    @Override
    public void destroy() {
        stopRecording();
    }

    @Override
    public void setBufferSize(int size) {

    }
}
