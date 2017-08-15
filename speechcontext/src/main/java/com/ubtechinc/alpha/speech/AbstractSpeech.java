/*
 *
 *  *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.ubtechinc.alpha.speech;


import android.content.Context;

import com.ubtech.utilcode.utils.notification.NotificationCenter;
import com.ubtechinc.alpha.callback.DispatchMessage;
import com.ubtechinc.alpha.callback.MessageQueue;
import com.ubtechinc.alpha.event.ErrorEvent;
import com.ubtechinc.alpha.event.SpeechDispatchEvent;
import com.ubtechinc.alpha.event.WakeUpTaskEvent;
import com.ubtechinc.alpha.speech.dispatch.Slot;
import com.ubtechinc.alpha.speech.recoder.IPcmListener;
import com.ubtechinc.alpha.speech.utils.IRecUtil;
import com.ubtechinc.alpha.speech.wakeuper.IWakeuperListener;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @date 2016/12/28
 * @author paul.zhang@ubtrobot.com
 * @Description 语音识别逻辑处理抽象基类
 * @modifier logic.peng
 * @modify_time
 */

public abstract class AbstractSpeech {
	//语音环境状态
	public enum SpeechState {
		ASR,//语音识别
		TTS,//语音合成
		WKU,//唤醒
		RECO,//待录音中
		IDLE,//空闲
	}
	//语音模式
	public enum SpeechMode {
		GRAMMAR(1),//语法识别
		IAT(0);//语音听写
		public final int mode;
		SpeechMode(int mode) {
			this.mode = mode;
		}
		public static SpeechMode valueOf(int value){
			switch (value){
				case 0:
					return IAT;
				case 1:
					return GRAMMAR;
				default:
					return null;
			}
		}
	}

	private DispatchMessage mDispatchMessage;
	protected volatile SpeechState state;
	protected volatile SpeechMode mode;

	public AbstractSpeech() {
		mDispatchMessage = new DispatchMessage( MessageQueue.newQueue());
	}

	public abstract void startSpeechAsr();

	public abstract void stopSpeechAsr();

	public abstract ConcurrentHashMap<String,IPcmListener> getPcmCallback();

	public abstract ConcurrentHashMap<String,IWakeuperListener> getWkuCallback();

	abstract public Context getContext();

	abstract protected IRecUtil getRecUtil();

	public SpeechState getState() {
		return state;
	}

	public SpeechMode getMode() {
		return mode;
	}

	/**
	 * @Description 获取回调处理池对象
	 * @param
	 * @return
	 * @throws
	 */
	public DispatchMessage getDispatch(){
		return mDispatchMessage;
	}

	/**
	 * @Description 发布语音指令
	 * @param slot 讯飞槽
	 * @return
	 * @throws
	 */
	public void notifyEvent(Slot slot){
		SpeechDispatchEvent event = new SpeechDispatchEvent();
		event.slot = slot;
		NotificationCenter.defaultCenter().publish(event);
	}
	/**
	 * @Description 语音识别完成后通知机器人转身
	 * @param angle 唤醒角度
	 * @return
	 * @throws
	 */

	public void notifyEvent(int angle) {
		WakeUpTaskEvent event = new WakeUpTaskEvent();
		event.angle = angle;
		NotificationCenter.defaultCenter().publish(event);
	}
	/**
	 * @Description 发布语音指令
	 * @param appId 应用唯一标识 content 语音内容
	 * @return
	 * @throws
	 */
	public void notifySlot(int appId,String content){
		Slot s = new Slot();
		s.setAppId(appId);
		s.setContent(content);
		notifyEvent(s);
	}

	/**
	 * @Description 发布语音指令
	 * @param slot 应用唯一标识 content 语音内容
	 * @return
	 * @throws
	 */
	public void notifySlot(Slot slot){
		notifyEvent(slot);
	}

	/**
	 * @Description 通知错误处理任务
	 * @param error 错误原因
	 * @return
	 * @throws
	 */

	public void notifyErrorEvent(int error) {
		ErrorEvent event = new ErrorEvent();
		event.error = error;
		NotificationCenter.defaultCenter().publish(event);
	}
}
