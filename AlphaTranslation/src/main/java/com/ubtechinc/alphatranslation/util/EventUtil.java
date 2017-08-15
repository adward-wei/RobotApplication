package com.ubtechinc.alphatranslation.util;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

/**
 * @author：tanghongyu
 * @date：3/16/2017 10:37 AM
 * @modifier：tanghongyu
 * @modify_date：3/16/2017 10:37 AM
 * [A brief description]
 * version
 */

public class EventUtil {

    public static void sendStatisticsEvent(Context context, String eventId, String... params) {

        Map<String, String> paramMap = new HashMap<String, String>();
        String key = "";
        String value= "";
        for (int i= 0; i<params.length ; i++) {
            if(i%2 == 0){
                key = params[i];
            } else {
                value = params[i];
                paramMap.put(key, value);
            }

        }
        MobclickAgent.onEvent(context,eventId, paramMap);

    }

    public static void postTranslationResult(Context context, String type, boolean isSuccess, String reason) {
        sendStatisticsEvent(context,StatisticsConstant.EVNET_ID_TRANSLATION, StatisticsConstant.PARAM_ID_TRANLATION_TYPE, type, StatisticsConstant.PARAM_ID_IS_TRANLATION_SUCCESS, String.valueOf(isSuccess), StatisticsConstant.PARAM_ID_FAIL_REASON, reason);
    }
    public static void postTranslationSuccessResult(Context context, String type) {
        postTranslationResult(context, type, true, "");
    }
    public static void postTranslationFailResult(Context context, String type, String reason) {
        postTranslationResult(context, type, false, reason);
    }
}
