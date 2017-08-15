package com.ubtechinc.alpha2ctrlapp.entity.business.robot;

import java.io.Serializable;


public class ChatInfoItem implements Serializable{
	
	private static final long serialVersionUID = 3998075489343609465L;
	public static final int CHAT_TYPE_INFO = 0;
	public static final int CHAT_TYPE_PIC = 1;

	private int Type;
	private String Name;
	private String Time;
	private String Info;
	private int  count ;
	public int getType() {
		return Type;
	}
	public void setType(int type) {
		Type = type;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getTime() {
		return Time;
	}
	public void setTime(String time) {
		Time = time;
	}
	public String getInfo() {
		return Info;
	}
	public void setInfo(String info) {
		Info = info;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
}