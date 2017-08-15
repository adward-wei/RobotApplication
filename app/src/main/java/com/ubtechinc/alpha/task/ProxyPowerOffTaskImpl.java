/*
 *
 *  *
 *  *  *
 *  *  * Copyright (c) 2008-2017 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *  *
 *  *
 *
 */

package com.ubtechinc.alpha.task;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;

import com.ubtech.utilcode.utils.notification.NotificationCenter;
import com.ubtech.utilcode.utils.notification.Subscriber;
import com.ubtech.utilcode.utils.thread.HandlerUtils;
import com.ubtechinc.alpha.appmanager.AppManager;
import com.ubtechinc.alpha.ops.ActionServiceProxy;
import com.ubtechinc.alpha.serverlibutil.aidl.ITtsCallBackListener;
import com.ubtechinc.alpha.service.ProxyServiceManager;
import com.ubtechinc.alpha.speech.SpeechServiceProxy;
import com.ubtechinc.alpha2services.R;
import com.ubtechinc.alpha.affairdispatch.constants.AffairPriority;
import com.ubtechinc.alpha.event.PowerOffEvent;
import com.ubtechinc.alpha.ops.RobotOpsManager;
import com.ubtechinc.alpha.ops.sys.ShutdownOps;

import java.lang.reflect.Method;
import java.util.Random;

import timber.log.Timber;

/**
 * @desc : 关机清理
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/26
 * @modifier:
 * @modify_time:
 */

public class ProxyPowerOffTaskImpl extends AbstractProxyService {
    private Context mContext;
    private volatile  boolean isPoweroffing = false;
    private Subscriber<PowerOffEvent> mPoweroffSubscriber = new Subscriber<PowerOffEvent>() {
        @Override
        public void onEvent(PowerOffEvent powerOffEvent) {
            if (isPoweroffing) return;
            isPoweroffing = true;
            String[] tts = mContext.getResources().getStringArray(R.array.robotManualShutDown);
            RobotOpsManager.get(mContext).stopAction(null);
            ActionServiceProxy.getInstance().clearAllPendingActions();
            SpeechServiceProxy.getInstance().clearAllPendingTTS();
            AppManager.getInstance().shutdown();
            SpeechServiceProxy.getInstance().speechStartTTS(tts[new Random(System.currentTimeMillis()).nextInt(tts.length)], AffairPriority.PRIORITY_MAX, new ITtsCallBackListener() {
                @Override
                public void onBegin() throws RemoteException {

                }

                @Override
                public void onEnd() throws RemoteException {
                    SpeechServiceProxy.getInstance().switchWakeup(false);//关闭语音唤醒
                    RobotOpsManager.get(mContext).stopAction(null);
                    ActionServiceProxy.getInstance().clearAllPendingActions();
                    SpeechServiceProxy.getInstance().clearAllPendingTTS();
                    RobotOpsManager.get(mContext).executeOpSync(new ShutdownOps());
                    ProxyServiceManager.get(mContext).getSerialCommandProxy().onDestroy();
                    HandlerUtils.runUITask(new Runnable() {
                        @Override
                        public void run() {
                            shutdownAndroid();
                            Runtime.getRuntime().exit(0);
                        }
                    }, 3000);
                }
                @Override
                public IBinder asBinder() {
                    return null;
                }
            });
        }
    };

    public ProxyPowerOffTaskImpl(Context cxt){
        this.mContext = cxt;
    }

    @Override
    public void registerEvent() {
        NotificationCenter.defaultCenter().subscriber(PowerOffEvent.class, mPoweroffSubscriber);
    }

    @Override
    public void unregisterEvent() {
        NotificationCenter.defaultCenter().unsubscribe(PowerOffEvent.class, mPoweroffSubscriber);
    }

    private void shutdownAndroid() {
        try {
            Timber.d("", "!!!! shutdownAndroid");
            // 获得ServiceManager类
            Class<?> ServiceManager = Class
                    .forName("android.os.ServiceManager");

            // 获得ServiceManager的getService方法
            Method getService = ServiceManager.getMethod("getService",
                    String.class);

            // 调用getService获取RemoteService
            Object oRemoteService = getService.invoke(null,
                    Context.POWER_SERVICE);
            // 获得IPowerManager.Stub类
            Class<?> cStub = Class.forName("android.os.IPowerManager$Stub");
            // 获得asInterface方法
            Method asInterface = cStub.getMethod("asInterface",
                    android.os.IBinder.class);
            // 调用asInterface方法获取IPowerManager对象
            Object oIPowerManager = asInterface.invoke(null, oRemoteService);
            // 获得shutdown()方法
            Method shutdown = oIPowerManager.getClass().getMethod("shutdown",
                    boolean.class, boolean.class);
            // 调用shutdown()方法
            shutdown.invoke(oIPowerManager, false, true);
        } catch (Exception e) {
            Timber.e(e.toString(), e);
        }
    }
}
