package com.ubtechinc.alpha.im.msghandler;

import com.google.protobuf.ByteString;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha.AlphaMessageOuterClass;
import com.ubtechinc.alpha.CmQuerySoftwareVersion;
import com.ubtechinc.alpha.robotinfo.SoftwareVersionInfo;
import com.ubtechinc.alpha.utils.SysUtils;
import com.ubtechinc.nets.im.service.RobotPhoneCommuniteProxy;
import com.ubtechinc.nets.phonerobotcommunite.ProtoBufferDispose;

/**
 * Created by Administrator on 2017/6/6 0006.
 */

public class QuerySoftwareVersionMsgHandler implements IMsgHandler{
    private static final String TAG ="QuerySoftwareVersionMsgHandler";
    @Override
    public void handleMsg(int requestCmdId, int responseCmdId, AlphaMessageOuterClass.AlphaMessage request, String peer) {
        ByteString requestBody = request.getBodyData();
        byte[] bodyBytes = requestBody.toByteArray();
        final long requestSerial = request.getHeader().getSendSerial();

        CmQuerySoftwareVersion.CmQuerySoftwareVersionRequest querySoftwareVersionRequest = (CmQuerySoftwareVersion.CmQuerySoftwareVersionRequest) ProtoBufferDispose.unPackData(CmQuerySoftwareVersion.CmQuerySoftwareVersionRequest.class,bodyBytes);
        LogUtils.d(TAG,"query softwareversion request:"+querySoftwareVersionRequest.toString());
            //发送广播  串口发送查询

            CmQuerySoftwareVersion.CmQuerySoftwareVersionResponse.Builder responseBuilder = CmQuerySoftwareVersion.CmQuerySoftwareVersionResponse.newBuilder();
            if(SoftwareVersionInfo.get().deviceVersion!=null){
                responseBuilder.setAndroidVersion(SoftwareVersionInfo.get().deviceVersion);}

            responseBuilder.setServiceVersionCode(SoftwareVersionInfo.get().serviceVersionCode);
        responseBuilder.setServiceVersionName(SoftwareVersionInfo.get().serviceVersionName);
            if(SoftwareVersionInfo.get().chestVersion!=null){
                responseBuilder.setChestVersion(SoftwareVersionInfo.get().chestVersion);}
            if(SoftwareVersionInfo.get().batteryVersion!=null){
                responseBuilder.setBatteryVersion(SoftwareVersionInfo.get().batteryVersion);}
            if(SysUtils.is2Mic()){
                responseBuilder.setHeaderVersion(SoftwareVersionInfo.get().headVersion);
            }

            RobotPhoneCommuniteProxy.getInstance().sendResponseMessage(responseCmdId,"1",requestSerial, responseBuilder.build() ,peer,null);
    }
}
