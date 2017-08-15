package com.ubt.alpha2.upgrade.bean;

import android.text.TextUtils;

import com.ubt.alpha2.upgrade.UpgradeApplication;
import com.ubt.alpha2.upgrade.utils.ApkUtils;
import com.ubt.alpha2.upgrade.utils.Constants;
import com.ubt.alpha2.upgrade.utils.FileManagerUtils;
import com.ubt.alpha2.upgrade.utils.FilePathUtils;
import com.ubt.alpha2.upgrade.utils.LogUtils;
import com.ubt.alpha2.upgrade.utils.VersionUtils;
import com.ubtechinc.alpha.sdk.sys.SysApi;

import java.io.File;

/**
 * Created by ubt on 2017/7/24.
 */

public class UpgradeModel {

    /** android 系统的升级*/
    public static final String ANDROID_OS = "android";

    /** 嵌入式胸版升级 **/
    public static final String MODEL_EMBEDDED_CHEST = "embedded_chest";
    /** 嵌入式头版升级 **/
    public static final String MODEL_EMBEDDED_HEAD = "embedded_head";
    /** 嵌入式舵机升级 **/
    public static final String MODEL_EMBEDDED_MOTOR = "embedded_motor";
    /** 嵌入式电池包升级 **/
    public static final String MODEL_EMBEDDED_BATTERY = "embedded_battery";
    /** 动作文件升级 **/
    public static final String MODEL_ACTION_FILE = "action_file";

    /** 主服务模块名称 **/
    public static final String MAINSERVICE_MODLE_NAME = "mainservice";
    /** 升级本身Apk模块名称 **/
    public static final String SELF_MODLE_NAME = "selfupgrade";

    /**
     * Android系统版本信息:大版本号.小版本号.日期.buildID
     */
    public static final String SYSTEM_VERSION = "ro.product.version";

    public static String getModuleVersion(String moduleName){
        if(TextUtils.isEmpty(moduleName))
            return null;
        if(moduleName.equals(ANDROID_OS)){
            return getAndroidOsVersion();
        }

        if(moduleName.equals(SELF_MODLE_NAME)){
            return getSelfModuleVersion();
        }

        if(moduleName.equals(MODEL_EMBEDDED_CHEST)){
            return getChestModuleVersion();
        }

        if(moduleName.equals(MODEL_EMBEDDED_BATTERY)){
            return getBatteryModuleVersion();
        }

        if(moduleName.equals(MAINSERVICE_MODLE_NAME)){
            return getMainServiceModuleVersion();
        }
        return null;
    }

    private static String getAndroidOsVersion(){
        return ApkUtils.getAndroidSystemVersion();
    }

    private static String getSelfModuleVersion(){
        return ApkUtils.getVersionName(UpgradeApplication.getContext().getPackageName());
    }

    private static String getMainServiceModuleVersion(){
        File localConfig = new File(FilePathUtils.LOCAL_CONFIG_PATH);
        if(!localConfig.exists()){
            FileManagerUtils.writeLocalConfig();
        }
        VersionConfigs versionConfigs = VersionUtils.getVersionConfigs(FilePathUtils.LOCAL_CONFIG_PATH);
        if(versionConfigs != null)
            return versionConfigs.version;
        return null;
    }

    private static String getChestModuleVersion(){
        SysApi mSysApi = SysApi.get();
        mSysApi.initializ(UpgradeApplication.getContext());
        String chestVersion = mSysApi.getChestVersion();
        LogUtils.d("chestVersion: "+chestVersion);
        if(TextUtils.isEmpty(chestVersion))
            return null;
        int index1 = chestVersion.indexOf("-V");
        if(index1<0)
            return null;
        int index2 = chestVersion.lastIndexOf("-");
        if(index2<0)
            return null;
        String version = chestVersion.substring(index1+2,index2);
        LogUtils.d("version: "+version);
        char[] chars = version.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0;i<chars.length;i++){
            stringBuilder.append(chars[i]);
            if(i<chars.length-1)
                stringBuilder.append('.');
        }
        version = stringBuilder.toString();
        LogUtils.d("version222: "+version);
        return "v"+version;
    }

    private static String getBatteryModuleVersion(){
        SysApi mSysApi = SysApi.get();
        mSysApi.initializ(UpgradeApplication.getContext());
        String batteryVersion = mSysApi.getBatteryVersion();
        LogUtils.d("batteryVersion: "+batteryVersion);
        if(TextUtils.isEmpty(batteryVersion))
            return null;
        int index1 = batteryVersion.indexOf("-V");
        if(index1<0)
            return null;
        int index2 = batteryVersion.lastIndexOf("-");
        if(index2<0)
            return null;
        String version = batteryVersion.substring(index1+2,index2);
        LogUtils.d("version: "+version);
        char[] chars = version.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0;i<chars.length;i++){
            stringBuilder.append(chars[i]);
            if(i<chars.length-1)
                stringBuilder.append('.');
        }
        version = stringBuilder.toString();
        LogUtils.d("version222: "+version);
        return "v"+version;
    }
}
