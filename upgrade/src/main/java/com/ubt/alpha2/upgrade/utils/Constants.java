package com.ubt.alpha2.upgrade.utils;

/**
 * Created by ubt on 2017/6/27.
 */

public class Constants {
    public static final String UPGRADE_ACTION = "com.ubt.alpha2.upgrade.action";

    //public static final String ROBOT_ID_KEY ="Robot_SerialNumber";
    public static final String ROBOT_ID_KEY =" ro.serialno";
    /** 是否在升级中 系统属性 **/ // 注： 如果存在系统正在安装  则 主服务没权利安装，且系统安装完后会重启
    public static final String IS_LYNX_INSTALLING = "isLynxInstalling";

    public static final String SELF_REPLACE_RESTART = "self_restart";

    /** 时间到，禁止安装 **/
    public static final String FORBIT_INSTALL = "isForbidInstall";
    /** 主服务开始进入升级模式 ，灯效只能转动**/
    public static final String IS_MAINSERVICE_UPDATE = "isMainserviceUpdate";

    /** 电量 **/
    public static final byte CHEST_SEND_POWER = (byte) 0x80;


    /** 杀死应用等待时间 单位ms **/
    public static final int Kill_WAIT_TIME  = 1000;

    /** 升级apk发给主服务的广播 **/
    public static final String UPDATE_ACTION_MASTER = "com.ubtechinc.function.master";
    /** 主服务发给升级apk的广播 **/
    public static final String UPDATE_ACTION_SERVANT = "com.ubtechinc.function.servant";

    public static final String GET_COMMAND = "get_command";
    public static final String GET_PARAMETER = "get_parameter";

    /** 升级通信命令：语音提示信息 **/
    public static final int UPDATE_CMD_TTSCONTENT = 4;

    public static class UpgradeCommand{

        /**
         * 开始升级 参数:1---4,文件大小,单位字节.
         */
        public static final byte CHES_CMD_START_UPDATE = 0x30;

        /**
         * 0x31:升级数据包 参数:1-2,数据包page 参数:3-66,共64字节数据,不到64字节用00填满
         */
        public static final byte CHES_CMD_UPDATE_PAGE = 0X31;

        /**
         * 0x32:结束升级
         */
        public static final byte CHES_CMD_UPDATE_END = 0x32;

        /**
         * 开始升级 参数:1---4,文件大小,单位字节.
         */
        public static final byte HEAD_CMD_START_UPDATE = 0x30;

        /**
         * 0x31:升级数据包 参数:1-2,数据包page 参数:3-66,共64字节数据,不到64字节用00填满
         */
        public static final byte HEAD_CMD_UPDATE_PAGE = 0X31;

        /**
         * 0x32:结束升级
         */
        public static final byte HEAD_CMD_UPDATE_END = 0x32;


        /** 电池包升级开始 **/
        public static final byte CHEST_BATTERY_UPDATE_START = (byte) 0x45;
        /** 电池包升级数据 **/
        public static final byte CHEST_BATTERY_UPDATE_PAGE = (byte) 0x46;
        /** 电池包升级结束 **/
        public static final byte CHEST_BATTERY_UPDATE_END = (byte) 0x47;
    }

    /**
     * 胸部广播消息
     */
    public static final String CHEST_ACTION = "com.ubtechinc.services.chest";

    /**
     * 头部广播消息
     */
    public static final String HEADER_ACTION = "com.ubtechinc.services.header";

}
