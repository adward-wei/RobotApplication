package com.ubtechinc.alpha2ctrlapp.entity.request;

public class GetWeixinUserInfo {
  private String access_token;
  private String openid;
public String getAccess_token() {
	return access_token;
}
public void setAccess_token(String access_token) {
	this.access_token = access_token;
}
public String getOpenid() {
	return openid;
}
public void setOpenid(String openid) {
	this.openid = openid;
}
  
}
