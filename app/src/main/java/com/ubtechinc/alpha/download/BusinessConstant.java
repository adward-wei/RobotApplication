package com.ubtechinc.alpha.download;

/**
 * @desc : 下载状态的常数定义
 * @author: wzt
 * @time : 2017/6/1
 * @modifier:
 * @modify_time:
 */

public class BusinessConstant {


    // 0表示初始状态
    public static final int DOWNLOAD_STATE_INIT = 0;
    // 1表示下载中，
    public static final int DOWNLOAD_STATE_DOWNLOADING = 1;
    // 2表示下载成功，
    public static final int DOWNLOAD_STATE_SUCCESS = 2;
    // 3表示下载失败
    public static final int DOWNLOAD_STATE_FAIL = 3;
    // 4表示安装成功
    public static final int INSTALL_STATE_INSTALL_SUCCESS = 4;
    // 5表示安装失败
    public static final int INSTALL_STATE_INSTALL_FAIL = 5;
    // 6表示不可安装
    public static final int ERROR_CODE_CAN_NOT_INSTALL = 6;
    // 7表示更新
    public static final int STATE_UPDATE = 7;
    // 11表示卸载成功
    public static final int UNINSTALL_STATE_SUCCESS = 11;
    // 12表示卸载失败
    public static final int UNINTALL_STATE_FAIL = 12;
    // 表示存储不足100M
    public static final int ERROR_CODE_MEMORY_INSUFFICIENT = 1001;
    // ActionID为空
    public static final int ERROR_CODE_ACTION_ID_IS_EMPTY = 1002;
    // 动作文件不存在
    public static final int ERROR_CODE_ACTION_FILE_NOT_EXIT = 1003;
    // 删除动作文件失败
    public static final int ERROR_CODE_DELETE_ACTION_FILE_FAIL = 1004;

    public static final String PACKAGENAME_CH_CHAT = "com.ubtech.iflytekmix";
    public static final String PACKAGENAME_EN_CHAT = "com.ubtechinc.alphaenglishchat";
    public static final String MAIN_SERVICE = "com.ubtechinc.alpha2services";
}
