package com.ubtechinc.alpha2ctrlapp.events;

public class DateTimeEvent {

	public int year, month, dayOfMonth, hour, min;

	public DateTimeEvent(int year, int month, int dayOfMonth, int hour, int min) {
		super();
		this.year = year;
		this.month = month;
		this.dayOfMonth = dayOfMonth;
		this.hour = hour;
		this.min = min;
	}

}
