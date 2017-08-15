package com.ubtechinc.alpha2ctrlapp.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/*************************
* @date 2016/6/28
* @author
* @Description APP工具类
* @modify
* @modify_time
**************************/
public class ApkTools {
	//版本名
	public static String getVersionName(Context context) {
	    return getPackageInfo(context).versionName;
	}
	 
	//版本号
	public static int getVersionCode(Context context) {
	    return getPackageInfo(context).versionCode;
	}
	 
	private static PackageInfo getPackageInfo(Context context) {
	    PackageInfo pi = null;
	 
	    try {
	        PackageManager pm = context.getPackageManager();
	        pi = pm.getPackageInfo(context.getPackageName(),
	                PackageManager.GET_CONFIGURATIONS);
	 
	        return pi;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	 
	    return pi;
	}
	// true表示远程版本大，即需要升级
		public static boolean compareVersion(String remote_version, Context context) {
			String verLocal = getVersionName(context);
			String[] lVersion = verLocal.split("\\.");
			String[] wVersion = remote_version.split("\\.");
			for (int i = 0; i < lVersion.length; i++) {

				if (Integer.parseInt(wVersion[i]) > Integer.parseInt(lVersion[i])) {
					return true;
				} else if (Integer.parseInt(wVersion[i]) == Integer
						.parseInt(lVersion[i])) {
					continue;
				} else {
					return false;
				}
			}
			return false;

		}
}
