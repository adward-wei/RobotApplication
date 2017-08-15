package com.ubtechinc.alpha.im.msghandler;

import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha.AlphaMessageOuterClass;
import com.ubtechinc.alpha.CmGetTTSActionActiveData;
import com.ubtechinc.alpha.app.AlphaApplication;
import com.ubtechinc.alpha.utils.SharedPreferenceUtil;
import com.ubtechinc.nets.im.service.RobotPhoneCommuniteProxy;


/**
 * @ClassName GetTTSActionActiveDataMsgHandler
 * @date 6/19/2017
 * @author tanghongyu
 * @Description 获取TTS动作是否打开
 * @modifier
 * @modify_time
 */
public class GetTTSActionActiveDataMsgHandler implements IMsgHandler {
    private static final String TAG = "SetTTSActionActiveDataMsgHandler";

    @Override
    public void handleMsg(int requestCmdId, int responseCmdId, AlphaMessageOuterClass.AlphaMessage request, String peer) {
        long requestSerial = request.getHeader().getSendSerial();
        LogUtils.i(TAG, "enter SetTTSActionActiveDataMsgHandler, requestSerial :" + requestSerial);
        CmGetTTSActionActiveData.CmGetTTSActionActiveDataResponse.Builder ttsActionActiveDataResponseBuilder =CmGetTTSActionActiveData.CmGetTTSActionActiveDataResponse.newBuilder();
            ttsActionActiveDataResponseBuilder.setIsOpen(SharedPreferenceUtil.readBoolean(AlphaApplication.getContext(),"is_open_TTS_action"));
            RobotPhoneCommuniteProxy.getInstance().sendResponseMessage(responseCmdId, "1", requestSerial, ttsActionActiveDataResponseBuilder.build(), peer, null);
    }
}






