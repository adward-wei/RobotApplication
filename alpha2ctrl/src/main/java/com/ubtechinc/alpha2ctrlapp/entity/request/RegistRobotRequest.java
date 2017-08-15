package com.ubtechinc.alpha2ctrlapp.entity.request;

public class RegistRobotRequest extends CommonRequest {
	private String userName;
	private String userOnlyId;
	private String activeArea;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserOnlyId() {
		return userOnlyId;
	}
	public void setUserOnlyId(String userOnlyId) {
		this.userOnlyId = userOnlyId;
	}
	public String getActiveArea() {
		return activeArea;
	}
	public void setActiveArea(String activeArea) {
		this.activeArea = activeArea;
	}
	

}
