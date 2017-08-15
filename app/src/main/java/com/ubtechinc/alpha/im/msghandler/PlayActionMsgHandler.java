package com.ubtechinc.alpha.im.msghandler;

import com.google.protobuf.ByteString;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha.AlPlayActionCommand;
import com.ubtechinc.alpha.AlphaMessageOuterClass;
import com.ubtechinc.alpha.ops.ActionServiceProxy;
import com.ubtechinc.nets.im.service.RobotPhoneCommuniteProxy;
import com.ubtechinc.nets.phonerobotcommunite.ProtoBufferDispose;

/**
 * @desc : 执行动作文件的消息处理器
 * @author: wzt
 * @time : 2017/6/1
 * @modifier:
 * @modify_time:
 */

public class PlayActionMsgHandler implements IMsgHandler {
    static final String TAG = "PlayActionMsgHandler";
    @Override
    public void handleMsg(int requestCmdId, int responseCmdId, AlphaMessageOuterClass.AlphaMessage request, String peer) {

        ByteString requestBody = request.getBodyData();
        byte[] bodyBytes = requestBody.toByteArray();
        long requestSerial = request.getHeader().getSendSerial();
        AlPlayActionCommand.AlPlayActionCommandRequest playActionCommand  = (AlPlayActionCommand.AlPlayActionCommandRequest) ProtoBufferDispose.unPackData(
                AlPlayActionCommand.AlPlayActionCommandRequest.class,bodyBytes);
        LogUtils.i("request body : fileName = "+playActionCommand.getActionName());


        ActionServiceProxy.getInstance().playAction(playActionCommand.getActionName(),null);

        AlPlayActionCommand.AlPlayActionCommandResponse.Builder builder = AlPlayActionCommand.AlPlayActionCommandResponse.newBuilder();
        builder.setIsSuccess(true);
        LogUtils.i( "PlayAction response result = " + builder.getIsSuccess());
        RobotPhoneCommuniteProxy.getInstance().sendResponseMessage(responseCmdId,"1",requestSerial, builder.build() ,peer,null);

    }
}
