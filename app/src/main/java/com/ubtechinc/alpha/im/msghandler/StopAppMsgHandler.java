package com.ubtechinc.alpha.im.msghandler;

import com.google.protobuf.ByteString;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtech.utilcode.utils.notification.NotificationCenter;
import com.ubtechinc.alpha.AlphaMessageOuterClass;
import com.ubtechinc.alpha.CmStopApp;
import com.ubtechinc.alpha.event.AppManageEvent;
import com.ubtechinc.nets.phonerobotcommunite.ProtoBufferDispose;

/**
 * @desc : 停止app的消息处理器
 * @author: wzt
 * @time : 2017/6/2
 * @modifier:
 * @modify_time:
 */

public class StopAppMsgHandler implements IMsgHandler {
    static final String TAG = "StopAppMsgHandler";
    @Override
    public void handleMsg(int requestCmdId, int responseCmdId, AlphaMessageOuterClass.AlphaMessage request, String peer) {

        ByteString requestBody = request.getBodyData();
        byte[] bodyBytes = requestBody.toByteArray();
        long requestSerial = request.getHeader().getSendSerial();
        CmStopApp.CmStopAppRequest stopAppRequest = (CmStopApp.CmStopAppRequest) ProtoBufferDispose.unPackData(CmStopApp.CmStopAppRequest.class,bodyBytes);
        LogUtils.i( "request body : package name = " + stopAppRequest.getPackageName());

        sendStopAppEvent(stopAppRequest.getPackageName(),
                stopAppRequest.getName(),
                requestSerial,
                peer,
                responseCmdId);
    }

    private void sendStopAppEvent(String packageName, String name,
                                   long requestSerial, String peer, int responseCmdId) {
        AppManageEvent event =  new AppManageEvent();
        event.requestSerial = requestSerial;
        event.peer = peer;
        event.responseCmdID = responseCmdId;

        event.packageName = packageName;
        event.name = name;
        event.event = "stop";
        NotificationCenter.defaultCenter().publish(event);
    }
}
