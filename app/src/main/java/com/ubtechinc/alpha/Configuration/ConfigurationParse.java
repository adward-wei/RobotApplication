package com.ubtechinc.alpha.Configuration;

import android.text.TextUtils;

import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha.robotinfo.RobotConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @desc : 配置文件解析
 * @author: wzt
 * @time : 2017/5/25
 * @modifier:
 * @modify_time:
 */

public class ConfigurationParse {
    private static final String TAG = "ConfigurationParse";

    private static final String KEY_ASR_LANGUAGE = "asr_Language";
    private static final String KEY_DEFAULT_APP = "default_App";
    private static final String KEY_ISBUSINESS = "isBusiness";
    private static final String KEY_ISOPENDEBUGLOG = "isOpenDebugLog";
    private static final String KEY_ISOPENINFOLOG = "isOpenInfoLog";
    private static final String KEY_WEB_SERVER = "web_Server";
    private static final String KEY_DEVELOP_SERVER = "develop_Server";
    private static final String KEY_ALICE_SERVER = "alice_Server";
    private static final String KEY_XMPP_SERVER = "xmpp_Server";
    private static final String KEY_WAKEUP_WORD = "wakeup_word";
    private static final String KEY_WAKEUP_THRESHOLD_MIC5 = "wakeup_threshold_mic5";

    private static final String DEFAULT_ASR_LANGUAGE = "zh_cn";
    private static final String DEFAULT_DEFAULT_APP = "com.ubtech.iflytekmix";
    private static final boolean DEFAULT_ISBUSINESS = false;
    private static final boolean DEFAULT_ISOPENDEBUGLOG = false;
    private static final boolean DEFAULT_ISOPENINFOLOG = true;
    private static final String DEFAULT_WEB_SERVER = "http://services.ubtrobot.com/ubx/";
    private static final String DEFAULT_DEVELOP_SERVER = "http://dev.ubtrobot.com/opencenter/app/accesscheckapp";
    private static final String DEFAULT_ALICE_SERVER = "http://10.10.1.54:8081/programd/talkServer?";
    private static final String DEFAULT_XMPP_SERVER = "services.ubtrobot.com";
    private static final String DEFAULT_WAKEUP_WORD = "CN_WAKEUP_NIHAO_ALPHA";
    private static final int DEFAULT_WAKEUP_THRESHOLD_MIC5  = 23;

    public boolean parseConfigJson(String info) {
        boolean isSuccess = true;

        RobotConfiguration configuration = RobotConfiguration.get();
        initConfigurationJsonEntrity(configuration);

        if(TextUtils.isEmpty(info)) {
            isSuccess = false;
        } else {
            JSONObject infoJson = null;
            LogUtils.i( "start="+System.currentTimeMillis());
            try {
                infoJson = new JSONObject(info);

                if(infoJson != null) {
                    String asrLanguage = DEFAULT_ASR_LANGUAGE;
                    try {
                        asrLanguage = infoJson.getString(KEY_ASR_LANGUAGE);

                        LogUtils.i(TAG, "asrLanguage=" + asrLanguage);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    configuration.asr_Language = asrLanguage;

                    String defaultApp = DEFAULT_DEFAULT_APP;
                    try {
                        defaultApp = infoJson.getString(KEY_DEFAULT_APP);
                        LogUtils.i(TAG, "defaultApp=" + defaultApp);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    configuration.default_App = defaultApp;

                    String webServer = DEFAULT_WEB_SERVER;
                    try {
                        webServer = infoJson.getString(KEY_WEB_SERVER);
                        LogUtils.i(TAG, "webServer=" + webServer);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // HttpPost.setWebServiceAdderss(webServer);
                    // to do
                    configuration.web_Server = webServer;

                    String developServer = DEFAULT_DEVELOP_SERVER;
                    try {
                        developServer = infoJson.getString(KEY_DEVELOP_SERVER);
                        LogUtils.i(TAG, "developServer=" + developServer);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // HttpPost.setDeveloperServerAddress(developServer);
                    // to do
                    configuration.develop_Server = developServer;

                    String aliceServer = DEFAULT_ALICE_SERVER;
                    try {
                        aliceServer = infoJson.getString(KEY_ALICE_SERVER);
                        LogUtils.i(TAG, "aliceServer=" + aliceServer);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // HttpPost4Alice.setWebAliceAdderss(aliceServer);
                    // to do
                    configuration.alice_Server = aliceServer;

                    String xmppServer = DEFAULT_XMPP_SERVER;
                    try {
                        xmppServer = infoJson.getString(KEY_XMPP_SERVER);
                        LogUtils.i(TAG, "xmppServer=" + xmppServer);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //LoginConfig.XMPP_HOST = xmppServer;
                    configuration.xmpp_Server = xmppServer;

                    boolean isBusiness = DEFAULT_ISBUSINESS;
                    try {
                        isBusiness = infoJson.getBoolean(KEY_ISBUSINESS);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    configuration.isBusiness = isBusiness;

                    boolean isOpenDebugLog = DEFAULT_ISOPENDEBUGLOG;
                    try {
                        isOpenDebugLog = infoJson.getBoolean(KEY_ISOPENDEBUGLOG);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

//                    NLog.setDebug(isOpenDebugLog, 5);

                    configuration.isOpenDebugLog = isOpenDebugLog;

                    boolean isOpenInfoLog = DEFAULT_ISOPENINFOLOG;
                    //isOpenInfoLog = infoJson.getBoolean(KEY_ISOPENINFOLOG);
                    try {
                        isOpenInfoLog = infoJson.getBoolean(KEY_ISOPENINFOLOG);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    if(isOpenInfoLog) {
//                        NLog.setDebug(true, 3);
//                    } else {
//                        NLog.setDebug(true, 5);
//                    }
                    configuration.isOpenInfoLog = isOpenInfoLog;

                    String wakeupWord = DEFAULT_WAKEUP_WORD;
                    try {
                        wakeupWord = infoJson.getString(KEY_WAKEUP_WORD);
                        LogUtils.i(TAG, "wakeupWord=" + wakeupWord);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // IflyteckEngine.setDefaultWakeupWord(wakeupWord);
                    // to do
                    configuration.wakeup_word = wakeupWord;


                    int wakeupThresholdMic5 = DEFAULT_WAKEUP_THRESHOLD_MIC5;
                    try {
                        wakeupThresholdMic5 = infoJson.getInt(KEY_WAKEUP_THRESHOLD_MIC5);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // IflyteckEngine.setmWakeupThresholdMic5(wakeupThresholdMic5);
                    // to do
                    configuration.wakeup_threshold_mic5 = wakeupThresholdMic5;
                } else {
                    isSuccess = false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            LogUtils.i( "end="+System.currentTimeMillis());
        }

        return isSuccess;
    }

    private void initConfigurationJsonEntrity(RobotConfiguration configuration) {
        configuration.asr_Language = DEFAULT_ASR_LANGUAGE;
        configuration.default_App = DEFAULT_DEFAULT_APP;
        configuration.isBusiness = DEFAULT_ISBUSINESS;
        configuration.isOpenDebugLog = DEFAULT_ISOPENDEBUGLOG;
        configuration.isOpenInfoLog = DEFAULT_ISOPENINFOLOG;
        configuration.web_Server = DEFAULT_WEB_SERVER;
        configuration.develop_Server = DEFAULT_DEVELOP_SERVER;
        configuration.alice_Server = DEFAULT_ALICE_SERVER;
        configuration.xmpp_Server = DEFAULT_XMPP_SERVER;
    }
}
