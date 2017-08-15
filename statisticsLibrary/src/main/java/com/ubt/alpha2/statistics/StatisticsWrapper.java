package com.ubt.alpha2.statistics;

import android.content.Context;
import android.provider.Settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author: slive
 * @description: 公开类给外部调用
 * @create: 2017/7/19
 * @email: slive.shu@ubtrobot.com
 * @modified: slive
 */

public class StatisticsWrapper implements IStatistics{

    private static final int DEFAULT_STATISTICS_NUMBER = 3;
    private static final String STATISTICS_MODULE_NOT_INIT = "Statistics module not init";
    private List<IStatistics> mReportImpl = new ArrayList<>(DEFAULT_STATISTICS_NUMBER);
    private List<StatisticsKind> mStatisticsKind = new ArrayList<>(DEFAULT_STATISTICS_NUMBER);
    private IStatiticsSwitch mStatiticsSwitch = new DefaultStatisticsSwitch();
    private Context contextApp;

    private static class StatisticsWrapperProduce {
        private static StatisticsWrapper instance = new StatisticsWrapper();
    }

    public static StatisticsWrapper getInstance(){
        return StatisticsWrapperProduce.instance;
    }

    /**
     * 外部接口，控制使用的统计类型.相应的统计对象的初始化。
     * @param statisticsKind
     */
    public void addStatisitcs(StatisticsKind statisticsKind) {
        if(!mStatisticsKind.contains(statisticsKind)) {
            mStatisticsKind.add(statisticsKind);
            // 生成出统计对象
            IStatistics iStatistics = StatisticsFactory.produce(statisticsKind);
            if(iStatistics == null){
                return;
            }
            if (contextApp == null) {
                throw new NullPointerException(STATISTICS_MODULE_NOT_INIT);
            }
            iStatistics.init(contextApp);   // 统计初始化
            mReportImpl.add(iStatistics);
        }
    }

    /**
     * 是否打开统计的总开关，已默认打开。
     * @param statiticsSwitch
     */
    public void setStatiticsSwitch(IStatiticsSwitch statiticsSwitch) {
            mStatiticsSwitch = statiticsSwitch;
    }

    @Override
    public void init(Context context) {
        contextApp = context.getApplicationContext();
    }

    @Override
    public void onEvent(Context context, String eventName) {
        if(context == null) {
            context = contextApp;
        }
        if(mStatiticsSwitch.isOpenStatistics()) {
            for (int i = 0; i < mReportImpl.size(); i++) {
                mReportImpl.get(i).onEvent(context, eventName);
            }
        }
    }

    @Override
    public void onEvent(Context context, String eventName, String value) {
        if (context == null) {
            context = contextApp;
        }
        if (mStatiticsSwitch.isOpenStatistics()) {
            for (int i = 0; i < mReportImpl.size(); i++) {
                mReportImpl.get(i).onEvent(context, eventName, value);
            }
        }
    }

    @Override
    public void onEvent(Context context, String eventName, HashMap<String, String> map) {
        System.out.println("onEvent_context: "+context);
        if(context == null) {
            context = contextApp;
        }
        System.out.println("mStatiticsSwitch.isOpenStatistics(): "+mStatiticsSwitch.isOpenStatistics());
        if(mStatiticsSwitch.isOpenStatistics()) {
            System.out.println("mReportImpl.size(): "+mReportImpl.size());
            for (int i = 0; i < mReportImpl.size(); i++) {
                mReportImpl.get(i).onEvent(context, eventName, map);
            }
        }
    }

    @Override
    public void onEvent(Context context, String eventName, HashMap<String, String> map, int value) {
        if(context == null) {
            context = contextApp;
        }
        if(mStatiticsSwitch.isOpenStatistics()) {
            for (int i = 0; i < mReportImpl.size(); i++) {
                mReportImpl.get(i).onEvent(context, eventName, map, value);
            }
        }
    }

    @Override
    public void onResume(Context context) {
        if(context == null) {
            context = contextApp;
        }
        if(mStatiticsSwitch.isOpenStatistics()) {
            for (int i = 0; i < mReportImpl.size(); i++) {
                mReportImpl.get(i).onResume(context);
            }
        }
    }

    @Override
    public void onPause(Context context) {
        if(context == null) {
            context = contextApp;
        }
        if(mStatiticsSwitch.isOpenStatistics()) {
            for (int i = 0; i < mReportImpl.size(); i++) {
                mReportImpl.get(i).onPause(context);
            }
        }
    }

    @Override
    public void onPageStart(String pageName) {
        if(mStatiticsSwitch.isOpenStatistics()) {
            for (int i = 0; i < mReportImpl.size(); i++) {
                mReportImpl.get(i).onPageStart(pageName);
            }
        }
    }

    @Override
    public void onPageEnd(String pageName) {
        if(mStatiticsSwitch.isOpenStatistics()) {
            for (int i = 0; i < mReportImpl.size(); i++) {
                mReportImpl.get(i).onPageEnd(pageName);
            }
        }
    }

    @Override
    public void initTraceException(boolean isTrace) {
        if(mStatiticsSwitch.isOpenStatistics()) {
            for (int i = 0; i < mReportImpl.size(); i++) {
                mReportImpl.get(i).initTraceException(isTrace);
            }
        }
    }

}