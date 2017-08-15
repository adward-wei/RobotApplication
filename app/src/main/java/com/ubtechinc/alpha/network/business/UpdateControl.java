package com.ubtechinc.alpha.network.business;

import com.ubtechinc.alpha.network.module.UpdateControlModule;
import com.ubtechinc.nets.ResponseListener;
import com.ubtechinc.nets.http.HttpProxy;

/**
 * Created by hongjie.xiang on 2017/6/1.
 */

public class UpdateControl {
    private static final String TAG = "UpdateControl";
    private static UpdateControl sInstance;

    public static UpdateControl getInstance() {
        if (sInstance == null) {
            synchronized (UpdateControlModule.class) {

                if (sInstance == null) {
                    sInstance = new UpdateControl();
                }
            }
        }
        return sInstance;
    }

    public void requestUpdate(String equipmentId, String controlUserId,ResponseListener<UpdateControlModule.Response> listener) {
        UpdateControlModule.Request request = new UpdateControlModule.Request();
        request.setEquipmentId(equipmentId);
        request.setControlUserId(controlUserId);
        HttpProxy.get().doPost(request,listener);
    }
}