
package com.ubtechinc.alpha.appmanager.old;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha.appmanager.AppConstants;
import com.ubtechinc.alpha.appmanager.AppInfo;
import com.ubtechinc.alpha.appmanager.install.AppSlience;
import com.ubtechinc.alpha.model.StaticValue;
import com.ubtechinc.alpha.provider.AppInfoVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * @desc : 旧的app管理器
 * @author: wzt
 * @time : 2017/5/23
 * @modifier:
 * @modify_time:
 */

public class AppManager4Old {

    private static final String TAG = "AppManager4Old";

    private static final String UBTIFLYTEK_PACKAGENAME = "com.ubtechinc.zh_chat";
    // 是否在更新主服务
    private boolean isUpdateService;



    /**
     * 默认启动的第三方App
     */

    private static Context mContext;
    private static List<AppInfo> mAppInfoList;

    private CheckThread mCheckThread;
//    private AppBroadcastReceiver appReceiver = new AppBroadcastReceiver();
    // private AppPackageBroadcast appPackageReceiver = new
    // AppPackageBroadcast();
    // private List<String> actionList = new ArrayList<String>();
//    private Alpha2SocketServiceImpl mSocketService;

    private static final boolean DEBUG = true;

    private static ArrayList<AppManagerListener> sListenerList;

    public static String  SENDROOMNUMBER="com.alpha2.videoSupervision";

    private static final String SYSTEM_UPDATE_PACKAGENAME = "com.ubtechinc.alpha2systemupdate";
    private static final String SYSTEM_UPDATE_TEMP_PACKAGENAME = "com.ubtechinc.alpha2systemupdate_temp";

    private static AppManager4Old sAppManager4Old;

    private AppManager4Old(Context context) {
        mContext = context;
        mAppInfoList = new ArrayList<AppInfo>();

        sListenerList = new ArrayList<AppManagerListener>();

        // 开启扫描线程
        this.mCheckThread = new CheckThread(mContext);
    }

    public static AppManager4Old get(Context context) {
        if(sAppManager4Old == null) {
            synchronized (AppManager4Old.class) {
                if(sAppManager4Old == null)
                    sAppManager4Old = new AppManager4Old(context);
            }
        }
        return sAppManager4Old;
    }

    public void initAppManager() {
/*
        IntentFilter filter = new IntentFilter();
        // 第三方app回复
        filter.addAction(DeveloperAppStaticValue.APP_CONFIG_BACK);
        filter.addAction(DeveloperAppStaticValue.APP_BUTOON_EVENT_BACK);
        mContext.registerReceiver(appReceiver, filter);
*/
        // IntentFilter filter2 = new IntentFilter();
        // // 安装，卸载
        // filter2.addAction(Intent.ACTION_PACKAGE_ADDED);
        // filter2.addAction(Intent.ACTION_PACKAGE_REMOVED);
        // filter2.addAction(Intent.ACTION_PACKAGE_REPLACED);
        // filter2.addDataScheme("package");
        // mContext.registerReceiver(appPackageReceiver, filter2);

        registerAlpha2Apps();
        // testInit();

    }

    /**
     * 开始APP MANAGER的处理
     */
    public void startProcess() {
        mCheckThread.start();
    }

    /**
     * 将使用机器人SDK应用注册进APPList
     *
     * @author zengdengyi
     * @date 2015年9月18日 上午9:21:44
     */
    private void registerAlpha2Apps() {
        // TODO Auto-generated method stub
        Alpha2Apps alpha2Apps = new Alpha2Apps(mContext);
        List<PackageInfo> mAppList = alpha2Apps.getAlpha2Apps();

        for (int i = 0; i < mAppList.size(); i++) {

            ApplicationInfo appInfo = null;
            try {
                appInfo = mContext.getPackageManager().getApplicationInfo(
                        mAppList.get(i).packageName,
                        PackageManager.GET_META_DATA);
            } catch (PackageManager.NameNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (appInfo == null)
                continue;
            Bundle meta = appInfo.metaData;
            if (meta == null)
                continue;
            String appKey = meta.getString("alpha2_appid");
            if (appKey != null) {// 有appid表示是机器人第三方应用

                AppInfo alphaApp = new AppInfo();
                alphaApp.setPackageName(mAppList.get(i).packageName);
                alphaApp.setAppKey(appKey);
                addToList(alphaApp);
//                // String config = meta.getString("alpha2_appconfig");
//                // if (config != null) {// 有config 表示有配置信息
//                // if (config.equals("config")) {
//                // String action = alphaApp.getPackageName()
//                // + DeveloperAppStaticValue.APP_CONFIG_BACK;
//                // actionList.add(action);
//                // filter.addAction(action);
//                // }
//                // }
//                // String buttonEvent = meta.getString("alpha2_buttonevent");
//                // if (buttonEvent != null) {// 有按钮事件信息
//                //
//                // if (buttonEvent.equals("buttonevent")) {
//                // String action = alphaApp.getPackageName()
//                // + DeveloperAppStaticValue.APP_BUTOON_EVENT_BACK;
//                // actionList.add(action);
//                // filter.addAction(action);
//                // }
//                //
//                // }

            }

        }
    }

//    private void testInit() {
//        // TODO: 初始化已经安装好的APP
//        AppInfo catApp = new AppInfo();
//        catApp.setPackageName("com.example.robottest");
//        catApp.setAppId("10086");
//        addToList(catApp);
//
//        // 智能语音
//        AppInfo chatApp = new AppInfo();
//        chatApp.setPackageName("com.iflytek.chat");
//        chatApp.setAppId("10010");
//        addToList(chatApp);
//
//        // 语法识别
//        AppInfo gramChatApp = new AppInfo();
//        gramChatApp.setPackageName("com.iflytekgrammar.chat");
//        gramChatApp.setAppId("10011");
//        addToList(gramChatApp);
//
//        // 在线翻译
//        AppInfo translateApp = new AppInfo();
//        translateApp.setPackageName("com.ubtechinc.alphatranslation");
//        translateApp.setAppId("10012");
//        addToList(translateApp);
//
//        // 音乐播放
//        AppInfo musicApp = new AppInfo();
//        musicApp.setPackageName("com.ubtechinc.alphamusicplayer");
//        musicApp.setAppId("10013");
//        addToList(musicApp);
//
//        // 健康管理
//        AppInfo logicsApp = new AppInfo();
//        logicsApp.setPackageName("com.tinylogics");
//        logicsApp.setAppId("10014");
//        addToList(logicsApp);
//
//        // 健康管理
//        AppInfo SearchApp = new AppInfo();
//        SearchApp.setPackageName("com.ubtechinc.alphasearch");
//        SearchApp.setAppId("10015");
//        addToList(SearchApp);
//    }

    /**
     * 判断需要添加的对象存不存在
     *
     * @param info
     * @return true - 存在；false - 不存在
     */
    synchronized static boolean isExist(AppInfo info) {
        boolean bExist = false;

        for (AppInfo i : mAppInfoList) {
            if (i.getAppKey().equalsIgnoreCase(info.getAppKey())) {
                bExist = true;
                break;
            }
        }

        return bExist;
    }

    /**
     * 添加对象到列表里
     *
     * @param info
     */
    synchronized static void addToList(AppInfo info) {
        if (isExist(info)) {
            return;
        }

        mAppInfoList.add(info);
    }

    /**
     * 移除对象
     *
     * @param info
     */
    synchronized static void removeAppInfo(AppInfo info) {
        AppInfo mRemove = null;
        if (mAppInfoList == null)
            return;
        for (AppInfo i : mAppInfoList) {
            if (i.getPackageName().equalsIgnoreCase(info.getPackageName())) {
                mRemove = i;
                break;
            }
        }
        if (mRemove != null) {
            mAppInfoList.remove(mRemove);
        }
    }

    /**
     * 查找APPINFO
     *
     * @author zengdengyi
     * @param packageName
     * @return
     * @date 2015年8月6日 下午3:53:53
     */
    synchronized AppInfo getAppInfo(String packageName) {
        for (AppInfo i : mAppInfoList) {
            if (i.getPackageName().equalsIgnoreCase(packageName)) {
                return i;
            }
        }

        return null;
    }

    /**
     * 启动第三方APP程序
     * @param info
     * @param clientIP
     * @param srcApp
     * @param angle
     */
    public void startApp(AppInfo info, String clientIP, String srcApp,
                         byte angle) {

        try {

            if (sListenerList != null) {
                for (AppManagerListener item : sListenerList) {
                    item.onAppStart();
                }
            }

            Intent intent = mContext.getPackageManager()
                    .getLaunchIntentForPackage(info.getPackageName());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if(UBTIFLYTEK_PACKAGENAME.equals(info.getPackageName())) {
                /** 如果是悠聊，将TTS动作开关情况发过去 **/
//                intent.putExtra("clientIP", AlphaMainSeviceImpl.isUbtIflytekTTSActionOpen + "");
            } else {
                intent.putExtra("clientIP", clientIP);
            }
            intent.putExtra("srcApp", srcApp);
            intent.putExtra("angle", angle);
            Log.v("zdy", "clientIP" + clientIP);

            mContext.startActivity(intent);

            // TODO: 保存当前APPID
            AppInfoVisitor provider = AppInfoVisitor.getInstance();
            provider.saveOrUpdate(info);
//            AppDao.insert(mContext, info.getPackageName(), info.getAppId());
//            EventUtil.postStartThirdAppResult(info);
            Log.d(TAG, "!!! clientIP=" + clientIP);
            if (clientIP.contains("xmpp")) { // for xmpp
                String[] strs = clientIP.split(" ");
                Log.d("", "!!! strs" + strs[1]);

//                XmppMsgParseThread.sendMessage(strs[1], strs[2],
//                        SocketCmdId.ALPHA_MSG_RSP_START_APP, null);
            } else {
/*                List<AbsSocketClientHandler> client = mSocketService
                        .getmClientManager().getmClientList();
                for (AbsSocketClientHandler alphaSocketClientHandler : client) {

                    alphaSocketClientHandler.sendMessage(
                            SocketCmdId.ALPHA_MSG_RSP_START_APP, null);
                }*/
            }
        } catch (Exception e) {
//            EventUtil.postStartThirdAppResult(info, false, e.getMessage());
            Log.v(TAG, "app not install");
        }
    }

    /**
     * 启动应用
     * @param packageName 应用包名
     * @param clientIP
     * @param srcApp
     * @param angle
     */
    public int startApp(String packageName, String clientIP, String srcApp,
                         byte angle) {
        int result = AppConstants.START_OK;

        AppInfoVisitor provider = AppInfoVisitor.getInstance();
        AppInfo appInfo = provider.getTopApp();
//        Alpha2App x = AppDao.getTopApp(mContext);
        if (appInfo != null) {
            // 已经是第一个TopApp
            //BRIAN SOLVE THE ALEXA VIDEO SUPERVISION BEGINNING
            if (appInfo.getPackageName().equals(packageName)) {
                if(appInfo.getPackageName().equals(SENDROOMNUMBER)) {
                    Intent intent = new Intent(SENDROOMNUMBER);
                    mContext.sendBroadcast(intent);
                }
                result = AppConstants.START_ALREADY;
            } else {// 非TopApp,先退出，在启动新的
                stopApp(appInfo.getPackageName());
                result = startApp(packageName, clientIP, srcApp, angle);
            }
            //BRIAN SOLVE THE ALEXA VIDEO SUPERVISION ENDING
        } else {
            AppInfo info = getAppInfo(packageName);
            if (info != null) {
                startApp(info, clientIP, srcApp, angle);
            } else {
                result = AppConstants.START_NOT_EXIST;
            }
        }

        return result;
    }

    public void startApp(String packageName, String clientIP) {
        AppInfoVisitor provider = AppInfoVisitor.getInstance();
        AppInfo appInfo = provider.getTopApp();
//        Alpha2App x = AppDao.getTopApp(mContext);
        if (appInfo != null) {
            // 已经是第一个TopApp
            if (appInfo.getPackageName().equals(packageName)) {
                return;
            } else {// 非TopApp,先退出，在启动新的
                stopApp(appInfo.getPackageName());
                startApp(packageName, clientIP);
                return;
            }
        } else {
            AppInfo info = getAppInfo(packageName);
            if (info != null) {
                startApp(info, clientIP, null, (byte) 0);
            }
        }
    }

    public void stopApp(String packageName) {
        AppInfoVisitor provider = AppInfoVisitor.getInstance();
        AppInfo appInfo = provider.getTopApp();
//        Alpha2App x = AppDao.getTopApp(mContext);
        // 存在第三方app
        if (appInfo != null) {
            // 要退出的app名称和顶部的app不一致
            if (!appInfo.getPackageName().equals(packageName)) {
                return;
            }
            AppInfo info = getAppInfo(packageName);
            if (info != null) {
                stopApp(info);
            }
        }
    }

    /**
     * debug 模式关闭正在运行的
     *
     * @author zengdengyi
     * @date 2015年9月15日 下午3:21:24
     */
    public void stopAPP2Debug() {
        AppInfoVisitor provider = AppInfoVisitor.getInstance();
        AppInfo appInfo = provider.getTopApp();
//        Alpha2App x = AppDao.getTopApp(mContext);
        // 存在第三方app
        if (appInfo != null) {
            AppInfo info = getAppInfo(appInfo.getPackageName());
            if (info != null) {
                stopApp(info);
            }
        }
    }

    /**
     * 停止第三方APP运行
     *
     * @param info
     */
    void stopApp(AppInfo info) {
        if (sListenerList != null) {
            for (AppManagerListener item : sListenerList) {
                item.onAppStop();
            }
        }

        Intent intent = new Intent(StaticValue.ACTION_UBT_APP_EXIT);
        Bundle bundle = new Bundle();
//        //	bundle.putSerializable("APP_INFO", info);
        bundle.putString("PACKAGENAME",info.getPackageName());
        intent.putExtras(bundle);
        mContext.sendBroadcast(intent);

        // TODO: 移除当前APPID
        AppInfoVisitor provider =  AppInfoVisitor.getInstance();
        provider.delete(info);
//        provider.deleteById(info.getAppId());
//        AppDao.delete(mContext, info.getAppId());
    }

    public void exitCheck() {
        if (mCheckThread != null) {
            mCheckThread.stopCheck();
        }
//        mContext.unregisterReceiver(appReceiver);
        // mContext.unregisterReceiver(appPackageReceiver);
    }

    public class CheckThread extends Thread {

        private ActivityManager mActivityManager;
        private int intProcessNum;
        private CheckThread mCheckThread;
        // private boolean isRun;
        private Context context;
        private boolean isLoop;
        private boolean isFirst;
        private int defSleep = 5000;

        public CheckThread(Context context) {
            this.context = context;
            init();
        }

        public void init() {
            mActivityManager = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
        }

        // 获取进程列表
        public boolean getProcess() {
            AppInfoVisitor provider = AppInfoVisitor.getInstance();
            AppInfo appInfo = provider.getTopApp();
//            Alpha2App x = AppDao.getTopApp(context);
            if (appInfo == null)
                return false;
            String processName = appInfo.getPackageName();
            boolean isRun = false;
            List<ActivityManager.RunningAppProcessInfo> apps = mActivityManager
                    .getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo app : apps) {
                if (processName.equals(app.processName)) {
                    isRun = true;
                    break;
                }
            }
            if (isRun == false) {// 没有第三方app在运行
                provider.deleteAll();
//                AppDao.clearData(context);
            }
            return isRun;
        }

        public void stopCheck() {
            isLoop = true;
            this.interrupt();
        }

        @Override
        public void run() {
            while (!isLoop) {
                try {
                    sleep(defSleep);
                    if (!getProcess()) {
                        // Log.i("TAG", "没有第三方app运行");
                        if (!isUpdateService) {//判断主服务是否更新
                            if (!isFirst) {
                                isFirst = true;
                                //启动悠聊

                                startApp(UBTIFLYTEK_PACKAGENAME, "");
                                uninstallSystemUpdateTemp();
                            }
                        }
                    }
                    // if (getProcess()) {
                    // if (!isRun) {
                    // Log.i("TAG", "应用启动运行");
                    // if (defApp != null && defApp.equals("")) {
                    // AppInfo info = getAppInfo(defApp);
                    // DbDao.insert(context, info.getPackageName(),
                    // info.getAppId());
                    // }
                    // isRun = true;
                    // } else {
                    // // Log.i("TAG", "应用一直运行");
                    // }
                    // } else {
                    // if (!isRun) {
                    //
                    // } else {
                    // Log.i("TAG", "不运行了");
                    // AppInfo info = getAppInfo(defApp);
                    // DbDao.delete(context, info.getAppId());
                    // defApp = "";
                    // isRun = false;
                    // // startApp();
                    // }
                    // }

                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 判断是否为使用SDK的第三方应用
     * @param packageName
     * @return
     */
    public static AppInfo checkApp(String packageName) {

        ApplicationInfo appInfo = null;
        try {
            if (mContext == null)
                return null;
            appInfo = mContext.getPackageManager().getApplicationInfo(
                    packageName, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (appInfo == null)
            return null;
        Bundle meta = appInfo.metaData;
        if (meta == null)
            return null;
        String appKey = meta.getString("alpha2_appid");
        if (appKey != null) {// 有appid表示是第三方应用

            AppInfo alphaApp = new AppInfo();
            alphaApp.setPackageName(packageName);
            alphaApp.setAppKey(appKey);
            // String config = meta.getString("alpha2_appconfig");
            // if (config != null) {// 有config 表示有配置信息
            // if (config.equals("config")) {
            // String action = alphaApp.getPackageName()
            // + DeveloperAppStaticValue.APP_CONFIG_BACK;
            // actionList.add(action);
            // filter.addAction(action);
            // }
            // }
            // String buttonEvent = meta.getString("alpha2_buttonevent");
            // if (buttonEvent != null) {// 有按钮事件信息
            //
            // if (buttonEvent.equals("buttonevent")) {
            // String action = alphaApp.getPackageName()
            // + DeveloperAppStaticValue.APP_BUTOON_EVENT_BACK;
            // actionList.add(action);
            // filter.addAction(action);
            // }
            //
            // }

            return alphaApp;
        } else {
            return null;
        }

    }

    /**
     * [the third party app config data receiver back]
     *
     * @author zengdengyi
     * @version 1.0
     *
     **/
/*    public class AppBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            Log.e(TAG, "third app info = " + action);

            //

            if (action.equals(DeveloperAppStaticValue.APP_CONFIG_BACK)) {

                Bundle bundle = intent.getExtras();
                DeveloperAppConfigData developer = (DeveloperAppConfigData) bundle
                        .getSerializable("appconfig");

                ReceiveAppConfigEvent event = new ReceiveAppConfigEvent();
                event.cmd = developer.getCmd();
                event.datas = developer.getDatas();
                event.packageName = developer.getPackageName();
                event.tags = developer.getTags();
                NotificationCenter.defaultCenter().publish(event);

            } else if (action
                    .equals(DeveloperAppStaticValue.APP_BUTOON_EVENT_BACK)) {

                Bundle bundle = intent.getExtras();

                DeveloperAppButtenEventData developer = (DeveloperAppButtenEventData) bundle
                        .getSerializable("appbutton");

                ReceiveAppButtonEvent event = new ReceiveAppButtonEvent();
                event.cmd = developer.getCmd();
                event.datas = developer.getDatas();
                event.packageName = developer.getPackageName();
                NotificationCenter.defaultCenter().publish(event);
            }

        }
    }
*/

    /**
     * [静态注册内部类广播 必须是static]
     *
     * @author zengdengyi
     * @version 1.0
     * @date 2015年9月28日 上午11:31:05
     *
     **/

    public static class AppPackageBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            // 安装卸载---start
            PackageManager manager = context.getPackageManager();
            if (action.equals(Intent.ACTION_PACKAGE_ADDED)) {
                String packageName = intent.getData().getSchemeSpecificPart();
                Log.e(TAG, "安装成功" + packageName);
                // 健康管理
                AppInfo addApp = checkApp(packageName);
                if (addApp == null)
                    return;
                addToList(addApp);
            } else if (action.equals(Intent.ACTION_PACKAGE_REMOVED)) {
                String packageName = intent.getData().getSchemeSpecificPart();
                Log.e(TAG, "卸载成功" + packageName);
                // // 主服务替换成功
                if (packageName.equals("com.ubtechinc.alpha2services")) {
                    Log.e(TAG, "主服务卸载成功");
                    // Intent intent2 = new Intent(
                    // "zdytsdasdfasdf");
                    // intent2.putExtra("isRestart", false);
                    // context.sendBroadcast(intent2);

                    return;
                }
                // 健康管理
                AppInfo removeApp = new AppInfo();// 不能使用这个，因为应用已经被卸载无法读取相应的信息checkApp(packageName);
                removeApp.setPackageName(packageName);

                removeAppInfo(removeApp);

            } else if (action.equals(Intent.ACTION_PACKAGE_REPLACED)) {
                String packageName = intent.getData().getSchemeSpecificPart();
                Log.e(TAG, "替换成功" + packageName);

                // 主服务替换成功
                if (packageName.equals("com.ubtechinc.alpha2services")) {
                    Log.e(TAG, "主服务更新完成重新启动");
					/*Intent intent2 = new Intent(StaticValue.ALPHA_SERVICE_UPDATE_RESULT);
					intent2.putExtra(StaticValue.SERVICE_UPDATE_RESULT, true);

					context.sendBroadcast(intent2);*/
                    Intent intent2 = new Intent(
                            "android.intent.action.RestartAlphaBroadcast");
                    intent2.putExtra("isRestart", true);

                    context.sendBroadcast(intent2);

                    return;
                }
            }
            // 安装卸载---end
        }

    }

/*
    public void setSocketService(Alpha2SocketServiceImpl mSocketService) {
        // TODO Auto-generated method stub
        this.mSocketService = mSocketService;
    }
*/

    public void addListener(AppManagerListener listener) {
        boolean isExist = false;
        if (sListenerList != null) {
            for (AppManagerListener item : sListenerList) {
                if (item == listener) {
                    isExist = true;
                    break;
                }
            }

            if (!isExist) {
                sListenerList.add(listener);
            }
        }
    }

    public void update(boolean ret) {
        // TODO Auto-generated method stub
        isUpdateService = ret;
    }

    /** 假如已经安装了正式的OTA升级apk，则卸载掉 **/
    private void uninstallSystemUpdateTemp() {

        if(isApkExist(SYSTEM_UPDATE_PACKAGENAME) && isApkExist(SYSTEM_UPDATE_TEMP_PACKAGENAME)) {
            new AppSlience().onUnInitall(SYSTEM_UPDATE_TEMP_PACKAGENAME,
                    new AppSlience.SlienceListener() {

                        @Override
                        public void onSlienceFail(int code, String msg) {
                            LogUtils.d(TAG, "uninstall SYSTEM_UPDATE_TEMP_PACKAGENAME fail");
                        }

                        @Override
                        public void onSlienceSuccess() {
                            LogUtils.d(TAG, "uninstall SYSTEM_UPDATE_TEMP_PACKAGENAME success");
                        }

                    });
        }

    }

    private static boolean isApkExist(String packageName) {
        boolean isExist = false;
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = null;
        try {
            pi = pm.getPackageInfo(packageName,
                    PackageManager.GET_CONFIGURATIONS);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if(pi != null) {
            if(pi.packageName.equals(packageName)) {
                isExist = true;
            }
        }

        return isExist;
    }

    public static boolean isSystemUpdateApkExist() {
        return isApkExist(SYSTEM_UPDATE_PACKAGENAME);
    }
}

