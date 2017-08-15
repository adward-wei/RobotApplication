/*
 *
 *  *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.ubtechinc.alpha.speech.ifytek;

import android.content.Context;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

/**
 * @author paul.zhang@ubtrobot.com
 * @date 2016/12/29
 * @Description 讯飞引擎核处理心类
 * @modifier
 * @modify_time
 */

public final class IfytekUtil {
	private static final String APP_ID = "56652373";  //      56652373   5806e81e
	public static void createUtility(Context context) {
		String param = ("appid=" + APP_ID) +
				"," +
				SpeechConstant.ENGINE_MODE + "=" + SpeechConstant.MODE_MSC +
				"," +
				"server_url=http://ubtek.openspeech.cn/index.htm";
		SpeechUtility.createUtility(context, param);
	}
}
