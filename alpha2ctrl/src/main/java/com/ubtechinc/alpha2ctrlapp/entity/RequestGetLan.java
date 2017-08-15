package com.ubtechinc.alpha2ctrlapp.entity;


import com.ubtechinc.alpha2ctrlapp.entity.request.CommonRequest;

/**
 * Created by nixiaoyan on 2016/6/20.
 * 获取多语言请求
 */
public class RequestGetLan extends CommonRequest {
    private int themeObjectType =5;
    private String appVersion;

    public int getThemeObjectType() {
        return themeObjectType;
    }

    public void setThemeObjectType(int themeObjectType) {
        this.themeObjectType = themeObjectType;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }
}
