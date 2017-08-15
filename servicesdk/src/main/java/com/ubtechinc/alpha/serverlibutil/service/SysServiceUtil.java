/*
 *
 *  *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.ubtechinc.alpha.serverlibutil.service;

import android.content.Context;
import android.net.Uri;
import android.os.RemoteException;

import com.ubtechinc.alpha.serverlibutil.aidl.AlarmInfo;
import com.ubtechinc.alpha.serverlibutil.aidl.ISysService;
import com.ubtechinc.alpha.sdk.SdkConstants;

/**
 * @author paul.zhang@ubtrobot.com
 * @date 2017/2/22
 * @Description 机器人系统服务绑定工具类、提供绑定系统控制服务
 * @modifier logic.peng 增加获取mic版本信息
 * @modify_time
 */

public class SysServiceUtil  {
	private ISysService mService;


	public SysServiceUtil(Context context) {
		mService = ISysService.Stub.asInterface(BinderFetchServiceUtil.get(context).getServiceBinder(SdkConstants.SYSINFO_BINDER_NAME));
	}

	public String getSid() {
		String ret = "";
		try {
			ret = mService.getSid();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public String getMICVersion(){
		String ret = "";
		try {
			ret = mService.getMICVersion();
		}catch (RemoteException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return ret;
	}

    public int insertAlarm(AlarmInfo alarmInfo) {
		int ret = SdkConstants.ErrorCode.RESULT_FAIL;
		try {
			ret = mService.insertAlarm(alarmInfo);
		} catch (RemoteException e) {
			e.printStackTrace();
		}catch (NullPointerException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public AlarmInfo[] queryAllAlarm(String date) {
		try {
			return mService.queryAllAlarm(date);
		} catch (RemoteException e) {
			e.printStackTrace();
		}catch (NullPointerException e) {
			e.printStackTrace();
		}
		return null;
	}

    public void startApp(Uri uri) {
		try {
			mService.startApp(uri);
		} catch (RemoteException e) {
			e.printStackTrace();
		}catch (NullPointerException e) {
			e.printStackTrace();
		}
    }

    public void enterUpgradeMode(){
		try {
			mService.enterUpgradeMode();
		} catch (RemoteException e) {
			e.printStackTrace();
		}catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
	public String getChestVersion() {
		try {
			return mService.getChestVersion();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return "";
	}

	public void exitUpgradeMode(){
		try {
			mService.exitUpgradeMode();
		}catch (RemoteException e) {
			e.printStackTrace();
		}catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	public String getHeadVersion() {
		try {
			return mService.getHeadVersion();
		} catch (RemoteException e) {
			e.printStackTrace();
		}catch (NullPointerException e) {
			e.printStackTrace();
		}
		return "";
	}

	public String getBatteryVersion() {
		try {
			return mService.getBatteryVersion();
		} catch (RemoteException e) {
			e.printStackTrace();
		}catch (NullPointerException e) {
			e.printStackTrace();
		}
		return "";
	}


	public boolean isPowerCharging(){
		try {
			return mService.isPowerCharging();
		} catch (RemoteException e) {
			e.printStackTrace();
		}catch (NullPointerException e) {
			e.printStackTrace();
		}
		return false;
	}

	public int getPowerValue(){
		try {
			return mService.getPowerValue();
		} catch (RemoteException e) {
			e.printStackTrace();
		}catch (NullPointerException e) {
			e.printStackTrace();
		}
		return 1;
	}
}
