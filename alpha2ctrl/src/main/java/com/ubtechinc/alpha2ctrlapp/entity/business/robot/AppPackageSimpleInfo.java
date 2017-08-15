package com.ubtechinc.alpha2ctrlapp.entity.business.robot;

import java.util.Arrays;

/**
 * @author：ubt
 * @date：2017/4/26 17:48
 * @modifier：ubt
 * @modify_date：2017/4/26 17:48
 * [A brief description]
 * version
 */

public class AppPackageSimpleInfo {
    private String packageName;
    private String name;
    private byte[] icon;
    /**
     * App大小
     **/
    private long appSize;

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

    public byte[] getIcon() {
        return icon;
    }

    public void setIcon(byte[] icon) {
        this.icon = icon;
    }

    public long getAppSize() {
        return appSize;
    }

    public void setAppSize(long appSize) {
        this.appSize = appSize;
    }

    @Override
    public String toString() {
        return "AppPackageSimpleInfo{" +
                "packageName='" + packageName + '\'' +
                ", name='" + name + '\'' +
                ", icon=" + Arrays.toString(icon) +
                ", appSize=" + appSize +
                '}';
    }
}