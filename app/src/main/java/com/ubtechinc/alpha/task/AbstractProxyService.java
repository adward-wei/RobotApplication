/*
 *
 *  *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.ubtechinc.alpha.task;
/**
 * @date 2016/10/17
 * @author paul.zhang@ubtrobot.com
 * @Description 抽象任务基类、解耦处理主要依赖于事件方式
 * @modifier
 * @modify_time
 */

public abstract class AbstractProxyService implements ProxyService {
	public static final String TAG = "paul";

	@Override
	public void onCreate() {
		registerEvent();
	}

	@Override
	public void onDestroy() {
		unregisterEvent();
	}

	@Override
	public void registerEvent() {
	}

	@Override
	public void unregisterEvent() {
	}
}
