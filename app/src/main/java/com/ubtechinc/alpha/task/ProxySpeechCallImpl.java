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

import com.ubtech.utilcode.utils.LogUtils;
import com.ubtech.utilcode.utils.notification.NotificationCenter;
import com.ubtech.utilcode.utils.notification.Subscriber;
import com.ubtechinc.alpha.event.CallEvent;


/**
 * @date 2017/2/28
 * @author paul.zhang@ubtrobot.com
 * @Description 通用语音指令业务处理 如打断词 停止等
 * @modifier
 * @modify_time
 */

public class ProxySpeechCallImpl extends AbstractProxyService{
	private Context mContext;

	public ProxySpeechCallImpl(Context context) {
		this.mContext = context;
	}

	@Override
	public void registerEvent() {
		NotificationCenter.defaultCenter().subscriber(CallEvent.class, mSubscriber);
	}

	@Override
	public void unregisterEvent() {
		NotificationCenter.defaultCenter().unsubscribe(CallEvent.class, mSubscriber);
	}

	protected Subscriber<CallEvent> mSubscriber = new Subscriber<CallEvent>() {

		@Override
		public void onEvent(CallEvent event) {
			LogUtils.i(TAG,"收到系统级别离线指令:%s",event.intent);
		}
	};

}
