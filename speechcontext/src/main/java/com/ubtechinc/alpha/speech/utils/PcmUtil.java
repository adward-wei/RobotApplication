package com.ubtechinc.alpha.speech.utils;

import com.ubtechinc.alpha.callback.message.speech.PcmCbMessage;
import com.ubtechinc.alpha.speech.AbstractSpeech;
import com.ubtechinc.alpha.speech.recoder.IPcmListener;

/**
 * @desc : pcm助手
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/28
 * @modifier:
 * @modify_time:
 */

public final class PcmUtil {
    private static final String TAG = "paul";
    private AbstractSpeech mSpeech;
    public volatile String mPackageName;

    public PcmUtil(AbstractSpeech speech) {
        this.mSpeech = speech;
    }

    public PcmUtil(AbstractSpeech speech, String packageName) {
        this.mSpeech = speech;
        this.mPackageName = packageName;
    }

    public IPcmListener getListener(){
        return mTtsListener;
    }

    private IPcmListener mTtsListener = new IPcmListener(){

        @Override
        public void onPcmData(byte[] bytes, int i) {
            if (mPackageName == null) return;
            PcmCbMessage cb = new PcmCbMessage(mSpeech,mPackageName);
            cb.setData(bytes);
            cb.setLength(i);
            mSpeech.getDispatch().enqueue(cb);
        }

    };
}
