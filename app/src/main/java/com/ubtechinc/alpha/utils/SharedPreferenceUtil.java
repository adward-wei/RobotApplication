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
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @author paul.zhang@ubtrobot.com
 * @date 2016/8/3
 * @Description 简单存储工具类
 * @modifier
 * @modify_time
 */

public class SharedPreferenceUtil {

	private static final String USERCARDSPFILE = "ALPHA_MAIN_SHARED";

	/**
	 * @param context
	 * @param key
	 * @param value
	 * @return
	 * @throws
	 * @Description 自定义存储数据
	 */
	public static boolean save(Context context, String key, String value) {
		Editor editor = context.getSharedPreferences(USERCARDSPFILE, Context.MODE_WORLD_READABLE).edit();
		editor.putString(key, value);
		return editor.commit();
	}

	/**
	 * @param context
	 * @param key
	 * @param value
	 * @return
	 * @throws
	 * @Description 存储String类型数据
	 */
	public static boolean saveString(Context context, String key, String value) {
		Editor editor = context.getSharedPreferences(USERCARDSPFILE, Context.MODE_WORLD_READABLE).edit();
		editor.putString(key, value);
		return editor.commit();
	}

	/**
	 * @param context
	 * @param key
	 * @return
	 * @throws
	 * @Description 根据key值读取String类型数据
	 */
	public static String readString(Context context, String key,String defaultVoicer) {
		SharedPreferences sp = context.getSharedPreferences(USERCARDSPFILE, Context.MODE_WORLD_READABLE);
		return sp.getString(key, defaultVoicer);
	}


	/**
	 * @param context
	 * @param key
	 * @param value
	 * @return
	 * @throws
	 * @Description 根据key存储int类型数据
	 */
	public static boolean saveInt(Context context, String key, int value) {
		Editor editor = context.getSharedPreferences(USERCARDSPFILE, Context.MODE_WORLD_READABLE).edit();
		editor.putInt(key, value);
		return editor.commit();
	}

	/**
	 * @param context
	 * @param key
	 * @param value
	 * @return
	 * @throws
	 * @Description 存储布尔类型数据
	 */
	public static boolean saveBoolean(Context context, String key, boolean value) {
		Editor editor = context.getSharedPreferences(USERCARDSPFILE, Context.MODE_WORLD_READABLE).edit();
		editor.putBoolean(key, value);
		return editor.commit();

	}

	/**
	 * @param context
	 * @param key
	 * @return
	 * @throws
	 * @Description 根据key值读取布尔类型数据,默认返回false
	 */
	public static boolean readBoolean(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(USERCARDSPFILE, Context.MODE_WORLD_READABLE);
		return sp.getBoolean(key, false);
	}

	/**
	 * @param context
	 * @param key
	 * @return
	 * @throws
	 * @Description 根据key值读取int类型数据,默认返回0
	 */
	public static int readInt(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(USERCARDSPFILE, Context.MODE_WORLD_READABLE);
		return sp.getInt(key, 0);

	}

	/**
	 * @param context
	 * @param key
	 * @param defaultValue
	 * @return
	 * @throws
	 * @Description 根据key值读取int类型数据
	 */

	public static int readInt(Context context, String key, int defaultValue) {
		SharedPreferences sp = context.getSharedPreferences(USERCARDSPFILE, Context.MODE_WORLD_READABLE);
		return sp.getInt(key, defaultValue);
	}

	/**
	 * @param context
	 * @return
	 * @throws
	 * @Description 清除全部存储
	 */

	public static void clear(Context context) {
		Editor editor = context.getSharedPreferences(USERCARDSPFILE, Context.MODE_WORLD_READABLE).edit();
		editor.clear();
		editor.commit();
	}

}
