/*
 *
 *  *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.ubtechinc.alpha.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha.utils.ServiceUtils;

/**
 * @date 2016/12/27
 * @author paul.zhang@ubtrobot.com
 * @Description 开机启动广播
 * @modifier
 * @modify_time
 */

public class BootReceiver extends BroadcastReceiver {

	public static final String TAG = "BootReceiver";
	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
			LogUtils.i(TAG,"收到开机自启动广播...");
			ServiceUtils.startService(context);
		}
	}
}
