package com.ubtechinc.alpha.model.upgrade;

/**
 * 主服务发给升级apk的关于升级结果的通信类
 *
 * @author wangzhengtian
 * @Date 2017-03-09
 */

public class Alpha2UpdateResultModel {
    /** 升级类型 **/
    private String updateType;
    /** 升级结果 **/
    private String updateResultCode;

    public String getUpdateType() {
        return updateType;
    }

    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }

    public String getUpdateResultCode() {
        return updateResultCode;
    }

    public void setUpdateResultCode(String updateResultCode) {
        this.updateResultCode = updateResultCode;
    }
}
