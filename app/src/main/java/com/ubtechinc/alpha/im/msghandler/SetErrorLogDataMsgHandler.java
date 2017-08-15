package com.ubtechinc.alpha.im.msghandler;

import com.google.protobuf.ByteString;
import com.ubtechinc.alpha.AlphaMessageOuterClass;
import com.ubtechinc.alpha.CmSetErrorLogData;
import com.ubtechinc.alpha.app.AlphaApplication;
import com.ubtechinc.alpha.upload.log.LogUploadByCommand;
import com.ubtechinc.nets.im.service.RobotPhoneCommuniteProxy;


/**
 * Created by Administrator on 2017/6/5 0005.
 */

public class SetErrorLogDataMsgHandler implements IMsgHandler {
    private static final String TAG = "SetErrorLogDataMsgHandler";

    @Override
    public void handleMsg(int requestCmdId, int responseCmdId, AlphaMessageOuterClass.AlphaMessage request, String peer) {
        ByteString requestBody = request.getBodyData();
        final long requestSerial = request.getHeader().getSendSerial();

        //需要在闲聊中添加开关
        LogUploadByCommand.getInstance(AlphaApplication.getContext());
        LogUploadByCommand logUploadByCommand = LogUploadByCommand.getInstance(AlphaApplication.getContext());
        int minute = logUploadByCommand.start();

        CmSetErrorLogData.CmSetErrorLogDataResponse.Builder responseBodyBuilder= CmSetErrorLogData.CmSetErrorLogDataResponse.newBuilder();
        responseBodyBuilder.setMinute2Wait(minute);
        RobotPhoneCommuniteProxy.getInstance().sendResponseMessage(responseCmdId,"1",requestSerial, responseBodyBuilder.build(), peer, null);

    }
}
