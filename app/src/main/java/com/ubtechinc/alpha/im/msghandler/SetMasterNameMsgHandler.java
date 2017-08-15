package com.ubtechinc.alpha.im.msghandler;

import com.google.protobuf.ByteString;
import com.ubtech.utilcode.utils.SPUtils;
import com.ubtechinc.alpha.AlphaMessageOuterClass;
import com.ubtechinc.alpha.CmSetMaterName;
import com.ubtechinc.alpha.utils.Constants;
import com.ubtechinc.nets.im.service.RobotPhoneCommuniteProxy;
import com.ubtechinc.nets.phonerobotcommunite.ProtoBufferDispose;

/**
 * @ClassName SetMasterNameMsgHandler
 * @date 6/20/2017
 * @author tanghongyu
 * @Description 设置主人名称
 * @modifier
 * @modify_time
 */
public class SetMasterNameMsgHandler implements IMsgHandler{
    private static final String TAG ="SetMasterNameMsgHandler";
    @Override
    public void handleMsg(int requestCmdId, int responseCmdId, AlphaMessageOuterClass.AlphaMessage request, String peer) {
        ByteString requestBody = request.getBodyData();
        byte[] bodyBytes = requestBody.toByteArray();
        final long requestSerial = request.getHeader().getSendSerial();
        CmSetMaterName.CmSetMasterNameRequest cmSetMasterNameRequest = (CmSetMaterName.CmSetMasterNameRequest)ProtoBufferDispose.unPackData(CmSetMaterName.CmSetMasterNameRequest.class,bodyBytes);
        SPUtils.get().put(Constants.MASTER_NAME, cmSetMasterNameRequest.getMasterName());

        RobotPhoneCommuniteProxy.getInstance().sendResponseMessage(responseCmdId,"1",requestSerial,CmSetMaterName.CmSetMasterNameResponse.newBuilder().setIsSuccess(true).build(),peer,null);
    }
}
