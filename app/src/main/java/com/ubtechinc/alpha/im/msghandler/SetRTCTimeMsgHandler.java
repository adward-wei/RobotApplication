package com.ubtechinc.alpha.im.msghandler;

import com.google.protobuf.ByteString;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtech.utilcode.utils.notification.NotificationCenter;
import com.ubtechinc.alpha.AlphaMessageOuterClass;
import com.ubtechinc.alpha.CmSetRTCTimeData;
import com.ubtechinc.alpha.event.TimeSettingEvent;
import com.ubtechinc.nets.im.service.RobotPhoneCommuniteProxy;
import com.ubtechinc.nets.phonerobotcommunite.ProtoBufferDispose;

import java.util.Calendar;

/**
 * @desc : 设置RTC时间的消息处理器
 * @author: wzt
 * @time : 2017/6/6
 * @modifier:
 * @modify_time:
 */

public class SetRTCTimeMsgHandler implements IMsgHandler {
    static final String TAG = "SetRTCTimeMsgHandler";

    @Override
    public void handleMsg(int requestCmdId, int responseCmdId, AlphaMessageOuterClass.AlphaMessage request, String peer) {

        ByteString requestBody = request.getBodyData();
        byte[] bodyBytes = requestBody.toByteArray();
        long requestSerial = request.getHeader().getSendSerial();
        CmSetRTCTimeData.CmSetRTCTimeDataRequest setRTCTimeData = (CmSetRTCTimeData.CmSetRTCTimeDataRequest) ProtoBufferDispose.unPackData(
                CmSetRTCTimeData.CmSetRTCTimeDataRequest.class, bodyBytes);
        LogUtils.d("request body : set RTC time hour = " + setRTCTimeData.getHour());

        byte[] rtcData = new byte[7];
        rtcData[0] = (byte) (setRTCTimeData.getYear() - 2000);
        rtcData[1] = (byte) setRTCTimeData.getMonth();
        rtcData[2] = (byte) setRTCTimeData.getDay();
        rtcData[3] = (byte) setRTCTimeData.getWeek();
        rtcData[4] = (byte) setRTCTimeData.getHour();
        rtcData[5] = (byte) setRTCTimeData.getMinute();
        rtcData[6] = (byte) setRTCTimeData.getSecond();

        TimeSettingEvent event = new TimeSettingEvent();
        System.arraycopy(rtcData, 0, event.rtcTime, 0, 7);
        NotificationCenter.defaultCenter().publish(event);

        CmSetRTCTimeData.CmSetRTCTimeDataResponse.Builder builder = CmSetRTCTimeData.CmSetRTCTimeDataResponse.newBuilder();
        final Calendar c = Calendar.getInstance();
        byte year = (byte) c.get(Calendar.YEAR); // 获取当前年份
        byte month = (byte) (c.get(Calendar.MONTH) + 1);// 获取当前月份
        byte day = (byte) c.get(Calendar.DAY_OF_MONTH);// 获取当前月份的日期号码
        byte week = (byte) c.get(Calendar.DAY_OF_WEEK);
        byte hour = (byte) c.get(Calendar.HOUR_OF_DAY);
        byte minute = (byte) c.get(Calendar.MINUTE);
        byte second = (byte) c.get(Calendar.SECOND);

        builder.setYear(year);
        builder.setMonth(month);
        builder.setDay(day);
        builder.setWeek(week);
        builder.setHour(hour);
        builder.setMinute(minute);
        builder.setSecond(second);

        CmSetRTCTimeData.CmSetRTCTimeDataResponse responseBody = builder.build();
        RobotPhoneCommuniteProxy.getInstance().sendResponseMessage(responseCmdId,
                "1",
                requestSerial,
                responseBody,
                peer, null);
    }
}
