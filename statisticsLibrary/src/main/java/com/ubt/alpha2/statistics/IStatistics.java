package com.ubt.alpha2.statistics;

import android.content.Context;

import java.util.HashMap;

/**
 * @author: liwushu
 * @description: 统计信息的相关接口
 * @created: 2017/7/19
 * @version: 1.0
 * @modify: liwushu
*/
public interface IStatistics {

    void init(Context context);
    // 统计发生次数
    void onEvent(Context context, String eventName);
    void onEvent(Context context, String eventName, String value);
    // 计算事件
    void onEvent(Context context, String eventName, HashMap<String, String> map);
    void onEvent(Context context, String eventName, HashMap<String, String> map, int value);
    void onResume(Context context);
    void onPause(Context context);
    void onPageStart(String pageName);
    void onPageEnd(String pageName);
    void initTraceException(boolean isTrace);
}
