package com.ubt.alpha2.upgrade.utils;

import android.content.Context;

import com.ubt.alpha2.upgrade.R;
import com.ubt.alpha2.upgrade.UpgradeApplication;
import com.ubt.alpha2.upgrade.impl.IUpgradeChooseListener;
import com.ubtechinc.alpha.sdk.speech.SpeechRobotApi;
import com.ubtechinc.alpha.serverlibutil.interfaces.SpeechASRListener;
import com.ubtechinc.alpha.serverlibutil.interfaces.SpeechTtsListener;
import com.ubtechinc.alpha.serverlibutil.interfaces.SpeechWakeUpListener;

/**
 * Created by ubt on 2017/7/12.
 */

public class UpgradeOptionsUtils {

    public static void upgradeChooseOptions(String tts, final IUpgradeChooseListener listener){
        final Context mc = UpgradeApplication.getContext();
        final SpeechRobotApi speechRobotApi = SpeechRobotApi.get().initializ(mc);
        speechRobotApi.switchWakeup(true);
        speechRobotApi.speechStartTTS(tts, new SpeechTtsListener() {
            @Override
            public void onEnd() {
                LogUtils.d("tts end");
                speechRobotApi.registerWakeUpListener(new SpeechWakeUpListener() {
                    @Override
                    public void onSuccess() {
                        LogUtils.d("WakeUp success");
                        speechRobotApi.speechStartTTS(mc.getString(R.string.upgrade_tts_tips), new SpeechTtsListener() {
                            @Override
                            public void onEnd() {
                                LogUtils.d("tts end");
                                startASRTest(listener);
                            }
                        });
                    }

                    @Override
                    public void onError(int errCode, String errDes) {
                        LogUtils.d("WakeUp failed");
                    }
                });
            }
        });
    }

    private static void startASRTest(final IUpgradeChooseListener listener){
        Context mContext = UpgradeApplication.getContext();
        final SpeechRobotApi speechRobotApi = SpeechRobotApi.get().initializ(mContext);
        speechRobotApi.setSpeechMode(0);
        speechRobotApi.switchSpeechCore("cn");
        speechRobotApi.startSpeechASR(9527, new SpeechASRListener() {
            @Override
            public void onBegin() {
                LogUtils.d("onBegin");
            }

            @Override
            public void onEnd() {
                LogUtils.d("onEnd");
            }

            @Override
            public void onResult(String text) {
                LogUtils.d("onResult: "+text);
                if(listener != null){
                    listener.onChooseResult(true);
                }
            }

            @Override
            public void onError(int code) {
                LogUtils.d("onError: "+code);
                if(listener != null){
                    listener.onChooseResult(true);
                }
            }
        });
    }
}
