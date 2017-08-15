/*
 *
 *  *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.ubtechinc.alpha.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * @date 2016/12/27
 * @author paul.zhang@ubtrobot.com
 * @Description 抽象服务基类
 * @modifier
 * @modify_time
 */


public abstract class ServiceBindable extends Service {

	public static final String TAG = "paul";
	private boolean started;

	protected void onStartOnce(){
	}


	@Override
	public void onCreate() {
		super.onCreate();
		if (!this.started) {
			this.started = true;
			onStartOnce();
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
