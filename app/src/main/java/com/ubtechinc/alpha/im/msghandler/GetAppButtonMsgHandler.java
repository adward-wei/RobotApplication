package com.ubtechinc.alpha.im.msghandler;

import android.content.Intent;
import android.os.Bundle;

import com.google.protobuf.ByteString;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtech.utilcode.utils.notification.NotificationCenter;
import com.ubtechinc.alpha.AlphaMessageOuterClass;
import com.ubtechinc.alpha.CmTransferAppData;
import com.ubtechinc.alpha.app.AlphaApplication;
import com.ubtechinc.alpha.event.RequestAppConfigEvent;
import com.ubtechinc.alpha.model.StaticValue;
import com.ubtechinc.alpha.model.app.UbtAppData;
import com.ubtechinc.nets.phonerobotcommunite.ProtoBufferDispose;

/**
 * @desc : 获取第三方app按键信息的消息处理器
 * @author: wzt
 * @time : 2017/6/5
 * @modifier:
 * @modify_time:
 */

public class GetAppButtonMsgHandler implements IMsgHandler {
    static final String TAG = "GetAppButtonMsgHandler";
    @Override
    public void handleMsg(int requestCmdId, int responseCmdId, AlphaMessageOuterClass.AlphaMessage request, String peer) {

        ByteString requestBody = request.getBodyData();
        byte[] bodyBytes = requestBody.toByteArray();
        long requestSerial = request.getHeader().getSendSerial();
        CmTransferAppData.CmTransferAppDataRequest transferAppData = (CmTransferAppData.CmTransferAppDataRequest) ProtoBufferDispose.unPackData(
                CmTransferAppData.CmTransferAppDataRequest.class, bodyBytes);
        LogUtils.i( "request body : get app button packageName = " + transferAppData.getPackageName());

        sendBroadcast(transferAppData.getCmdID(),
                transferAppData.getDatas().toByteArray(),
                transferAppData.getPackageName());

        sendRequestAppConfigEvent(transferAppData.getPackageName(),
                requestSerial,
                peer,
                responseCmdId);
    }

    private void sendBroadcast(int cmd, byte[] datas, String packageName) {
        UbtAppData developer = new UbtAppData();
        developer.setCmd(cmd);
        developer.setDatas(datas);
        developer.setPackageName(packageName);

        Intent intent = new Intent(developer.getPackageName()
                + StaticValue.APP_BUTTON_EVENT);
        Bundle bundle = new Bundle();
        bundle.putSerializable("appdata", developer);
        intent.putExtras(bundle);
        AlphaApplication.getContext().sendBroadcast(intent);
    }

    private void sendRequestAppConfigEvent(String packageName, long requestSerial, String peer, int responseCmdId) {
        RequestAppConfigEvent event =  new RequestAppConfigEvent();
        event.requestSerial = requestSerial;
        event.peer = peer;
        event.responseCmdID = responseCmdId;
        NotificationCenter.defaultCenter().publish(event);
    }
}
