/*
 *
 *  *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.ubtechinc.alpha.app;


import android.content.Context;

/**
 * @author paul.zhang@ubtrobot.com
 * @date 2016/8/3
 * @Description
 * @modifier
 * @modify_time
 */

public abstract class ServiceContext {
	protected static ServiceContext _instance = null;
	Context mContext;

	public static ServiceContext getInstance() {
		return _instance;
	}

	public void resetContext() {

	}

	public ServiceContext(Context context) {
		this.mContext = context;
	}

	public Context getApplicationContext() {
		return mContext;
	}

	public abstract void registerSystemObject(String name, Object obj);

	public abstract Object getSystemObject(String name);
}
