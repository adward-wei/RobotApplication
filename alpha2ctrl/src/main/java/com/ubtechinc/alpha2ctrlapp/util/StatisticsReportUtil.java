package com.ubtechinc.alpha2ctrlapp.util;

import com.ubtech.utilcode.utils.Utils;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.umeng.analytics.MobclickAgent;

import java.util.Map;

/**
 * @ClassName StatisticsReportUtil
 * @date 6/6/2017
 * @author tanghongyu
 * @Description 统计上报工具类
 * @modifier
 * @modify_time
 */
public class StatisticsReportUtil {

    public static void  reportRegisterCount(Map<String, String> map) {
        MobclickAgent.onEvent(Utils.getContext(), Constants.YOUMENT_APP_REGISTER_TIMES, map);
    }
}
