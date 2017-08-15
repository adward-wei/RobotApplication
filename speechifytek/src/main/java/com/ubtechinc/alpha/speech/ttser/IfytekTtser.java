package com.ubtechinc.alpha.speech.ttser;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.Setting;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.util.ResourceUtil;
import com.iflytek.msc.MSC;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha.speech.ifytek.IfytekConstants;


/**
 * @desc : 科大讯飞tts模块
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/29
 * @modifier:
 * @modify_time:
 */

public final class IfytekTtser implements ITtser, SynthesizerListener {
    private static final String TAG = "IfytekTtser";
    private final Context mContext;
    private final SpeechSynthesizer mTts;
    private ITTSListener listener;

    private String mVoiceName;
    private String mTtsSpeed;
    private String mTtsVolume;
    private String mTtsLanguage;
    private String mTtsPitch;

    public IfytekTtser(Context mContext) {
        this.mContext = mContext;
        MSC.DebugLog(false);
        Setting.setShowLog(false);
        this.mTts = SpeechSynthesizer.createSynthesizer(mContext, new InitListener() {
            @Override
            public void onInit(int code) {
                LogUtils.i(TAG, "科大讯飞TTS 初始化返回码：" + code);
            }
        });
    }

    @Override
    public void init() {
        //设置语音播报参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 设置使用本地引擎
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
        setTtsVoicer(IfytekConstants.VOICENAME_NANA);
        setTtsLanguage(IfytekConstants.DEFAULT_TTS_LANGUAGE);
        setTtsSpeed(IfytekConstants.DEFAULT_TTS_SPEED);
        setTtsPitch(IfytekConstants.DEFAULT_TTS_PITCH);
        setTtsVolume(IfytekConstants.DEFAULT_TTS_VOLUME);
        // 设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "false");
    }

    @Override
    public void startSpeaking(String text, ITTSListener listener) {
        this.listener = listener;
        int code = mTts.startSpeaking(text, this);
        if (code != ErrorCode.SUCCESS) {
            LogUtils.i(TAG,"科大讯飞语音合成失败,错误码:" + code);
        }
    }

    @Override
    public void stopSpeaking() {
        mTts.stopSpeaking();
    }

    @Override
    public boolean isSpeaking() {
        return mTts.isSpeaking();
    }

    @Override
    public boolean destroy() {
        return mTts.destroy();
    }

    @Override
    public void setTtsSpeed(String speed) {
        if (TextUtils.isEmpty(speed) || speed.equals(mTtsSpeed)) return;
        mTtsSpeed = speed;
        mTts.setParameter(SpeechConstant.SPEED, mTtsSpeed);
    }

    @Override
    public String getTtsSpeed() {
        return mTtsSpeed == null?mTts.getParameter(SpeechConstant.SPEED):mTtsSpeed;
    }

    @Override
    public void setTtsPitch(String pitch) {
        if (TextUtils.isEmpty(pitch) || pitch.equals(mTtsPitch)) return;
        mTtsPitch = pitch;
        mTts.setParameter(SpeechConstant.PITCH, mTtsPitch);
    }

    @Override
    public String getTtsPitch() {
        return mTtsPitch == null? mTts.getParameter(SpeechConstant.PITCH):mTtsPitch;
    }

    @Override
    public void setTtsVolume(String volume) {
        if (TextUtils.isEmpty(volume) || volume.equals(mTtsVolume)) return;
        mTtsVolume = volume;
        mTts.setParameter(SpeechConstant.VOLUME, volume);
    }

    @Override
    public String getTtsVolume() {
        return mTtsVolume == null? mTts.getParameter(SpeechConstant.VOLUME):mTtsVolume;
    }

    @Override
    public void setTtsLanguage(String language) {
        if (TextUtils.isEmpty(language) || language.equals(mTtsLanguage)) return;
        mTtsLanguage = language;
        mTts.setParameter(SpeechConstant.LANGUAGE, language);
        if (language.equals(IfytekConstants.DEFAULT_TTS_LANGUAGE)){
            // 设置语言区域
            mTts.setParameter(SpeechConstant.ACCENT, "mandarin");//普通话
        }else {
            mTts.setParameter(SpeechConstant.ACCENT, "");
        }
    }

    @Override
    public String getTtsLanguage() {
        return mTtsLanguage == null? mTts.getParameter(SpeechConstant.LANGUAGE):mTtsLanguage;
    }

    @Override
    public void setTtsVoicer(String voicer) {
        if (TextUtils.isEmpty(voicer) || voicer.equals(mVoiceName)) return;
        mVoiceName = voicer;
        mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
        // 设置发音人资源路径
        mTts.setParameter(ResourceUtil.TTS_RES_PATH, getTTSResourcePath(mVoiceName));
    }

    @Override
    public String getTtsVoicer() {
        return mVoiceName == null? mTts.getParameter(SpeechConstant.VOICE_NAME): mVoiceName;
    }

    // 获取发音人资源路径
    private String getTTSResourcePath(String strVoiceName) {
        String tempBuffer = ResourceUtil.generateResourcePath(mContext, ResourceUtil.RESOURCE_TYPE.assets, "tts/common.jet") + ";" +
                ResourceUtil.generateResourcePath(mContext, ResourceUtil.RESOURCE_TYPE.assets, "tts/" + strVoiceName + ".jet");
        return tempBuffer;
    }

    @Override
    public void onSpeakBegin() {
        LogUtils.i(TAG, "科大讯飞语音合成开始... ");
        if (listener != null)
            listener.onTtsBegin();
    }

    @Override
    public void onBufferProgress(int i, int i1, int i2, String s) {
    }

    @Override
    public void onSpeakPaused() {
    }

    @Override
    public void onSpeakResumed() {
    }

    @Override
    public void onSpeakProgress(int i, int i1, int i2) {
    }

    @Override
    public void onCompleted(SpeechError speechError) {
        LogUtils.i(TAG, "科大讯飞语音合成完成..."+
                (speechError == null? "": "error:"+speechError.getErrorDescription()));
        if (listener != null)
            listener.onTtsCompleted(speechError == null? 0 : speechError.getErrorCode());
    }

    @Override
    public void onEvent(int code, int i1, int i2, Bundle bundle) {
        if (listener != null && code == 21002){
            listener.onTtsCompleted(code);
        }
    }
}
