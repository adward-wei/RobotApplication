package com.ubtechinc.zh_chat.robot;


/**
 * <pre>
 *   @author: Logic
 *   @email : 2091843903@qq.com
 *   @time  : 2017/3/24
 *   @desc  : 机器人代理类
 * </pre>
 */
public interface UBTSemanticRootProxy {
    void start_TTS(String text, boolean isNeedAction);
    void start_TTS(UBTSemanticRobot.ISpeechTTSFinishedListener listener, String text, boolean isNeedAction);
    void stop_TTS();
    void stop_Action();
    void switchWakeup(boolean enable);
    void start_Action(String action);
    void stop_Grammar();
    void start_Grammar();
    boolean isActioning();
    boolean isTTSing();
    boolean startLocalFunction(String function);
    void enterSavePowerMode();
}