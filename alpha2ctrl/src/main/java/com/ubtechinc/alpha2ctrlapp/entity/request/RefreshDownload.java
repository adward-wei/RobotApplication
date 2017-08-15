package com.ubtechinc.alpha2ctrlapp.entity.request;

/**
 * Created by nixiaoyan on 2016/12/2.
 */

public class RefreshDownload extends CommonRequest {
    private int appId;

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }
}
