package com.ubtechinc.alpha2ctrlapp.entity.business.shop;
/**
 * @ClassName AppInfo
 * @date 6/6/2017
 * @author tanghongyu
 * @Description 商店应用相关信息
 * @modifier
 * @modify_time
 */
public class AppInfo {

	//App上传日期 例如 ："2016-09-22 17:23:40.0",
	protected String appDate;
	//App相关的测试账号 例如 ： 250762,255262,260972,254582,250672,2593
	protected String appDebugAccount;
	//App说明
	protected String appDesc;
	//App
	protected int appDownloadTime;
	protected String appEquipment;
	//App图标地址
	protected String appIcon;
	protected int appId;
	protected String appKey;
	protected String appLanguageName;
	protected String appLinkId;
	protected String appName;
	//App下载地址
	protected String appPath;
	//旧：App描述 新：App审核描述
	protected String appResume;
	//例如 ：http://10.10.1.54:8081/appScan/2.jpg
	protected String appScanshot;// 应用截图
	protected String appScreen;
	protected String appImagePath;
	protected String appPackage;
	protected String appCategory;
	protected String appHeadImage;
	protected String appDesciber;
	protected String appVersion;
	protected String appSize;
	protected int versionCode;

	protected String appLanguageDesciber;

	protected String appType;
	protected String sdkType ;

	protected String runLevel;
	protected String bundleId;
	protected String packageName;

	protected int userId;
	protected int appUserId;

	protected int code;

	protected String appStatus;


	protected String appLanguage;

	protected String accessToken;

	protected String appLinkPath;

	protected String appVersionName;

	public int getAppDownloadTime() {
		return appDownloadTime;
	}

	public void setAppDownloadTime(int appDownloadTime) {
		this.appDownloadTime = appDownloadTime;
	}

	public String getAppVersionName() {
		return appVersionName;
	}

	public void setAppVersionName(String appVersionName) {
		this.appVersionName = appVersionName;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppImagePath() {
		return appImagePath;
	}

	public void setAppImagePath(String appImagePath) {
		this.appImagePath = appImagePath;
	}

	public String getAppPath() {
		return appPath;
	}

	public void setAppPath(String appPath) {
		this.appPath = appPath;
	}

	public String getAppPackage() {
		return appPackage;
	}

	public void setAppPackage(String appPackage) {
		this.appPackage = appPackage;
	}

	public String getAppCategory() {
		return appCategory;
	}

	public void setAppCategory(String appCategory) {
		this.appCategory = appCategory;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public String getAppHeadImage() {
		return appHeadImage;
	}

	public void setAppHeadImage(String appHeadImage) {
		this.appHeadImage = appHeadImage;
	}

	public String getAppDesciber() {
		return appDesciber;
	}

	public void setAppDesciber(String appDesciber) {
		this.appDesciber = appDesciber;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getAppSize() {
		return appSize;
	}

	public void setAppSize(String appSize) {
		this.appSize = appSize;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public String getAppResume() {
		return appResume;
	}

	public void setAppResume(String appResume) {
		this.appResume = appResume;
	}

	public String getAppLanguageName() {
		return appLanguageName;
	}

	public void setAppLanguageName(String appLanguageName) {
		this.appLanguageName = appLanguageName;
	}

	public String getAppLanguageDesciber() {
		return appLanguageDesciber;
	}

	public void setAppLanguageDesciber(String appLanguageDesciber) {
		this.appLanguageDesciber = appLanguageDesciber;
	}


	public String getSdkType() {
		return sdkType;
	}

	public void setSdkType(String sdkType) {
		this.sdkType = sdkType;
	}

	public String getAppIcon() {
		return appIcon;
	}

	public void setAppIcon(String appIcon) {
		this.appIcon = appIcon;
	}

	public String getRunLevel() {
		return runLevel;
	}

	public void setRunLevel(String runLevel) {
		this.runLevel = runLevel;
	}

	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getAppDesc() {
		return appDesc;
	}

	public void setAppDesc(String appDesc) {
		this.appDesc = appDesc;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getAppUserId() {
		return appUserId;
	}

	public void setAppUserId(int appUserId) {
		this.appUserId = appUserId;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getAppStatus() {
		return appStatus;
	}

	public void setAppStatus(String appStatus) {
		this.appStatus = appStatus;
	}

	public String getAppDate() {
		return appDate;
	}

	public void setAppDate(String appDate) {
		this.appDate = appDate;
	}

	public String getAppScanshot() {
		return appScanshot;
	}

	public void setAppScanshot(String appScanshot) {
		this.appScanshot = appScanshot;
	}

	public String getAppScreen() {
		return appScreen;
	}

	public void setAppScreen(String appScreen) {
		this.appScreen = appScreen;
	}

	public String getAppLanguage() {
		return appLanguage;
	}

	public void setAppLanguage(String appLanguage) {
		this.appLanguage = appLanguage;
	}

	public String getAppEquipment() {
		return appEquipment;
	}

	public void setAppEquipment(String appEquipment) {
		this.appEquipment = appEquipment;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getAppLinkId() {
		return appLinkId;
	}

	public void setAppLinkId(String appLinkId) {
		this.appLinkId = appLinkId;
	}

	public String getAppLinkPath() {
		return appLinkPath;
	}

	public void setAppLinkPath(String appLinkPath) {
		this.appLinkPath = appLinkPath;
	}

	public String getAppDebugAccount() {
		return appDebugAccount;
	}

	public void setAppDebugAccount(String appDebugAccount) {
		this.appDebugAccount = appDebugAccount;
	}
}
