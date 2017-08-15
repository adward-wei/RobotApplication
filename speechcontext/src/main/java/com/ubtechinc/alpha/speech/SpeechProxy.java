/*
 *
 *  *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.ubtechinc.alpha.speech;

import com.ubtechinc.alpha.serverlibutil.aidl.SpeechVoice;
import com.ubtechinc.alpha.speech.recoder.IPcmListener;
import com.ubtechinc.alpha.speech.recognizer.IGrammarListener;
import com.ubtechinc.alpha.speech.recognizer.IRecognizerListener;
import com.ubtechinc.alpha.speech.ttser.ITTSListener;
import com.ubtechinc.alpha.speech.wakeuper.IWakeuperListener;

import java.util.List;

/**
 * @date 2016/12/27
 * @author paul.zhang@ubtrobot.com
 * @Description 语音引擎代理类、支持多语言、多引擎切换解耦
 * @modifier  logic.peng
 * @modify_time 2017/4/7 增加唤醒回调api
 */

public class SpeechProxy implements SpeechContext {

	private SpeechContext mSpeechInterface;

	public SpeechProxy(SpeechContext speech) {
		this.mSpeechInterface = speech;
	}

	@Override
	public void onCreate() {
		mSpeechInterface.onCreate();
	}

	@Override
	public void onDestroy() {
		mSpeechInterface.onDestroy();
	}

	@Override
	public int registerPcmListener(String packageName, IPcmListener listener) {
		return mSpeechInterface.registerPcmListener(packageName, listener);
	}

	@Override
	public int unregisterPcmListener(String packageName) {
		return mSpeechInterface.unregisterPcmListener(packageName);
	}

	@Override
    public int registerWakeUpCallbackListener(String packageName, IWakeuperListener callback) {
        return mSpeechInterface.registerWakeUpCallbackListener(packageName, callback);
    }

	@Override
	public int unregisterWakeUpCallbackListener(String packageName) {
		return mSpeechInterface.unregisterWakeUpCallbackListener(packageName);
	}

	@Override
	public int onPlay(String packageName, String text, ITTSListener callback) {
		return mSpeechInterface.onPlay(packageName, text, callback);
	}

	@Override
	public void onStopPlay() {
		mSpeechInterface.onStopPlay();
	}

	@Override
	public void setVoiceName(String strVoiceName) {
		mSpeechInterface.setVoiceName(strVoiceName);
	}

	@Override
	public void setTtsSpeed(String speed) {
		mSpeechInterface.setTtsSpeed(speed);
	}

	@Override
	public String getTtsSpeed() {
		return mSpeechInterface.getTtsSpeed();
	}

	@Override
	public void setTtsVolume(String volume) {
		mSpeechInterface.setTtsVolume(volume);
	}

	@Override
	public String getTtsVolume() {
		return mSpeechInterface.getTtsVolume();
	}

	@Override
	public void startSpeechAsr(String packageName, int appId, IRecognizerListener callBack) {
		mSpeechInterface.startSpeechAsr(packageName,appId, callBack);
	}

	@Override
	public void stopSpeechAsr() {
		mSpeechInterface.stopSpeechAsr();
	}

	@Override
	public List<SpeechVoice> getSpeechVoices() {
		return mSpeechInterface.getSpeechVoices();
	}

	@Override
	public SpeechVoice getCurSpeechVoices() {
		return mSpeechInterface.getCurSpeechVoices();
	}

	@Override
	public void initSpeechGrammar(String strGrammar, IGrammarListener listener) {
		mSpeechInterface.initSpeechGrammar(strGrammar,listener);
	}

	@Override
	public void switchWakeup(boolean enable) {
		mSpeechInterface.switchWakeup(enable);
	}

	@Override
	public boolean isSpeechGrammar() {
		return mSpeechInterface.isSpeechGrammar();
	}

	@Override
	public boolean isSpeechIat() {
		return mSpeechInterface.isSpeechIat();
	}

	@Override
	public void setSpeechMode(AbstractSpeech.SpeechMode mode) {
		mSpeechInterface.setSpeechMode(mode);
	}

	public void removeAsrListener(){
		if (mSpeechInterface instanceof SpeechContextBase){
			((SpeechContextBase)mSpeechInterface).getRecUtil().setListener(null);
		}
	}
}
