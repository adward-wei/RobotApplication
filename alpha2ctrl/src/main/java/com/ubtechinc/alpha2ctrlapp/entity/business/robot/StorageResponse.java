package com.ubtechinc.alpha2ctrlapp.entity.business.robot;


import java.util.ArrayList;
import java.util.List;


/**
 * @ClassName StorageResponse
 * @date 6/17/2017
 * @author tanghongyu
 * @Description
 * @modifier
 * @modify_time
 */
public class StorageResponse {

//剩余内部存储空间
    private long availableInternalSize;
  //总内部存储空间
    private long totalInternalSize;
//剩余外部存储空间
    private long availableExternalSize;
//剩余外部存储空间
    private long totalExternalSize;

    private List<AppPackageSimpleInfo> appList = new ArrayList<AppPackageSimpleInfo>();

    public List<AppPackageSimpleInfo> getAppList() {
        return appList;
    }

    public void setAppList(List<AppPackageSimpleInfo> appList) {
        this.appList = appList;
    }


    public long getAvailableInternalSize() {
        return availableInternalSize;
    }

    public void setAvailableInternalSize(long availableInternalSize) {
        this.availableInternalSize = availableInternalSize;
    }

    public long getTotalInternalSize() {
        return totalInternalSize;
    }

    public void setTotalInternalSize(long totalInternalSize) {
        this.totalInternalSize = totalInternalSize;
    }

    public long getAvailableExternalSize() {
        return availableExternalSize;
    }

    public void setAvailableExternalSize(long availableExternalSize) {
        this.availableExternalSize = availableExternalSize;
    }

    public long getTotalExternalSize() {
        return totalExternalSize;
    }

    public void setTotalExternalSize(long totalExternalSize) {
        this.totalExternalSize = totalExternalSize;
    }
}
