package com.ubt.alpha2.upgrade.install;/** * @author: slive * @description: 不返回安装成功结果 * @create: 2017/6/29 * @email: slive.shu@ubtrobot.com * @modified: slive */public interface IInstallResultListener {    /**     * 请求成功回调方法     * @param result     *            结果     */    void onSuccess(String result);    /**     * 请求失败回调方法     *     */    void onFailure(String reason);}