package com.ubtechinc.alpha.im;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.google.protobuf.GeneratedMessageLite;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtech.utilcode.utils.notification.NotificationCenter;
import com.ubtech.utilcode.utils.notification.Subscriber;
import com.ubtechinc.alpha.AlphaMessageOuterClass;
import com.ubtechinc.alpha.app.AlphaApplication;
import com.ubtechinc.alpha.event.PowerOffEvent;
import com.ubtechinc.alpha.robotinfo.RobotState;
import com.ubtechinc.nets.phonerobotcommunite.ICallback;
import com.ubtechinc.nets.phonerobotcommunite.RobotCommuniteManager;

/**
 * Created by Administrator on 2017/5/25.
 */

public class Robot2PhoneMsgMgr {
    private static Robot2PhoneMsgMgr sInstance;
    private static String TAG = "Robot2PhoneMsgMgr";

    public static Robot2PhoneMsgMgr getInstance() {
        if(sInstance == null) {
            synchronized (Robot2PhoneMsgMgr.class) {
                if(sInstance == null) {
                    sInstance = new Robot2PhoneMsgMgr();
                }
            }
        }
        return sInstance;
    }

    public void init() {
        LogUtils.i("init");
        RobotCommuniteManager.getInstance().init();
        RobotCommuniteManager.getInstance().setMsgDispathcer(new ImMainServiceMsgDispatcher());
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                TecentIMManager.getInstance(AlphaApplication.getContext()).init(RobotState.get().getSid());
            }
        });
        NotificationCenter.defaultCenter().subscriber(PowerOffEvent.class,powerOffSubscriber);

    }

    Subscriber powerOffSubscriber = new Subscriber() {
        @Override
        public void onEvent(Object o) {
            TecentIMManager.getInstance(AlphaApplication.getContext()).logout();
        }
    };


    public void sendData(int cmdId,String version,@NonNull GeneratedMessageLite requestBody, String peer, @NonNull ICallback<AlphaMessageOuterClass.AlphaMessage> dataCallback) {
        RobotCommuniteManager.getInstance().sendData(cmdId,version,requestBody,peer,dataCallback);
    }


}
