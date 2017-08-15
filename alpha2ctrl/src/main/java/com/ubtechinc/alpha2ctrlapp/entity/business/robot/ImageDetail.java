package com.ubtechinc.alpha2ctrlapp.entity.business.robot;

import java.io.Serializable;

public class ImageDetail implements Serializable {
	private String dateType;
	private String imageName;
	private String imagePath;
	public String getDateType() {
		return dateType;
	}
	public void setDateType(String dateType) {
		this.dateType = dateType;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

}
