package com.ubtechinc.alpha2ctrlapp.entity.business.robot;

import java.io.Serializable;

/**
 * @ClassName AlphaParam
 * @date 2016/6/27.
 * @author nixiaoyan
 * @Description 
 * @modifier
 * @modify_time
 */
public class AlphaParam implements Serializable {


    // android系统版本号
    private  String androidVersion;
    // 主服务版本号
    private   String serviceVersionName;
    private int serviceVersionCode;
    // 头部版本号
    private  String headerVersion;
    // 胸部版本号
    private  String chestVersion;
    // sd卡总容量
    private  long SDTotalVolume;
    // sd卡剩余容量
    private  long SDAvailableVolume;
    //主服务语言
    private String serviceLanguage;
    // sd主人称呼名称
    private String masterName;

    public String getServiceVersionName() {
        return serviceVersionName;
    }

    public void setServiceVersionName(String serviceVersionName) {
        this.serviceVersionName = serviceVersionName;
    }

    public int getServiceVersionCode() {
        return serviceVersionCode;
    }

    public void setServiceVersionCode(int serviceVersionCode) {
        this.serviceVersionCode = serviceVersionCode;
    }

    public String getAndroidVersion() {
        return androidVersion;
    }

    public void setAndroidVersion(String androidVersion) {
        this.androidVersion = androidVersion;
    }


    public String getHeaderVersion() {
        return headerVersion;
    }

    public void setHeaderVersion(String headerVersion) {
        this.headerVersion = headerVersion;
    }

    public String getChestVersion() {
        return chestVersion;
    }

    public void setChestVersion(String chestVersion) {
        this.chestVersion = chestVersion;
    }

    public long getSDTotalVolume() {
        return SDTotalVolume;
    }

    public void setSDTotalVolume(long SDTotalVolume) {
        this.SDTotalVolume = SDTotalVolume;
    }

    public long getSDAvailableVolume() {
        return SDAvailableVolume;
    }

    public void setSDAvailableVolume(long SDAvailableVolume) {
        this.SDAvailableVolume = SDAvailableVolume;
    }

    public String getServiceLanguage() {
        return serviceLanguage;
    }

    public void setServiceLanguage(String serviceLanguage) {
        this.serviceLanguage = serviceLanguage;
    }

    public String getMasterName() {
        return masterName;
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }

    @Override
    public String toString() {
        return "AlphaParam{" +
                "androidVersion='" + androidVersion + '\'' +
                ", serviceVersionName='" + serviceVersionName + '\'' +
                ", serviceVersionCode=" + serviceVersionCode +
                ", headerVersion='" + headerVersion + '\'' +
                ", chestVersion='" + chestVersion + '\'' +
                ", SDTotalVolume=" + SDTotalVolume +
                ", SDAvailableVolume=" + SDAvailableVolume +
                ", serviceLanguage='" + serviceLanguage + '\'' +
                ", masterName='" + masterName + '\'' +
                '}';
    }
}

