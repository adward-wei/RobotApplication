package com.ubtechinc.alpha.robotinfo;

/**
 * @desc : 软件版本信息
 * @author: wzt
 * @time : 2017/5/23
 * @modifier:
 * @modify_time:
 */

public class SoftwareVersionInfo {
    public String chestVersion;
    public String headVersion;
    public String batteryVersion;
    public String serviceVersionName;
    public int serviceVersionCode;
    public String deviceVersion;

    private static SoftwareVersionInfo sSoftwareVersionInfo;

    private SoftwareVersionInfo() {}

    public static SoftwareVersionInfo get() {
        if(sSoftwareVersionInfo == null) {
            synchronized (SoftwareVersionInfo.class) {
                if(sSoftwareVersionInfo == null)
                    sSoftwareVersionInfo = new SoftwareVersionInfo();
            }
        }

        return sSoftwareVersionInfo;
    }
}
