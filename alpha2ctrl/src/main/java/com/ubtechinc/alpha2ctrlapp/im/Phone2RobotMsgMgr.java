package com.ubtechinc.alpha2ctrlapp.im;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.google.protobuf.GeneratedMessageLite;
import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtech.utilcode.utils.notification.NotificationCenter;
import com.ubtech.utilcode.utils.notification.Subscriber;
import com.ubtechinc.alpha.AlphaMessageOuterClass;
import com.ubtechinc.alpha.event.IMLoginResultEvent;
import com.ubtechinc.alpha.im.TecentIMManager;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.nets.phonerobotcommunite.ICallback;
import com.ubtechinc.nets.phonerobotcommunite.RobotCommuniteManager;

/**
 * Created by Administrator on 2017/5/26.
 */

public class Phone2RobotMsgMgr <T>{
    private static Phone2RobotMsgMgr sInstance;
    private final static String TAG = "Phone2RobotMsgMgr";
    private IMLoginSubscriber loginSubscriber = new IMLoginSubscriber();

    public static Phone2RobotMsgMgr getInstance() {
        if(sInstance == null) {
            synchronized (Phone2RobotMsgMgr.class) {
                if(sInstance == null) {
                    sInstance = new Phone2RobotMsgMgr();
                }
            }
        }
        return sInstance;
    }

    public void init() {
        RobotCommuniteManager.getInstance().init();
        RobotCommuniteManager.getInstance().setMsgDispathcer(new ImPhoneClientMsgDispatcher());
        new Handler(Looper.getMainLooper()).post(new Runnable() { //TIMManager要求在主线程或有Looper的线程中初始化
            @Override
            public void run() {
                TecentIMManager.getInstance(Alpha2Application.getInstance()).init(Alpha2Application.getInstance().getUserId());
            }
        });
        NotificationCenter.defaultCenter().subscriber(IMLoginResultEvent.class, loginSubscriber);
    }


    private class IMLoginSubscriber implements Subscriber<IMLoginResultEvent> {

        @Override
        public void onEvent(IMLoginResultEvent imLoginResultEvent) {
            if(imLoginResultEvent != null && imLoginResultEvent.success) {
                Logger.d(TAG, "IMLogin success");
            }else {
                Logger.i(TAG, "IMLogin fail");
            }
        }
    }





    public void sendData(int cmdId,String version, GeneratedMessageLite requestBody, String peer, @NonNull ICallback<AlphaMessageOuterClass.AlphaMessage> dataCallback) {
        RobotCommuniteManager.getInstance().sendData(cmdId,version,requestBody,peer,dataCallback);
    }


    public void sendDataToRobot(int cmdId,String version, GeneratedMessageLite requestBody, String peer, @NonNull ICallback<T> dataCallback) {

        RobotCommuniteManager.getInstance().sendDataToRobot(cmdId,version,requestBody,peer,dataCallback);
    }
}
