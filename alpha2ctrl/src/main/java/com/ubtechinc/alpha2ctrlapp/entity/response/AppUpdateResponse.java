package com.ubtechinc.alpha2ctrlapp.entity.response;


import com.ubtechinc.alpha2ctrlapp.entity.AppImageInfo;
import com.ubtechinc.alpha2ctrlapp.entity.AppUpdateModel;

import java.util.List;


/**
 * [app update response from server]
 * 
 * @author zengdengyi
 * @version 1.0
 * 
 **/
    
public class AppUpdateResponse {

	private boolean status;
	private String info;
	private List<AppUpdateModel> models;
	private List<AppImageInfo>urls;

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

	public List<AppUpdateModel> getModels() {
		return models;
	}

	public void setModels(List<AppUpdateModel> models) {
		this.models = models;
	}

	public List<AppImageInfo> getUrls() {
		return urls;
	}

	public void setUrls(List<AppImageInfo> urls) {
		this.urls = urls;
	}

}
