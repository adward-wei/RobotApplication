package com.ubt.alpha2.statistics;

import android.content.Context;

import java.util.HashMap;

/**
 * @author: liwushu
 * @description: 统计接口的空实现
 * @created: 2017/7/19
 * @version: 1.0
 * @modify: liwushu
*/
class AbstractStatistics implements IStatistics{

    @Override
    public void init(Context context) {

    }

    @Override
    public void onEvent(Context context, String eventName) {

    }

    @Override
    public void onEvent(Context context, String eventName, String value) {

    }

    @Override
    public void onEvent(Context context, String eventName, HashMap<String, String> map) {

    }

    @Override
    public void onEvent(Context context, String eventName, HashMap<String, String> map, int value) {

    }

    @Override
    public void onResume(Context context) {

    }

    @Override
    public void onPause(Context context) {

    }

    @Override
    public void onPageStart(String pageName) {

    }

    @Override
    public void onPageEnd(String pageName) {

    }

    @Override
    public void initTraceException(boolean isTrace) {

    }
}
