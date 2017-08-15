package com.ubtechinc.alpha.robotinfo;

/**
 * @desc : 机器人配置信息
 * @author: wzt
 * @time : 2017/5/25
 * @modifier:
 * @modify_time:
 */

public class RobotConfiguration {
    /** 语音识别语言 **/
    public String asr_Language = "zh_cn";
    /** 默认启动的第三方app **/
    public String default_App;
    /** 是否是商演版 **/
    public boolean isBusiness;
    /** 是否打开debug类log **/
    public boolean isOpenDebugLog;
    /** 是否打开Info类log **/
    public boolean isOpenInfoLog;
    /** web服务器地址 **/
    public String web_Server;
    /** 开发者服务器地址 **/
    public String develop_Server;
    /** Alice聊天机器人地址 **/
    public String alice_Server;
    /** Xmpp服务器地址 **/
    public String xmpp_Server;
    /** 唤醒词 **/
    public String wakeup_word;
    /** 5麦唤醒词阈值，主要调试用 **/
    public int wakeup_threshold_mic5;

    private static RobotConfiguration sRobotConfiguration;

    private RobotConfiguration() {}

    public static RobotConfiguration get() {
        if(sRobotConfiguration == null) {
            synchronized (RobotConfiguration.class) {
                if(sRobotConfiguration == null)
                    sRobotConfiguration = new RobotConfiguration();
            }
        }

        return sRobotConfiguration;
    }
}
