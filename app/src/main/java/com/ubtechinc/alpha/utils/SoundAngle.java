/*
 *
 *  *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.ubtechinc.alpha.utils;

import com.ubtech.utilcode.utils.notification.NotificationCenter;
import com.ubtechinc.alpha.event.WakeUpEvent;
import com.ubtechinc.alpha.speech.wakeuper.IWakeuperListener;

/**
 * @author paul.zhang@ubtrobot.com
 * @date 2017/2/10
 * @Description 讯飞麦克风阵列通过JNI硬中断方式唤醒
 * @modifier
 * @modify_time
 */

public class SoundAngle {

	private boolean isInit = false;

	static {
		System.loadLibrary("SoundLocatorJni");
	}

	public SoundAngle() {
		openDevice();
		initWakeup();
	}

	public static SoundAngle get() {
		return SoundAngleInstance.INSTANCE;
	}

	public static class SoundAngleInstance {
		public static SoundAngle INSTANCE = new SoundAngle();
	}

	public native int startWakeup();

	public native boolean setBeam(int beam);

	public native boolean reset();

	public native boolean openDevice();

	public native boolean closeDevice();

	public native boolean initWakeup();

	public native boolean releaseWakeup();

	public void notifyWakeup(int angle) {
		notifyEvent(angle);
	}

	public void notifyEvent(int angle) {
		WakeUpEvent event = new WakeUpEvent();
		event.angle = angle;
		NotificationCenter.defaultCenter().publish(event);
	}

	public void init(IWakeuperListener listener){
		if (isInit){
			return;
		}
		isInit = true;
		SoundAngle.get().setBeam(0);
		SoundAngle.get().startWakeup();
		if (listener != null){
			listener.onError(-1);
		}
	}

}
