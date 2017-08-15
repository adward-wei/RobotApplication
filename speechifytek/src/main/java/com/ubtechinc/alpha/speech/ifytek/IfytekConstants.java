package com.ubtechinc.alpha.speech.ifytek;

import android.os.Environment;

import com.iflytek.cloud.SpeechConstant;

/**
 * @desc : 科大讯飞配置
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/30
 * @modifier:
 * @modify_time:
 */
public class IfytekConstants {
    //TTS
    public static final String VOICENAME_NANA = "nannan";
    public static final String VOICENAME_XIAOYAN = "xiaoyan";
    public static final String VOICENAME_XIAOFENG = "xiaofeng";
    public static final String VOICENAME_CATHERINE = "catherine";
    public static final String VOICENAME_JOHN = "john";
    public static final String VOICENAME_YBXF1 = "ybxf1";
    public final static String DEFAULT_TTS_SPEED = "64";
    public final static String DEFAULT_TTS_PITCH = "50";
    public final static String DEFAULT_TTS_VOLUME = "80";
    public final static String DEFAULT_TTS_LANGUAGE = "zh_cn";

    public static final String KEY_GRAMMAR_ABNF_ID = "grammar_abnf_id";
    public static final String GRAMMAR_TYPE_ABNF = "abnf";
    public static final String GRAMMAR_TYPE_BNF = "bnf";


    public static final String mGrammarEngineType = SpeechConstant.TYPE_LOCAL;//语音识别本地还是云识别标志位

    public static final int ASR_INIT_PARAM = 1;
    public static final String GRAMMAR_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/msc/test5mic";// 本地语法构建路径


    public final static int WAKEUP_MAX_VALUE = 60;  // 唤醒门限最大值
    public final static int WAKEUP_MIN_VALUE = -20;  // 唤醒门限最小值




}
