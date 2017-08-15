package com.ubtechinc.alpha2ctrlapp.im.msghandler;

import com.google.common.collect.ImmutableMap;
import com.google.protobuf.ByteString;
import com.orhanobut.logger.Logger;
import com.ubtechinc.alpha.AlphaMessageOuterClass;
import com.ubtechinc.alpha.CmAppInstallState;
import com.ubtechinc.alpha.im.msghandler.IMsgHandler;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.database.AppInfoProvider;
import com.ubtechinc.alpha2ctrlapp.database.RobotAppEntrity;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.RobotApp;
import com.ubtechinc.alpha2ctrlapp.events.AppDownloadStatusChangeEvent;
import com.ubtechinc.nets.phonerobotcommunite.ProtoBufferDispose;

import org.greenrobot.eventbus.EventBus;


/**
 * @ClassName AppDownloadStatusMsgHandler
 * @date 7/7/2017
 * @author tanghongyu
 * @Description App下载状态回调
 * @modifier
 * @modify_time
 */
public class AppDownloadStatusMsgHandler implements IMsgHandler {
    private static final String TAG = "AppDownloadStatusMsgHan";
    @Override
    public void handleMsg(int requestCmdId, int responseCmdId, AlphaMessageOuterClass.AlphaMessage request, String peer) {
        ByteString requestBody = request.getBodyData();
        byte[] bodyBytes = requestBody.toByteArray();
        CmAppInstallState.CmAppInstallStateResponse appEntrityInfo = (CmAppInstallState.CmAppInstallStateResponse) ProtoBufferDispose.unPackData(CmAppInstallState.CmAppInstallStateResponse.class,bodyBytes);
        Logger.t(TAG).d("App install state = %d" , appEntrityInfo.getState());

        AppDownloadStatusChangeEvent event = new AppDownloadStatusChangeEvent();
        RobotApp app = new RobotApp();
        app.setAppKey(appEntrityInfo.getAppId());
        app.setDownloadState(appEntrityInfo.getState());
        app.setPackageName(appEntrityInfo.getPackageName());
        event.setAppEntrityInfo(app);
        RobotAppEntrity robotAppEntrity =AppInfoProvider.get().findAppByParam(ImmutableMap.of("serialNo", Alpha2Application.getRobotSerialNo(), "packageName", (Object)app.getPackageName()));
        robotAppEntrity.setDownloadState(appEntrityInfo.getState());

        AppInfoProvider.get().updateValuesByParam(robotAppEntrity,ImmutableMap.of("serialNo", Alpha2Application.getRobotSerialNo(), "packageName", (Object)app.getPackageName()), "downloadState" );
        EventBus.getDefault().post(event);
    }
}
