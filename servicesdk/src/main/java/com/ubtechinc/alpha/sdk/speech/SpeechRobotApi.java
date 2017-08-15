/*
 *
 *  *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */
package com.ubtechinc.alpha.sdk.speech;

import android.content.Context;

import com.ubtechinc.alpha.sdk.SdkConstants;
import com.ubtechinc.alpha.serverlibutil.aidl.SpeechVoice;
import com.ubtechinc.alpha.serverlibutil.interfaces.PcmListener;
import com.ubtechinc.alpha.serverlibutil.interfaces.SpeechASRListener;
import com.ubtechinc.alpha.serverlibutil.interfaces.SpeechInitGrammarListener;
import com.ubtechinc.alpha.serverlibutil.interfaces.SpeechTtsListener;
import com.ubtechinc.alpha.serverlibutil.interfaces.SpeechWakeUpListener;
import com.ubtechinc.alpha.serverlibutil.service.SpeechMainServiceUtil;

import java.util.List;


/**
 * @author paul.zhang@ubtrobot.com
 * @date 2016/11/5
 * @Description 单例模式、对外提供语音TTS等api
 * @modifier  logic.peng
 * @modify_time 2017/4/7 增加唤醒回调api
 */
public class SpeechRobotApi {
	private SpeechMainServiceUtil mSpeechServiceUtil;
	private static volatile SpeechRobotApi mSpeechRobotApi;
	private Context mContext;

	public static SpeechRobotApi get() {
		if (mSpeechRobotApi == null) {
			synchronized (SpeechRobotApi.class) {
				if (mSpeechRobotApi == null) {
					mSpeechRobotApi = new SpeechRobotApi();
				}
			}
		}
		return mSpeechRobotApi;
	}

	public synchronized SpeechRobotApi initializ(Context context) {
		if (mSpeechServiceUtil == null) {
			mSpeechServiceUtil = new SpeechMainServiceUtil(context);
		}
		return get();
	}

	public SpeechRobotApi initializ(final Context context, int appId) {
		this.initializ(context);
		SpeechUtil.getInstance(context).initialize(appId, new SpeechUtil.ISpeechBindListener() {
			@Override
			public void onBindSpeech() {}
		});
		return get();
	}

//	public void registerSpeech(ISpeechContext isc) {
//		SpeechUtil.getInstance(mContext).registerSpeech(isc);
//	}

	public synchronized void destroy() {
		unregisterPcmListener();
		unregisterWkuListener();
		mSpeechServiceUtil = null;
	}

	public int registerPcmListener(PcmListener listener){
		int ret = SdkConstants.ErrorCode.RESULT_SUCCESS;
		if (mSpeechServiceUtil == null) {
			ret = SdkConstants.ErrorCode.RESULT_FAIL;
			return ret;
		}
		if (mSpeechServiceUtil != null) {
			mSpeechServiceUtil.registerPcmListener(listener);
		}
		return ret;
	}

	public int unregisterPcmListener(){
		int ret = SdkConstants.ErrorCode.RESULT_SUCCESS;
		if (mSpeechServiceUtil == null) {
			ret = SdkConstants.ErrorCode.RESULT_FAIL;
			return ret;
		}
		if (mSpeechServiceUtil != null) {
			mSpeechServiceUtil.unregisterPcmListener();
		}
		return ret;
	}

	public int registerWakeUpListener(SpeechWakeUpListener listener){
		if (null == mSpeechServiceUtil){
			return SdkConstants.ErrorCode.RESULT_FAIL;
		}
		mSpeechServiceUtil.registerWakeUpCallback(listener);
		return SdkConstants.ErrorCode.RESULT_SUCCESS;
	}

	private int unregisterWkuListener() {
		int ret = SdkConstants.ErrorCode.RESULT_SUCCESS;
		if (mSpeechServiceUtil == null) {
			ret = SdkConstants.ErrorCode.RESULT_FAIL;
			return ret;
		}
		if (mSpeechServiceUtil != null) {
			mSpeechServiceUtil.unregisterWakeUpCallback();
		}
		return ret;
	}

	public void switchSpeechCore(String language) {
		if (mSpeechServiceUtil != null) {
			mSpeechServiceUtil.switchSpeechCore(language);
		}
	}

	public int switchWakeup(boolean enable){
		int ret = SdkConstants.ErrorCode.RESULT_SUCCESS;
		if (mSpeechServiceUtil == null) {
			ret = SdkConstants.ErrorCode.RESULT_FAIL;
			return ret;
		}
		if (mSpeechServiceUtil != null) {
			mSpeechServiceUtil.switchWakeup(enable);
		}
		return ret;
	}


	public int speechSetVoiceName(String strVoiceName) {
		int nState = SdkConstants.ErrorCode.RESULT_SUCCESS;
		if (mSpeechServiceUtil == null) {
			nState = SdkConstants.ErrorCode.RESULT_FAIL;
			return nState;
		}
		mSpeechServiceUtil.setVoiceName(strVoiceName);
		return nState;
	}

	public int speechSetTtsSpeed(int speed){
		int nState = SdkConstants.ErrorCode.RESULT_SUCCESS;
		if (mSpeechServiceUtil == null) {
			nState = SdkConstants.ErrorCode.RESULT_FAIL;
			return nState;
		}
		mSpeechServiceUtil.setTtsSpeed(speed);
		return nState;
	}

	public int speechSetTtsVolume(int volume){
		int nState = SdkConstants.ErrorCode.RESULT_SUCCESS;
		if (mSpeechServiceUtil == null) {
			nState = SdkConstants.ErrorCode.RESULT_FAIL;
			return nState;
		}
		mSpeechServiceUtil.setTtsVolume(volume);
		return nState;
	}

	public int speechStartTTS(String text, SpeechTtsListener listener) {
		int nState = SdkConstants.ErrorCode.RESULT_SUCCESS;
		if (mSpeechServiceUtil == null) {
			nState = SdkConstants.ErrorCode.RESULT_FAIL;
			return nState;
		}
		mSpeechServiceUtil.onPlay(text, listener);
		return nState;
	}

	public int speechStartTTS(String text, int speed, SpeechTtsListener listener) {
		int nState = SdkConstants.ErrorCode.RESULT_SUCCESS;
		if (mSpeechServiceUtil == null) {
			nState = SdkConstants.ErrorCode.RESULT_FAIL;
			return nState;
		}
		speechSetTtsSpeed(speed);
		mSpeechServiceUtil.onPlay(text, listener);
		return nState;
	}

	public int speechStartTTS(String text, String strVoicName, SpeechTtsListener listener) {
		int nState = SdkConstants.ErrorCode.RESULT_SUCCESS;
		if (mSpeechServiceUtil == null) {
			nState = SdkConstants.ErrorCode.RESULT_FAIL;
			return nState;
		}
		speechSetVoiceName(strVoicName);
		mSpeechServiceUtil.onPlay(text, listener);
		return nState;
	}

	public int speechStartTTS(String text, int speed, String strVoicName, SpeechTtsListener listener) {
		int nState = SdkConstants.ErrorCode.RESULT_SUCCESS;
		if (mSpeechServiceUtil == null) {
			nState = SdkConstants.ErrorCode.RESULT_FAIL;
			return nState;
		}
		speechSetTtsSpeed(speed);
		speechSetVoiceName(strVoicName);
		mSpeechServiceUtil.onPlay(text, listener);
		return nState;
	}

	public int speechStopTTS() {
		int nState = SdkConstants.ErrorCode.RESULT_SUCCESS;
		if (mSpeechServiceUtil == null) {
			nState = SdkConstants.ErrorCode.RESULT_FAIL;
			return nState;
		}
		mSpeechServiceUtil.onStopPlay();
		return nState;
	}

	//初始化语法文件
	public int speechInitGrammar(String grammar,SpeechInitGrammarListener listener){
		int ret = SdkConstants.ErrorCode.RESULT_SUCCESS;
		if (mSpeechServiceUtil == null) {
			ret = SdkConstants.ErrorCode.RESULT_FAIL;
			return ret;
		}
		if (mSpeechServiceUtil != null) {
			mSpeechServiceUtil.initGrammar(grammar,listener);
		}
		return ret;
	}

	public int startSpeechASR(int appId, SpeechASRListener listener) {
		if (mSpeechServiceUtil == null) {
			return SdkConstants.ErrorCode.RESULT_FAIL;
		}
		mSpeechServiceUtil.startSpeechAsr(appId, listener);
		return SdkConstants.ErrorCode.RESULT_SUCCESS;
	}

	public int stopSpeechASR() {
		if (mSpeechServiceUtil == null) {
			return SdkConstants.ErrorCode.RESULT_FAIL;
		}
		mSpeechServiceUtil.stopSpeechAsr();
		return SdkConstants.ErrorCode.RESULT_SUCCESS;
	}

	public List<SpeechVoice> getSpeechVoices() {
		if (mSpeechServiceUtil == null) {
			return null;
		}
		return mSpeechServiceUtil.getSpeechVoices();

	}

	public SpeechVoice getCurSpeechVoices() {
		if (mSpeechServiceUtil == null) {
			return null;
		}
		return mSpeechServiceUtil.getCurSpeechVoice();
	}

	public void startLocalFunction(String function){
		if (mSpeechServiceUtil == null){
			return;
		}
		mSpeechServiceUtil.startLocalFunction(function);
	}

	public boolean isSpeechGrammar(){
		return mSpeechServiceUtil != null && mSpeechServiceUtil.isSpeechGrammar();
	}

	public boolean isSpeechAsr(){
		return mSpeechServiceUtil != null && mSpeechServiceUtil.isSpeechIat();
	}

    public void setSpeechMode(int mode) {
		if (mSpeechServiceUtil == null){
			return;
		}
		mSpeechServiceUtil.setSpeechMode(mode);
    }
}

