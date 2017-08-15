package com.ubtechinc.alpha.im.msghandler;

import com.google.protobuf.ByteString;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha.AIDisConnectRobot;
import com.ubtechinc.alpha.AlphaMessageOuterClass;
import com.ubtechinc.alpha.im.ControlClientManager;
import com.ubtechinc.alpha.robotinfo.RobotState;
import com.ubtechinc.nets.im.service.RobotPhoneCommuniteProxy;
import com.ubtechinc.nets.phonerobotcommunite.ProtoBufferDispose;

/**
 * Created by Administrator on 2017/6/2.
 */

public class DisConnectRobotMsgHandler implements IMsgHandler {
    private final static String TAG = "DisConnectRobotMsgHandler";
    @Override
    public void handleMsg(int requestCmdId, int responseCmdId, AlphaMessageOuterClass.AlphaMessage request, String peer) {

        ByteString requestBody = request.getBodyData();
        byte[] bodyBytes = requestBody.toByteArray();
        long requestSerial = request.getHeader().getSendSerial();
        AIDisConnectRobot.AlDisConnectRobotRequest disConnectRequest = (AIDisConnectRobot.AlDisConnectRobotRequest) ProtoBufferDispose.unPackData(AIDisConnectRobot.AlDisConnectRobotRequest.class,bodyBytes);
        LogUtils.i("request body : robotId = "+disConnectRequest.getRobotId()+", userId = "+disConnectRequest.getUserId());
        if (disConnectRequest.getRobotId().equals(RobotState.get().getSid())) {
            ControlClientManager.getInstance().removeClient(disConnectRequest.getUserId());
        }
        //最新方案，不需要发新跳包，当Robot下线时IM服务器会向ubt服务器回调状态变化，后台再通过Im向客户端发送Robot状态变化的msg。
        //TecentIMManager.getInstance(AlphaApplication.getContext()).startToHeartMessage();

        AIDisConnectRobot.AlDisConnectRobotResponse responseBody = AIDisConnectRobot.AlDisConnectRobotResponse.newBuilder()
                .setRobotId(RobotState.get().getSid())
                .setIsSuccess(true).build();
        RobotPhoneCommuniteProxy.getInstance().sendResponseMessage(responseCmdId,"1",requestSerial,responseBody,peer,null);
    }
}
