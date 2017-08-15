package com.ubtechinc.alpha2ctrlapp.base;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.tencent.bugly.crashreport.CrashReport;
import com.ubtech.utilcode.utils.Utils;
import com.ubtechinc.alpha2ctrlapp.BuildConfig;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.service.InitService;
import com.ubtechinc.alpha2ctrlapp.ui.activity.main.MainPageActivity;
import com.ubtechinc.blelibrary.BLEManager;

import java.util.ArrayList;


/**
 * @author tanghongyu
 * @ClassName Alpha2Application
 * @date 4/6/2017
 * @Description
 * @modifier
 * @modify_time
 */
public class Alpha2Application extends MultiDexApplication {


    //设备序列号
    private static String robotSerialNo = "";
    //当前登录的用户ID
    private volatile String userId = "";
    private String chatIconPath;//悠聊icon path
    private boolean isFromAction;
    private boolean isShopAction;




    // 接收到数据，回调到界面，更新UI
    private static ArrayList<BaseHandler> listeners = new ArrayList<>();
    private static Alpha2Application alpha2;
    //退出登录时，销毁所有页面用到
    private static ArrayList<Activity> activitys = new ArrayList<>();
    // 当前连接设备的Mac地址
    public static String currentAlpha2Mac = "";
    //处理Htc motog 中兴 收不到组播，需要将请求组播权限 CHANGE_WIFI_MULTICAST_STATE
    private WifiManager.MulticastLock multicastLock;
    public static int days = 0;
    private   ActivitySupport  currentContex;
    private String currentPlayFileName;//当前播放的动作名称


    /**
     * 服务 uuid
     */
    private static final String UUID_SERVICE = "0000180D-0000-1000-8000-00805f9b34fb";
    /**
     * 特征 uuid
     */
    private static final String UUID_CHARACTER = "00002A39-0000-1000-8000-00805f9b34fb";

    public static String getRobotSerialNo() {
        return robotSerialNo;
    }

    public static void setRobotSerialNo(String robotSerialNo) {
        Alpha2Application.robotSerialNo = robotSerialNo;
    }

    public void onCreate() {
        super.onCreate();
        //异步初始化信息放在服务中
        MultiDex.install(this);
        Utils.init(this);
        startService(new Intent(this, InitService.class));
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(0)         // (Optional) How many method line to show. Default 2
                .methodOffset(5)        // (Optional) Hides internal method calls up to offset. Default 5
                .tag("Alpha2App")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
        initBugly();

        BLEManager.install(this, UUID_SERVICE, UUID_CHARACTER);

//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated robotSerialNo LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);
        // Normal app init code...
        alpha2 = this;
    }


    public static Alpha2Application getAlpha2() {
        return alpha2;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getChatIconPath() {
        return chatIconPath;
    }

    public void setChatIconPath(String chatIconPath) {
        this.chatIconPath = chatIconPath;
    }

    public boolean isFromAction() {
        return isFromAction;
    }

    public void setFromAction(boolean fromAction) {
        isFromAction = fromAction;
    }

    public boolean isShopAction() {
        return isShopAction;
    }

    public void setShopAction(boolean shopAction) {
        isShopAction = shopAction;
    }


    /**
     * 保留当前activity 和主界面
     * @param olnyA 要保留的界面
     */
    public  void onlyActivity(Activity olnyA){

        for(int i=0;i<activitys.size();i++){
            Activity activity = activitys.get(i);
            if(!activity.getClass().getName().equals(olnyA.getClass().getName())&& !activity.getClass().getName().equals(MainPageActivity.class.getName())){
                activity.finish();
                activitys.remove(activity);
                i--;
            }
        }


    }

    private void initBugly() {
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplicationContext());
        strategy.setCrashHandleCallback(new CrashReport.CrashHandleCallback() {


            @Override
            public byte[] onCrashHandleStart2GetExtraDatas(int crashType, String errorType,
                                                           String errorMessage, String errorStack) {
                try {
                    Logger.e("errorMessage: %s", errorMessage);
                    Logger.e("errorStack: %s", errorStack);
                    return "Extra data.".getBytes("UTF-8");
                } catch (Exception e) {
                    return null;
                }
            }

        });

        CrashReport.initCrashReport(getApplicationContext(),"51abe4f187", BuildConfig.DEBUG,strategy);


    }

    public void stopService() {
        // 好友联系人服务
        Constants.hasStartService  = false;

    }

    // 遍历所有Activity并finish
    public void exit(boolean isneedKill) {
        for (Activity activity : activitys) {
            activity.finish();
        }
        stopService();


    }

    public  void removeActivity(){
        for(int i=0;i<activitys.size();i++){
            Activity activity = activitys.get(i);
            if(!activity.getClass().getName().equals(MainPageActivity.class.getName())){
                activity.finish();
                activitys.remove(activity);
                i--;
            }

        }

    }
    public  boolean removeActivity(String  activityName){
        for(int i=0;i<activitys.size();i++){
            Activity activity = activitys.get(i);
            if(activity.getClass().getName().equals(activityName)){
                activity.finish();
                activitys.remove(activity);
                i--;
            }

        }
        return  true;
    }


    public String getCurrentPlayFileName() {
        return currentPlayFileName;
    }

    public void setCurrentPlayFileName(String currentPlayFileName) {
        this.currentPlayFileName = currentPlayFileName;
    }

    public static Alpha2Application getInstance() {
        return alpha2;
    }

    public static ArrayList<BaseHandler> getHandlers() {
        return listeners;
    }

    public static void registerHandler(BaseHandler handler) {
        listeners.add(handler);
    }

    public static void unregisterHandler(BaseHandler handler) {
        listeners.remove(handler);
    }

    public static void registerActivity(ActivitySupport activity) {
        activitys.add(activity);
    }

    public static void unregisterActivity(ActivitySupport activity) {
        activitys.remove(activity);
    }

    public static ArrayList<Activity> getActicitys() {
        return activitys;
    }
}
