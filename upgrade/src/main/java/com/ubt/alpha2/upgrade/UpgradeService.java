package com.ubt.alpha2.upgrade;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;

import com.ubt.alpha2.upgrade.bean.PowerInfo;
import com.ubt.alpha2.upgrade.bean.WifiStateInfo;
import com.ubt.alpha2.upgrade.protocol.Alpha2ProtocolPacket;
import com.ubt.alpha2.upgrade.utils.Constants;
import com.ubt.alpha2.upgrade.utils.LogUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author: slive
 * @description: upgrade service
 * @create: 2017/6/27
 * @email: slive.shu@ubtrobot.com
 * @modified: slive
 */
public class UpgradeService extends Service {
    //检测版本周期 60*60*1000 //暂定一小时
    private final long CHECK_CIRCLE = 60*60*1000;
    UpgradeManager upgradeManager;
    private boolean isUpgradeStart = false;
    private boolean isUpgradeRestart = false;
    private boolean mWifiConnectFirst = true;
    private Timer mTimer = new Timer();

    @WifiStateInfo.WifiState
    int wifiState = WifiStateInfo.IDLE;

    public UpgradeService() {
    }

    @Override
    public void onCreate(){
        super.onCreate();
        initUpgradeManager();
    }

    private void initUpgradeManager(){
        upgradeManager = UpgradeManager.getInstance();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        super.onStartCommand(intent,flags,startId);
        if(intent != null)
            isUpgradeRestart = intent.getBooleanExtra(Constants.SELF_REPLACE_RESTART,false);
        LogUtils.e("onStartCommand: "+"  isUpgradeRestart: "+isUpgradeRestart+"  isUpgradeStart: "+isUpgradeStart);
        if(!isUpgradeStart){
            isUpgradeStart = true;
            registerBroadcastReceive();
            startUpdate();
        }
        return START_STICKY;
    }

    public void registerBroadcastReceive() {
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(Constants.CHEST_ACTION);
        registerReceiver(mBroadCastReceiver, mIntentFilter);
    }

    private BroadcastReceiver mBroadCastReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            LogUtils.d("action: "+action);
            if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                //connected and disconnected
                wifiConnectState(intent);
            }else if(action.equals(Constants.CHEST_ACTION)){
                Bundle bundle = intent.getExtras();
                byte[] datas = bundle.getByteArray("value");
                Alpha2ProtocolPacket packet = new Alpha2ProtocolPacket();
                packet.setRawData(datas);
                switch (packet.getmCmd()) {
                    case Constants.CHEST_SEND_POWER:{// 电量 电量状态
                        byte[] param = packet.getmParam();
                        if(param.length > 2 ) {
                            if(param[2] == 1) {
                                PowerInfo.getInstance().setPowerState(PowerInfo.POWER_STATE_CHARGING);
                            } else {
                                PowerInfo.getInstance().setPowerState(PowerInfo.POWER_STATE_UNCHARGED);
                            }
                        }
                        PowerInfo.getInstance().setPowerValue(param[1]);
                        break;
                    }
                }
            }
        }
    };

    /**
     *  WIFI网络状态改变处理
     */
    protected void wifiConnectState(Intent intent) {
        Parcelable parcelableExtra = intent
                .getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if (null != parcelableExtra) {
            NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
            NetworkInfo.State state = networkInfo.getState();
            boolean isConnected = state == NetworkInfo.State.CONNECTED;
            if (isConnected) {
                if(wifiState != WifiStateInfo.CONNECTED){
                    wifiState = WifiStateInfo.CONNECTED;
                    LogUtils.d("isConnected yes");
                    if(mWifiConnectFirst){
                        mTimer.schedule(checkTask, 0, CHECK_CIRCLE); // 只执行一次
                        mWifiConnectFirst = false;
                    }
                }
            } else {
                wifiState = WifiStateInfo.IDLE;
                LogUtils.d("isConnected no");
            }
        }
    }

    //定时查询版本信息
    TimerTask checkTask = new TimerTask() {
        @Override
        public void run() {
            if(wifiState == WifiStateInfo.CONNECTED )
                startUpdate();
            mTimer.schedule(checkTask, 0, CHECK_CIRCLE); // 只执行一次
        }
    };

    private void startUpdate(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        NetworkInfo.State state = networkInfo.getState();
        boolean isConnected = state == NetworkInfo.State.CONNECTED;
        LogUtils.d("isConnected: "+isConnected);
        if(isConnected) {
            wifiState = WifiStateInfo.CONNECTED;
            LogUtils.d("isAllTaskCompleted: "+UpgradeManager.getInstance().isAllTaskCompleted());
            if(UpgradeManager.getInstance().isAllTaskCompleted()){
                UpgradeManager.getInstance().startTask();
            }
        }else{
            wifiState = WifiStateInfo.IDLE;
        }
    }

    @Override
    public void onDestroy(){
        isUpgradeStart = false;
    }


}
