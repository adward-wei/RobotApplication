package com.ubtechinc.alpha2ctrlapp.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;

import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha2ctrlapp.base.CrashHandler;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.umeng.analytics.MobclickAgent;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class InitService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.ubtechinc.alpha2ctrlapp.service.action.FOO";
    private static final String ACTION_BAZ = "com.ubtechinc.alpha2ctrlapp.service.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.ubtechinc.alpha2ctrlapp.service.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.ubtechinc.alpha2ctrlapp.service.extra.PARAM2";

    public InitService() {
        super("InitService");
    }
    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, InitService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, InitService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {


        CrashHandler.getInstance().init(getApplication());// 设置捕捉异常日志,发布时启用
        allowMulticast();
        //必须调用初始化
        MobclickAgent.setDebugMode(true);
        setupVersionName();
//        RobotCommuniteManager.getInstance(getApplication()).init();
//        //长连接管理放在异步初始化
//        RobotCommuniteManager.getInstance(getApplication()).connect();
    }
    /**
     * 设置版本名称
     * */
    private void setupVersionName() {
        try {
            // ---get the package info---
            PackageManager pm = getPackageManager();
            PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);
            Constants.VERSION_NAME = "V" + pi.versionName;
        } catch (Exception e) {
            Logger.i(e.getMessage());
        }
    }

    //处理Htc motog 中兴 收不到组播，需要将请求组播权限 CHANGE_WIFI_MULTICAST_STATE
    private WifiManager.MulticastLock multicastLock;
    /**
     * 处理Htc motog 中兴 收不到组播，需要将请求组播权限 CHANGE_WIFI_MULTICAST_STATE
     *
     * @author zengdengyi
     * @date 2015年9月29日 下午4:42:51
     */
    private void allowMulticast() {
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        multicastLock = wifiManager.createMulticastLock("com");
        multicastLock.acquire();
    }
    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
