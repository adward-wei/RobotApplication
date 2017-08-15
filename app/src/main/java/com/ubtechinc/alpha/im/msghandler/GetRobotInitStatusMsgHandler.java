package com.ubtechinc.alpha.im.msghandler;

import android.text.TextUtils;

import com.ubtech.utilcode.utils.SPUtils;
import com.ubtechinc.alpha.AlphaMessageOuterClass;
import com.ubtechinc.alpha.CmAlphaInitParam;
import com.ubtechinc.alpha.CmQueryPowerData;
import com.ubtechinc.alpha.CmQuerySoftwareVersion;
import com.ubtechinc.alpha.CmRobotMemory;
import com.ubtechinc.alpha.robotinfo.RobotConfiguration;
import com.ubtechinc.alpha.robotinfo.RobotState;
import com.ubtechinc.alpha.robotinfo.SoftwareVersionInfo;
import com.ubtechinc.alpha.utils.StorageUtil;
import com.ubtechinc.alpha.utils.SysUtils;
import com.ubtechinc.nets.im.service.RobotPhoneCommuniteProxy;
import com.ubtechinc.alpha.utils.Constants;

/**
 * Created by Administrator on 2017/6/9 0009.
 */

public class GetRobotInitStatusMsgHandler implements IMsgHandler{
    public static final String TAG="GetRobotInitStatusMsgHandler";
    @Override
    public void handleMsg(int requestCmdId, int responseCmdId, AlphaMessageOuterClass.AlphaMessage request, String peer) {
        final long requestSerial = request.getHeader().getSendSerial();
        CmAlphaInitParam.CmAlphaInitParamResponse.Builder builder = CmAlphaInitParam.CmAlphaInitParamResponse.newBuilder();

        final CmRobotMemory.CmRobotMemoryResponse.Builder memoryResponse =CmRobotMemory.CmRobotMemoryResponse.newBuilder();
        memoryResponse.setAvailableExternalSize(StorageUtil.getAvailableExternalMemorySize());
        memoryResponse.setAvailableInternalSize(StorageUtil.getAvailableInternalMemorySize());
        memoryResponse.setTotalExternalSize(StorageUtil.getTotalExternalMemorySize());
        memoryResponse.setTotalInternalSize(StorageUtil.getTotalInternalMemorySize());
        builder.setMemory(memoryResponse);
        CmQuerySoftwareVersion.CmQuerySoftwareVersionResponse.Builder softwareVersionResponse = CmQuerySoftwareVersion.CmQuerySoftwareVersionResponse.newBuilder();
        if(SoftwareVersionInfo.get().deviceVersion!=null){
            softwareVersionResponse.setAndroidVersion(SoftwareVersionInfo.get().deviceVersion);}

        softwareVersionResponse.setServiceVersionName(SoftwareVersionInfo.get().serviceVersionName);
        softwareVersionResponse.setServiceVersionCode(SoftwareVersionInfo.get().serviceVersionCode);
        if(SoftwareVersionInfo.get().chestVersion!=null){
            softwareVersionResponse.setChestVersion(SoftwareVersionInfo.get().chestVersion);}
        if(SoftwareVersionInfo.get().batteryVersion!=null){
            softwareVersionResponse.setBatteryVersion(SoftwareVersionInfo.get().batteryVersion);}
        if(SysUtils.is2Mic()){
            softwareVersionResponse.setHeaderVersion(SoftwareVersionInfo.get().headVersion);
        }
        builder.setSoftwareVersion(softwareVersionResponse.build());

        int power  = RobotState.get().getPowerValue();
        builder.setPowerData(CmQueryPowerData.CmQueryPowerDataResponse.newBuilder().setPowerValue(power).build());
        builder.setServiceLanguage( RobotConfiguration.get().asr_Language);
        String masterName = SPUtils.get().getString(Constants.MASTER_NAME);

        builder.setMasterName(TextUtils.isEmpty(masterName) ? "": masterName);
        RobotPhoneCommuniteProxy.getInstance().sendResponseMessage(responseCmdId,"1",requestSerial,builder.build(),peer,null);
    }
}
