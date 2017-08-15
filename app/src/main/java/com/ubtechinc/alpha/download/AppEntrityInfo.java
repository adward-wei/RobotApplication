package com.ubtechinc.alpha.download;

import android.support.annotation.Keep;

import java.util.Arrays;

/**
 * @desc : app详细信息的内部类
 * @author: wzt
 * @time : 2017/6/2
 * @modifier:
 * @modify_time:
 */

@Keep
public class AppEntrityInfo {
    private String packageName;

    private String name;

    private String appKey;

    private String versionCode="";

    private String versionName="";

    private boolean isDownLoad;

    private String url="";

    private byte[] icon;
    /** 有配置信息 **/
    private boolean isSetting;
    /** 有alpha 系统应用 **/
    private boolean isSystemApp;
    /** 有按钮时间 **/
    private boolean isButtonEvent;
    /** 下载状态 **/
    private int downLoadState;
    private int downloadProgress;

    public int getDownloadProgress() {
        return downloadProgress;
    }

    public void setDownloadProgress(int downloadProgress) {
        this.downloadProgress = downloadProgress;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public boolean isDownLoad() {
        return isDownLoad;
    }

    public void setDownLoad(boolean downLoad) {
        isDownLoad = downLoad;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public byte[] getIcon() {
        return icon;
    }

    public void setIcon(byte[] icon) {
        this.icon = icon;
    }

    public boolean isSetting() {
        return isSetting;
    }

    public void setSetting(boolean setting) {
        isSetting = setting;
    }

    public boolean isSystemApp() {
        return isSystemApp;
    }

    public void setSystemApp(boolean systemApp) {
        isSystemApp = systemApp;
    }

    public boolean isButtonEvent() {
        return isButtonEvent;
    }

    public void setButtonEvent(boolean buttonEvent) {
        isButtonEvent = buttonEvent;
    }

    public int getDownLoadState() {
        return downLoadState;
    }

    public void setDownLoadState(int downLoadState) {
        this.downLoadState = downLoadState;
    }

    @Override
    public String toString() {
        return "AppEntrityInfo{" +
                "packageName='" + packageName + '\'' +
                ", name='" + name + '\'' +
                ", appKey='" + appKey + '\'' +
                ", versionCode='" + versionCode + '\'' +
                ", versionName='" + versionName + '\'' +
                ", isDownLoad=" + isDownLoad +
                ", url='" + url + '\'' +
                ", icon=" + Arrays.toString(icon) +
                ", isSetting=" + isSetting +
                ", isSystemApp=" + isSystemApp +
                ", isButtonEvent=" + isButtonEvent +
                ", downLoadState=" + downLoadState +
                '}';
    }
}
