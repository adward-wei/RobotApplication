package com.ubtechinc.alpha.im.msghandler;

import android.content.Intent;
import android.os.Bundle;

import com.google.protobuf.ByteString;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha.AlphaMessageOuterClass;
import com.ubtechinc.alpha.CmBaseResponseOuterClass;
import com.ubtechinc.alpha.CmTransferAppData;
import com.ubtechinc.alpha.app.AlphaApplication;
import com.ubtechinc.alpha.model.StaticValue;
import com.ubtechinc.alpha.model.app.UbtAppData;
import com.ubtechinc.nets.im.service.RobotPhoneCommuniteProxy;
import com.ubtechinc.nets.phonerobotcommunite.ProtoBufferDispose;

/**
 * @desc : 触发第三方app按键的消息处理器
 * @author: wzt
 * @time : 2017/6/5
 * @modifier:
 * @modify_time:
 */

public class ClickAppButtonMsgHandler implements IMsgHandler {
    static final String TAG = "ClickAppButtonMsgHandler";
    @Override
    public void handleMsg(int requestCmdId, int responseCmdId, AlphaMessageOuterClass.AlphaMessage request, String peer) {

        ByteString requestBody = request.getBodyData();
        byte[] bodyBytes = requestBody.toByteArray();
        long requestSerial = request.getHeader().getSendSerial();
        CmTransferAppData.CmTransferAppDataRequest transferAppData = (CmTransferAppData.CmTransferAppDataRequest) ProtoBufferDispose.unPackData(
                CmTransferAppData.CmTransferAppDataRequest.class, bodyBytes);
        LogUtils.i( "request body : click app button packageName = " + transferAppData.getPackageName());

        sendBroadcast(transferAppData.getCmdID(),
                transferAppData.getDatas().toByteArray(),
                transferAppData.getPackageName());

        CmBaseResponseOuterClass.CmBaseResponse.Builder builder = CmBaseResponseOuterClass.CmBaseResponse.newBuilder();
        builder.setResult(0);
        CmBaseResponseOuterClass.CmBaseResponse responseBody = builder.build();

        RobotPhoneCommuniteProxy.getInstance().sendResponseMessage(responseCmdId,"1",requestSerial, responseBody, peer,null);
    }

    private void sendBroadcast(int cmd, byte[] datas, String packageName) {
        UbtAppData developer = new UbtAppData();
        developer.setCmd(cmd);
        developer.setDatas(datas);
        developer.setPackageName(packageName);

        Intent intent = new Intent(developer.getPackageName()
                + StaticValue.APP_BUTOON_EVENT_CLICK);
        Bundle bundle = new Bundle();
        bundle.putSerializable("appdata", developer);
        intent.putExtras(bundle);
        AlphaApplication.getContext().sendBroadcast(intent);
    }
}
