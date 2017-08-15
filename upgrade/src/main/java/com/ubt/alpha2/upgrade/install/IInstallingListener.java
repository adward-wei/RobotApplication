package com.ubt.alpha2.upgrade.install;

import com.ubt.alpha2.upgrade.bean.VersionConfigs;

import java.util.List;

/**
 * @author: slive
 * @description: 安装返回安装结果
 * @create: 2017/6/29
 * @email: slive.shu@ubtrobot.com
 * @modified: slive
 */
public interface IInstallingListener {
    /**
     * 安装成功回调方法
     * @param
     *
     */
    void onSuccess(VersionConfigs versionConfigs);

    /**
     * 安装失败回调方法,返回已经安装的几个apk
     *
     */
    void onFailure(List<VersionConfigs.ApkInfo> installApks);
}
