package com.ubtechinc.alpha.network.business;

import com.ubtechinc.alpha.network.module.SystemConfigModule;
import com.ubtechinc.nets.ResponseListener;
import com.ubtechinc.nets.http.HttpProxy;

/**
 * Created by hongjie.xiang on 2017/6/1.
 */

public class SystemConfig {
    private static final String TAG = "SystemConfig";
    private static SystemConfig sInstance;

    public static SystemConfig getInstance() {
        if (sInstance == null) {
            synchronized (SystemConfig.class) {

                if (sInstance == null) {
                    sInstance = new SystemConfig();
                }
            }
        }
        return sInstance;
    }



    public void requestSystemConfig(String apkLanguage, String robotVersion,ResponseListener<SystemConfigModule.Response> listener) {
        SystemConfigModule.Request request = new SystemConfigModule.Request();
        request.setEquipmentId(apkLanguage);
        request.setControlUserId(robotVersion);
        HttpProxy.get().doPost(request,listener);
    }

}
