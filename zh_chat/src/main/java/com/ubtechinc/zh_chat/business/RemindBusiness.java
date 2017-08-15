package com.ubtechinc.zh_chat.business;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.ubtech.iflytekmix.R;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha.sdk.sys.SysApi;
import com.ubtechinc.alpha.serverlibutil.aidl.AlarmInfo;
import com.ubtechinc.zh_chat.entity.Remind;
import com.ubtechinc.zh_chat.nets.business.AddRecord;
import com.ubtechinc.zh_chat.robot.UBTSemanticRootProxy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * <pre>
 *   @author: zengdengyi
 *   @email : 2091843903@qq.com
 *   @time  : 2017/3/24
 *   @desc  : 日程提醒
 * </pre>
 */

public class RemindBusiness extends BaseBusiness {
    private static final String TAG ="RemindBusiness";
    public RemindBusiness(Context cxt) {
        super(cxt);
    }

    @Override
    public void start(UBTSemanticRootProxy handle) {
        if (getSlots().isNLPAvailable()) {// 有明确的提醒信息
            String date = getSlots().getDatetime().getDate();
            String time = getSlots().getDatetime().getTime();
            String repeat = getSlots().getRepeat();
            if (getOperation().equals("VIEW")) {
                // getRobotProxy().start_TTS("正在查找日程", true);
                process(handle, date);

            } else if (getOperation().equals("CREATE")) {
                process(handle, date, time, repeat);
            }else if(getOperation().equals("CANCEL")){
                //nothing...
            }
        } else {
            handle.start_TTS(
                    mContext.getString(R.string.reminder_no_date), false);
        }
    }

    @Override
    public void clean(UBTSemanticRootProxy handle) {

    }

    public long getDtstart(String start) {
        Calendar cal = Calendar.getInstance();
        long dtStart = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dBegin;
        try {
            dBegin = sdf.parse(start);
            cal.setTime(dBegin);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            dtStart = +cal.getTimeInMillis();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return dtStart;
    }

    private long[] getDtstart(String start, int hour, int minute) {
        Calendar cal = Calendar.getInstance();
        long[] data = new long[2];
        long dtStart ;
        long dtTime ;
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

    public void process(UBTSemanticRootProxy handle, String date) {

        String dts = Long.toString(getDtstart(date));
        String tts;
        List<Remind> d2 = getAlarmListByDate(dts);

        if ((d2 != null ? d2.size() : 0) == 0) {
            tts = mContext.getString(R.string.remind_no_data);
        } else {
            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String nowStr = dateFormat.format(now);

            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(System.currentTimeMillis());

            int nowHour = c.get(Calendar.HOUR_OF_DAY);
            int nowMinute = c.get(Calendar.MINUTE);

            Remind currentRemind = null;
            if (nowStr.equals(date)) {
                for (int i = 0; i < d2.size(); i++) {
                    Remind remind = d2.get(i);

                    int minute = remind.getMinutes();
                    int hour = remind.getHour();
                    // if alarm is behind current time, advance one day
                    if (hour > nowHour || hour == nowHour && minute > nowMinute) {
                        currentRemind = remind;
                        break;
                    }
                }
            } else {
                int number = new Random(System.currentTimeMillis()).nextInt(d2.size());
                currentRemind = d2.get(number);
            }
            if (currentRemind != null) {
                String text = mContext.getString(R.string.remind_nin) +
                        currentRemind.getHour() +
                        mContext.getString(R.string.remind_hour) +
                        currentRemind.getMinutes() +
                        mContext.getString(R.string.remind_munite) +
                        mContext.getString(R.string.remind_need) +
                        currentRemind.getMessage();
                tts = text;
            } else {
                tts = mContext.getString(R.string.remind_no_data);
            }
        }
        handle.start_TTS(tts, false);
        AddRecord.instance().requestAddRecord(Type.REMIND.getValue(), tts, null, null, getSpeechResult());
    }

    public void process(UBTSemanticRootProxy handle, String date, String time, String repeat) {
        // long dtstart = getDtstart(date);
        String[] mTime = time.split(":");
        LogUtils.i(TAG,"data:"+date+"  time:"+time);
        long[] data = getDtstart(date, Integer.parseInt(mTime[0]), Integer.parseInt(mTime[1]));
        Remind bean = new Remind();
        bean.set_id(-1);
        bean.setHour(Integer.parseInt(mTime[0]));
        bean.setMinutes(Integer.parseInt(mTime[1]));
        bean.setEnabled(true);
        bean.setDaysofweek(getRepeateCode(repeat));
        bean.setVibrate(0);
        bean.setMessage(this.getContent());
        LogUtils.i(TAG,"alarm label:"+this.getContent());
        bean.setAlert("");
        bean.setIscomplete(false);

//        Alarm alarm = new Alarm();
//        alarm.id = bean.get_id();
//        alarm.enabled = bean.isEnabled();
//        alarm.hour = bean.getHour();
//        alarm.minutes = bean.getMinutes();
//        Alarm.DaysOfWeek mDaysOfWeek = new Alarm.DaysOfWeek(
//                bean.getDaysofweek());
//        alarm.daysOfWeek = mDaysOfWeek;
//        alarm.vibrate = (bean.getVibrate() == 1);
//        alarm.label = bean.getMessage();
//        alarm.iscomplete = bean.isIscomplete();
////        Calendar cal = Calendar.getInstance();
//        alarm.dtstart = data[0];
//        alarm.dttime = data[1];
//        Uri uri = Uri.parse(bean.getAlert());
//        alarm.alert = uri;
//        String s = uri.getPath();
//        long rt = Alarms.addAlarm(mContext, alarm);

        AlarmInfo alarmInfo = new AlarmInfo();
        alarmInfo.state = 0;
        alarmInfo.hh = bean.getHour();
        alarmInfo.mm = bean.getMinutes();
        alarmInfo.date = getRepeateCode(repeat);
        alarmInfo.vibrate = (bean.getVibrate() == 1);
        alarmInfo.label = bean.getMessage();

        alarmInfo.iscomplete = bean.isIscomplete();

        alarmInfo.dtstart = data[0];
        alarmInfo.dttime = data[1];
        LogUtils.i(TAG,"dtstart:"+data[0]+"  dttime:"+data[1]);
        Uri uri = Uri.parse(bean.getAlert());
        alarmInfo.alert = uri;
        SysApi.get().insertAlarm(alarmInfo);

        // 7点3分,提醒您 开会 的日程已经创建
        StringBuffer text = new StringBuffer();
        text.append(mTime[0]);
        text.append(mContext.getString(R.string.remind_hour));
        text.append(mTime[1]);
        text.append(mContext.getString(R.string.remind_munite));
        text.append(mContext.getString(R.string.remind_remind));
        if(!TextUtils.isEmpty(this.getContent())){
            text.append(this.getContent());
        }else{
            text.append("闹钟");
        }
        text.append(mContext.getString(R.string.remind_create));
        handle.start_TTS(text.toString(), false);
        AddRecord.instance().requestAddRecord(Type.REMIND.getValue(), text.toString(), null, null, getSpeechResult());
    }

    /**
     * 获取重复值
     *
     * @param repeat
     * @return
     */
    public int getRepeateCode(String repeat) {
        DaysOfRepeat days = new DaysOfRepeat(0);
        if (repeat != null && !repeat.equals("")) {// 没有设置
            if (repeat.equals("WEEKEND")) {// 每周末
                days.set(5, true);
                days.set(6, true);
            } else if (repeat.equals("WORKDAY")) {// 工作日
                for (int i = 0; i < 5; i++) {
                    days.set(i, true);
                }
            } else if (repeat.startsWith("W")) {// 周循环
                String rep[] = repeat.split("-");
                int start = Integer.parseInt(rep[0].substring(1));
                if (rep.length == 1) {
                    days.set(start - 1, true);
                } else {// 1-3
                    int end = Integer.parseInt(rep[1].substring(1));
                    for (int i = start - 1; i < end; i++) {
                        days.set(i, true);
                    }
                }

            } else if (repeat.startsWith("M")) {// 每月循环
                days.set(128);
            } else if (repeat.equals("EVERYDAY")) {// 每天
                days.set(127);
            }
        }
        return days.getCoded();
    }

    /*
     * Days of week code as a single int. 0x00: no day 0x01: Monday 0x02:
     * Tuesday 0x04: Wednesday 0x08: Thursday 0x10: Friday 0x20: Saturday 0x40:
     * Sunday 2的7次方表示每月
     */
    private class DaysOfRepeat {

        // Bitmask of all repeating days
        private int mDays;

        public DaysOfRepeat(int days) {
            mDays = days;
        }

        private boolean isSet(int day) {
            return ((mDays & (1 << day)) > 0);
        }

        public void set(int day, boolean set) {
            if (set) {
                mDays |= (1 << day);
            } else {
                mDays &= ~(1 << day);
            }
        }

        public void set(int dow) {
            mDays = dow;
        }

        public int getCoded() {
            return mDays;
        }

        // Returns days of week encoded in an array of booleans.
        public boolean[] getBooleanArray() {
            boolean[] ret = new boolean[7];
            for (int i = 0; i < 7; i++) {
                ret[i] = isSet(i);
            }
            return ret;
        }

        public boolean isRepeatSet() {
            return mDays != 0;
        }

    }

    public static List<Remind> getAlarmListByDate(String date) {
        List<Remind> list = new ArrayList<Remind>();
        AlarmInfo[] infos = SysApi.get().queryAllAlarm(date);
        if (infos == null) return null;
        for (AlarmInfo info: infos){
            Remind bean = new Remind();
            bean.set_id(info.id);
            bean.setHour(info.hh);
            bean.setMinutes(info.mm);
            bean.setEnabled(info.state == 0);
            bean.setDaysofweek(info.date);
            bean.setVibrate(info.vibrate ? 1 : 0);
            bean.setMessage(info.label);
            bean.setAlert(info.alert.getPath());
            bean.setDtstart(dtLongToString(info.dtstart));
            list.add(bean);
        }
        return list;
    }

    public static String dtLongToString(long time) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String dt = df.format(time);
        return dt;
    }
}
