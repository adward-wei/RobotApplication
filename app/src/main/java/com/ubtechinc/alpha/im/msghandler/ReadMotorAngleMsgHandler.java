package com.ubtechinc.alpha.im.msghandler;

import com.google.protobuf.ByteString;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtech.utilcode.utils.notification.NotificationCenter;
import com.ubtechinc.alpha.AlphaMessageOuterClass;

import com.ubtechinc.alpha.CmReadMotorAngle;
import com.ubtechinc.alpha.event.ReadMotorAngleEvent;
import com.ubtechinc.nets.phonerobotcommunite.ProtoBufferDispose;

/**
 * @desc : 读取舵机角度的消息处理器
 * @author: wzt
 * @time : 2017/6/1
 * @modifier:
 * @modify_time:
 */

public class ReadMotorAngleMsgHandler implements IMsgHandler {
    static final String TAG = "ReadMotorAngleMsgHandler";
    @Override
    public void handleMsg(int requestCmdId, int responseCmdId, AlphaMessageOuterClass.AlphaMessage request, String peer) {

        ByteString requestBody = request.getBodyData();
        byte[] bodyBytes = requestBody.toByteArray();
        long requestSerial = request.getHeader().getSendSerial();
        CmReadMotorAngle.CmReadMotorAngleRequest readMotorAngle = (CmReadMotorAngle.CmReadMotorAngleRequest) ProtoBufferDispose.unPackData(
                CmReadMotorAngle.CmReadMotorAngleRequest.class,bodyBytes);
        LogUtils.i( "request body : read motorID = " + readMotorAngle.getMotorID());

        sendReadMotorAngleEvent(readMotorAngle.getMotorID(),
                requestSerial,
                peer,
                responseCmdId);
    }

    private void sendReadMotorAngleEvent(int motorID, long requestSerial, String peer, int responseCmdId) {
        ReadMotorAngleEvent event =  new ReadMotorAngleEvent();
        event.requestSerial = requestSerial;
        event.peer = peer;
        event.responseCmdID = responseCmdId;

        event.motorID = motorID;
        event.acdump = true;
        NotificationCenter.defaultCenter().publish(event);
    }
}
