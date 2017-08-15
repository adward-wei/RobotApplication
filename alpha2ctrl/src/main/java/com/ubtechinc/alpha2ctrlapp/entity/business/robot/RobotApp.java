package com.ubtechinc.alpha2ctrlapp.entity.business.robot;


import java.util.Arrays;

/**
 * @ClassName RobotApp
 * @date 6/26/2017
 * @author tanghongyu
 * @Description 机器人端应用实体信息
 * @modifier
 * @modify_time
 */
public class RobotApp {
	protected String packageName;
	protected String name;
	protected String appId;
	protected String appKey;
	protected String versionCode="";
	protected String versionName="";
	protected String url="";
	protected byte[] icon;
	/** 有配置信息 **/
	protected boolean isSetting;
	/** 有alpha 系统应用 true表示系统应用，false表示普通应用
	 **/
	protected boolean isSystemApp;
	/** 有按钮时间 **/
	protected boolean isButtonEvent;
	/** 下载状态 **/
	//0表示初始状态，1，表示下载中，2表示下载成功，3表示下载失败，4表示解压成功，5表示解压失败,6表示是系统保护的应用不能删除, 7被客户端占用，8表示存储空间不足
	protected int downloadState;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public int getDownloadState() {
		return downloadState;
	}

	public void setDownloadState(int downloadState) {
		this.downloadState = downloadState;
	}



	public boolean isSystemApp() {
		return isSystemApp;
	}

	public void setSystemApp(boolean isSystemApp) {
		this.isSystemApp = isSystemApp;
	}

	public boolean isButtonEvent() {
		return isButtonEvent;
	}


	public void setButtonEvent(boolean buttonEvent) {
		isButtonEvent = buttonEvent;
	}

	public boolean isSetting() {
		return isSetting;
	}

	public void setSetting(boolean isSetting) {
		this.isSetting = isSetting;
	}

	public byte[] getIcon() {
		return icon;
	}

	public void setIcon(byte[] icon) {
		this.icon = icon;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "RobotApp{" +
				"packageName='" + packageName + '\'' +
				", name='" + name + '\'' +
				", appKey='" + appKey + '\'' +
				", versionCode='" + versionCode + '\'' +
				", versionName='" + versionName + '\'' +
				", url='" + url + '\'' +
				", icon=" + Arrays.toString(icon) +
				", isSetting=" + isSetting +
				", isSystemApp=" + isSystemApp +
				", isButtonEvent=" + isButtonEvent +
				", downloadState=" + downloadState +
				'}';
	}
}
