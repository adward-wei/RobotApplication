package com.ubtechinc.alpha.im.msghandler;

import com.google.protobuf.ByteString;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha.AlphaMessageOuterClass;
import com.ubtechinc.alpha.CmSetTTSActionActiveData;
import com.ubtechinc.alpha.app.AlphaApplication;
import com.ubtechinc.alpha.utils.SharedPreferenceUtil;
import com.ubtechinc.nets.im.service.RobotPhoneCommuniteProxy;
import com.ubtechinc.nets.phonerobotcommunite.ProtoBufferDispose;

/**
 * Created by Administrator on 2017/6/5 0005.
 */

public class SetTTSActionActiveDataMsgHandler implements IMsgHandler {
    private static final String TAG = "SetTTSActionActiveDataMsgHandler";

    @Override
    public void handleMsg(int requestCmdId, int responseCmdId, AlphaMessageOuterClass.AlphaMessage request, String peer) {
        ByteString requestBody = request.getBodyData();
        byte[] bodyBytes = requestBody.toByteArray();
        long requestSerial = request.getHeader().getSendSerial();
        LogUtils.i(TAG, "enter SetTTSActionActiveDataMsgHandler, requestSerial :" + requestSerial);

        CmSetTTSActionActiveData.CmSetTTSActionActiveDataRequest ttsSettingRequest = (CmSetTTSActionActiveData.CmSetTTSActionActiveDataRequest) ProtoBufferDispose.unPackData(CmSetTTSActionActiveData.CmSetTTSActionActiveDataRequest.class, bodyBytes);
        boolean isOpenTTSAction = ttsSettingRequest.getIsOpen();
        SharedPreferenceUtil.saveBoolean(AlphaApplication.getContext(), "is_open_TTS_action", isOpenTTSAction);
        LogUtils.i(TAG, "isOpenTTSAction :" + isOpenTTSAction);
        CmSetTTSActionActiveData.CmSetTTSActionActiveDataResponse.Builder builder =CmSetTTSActionActiveData.CmSetTTSActionActiveDataResponse.newBuilder();
        builder.setIsSuccess(true);
        RobotPhoneCommuniteProxy.getInstance().sendResponseMessage(responseCmdId, "1", requestSerial, builder.build(), peer, null);

    }
}





