/*
 *
 *  *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.ubtechinc.alpha.task;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.UserHandle;

import com.ubtech.utilcode.utils.LogUtils;
import com.ubtech.utilcode.utils.notification.NotificationCenter;
import com.ubtech.utilcode.utils.notification.Subscriber;
import com.ubtech.utilcode.utils.thread.ThreadPool;
import com.ubtechinc.alpha.behavior.RobotStandup;
import com.ubtechinc.alpha.behavior.RobotTakeARest;
import com.ubtechinc.alpha.event.WakeUpEvent;
import com.ubtechinc.alpha.event.WakeUpTaskEvent;
import com.ubtechinc.alpha.ops.OpResult;
import com.ubtechinc.alpha.ops.RobotOpsManager;
import com.ubtechinc.alpha.ops.motor.MoveAbsAngleOp;
import com.ubtechinc.alpha.ops.motor.ReadAngleOp;
import com.ubtechinc.alpha.robotinfo.RobotState;
import com.ubtechinc.alpha.sdk.SdkConstants;
import com.ubtechinc.alpha.serial.SerialConstants;
import com.ubtechinc.alpha.serverlibutil.aidl.ITtsCallBackListener;
import com.ubtechinc.alpha.speech.SpeechServiceProxy;
import com.ubtechinc.alpha.utils.AlphaUtils;
import com.ubtechinc.alpha.utils.AngleCheckUtils;
import com.ubtechinc.alpha.utils.StringUtil;
import com.ubtechinc.alpha.utils.SysUtils;
import com.ubtechinc.alpha2services.R;

/**
 * @author paul.zhang@ubtrobot.com
 * @date 2017/2/8
 * @Description 麦克风阵列唤醒给出角度、根据该角度做相应处理、如转头、以及其它闪灯、TTS任务
 * @modifier logic.peng 5mic唤醒处理
 * @modify_time
 */

public class ProxyWakupTaskImpl extends AbstractProxyService {
	private Ringtone mRingtone;
	private Context mContext;
	private int curHeaderAngle = 0;

	public ProxyWakupTaskImpl(Context context) {
		this.mContext = context;
		this.mRingtone = RingtoneManager.getRingtone(context, SysUtils.getMediaUri(context));
		this.mRingtone.setStreamType(AudioManager.STREAM_MUSIC);

	}

	@Override
	public void registerEvent() {
//		NotificationCenter.defaultCenter().subscriber(WakeUpTaskEvent.class, mSubscriber);
		NotificationCenter.defaultCenter().subscriber(WakeUpEvent.class, mWakeupSubscriber);
	}

	@Override
	public void unregisterEvent() {
//		NotificationCenter.defaultCenter().unsubscribe(WakeUpTaskEvent.class, mSubscriber);
		NotificationCenter.defaultCenter().unsubscribe(WakeUpEvent.class, mWakeupSubscriber);
	}

	protected Subscriber<WakeUpTaskEvent> mSubscriber = new Subscriber<WakeUpTaskEvent>() {

		@Override
		public void onEvent(WakeUpTaskEvent event) {
//			LogUtils.i(TAG, "麦克风阵列唤醒角度:%d", event.angle);
//			if (event.angle == -1) {
//				return;
//			}
//			int angle = SpeechUtils.getAngle(event.angle);
//			float radian = SpeechUtils.getRadian(angle);
//			LogUtils.i(TAG, "机器人旋转角度:%d 弧度:%f", angle, radian);
//			//19号舵机移动到一个相对角度
//			RobotOpsManager.get(mContext).executeOp(new MoveRefAngleOp((byte)19, angle, (short)500));
		}
	};

	protected Subscriber<WakeUpEvent> mWakeupSubscriber = new Subscriber<WakeUpEvent>() {

		@Override
		public void onEvent(final WakeUpEvent event) {
			LogUtils.I("麦克风阵列唤醒角度: %d", event.angle);
			//先上电
			if(RobotState.get().isInPowerSave()) {
				RobotTakeARest.instance().start(false);
			}
			AlphaUtils.interruptAlphaNoIntent(mContext);
			ThreadPool.runOnNonUIThread(new Runnable() {
				@Override
				public void run() {
					handleAngle(event.angle);
					new RobotStandup(mContext).start();
				}
			});
			//tts："干啥"
			SpeechServiceProxy.getInstance().speechStartTTS(StringUtil.getString(R.string.hint_wakeup_3), new ITtsCallBackListener() {
				@Override
				public void onBegin() throws RemoteException {

				}

				@Override
				public void onEnd() throws RemoteException {
					//最后通知第三方应用
					AlphaUtils.sendInterruptIntent(mContext);
				}

				@Override
				public IBinder asBinder() {
					return null;
				}
			});
		}
	};

	private void handleAngle(int angle){
		if (curHeaderAngle == 0) {
			OpResult<Short> motorAngle = RobotOpsManager.get(mContext).executeOpSync(new ReadAngleOp((byte) 19, false));
			if (motorAngle == null) return;
			curHeaderAngle = motorAngle.errorCode != SerialConstants.ERR_OK ?0 : motorAngle.data;
		}
		final int curAngle = curHeaderAngle;
		LogUtils.I("19号舵机当前角度: %d", curAngle);
		int detlaAngle = 0;
		if(angle > 270) { /** 机器人的左边 **/
			detlaAngle = (byte)(angle - 360);
		} else if(angle < 90) {/** 机器人的右边 **/
			detlaAngle = (byte)(angle);
		}
		LogUtils.I("19号舵机delta角度：%d", detlaAngle);
		int newAngle = AngleCheckUtils.limitAngle(19, curAngle + detlaAngle);
		curHeaderAngle = newAngle;
		LogUtils.I("19号舵机新角度: %d", newAngle);
		if (newAngle > 10)
			RobotOpsManager.get(mContext).executeOp(new MoveAbsAngleOp((byte)19, newAngle, (short)500));
	}
}
