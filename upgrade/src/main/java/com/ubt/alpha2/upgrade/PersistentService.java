package com.ubt.alpha2.upgrade;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.service.notification.NotificationListenerService;

@SuppressLint("OverrideAbstract")
public class PersistentService extends NotificationListenerService {

    @Override
    public void onCreate(){
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        return super.onStartCommand(intent,flags,startId);
    }
}
