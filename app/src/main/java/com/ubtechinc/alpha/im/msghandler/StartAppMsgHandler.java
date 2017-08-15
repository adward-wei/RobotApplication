package com.ubtechinc.alpha.im.msghandler;

import com.google.protobuf.ByteString;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtech.utilcode.utils.notification.NotificationCenter;
import com.ubtechinc.alpha.AlphaMessageOuterClass;
import com.ubtechinc.alpha.CmStartApp;
import com.ubtechinc.alpha.event.AppManageEvent;
import com.ubtechinc.nets.phonerobotcommunite.ProtoBufferDispose;



/**
 * @desc : 启动app的消息处理器
 * @author: wzt
 * @time : 2017/6/2
 * @modifier:
 * @modify_time:
 */

public class StartAppMsgHandler implements IMsgHandler {
    static final String TAG = "StartAppMsgHandler";
    @Override
    public void handleMsg(int requestCmdId, int responseCmdId, AlphaMessageOuterClass.AlphaMessage request, String peer) {

        ByteString requestBody = request.getBodyData();
        byte[] bodyBytes = requestBody.toByteArray();
        long requestSerial = request.getHeader().getSendSerial();
        CmStartApp.CmStartAppRequest startAppRequest =  (CmStartApp.CmStartAppRequest) ProtoBufferDispose.unPackData(CmStartApp.CmStartAppRequest.class,bodyBytes);
        LogUtils.i( "request body :start app package name = " + startAppRequest.getPackageName());

        sendStartAppEvent(startAppRequest.getPackageName(),
                startAppRequest.getName(),
                requestSerial,
                peer,
                responseCmdId);
    }

    private void sendStartAppEvent(String packageName, String name,
                                   long requestSerial, String peer, int responseCmdId) {
        AppManageEvent event =  new AppManageEvent();
        event.requestSerial = requestSerial;
        event.peer = peer;
        event.responseCmdID = responseCmdId;

        event.packageName = packageName;
        event.name = name;
        event.event = "start";
        NotificationCenter.defaultCenter().publish(event);
    }
}
