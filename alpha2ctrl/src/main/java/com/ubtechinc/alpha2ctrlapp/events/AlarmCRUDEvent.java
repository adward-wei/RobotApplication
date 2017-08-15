package com.ubtechinc.alpha2ctrlapp.events;

import com.ubtechinc.alpha2ctrlapp.entity.business.robot.DeskClock;

public class AlarmCRUDEvent {

	public DeskClock deskClock;

	public AlarmCRUDEvent(DeskClock deskClock) {
		this.deskClock = deskClock;
	}
}
