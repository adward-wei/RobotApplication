package com.ubtechinc.alpha.network.business;

import com.ubtechinc.alpha.network.module.AppVerifyModule;
import com.ubtechinc.nets.ResponseListener;
import com.ubtechinc.nets.http.HttpProxy;

/**
 * Created by hongjie.xiang on 2017/6/1.
 */

public class AppVerify {
    private static final String TAG = "AppVerify";
    private static AppVerify sInstance;
    public static AppVerify getInstance() {
        if (sInstance == null) {
            synchronized (AppVerify.class) {

                if (sInstance == null) {
                    sInstance = new AppVerify();
                }
            }
        }

        return sInstance;
    }



    public void requestAppVerify(String appKey, String packageName,ResponseListener<AppVerifyModule.Response> listener) {

        AppVerifyModule.Request request=new AppVerifyModule.Request();
        request.setAppKey(appKey);
        request.setPackageName(packageName);
        HttpProxy.get().doPost(request,listener);
    }

}
