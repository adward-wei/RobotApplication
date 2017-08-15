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
 * @date 2017/8/7
 * @author logic.peng@ubtrobot.com
 * @Description 语音引擎通用接口
 * @modifier  logic.peng
 * @modify_time
 */

public interface SpeechContext {

	void onCreate();

	void onDestroy();

	int registerPcmListener(String packageName, IPcmListener listener);

	int unregisterPcmListener(String packageName);

	int registerWakeUpCallbackListener(String packageName, IWakeuperListener callback);

	int unregisterWakeUpCallbackListener(String packageName);

	int onPlay(String packageName, String text, ITTSListener callback);
	void onStopPlay();

	void setVoiceName(String strVoiceName);

	void setTtsSpeed(String speed);

	String getTtsSpeed();

	void setTtsVolume(String volume);

	String getTtsVolume();

	void startSpeechAsr(String packageName, int appId, IRecognizerListener listener);
	void stopSpeechAsr();

	List<SpeechVoice> getSpeechVoices();

	SpeechVoice getCurSpeechVoices();

	/** 初始化语法识别 **/
	void initSpeechGrammar(String strGrammar, IGrammarListener listener);
	/** 开关唤醒 **/
	void switchWakeup(boolean enable);
	//语法识别模式
	boolean isSpeechGrammar();
	//语音听写模式
	boolean isSpeechIat();
	void setSpeechMode(AbstractSpeech.SpeechMode mode);
}
