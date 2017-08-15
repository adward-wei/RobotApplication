package com.ubtechinc.alpha.callback.message.speech;

import com.ubtechinc.alpha.callback.DispatchMessage;
import com.ubtechinc.alpha.speech.AbstractSpeech;
import com.ubtechinc.alpha.speech.recoder.IPcmListener;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @desc : pcm回调处理
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/28
 * @modifier:
 * @modify_time:
 */

public class PcmCbMessage implements DispatchMessage.Callback {
    private final AbstractSpeech mSpeech;
    private final String mPackageName;

    public PcmCbMessage(AbstractSpeech speech, String packageName) {
        this.mSpeech = speech;
        this.mPackageName = packageName;
    }

    private byte[] data;
    private int length;

    public void setData(byte[] data) {
        this.data = data;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public void handleMessage() {
        ConcurrentHashMap<String, IPcmListener> maps = mSpeech.getPcmCallback();
        if (maps == null || maps.size() == 0) {
            return;
        }
        IPcmListener listener = maps.get(mPackageName);
        if (listener != null) {
            listener.onPcmData(data, length);
        }
    }
}
