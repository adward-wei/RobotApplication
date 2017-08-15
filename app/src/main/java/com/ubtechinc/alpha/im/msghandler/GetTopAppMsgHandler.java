package com.ubtechinc.alpha.im.msghandler;


import android.text.TextUtils;

import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha.AlphaMessageOuterClass;
import com.ubtechinc.alpha.CmAppEntrityInfoOuterClass;
import com.ubtechinc.alpha.appmanager.AppInfo;
import com.ubtechinc.alpha.appmanager.AppManager;
import com.ubtechinc.nets.im.service.RobotPhoneCommuniteProxy;

public class GetTopAppMsgHandler implements IMsgHandler {
    static final String TAG = "GetTopAppMsgHandler";
    @Override
    public void handleMsg(int requestCmdId, int responseCmdId, AlphaMessageOuterClass.AlphaMessage request, String peer) {

        long requestSerial = request.getHeader().getSendSerial();

        AppInfo appInfo = AppManager.getInstance().getTopApp();

        CmAppEntrityInfoOuterClass.CmAppEntrityInfo.Builder builder = CmAppEntrityInfoOuterClass.CmAppEntrityInfo.newBuilder();
        if(appInfo != null) {
            if(TextUtils.isEmpty(appInfo.appKey)) {
                builder.setAppKey("");
            } else {
                builder.setAppKey(appInfo.appKey);
            }
            builder.setPackageName(appInfo.getPackageName());
        } else {
            builder.setAppKey("");
            builder.setPackageName("");
        }
        CmAppEntrityInfoOuterClass.CmAppEntrityInfo responseBody = builder.build();
        LogUtils.d("GetTopApp = " + appInfo);
        RobotPhoneCommuniteProxy.getInstance().sendResponseMessage(responseCmdId,"1",requestSerial, responseBody, peer, null);
    }
}
