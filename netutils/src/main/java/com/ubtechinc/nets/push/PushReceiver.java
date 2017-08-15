package com.ubtechinc.nets.push;

import android.content.Context;

import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;
import com.ubtech.utilcode.utils.LogUtils;

/**
 * Created by bob.xu on 2017/6/27.
 */

public class PushReceiver extends XGPushBaseReceiver {
    public static final String TAG = "PushReceiver";
    @Override
    public void onRegisterResult(Context context, int errorCode, XGPushRegisterResult xgPushRegisterResult) {
        LogUtils.d(TAG,"onRegisterResult--errorCode = "+errorCode+" , xgPushRegisterResult = "+xgPushRegisterResult != null ? xgPushRegisterResult.toString() : "");
    }

    @Override
    public void onUnregisterResult(Context context, int errorCode) {
        LogUtils.d(TAG,"onUnregisterResult---errorCode = "+errorCode);
    }

    @Override
    public void onSetTagResult(Context context, int errorCode, String tagName) {
        LogUtils.d(TAG,"onSetTagResult---errorCode = "+errorCode+", tagName = "+tagName);
    }

    @Override
    public void onDeleteTagResult(Context context, int errorCode, String tagName) {
        LogUtils.d(TAG,"onDeleteTagResult---errorCode = "+errorCode+", tagName = "+tagName);
    }

    @Override
    public void onTextMessage(Context context, XGPushTextMessage xgPushTextMessage) {
        LogUtils.d(TAG,"onTextMessage---xgPushTextMessage = "+xgPushTextMessage != null ? xgPushTextMessage.toString():"");
    }

    @Override
    public void onNotifactionClickedResult(Context context, XGPushClickedResult xgPushClickedResult) {
        LogUtils.d(TAG,"onNotifactionClickedResult--xgPushClickedResult : "+xgPushClickedResult != null ? xgPushClickedResult.toString():"");
    }

    @Override
    public void onNotifactionShowedResult(Context context, XGPushShowedResult xgPushShowedResult) {
        LogUtils.d(TAG,"onNotifactionShowedResult--xgPushShowedResult: "+xgPushShowedResult != null ? xgPushShowedResult.toString():"");
    }
}
