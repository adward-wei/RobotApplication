package com.ubtechinc.alpha.download;

import android.os.Environment;

public class FilePath {
	private static String sdPath = Environment.getExternalStorageDirectory()
			.getAbsolutePath();
	public static String appPath = sdPath + "/ubtech/alpha2s/app";
	public static String actionsPath = sdPath + "/actions";
	public static String versionPath = sdPath + "/ubtech/alpha2s/version.config";
}
