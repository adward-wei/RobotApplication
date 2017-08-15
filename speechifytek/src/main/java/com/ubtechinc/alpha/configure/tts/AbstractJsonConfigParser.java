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
import android.content.res.AssetManager;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ubtech.utilcode.utils.CloseUtils;
import com.ubtech.utilcode.utils.LogUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @date 2016/11/3
 * @author paul.zhang@ubtrobot.com
 * @Description 抽象解析asserts下json数据抽象基类
 * @modifier
 * @modify_time
 */

public abstract class AbstractJsonConfigParser {
	private static final String TAG = "AbstractJsonConfigParser";
	private JsonObject mRoot;

	protected abstract void parse(JsonObject root);

	public AbstractJsonConfigParser(Context context, String path) {
		InputStream in = null;
		try {
			in = context.openFileInput(path);
		} catch (final FileNotFoundException e1) {
			LogUtils.w(TAG, "file not found exception.");
		} catch (final IllegalArgumentException e) {
			LogUtils.w(TAG, "read config file failed." + path);
		}

		if (null == in) {
			try {
				final AssetManager assets = context.getAssets();
				in = assets.open(path);
			} catch (final IOException e) {
				LogUtils.e(TAG, "read config file failed.");
			}
		}

		JsonElement rootJson;
		final JsonParser jsonParser = new JsonParser();
		try {
			if (in != null) {
				rootJson = jsonParser.parse(new InputStreamReader(in));
				mRoot = rootJson.getAsJsonObject();
			}
		} finally {
			CloseUtils.closeIOQuietly(in);
		}
	}

	public void parse() {
		parse(mRoot);
	}


}
