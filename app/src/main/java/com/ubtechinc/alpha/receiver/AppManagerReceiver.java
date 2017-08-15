package com.ubtechinc.alpha.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.ubtech.utilcode.utils.notification.NotificationCenter;
import com.ubtechinc.alpha.app.AlphaApplication;
import com.ubtechinc.alpha.event.ReceiveAppButtonEvent;
import com.ubtechinc.alpha.event.ReceiveAppConfigEvent;
import com.ubtechinc.alpha.model.StaticValue;
import com.ubtechinc.alpha.model.app.UbtAppButtonEventData;
import com.ubtechinc.alpha.model.app.UbtAppConfigData;

/**
 * @date 2017/6/9
 * @author wzt
 * @Description App管理器接收第三方应用的广播
 * @modifier
 * @modify_time
 */

public class AppManagerReceiver extends BroadcastReceiver {

    public static final String TAG = "AppManagerReceiver";
    private static AppManagerReceiver instance;
    public static AppManagerReceiver getInstance() {
        if(instance == null) {
            synchronized (AppManagerReceiver.class) {
                if (instance == null) {
                    instance = new AppManagerReceiver();
                }
            }
        }
        return instance;
    }

    public void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        // 第三方app回复
        filter.addAction(StaticValue.APP_CONFIG_BACK);
        filter.addAction(StaticValue.APP_BUTOON_EVENT_BACK);
        AlphaApplication.getContext().registerReceiver(this, filter);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        String action = intent.getAction();
        Log.e(TAG, "third app info = " + action);

        //

        if (action.equals(StaticValue.APP_CONFIG_BACK)) {

            Bundle bundle = intent.getExtras();
            UbtAppConfigData developer = (UbtAppConfigData) bundle
                    .getSerializable("appconfig");

            ReceiveAppConfigEvent event = new ReceiveAppConfigEvent();
            event.cmd = developer.getCmd();
            event.datas = developer.getDatas();
            event.packageName = developer.getPackageName();
            event.tags = developer.getTags();
            NotificationCenter.defaultCenter().publish(event);

        } else if (action
                .equals(StaticValue.APP_BUTOON_EVENT_BACK)) {

            Bundle bundle = intent.getExtras();

            UbtAppButtonEventData developer = (UbtAppButtonEventData) bundle
                    .getSerializable("appbutton");

            ReceiveAppButtonEvent event = new ReceiveAppButtonEvent();
            event.cmd = developer.getCmd();
            event.datas = developer.getDatas();
            event.packageName = developer.getPackageName();
            NotificationCenter.defaultCenter().publish(event);
        }

    }
}