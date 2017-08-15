package com.ubtechinc.zh_chat.entity;

/**
 * [提醒功能]
 * 
 * @author zengdengyi
 * @version 1.0
 * @date 2015年10月15日 下午3:33:28
 * 
 **/

public class Remind {
	private int type;// 操作类型 0-add 1-delete 2-update 3-find
	private int _id;
	private int hour;
	private int minutes;
	private int daysofweek;// 重复
	private long alarmtime;

	private boolean enabled;// 是否开启
	private int vibrate;// 震动
	private String message;// 对应名称
	private String alert;// 对应动作路径
	private String dtstart;//
	private boolean iscomplete;// 执行与否

	public boolean isIscomplete() {
		return iscomplete;
	}

	public void setIscomplete(boolean iscomplete) {
		this.iscomplete = iscomplete;
	}

	public String getDtstart() {
		return dtstart;
	}

	public void setDtstart(String dtstart) {
		this.dtstart = dtstart;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	public int getDaysofweek() {
		return daysofweek;
	}

	public void setDaysofweek(int daysofweek) {
		this.daysofweek = daysofweek;
	}

	public long getAlarmtime() {
		return alarmtime;
	}

	public void setAlarmtime(long alarmtime) {
		this.alarmtime = alarmtime;
	}

	public int getVibrate() {
		return vibrate;
	}

	public void setVibrate(int vibrate) {
		this.vibrate = vibrate;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getAlert() {
		return alert;
	}

	public void setAlert(String alert) {
		this.alert = alert;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return " " + _id + " " + hour + " " + minutes + " " + daysofweek + " "
				+ alarmtime + " " + enabled + " " + vibrate + " " + message
				+ " " + alert + " " + dtstart;
	}

}
