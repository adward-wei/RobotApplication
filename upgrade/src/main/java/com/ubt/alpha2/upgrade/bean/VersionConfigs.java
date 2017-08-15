package com.ubt.alpha2.upgrade.bean;

import java.util.List;

/**
 * @author: slive
 * @description:  版本配置信息
 * @create: 2017/6/28
 * @email: slive.shu@ubtrobot.com
 * @modified: slive
 */
public class VersionConfigs {
    public List<ApkInfo> apks;
    public String version;

    public static class ApkInfo{
        public String packagename;
        public String versionCode;
        public String fileType;
        public String filename;
        public String apkpackageMd5Value;
        public String versionName;
    }
}