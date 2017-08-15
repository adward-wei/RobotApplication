package com.ubtechinc.alpha.im.msghandler;

import com.ubtech.utilcode.utils.LogUtils;
import com.ubtech.utilcode.utils.thread.ThreadPool;
import com.ubtechinc.alpha.AlphaMessageOuterClass;
import com.ubtechinc.alpha.CmAppUninstallRobotInfo;
import com.ubtechinc.alpha.CmRobotMemory;
import com.ubtechinc.alpha.app.AlphaApplication;
import com.ubtechinc.alpha.business.StorageBusiness;
import com.ubtechinc.alpha.utils.StorageUtil;
import com.ubtechinc.nets.im.service.RobotPhoneCommuniteProxy;

import java.util.List;

/**
 * Created by Administrator on 2017/6/5 0005.
 */

public class GetStorageAppListMsgHandler implements IMsgHandler {
    private static final String TAG="GetStorageAppListMsgHandler";
    @Override
    public void handleMsg(int requestCmdId, final int responseCmdId, AlphaMessageOuterClass.AlphaMessage request, final String peer) {
        final long requestSerial = request.getHeader().getSendSerial();
//        Alpha2Apps alpha2Apps = new Alpha2Apps(AlphaApplication.getContext());
        //改成线程池
        ThreadPool.getInstance().submit(new ThreadPool.Job<Object>(){
            @Override
            public Object run(ThreadPool.JobContext jobContext) {
                final CmRobotMemory.CmRobotMemoryResponse.Builder builder =CmRobotMemory.CmRobotMemoryResponse.newBuilder();
                builder.setAvailableExternalSize(StorageUtil.getAvailableExternalMemorySize());
                builder.setAvailableInternalSize(StorageUtil.getAvailableInternalMemorySize());
                builder.setTotalExternalSize(StorageUtil.getTotalExternalMemorySize());
                builder.setTotalInternalSize(StorageUtil.getTotalInternalMemorySize());
//

                new StorageBusiness().getAllInstalledApps(AlphaApplication.getContext(), new StorageBusiness.IGetPkgCallback<List<StorageBusiness.AppPackageSimpleInfo>>() {
                    @Override
                    public void onGetPkgInfo(final List<StorageBusiness.AppPackageSimpleInfo> appPackageInfos) {

                        for(StorageBusiness.AppPackageSimpleInfo packageSimpleInfo : appPackageInfos) {
                            CmAppUninstallRobotInfo.CmAppUninstallRobotInfoResponse.Builder appInfoResponseBuilder = CmAppUninstallRobotInfo.CmAppUninstallRobotInfoResponse.newBuilder();
                            appInfoResponseBuilder.setName(packageSimpleInfo.getName());
                            appInfoResponseBuilder.setPackageName(packageSimpleInfo.getPackageName());
                            appInfoResponseBuilder.setAppSize(packageSimpleInfo.getAppSize());
//                            appInfoResponseBuilder.setIcon(ByteString.copyFrom(packageSimpleInfo.getIcon()));
                            builder.addAppList(appInfoResponseBuilder);
                        }
                        LogUtils.d(TAG, "send  CmRobotMemory.CmRobotMemoryResponse");
                        RobotPhoneCommuniteProxy.getInstance().sendResponseMessage(responseCmdId,"1",requestSerial, builder.build(), peer,null);
                    }
                });

                return null;
            }
        });
    }
}
