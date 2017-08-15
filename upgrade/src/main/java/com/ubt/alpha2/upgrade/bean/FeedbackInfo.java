package com.ubt.alpha2.upgrade.bean;

import android.support.annotation.IntDef;

/**
 * @author: slive
 * @description: 升级反馈信息
 * @create: 2017/6/29
 * @email: slive.shu@ubtrobot.com
 * @modified: slive
 */
public class FeedbackInfo {

    public static final int NO_RESPONSE = 0;  //app端没有返回状态
    public static final int INSTALL_SUCCESS = 1; //下载成功，并安装成功
    public static final int DOWNLOAD_FAILED = 2;  //下载失败
    public static final int DOWNLOAD_NO_INSTALL = 3; //下载成功，但未安装
    public static final int DOWNLOAD_INSTALL_FAILED = 4;  //下载成功，安装失败

    public String robot_id;
    public String module_name;
    public String from_version;
    public String access_token;
    public String to_version;
    public String module_id;
    public String upgrade_status;
    public String robot_type;


    @IntDef({NO_RESPONSE,INSTALL_SUCCESS,DOWNLOAD_FAILED,DOWNLOAD_NO_INSTALL,DOWNLOAD_INSTALL_FAILED})
    public @interface UpgradeStatus{}

    public FeedbackInfo() {

    }
    
}
