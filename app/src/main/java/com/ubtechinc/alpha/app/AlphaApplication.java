/*
 *
 *  *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.ubtechinc.alpha.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.tencent.bugly.crashreport.CrashReport;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtech.utilcode.utils.ProcessUtils;
import com.ubtech.utilcode.utils.Utils;
import com.ubtech.utilcode.utils.network.NetworkHelper;
import com.ubtechinc.alpha.robotinfo.RobotState;
import com.ubtechinc.alpha.utils.PackageUtils;
import com.ubtechinc.alpha.utils.StringUtil;
import com.ubtechinc.alpha2services.BuildConfig;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import timber.log.Timber;


/**
 * @author paul.zhang@ubtrobot.com
 * @date 2016/12/26
 * @Description 全局实例,应用程序入口
 * @modifier logic.peng
 * @modify_time 2017/5/11
 */

public class AlphaApplication extends Application {
	private static final String TAG = "AlphaApplication";
	private static Context mContext = null;

	public static Context getContext() {
		return mContext;
	}

	private synchronized void setContext(Context context) {
		AlphaApplication.mContext = context;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		if(ProcessUtils.isMainProcess(this)) {
			//设置全局上下文
			setContext(this.getApplicationContext());
			Utils.init(this);
			//setprop log.tag.alpha VERBOSE 可打开v级别，默认是i级别
			LogUtils.init(true, true, "alpha");
			initTimber();
			//实例化文件夹
			ACContext.initInstance(getContext());
			//appId=68dc3c67e4, appKey= 065f68ad-fc76-4484-ad2e-eb0846185cc9
			initBugly();
			//抓取崩溃日志
			ACUncaughtExceptionHandler handler = new ACUncaughtExceptionHandler(getApplicationContext(), ACContext.getDirectoryPath(DirType.crash));
			handler.registerForExceptionHandler();
			//注册网络状态广播
			NetworkHelper.sharedHelper().registerNetworkSensor(this);
			//版本信息日志
			LogUtils.i(TAG, "%s %s", PackageUtils.getAppInfo(this), PackageUtils.getBuildTime(this));
			StringUtil.setLanguage(Locale.CHINA);
		}
	}

	private void initBugly() {
		CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplicationContext());
		strategy.setCrashHandleCallback(new CrashReport.CrashHandleCallback() {
			public Map<String, String> onCrashHandleStart(int crashType, String errorType,
														  String errorMessage, String errorStack) {
				//上报的自定义字段
				LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
				map.put("robotSerialId", RobotState.get().getSid());
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

		CrashReport.initCrashReport(getApplicationContext(),"68dc3c67e4", BuildConfig.DEBUG,strategy);

//		new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				CrashReport.testJavaCrash();
//			}
//		},5000);
	}

	private void initTimber() {
		if (BuildConfig.DEBUG){
			Timber.plant(new Timber.DebugTree());
		}else {
			Timber.plant(new Timber.Tree() {
				@Override
				protected void log(int priority, String tag, String message, Throwable t) {
					if (priority >= Log.WARN){
						LogUtils.w(message);
					}
				}
			});
		}
	}
}