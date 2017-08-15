package com.ubtechinc.nets.im.model;

import android.text.TextUtils;

/**
 * Created by Administrator on 2016/12/5.
 */
public class UserInfo {
    public String accountType;
    public String userSig;
    public String appidAt3rd;
    public String id;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserSig() {
        return userSig;
    }

    public void setUserSig(String userSig) {
        this.userSig = userSig;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAppidAt3rd(String appidAt3rd) {
        this.appidAt3rd = appidAt3rd;
    }

    public String getAppidAt3rd() {
        return appidAt3rd;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("accountType = ").append(accountType).append(" ,")
                .append("userSig = ").append(userSig).append(" ,")
                .append("appidAt3rd = ").append(appidAt3rd).append(" ,")
                .append("id = ").append(id);
        return builder.toString();
    }

    public boolean hasGetAllInfo() {
        if (TextUtils.isEmpty(accountType) ||TextUtils.isEmpty(userSig)
                || TextUtils.isEmpty(appidAt3rd)) {
            return false;
        } else {
            return true;
        }
    }
}
