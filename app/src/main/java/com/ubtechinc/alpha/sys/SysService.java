/*
 *
 *  *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.ubtechinc.alpha.sys;

import android.content.Intent;
import android.os.IBinder;

import com.ubtechinc.alpha.service.ServiceBindable;

/**
 * @date 2017/2/22
 * @author paul.zhang@ubtrobot.com
 * @Description 机器人系统服务、对外提供版本信息等接口
 * @modifier
 * @modify_time
 */

public class SysService extends ServiceBindable {
	@Override
	protected void onStartOnce() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		return SysServiceImpl.get(getApplicationContext());
	}
}
