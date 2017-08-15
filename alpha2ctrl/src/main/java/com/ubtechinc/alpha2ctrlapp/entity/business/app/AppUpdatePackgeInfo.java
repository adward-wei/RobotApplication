package com.ubtechinc.alpha2ctrlapp.entity.business.app;

import com.ubtechinc.alpha2ctrlapp.entity.business.shop.AppInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName AppUpdatePackgeInfo
 * @date 5/15/2017
 * @author tanghongyu
 * @Description app更新包信息（一个更新包，是IOS、Android、Robot的组合）
 * @modifier
 * @modify_time
 */
public class AppUpdatePackgeInfo extends AppInfo implements Serializable, Cloneable{
	
	private static final long serialVersionUID = 7253218818751781665L;
	private int status;//-1 表示不可下载0表示初始状态，1，表示下载中，2表示下载成功，3表示下载失败，4表示安装成功，5表示安装失败,6表示不能下载，7表示更新
	private String statusTv;
	private AppUpdatePackgeInfo mainApp;
	private AppUpdatePackgeInfo androidApp;
	private AppUpdatePackgeInfo robotApp;
	private AppUpdatePackgeInfo iosApp;
	private List<AppUpdatePackgeInfo> linkedAppList = new ArrayList<>();
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

	public AppUpdatePackgeInfo getRobotApp() {
		return robotApp;
	}

	public void setRobotApp(AppUpdatePackgeInfo robotApp) {
		this.robotApp = robotApp;
	}

	public AppUpdatePackgeInfo getMainApp() {
		return mainApp;
	}

	public void setMainApp(AppUpdatePackgeInfo mainApp) {
		this.mainApp = mainApp;
	}

	public AppUpdatePackgeInfo getAndroidApp() {
		return androidApp;
	}

	public void setAndroidApp(AppUpdatePackgeInfo androidApp) {
		this.androidApp = androidApp;
	}

	public AppUpdatePackgeInfo getIosApp() {
		return iosApp;
	}

	public void setIosApp(AppUpdatePackgeInfo iosApp) {
		this.iosApp = iosApp;
	}

	public List<AppUpdatePackgeInfo> getLinkedAppList() {
		return linkedAppList;
	}

	public void setLinkedAppList(List<AppUpdatePackgeInfo> linkedAppList) {
		this.linkedAppList = linkedAppList;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
