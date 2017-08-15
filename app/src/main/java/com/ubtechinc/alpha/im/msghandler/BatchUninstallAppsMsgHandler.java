package com.ubtechinc.alpha.im.msghandler;

import com.google.protobuf.ByteString;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha.AlphaMessageOuterClass;
import com.ubtechinc.alpha.CmAppUninstallInfo;
import com.ubtechinc.alpha.CmAppUninstallList;
import com.ubtechinc.alpha.appmanager.install.AppSlience;
import com.ubtechinc.alpha.entity.UnIntallApp;
import com.ubtechinc.nets.im.service.RobotPhoneCommuniteProxy;
import com.ubtechinc.nets.phonerobotcommunite.ProtoBufferDispose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/5 0005.
 */

public class BatchUninstallAppsMsgHandler implements IMsgHandler{
    private static final String TAG ="BatchUninstallAppsMsgHandler";
    @Override
    public void handleMsg(int requestCmdId, final int responseCmdId, AlphaMessageOuterClass.AlphaMessage request, final String peer) {
        ByteString requestBody = request.getBodyData();
        byte[] bodyBytes = requestBody.toByteArray();
        final long requestSerial = request.getHeader().getSendSerial();

        CmAppUninstallList.CmAppUninstallListRequest appUninstallListRequest =(CmAppUninstallList.CmAppUninstallListRequest)ProtoBufferDispose.unPackData(CmAppUninstallList.CmAppUninstallListRequest.class,bodyBytes);
        List<CmAppUninstallInfo.CmAppUninstallInfoRequest> appUninstallInfoRequests = appUninstallListRequest.getAppListList();

        if(appUninstallInfoRequests ==null){
            LogUtils.i( TAG,"request body is null!!!" );
            return ;
        }

        LogUtils.i( TAG,"request body : body size = "+appUninstallInfoRequests.size());
        List<String> uninstallPackages = new ArrayList<>();
        for (int i=0; i< appUninstallInfoRequests.size(); i++) {
            CmAppUninstallInfo.CmAppUninstallInfoRequest cmAppUninstallInfoRequest=appUninstallInfoRequests.get(i);
            uninstallPackages.add(cmAppUninstallInfoRequest.getPackageName());
        }

        AppSlience appSlience = new AppSlience();
        appSlience.bathUninstall(uninstallPackages, new AppSlience.BatchSlienceListener() {
            List<UnIntallApp> unIntallApps = new ArrayList<UnIntallApp>();
            @Override
            public void onSlienceFail(String pkgName) {
                UnIntallApp unIntallApp = new UnIntallApp();
                unIntallApp.setUnInstallSuccess(false);
                unIntallApp.setPackageName(pkgName);
                unIntallApps.add(unIntallApp);
                LogUtils.i( TAG,"onSlienceFail pkgName = " + pkgName);
            }

            @Override
            public void onSlienceSuccess(String pkgName) {
                UnIntallApp unIntallApp = new UnIntallApp();
                unIntallApp.setUnInstallSuccess(true);
                unIntallApp.setPackageName(pkgName);
                unIntallApps.add(unIntallApp);
                LogUtils.i( TAG,"onSlienceSuccess pkgName = " + pkgName);
            }

            @Override
            public void onComplete() {
                LogUtils.i(TAG, "unintall apps complete!" );
                CmAppUninstallList.CmAppUninstallListResponse.Builder builder =CmAppUninstallList.CmAppUninstallListResponse.newBuilder();
                for(UnIntallApp uninstallApp :unIntallApps){
                    CmAppUninstallInfo.CmAppUninstallInfoResponse.Builder uninstallAppInfoResponseBuilder = CmAppUninstallInfo.CmAppUninstallInfoResponse.newBuilder();
                    if(uninstallApp.getPackageName()!=null)
                        uninstallAppInfoResponseBuilder.setPackageName(uninstallApp.getPackageName());
                    if(uninstallApp.getName()!=null)
                        uninstallAppInfoResponseBuilder.setName(uninstallApp.getName());
                    uninstallAppInfoResponseBuilder.setIsUnInstallSuccess(uninstallApp.isUnInstallSuccess());
                    builder.addAppList(uninstallAppInfoResponseBuilder.build());
                }
                RobotPhoneCommuniteProxy.getInstance().sendResponseMessage(responseCmdId,"1",requestSerial,builder.build(),peer,null);


            }
        });

    }
}

