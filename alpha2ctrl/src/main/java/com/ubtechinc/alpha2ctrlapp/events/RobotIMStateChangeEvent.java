package com.ubtechinc.alpha2ctrlapp.events;

public class RobotIMStateChangeEvent {

	private String userId;
	private int state;

	public RobotIMStateChangeEvent(String userId, int state) {
		super();
		this.userId = userId;
		this.state = state;

	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
}
