package com.ubtechinc.alpha.im.msghandler;

import com.google.protobuf.ByteString;
import com.ubtechinc.alpha.AlphaMessageOuterClass;
import com.ubtechinc.alpha.CmBaseResponseOuterClass;
import com.ubtechinc.alpha.CmConfirmOnline;
import com.ubtechinc.nets.im.service.RobotPhoneCommuniteProxy;

/**
 * Created by bob.xu on 2017/8/7.
 */

public class ConfirmOnlineMsgHandler implements IMsgHandler {
    @Override
    public void handleMsg(int requestCmdId, int responseCmdId, AlphaMessageOuterClass.AlphaMessage request, String peer) {
        long requestSerial = request.getHeader().getSendSerial();


        CmConfirmOnline.CmConfirmOnlineResponse.Builder builder = CmConfirmOnline.CmConfirmOnlineResponse.newBuilder();
        CmConfirmOnline.CmConfirmOnlineResponse responseBody = builder.setIsOnline(1).build();
        RobotPhoneCommuniteProxy.getInstance().sendResponseMessage(responseCmdId,"1",requestSerial,responseBody,peer,null);
    }
}
