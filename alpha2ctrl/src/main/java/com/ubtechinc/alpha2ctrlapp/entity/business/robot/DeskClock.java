package com.ubtechinc.alpha2ctrlapp.entity.business.robot;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

 /**
  * @ClassName DeskClock
  * @date 6/16/2017
  * @author tanghongyu
  * @Description 闹钟提醒
  * @modifier
  * @modify_time
  */
public class DeskClock implements Serializable, Parcelable {
	private int type;// 操作类型 0-add 1-delete 2-update 3-find
	private int clockID;
	private int hour;
	private int minutes;
	private int daysofweek;// 重复
	private long alarmtime;
	private boolean enabled;// 是否开启
	private int vibrate;// 震动
	private String message;// 对应名称
	private String alert;// 对应动作路径
	private String dtstart;// 开始时间
	private boolean iscomplete;

	 public int getType() {
		 return type;
	 }

	 public void setType(int type) {
		 this.type = type;
	 }

	 public int getClockID() {
		 return clockID;
	 }

	 public void setClockID(int clockID) {
		 this.clockID = clockID;
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

	 public boolean isEnabled() {
		 return enabled;
	 }

	 public void setEnabled(boolean enabled) {
		 this.enabled = enabled;
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

	 public String getDtstart() {
		 return dtstart;
	 }

	 public void setDtstart(String dtstart) {
		 this.dtstart = dtstart;
	 }

	 public boolean iscomplete() {
		 return iscomplete;
	 }

	 public void setIscomplete(boolean iscomplete) {
		 this.iscomplete = iscomplete;
	 }

	 @Override
	 public String toString() {
		 return "DeskClock{" +
				 "type=" + type +
				 ", clockID=" + clockID +
				 ", hour=" + hour +
				 ", minutes=" + minutes +
				 ", daysofweek=" + daysofweek +
				 ", alarmtime=" + alarmtime +
				 ", enabled=" + enabled +
				 ", vibrate=" + vibrate +
				 ", message='" + message + '\'' +
				 ", alert='" + alert + '\'' +
				 ", dtstart='" + dtstart + '\'' +
				 ", iscomplete=" + iscomplete +
				 '}';
	 }

	 public void writeToParcel(Parcel p, int flags) {
		p.writeInt(clockID);
		p.writeInt(enabled ? 1 : 0);
		p.writeInt(hour);
		p.writeInt(minutes);
		p.writeInt(daysofweek);
		p.writeLong(alarmtime);

		p.writeInt(vibrate);
		p.writeString(message);
		p.writeString(alert);
		p.writeString(dtstart);
		p.writeInt(iscomplete?1:0);
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public DeskClock() {

	}

	public static final Creator<DeskClock> CREATOR = new Creator<DeskClock>() {
		public DeskClock createFromParcel(Parcel p) {
			return new DeskClock(p);
		}

		public DeskClock[] newArray(int size) {
			return new DeskClock[size];
		}
	};

	public DeskClock(Parcel p) {
		clockID = p.readInt();
		enabled = p.readInt() == 1;
		hour = p.readInt();
		minutes = p.readInt();
		daysofweek = p.readInt();
		alarmtime = p.readLong();
		vibrate = p.readInt();
		message = p.readString();
		alert = p.readString();
		dtstart = p.readString();
		iscomplete=p.readInt()==1;
	}
}
