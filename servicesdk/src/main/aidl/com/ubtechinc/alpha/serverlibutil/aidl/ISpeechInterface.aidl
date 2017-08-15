package com.ubtechinc.alpha.serverlibutil.aidl;

import com.ubtechinc.alpha.serverlibutil.aidl.ITtsCallBackListener;
import com.ubtechinc.alpha.serverlibutil.aidl.ISpeechAsrListener;
import com.ubtechinc.alpha.serverlibutil.aidl.ISpeechWakeUpListener;
import com.ubtechinc.alpha.serverlibutil.aidl.SpeechVoice;
import com.ubtechinc.alpha.serverlibutil.aidl.ISpeechGrammarInitListener;
import com.ubtechinc.alpha.serverlibutil.aidl.IPcmListener;

interface ISpeechInterface{
    int registerPcmListener(String packageName,IPcmListener callBack);
    int unregisterPcmListener(String packageName);
    int registerWakeUpCallbackListener(String packageName, ISpeechWakeUpListener callback);
    int unregisterWakeUpCallbackListener(String packageName);
    int onPlayCallback(String packageName,String text,ITtsCallBackListener callback);
    void onStopPlay();
    void setVoiceName(String strVoiceName);
    void setTtsSpeed(String speed);
    String getTtsSpeed();
    void setTtsVolume(String volume);
    String getTtsVolume();
    void startSpeechAsr(String packageName,int appId,ISpeechAsrListener callBack);
    void stopSpeechAsr();
    List getSpeechVoices();
    SpeechVoice getCurSpeechVoices();
    /** 初始化语法识别*/
    void initSpeechGrammar(String strGrammar, ISpeechGrammarInitListener listener);
    /** 语音引擎切换 **/
    void switchSpeechCore(String language);
    /**开关cae语音唤醒*/
    void switchWakeup(boolean enable);
    void startLocalFunction(String function);
    boolean isSpeechGrammar();
    boolean isSpeechIat();
    //0-听写 1-语法识别
    void setSpeechMode(int mode);
}