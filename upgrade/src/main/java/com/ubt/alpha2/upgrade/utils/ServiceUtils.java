package com.ubt.alpha2.upgrade.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ubt.alpha2.upgrade.UpgradeService;

/**
 * Created by ubt on 2017/6/28.
 */

public class ServiceUtils {

    // 主服务包名
    public static final String MAINSERVICE_PACKAGE_NAME = "com.ubtechinc.alpha2services";
    //主服务的action
    public static final String MAINSERVICE_ACTION = "com.ubtechinc.alpha.service.MainService";

    //OTA升级包名"com.ubtechinc.alpha2systemupdate"
    public static final String OTA_SERVICE_PACKAGE_NAME = "com.ubtechinc.alpha2systemupdate";
    //OTA 服务action
    public static final String OTAG_ACTION = "com.ubtechinc.systemupdate.START_TIMER.ACTION";
    /**
     * startService
     */
    public static void startService(Context mContext){
        LogUtils.d("startService: "+Log.getStackTraceString(new Throwable()));
        Intent intent = new Intent(mContext,UpgradeService.class);
        intent.putExtra(Constants.SELF_REPLACE_RESTART,true);
        mContext.startService(intent);
    }

    /**
     * stopService
     */
    public void stopService(Context mContext){
        Intent intent = new Intent(Constants.UPGRADE_ACTION);
        mContext.stopService(intent);
    }

    //启动主服务
    public static void startMainService(Context context){
        Intent intent = new Intent();
        intent.setAction(MAINSERVICE_ACTION) ;
        intent.setPackage(MAINSERVICE_PACKAGE_NAME);
        context.startService(intent);
    }

    //启动OTA升级
    public static void startOTAService(Context context){
        Intent intent = new Intent();
        intent.setAction(OTAG_ACTION) ;
        intent.setPackage(OTA_SERVICE_PACKAGE_NAME);
        context.startService(intent);
    }

}
