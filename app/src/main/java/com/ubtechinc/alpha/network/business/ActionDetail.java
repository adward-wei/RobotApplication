package com.ubtechinc.alpha.network.business;

import com.ubtechinc.alpha.network.module.ActionDetailModule;
import com.ubtechinc.nets.ResponseListener;
import com.ubtechinc.nets.http.HttpProxy;

/**
 * Created by hongjie.xiang on 2017/6/1.
 */

public class ActionDetail {
    private static final String TAG = "AppDetail";
    private static ActionDetail sInstance;
    public static ActionDetail getInstance() {
        if (sInstance == null) {
            synchronized (ActionDetail.class) {

                if (sInstance == null) {
                    sInstance = new ActionDetail();
                }
            }
        }
        return sInstance;
    }


    public void requestAcionDetail(String actionOriginalId,ResponseListener<ActionDetailModule.Response> listener) {
        ActionDetailModule.Request request=new ActionDetailModule.Request();
        request.setActionOriginalId(actionOriginalId);
        HttpProxy.get().doGet(request,listener);
    }
}
