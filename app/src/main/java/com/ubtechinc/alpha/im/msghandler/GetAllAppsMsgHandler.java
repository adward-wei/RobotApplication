package com.ubtechinc.alpha.im.msghandler;

import com.ubtechinc.alpha.AlphaMessageOuterClass;
import com.ubtechinc.alpha.CmAppEntrityInfoOuterClass;
import com.ubtechinc.alpha.CmAppEntrityList;
import com.ubtechinc.alpha.app.AlphaApplication;
import com.ubtechinc.alpha.appmanager.old.Alpha2Apps;
import com.ubtechinc.alpha.download.AppEntrityInfo;
import com.ubtechinc.nets.im.service.RobotPhoneCommuniteProxy;

import java.util.List;

/**
 * @desc : 所有第三方app信息的消息处理器
 * @author: wzt
 * @time : 2017/6/2
 * @modifier:
 * @modify_time:
 */

public class GetAllAppsMsgHandler implements IMsgHandler {
    static final String TAG = "GetAllAppsMsgHandler";
    @Override
    public void handleMsg(int requestCmdId, int responseCmdId, AlphaMessageOuterClass.AlphaMessage request, String peer) {

        long requestSerial = request.getHeader().getSendSerial();

        Alpha2Apps alpha2Apps = new Alpha2Apps(AlphaApplication.getContext());
        List<AppEntrityInfo> list =  alpha2Apps.getAppEntityInfo();

        CmAppEntrityList.CmAppEntrityListResponse.Builder builder = CmAppEntrityList.CmAppEntrityListResponse.newBuilder();
        for(AppEntrityInfo item : list) {
            CmAppEntrityInfoOuterClass.CmAppEntrityInfo.Builder appbuilder = CmAppEntrityInfoOuterClass.CmAppEntrityInfo.newBuilder();
            appbuilder.setPackageName(item.getPackageName());
            appbuilder.setName(item.getName());
            appbuilder.setAppKey(item.getAppKey());
            appbuilder.setVersionCode(item.getVersionCode());
            appbuilder.setVersionName(item.getVersionName());
            appbuilder.setIsButtonEvent(item.isButtonEvent());
            appbuilder.setIsSetting(item.isSetting());
            appbuilder.setIsSystemApp(item.isSystemApp());

            builder.addAppList(appbuilder.build());
        }
        CmAppEntrityList.CmAppEntrityListResponse responseBody = builder.build();

        RobotPhoneCommuniteProxy.getInstance().sendResponseMessage(responseCmdId,"1",requestSerial, responseBody, peer,null);

    }
}
