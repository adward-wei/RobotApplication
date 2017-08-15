package com.ubtechinc.alpha2ctrlapp.entity.response;


import com.ubtechinc.alpha2ctrlapp.entity.LoginInfo;

import java.util.List;

public class ThirdLoginResponse {
	private boolean status;
	private String info;
	private List<LoginInfo> models;
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
	public List<LoginInfo> getModels() {
		return models;
	}
	public void setModels(List<LoginInfo> models) {
		this.models = models;
	}
	
}
