package com.ubtechinc.alpha.im.msghandler;


import com.google.protobuf.ByteString;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha.AlphaMessageOuterClass;
import com.ubtechinc.alpha.CmUninstallApp;
import com.ubtechinc.alpha.app.AlphaApplication;
import com.ubtechinc.alpha.appmanager.install.AppSlience;
import com.ubtechinc.alpha.appmanager.old.Alpha2Apps;
import com.ubtechinc.alpha.download.BusinessConstant;
import com.ubtechinc.nets.im.service.RobotPhoneCommuniteProxy;
import com.ubtechinc.nets.phonerobotcommunite.ProtoBufferDispose;

/**
 * @desc : 卸载应用的消息处理器
 * @author: wzt
 * @time : 2017/6/2
 * @modifier:
 * @modify_time:
 */

public class UninstallAppMsgHandler  implements IMsgHandler {
    static final String TAG = "UninstallAppMsgHandler";
    @Override
    public void handleMsg(int requestCmdId, int responseCmdId, AlphaMessageOuterClass.AlphaMessage request, String peer) {

        ByteString requestBody = request.getBodyData();
        byte[] bodyBytes = requestBody.toByteArray();
        final long requestSerial = request.getHeader().getSendSerial();
        CmUninstallApp.CmUninstallAppRequest appEntrityInfo = (CmUninstallApp.CmUninstallAppRequest) ProtoBufferDispose.unPackData(
                CmUninstallApp.CmUninstallAppRequest.class,bodyBytes);
        LogUtils.i( "request body : uninstall package name = " + appEntrityInfo.getPackageName());

        final CmUninstallApp.CmUninstallAppResponse.Builder builder =  CmUninstallApp.CmUninstallAppResponse.newBuilder();
        Alpha2Apps alpha2Apps = new Alpha2Apps(AlphaApplication.getContext());
        if (alpha2Apps.getUsesPermission(appEntrityInfo.getPackageName())) {
            builder.setResultCode(BusinessConstant.ERROR_CODE_CAN_NOT_INSTALL);

            RobotPhoneCommuniteProxy.getInstance().sendResponseMessage(responseCmdId,"1",requestSerial, builder.build(), peer,null);
        } else {

            final int responseCmdIdFinal = responseCmdId;
            final String peerFinal = peer;

            new AppSlience().onUnInitall(appEntrityInfo.getPackageName(),
                    new AppSlience.SlienceListener() {
                        @Override
                        public void onSlienceFail(int code, String msg) {
                            builder.setIsSuccess(false);
                            builder.setResultCode(BusinessConstant.UNINTALL_STATE_FAIL);

                            RobotPhoneCommuniteProxy.getInstance().sendResponseMessage(responseCmdIdFinal,"1",requestSerial, builder.build(), peerFinal, null);
                        }

                        @Override
                        public void onSlienceSuccess() {
                            builder.setIsSuccess(true);
                            RobotPhoneCommuniteProxy.getInstance().sendResponseMessage(responseCmdIdFinal,"1",requestSerial, builder.build(), peerFinal, null);
                        }
                    });
        }
    }
}
