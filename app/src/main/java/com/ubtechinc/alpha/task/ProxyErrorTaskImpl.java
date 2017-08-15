/*
 *
 *  *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *  
 */

package com.ubtechinc.alpha.task;

import android.content.Context;

import com.ubtech.utilcode.utils.notification.NotificationCenter;
import com.ubtech.utilcode.utils.notification.Subscriber;
import com.ubtechinc.alpha.event.ErrorEvent;
import com.ubtechinc.alpha.event.ForbiddenChargePlayingEvent;

/**
 * @date 2017/2/28
 * @author paul.zhang@ubtrobot.com
 * @Description 用于处理错误机制任务
 * @modifier
 * @modify_time
 */

public class ProxyErrorTaskImpl extends AbstractProxyService{
	private Context mContext;

	public ProxyErrorTaskImpl(Context context) {
		this.mContext = context;
	}

	@Override
	public void registerEvent() {
		NotificationCenter.defaultCenter().subscriber(ErrorEvent.class, mSubscriber);
		NotificationCenter.defaultCenter().subscriber(ForbiddenChargePlayingEvent.class, mForbiddenChargePlayingSubscriber);
	}

	@Override
	public void unregisterEvent() {
		NotificationCenter.defaultCenter().unsubscribe(ErrorEvent.class, mSubscriber);
	}

	protected Subscriber<ErrorEvent> mSubscriber = new Subscriber<ErrorEvent>() {

		@Override
		public void onEvent(ErrorEvent event) {
			int error = event.error;
			switch (error) {
				case ErrorEvent.ERROR_SPEECH_ERROR:
					//暂时采用噗音其它不做处理
					//int value = SysUtils.randomSet(0,3);
					//SpeechRobotApi.get().speechSetVoiceName(SpeechConstant.IflySpeech.XIAO_HONG);
					//SpeechRobotApi.get().speechStartTTS(mContext.getString(R.string.action_wakeup_0 + value));
					break;
			}
		}
	};

	private Subscriber<ForbiddenChargePlayingEvent> mForbiddenChargePlayingSubscriber = new Subscriber<ForbiddenChargePlayingEvent>() {
		@Override
		public void onEvent(ForbiddenChargePlayingEvent forbiddenChargePlayingEvent) {

		}
	};

}
