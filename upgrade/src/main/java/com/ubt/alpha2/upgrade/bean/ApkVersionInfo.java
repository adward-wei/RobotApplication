package com.ubt.alpha2.upgrade.bean;

/**
 * @author: slive
 * @description: app info
 * @create: 2017/6/27
 * @email: slive.shu@ubtrobot.com
 * @modified: slive
 */
public class ApkVersionInfo {
    // package name
    private String packageName;
    // new version
    private String newVersion;
    // 本地 app version
    private String localVersion;
    // 差分包 url
    private String patchURL;
    // 差分包 md5
    private String patchMd5;
    // 整包 url
    private String apkURL;
    // 整包 md5
    private String apkMd5;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getNewVersion() {
        return newVersion;
    }

    public void setNewVersion(String newVersion) {
        this.newVersion = newVersion;
    }

    public String getPatchURL() {
        return patchURL;
    }

    public void setPatchURL(String patchURL) {
        this.patchURL = patchURL;
    }

    public String getPatchMd5() {
        return patchMd5;
    }

    public void setPatchMd5(String patchMd5) {
        this.patchMd5 = patchMd5;
    }

    public String getApkURL() {
        return apkURL;
    }

    public void setApkURL(String apkURL) {
        this.apkURL = apkURL;
    }

    public String getApkMd5() {
        return apkMd5;
    }

    public void setApkMd5(String apkMd5) {
        this.apkMd5 = apkMd5;
    }

    public String getLocalVersion() {
        return localVersion;
    }

    public void setLocalVersion(String localVersion) {
        this.localVersion = localVersion;
    }
}