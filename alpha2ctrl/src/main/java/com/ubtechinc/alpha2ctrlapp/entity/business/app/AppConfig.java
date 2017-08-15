package com.ubtechinc.alpha2ctrlapp.entity.business.app;

import java.util.List;

public class AppConfig {
	private List<AppItemGroup> models;

	public List<AppItemGroup> getModels() {
		return models;
	}

	public void setModels(List<AppItemGroup> models) {
		this.models = models;
	}

	private String version;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
