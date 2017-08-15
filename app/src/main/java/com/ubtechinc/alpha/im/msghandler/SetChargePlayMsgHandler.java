package com.ubtechinc.alpha.im.msghandler;


import com.google.protobuf.ByteString;
import com.ubtech.utilcode.utils.notification.NotificationCenter;
import com.ubtechinc.alpha.AlphaMessageOuterClass;
import com.ubtechinc.alpha.CmSetChargeAndPlayData;
import com.ubtechinc.alpha.event.SetChargePlayEvent;
import com.ubtechinc.nets.im.service.RobotPhoneCommuniteProxy;
import com.ubtechinc.nets.phonerobotcommunite.ProtoBufferDispose;

/**
 * @desc : 边充边玩设置与查询的消息处理器
 * @author: wzt
 * @time : 2017/6/6
 * @modifier:
 * @modify_time:
 */

public class SetChargePlayMsgHandler implements IMsgHandler {
    static final String TAG = "SetChargePlayMsgHandler";

    @Override
    public void handleMsg(int requestCmdId, int responseCmdId, AlphaMessageOuterClass.AlphaMessage request, String peer) {

        ByteString requestBody = request.getBodyData();
        byte[] bodyBytes = requestBody.toByteArray();
        long requestSerial = request.getHeader().getSendSerial();
        CmSetChargeAndPlayData.CmSetChargeAndPlayDataRequest setChargeAndPlayData = (CmSetChargeAndPlayData.CmSetChargeAndPlayDataRequest) ProtoBufferDispose.unPackData(
                CmSetChargeAndPlayData.CmSetChargeAndPlayDataRequest.class, bodyBytes);

            SetChargePlayEvent event = new SetChargePlayEvent();
            event.isChargePlayingOpen = setChargeAndPlayData.getIsOpen();
            NotificationCenter.defaultCenter().publish(event);

            CmSetChargeAndPlayData.CmSetChargeAndPlayDataResponse.Builder builder = CmSetChargeAndPlayData.CmSetChargeAndPlayDataResponse.newBuilder();
            builder.setIsSuccess(true);
            CmSetChargeAndPlayData.CmSetChargeAndPlayDataResponse responseBody = builder.build();

            RobotPhoneCommuniteProxy.getInstance().sendResponseMessage(responseCmdId,
                    "1",
                    requestSerial,
                    responseBody,
                    peer, null);

    }
}
