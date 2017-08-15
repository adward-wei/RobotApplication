package com.ubtechinc.alpha.entity;

/**
 * @author：liuhai
 * @date：2017/4/19 10:11
 * @modifier：liuhai
 * @modify_date：2017/4/19 10:11
 * [A brief description]
 * version
 */

public class UnIntallApp {
    private String packageName;
    private String name;
    private boolean isUnInstallSuccess;

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

    public boolean isUnInstallSuccess() {
        return isUnInstallSuccess;
    }

    public void setUnInstallSuccess(boolean unInstallSuccess) {
        isUnInstallSuccess = unInstallSuccess;
    }
}