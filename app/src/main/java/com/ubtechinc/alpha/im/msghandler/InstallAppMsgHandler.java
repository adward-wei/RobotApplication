package com.ubtechinc.alpha.im.msghandler;

import android.text.TextUtils;

import com.google.protobuf.ByteString;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha.AlphaMessageOuterClass;
import com.ubtechinc.alpha.CmAppInstallState;
import com.ubtechinc.alpha.CmInstallApp;
import com.ubtechinc.alpha.app.AlphaApplication;
import com.ubtechinc.alpha.download.AppEntrityInfo;
import com.ubtechinc.alpha.download.BusinessConstant;
import com.ubtechinc.alpha.download.FileDownload;
import com.ubtechinc.alpha.im.IMCmdId;
import com.ubtechinc.alpha.im.IMHeaderField;
import com.ubtechinc.alpha.im.IMsgResponseCallback;
import com.ubtechinc.alpha.im.Robot2PhoneMsgMgr;
import com.ubtechinc.alpha.utils.StorageUtil;
import com.ubtechinc.nets.im.service.RobotPhoneCommuniteProxy;
import com.ubtechinc.nets.phonerobotcommunite.ProtoBufferDispose;

/**
 * @desc : 安装应用的消息处理器
 * @author: wzt
 * @time : 2017/6/2
 * @modifier:
 * @modify_time:
 */

public class InstallAppMsgHandler implements IMsgHandler, IMsgResponseCallback {
    static final String TAG = "InstallAppMsgHandler";
    @Override
    public void handleMsg(int requestCmdId, int responseCmdId, AlphaMessageOuterClass.AlphaMessage request, String peer) {

        ByteString requestBody = request.getBodyData();
        byte[] bodyBytes = requestBody.toByteArray();
        long requestSerial = request.getHeader().getSendSerial();
        CmInstallApp.CmInstallAppRequest cmUpdateAppRequest = ( CmInstallApp.CmInstallAppRequest) ProtoBufferDispose.unPackData( CmInstallApp.CmInstallAppRequest.class,bodyBytes);
        LogUtils.i( "request body : install package name = " + cmUpdateAppRequest.getPackageName());
        CmInstallApp.CmInstallAppResponse.Builder builder =   CmInstallApp.CmInstallAppResponse.newBuilder();
        if(!StorageUtil.isAvailableExternalMemoryGreaterThan100M()) {


            builder.setResultCode(BusinessConstant.ERROR_CODE_MEMORY_INSUFFICIENT);

            RobotPhoneCommuniteProxy.getInstance().sendResponseMessage(responseCmdId,"1",requestSerial, builder.build(), peer,null);
        } else {
            builder.setResultCode(BusinessConstant.DOWNLOAD_STATE_DOWNLOADING);
            AppEntrityInfo app = new AppEntrityInfo();
            app.setPackageName(cmUpdateAppRequest.getPackageName());
            app.setUrl(cmUpdateAppRequest.getUrl());
            app.setAppKey(cmUpdateAppRequest.getAppKey());
            IMHeaderField headerField = new IMHeaderField();
            headerField.requestSerial = requestSerial;
            headerField.responseCmdId = responseCmdId;
            headerField.peer = peer;

            FileDownload downLoad = new FileDownload(AlphaApplication.getContext(), FileDownload.DOWNLOAD_TYPE_APP,
                    headerField, this);
            downLoad.download(app);

            RobotPhoneCommuniteProxy.getInstance().sendResponseMessage(responseCmdId,"1",requestSerial, builder.build(), peer,null);
        }
    }

    @Override
    public void onResult(IMHeaderField headerField, Object obj) {

        AppEntrityInfo appEntrityInfo = (AppEntrityInfo) obj;
        CmAppInstallState.CmAppInstallStateResponse.Builder builder = CmAppInstallState.CmAppInstallStateResponse.newBuilder();
        builder.setPackageName(appEntrityInfo.getPackageName());
        if(TextUtils.isEmpty(appEntrityInfo.getAppKey())) {
            builder.setAppId("");
        } else {
            builder.setAppId(appEntrityInfo.getAppKey());
        }
        builder.setState(appEntrityInfo.getDownLoadState());
        builder.setProgress(appEntrityInfo.getDownloadProgress());
        LogUtils.d( "AppDownload callback = " +  appEntrityInfo.toString());
        Robot2PhoneMsgMgr.getInstance().sendData(IMCmdId.IM_APP_INSTALL_STATE_RESPONSE,"1",  builder.build(), headerField.peer, null);

    }
}
