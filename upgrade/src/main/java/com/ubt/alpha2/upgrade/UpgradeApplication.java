package com.ubt.alpha2.upgrade;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.ubt.alpha2.statistics.DefaultStatisticsSwitch;
import com.ubt.alpha2.statistics.StatisticsKind;
import com.ubt.alpha2.statistics.StatisticsWrapper;
import com.ubt.alpha2.upgrade.receiver.AppReplacedReceiver;
import com.ubt.alpha2.upgrade.receiver.PowerInfoReceiver;
import com.ubt.alpha2.upgrade.utils.ApkUtils;
import com.ubt.alpha2.upgrade.utils.LogUtils;
import com.ubt.alpha2.upgrade.utils.ServiceUtils;

import timber.log.Timber;

/**
 * @author: slive
 * @description: this class used to save global variables
 * @create: 2017/6/27
 * @email: slive.shu@ubtrobot.com
 * @modified: slive
 */
public class UpgradeApplication extends MultiDexApplication {

    private static Context context;
    private static UpgradeApplication _instance;
    public boolean isFirstChecked = true;


    @Override
    public void onCreate(){
        super.onCreate();
        MultiDex.install(this);
        context = this;
        _instance = this;
        registerPowerBroadcastReceive();
        registerSelfUpgrade();
        Timber.plant(new Timber.DebugTree());
        LogUtils.d("BuildConfig.Debug: "+BuildConfig.isDebug);
        if(!BuildConfig.isDebug)
            ServiceUtils.startService(context);
        initStatistics();
        ApkUtils.reflectReleaseProvider();
    }

    public static Context getContext(){
        return context;
    }

    public static UpgradeApplication getInstance(){
        return _instance;
    }

    private void registerPowerBroadcastReceive(){
        PowerInfoReceiver powerInfoReceiver = new PowerInfoReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        Intent intent = registerReceiver(powerInfoReceiver,intentFilter);
        LogUtils.d("intent: "+intent);
    }

    private void registerSelfUpgrade(){
        AppReplacedReceiver appReplacedReceiver = new AppReplacedReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_FULLY_REMOVED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        intentFilter.addDataScheme("package");
        registerReceiver(appReplacedReceiver,intentFilter);
    }

    private void initStatistics(){
        StatisticsWrapper statisticsWrapper = StatisticsWrapper.getInstance();
        statisticsWrapper.init(this);
        statisticsWrapper.addStatisitcs(StatisticsKind.STATISTICS_UMENG);
        statisticsWrapper.setStatiticsSwitch(new DefaultStatisticsSwitch());
    }
}
