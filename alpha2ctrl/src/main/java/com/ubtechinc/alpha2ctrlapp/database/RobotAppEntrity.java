package com.ubtechinc.alpha2ctrlapp.database;

import com.ubtechinc.framework.db.annotation.Column;
import com.ubtechinc.framework.db.annotation.GenerationType;
import com.ubtechinc.framework.db.annotation.Id;
import com.ubtechinc.framework.db.annotation.Table;

/**
 * @ClassName RobotApp
 * @date 5/22/2017
 * @author tanghongyu
 * @Description 数据库实体，存储机器人端返回的App列表信息
 * @modifier
 * @modify_time
 */
@Table(version = EntityManagerHelper.DB_APP_VERSION)
public class RobotAppEntrity {
	@Id(strategy = GenerationType.AUTO_INCREMENT)
	public int id;
	@Column
	private String packageName;
	@Column
	private String name;
	@Column
	private String appId;
	@Column
	private String appKey;
	@Column
	private String versionCode="";
	@Column
	private String versionName="";
	@Column
	private boolean isDownLoad;
	@Column
	private String url="";
	@Column
	private String appImagePath;
	/** 有配置信息 **/
	@Column
	private boolean isSetting;
	/** 有alpha 系统应用 true表示系统应用，false表示普通应用
	 **/
	@Column
	private boolean isSystemApp;
	/** 有按钮时间 **/
	@Column
	private boolean isButtonEvent;
	/** 下载状态 **/
	//0表示初始状态，1，表示下载中，2表示下载成功，3表示下载失败，4表示解压成功，5表示解压失败,6表示是系统保护的应用不能删除, 7被客户端占用，8表示存储空间不足
	@Column
	private int downloadState;
	@Column
	private String serialNo;

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getAppImagePath() {
		return appImagePath;
	}

	public void setAppImagePath(String appImagePath) {
		this.appImagePath = appImagePath;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
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

	public void setButtonEvent(boolean isButtonEvent) {
		this.isButtonEvent = isButtonEvent;
	}

	public boolean isSetting() {
		return isSetting;
	}

	public void setSetting(boolean isSetting) {
		this.isSetting = isSetting;
	}

	public int getDownloadState() {
		return downloadState;
	}

	public void setDownloadState(int downloadState) {
		this.downloadState = downloadState;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isDownLoad() {
		return isDownLoad;
	}

	public void setDownLoad(boolean isDownLoad) {
		this.isDownLoad = isDownLoad;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
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
				", appId='" + appId + '\'' +
				", versionCode='" + versionCode + '\'' +
				", versionName='" + versionName + '\'' +
				", isDownLoad=" + isDownLoad +
				", url='" + url + '\'' +
				", isSetting=" + isSetting +
				", isSystemApp=" + isSystemApp +
				", isButtonEvent=" + isButtonEvent +
				", downloadState=" + downloadState +
				'}';
	}
}
