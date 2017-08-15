package com.ubtechinc.alpha.appmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;

import com.ubtech.utilcode.utils.LogUtils;
import com.ubtech.utilcode.utils.notification.NotificationCenter;
import com.ubtech.utilcode.utils.thread.ThreadPool;
import com.ubtechinc.alpha.app.AlphaApplication;
import com.ubtechinc.alpha.event.AppDiedEvent;
import com.ubtechinc.alpha.event.ReceiveAppButtonEvent;
import com.ubtechinc.alpha.event.ReceiveAppConfigEvent;
import com.ubtechinc.alpha.model.StaticValue;
import com.ubtechinc.alpha.model.app.UbtAppButtonEventData;
import com.ubtechinc.alpha.model.app.UbtAppConfigData;
import com.ubtechinc.alpha.provider.AppInfoVisitor;
import com.ubtechinc.alpha.utils.PackageUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2017/6/2.
 */

public class AppManager {
    private static AppManager sInstance;
    private ConcurrentHashMap<String,AppInfo> appInfoMap = new ConcurrentHashMap();
    private static final String CHAT_PKG = StaticValue.CHAT_PACKAGE_NAME;
    private static final String TRANSFER_PKG = StaticValue.TRANSLATION_PACKAGE_NAME;

    private static final String FIRST_APP_DEFAULT = CHAT_PKG;
    private static final String TAG = "AppManager";
    //App生命周期变化过程： starting ---> running --> stopping --->stoped

    private ArrayList<String> startingAppList = new ArrayList<String>();
    private ArrayList<String> runingAppList = new ArrayList<String>(); //正在运行的应用
    private ArrayList<String> stopingAppList = new ArrayList<String>();
    private volatile boolean shutdown;

    public static AppManager getInstance() {
        if (sInstance == null) {
            synchronized (AppManager.class) {
                if (sInstance == null) {
                    sInstance = new AppManager();
                }
            }
        }
        return sInstance;
    }

    private AppManager() {
        init();
    }

    public void startApp(String pkgName,String ip,String srcApp,byte angel) {
        LogUtils.d(TAG,"startApp ----pkgName :"+pkgName);
        boolean needContinue = handleCurrRunningApp(pkgName);
        if (!needContinue) {
            return;
        }
        Intent intent = AlphaApplication.getContext().getPackageManager()
                .getLaunchIntentForPackage(pkgName);

        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("clientIP", ip);
            intent.putExtra("srcApp", srcApp);
            intent.putExtra("angle", angel);
            AlphaApplication.getContext().startActivity(intent);

            synchronized (this) {
                startingAppList.add(pkgName);
            }
        }
    }

    public void startApp(Uri uri){
        LogUtils.d(TAG,"startApp ----uri :"+uri.toString());
        String scheme = uri.getScheme();
        String host = uri.getHost();
        String path = uri.getPath().replace("/", "");
        if (!StaticValue.SCHEME.equals(scheme) || !StaticValue.HOST.equals(host))
            return;
        boolean needContinue = handleCurrRunningApp(path);
        LogUtils.d(TAG,"startApp ----uri :"+path);
        if (!needContinue) {
            return;
        }

        Intent intent = AlphaApplication.getContext().getPackageManager().getLaunchIntentForPackage(path);
        if (intent == null) return;
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        LogUtils.d(TAG,"startApp ----uri :"+path);
        Iterator<String> iter = uri.getQueryParameterNames().iterator();
        String key;
        while (iter.hasNext()){
            key = iter.next();
            intent.putExtra(key, uri.getQueryParameter(key));
        }
        AlphaApplication.getContext().startActivity(intent);
        synchronized (this) {
            startingAppList.add(path);
        }
    }

    /** pendingStartPkg: 即将启动的应用
     * return:
     * */
    private boolean handleCurrRunningApp(String pendingStartPkg) {
        if (runingAppList.size() > 0) {
            String topPkg = runingAppList.get(runingAppList.size() -1);
            if (topPkg.equals(pendingStartPkg)) {
                return false; //不需要再启动
            } else {
                //悠聊与中英文互译不能共存

                if (topPkg.equals(TRANSFER_PKG) && pendingStartPkg.equals(CHAT_PKG)
                        || topPkg.equals(CHAT_PKG) && pendingStartPkg.equals(TRANSFER_PKG)) {
                    stopApp(topPkg);
                }
            }
        }
        return true;
    }

    public AppInfo getTopApp() {
        if (runingAppList.size() > 0) {
            String topPkg = runingAppList.get(runingAppList.size() - 1);
            return getAppInfoByPkgName(topPkg);
        }
        return null;
    }

    public void startDefaultApp() {
        try {
            startApp(FIRST_APP_DEFAULT,"","",(byte)0);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private AppInfo getAppInfoByPkgName(String pkgName) {
        return appInfoMap.get(pkgName);
    }


    /**异步获取所有app信息*/
    private void init() {
        loadFromDb();
        if (appInfoMap.size() > 0) {
            loadAsync();
        } else {
            loadSync();
        }
        registerReceiver();

    }

    private void registerReceiver() {
        AppPackageBroadcast appReceiver = new AppPackageBroadcast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        intentFilter.addDataScheme("package");
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction(StaticValue.APP_CONFIG_BACK);
        intentFilter2.addAction(StaticValue.APP_BUTOON_EVENT_BACK);
        AlphaApplication.getContext().registerReceiver(appReceiver,intentFilter);
        AlphaApplication.getContext().registerReceiver(appReceiver,intentFilter2);

    }

    private void loadFromDb() {
        List<AppInfo> list = AppInfoVisitor.getInstance().getAllData();
        LogUtils.d(TAG, "loadFromDb = " + list.size());
        if (list != null && list.size() > 0) {
            for(AppInfo appInfo: list) {
                LogUtils.d(TAG, "loadFromDb appInfo = " + appInfo);
                appInfoMap.put(appInfo.getPackageName(),appInfo);
            }
        }
    }

    private void loadSync() {
        Map<String,AppInfo> appMap = PackageUtils.getInstalledAppInfo(AlphaApplication.getContext());
        appInfoMap.putAll(appMap);
        for(String packageName : appMap.keySet()) {
            LogUtils.d(TAG, "loadSync packageName =  " + packageName);
        }

        saveOrUpdateAllApp();
    }

    private void saveOrUpdateAllApp() {
        final List<AppInfo> appList = new ArrayList<>();
        Set<Map.Entry<String,AppInfo>> appSet = appInfoMap.entrySet();
        for(Map.Entry<String,AppInfo> entry : appSet) {
            appList.add(entry.getValue());
        }
        LogUtils.d(TAG, "saveOrUpdateAllApp =  " + appList.size());
        ThreadPool.getInstance().submit(new ThreadPool.Job<Object>() {
            @Override
            public Object run(ThreadPool.JobContext jobContext) {
                AppInfoVisitor.getInstance().deleteAll();
                AppInfoVisitor.getInstance().saveOrUpdateAll(appList);
                return null;
            }
        });
    }

    private void loadAsync() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                ThreadPool.getInstance().submit(new ThreadPool.Job<Object>() {
                    @Override
                    public Object run(ThreadPool.JobContext jobContext) {
                        Map<String,AppInfo> appMap = PackageUtils.getInstalledAppInfo(AlphaApplication.getContext());
                        appInfoMap.putAll(appMap);
                        for(String packageName : appMap.keySet()) {
                            LogUtils.d(TAG, "loadAsync packageName =  " + packageName);
                        }
                        saveOrUpdateAllApp();
                        return null;
                    }
                });
            }
        },2000);
    }


    private void addAppInfo(final AppInfo addApp) {
        if (addApp != null) {
            appInfoMap.put(addApp.getPackageName(),addApp);
            //更新数据库
            ThreadPool.getInstance().submit(new ThreadPool.Job<Object>() {
                @Override
                public Object run(ThreadPool.JobContext jobContext) {
                    AppInfoVisitor.getInstance().saveOrUpdate(addApp);
                    return null;
                }
            });

        }
    }

    private void removeAppInfo(String packageName) {
        final AppInfo appInfo = appInfoMap.remove(packageName);
        ThreadPool.getInstance().submit(new ThreadPool.Job<Object>() {
            @Override
            public Object run(ThreadPool.JobContext jobContext) {
                AppInfoVisitor.getInstance().delete(appInfo);
                return null;
            }
        });
    }

    /*通过调用ActivityManagerNative.forceStopPackage()来杀进程，需要system权限，杀的很彻底*/
    public void stopApp(String packageName) {
        LogUtils.d(TAG,"stopApp--pkgName :"+packageName);
        try {
            Object objIActMag, objActMagNative;
            Class clzIActMag = Class.forName("android.app.IActivityManager");
            Class clzActMagNative = Class.forName("android.app.ActivityManagerNative");
            Method mtdActMagNative$getDefault = clzActMagNative.getDeclaredMethod("getDefault");
            // IActivityManager iActMag = ActivityManagerNative.getDefault();
            objIActMag = mtdActMagNative$getDefault.invoke(clzActMagNative);
            // Configuration config = iActMag.getConfiguration();
            Method stopForceMethod = clzIActMag.getDeclaredMethod("forceStopPackage",String.class,int.class);
            stopForceMethod.invoke(objIActMag,packageName,0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        stopingAppList.add(packageName);
    }

    public synchronized void lifeRegister(final String pkgName, final IBinder lifeBinder) {
        LogUtils.d(TAG,"lifeRegister-"+" 应用： "+pkgName+" 绑定主服务成功");
        if (pkgName.equals(AlphaApplication.getContext().getPackageName())) {
            return;
        }
        if(lifeBinder != null) {

            try {
                lifeBinder.linkToDeath(new IBinder.DeathRecipient() {
                    @Override
                    public void binderDied() {
                        LogUtils.d(TAG,"应用 ("+pkgName+") process died");
                        lifeBinder.unlinkToDeath(this,0);
                        handleAppDied(pkgName);
                    }
                }, Binder.FLAG_ONEWAY);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        synchronized (this) {
            startingAppList.remove(pkgName);
            runingAppList.add(pkgName);
        }
    }

    private void handleAppDied(String pkgName) {
        synchronized (this) {
            runingAppList.remove(pkgName);
            if (stopingAppList.contains(pkgName)) {// 由主服务主动杀掉该app
                stopingAppList.remove(pkgName);
            } else { //应用意外退出
                if (pkgName.equals(FIRST_APP_DEFAULT) && !shutdown) {
                    startDefaultApp();
                }
            }
        }
        notifyAppDied(pkgName);
    }

    //如果有其他模块关注app的死亡情况，可以监听此event
    private void notifyAppDied(String pkgName) {
        AppDiedEvent event = new AppDiedEvent();
        event.pkgName = pkgName;
        NotificationCenter.defaultCenter().publish(event);

    }

    public void shutdown(){
        shutdown = true;
        stopAllApp();
    }

    private void stopAllApp() {
        int it = 0;
        while (it < runingAppList.size()){
            stopApp(runingAppList.get(it));
            it++;
        }
        stopingAppList.clear();
        startingAppList.clear();
        runingAppList.clear();
    }

    public  class AppPackageBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            LogUtils.d(TAG,"Receive broadcast---action : "+action);
            // 安装卸载---start
            if (action.equals(Intent.ACTION_PACKAGE_ADDED)) {
                String packageName = intent.getData().getSchemeSpecificPart();
                Log.e(TAG, "安装成功" + packageName);
                AppInfo addApp = PackageUtils.getAppInfoByPkgName(AlphaApplication.getContext(),packageName);
                if (addApp == null) {
                    return;
                } else {
                    addAppInfo(addApp);
                }
            } else if (action.equals(Intent.ACTION_PACKAGE_REMOVED)) {
                String packageName = intent.getData().getSchemeSpecificPart();
                Log.e(TAG, "卸载成功" + packageName);
                removeAppInfo(packageName);

            } else if (action.equals(Intent.ACTION_PACKAGE_REPLACED)) {
                String packageName = intent.getData().getSchemeSpecificPart();
                Log.e(TAG, "替换成功" + packageName);
                AppInfo addApp = PackageUtils.getAppInfoByPkgName(AlphaApplication.getContext(),packageName);
                if (addApp == null) {
                    return;
                } else {
                    addAppInfo(addApp);
                }
            }else if (action.equals(StaticValue.APP_CONFIG_BACK)) {

                Bundle bundle = intent.getExtras();
                UbtAppConfigData developer = (UbtAppConfigData) bundle
                        .getSerializable(StaticValue.APP_CONFIG);

                ReceiveAppConfigEvent event = new ReceiveAppConfigEvent();
                event.cmd = developer.getCmd();
                event.datas = developer.getDatas();
                event.packageName = developer.getPackageName();
                event.tags = developer.getTags();
                NotificationCenter.defaultCenter().publish(event);

            } else if (action.equals(StaticValue.APP_BUTOON_EVENT_BACK)) {

                Bundle bundle = intent.getExtras();

                UbtAppButtonEventData developer = (UbtAppButtonEventData) bundle
                        .getSerializable("appbutton");

                ReceiveAppButtonEvent event = new ReceiveAppButtonEvent();
                event.cmd = developer.getCmd();
                event.datas = developer.getDatas();
                event.packageName = developer.getPackageName();
                NotificationCenter.defaultCenter().publish(event);
            }
        }


    }
}
