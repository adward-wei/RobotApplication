/*
 *
 *  *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.ubtechinc.alpha.speech.dispatch;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 第三方服务对应的通信器
 *
 * @author Administrator
 */
public class SpeechServiceUtil implements ServiceConnection {
	private static final String TAG = "dispatch";
	private static final String SPEECH_RST = "speech_rst";
	/**
	 * 第三方app speech service返回指令结果的信息值
	 */
	private static final int MSG_DEV_APP_ONRESULT = 0;
	/**
	 * 第三方app speech service在前台的信息值
	 */
	private static final int MSG_DEV_APP_ONSTART = 1;
	/**
	 * 第三方app speech service在后台的信息值
	 */
	private static final int MSG_DEV_APP_ONSTOP = 2;
	/**
	 * bind的延迟时间
	 */
	private static final int BIND_DELAY_TIME = 200;
	/**
	 * 重bind的上限
	 */
	private static final int BIND_AGAIN_LIMITED_COUNT = 4;
	/**
	 * bind计数器
	 */
	private int mBindCount = 0;
	/**
	 * 对应第三方服务
	 */
	private String mServiceAction;
	/**
	 * 对应第三方的AppId
	 */
	private int appId;
	/**
	 * 第三方app包名
	 */
	private String pckName;
	/**
	 * 第三方语音service的对应消息发送器
	 */
	private Messenger mMessenger;
	/**
	 * 是否开启进行bind服务
	 */
	private boolean isStarted;
	/**
	 * 服务是否已绑定
	 */
	private boolean isBinded;
	private Context mContext;


	public String getPckName() {
		return pckName;
	}

	public void setPckName(String pckName) {
		this.pckName = pckName;
	}

	public String getmServiceAction() {
		return mServiceAction;
	}

	/**
	 * 获取第三方应用的appId
	 *
	 * @return
	 */
	public int getAppId() {
		return appId;
	}

	/**
	 * 构造方法
	 *
	 * @param context
	 * @param appId 第三方AppId
	 */
	public SpeechServiceUtil(Context context, int appId) {
		Log.e(TAG, "SpeechServiceUtil==>>");
		this.appId = appId;
//		this.mServiceAction=getActByAppId(appId);
		this.mContext = context;
		startService();
	}


	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		Log.e(TAG, "bind+mServiceAction=" + mServiceAction);
		isBinded = true;
		mMessenger = new Messenger(service);
		isStarted = false;
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		Log.e(TAG, "unbind+mServiceAction=" + mServiceAction);
		isBinded = false;
		mMessenger = null;
	}

	/**
	 * 检查service bind的情况
	 */
	public void checkBindService() {
		/**检测是否已start或bind服务*/
		if (!isBinded && !isStarted) {
			startService();
		}
	}

	/**
	 * 调起第三方app speech service在前台
	 */
	public void callAppServiceOnStart() {
		Log.e(TAG, "callAppServiceOnStart()==>>action=" + mServiceAction);
		checkBindService();
		Message msg = assembleMsg(null, MSG_DEV_APP_ONSTART);
		sendMsg(msg);
	}

	/**
	 * 调起第三方app speech service到后台
	 */
	public void callAppServiceOnStop() {
		Log.e(TAG, "callAppServiceOnStop()==>>action=" + mServiceAction);
		Message msg = assembleMsg(null, MSG_DEV_APP_ONSTOP);
		sendMsg(msg);
	}

	/**
	 * 往对应第三方的语音服务发数据信息
	 */
	public void sendMsg(String msgStr) {
		Log.e(TAG, "mServiceAction=" + mServiceAction + ">>1msgStr==" + msgStr);
		mBindCount = 0;
		Message msg = assembleMsg(msgStr, MSG_DEV_APP_ONRESULT);
		sendMsg(msg);
	}

	/**
	 * 发送封装好的msg
	 *
	 * @param msg
	 */
	private void sendMsg(final Message msg) {
		if (mMessenger != null) {
			/**bind计数器置零*/
			mBindCount = 0;
			Log.e(TAG, "sendMsg+mMessenger!=null===>>");
			try {
				mMessenger.send(msg);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} else/**防止第一次bind有点延迟*/ {
			Log.e(TAG, "mBindCount=" + mBindCount + ">>mServiceAction=" + mServiceAction + ">>2msgStr==isBinded=" + isBinded);

			if (!isBinded && !TextUtils.isEmpty(mServiceAction) && mBindCount < BIND_AGAIN_LIMITED_COUNT) {
				mBindCount++;
				TimerTask task = new TimerTask() {

					@Override
					public void run() {
						sendMsg(msg);
						cancel();
					}
				};
				new Timer().schedule(task, BIND_DELAY_TIME);
			}
		}
	}

	/**
	 * 组装消息体
	 *
	 * @param msgStr 要发送给第三方Service的speech文本内容
	 * @param msgTypeId 信息的类型ID  0:onResult 1:onStart 2:onStop
	 * @return 组装完包含appId, speech文本内容的 Message
	 */
	private Message assembleMsg(String msgStr, int msgTypeId) {
		Message msg = new Message();
//		msg.replyTo=mServerMessenger;
		msg.what = appId;
		msg.arg1 = msgTypeId;
		if (!TextUtils.isEmpty(msgStr)) {
			Bundle bundle = new Bundle();
			bundle.putString(SPEECH_RST, msgStr);
			msg.setData(bundle);
		}
		return msg;
	}

	/**
	 * 开启第三方speech Service
	 */
	private void startService() {
		/**检测serAct，有可能是检测到刚安装的dev包信息*/
		this.mServiceAction = getActByAppId(appId);
		if (TextUtils.isEmpty(mServiceAction)) {
			Log.e(TAG, "startService==>>app isn't alpha dev app");
			return;
		}
		Log.e(TAG, "startService==>>+mServiceAction=" + mServiceAction);
		isStarted = true;
		Intent intent = new Intent(mServiceAction);
		pckName = mServiceAction.substring(0, mServiceAction.lastIndexOf("."));
		Log.e(TAG, "service pckName=" + pckName);
		intent.setPackage(pckName);
		mContext.bindService(intent, this, Context.BIND_AUTO_CREATE);
	}

	/**
	 * 停止第三方speech service
	 */
	public void stopService() {
		mContext.unbindService(this);
	}

	/**
	 * 根据appId获取app service act
	 *
	 * @param appId
	 * @return service act
	 */
	private String getActByAppId(int appId) {
	//	return AppManager.getInstance().getAppServiceMap().get(appId);
	 return null;
	}


}
