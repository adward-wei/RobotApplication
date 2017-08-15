package com.ubtechinc.nets.push;


import android.content.Context;

import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.ubtech.utilcode.utils.LogUtils;

/**
 * description: Push管理类，当前使用的是信鸽
 ** autour: bob.xu
 * date: 2017/6/27 14:01
 * update: 2017/6/27
 * version: 1.0*/
public class PushManager {

    private static PushManager instance;
    public static final String TAG = "PushManager";
    private Context context;
    public static PushManager getInstance(Context context) {
        if (instance == null) {
            synchronized (PushManager.class) {
                instance = new PushManager(context);
            }
        }
        return instance;
    }

    private PushManager(Context context) {
        this.context = context.getApplicationContext();
    }

    public void setPushDispatcher(IPushDispatcher dispatcher) {

    }

    public void init() {
        XGPushConfig.enableDebug(context, true);
        XGPushManager.registerPush(context,new XGIOperateCallback() {

            @Override
            public void onSuccess(Object o, int i) {
                String token = XGPushConfig.getToken(context);
                LogUtils.d(TAG,"registerPush--success--token = "+token);
            }

            @Override
            public void onFail(Object data, int errorCode, String msg) {
                LogUtils.e(TAG,"registerPush--fail--errorCode = "+errorCode+", msg = "+msg);
            }
        });
    }
}
