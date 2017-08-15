package com.ubtechinc.alpha.im.msghandler;

import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha.AlphaMessageOuterClass;
import com.ubtechinc.alpha.CmGetDeskClockList;
import com.ubtechinc.alpha.DeskClockOuterClass;
import com.ubtechinc.alpha.deskclock.DeskClock;
import com.ubtechinc.alpha.provider.AlarmInfoVisitor;
import com.ubtechinc.nets.im.service.RobotPhoneCommuniteProxy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/6 0006.
 */

public class GetDeskClockHistoryMsgHandler implements  IMsgHandler{
    private static final String TAG="GetDeskClockHistoryMsgHandler";
    @Override
    public void handleMsg(int requestCmdId, int responseCmdId, AlphaMessageOuterClass.AlphaMessage request, String peer) {
        LogUtils.d(TAG,"enter GetDeskClockHistoryMsgHandler handleMsg.");
        final long requestSerial = request.getHeader().getSendSerial();

        CmGetDeskClockList.CmGetDeskClockListResponse.Builder  cmDeskClockListResponseBuilder = CmGetDeskClockList.CmGetDeskClockListResponse.newBuilder();
        List<DeskClockOuterClass.DeskClock> deskClockResponses =new ArrayList<>();

        AlarmInfoVisitor provider = AlarmInfoVisitor.get();
        List<DeskClock> deskclocks = provider.getHistory();
        if(deskclocks!=null) {
            for (int i = 0; i < deskclocks.size(); i++) {
                DeskClockOuterClass.DeskClock cmdeskClockResponse = DeskClockOuterClass.DeskClock.newBuilder().setAlarmtime(deskclocks.get(i).getAlarmtime()).setAlert(deskclocks.get(i).getAlert()).setEnabled(deskclocks.get(i).isEnabled()).
                        setClockID(deskclocks.get(i).getClockID()).setDaysofweek(deskclocks.get(i).getDaysofweek()).setDtstart(deskclocks.get(i).getDtstart()).setHour(deskclocks.get(i).getHour()).setMinutes(deskclocks.get(i).getMinutes()).setMessage(deskclocks.get(i).getMessage()).setIscomplete(deskclocks.get(i).iscomplete()).setVibrate(deskclocks.get(i).getVibrate())
                        .build();

                deskClockResponses.add(cmdeskClockResponse);
            }
        }
        cmDeskClockListResponseBuilder.addAllClockList(deskClockResponses);
        CmGetDeskClockList.CmGetDeskClockListResponse deskClockListResponse =cmDeskClockListResponseBuilder.build();

        RobotPhoneCommuniteProxy.getInstance().sendResponseMessage(responseCmdId,"1",requestSerial,deskClockListResponse,peer,null);


    }
}
