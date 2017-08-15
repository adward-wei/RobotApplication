package com.ubtechinc.alpha2ctrlapp.entity.request;

public class GetShareUrlRequest extends CommonRequest{
	private String type;
	private String code;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
}
