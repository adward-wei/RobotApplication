package com.ubtechinc.alpha2ctrlapp.entity.business.robot;

import java.util.List;

public class ButtonConfig {

	private List<ButtonConfigItem> models;
	private String details;
	private String version;
	private String title;
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public List<ButtonConfigItem> getModels() {
		return models;
	}

	public void setModels(List<ButtonConfigItem> models) {
		this.models = models;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
