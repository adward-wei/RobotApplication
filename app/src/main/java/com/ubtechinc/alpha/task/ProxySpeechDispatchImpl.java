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
import com.ubtechinc.alpha.event.SpeechDispatchEvent;
import com.ubtechinc.alpha.speech.dispatch.SpeechDispatcher;

/**
 * @author paul.zhang@ubtrobot.com
 * @date 2016/12/28
 * @Description 通过注册事件观察、语音指令分发任务处理
 * @modifier
 * @modify_time
 */

public class ProxySpeechDispatchImpl extends AbstractProxyService {

	private Context mContext;

	public ProxySpeechDispatchImpl(Context context) {
		this.mContext = context;
	}

	@Override
	public void registerEvent() {
		NotificationCenter.defaultCenter().subscriber(SpeechDispatchEvent.class, mSubscriber);
	}

	@Override
	public void unregisterEvent() {
		NotificationCenter.defaultCenter().unsubscribe(SpeechDispatchEvent.class, mSubscriber);
	}

	protected Subscriber<SpeechDispatchEvent> mSubscriber = new Subscriber<SpeechDispatchEvent>() {

		@Override
		public void onEvent(SpeechDispatchEvent event) {
			if(event.slot != null) {
				SpeechDispatcher.getInstance(mContext).sendSpeechRstToClient(event.slot);
			}
		}
	};

}
