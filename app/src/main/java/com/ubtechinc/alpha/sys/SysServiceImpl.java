/*
 *
 *  *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.ubtechinc.alpha.sys;

import android.content.Context;
import android.net.Uri;
import android.os.RemoteException;

import com.ubtech.utilcode.utils.LogUtils;
import com.ubtech.utilcode.utils.SystemProperty;
import com.ubtechinc.alpha.appmanager.AppManager;
import com.ubtechinc.alpha.model.StaticValue;
import com.ubtechinc.alpha.ops.ActionServiceProxy;
import com.ubtechinc.alpha.ops.OpResult;
import com.ubtechinc.alpha.ops.RobotOpsManager;
import com.ubtechinc.alpha.ops.sys.ReadSidOp;
import com.ubtechinc.alpha.provider.AlarmInfoVisitor;
import com.ubtechinc.alpha.robotinfo.RobotState;
import com.ubtechinc.alpha.robotinfo.SoftwareVersionInfo;
import com.ubtechinc.alpha.serverlibutil.aidl.AlarmInfo;
import com.ubtechinc.alpha.serverlibutil.aidl.ISysService;
import com.ubtechinc.alpha.sdk.SdkConstants;
import com.ubtechinc.alpha.service.ProxyServiceManager;
import com.ubtechinc.alpha.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @date 2017/2/22
 * @author paul.zhang@ubtrobot.com
 * @Description 单例模式、机器人系统层操作具体实现
 * @modifier logic.peng
 * @modify_time 2017/5/23
 */

public class SysServiceImpl extends ISysService.Stub {
	private Context mContext;
	private static SysServiceImpl instance;
	private String mSid;
	private Lock lock = new ReentrantLock();

	private SysServiceImpl(Context cxt) {
		this.mContext = cxt.getApplicationContext();
	}

	public static SysServiceImpl get(Context cxt) {
		if (instance!= null) return instance;
		synchronized (SysServiceImpl.class){
			if (instance == null) instance = new SysServiceImpl(cxt);
		}
		return instance;
	}

	@Override
	public String getSid() throws RemoteException {
		lock.lock();
		try {
			if (mSid == null) {
				OpResult<String> result = RobotOpsManager.get(mContext).executeOpSync(new ReadSidOp());
				if (result != null) {
					mSid = result.data;
				}
			}
		}finally {
			lock.unlock();
		}

		return mSid;
	}

	@Override
	public String getMICVersion() throws RemoteException {
		return SystemProperty.getProperty(Constants.ALPHA_MIC_HARDWARE_VERSION);
	}

	@Override
	public AlarmInfo[] queryAllAlarm(String data) throws RemoteException {
		lock.lock();
		try{
			AlarmInfoVisitor provider = AlarmInfoVisitor.get();
			List<com.ubtechinc.alpha.ops.alarm.AlarmInfo> alarms = provider.getAllData();
			List alarmInfos = new ArrayList<>(alarms.size());
			for (com.ubtechinc.alpha.ops.alarm.AlarmInfo alarm: alarms){
				AlarmInfo alarmInfo = new AlarmInfo();
				alarmInfo.id = alarm.id;
				alarmInfo.state = alarm.state;//0 未结束,1，结束
				alarmInfo.hh = alarm.hh;
				alarmInfo.mm = alarm.mm;
				alarmInfo.repeat = alarm.repeat;// 0一次 1每天 2工作日
				alarmInfo.isUseAble = alarm.isUseAble;// 是否开启
				alarmInfo.actionStartName = alarm.actionStartName;// 闹钟开始时候执行动作
				alarmInfo.acitonEndName = alarm.acitonEndName;// 闹钟结束时候执行动作 0 关机 1保持开机
				alarmInfo.actionType = alarm.actionType;// 0 动作表 1录音提醒 2拨打电话
				alarmInfo.yy = alarm.yy;// 年
				alarmInfo.mo = alarm.mo;// 月
				alarmInfo.day = alarm.day;// 日
				alarmInfo.date = alarm.date;// 星期
				alarmInfo.ss = alarm.ss;// 秒
				alarmInfo.vibrate = alarm.vibrate == 1;//震动
				alarmInfo.label = alarm.label;//
				alarmInfo.alert = alarm.alert;
				alarmInfo.silent = alarm.silent;//静音
				alarmInfo.dtstart = alarm.dtstart;
				alarmInfo.iscomplete = alarm.iscomplete;
				alarmInfo.dttime = alarm.dttime;
				if (data.equals(dtLongToString(alarm.dtstart)))
					alarmInfos.add(alarmInfo);
			}
			AlarmInfo[] alarmArr = new AlarmInfo[alarmInfos.size()];
			alarmInfos.toArray(alarmArr);
			return alarmArr;
		}finally {
			lock.unlock();
		}
	}

	@Override
	public int insertAlarm(AlarmInfo alarm) throws RemoteException {
		lock.lock();
		try{
			com.ubtechinc.alpha.ops.alarm.AlarmInfo alarmInfo = new com.ubtechinc.alpha.ops.alarm.AlarmInfo();
			alarmInfo.state = alarm.state;//0 未结束,1，结束
			alarmInfo.hh = alarm.hh;
			alarmInfo.mm = alarm.mm;
			alarmInfo.repeat = alarm.repeat;// 0一次 1每天 2工作日
			alarmInfo.isUseAble = alarm.isUseAble;// 是否开启
			alarmInfo.actionStartName = alarm.actionStartName;// 闹钟开始时候执行动作
			alarmInfo.acitonEndName = alarm.acitonEndName;// 闹钟结束时候执行动作 0 关机 1保持开机
			alarmInfo.actionType = alarm.actionType;// 0 动作表 1录音提醒 2拨打电话
			alarmInfo.yy = alarm.yy;// 年
			alarmInfo.mo = alarm.mo;// 月
			alarmInfo.day = alarm.day;// 日
			alarmInfo.date = alarm.date;// 星期
			alarmInfo.ss = alarm.ss;// 秒
			alarmInfo.vibrate = alarm.vibrate ? 1:0;//震动
			alarmInfo.label = alarm.label;//
			alarmInfo.alert = alarm.alert;
			alarmInfo.silent = alarm.silent;//静音
			alarmInfo.dtstart = alarm.dtstart;
			alarmInfo.iscomplete = alarm.iscomplete;
			alarmInfo.dttime = alarm.dttime;
			LogUtils.D("insert alarm = %s", alarm.toString());
			AlarmInfoVisitor.get().saveOrUpdate(alarmInfo);
		}finally {
			lock.unlock();
		}
		return SdkConstants.ErrorCode.RESULT_SUCCESS;
	}

	@Override
	public void startApp(Uri uri) throws RemoteException {
		lock.lock();
		try{
			AppManager.getInstance().startApp(uri);
		}finally {
			lock.unlock();
		}
	}

	private static String dtLongToString(long time) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String dt = df.format(time);
		return dt;
	}

	@Override
	public void enterUpgradeMode() throws RemoteException{
		lock.lock();
		try{
			AppManager.getInstance().stopApp(StaticValue.CHAT_PACKAGE_NAME);
			ProxyServiceManager.get(mContext).getSerialCommandProxy().onDestroy();
			ActionServiceProxy.getInstance().clearAllPendingActions();
			RobotState.get().setUpgradeModeOpened(true);
		}finally {
			lock.unlock();
		}
	}

	@Override
	public void exitUpgradeMode() {
		lock.lock();
		try {
			RobotState.get().setUpgradeModeOpened(false);
			ProxyServiceManager.get(mContext).restartSerialCommandProxy();
			AppManager.getInstance().startDefaultApp();
		} finally {
			lock.unlock();
		}
	}

	public String getChestVersion() throws RemoteException {
		return SoftwareVersionInfo.get().chestVersion;
	}

	@Override
	public String getHeadVersion() throws RemoteException {
		return SoftwareVersionInfo.get().headVersion;
	}

	@Override
	public String getBatteryVersion() throws RemoteException {
		return SoftwareVersionInfo.get().batteryVersion;
	}

	@Override
	public boolean isPowerCharging(){
		return RobotState.get().isInsertDC();
	}

	@Override
	public int getPowerValue(){
		return RobotState.get().getPowerValue();
	}

}
