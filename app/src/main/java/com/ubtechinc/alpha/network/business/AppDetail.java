package com.ubtechinc.alpha.network.business;

import com.ubtechinc.alpha.network.module.AppDetailModule;
import com.ubtechinc.nets.ResponseListener;
import com.ubtechinc.nets.http.HttpProxy;

/**
 * Created by hongjie.xiang on 2017/6/1.
 */

public class AppDetail {
    private static final String TAG = "AppDetail";
    private static AppDetail sInstance;
    public static AppDetail getInstance(){
        if (sInstance == null) {
            synchronized (AppDetail.class) {

                if (sInstance == null) {
                    sInstance = new AppDetail();
                }
            }
        }
        return sInstance;
    }


    public void requestAppDetail(String packageName, String fromVersion,String toVersion,String apkLanguage,String robotVersion,ResponseListener<AppDetailModule.Response> listener) {
        AppDetailModule.Request request = new AppDetailModule.Request();
        request.setPackageName(packageName);
        request.setFromVersion(fromVersion);
        request.setToVersion(toVersion);
        request.setApkLanguage(apkLanguage);
        request.setRobotVersion(robotVersion);
        HttpProxy.get().doPost(request, listener);
    }
}
