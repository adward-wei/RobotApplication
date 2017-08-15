package com.ubtechinc.alpha.deskclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha.app.AlphaApplication;
import com.ubtechinc.alpha.ops.alarm.AlarmInfo;

import java.text.SimpleDateFormat;



/**
 * Created by Administrator on 2017/6/14 0014.
 */

public class DeskclockManager {

    private static final String TAG="DeskclockManager";
    public static final String ALARM_ALERT_ACTION = "com.ubtechinc.alpha2services.ALARM_ALERT";
    public static final String ALARM_DONE_ACTION = "com.android.deskclock.ALARM_DONE";
    public static final String ALARM_SNOOZE_ACTION = "com.android.deskclock.ALARM_SNOOZE";
    public static final String ALARM_DISMISS_ACTION = "com.android.deskclock.ALARM_DISMISS";
    public static final String ALARM_KILLED = "alarm_killed";
    public static final String ALARM_KILLED_TIMEOUT = "alarm_killed_timeout";
    public static final String ALARM_ALERT_SILENT = "silent";
    public static final String ALARM_RAW_DATA = "intent.extra.alarm_raw";

    private DeskclockManager(){}

    public static volatile DeskclockManager  deskclockManager ;

    public static DeskclockManager getDeskclockManager(){
        if(deskclockManager == null){
            synchronized (DeskclockManager.class){
                if(deskclockManager == null){
                    deskclockManager =new DeskclockManager();
                }
            }
        }
        return deskclockManager;
    }

    public void enableDeskclock(AlarmInfo alarm){
        AlarmManager alarmManager = (AlarmManager) AlphaApplication.getContext().getSystemService(Context.ALARM_SERVICE);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dt = df.format(alarm.dttime);
        LogUtils.i(TAG,"dttime:"+dt +" alarm id:"+alarm.id);

        Intent alarmIntent = new Intent(ALARM_ALERT_ACTION);
        alarmIntent.putExtra("alarm_id",alarm.id);
        alarmIntent.putExtra("alarm_label",alarm.label);
        PendingIntent sender = PendingIntent.getBroadcast(AlphaApplication.getContext(), 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarm.dttime, sender);
    }
}
