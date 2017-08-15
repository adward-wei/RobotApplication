package com.ubtechinc.alpha2ctrlapp.entity.request;


import com.ubtechinc.alpha2ctrlapp.constants.Constants;

/*************************
* @date 2016/6/24
* @author
* @Description 通用请求实体
* @modify
* @modify_time
**************************/
public class CommonRequest {
	//APP类型
	private String appType ="2";
	//服务接口版本
	private String serviceVersion="V1.0";
	//请求ID
	private String requestKey;
	//请求时间
	private String requestTime;
	//系统语言
	private String 	systemLanguage = Constants.SYSTEM_LAN ;
	public String getAppType() {
		return appType;
	}
	public void setAppType(String appType) {
		this.appType = appType;
	}
	public String getServiceVersion() {
		//return serviceVersion;
		return Constants.VERSION_NAME;//填坑或者埋一个新坑
	}
	public void setServiceVersion(String serviceVersion) {
		this.serviceVersion = serviceVersion;
	}
	public String getRequestKey() {
		return requestKey;
	}
	public void setRequestKey(String requestKey) {
		this.requestKey = requestKey;
	}
	public String getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}
	public String getSystemLanguage() {
		return systemLanguage;
	}
	public void setSystemLanguage(String systemLanguage) {
		this.systemLanguage = systemLanguage;
	}
	
  
}
