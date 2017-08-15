/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ubtechinc.zh_chat.entity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.provider.Settings;
import android.text.format.DateFormat;

/**
 * The Alarms provider supplies info about Alarm Clock settings
 */
public class Alarms {
	static final String PREFERENCES = "AlarmClock";
	// This action triggers the AlarmReceiver as well as the AlarmKlaxon. It
	// is a public action used in the manifest for receiving Alarm broadcasts
	// from the alarm manager.
	public static final String ALARM_ALERT_ACTION = "com.ubtechinc.alpha2services.ALARM_ALERT";

	// A public action sent by AlarmKlaxon when the alarm has stopped sounding
	// for any reason (e.g. because it has been dismissed from
	// AlarmAlertFullScreen,
	// or killed due to an incoming phone call, etc).
	public static final String ALARM_DONE_ACTION = "com.android.deskclock.ALARM_DONE";

	// AlarmAlertFullScreen listens for this broadcast intent, so that other
	// applications
	// can snooze the alarm (after ALARM_ALERT_ACTION and before
	// ALARM_DONE_ACTION).
	public static final String ALARM_SNOOZE_ACTION = "com.android.deskclock.ALARM_SNOOZE";

	// AlarmAlertFullScreen listens for this broadcast intent, so that other
	// applications
	// can dismiss the alarm (after ALARM_ALERT_ACTION and before
	// ALARM_DONE_ACTION).
	public static final String ALARM_DISMISS_ACTION = "com.android.deskclock.ALARM_DISMISS";

	// This is a private action used by the AlarmKlaxon to update the UI to
	// show the alarm has been killed.
	public static final String ALARM_KILLED = "alarm_killed";

	// Extra in the ALARM_KILLED intent to indicate to the user how long the
	// alarm played before being killed.
	public static final String ALARM_KILLED_TIMEOUT = "alarm_killed_timeout";

	// This string is used to indicate a silent alarm in the db.
	public static final String ALARM_ALERT_SILENT = "silent";

	// This intent is sent from the notification when the user cancels the
	// snooze alert.
	public static final String CANCEL_SNOOZE = "cancel_snooze";

	// This string is used when passing an Alarm object through an intent.
	public static final String ALARM_INTENT_EXTRA = "intent.extra.alarm";

	// This extra is the raw Alarm object data. It is used in the
	// AlarmManagerService to avoid a ClassNotFoundException when filling in
	// the Intent extras.
	public static final String ALARM_RAW_DATA = "intent.extra.alarm_raw";

	// This string is used to identify the alarm id passed to SetAlarm from the
	// list of alarms.
	public static final String ALARM_ID = "alarm_id";

	final static String PREF_SNOOZE_ID = "snooze_id";
	final static String PREF_SNOOZE_TIME = "snooze_time";

	private final static String DM12 = "E h:mm aa";
	private final static String DM24 = "E k:mm";

	private final static String M12 = "h:mm aa";
	// Shared with DigitalClock
	final static String M24 = "kk:mm";

	public static Cursor getAlarmsCursor(ContentResolver contentResolver) {
		return contentResolver.query(Alarm.Columns.CONTENT_URI,
				Alarm.Columns.ALARM_QUERY_COLUMNS, null, null,
				Alarm.Columns.DEFAULT_SORT_ORDER);
	}

	public static List<Remind> getAlarmList(ContentResolver contentResolver) {
		List<Remind> list = new ArrayList<Remind>();
		Cursor cursor = getAlarmsCursor(contentResolver);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				while (!cursor.isAfterLast()) {// 遍历数据
					Alarm alarm = new Alarm(cursor);
					Remind bean = new Remind();
					bean.set_id(alarm.id);
					bean.setHour(alarm.hour);
					bean.setMinutes(alarm.minutes);
					bean.setEnabled(alarm.enabled);
					bean.setDaysofweek(alarm.daysOfWeek.getCoded());
					bean.setVibrate(alarm.vibrate ? 1 : 0);
					bean.setMessage(alarm.label);
					bean.setAlert(alarm.alert.getPath());
					bean.setDtstart(dtLongToString(alarm.dtstart));
					list.add(bean);
					cursor.moveToNext();
				}
			}
			cursor.close();
		}
		return list;
	}

	public static Cursor getAlarmsCursor(ContentResolver contentResolver,
			String dtstart) {
		return contentResolver.query(Alarm.Columns.CONTENT_URI,
				Alarm.Columns.ALARM_QUERY_COLUMNS, Alarm.Columns.DTSTART
						+ " =? ", new String[] { dtstart },
				Alarm.Columns.DEFAULT_SORT_ORDER);
	}

	public static List<Remind> getAlarmList(ContentResolver contentResolver,
			String dtstart) {
		List<Remind> list = new ArrayList<Remind>();
		Cursor cursor = getAlarmsCursor(contentResolver, dtstart);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				while (!cursor.isAfterLast()) {// 遍历数据
					Alarm alarm = new Alarm(cursor);
					Remind bean = new Remind();
					bean.set_id(alarm.id);
					bean.setHour(alarm.hour);
					bean.setMinutes(alarm.minutes);
					bean.setEnabled(alarm.enabled);
					bean.setDaysofweek(alarm.daysOfWeek.getCoded());
					bean.setVibrate(alarm.vibrate ? 1 : 0);
					bean.setMessage(alarm.label);
					bean.setAlert(alarm.alert.getPath());
					bean.setDtstart(dtLongToString(alarm.dtstart));
					list.add(bean);
					cursor.moveToNext();
				}
			}
			cursor.close();
		}
		return list;
	}

	public static List<Remind> getAlarmListByDate(
			ContentResolver contentResolver, String date) {
		List<Remind> list = new ArrayList<Remind>();
		Cursor cursor = getAlarmsCursor(contentResolver, date);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				while (!cursor.isAfterLast()) {// 遍历数据
					Alarm alarm = new Alarm(cursor);
					Remind bean = new Remind();
					bean.set_id(alarm.id);
					bean.setHour(alarm.hour);
					bean.setMinutes(alarm.minutes);
					bean.setEnabled(alarm.enabled);
					bean.setDaysofweek(alarm.daysOfWeek.getCoded());
					bean.setVibrate(alarm.vibrate ? 1 : 0);
					bean.setMessage(alarm.label);
					bean.setAlert(alarm.alert.getPath());
					bean.setDtstart(dtLongToString(alarm.dtstart));
					list.add(bean);
					cursor.moveToNext();
				}
			}
			cursor.close();
		}
		return list;
	}

	public static String dtLongToString(long time) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String dt = df.format(time);
		return dt;
	}

	public static long addAlarm(Context context, Alarm alarm) {
		ContentValues values = createContentValues(alarm);
		Uri uri = context.getContentResolver().insert(
				Alarm.Columns.CONTENT_URI, values);
		alarm.id = (int) ContentUris.parseId(uri);

		long timeInMillis = calculateAlarm(alarm);
		if (alarm.enabled) {
			clearSnoozeIfNeeded(context, timeInMillis);
		}
		setNextAlert(context);
		return /* timeInMillis */alarm.id;
	}

	/************** 以下不用看 **********/
	private static ContentValues createContentValues(Alarm alarm) {
		ContentValues values = new ContentValues(11);
		// Set the alarm_time value if this alarm does not repeat. This will be
		// used later to disable expire alarms.
//		long time = 0;
//		if (!alarm.daysOfWeek.isRepeatSet()) {
//			time = calculateAlarm(alarm);
//		}

		values.put(Alarm.Columns.ENABLED, alarm.enabled ? 1 : 0);
		values.put(Alarm.Columns.HOUR, alarm.hour);
		values.put(Alarm.Columns.MINUTES, alarm.minutes);
		values.put(Alarm.Columns.ALARM_TIME, alarm.time);
		values.put(Alarm.Columns.DAYS_OF_WEEK, alarm.daysOfWeek.getCoded());
		values.put(Alarm.Columns.VIBRATE, alarm.vibrate);
		values.put(Alarm.Columns.MESSAGE, alarm.label);
		// A null alert Uri indicates a silent alarm.
		values.put(
				Alarm.Columns.ALERT,
				alarm.alert == null ? ALARM_ALERT_SILENT : alarm.alert
						.toString());
		values.put(Alarm.Columns.DTSTART, alarm.dtstart);
		values.put(Alarm.Columns.ISCOMPLETE, alarm.iscomplete);
		values.put(Alarm.Columns.DTTIME, alarm.dttime);
		return values;
	}

	private static long calculateAlarm(Alarm alarm) {
		// return calculateAlarm(alarm.hour, alarm.minutes, alarm.daysOfWeek)
		// .getTimeInMillis();
		if (alarm.daysOfWeek.getCoded() < 128) {// 一律不 星期几。。。每天
			return calculateAlarm(alarm.dtstart, alarm.hour, alarm.minutes,
					alarm.daysOfWeek).getTimeInMillis();
		} else {// 指定日期-每月几号
			return calculateAlarm(alarm.dtstart, alarm.hour, alarm.minutes)
					.getTimeInMillis();
		}
	}

	/**
	 * 计算指定日期-将long时间转化为日期
	 * 
	 * @param hour
	 * @param minute
	 */
	static Calendar calculateAlarm(long date, int hour, int minute) {

		Calendar cal = Calendar.getInstance();

		cal.setTimeInMillis(date);

		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		int day = cal.get(Calendar.DAY_OF_MONTH);// 指定的天数

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());

		int nowHour = c.get(Calendar.HOUR_OF_DAY);
		int nowMinute = c.get(Calendar.MINUTE);
		int nowDay = c.get(Calendar.DAY_OF_MONTH);// 当前几号

		if (nowDay == day) {// 同一天
			// 时间已经过去了，则开始到下一个日期
			if (hour < nowHour || hour == nowHour && minute <= nowMinute) {
				c.add(Calendar.MONTH, 1);// 进入到了下一个月
				int max = c.getActualMaximum(Calendar.DATE);// 下个月最大的天数
				if (max < day) {// 下个月最大的天数小于指定日期则
					c.add(Calendar.MONTH, 1);// 进入到了下下一个月
				}
			}
		} else if (nowDay < day) {// 还没有达到指定日期
			int max = c.getActualMaximum(Calendar.DATE);// 这个月最大的天数
			if (max < day) {// 这个月最大的天数小于指定日期则
				c.add(Calendar.MONTH, 1);// 进入到了下一个月
			}
		} else {// 日期已经过了则
			c.add(Calendar.MONTH, 1);// 进入到了下一个月
			int max = c.getActualMaximum(Calendar.DATE);// 下个月最大的天数
			if (max < day) {// 下个月最大的天数小于指定日期则
				c.add(Calendar.MONTH, 1);// 进入到了下下一个月
			}
		}
		c.set(Calendar.DAY_OF_MONTH, day);
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, minute);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c;

	}

	/**
	 * Given an alarm in hours and minutes, return a time suitable for setting
	 * in AlarmManager.
	 */
	/**
	 * 计算按星期几重复的日期
	 * 
	 * @param hour
	 * @param minute
	 * @param daysOfWeek
	 * @return
	 */
	static Calendar calculateAlarm(long dtstart, int hour, int minute,
			Alarm.DaysOfWeek daysOfWeek) {
		android.util.Log.i("zdy", "calculateAlarm1");

		if (daysOfWeek.getCoded() == 0) {// 指定日期，不重复
			Calendar cal = Calendar.getInstance();

			cal.setTimeInMillis(dtstart);

			cal.set(Calendar.HOUR_OF_DAY, hour);
			cal.set(Calendar.MINUTE, minute);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			return cal;
		}

		// start with now
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());

		int nowHour = c.get(Calendar.HOUR_OF_DAY);
		int nowMinute = c.get(Calendar.MINUTE);

		// if alarm is behind current time, advance one day
		if (hour < nowHour || hour == nowHour && minute <= nowMinute) {
			c.add(Calendar.DAY_OF_YEAR, 1);
		}
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, minute);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		int addDays = daysOfWeek.getNextAlarm(c);
		if (addDays > 0)
			c.add(Calendar.DAY_OF_WEEK, addDays);
		return c;
	}

	private static void clearSnoozeIfNeeded(Context context, long alarmTime) {
		// If this alarm fires before the next snooze, clear the snooze to
		// enable this alarm.
		SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, 0);
		long snoozeTime = prefs.getLong(PREF_SNOOZE_TIME, 0);
		if (alarmTime < snoozeTime) {
			clearSnoozePreference(context, prefs);
		}
	}

	static void saveSnoozeAlert(final Context context, final int id,
			final long time) {
		SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, 0);
		if (id == -1) {
			clearSnoozePreference(context, prefs);
		} else {
			SharedPreferences.Editor ed = prefs.edit();
			ed.putInt(PREF_SNOOZE_ID, id);
			ed.putLong(PREF_SNOOZE_TIME, time);
			ed.apply();
		}
		// Set the next alert after updating the snooze.
		setNextAlert(context);
	}

	private static void clearSnoozePreference(final Context context,
			final SharedPreferences prefs) {
		final int alarmId = prefs.getInt(PREF_SNOOZE_ID, -1);
		if (alarmId != -1) {
			NotificationManager nm = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			nm.cancel(alarmId);
		}

		final SharedPreferences.Editor ed = prefs.edit();
		ed.remove(PREF_SNOOZE_ID);
		ed.remove(PREF_SNOOZE_TIME);
		ed.apply();
	};

	/**
	 * Called at system startup, on time/timezone change, and whenever the user
	 * changes alarm settings. Activates snooze if set, otherwise loads all
	 * alarms, activates next alert.
	 */
	public static void setNextAlert(final Context context) {
		if (!enableSnoozeAlert(context)) {
			Alarm alarm = calculateNextAlert(context);
			if (alarm != null) {
				enableAlert(context, alarm, alarm.time);
			} else {
				disableAlert(context);
			}
		}
	}

	private static boolean enableSnoozeAlert(final Context context) {
		SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, 0);

		int id = prefs.getInt(PREF_SNOOZE_ID, -1);
		if (id == -1) {
			return false;
		}
		long time = prefs.getLong(PREF_SNOOZE_TIME, -1);

		// Get the alarm from the db.
		final Alarm alarm = getAlarm(context.getContentResolver(), id);
		if (alarm == null) {
			return false;
		}
		// The time in the database is either 0 (repeating) or a specific time
		// for a non-repeating alarm. Update this value so the AlarmReceiver
		// has the right time to compare.
		alarm.time = time;

		enableAlert(context, alarm, time);
		return true;
	}

	public static Alarm getAlarm(ContentResolver contentResolver, int alarmId) {
		Cursor cursor = contentResolver.query(
				ContentUris.withAppendedId(Alarm.Columns.CONTENT_URI, alarmId),
				Alarm.Columns.ALARM_QUERY_COLUMNS, null, null, null);
		Alarm alarm = null;
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				alarm = new Alarm(cursor);
			}
			cursor.close();
		}
		return alarm;
	}

	private static void enableAlert(Context context, final Alarm alarm,
			final long atTimeInMillis) {
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);

		Intent intent = new Intent(ALARM_ALERT_ACTION);

		// XXX: This is a slight hack to avoid an exception in the remote
		// AlarmManagerService process. The AlarmManager adds extra data to
		// this Intent which causes it to inflate. Since the remote process
		// does not know about the Alarm class, it throws a
		// ClassNotFoundException.
		//
		// To avoid this, we marshall the data ourselves and then parcel a plain
		// byte[] array. The AlarmReceiver class knows to build the Alarm
		// object from the byte[] array.
		Parcel out = Parcel.obtain();
		alarm.writeToParcel(out, 0);
		out.setDataPosition(0);
		intent.putExtra(ALARM_RAW_DATA, out.marshall());

		PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		am.set(AlarmManager.RTC_WAKEUP, atTimeInMillis, sender);

		setStatusBarIcon(context, true);

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(atTimeInMillis);
		String timeString = formatDayAndTime(context, c);
		saveNextAlarm(context, timeString);
	}

	private static void setStatusBarIcon(Context context, boolean enabled) {
		Intent alarmChanged = new Intent("android.intent.action.ALARM_CHANGED");
		alarmChanged.putExtra("alarmSet", enabled);
		context.sendBroadcast(alarmChanged);
	}

	/**
	 * Shows day and time -- used for lock screen
	 */
	private static String formatDayAndTime(final Context context, Calendar c) {
		String format = get24HourMode(context) ? DM24 : DM12;
		return (c == null) ? "" : (String) DateFormat.format(format, c);
	}

	/* used by AlarmAlert */
	static String formatTime(final Context context, Calendar c) {
		String format = get24HourMode(context) ? M24 : M12;
		return (c == null) ? "" : (String) DateFormat.format(format, c);
	}

	static boolean get24HourMode(final Context context) {
		return DateFormat.is24HourFormat(context);
	}

	static void saveNextAlarm(final Context context, String timeString) {
		Settings.System.putString(context.getContentResolver(),
				Settings.System.NEXT_ALARM_FORMATTED, timeString);
	}

	public static Alarm calculateNextAlert(final Context context) {
		Alarm alarm = null;
		long minTime = Long.MAX_VALUE;
		long now = System.currentTimeMillis();
		Cursor cursor = getFilteredAlarmsCursor(context.getContentResolver());
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				do {
					Alarm a = new Alarm(cursor);
					// A time of 0 indicates this is a repeating alarm, so
					// calculate the time to get the next alert.
					if (a.time == 0) {
						a.time = calculateAlarm(a);
					} else if (a.time < now) {

						// Expired alarm, disable it and move along.
						enableAlarmInternal(context, a, false);
						continue;
					}
					if (a.time < minTime && a.time > now) {
						minTime = a.time;
						alarm = a;
					}
				} while (cursor.moveToNext());
			}
			cursor.close();
		}
		return alarm;
	}

	// Private method to get a more limited set of alarms from the database.
	private static Cursor getFilteredAlarmsCursor(
			ContentResolver contentResolver) {
		return contentResolver.query(Alarm.Columns.CONTENT_URI,
				Alarm.Columns.ALARM_QUERY_COLUMNS, Alarm.Columns.WHERE_ENABLED,
				null, null);
	}

	public static void enableAlarm(final Context context, final int id,
			boolean enabled) {
		enableAlarmInternal(context, id, enabled);
		setNextAlert(context);
	}

	private static void enableAlarmInternal(final Context context,
			final int id, boolean enabled) {
		enableAlarmInternal(context,
				getAlarm(context.getContentResolver(), id), enabled);
	}

	private static void enableAlarmInternal(final Context context,
			final Alarm alarm, boolean enabled) {
		if (alarm == null) {
			return;
		}
		ContentResolver resolver = context.getContentResolver();

		ContentValues values = new ContentValues(2);
		values.put(Alarm.Columns.ENABLED, enabled ? 1 : 0);

		// If we are enabling the alarm, calculate alarm time since the time
		// value in Alarm may be old.
		if (enabled) {
			long time = 0;
			if (!alarm.daysOfWeek.isRepeatSet()) {
				time = calculateAlarm(alarm);
			}
			values.put(Alarm.Columns.ALARM_TIME, time);
		} else {
			// Clear the snooze if the id matches.
			disableSnoozeAlert(context, alarm.id);
		}

		resolver.update(
				ContentUris.withAppendedId(Alarm.Columns.CONTENT_URI, alarm.id),
				values, null, null);
	}

	static void disableSnoozeAlert(final Context context, final int id) {
		SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, 0);
		int snoozeId = prefs.getInt(PREF_SNOOZE_ID, -1);
		if (snoozeId == -1) {
			// No snooze set, do nothing.
			return;
		} else if (snoozeId == id) {
			// This is the same id so clear the shared prefs.
			clearSnoozePreference(context, prefs);
		}
	}

	/**
	 * Disables alert in AlarmManger and StatusBar.
	 *
	 * @param context
	 *            context
	 */
	static void disableAlert(Context context) {
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		PendingIntent sender = PendingIntent.getBroadcast(context, 0,
				new Intent(ALARM_ALERT_ACTION),
				PendingIntent.FLAG_CANCEL_CURRENT);
		am.cancel(sender);
		setStatusBarIcon(context, false);
		saveNextAlarm(context, "");
	}
}
