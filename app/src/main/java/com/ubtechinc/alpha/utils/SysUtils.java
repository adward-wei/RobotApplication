/*
 *
 *  *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.ubtechinc.alpha.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import com.ubtech.utilcode.utils.SystemProperty;
import com.ubtechinc.alpha2services.R;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

/**
 * @author paul.zhang@ubtrobot.com
 * @date 2017/2/22
 * @Description 系统工具类
 * @modifier
 * @modify_time
 */

public class SysUtils {

	public static String getMac() {
		String macSerial = null;
		String str = "";
		String newMac = "";

		try {
			Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
			InputStreamReader ir = new InputStreamReader(pp.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);

			for (; null != str; ) {
				str = input.readLine();
				if (str != null) {
					macSerial = str.trim();// 去空格
					newMac = macSerial.replaceAll(":", "");
					break;
				}
			}
		} catch (IOException ex) {
			// 赋予默认值
			ex.printStackTrace();
		}
		return newMac;
	}

	public static Uri getMediaUri(Context context) {
		return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/" + R.raw.wakeup);
	}

	public static int randomSet(double m, double n) {
		int num = (int) m + (int) (Math.random() * (n - m));
		return num;
	}

	public static String getMicVersion(){
		return SystemProperty.getProperty(Constants.ALPHA_MIC_HARDWARE_VERSION);
	}

	public static String getSystemVersion(){
		return SystemProperty.getProperty(Constants.ROBOT_SYSTEM_VERSION);
	}

	public static boolean isLynxSystem(){
		return getSystemVersion().startsWith(Constants.LYNX_SYSTEM_VERSION);
	}

	public static boolean is5Mic(){
		return Constants.MIC5_VERSION.equals(getMicVersion());
	}

	public static boolean is2Mic(){
		return Constants.MIC2_VERSION.equals(getMicVersion());
	}
}
