/*
 *
 *  *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.ubtechinc.alpha.service;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.ubtech.utilcode.utils.DeviceUtils;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtech.utilcode.utils.WifiControl;
import com.ubtechinc.alpha.Configuration.ConfigurationLoad;
import com.ubtechinc.alpha.app.AlphaApplication;
import com.ubtechinc.alpha.appmanager.AppManager;
import com.ubtechinc.alpha.behavior.RobotStandup;
import com.ubtechinc.alpha.behavior.RobotTakeARest;
import com.ubtechinc.alpha.im.Robot2PhoneMsgMgr;
import com.ubtechinc.alpha.ops.ActionServiceProxy;
import com.ubtechinc.alpha.ops.LedControlServiceImpl;
import com.ubtechinc.alpha.ops.MotorServiceImpl;
import com.ubtechinc.alpha.ops.RobotOpsManager;
import com.ubtechinc.alpha.ops.led.SetEyeBlinkOp;
import com.ubtechinc.alpha.receiver.AppManagerReceiver;
import com.ubtechinc.alpha.receiver.ThirdPartyReceiver;
import com.ubtechinc.alpha.robotinfo.RobotConfiguration;
import com.ubtechinc.alpha.robotinfo.RobotState;
import com.ubtechinc.alpha.serverlibutil.aidl.ActionInfo;
import com.ubtechinc.alpha.serverlibutil.interfaces.ActionListResultListener;
import com.ubtechinc.alpha.speech.SpeechServiceProxy;
import com.ubtechinc.alpha.sys.SysServiceImpl;
import com.ubtechinc.alpha.utils.ActionUtil;
import com.ubtechinc.alpha.utils.BootUtils;
import com.ubtechinc.alpha.utils.Constants;
import com.ubtechinc.alpha.utils.StringUtil;
import com.ubtechinc.alpha.utils.VersionCollector;
import com.ubtechinc.alpha.wificonnect.Alpha2Connection;
import com.ubtechinc.alpha2services.BuildConfig;
import com.ubtechinc.nets.push.PushManager;
import com.ubtechinc.nets.socket.udp.MulticastSocketProcess;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.zhaiyifan.appinit.Flow;
import cn.zhaiyifan.appinit.Init;
import cn.zhaiyifan.appinit.Task;

/**
 * @date 2016/12/28
 * @author paul.zhang@ubtrobot.com
 * @Description 系统启动机器人主服务
 * @modifier logic.peng@ubtrobot.com
 * @modify_time 2017/4/19
 */

public class MainService extends ServiceBindable {
	private static final String TAG = "MainService";
	private Context mContext;
	private AppManagerReceiver appManagerReceiver;
	private ThirdPartyReceiver thirdPartyReceiver;

	@Override
	protected void onStartOnce() {
		super.onStartOnce();
		mContext = this;

		LogUtils.i("MainService---onStartOnce");
		ProxyServiceManager.get(this).initProxyService();
		//bindCache add action&speech&led&motor binder
		ServiceCache.addService(Constants.SPEECH_BINDER_NAME, SpeechServiceProxy.
				getInstance().getBinderStub());
		ServiceCache.addService(Constants.ACTION_BINDER_NAME, ActionServiceProxy.getInstance().getBinderStub());
		ServiceCache.addService(Constants.LED_BINDER_NAME, LedControlServiceImpl.get(this));
		ServiceCache.addService(Constants.MOTOR_BINDER_NAME, MotorServiceImpl.get(this));
		ServiceCache.addService(Constants.SYSINFO_BINDER_NAME, SysServiceImpl.get(this));
		init();
	}

	public void init(){
		Init.setThreadPoolSize(2);
		Init.init(this);

		Task task0 = new Task("task0", true) {
			@Override
			protected void start() {
				ConfigurationLoad.get(getApplicationContext()).load();
				StringUtil.setLanguage(getApplicationContext(), RobotConfiguration.get().asr_Language);
			}
		};

		Task task1 = new Task("task1", false) {
			@Override
			protected void start() {
				LogUtils.i(TAG,"初始化语音引擎服务");
			}
		};

		Task task2 = new Task("task2", true) {
			@Override
			protected void start() {
				LogUtils.i(TAG,"初始化alpha 服务");
			}
		};

		Task task3 = new Task("task3", true, 200) {
			@Override
			protected void start() {
				LogUtils.i(TAG,"初始化第三方语音引擎注册唤醒词");
				//IfytekBnf.get().initializ(MainService.this);
			}
		};

		Task task4 = new Task("task4", true) {
			@Override
			protected void start() {


				LogUtils.i(TAG,"初始化应用程序管理");
				AppManager.getInstance().startDefaultApp();
			}
		};
		Task task5 = new Task("task5", true) {
			@Override
			protected void start() {
//				NLog.i(TAG, "复制动作文件");
				//工厂要求
				BootUtils.get().copyActionsFromSystem();
				LogUtils.i(TAG, "预载动作信息文件");
				BootUtils.get().preGetActionInfo();
				ActionServiceProxy.getInstance().getActionList(new ActionListResultListener() {
					@Override
					public void onGetActionList(int nOpId, int nErr, List<ActionInfo> onArrAction) {
						LogUtils.i(TAG, "初始化动作列表...size ="+ onArrAction.size());
						ActionUtil.initActionList(onArrAction);
					}
				});
			}
		};
		Task task6 = new Task("task6" , false){
			 @Override
			 protected void start() {
				 RobotOpsManager.get(getApplicationContext()).init();
				 RobotTakeARest.instance().start(false);
				 VersionCollector.get(getApplicationContext()).requestVersion();
				 String sid = null;
				 try {
					 sid = SysServiceImpl.get(getApplicationContext()).getSid();
				 } catch (RemoteException e) {
					 e.printStackTrace();
				 }

				 RobotState.get().setSid(sid);
				 LogUtils.i( "开机机器人站起来");
				 RobotStandup robotStandup = new RobotStandup(getApplicationContext());
				 robotStandup.start();

				 LogUtils.i("初始化客户端与机器人通信模块（IM）-- 序列号："+RobotState.get().getSid());
				 Robot2PhoneMsgMgr.getInstance().init();
			 }
		 };

		Task task7 = new Task("task7", true) {

			@Override
			protected void start() {
//				Alpha2Connection.getInstance(getApplicationContext()).beginWifiConnection();
			}
		};

		Task task8 = new Task("task8", true) {

			@Override
			protected void start() {
				RobotOpsManager.get(getApplicationContext()).executeOp(new SetEyeBlinkOp(true));
			}
		};

		Task task10 = new Task("task10",true){
			@Override
			protected void start() {
				AppManagerReceiver.getInstance().registerReceiver();
				ThirdPartyReceiver.getInstance().registerReceiver();
			}
		};


		Task task11 = new Task("task11", true) {
			@Override
			protected void start() {
				Alpha2Connection connection = Alpha2Connection.getInstance(mContext);
				connection.beginWifiConnection();

				String strBroadcastIpAddr = WifiControl.get(mContext).getNetbroadcastAddr();
				MulticastSocketProcess.get(mContext,
						strBroadcastIpAddr, 6000).startProcess();
			}
		};

		Task task12 = new Task("task12", true) {
			@Override
			protected void start() {
				LogUtils.d(TAG,"Push模块初始化");
//				PushManager.getInstance(AlphaApplication.getContext()).init();
			}
		};
		PushManager.getInstance(AlphaApplication.getContext()).init();
		if (false && BuildConfig.DEBUG) {
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					String cpuDesc = DeviceUtils.getCPURateDesc();
					LogUtils.d(TAG, "CPU 占用率：" + cpuDesc);
				}
			}, 10000, 1000);
		}

		Flow flow = new Flow("main service init task");
		flow.addTask(1, task0)
				.addTask(1, task1)
				.addTask(1, task2)
				.addTask(1, task3)
				.addTask(1, task4)
				.addTask(1, task5)
				.addTask(1, task6)
				.addTask(1, task7)
				.addTask(1, task8)
				.addTask(1, task10)
				.addTask(1, task11)
				.addTask(1, task12);
		Init.start(flow);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		RobotOpsManager.get(this).destroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return MainServiceImpl.getMainServiceImplInstance();
	}
}
