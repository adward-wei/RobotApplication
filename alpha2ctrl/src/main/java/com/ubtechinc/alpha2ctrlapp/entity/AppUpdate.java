package com.ubtechinc.alpha2ctrlapp.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AppUpdate extends AppUpdateModel implements Serializable, Cloneable{
	
	private static final long serialVersionUID = 7253218818751781665L;
	private int status;//-1 表示不可下载0表示初始状态，1，表示下载中，2表示下载成功，3表示下载失败，4表示安装成功，5表示安装失败,6表示不能下载，7表示更新
	private String statusTv;
	private AppUpdate mainApp;
	private AppUpdate androidApp;
	private AppUpdate robotApp;
	private AppUpdate iosApp;
	private List<AppUpdate> linkedAppList = new ArrayList<>();
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getStatusTv() {
		return statusTv;
	}
	public void setStatusTv(String statusTv) {
		this.statusTv = statusTv;
	}

	public AppUpdate getRobotApp() {
		return robotApp;
	}

	public void setRobotApp(AppUpdate robotApp) {
		this.robotApp = robotApp;
	}

	public AppUpdate getMainApp() {
		return mainApp;
	}

	public void setMainApp(AppUpdate mainApp) {
		this.mainApp = mainApp;
	}

	public AppUpdate getAndroidApp() {
		return androidApp;
	}

	public void setAndroidApp(AppUpdate androidApp) {
		this.androidApp = androidApp;
	}

	public AppUpdate getIosApp() {
		return iosApp;
	}

	public void setIosApp(AppUpdate iosApp) {
		this.iosApp = iosApp;
	}

	public List<AppUpdate> getLinkedAppList() {
		return linkedAppList;
	}

	public void setLinkedAppList(List<AppUpdate> linkedAppList) {
		this.linkedAppList = linkedAppList;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
