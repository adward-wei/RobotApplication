package com.ubtechinc.alpha.appmanager;

import com.ubtechinc.alpha.provider.EntityManagerHelper;
import com.ubtechinc.framework.db.annotation.Column;
import com.ubtechinc.framework.db.annotation.GenerationType;
import com.ubtechinc.framework.db.annotation.Table;

import java.io.Serializable;

/**
 * @desc : 第三方APP信息
 * @author: wzt
 * @time : 2017/5/23
 * @modifier:
 * @modify_time:
 */
@Table(version = EntityManagerHelper.DB_APP_INFO_VERSION)
public class AppInfo implements Serializable {

	private static final long serialVersionUID = 1122907755312881321L;

	@com.ubtechinc.framework.db.annotation.Id(strategy = GenerationType.AUTO_INCREMENT)
	public int Id;
	/**
	 * 第三方APP的包名
	 */
	@Column
	public String packageName;
	/**
	 * 第三方APP的appid
	 */
	@Column
	public String appKey;
	
	/** 第三方 app的名字 **/
	@Column
	public String appName;

	/**是否是官方app*/
	@Column
	private String isOfficial;


	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getPackageName() {
		return packageName;
	}
	
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	@Override
	public String toString() {
		return "AppInfo{" +
				"Id=" + Id +
				", packageName='" + packageName + '\'' +
				", appKey='" + appKey + '\'' +
				", appName='" + appName + '\'' +
				", isOfficial='" + isOfficial + '\'' +
				'}';
	}
}
