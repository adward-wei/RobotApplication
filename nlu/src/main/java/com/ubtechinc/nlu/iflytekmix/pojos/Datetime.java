package com.ubtechinc.nlu.iflytekmix.pojos;

public class Datetime {
	private String date;
	private String type;
	private String dateOrig;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDateOrig() {
		return dateOrig;
	}

	public void setDateOrig(String dateOrig) {
		this.dateOrig = dateOrig;
	}

	// 提醒
	// "dateOrig": "今天",
	// "type": "DT_BASIC",
	// "time": "14:00:00",
	// "timeOrig": "下午",
	// "date": "2016-01-06"
	private String time;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}
