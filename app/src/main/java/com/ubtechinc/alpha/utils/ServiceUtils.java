/*
 *
 *  *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.ubtechinc.alpha.utils;

import android.content.Context;
import android.content.Intent;

/**
 * @date 2016/12/28
 * @author paul.zhang@ubtrobot.com
 * @Description 服务相关工具类
 * @modifier
 * @modify_time
 */

public class ServiceUtils {


	public static void startService(Context context){
		String service =  "com.ubtechinc.alpha.service.MainService";
		Intent intent = new Intent(service);
		intent.setPackage(context.getPackageName());
		context.startService(intent);
	}


	public static void stopService(Context context){
		String service = "com.ubtechinc.alpha.service.MainService";
		Intent intent = new Intent(service);
		intent.setPackage(context.getPackageName());
		context.stopService(intent);
	}
}
