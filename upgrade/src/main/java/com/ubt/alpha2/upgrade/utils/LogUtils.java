package com.ubt.alpha2.upgrade.utils;

import android.util.Log;

import com.ubt.alpha2.upgrade.BuildConfig;
import com.ubtechinc.alpha.serverlibutil.service.BinderFetchServiceUtil;

/**
 * Created by ubt on 2017/6/27.
 */

public class LogUtils {
    private static final String TAG = "slive";

    public static void d(String tag,String msg){
        Log.d(tag,msg);
    }

    public static void d(String msg){
        if(BuildConfig.isDebug)
            d(TAG,msg);
    }

    public static void e(String tag,String msg){
        //if(BuildConfig.DEBUG)
            Log.e(tag,msg);
    }

    public static void logException(Exception e){
        if(BuildConfig.DEBUG){
            logException(TAG,e);
        }
    }

    public static void logException(String tag,Exception e){
        if(BuildConfig.DEBUG){
            e(tag,Log.getStackTraceString(e));
        }
    }

    public static void e(String msg){
        e(TAG,msg);
    }
}
