package com.ubtechinc.alpha.im.msghandler;

import android.net.Uri;

import com.google.protobuf.ByteString;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha.AlphaMessageOuterClass;
import com.ubtechinc.alpha.CmManageDeskClock;
import com.ubtechinc.alpha.DeskClockOuterClass;
import com.ubtechinc.alpha.ops.alarm.AlarmInfo;
import com.ubtechinc.alpha.provider.AlarmInfoVisitor;
import com.ubtechinc.nets.im.service.RobotPhoneCommuniteProxy;
import com.ubtechinc.nets.phonerobotcommunite.ProtoBufferDispose;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * @desc : 与提醒设置、更新、删除相关的消息处理器
 * @author: wzt
 * @time : 2017/6/5
 * @modifier:
 * @modify_time:
 */


public class DeskClockManageHandler implements IMsgHandler {
    static final String TAG = "DeskClockManageHandler";

    @Override
    public void handleMsg(int requestCmdId, int responseCmdId, AlphaMessageOuterClass.AlphaMessage request, String peer) {

        ByteString requestBody = request.getBodyData();
        byte[] bodyBytes = requestBody.toByteArray();
        long requestSerial = request.getHeader().getSendSerial();
        CmManageDeskClock.CmManageDeskClockRequest manageDeskClockRequest = (CmManageDeskClock.CmManageDeskClockRequest) ProtoBufferDispose.unPackData( CmManageDeskClock.CmManageDeskClockRequest .class, bodyBytes);
        DeskClockOuterClass.DeskClock deskClock = manageDeskClockRequest.getDeskClock();
        LogUtils.i( TAG,"request body : desk clock type = " + deskClock.getType());

        int result = processDeskClock(deskClock);
        CmManageDeskClock.CmManageDeskClockResponse.Builder builder = CmManageDeskClock.CmManageDeskClockResponse.newBuilder();
        builder.setIsSuccess(true);
        RobotPhoneCommuniteProxy.getInstance().sendResponseMessage(responseCmdId, "1", requestSerial, builder.build(), peer,null);

    }

    private int processDeskClock(DeskClockOuterClass.DeskClock deskClock) {
        int result = 0;
        int type = deskClock.getType();


        LogUtils.i(TAG,"deskclock handler:"+deskClock.toString());
        AlarmInfoVisitor provider = AlarmInfoVisitor.get();

        switch (type) {
            case 0: { //add alarm
                AlarmInfo alarmInfo = new AlarmInfo();
                alarmInfo.isUseAble = deskClock.getEnabled();
                alarmInfo.hh = deskClock.getHour();
                alarmInfo.mm = deskClock.getMinutes();
                alarmInfo.repeat = deskClock.getDaysofweek();
                alarmInfo.vibrate = deskClock.getVibrate();
                long[] data = getDtstart(deskClock.getDtstart(), deskClock.getHour(), deskClock.getMinutes());
                alarmInfo.dtstart = data[0];
                alarmInfo.label = deskClock.getMessage();
                Uri uri = Uri.parse(deskClock.getAlert());
                alarmInfo.alert = uri;
                alarmInfo.iscomplete = deskClock.getIscomplete();
                alarmInfo.dttime = data[1];
                LogUtils.i(TAG,"add deskclock :"+alarmInfo);
                provider.saveOrUpdate(alarmInfo);
            }
            break;
            case 1: { //delete alarm
                LogUtils.i(TAG,"deleteById deskclock :"+deskClock);
                provider.deleteById(deskClock.getClockID());
            }
            break;
            case 2: { //update alarm
                AlarmInfo alarmInfo = new AlarmInfo();
                alarmInfo.id = deskClock.getClockID();
                alarmInfo.isUseAble = deskClock.getEnabled();
                alarmInfo.hh = deskClock.getHour();
                alarmInfo.mm = deskClock.getMinutes();

                alarmInfo.date = deskClock.getDaysofweek();
                alarmInfo.vibrate = (deskClock.getVibrate());
                long[] data = getDtstart(deskClock.getDtstart(), deskClock.getHour(), deskClock.getMinutes());
                alarmInfo.dtstart = data[0];

                alarmInfo.label = deskClock.getMessage();
                Uri uri = Uri.parse(deskClock.getAlert());
                alarmInfo.alert = uri;
                alarmInfo.iscomplete = deskClock.getIscomplete();
                alarmInfo.dttime = data[1];
                provider.saveOrUpdate(alarmInfo);
                LogUtils.i(TAG,"update deskclock :"+deskClock);
            }
            break;
        }

        return result;
    }

    /**
     * 将得到的日期为long
     *
     * @param start
     * @return
     */
    private long[] getDtstart(String start, int hour, int minute) {
        Calendar cal = Calendar.getInstance();
        long[] data = new long[2];
        long dtStart = 0;
        long dtTime = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dBegin;
        try {
            dBegin = sdf.parse(start);
            cal.setTime(dBegin);// 将时间设置到指定的日期
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            dtStart = +cal.getTimeInMillis();
            data[0] = dtStart;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        dtTime = +cal.getTimeInMillis();
        data[1] = dtTime;
        return data;
    }
}
