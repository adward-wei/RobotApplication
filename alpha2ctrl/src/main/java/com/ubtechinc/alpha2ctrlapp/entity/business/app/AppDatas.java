package com.ubtechinc.alpha2ctrlapp.entity.business.app;

import java.util.List;

public class AppDatas {

	private List<AppConfigItem> datas;

	public List<AppConfigItem> getDatas() {
		return datas;
	}

	public void setDatas(List<AppConfigItem> datas) {
		this.datas = datas;
	}
	private String version;
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
