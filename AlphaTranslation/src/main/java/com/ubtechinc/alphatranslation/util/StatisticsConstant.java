package com.ubtechinc.alphatranslation.util;

/**
 * @author：tanghongyu
 * @date：3/15/2017 6:42 PM
 * @modifier：tanghongyu
 * @modify_date：3/15/2017 6:42 PM
 * [友盟统计id]
 * version
 */

public class StatisticsConstant {

    //语法识别ID
    public static final String EVNET_ID_TRANSLATION = "translation_content";
    //app下载ID
    public static final String EVNET_ID_APP_DOWNLOAD = "app_download";
    public static final String EVNET_ID_MAIN_SERVICE_INSTALL = "main_service_install";
    public static final String EVNET_ID_PLAY_ACTION = "play_action";
    public static final String EVNET_ID_START_THIRD_APP = "start_third_app";
    /**********************************参数ID*************************/
    public static final String PARAM_ID_TRANLATION_TYPE = "translation_type";
    public static final String PARAM_ID_IS_TRANLATION_SUCCESS = "is_translation_success";
    public static final String PARAM_ID_FAIL_REASON = "fail_reason";
    public static final String PARAM_ID_ACTION_TYPE = "action_type";
    public static final String PARAM_ID_ACTION_NAME = "action_name";
    /************************动作类型************************/
    public static final String TARNSLATION_TYPE_CHINESE = "chinese_to_english";
    public static final String TARNSLATION_TYPE_ENGLISH = "english_to_chinese";
    public static final String ACTION_TYPE_STORY = "story";

    /**************************app相关********************************/
    public static final String PARAM_ID_APP_NAME = "app_name";
    public static final String PARAM_ID_APP_ID = "app_id";
    /*********************失败原因******************************/
    public static final String REASON_INSUFFICIENT_MEMORY_SPACE = "insufficient_memory_space";
    public static final String REASON_FILE_IS_NOT_EXIST = "file_not_exist";
    public static final String REASON_IO_EXCEPTION = "io_exception";
    public static final String REASON_NETWORK_NOT_AVAILABLE = "network_not_available";
    public static final String REASON_NETWORK_PROBLEM = "network_problem";

}