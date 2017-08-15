package com.ubtechinc.alpha.im.msghandler;

import com.ubtechinc.alpha.AlphaMessageOuterClass;
import com.ubtechinc.alpha.CmGetDeskClockList;
import com.ubtechinc.alpha.DeskClockOuterClass;
import com.ubtechinc.alpha.deskclock.DeskClock;
import com.ubtechinc.alpha.provider.AlarmInfoVisitor;
import com.ubtechinc.nets.im.service.RobotPhoneCommuniteProxy;

import java.util.List;

/**
 * @desc : 获取所有提醒列表的消息处理器
 * @author: wzt
 * @time : 2017/6/2
 * @modifier:
 * @modify_time:
 */

public class GetActiveDeskClockListHandler implements IMsgHandler {
    static final String TAG = "GetActiveDeskClockListHandler";
    @Override
    public void handleMsg(int requestCmdId, int responseCmdId, AlphaMessageOuterClass.AlphaMessage request, String peer) {

        long requestSerial = request.getHeader().getSendSerial();
        AlarmInfoVisitor provider = AlarmInfoVisitor.get();
        List<DeskClock> beanList = provider.getActive(System.currentTimeMillis());
        CmGetDeskClockList.CmGetDeskClockListResponse.Builder builder = CmGetDeskClockList.CmGetDeskClockListResponse.newBuilder();


        for(DeskClock item : beanList) {
             DeskClockOuterClass.DeskClock.Builder itemBuilder = DeskClockOuterClass.DeskClock.newBuilder();
            itemBuilder.setType(item.getType());
            itemBuilder.setClockID(item.getClockID());
            itemBuilder.setHour(item.getHour());
            itemBuilder.setMinutes(item.getMinutes());
            itemBuilder.setDaysofweek(item.getDaysofweek());
            itemBuilder.setAlarmtime(item.getAlarmtime());
            itemBuilder.setEnabled(item.isEnabled());
            itemBuilder.setVibrate(item.getVibrate());
            itemBuilder.setMessage(item.getMessage());
            itemBuilder.setAlert(item.getAlert());
            itemBuilder.setDtstart(item.getDtstart());
            itemBuilder.setIscomplete(item.iscomplete());

            builder.addClockList(itemBuilder.build());
        }
        CmGetDeskClockList.CmGetDeskClockListResponse responseBody = builder.build();

        RobotPhoneCommuniteProxy.getInstance().sendResponseMessage(responseCmdId,"1",requestSerial, responseBody, peer,null);

    }
}
