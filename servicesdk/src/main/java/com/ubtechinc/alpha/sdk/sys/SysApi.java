/*
 *
 *  *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.ubtechinc.alpha.sdk.sys;

import android.content.Context;
import android.net.Uri;

import com.ubtechinc.alpha.sdk.SdkConstants;
import com.ubtechinc.alpha.serverlibutil.aidl.AlarmInfo;
import com.ubtechinc.alpha.serverlibutil.service.SysServiceUtil;

/**
 * @date 2017/2/22
 * @author paul.zhang@ubtrobot.com
 * @Description 机器人系统层对外提供的接口
 * @modifier
 * @modify_time
 */

public class SysApi {
	private SysServiceUtil mSysServiceUtil;
	private static volatile SysApi mSysApi;

	public static SysApi get() {
		if (mSysApi == null) {
			synchronized (SysApi.class) {
				if (mSysApi == null) {
					mSysApi = new SysApi();
				}
			}
		}
		return mSysApi;
	}

	public synchronized SysApi initializ(Context context) {
		if (mSysServiceUtil == null) {
			mSysServiceUtil = new SysServiceUtil(context);
		}
		return get();
	}

	public synchronized void destroy(){
		mSysServiceUtil = null;
	}

	/**
	 * @Description 获取机器人系统唯一标识
	 * @param
	 * @return ep: Cruzr.01.mac地址
	 * @throws
	 */
	public String getSid() {
		if (mSysServiceUtil == null){
			return "";
		}
		return mSysServiceUtil.getSid();
	}

	/**
	 * MIC版本
	 * @return
	 */
	public String getMICVersion(){
		if (mSysServiceUtil == null)
			return "";
		return mSysServiceUtil.getMICVersion();
	}

    public int insertAlarm(AlarmInfo alarmInfo) {
		if (mSysServiceUtil == null)
			return SdkConstants.ErrorCode.RESULT_FAIL;
		return mSysServiceUtil.insertAlarm(alarmInfo);
    }

	public AlarmInfo[] queryAllAlarm(String date) {
		if (mSysServiceUtil == null)
			return null;
		return mSysServiceUtil.queryAllAlarm(date);
	}

    public void startApp(Uri uri) {
		if (mSysServiceUtil != null)
			mSysServiceUtil.startApp(uri);
    }

    public void enterUpgradeMode(){
		if (mSysServiceUtil != null)
			mSysServiceUtil.enterUpgradeMode();
	}

	public void exitUpgradeMode(){
		if (mSysServiceUtil != null)
			mSysServiceUtil.exitUpgradeMode();
	}

	public String getChestVersion() {
		if (mSysServiceUtil != null)
			return mSysServiceUtil.getChestVersion();
		return "";
	}

	public String getHeadVersion() {
		if(mSysServiceUtil != null)
			return mSysServiceUtil.getHeadVersion();
		return "";
	}

	public String getBatteryVersion() {
		if(mSysServiceUtil != null)
			return mSysServiceUtil.getBatteryVersion();
		return "";
	}

	public boolean isPowerCharging(){
		if(mSysServiceUtil != null)
			return mSysServiceUtil.isPowerCharging();
		return false;
	}

	public int getPowerValue(){
		if(mSysServiceUtil != null)
			return mSysServiceUtil.getPowerValue();
		return 1;
	}
}
