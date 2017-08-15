package com.ubtechinc.alpha.behavior;

import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;

import com.ubtech.utilcode.utils.thread.HandlerUtils;
import com.ubtechinc.alpha.serverlibutil.aidl.ITtsCallBackListener;
import com.ubtechinc.alpha.serverlibutil.aidl.SpeechVoice;
import com.ubtechinc.alpha.speech.SpeechServiceProxy;

import timber.log.Timber;

/**
 * @desc :
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/5
 * @modifier:
 * @modify_time:
 */

public class TtsBehavior extends Behavior implements ITtsCallBackListener {
    private String text;
    // FIXME: 2017/8/8 参考IfytekConstants
    private String voicer = "nannan";
    private int speed = 64;
    private int volume = 80;
    private String mood;

    void setText(String text) {
        this.text = text;
    }

    void setVoicer(String voicer) {
        this.voicer = voicer;
    }

    void setSpeed(int speed) {
        this.speed = speed;
    }

    void setMood(String mood) {
        this.mood = mood;
    }

    void setVolume(int volume) {
        this.volume = volume;
    }

    private String preSpeed;
    private String preVoicer = null;
    private String preVolume;

    @Override
    protected boolean onStart() {
        if (TextUtils.isEmpty(text)) return false;
        try {
            preSpeed = SpeechServiceProxy.getInstance().impl().getTtsSpeed();
            preVolume = SpeechServiceProxy.getInstance().impl().getTtsVolume();
            SpeechVoice speechVoice = SpeechServiceProxy.getInstance().impl().getCurSpeechVoices();
            if (speechVoice != null)
            preVoicer = speechVoice.getName();
            SpeechServiceProxy.getInstance().impl().setTtsSpeed(speed + "");
            SpeechServiceProxy.getInstance().impl().setVoiceName(voicer);
            SpeechServiceProxy.getInstance().impl().setTtsVolume(volume + "");
            SpeechServiceProxy.getInstance().speechStartTTS(text, this);
        }catch (RemoteException e){
            e.printStackTrace();
            return false;
        }
        Timber.d(TAG, "TtsBehavior start: text= %s", text);
        return true;
    }

    @Override
    public void onBegin() throws RemoteException {

    }

    @Override
    public void onEnd() {
        HandlerUtils.runUITask(new Runnable() {
            @Override
            public void run() {
                try {
                    SpeechServiceProxy.getInstance().impl().setTtsVolume(preVolume);
                    SpeechServiceProxy.getInstance().impl().setTtsSpeed(preSpeed);
                    if (!TextUtils.isEmpty(preVoicer)){
                        SpeechServiceProxy.getInstance().impl().setVoiceName(preVoicer);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                resultCallback.onBehaviorResult(true);
            }
        });
    }

    @Override
    public IBinder asBinder() {
        return null;
    }
}
