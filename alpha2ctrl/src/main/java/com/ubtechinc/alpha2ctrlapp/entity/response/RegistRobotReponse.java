package com.ubtechinc.alpha2ctrlapp.entity.response;


import com.ubtechinc.alpha2ctrlapp.entity.UserInfo;

import java.util.List;

public class RegistRobotReponse {
	private boolean status;
	private String info;
	private List<UserInfo> models;
	private String macAddress;
	private int MessageCode =-1;
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public List<UserInfo> getModels() {
		return models;
	}
	public void setModels(List<UserInfo> models) {
		this.models = models;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public int getMessageCode() {
		return MessageCode;
	}
	public void setMessageCode(int messageCode) {
		MessageCode = messageCode;
	}
}
