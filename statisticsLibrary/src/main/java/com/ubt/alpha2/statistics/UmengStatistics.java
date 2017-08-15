package com.ubt.alpha2.statistics;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

/**
 * @author: liwushu
 * @description: umeng 统计的实现类
 * @created: 2017/7/19
 * @version: 1.0
 * @modify: liwushu
*/
public class UmengStatistics extends AbstractStatistics{

    @Override
    public void init(Context context) {
        MobclickAgent.setDebugMode(false);
        MobclickAgent.enableEncrypt(true);
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setSessionContinueMillis(10);   // 黑屏超过10ms就算为一次新的启动
    }

    @Override
    public void onEvent(Context context, String eventName) {
        MobclickAgent.onEvent(context, eventName);
    }

    @Override
    public void onEvent(Context context, String eventName, String value) {
        MobclickAgent.onEvent(context, eventName, value);
    }

    @Override
    public void onEvent(Context context, String eventName, HashMap<String, String> map) {
        MobclickAgent.onEvent(context, eventName, map);
    }

    @Override
    public void onEvent(Context context, String eventName, HashMap<String, String> map, int value) {
        MobclickAgent.onEventValue(context, eventName, map, value);
    }

    @Override
    public void onResume(Context context) {
        MobclickAgent.onResume(context);
    }

    @Override
    public void onPause(Context context) {
        MobclickAgent.onPause(context);
    }

    @Override
    public void onPageStart(String pageName) {
        MobclickAgent.onPageStart(pageName);
    }

    @Override
    public void onPageEnd(String pageName) {
        MobclickAgent.onPageEnd(pageName);
    }

    @Override
    public void initTraceException(boolean isTrace){
        MobclickAgent.setCatchUncaughtExceptions(isTrace);
    }
}