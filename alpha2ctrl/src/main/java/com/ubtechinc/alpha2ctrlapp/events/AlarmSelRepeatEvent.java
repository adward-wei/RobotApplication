package com.ubtechinc.alpha2ctrlapp.events;

import com.ubtechinc.alpha2ctrlapp.entity.business.robot.DeskClock;

public class AlarmSelRepeatEvent {

	public DeskClock deskClock;

	public AlarmSelRepeatEvent(DeskClock deskClock) {
		this.deskClock = deskClock;
	}

}
