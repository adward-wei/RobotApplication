package com.ubtechinc.alpha2ctrlapp.entity;


import com.ubtech.utilcode.utils.StringUtils;

import java.io.Serializable;

/**
 * @ClassName ServiceLanguage
 * @date 2016/7/22
 * @author tanghongyu
 * @Description 主服务语言
 * @modifier
 * @modify_time
 */
public class ServiceLanguage implements Serializable  {

	private String userId;


	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	//语言
	private String language;
	//中文显示
	private String languageShow;
	//是否选中
	private boolean isChecked;

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getLanguageShow() {
		return languageShow;
	}

	public void setLanguageShow(String languageShow) {
		this.languageShow = languageShow;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean checked) {
		isChecked = checked;
	}

	@Override
	public boolean equals(Object o) {
		ServiceLanguage device = (ServiceLanguage)o;

		return StringUtils.isEquals(device.getLanguage(), this.language);
	}
}
