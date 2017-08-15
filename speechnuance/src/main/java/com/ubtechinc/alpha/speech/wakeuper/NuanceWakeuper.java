package com.ubtechinc.alpha.speech.wakeuper;

import android.content.Context;

import com.nuance.dragon.toolkit.audio.AudioType;
import com.nuance.dragon.toolkit.file.FileManager;
import com.nuance.dragon.toolkit.vocon.VoconConfig;
import com.nuance.dragon.toolkit.vocon.VoconError;
import com.nuance.dragon.toolkit.vocon.VoconRecognizer;
import com.nuance.dragon.toolkit.vocon.VoconResult;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha.speech.recoder.PcmAudioSource;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @desc : nuance唤醒模块
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/30
 * @modifier:
 * @modify_time:
 */

public final class NuanceWakeuper implements IWakeuper {
    private static final String TAG = "NuanceWakeuper";
    private static final int LOCAL_GRAMMAR_SCORE_VALUE = 4500;
    private final VoconRecognizer mVoconRecognizer;
    //接收麦克风阵列的source;
    private PcmAudioSource mPcmAudioSource = new PcmAudioSource(AudioType.PCM_16k);
    private List<String> wakeupWords = new LinkedList<>();
    private volatile AtomicBoolean isWakeup = new AtomicBoolean(false);

    private volatile IWakeuperListener listener;

    private VoconRecognizer.ResultListener mInnerResultListener = new VoconRecognizer.ResultListener() {
        @Override
        public void onResult(VoconResult voconResult) {
            //主线程执行，参考nuance代码
            isWakeup.set(true);
            final String result = voconResult.getRawResult().toString();
            LogUtils.i(TAG, "nuance 唤醒成功：" + result);
            if (listener != null){
                listener.onWakeup(result, 0);
            }
        }

        @Override
        public void onError(final VoconError voconError) {
            isWakeup.set(false);
            reset();
            LogUtils.i(TAG, "nuance 唤醒失败："+voconError.getReason());
            if (listener != null){
                listener.onError(voconError.getReasonCode());
            }
        }
    };

    public NuanceWakeuper(Context context) {
        mVoconRecognizer = VoconRecognizer.createVoconRecognizer(new FileManager(context, ".jpg", "vocon", "vocon"));
        wakeupWords.add("hello alpha");
    }

    @Override
    public void init() {
        mVoconRecognizer.enableVerboseAndroidLogging(true);
        mVoconRecognizer.initialize(new VoconConfig("acmod5_4000_enu_gen_car_f16_v2_0_0.dat",
                "clc_enu_cfg3_v6_0_4.dat"), "wakeuper", new VoconRecognizer.InitializeListener() {
            @Override
            public void onLoaded(VoconRecognizer vocon, boolean success) {
                if (!success){
                    LogUtils.i(TAG,"nuance本地唤醒初始化失败");
                    return;
                }
                LogUtils.i(TAG,"nuance本地唤醒初始化成功");
            }
        });
    }

    @Override
    public void writeAudio(byte[] pcmData, int dataLen) {
        if (isWakeup.get()) {
            mPcmAudioSource.writeAudio(pcmData, dataLen);
        }else {
            if (listener != null){
                listener.onAudio(pcmData, dataLen, 0, 0);
            }
        }
    }

    @Override
    public void setWakeupListener(IWakeuperListener listener) {
        this.listener = listener;
        mVoconRecognizer.startWakeupMode(mPcmAudioSource, wakeupWords, LOCAL_GRAMMAR_SCORE_VALUE, mInnerResultListener);
    }

    @Override
    public void reset() {
        mVoconRecognizer.cancelRecognition();
        mVoconRecognizer.stopListening();
        mPcmAudioSource.clearChunks();
        mVoconRecognizer.startWakeupMode(mPcmAudioSource, wakeupWords, LOCAL_GRAMMAR_SCORE_VALUE, mInnerResultListener);
    }

    @Override
    public void destroy() {
        if (mPcmAudioSource != null){
            mPcmAudioSource = null;
        }
        if (mVoconRecognizer != null) {
            mVoconRecognizer.release();
        }
    }

    @Override
    public boolean isWakeup() {
        return true;
    }
}
