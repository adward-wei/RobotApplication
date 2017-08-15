package com.ubtechinc.alpha2ctrlapp.entity.response;


public class CommonResPonse {
	protected boolean status;
	protected String info;
	protected Object models;
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
	public Object getModels() {
		return models;
	}
	public void setModels(Object models) {
		this.models = models;
	}
	
}
