package com.ubtechinc.alpha.task;

import android.content.Context;

import com.ubtech.utilcode.utils.LogUtils;
import com.ubtech.utilcode.utils.notification.NotificationCenter;
import com.ubtech.utilcode.utils.notification.Subscriber;
import com.ubtech.utilcode.utils.thread.ThreadPool;
import com.ubtechinc.alpha.CmStartApp;
import com.ubtechinc.alpha.CmStopApp;
import com.ubtechinc.alpha.app.AlphaApplication;
import com.ubtechinc.alpha.appmanager.AppManager;
import com.ubtechinc.alpha.event.AppManageEvent;
import com.ubtechinc.alpha.utils.SoundVolumesUtils;
import com.ubtechinc.nets.im.service.RobotPhoneCommuniteProxy;


/**
 * @desc : App管理
 * @author: wzt
 * @time : 2017/5/22
 * @modifier:
 * @modify_time:
 */

public class ProxyAppManageImpl extends AbstractProxyService {
    private static final String APP_START = "start";
    private static final String APP_STOP = "stop";

    public ProxyAppManageImpl(Context context) {
    }

    private Subscriber<AppManageEvent> appManageEventSubscriber = new Subscriber<AppManageEvent>() {
        @Override
        public void onEvent(final AppManageEvent event) {
            if(APP_START.equals(event.event)) {

                ThreadPool.runOnNonUIThread(new Runnable() {
                    @Override
                    public void run() {
                        SoundVolumesUtils.get(AlphaApplication.getContext()).playSound(4);

                        AppManager.getInstance().startApp(event.packageName,
                                event.clientIP,
                                event.srcApp,

                                event.angle);
                    }
                });

                CmStartApp.CmStartAppResponse.Builder builder =  CmStartApp.CmStartAppResponse.newBuilder();
                builder.setIsSuccess(true);
                LogUtils.i( "StartApp response result = " + builder.getIsSuccess());
                RobotPhoneCommuniteProxy.getInstance().sendResponseMessage(event.responseCmdID,"1",event.requestSerial, builder.build() , event.peer, null);
            } else if(APP_STOP.equals(event.event)) {
                ThreadPool.runOnNonUIThread(new Runnable() {
                    @Override
                    public void run() {
                        AppManager.getInstance().stopApp(event.packageName);
                    }
                });
                CmStopApp.CmStopAppResponse.Builder builder =  CmStopApp.CmStopAppResponse.newBuilder();
                builder.setIsSuccess(true);
                RobotPhoneCommuniteProxy.getInstance().sendResponseMessage(event.responseCmdID,"1",event.requestSerial, builder.build() , event.peer, null);
            }


        }
    };

    @Override
    public void registerEvent() {
        NotificationCenter.defaultCenter().subscriber(AppManageEvent.class, appManageEventSubscriber);
    }

    @Override
    public void unregisterEvent() {
        NotificationCenter.defaultCenter().unsubscribe(AppManageEvent.class, appManageEventSubscriber);
    }
}
