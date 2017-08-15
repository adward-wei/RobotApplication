package com.ubtechinc.alpha2ctrlapp.entity;

import com.ubtechinc.alpha2ctrlapp.entity.business.robot.RobotApp;

import java.io.Serializable;

public class AppInstalledInfo extends RobotApp implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4844489927739995467L;
	protected String appImagePath;

	public String getAppImagePath() {
		return appImagePath;
	}

	public void setAppImagePath(String appImagePath) {
		this.appImagePath = appImagePath;
	}



}
