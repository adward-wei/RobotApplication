package com.ubtechinc.alpha2ctrlapp.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.DeskClock;
import com.ubtechinc.alpha2ctrlapp.events.DateTimeEvent;

@SuppressLint("DefaultLocale")
public class AlarmUtil {
	private static final String TAG = AlarmUtil.class.getSimpleName();
	private static final int WORKDAY = 1 | 1 << 1 | 1 << 2 | 1 << 3 | 1 << 4 ;
	private static final int WEEKEND = 1 << 5 | 1 << 6;
	public static final int SUNDAY = 1 << 6;

	public static final int[] DAYS_STR_RES = {
			 R.string.alarm_monday,
			 R.string.alarm_tuesday,
			 R.string.alarm_wednesday,
			 R.string.alarm_thursday,
			 R.string.alarm_friday,
			 R.string.alarm_saturday,
			 R.string.alarm_sunday,
	};

	/** 
	 * 0: once <br/>
	 * 127: everyday <br/>
	 * x: evryweek <br/>
	 * 128: everymonth <br/> 
	 * @see DeskClock
	 */ 
	public static enum RepeatType {
		once, everyday, everyweek, everymonth;
	}
	
	public static RepeatType getDeskClockRepeatType(DeskClock deskClock) {
		switch (deskClock.getDaysofweek()) {
		case 0:
		return RepeatType.once;
		case 128:
		return RepeatType.everymonth;
		case 127:
		return RepeatType.everyday;
		default:
			return RepeatType.everyweek;
		}
	}
	
//	public static String getRepeatTypeStr(Context context, RepeatType repeatType, int dayOfWeek) {
//		Resources res = context.getResources();
//		switch(repeatType) {
//		case once:
//			return res.getString(R.string.repeat_once);
//		case everyday:
//			return res.getString(R.string.repeat_everyday);
//		case everyweek:
//			return getDayOfWeekStr(context, dayOfWeek);
//		case everymonth:
//			return res.getString(R.string.repeat_everymonth);
//		default:
//			throw new RuntimeException("never happend");
//		}
//	}
	
	public static String getRepeatTypeStr(Context context, DeskClock dc) {
		Resources res = context.getResources();
		switch(getDeskClockRepeatType(dc)) {
		case once:
			return res.getString(R.string.alarm_repeat_once);
		case everyday:
			return res.getString(R.string.alarm_repeat_everyday);
		case everyweek:
			return getDayOfWeekStr(context, dc.getDaysofweek());
		case everymonth:
			String str = dc.getDtstart();			
			return res.getString(R.string.alarm_repeat_everymonth) + str.substring(str.length() - 2) + "日";
		default:
			throw new RuntimeException("never happend");
		}
	}
	
	/**
	 * { <br/>
	 * 	selDayOfWeekCount: 被选中星期个数, <br/>
	 *  noHitIndex: selDayOfWeekCount = 6, 没被选中的唯一个星期, <br/>
	 * } <br/>
	 * 例如 11000000 返回周末,  0011111返回工作日<br/>
	 * @param context 上下文
	 * @param dayOfWeek 一周星期数二进制
	 * @return 
	 */
	public static String getDayOfWeekStr(Context context, int dayOfWeek) {
		StringBuilder sb = new StringBuilder();
		StringBuilder debugStr = new StringBuilder();
		Resources resources = context.getResources();
		
		int selDayOfWeekCount = 0, noHitIndex = 0;
		for (int i = 0; i < 7; ++i) {		
			if ((dayOfWeek & (1 << i)) == (1 << i)) { 
				sb.append(resources.getString(AlarmUtil.DAYS_STR_RES[i]) + " ");
				debugStr.append(resources.getString(AlarmUtil.DAYS_STR_RES[i]) + " ");
				++selDayOfWeekCount;
			} else {
				noHitIndex = i;
			}
		}
		Log.d(TAG, "getDayOfWeekStr: " + debugStr.toString());
		switch (selDayOfWeekCount) {
		case 2: //星期六，星期天 周末
			if (dayOfWeek == WEEKEND) {
				return resources.getString(R.string.alarm_weekend);
			}
			break;
		case 5:
			if (dayOfWeek == WORKDAY) { // 星期一至星期五，工作日
				return resources.getString(R.string.alarm_workday);
			} 
			break;
		case 6: //仅有一天不提醒
			return ("仅" + resources.getString(AlarmUtil.DAYS_STR_RES[noHitIndex]) + "不提醒");
		}
		return sb.toString();
	}
	
	/**
	 * 闹钟详情和
	 * @param dc
	 * @param event
	 */
	public static void fillDeskClock(DeskClock dc, DateTimeEvent event) {
		String format = String.format("%d-%02d-%02d", event.year, event.month, event.dayOfMonth);
		dc.setDtstart(format);
		dc.setHour(event.hour);
		dc.setMinutes(event.min);	
	}
	
	/**
	 * 提供从DeskClock获取格式化文本
	 * @param dc
	 * @return
	 */
	public static String getAlarmDateTimeStr(DeskClock dc) {
		Log.d(TAG, "getAlarmDateTimeStr: " + dc.getDtstart());
		if(dc.getDaysofweek()==127){
			return String.format("%02d:%02d",dc.getHour(),
					dc.getMinutes()
					);
		}
		String[] dateArray = dc.getDtstart().split("-");
		return String.format("%s/%s/%s %02d:%02d",
				dateArray[0],
				dateArray[1],
				dateArray[2],
				dc.getHour(),
				dc.getMinutes());
	}
		
	/**
	 * 提供从DeskClock获取格式化文本
	 * @param dc
	 * @return
	 */
	public static String getDeskClockDateTimeStr(DeskClock dc) {
		Log.d(TAG, "getDeskClockDateTimeStr: " + dc.getDtstart());
		String[] dateArray = dc.getDtstart().split("-");
		return String.format("%s-%s-%s %02d:%02d",
				dateArray[0],
				dateArray[1],
				dateArray[2],
				dc.getHour(),
				dc.getMinutes());
	}
	
	/**
	 * 提供从DeskClock获取格式化文本
	 * @param dc
	 * @return
	 */
	public static String getDeskClockDateStr(DeskClock dc) {
		Log.d(TAG, "getDeskClockDateStr: " + dc.getDtstart());
		String[] dateArray = dc.getDtstart().split("-");
		return String.format("%s-%s-%s",
				dateArray[0],
				dateArray[1],
				dateArray[2]);
	}
	
	/**
	 * 提供从DeskClock获取格式化文本
	 * @param dc
	 * @return
	 */
	public static String getDeskClockTimeStr(DeskClock dc) {
		Log.d(TAG, "getDeskClockTimeStr: " + dc.getDtstart());
		return String.format("%02d:%02d",
				dc.getHour(),
				dc.getMinutes());
	}
}
