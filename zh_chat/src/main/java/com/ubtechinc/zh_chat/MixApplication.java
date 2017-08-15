package com.ubtechinc.zh_chat;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.tencent.bugly.crashreport.CrashReport;
import com.ubtech.iflytekmix.BuildConfig;
import com.ubtech.utilcode.utils.AppUtils;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtech.utilcode.utils.Utils;

import java.util.LinkedHashMap;
import java.util.Map;

import timber.log.Timber;

public class MixApplication extends Application {
	private static SharedPreferences mPreferences;
	private static String SHARED_PREFERENCES_NAME ;
	private static final String TAG = "MixApplication";

	@Override
	public void onCreate() {
		super.onCreate();
		SHARED_PREFERENCES_NAME = AppUtils.getAppPackageName(this);
		mPreferences = this.getSharedPreferences(SHARED_PREFERENCES_NAME,
				MODE_PRIVATE);
		initBugly();
		initTimber();
		Utils.init(this);
		LogUtils.init(true, true, "zh_chat");
	}

	private void initTimber() {
		if (BuildConfig.DEBUG) {
			Timber.plant(new Timber.DebugTree());
		} else {
			Timber.plant(new Timber.Tree(){
				@Override
				protected void log(int priority, String tag, String message, Throwable t) {

				}
			});
		}
	}

	public static boolean getSettingBoolean(String key, boolean defaultVal) {
		boolean ret = mPreferences.getBoolean(key, defaultVal);
		return ret;
	}

	public static void setSettingBoolean(String key, boolean val) {
		Editor editor = mPreferences.edit();
		editor.putBoolean(key, val);
		editor.commit();
	}

	private void initBugly() {
		CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplicationContext());
		strategy.setCrashHandleCallback(new CrashReport.CrashHandleCallback() {
			public Map<String, String> onCrashHandleStart(int crashType, String errorType,
														  String errorMessage, String errorStack) {
				//上报的自定义字段
				LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

				return map;
			}

			@Override
			public byte[] onCrashHandleStart2GetExtraDatas(int crashType, String errorType,
														   String errorMessage, String errorStack) {
				try {
					LogUtils.d(TAG,"errorMessage: "+errorMessage);
					LogUtils.d(TAG,"errorStack: "+errorStack);
					return "Extra data.".getBytes("UTF-8");
				} catch (Exception e) {
					return null;
				}
			}

		});

		CrashReport.initCrashReport(getApplicationContext(),"3513ff6a73",true,strategy);
	}
}
