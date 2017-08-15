package com.ubtechinc.alpha2ctrlapp.entity.request;

/**
 * [Third party development app update request]
 * 
 * @author zengdengyi
 * @version 1.0
 * 
 **/
    
public class AppUpdateRequst  extends CommonRequest{

	private String appKey;
	
	private String appVersion;

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	
}
