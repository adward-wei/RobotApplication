/*
 *
 *  *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.ubtechinc.alpha.speech;

import com.ubtechinc.alpha.speech.recoder.IPcmListener;
import com.ubtechinc.alpha.speech.recognizer.IGrammarListener;
import com.ubtechinc.alpha.speech.recognizer.IRecognizerListener;
import com.ubtechinc.alpha.speech.ttser.ITTSListener;
import com.ubtechinc.alpha.speech.wakeuper.IWakeuperListener;

/**
 * @date 2017/1/12
 * @author paul.zhang@ubtrobot.com
 * @Description 语音引擎核心实现接口
 * @modifier  logic.peng
 * @modify_time 2017/8/9
 */

public interface SpeechUtil {
	/**
	 * @param
	 * @return
	 * @throws
	 * @Description 初始化语音引擎资源
	 */
	void init();
	/**
	 * @param
	 * @return
	 * @throws
	 * @Description 设定引擎参数
	 */
	void initParam();

	/**
	 * @param
	 * @return
	 * @throws
	 * @Description 释放语音引擎全部资源
	 */
	void release();

	/**
	 * @description 构建离线语法
	 * @param grammarStr
	 * @param listener
	 */
	void buildGrammar(String grammarStr, IGrammarListener listener);

	/**
	 * @param text 播报内容
	 * @return
	 * @throws
	 * @Description TTS开始播报
	 */
	void startTts(String text, ITTSListener listener);

	/**
	 * @param
	 * @return
	 * @throws
	 * @Description TTS停止播报
	 */
	void stopTts();

	/**
	 * @param listener 识别回调接口
	 * @return
	 * @throws
	 * @Description 开始语音识别
	 */
	void startRecognize(IRecognizerListener listener);

	/**
	 * @param
	 * @return
	 * @throws
	 * @Description 停止语音识别
	 */
	void stopRecognize();

	/**
	 * @param
	 * @return
	 * @throws
	 * @Description 进入唤醒模式
	 */
	void enterWakeUp(IWakeuperListener listener);

	/**
	 * @param
	 * @return
	 * @throws
	 * @Description 退出唤醒模式
	 */
	void exitWakeUp();

	/**
	 * @param
	 * @return
	 * @throws
	 * @Description 录音模式
	 */
	void enterRecoding(IPcmListener listener);

	/**
	 * @param
	 * @return
	 * @throws
	 * @Description 录音停止
	 */
	void exitRecoding();

	/**
	 * @param
	 * @return
	 * @throws
	 * @Description 设定发音人名字
	 */
	void setVoiceName(String voiceName);
	/**
	 * @param
	 * @return
	 * @throws
	 * @Description 设定发音人语速
	 */
	void setTtsSpeed(String speed);

	/**
	 *
	 * @return
	 * @Description 获取发音人语速
	 */
	String getTtsSpeed();

	/**
	 * @param
	 * @return
	 * @throws
	 * @Description 设定发音人音量
	 */
	void setTtsVolume(String volume);

	/**
	 *
	 * @return
	 * @Description 获取发音人音量
	 */
	String getTtsVolume();

	/**
	 * @param
	 * @return
	 * @throws
	 * @Description 获取当前TTS发音人信息
	 */
	String getCurVoiceName();

}
