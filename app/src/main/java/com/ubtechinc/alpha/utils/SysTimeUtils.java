package com.ubtechinc.alpha.utils;

import android.app.AlarmManager;
import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;

import com.ubtech.SystemExt;
import com.ubtech.utilcode.utils.Utils;
import com.ubtechinc.alpha.app.AlphaApplication;

import java.io.IOException;
import java.util.Calendar;


/**
 * @author wzt
 * @date 2017/5/22
 * @Description 系统时间设置工具类
 * @modifier
 * @modify_time
 */

public class SysTimeUtils {

    public static void setTimezone(String timezone) {
        SystemExt.setSystemCmd("setprop persist.sys.timezone " + timezone);
        SystemExt.setSystemCmd("TZ='" + timezone + "';export TZ");
    }

    public static boolean setDateTime(int year, int month, int day, int week,
                                      int hour, int minute, int second, String tz) throws IOException, InterruptedException {
//        requestPermission();

        // 手机与机器人时间差小，就不去设置
        if(compareTime(year, month, day, week, hour, minute)) {
            return false;
        }

        if (SysUtils.is5Mic()){
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month-1);
            c.set(Calendar.DAY_OF_MONTH, day);
            c.set(Calendar.DAY_OF_WEEK, week);
            c.set(Calendar.HOUR_OF_DAY, hour);
            c.set(Calendar.MINUTE, minute);
            long when = c.getTimeInMillis();

            // 5麦通过Android系统api去设置系统时间，目前还差取消自动同步时间和时区的操作
            if (when / 1000 < Integer.MAX_VALUE) {
                SystemClock.setCurrentTimeMillis(when);
            }

            // 5麦通过Android系统api去设置时区
            AlarmManager timeZone= (AlarmManager) AlphaApplication.getContext().getSystemService(Context.ALARM_SERVICE);
            if(!TextUtils.isEmpty(tz)) {
                timeZone.setTimeZone(tz);
            } else {
                //timeZone.setTimeZone("America/Los_Angeles");
            }
        }else{
            StringBuilder sb = new StringBuilder();
            sb.append(year).append("");
            if(month < 10) {
                sb.append("0").append(month);
            } else {
                sb.append(month);
            }

            if(day < 10) {
                sb.append("0").append(day);
            } else {
                sb.append(day);
            }

            sb.append(".");
            if(hour < 10) {
                sb.append("0").append(hour);
            } else {
                sb.append(hour);
            }
            sb.append("");
            if(minute < 10) {
                sb.append("0").append(minute);
            } else {
                sb.append(minute);
            }
            sb.append("");
            if(second < 10) {
                sb.append("0").append(second);
            } else {
                sb.append(second);
            }
            //       sb.append("00");
            try {
                // 2麦通过系统专门提供的API去设置系统时间
                SystemExt.setSystemCmd("/system/bin/date -s " + sb.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        long now = Calendar.getInstance().getTimeInMillis();
        return true;
        //Log.d(TAG, "set tm="+when + ", now tm="+now);

    }

    private static boolean compareTime(int year, int month, int day, int week, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        int currYear = calendar.get(Calendar.YEAR);
        int currMonth = calendar.get(Calendar.MONTH);
        int currDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currHour = calendar.get(Calendar.HOUR);
        int currMinute = calendar.get(Calendar.MINUTE);

        boolean isSame = false;
        if(currYear == year) {
            if(currMonth == month) {
                if(currDay == day) {
                    if(currHour == hour) {
                        if((currMinute - minute) < 3){
                            isSame = true;
                        }
                    }
                }
            }
        }

        return isSame;
    }
}
