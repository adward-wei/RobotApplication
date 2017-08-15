package com.ubt.alpha2.upgrade.bean;

import com.ubt.alpha2.upgrade.utils.LogUtils;

import java.io.File;

/**
 * @author: slive
 * @description: 
 * @create: 2017/6/27
 * @email: slive.shu@ubtrobot.com
 * @modified: slive
 */

public class SystemVersionInfo {

    /**
     * {
     * "upgradeType": "0",
     * "module_id": "3",
     * "remark": "差分文件",
     * "fromVersion": "V0.7",
     * "md5": "586F366DF7FCA02736F7B9A4530D6089",
     * "updateType": "0",
     * "toVersion": "V0.9",
     * "url": "http://10.10.20.30:8010/server_30/diff/25/3/1486395022959/ota-07to08.zip"
     * }
     */

    private int id;
    private String upgradeType; //升级模块，升级类型 强制更新 1：是 0：否
    private String module_id;   //升级的模块id值
    private String remark;      //升级的模块评论
    private String fromVersion; //基础版本
    private String md5;         //升级模块，升级包的MD5值
    private String updateType;  //升级模块，版本属于增、删、改、查
    private String toVersion;   //升级版本
    private String url;         //升级模块，升级包的URL地址
    private String filepath;    //升级文件下载保存路径

    public static final String ID = "id";
    public static final String UPGRADE_TYPE = "upgradeType";
    public static final String MODULE_ID = "module_id";
    public static final String REMARK = "remark";
    public static final String FROMVERSION = "fromVersion";
    public static final String MD5 = "md5";
    public static final String UPDATETYPE = "updateType";
    public static final String TOVERSION = "toVersion";
    public static final String URL = "url";
    public static final String STATUS = "status";
    public static final String FILEPATH = "filepath";

    public String getUpgradeType() {
        return upgradeType;
    }

    public void setUpgradeType(String upgradeType) {
        this.upgradeType = upgradeType;
    }

    public String getUpdateType() {
        return updateType;
    }

    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }

    public String getModuleId() {
        return module_id;
    }

    public void setModuleId(String module_id) {
        this.module_id = module_id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getFromVersion() {
        return fromVersion;
    }

    public void setFromVersion(String fromVersion) {
        this.fromVersion = fromVersion;
    }

    public String getToVersion() {
        return toVersion;
    }

    public void setToVersion(String toVersion) {
        this.toVersion = toVersion;
    }

    public void updateVersionDownloadCompleted() {
        File file = new File(filepath);

        if(file.exists()){
            String strMd5 = "";//MD5FileUtil.encode(file);

            LogUtils.d("strMd5:" + strMd5);

            if (md5.equalsIgnoreCase(strMd5)) {
                //设置升级文件下载完成

                //DBController.getInstance(AppApplication.getAppApplication()).newOrUpdateUpdateVersion(this);

                LogUtils.d("升级文件:" + module_id + ", url:" + url + "设置升级文件下载完成");
            } else {
                LogUtils.e("升级文件MD5校验失败!!");

                //1.删除数据库升级版本信息
                //DBController.getInstance(AppApplication.getAppApplication()).deleteAllUpdateVersionByModuleID(module_id);
                //设置升级文件下载出错
                //DBController.getInstance(AppApplication.getAppApplication()).newOrUpdateUpdateVersion(this);

                //2.删除md5校验失败的下载文件
                //com.ubtechinc.alpha2systemupdate.utils.FileUtils.deleteFile(filepath);

                //TODO zikun.mo MD5校验失败的流程如何处理
                //3.服务重启是重新下载文件
            }
        }

    }

}
