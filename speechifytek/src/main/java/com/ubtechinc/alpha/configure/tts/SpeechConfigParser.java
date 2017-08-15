/*
 *
 *  *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.ubtechinc.alpha.configure.tts;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ubtechinc.alpha.serverlibutil.aidl.SpeechVoice;

import java.util.List;

/**
 * @author paul.zhang@ubtrobot.com
 * @date 2016/11/3
 * @Description 负责解析TTS发音人信息
 * @modifier
 * @modify_time;
 */

public class SpeechConfigParser extends AbstractJsonConfigParser {
	private Gson gson = new Gson();
	private SpeechTTsVoice mConfig;

	public SpeechConfigParser(Context context, String path) {
		super(context, path);
	}

	@Override
	protected void parse(JsonObject root) {
		try {
			mConfig = gson.fromJson(root.toString(), SpeechTTsVoice.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<SpeechVoice> getData() {
		if (mConfig == null){
			return null;
		}
		return mConfig.config;
	}
}
