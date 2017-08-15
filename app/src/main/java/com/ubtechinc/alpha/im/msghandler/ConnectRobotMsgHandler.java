package com.ubtechinc.alpha.im.msghandler;

import com.google.protobuf.ByteString;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha.AlConnectRobot;
import com.ubtechinc.alpha.AlphaMessageOuterClass;
import com.ubtechinc.alpha.im.ControlClientManager;
import com.ubtechinc.alpha.robotinfo.RobotState;
import com.ubtechinc.nets.im.service.RobotPhoneCommuniteProxy;
import com.ubtechinc.nets.phonerobotcommunite.ProtoBufferDispose;

/**
 * Created by Administrator on 2017/6/1.
 */

public class ConnectRobotMsgHandler implements IMsgHandler {
    private static final String TAG = "ConnectRobotMsgHandler";

    @Override
    public void handleMsg(int requestCmdId, int responseCmdId, AlphaMessageOuterClass.AlphaMessage request, String peer) {
        ByteString requestBody = request.getBodyData();
        byte[] bodyBytes = requestBody.toByteArray();
        long requestSerial = request.getHeader().getSendSerial();
        AlConnectRobot.AlConnectRobotRequest connRobotRequest  = (AlConnectRobot.AlConnectRobotRequest)ProtoBufferDispose.unPackData(AlConnectRobot.AlConnectRobotRequest.class,bodyBytes);
        LogUtils.i("request body : robotId = "+connRobotRequest.getRobotId()+", userId = "+connRobotRequest.getUserId());
        if (connRobotRequest.getRobotId().equals(RobotState.get().getSid())) {
            ControlClientManager.getInstance().addClient(connRobotRequest.getUserId());
        }

        AlConnectRobot.AlConnectRobotResponse responseBody = AlConnectRobot.AlConnectRobotResponse.newBuilder()
                .setRobotId(RobotState.get().getSid())
                .setIsSuccess(true).build();
        RobotPhoneCommuniteProxy.getInstance().sendResponseMessage(responseCmdId,"1",requestSerial,responseBody,peer,null);

    }
}
