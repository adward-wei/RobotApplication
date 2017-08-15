package com.ubtechinc.alpha.speech.recoder;

import com.nuance.dragon.toolkit.audio.AudioChunk;
import com.nuance.dragon.toolkit.audio.AudioType;
import com.nuance.dragon.toolkit.audio.sources.MicrophoneRecorderSource;
import com.ubtech.utilcode.utils.ConvertUtils;

/**
 * @desc :
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/8/1
 * @modifier:
 * @modify_time:
 */

public final class MicrophoneAudioSource extends MicrophoneRecorderSource {
    private IPcmListener listener;
    public MicrophoneAudioSource(IPcmListener listener) {
        //PCM_48k;PCM_44k;PCM_22k;PCM_16k;PCM_11k;
        super(AudioType.PCM_48k);
        this.listener = listener;
    }

    @Override
    protected AudioChunk createNewAudioChunk(AudioType type, short[] data, long timestamp) {
        if (this.listener != null){
            listener.onPcmData(convertShortArrayToByteArray(data, true), data.length * 2);
        }
        return super.createNewAudioChunk(type, data, timestamp);
    }

    private static byte[] convertShortArrayToByteArray(short[] data, boolean littleEndian) {
        final int length = data.length;
        byte[] bytes = new byte[length * 2];
        for (int i = 0; i < 2 * length - 1; i += 2) {
            if (littleEndian) {
                System.arraycopy(ConvertUtils.l_short2Byte(data[2/i]), 0, bytes, i, 2);
            } else {
                System.arraycopy(ConvertUtils.h_short2Byte(data[2/i]), 0, bytes, i, 2);
            }
        }
        return bytes;
    }
}
