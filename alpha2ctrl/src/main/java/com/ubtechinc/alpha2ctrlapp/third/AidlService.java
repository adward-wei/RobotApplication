package com.ubtechinc.alpha2ctrlapp.third;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.text.TextUtils;

import com.nostra13.universalimageloader.utils.L;
import com.orhanobut.logger.Logger;
import com.ubt.alpha2sdk.aidlclient.AidlInterface;
import com.ubt.alpha2sdk.aidlclient.NotifyCallBack;
import com.ubt.alpha2sdk.aidlclient.RobotInfo;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtech.utilcode.utils.StringUtils;
import com.ubtechinc.alpha2ctrlapp.constants.IntentConstants;
import com.ubtechinc.alpha2ctrlapp.entity.ThirdToken;
import com.ubtechinc.alpha2ctrlapp.util.Tools;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author nixiaoyan
 * @ClassName AidlService
 * @date 2016/8/30
 * @Description 与第三方APP AIDL通信服务
 * @modifier tanghongyu
 * @modify_time 2016/8/30R
 */
public class AidlService extends Service {

    public static final String TAG = "Aidl";
    private NotifyCallBack listener;
    public static final String RECEIVE_DATA_FROM_SERVICE = "aild_action_receive_data_from_service";
    private Context mContext;
    public static final String REFRESH_CONNECTED_JID = "refresh_connected_jid";
    public static final String RECEIVE_ERROR_DATA = "aild_action_receive_error_data";
    //发送授权请求
    public static final String ACTION_SEND_TO_VERIFY = "action_send_to_verify";
    //接收授权结果
    public static final String RECEIVE_VERIFY_RESULT = "receive_verify_result";

    public static final String ACTION_SEND_DATA_TO_SERVICE = "send_data_to_service";

    public static final String AIDL_RECEIVE_ERROR_CODE = "aidl_receive_error_code";
    public static final String AIDL_KEYID_FROM_THIRD = "aidl_keyid_from_third";
    public static final String AIDL_SEND_DATA_FROM_THIRD = "aild_send_data_from_third";

    public static final String AIDL_DATA_APPID = "aild_appid_data_from_third";
    public static final String AIDL_DATA_APPKEY = "aild_appkey_data_from_third";
    public static final String AIDL_DATA_ROBOT = "aild_robot_data_from_third";
    String mThridAppPackageName;

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        logE("onBind() id = " + Thread.currentThread().getId());
        return mBinder;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        logE("onCreate()");
        registeReceiver();
        mContext = this.getApplicationContext();
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        logE("onStart()");
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        logE("onStart()");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // TODO Auto-generated method stub
        logE("onUnbind()");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        logE("onDestroy()");
        unregisterReceiver(reFreshBroadcastReceiver);
        //销毁回调资源 否则要内存泄露
        mCallBacks.kill();
        super.onDestroy();
    }

    private static void logE(String str) {
        Logger.d( "--------" + str + "--------");
    }

    //上面用CustomerClient 的原因是因为害怕客户端异常销毁时，服务器收不到消息 造成资源浪费等异常
    //同样的 我们在服务端通知客户端消息的时候 也害怕 服务端 会异常销毁 导致客户端收不到消息
    //好在谷歌早就为我们考虑到这种情况  提供了RemoteCallbackList 来完成对应的功能
    //避免我们再重复一遍上述的过程
    private RemoteCallbackList<NotifyCallBack> mCallBacks = new RemoteCallbackList<>();
    List<RobotInfo> robotInfos = new ArrayList<>();
    private final AidlInterface.Stub mBinder = new AidlInterface.Stub() {


        @Override
        public void registerCallback(NotifyCallBack notifyCallBack) throws RemoteException {
            listener = notifyCallBack;
            if(notifyCallBack != null) {
                mCallBacks.register(notifyCallBack);
                Logger.d("register listener = " + listener + " id = "+ Thread.currentThread().getId());
            }else {
                LogUtils.w("notifyCallBack = null" );

            }


        }

        @Override
        public void unRegisterCallback(NotifyCallBack notifyCallBack) throws RemoteException {
            if(notifyCallBack != null) {
                mCallBacks.unregister(notifyCallBack);
                Logger.d("unregister listener = " + listener);
            }else {
                Logger.d("notifyCallBack = null " );
            }

        }



        @Override
        public void getVerifyData(String appId, String appKey, String packageName) throws RemoteException {
            mThridAppPackageName = packageName;
            Logger.d(" appId = "+ appId+ " appKey = "+ appKey+ " packageName = "+ packageName + " id = "+ Thread.currentThread().getId());
            sendToVerify(appId, appKey);
        }

        @Override
        public void configRobotNetwork(RobotInfo robotInfo) throws RemoteException {
            sendToConfigNetwork(robotInfo);
        }

        @Override
        public void downloadRobotApp(String appId) throws RemoteException {
            sendToDownloadRobotApp(appId);
        }

        @Override
        public List<RobotInfo> getRobots() throws RemoteException {
            return robotInfos;
        }


    };
    private void sendToDownloadRobotApp(String appId) {

        String errorStr = null;
        if ( StringUtils.isEmpty(appId)) {
            errorStr = "请检查你的发送信息";
        } else {
            L.d(TAG, "Alpha2接收到数据" + " appId "+ appId);
            L.d(TAG, "Alpha2 发送广播");
            Intent sendIntent = new Intent();
            sendIntent.setAction(IntentConstants.ACTION_THIRD_SDK_DOWNLOAD_APP);
            sendIntent.putExtra(AidlService.AIDL_DATA_APPID, appId);
            sendBroadcast(sendIntent);
        }
        if (!StringUtils.isEmpty(errorStr)) {
            try {
                listener.onError(errorStr);
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    private void sendToConfigNetwork( RobotInfo robotInfo) {

        String errorStr = null;
        if ( robotInfo == null ) {
            errorStr = "请检查你的发送信息";
        } else {
            L.d(TAG, "Alpha2接收到数据" + " robotInfo "+ robotInfo.toString());
            L.d(TAG, "Alpha2 发送广播");
            Intent sendIntent = new Intent();
            sendIntent.setAction(IntentConstants.ACTION_THIRD_SDK_CONFIG_NETWORK);
            sendIntent.putExtra(AidlService.AIDL_DATA_ROBOT, (Serializable) robotInfo);
            sendBroadcast(sendIntent);
        }
        if (!StringUtils.isEmpty(errorStr)) {
            try {
                listener.onError(errorStr);
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void sendToVerify( String appId, String appKey) {
        L.d(TAG, "Alpha2接收到数据" + " id = "+ Thread.currentThread().getId());
        String errorStr = null;
        if ( TextUtils.isEmpty(appId) || TextUtils.isEmpty(appKey)) {
            errorStr = "请检查你的发送信息";
        } else {

            L.d(TAG, "Alpha2 发送广播");
            Intent sendIntent = new Intent();
            sendIntent.setAction(IntentConstants.ACTION_THIRD_SDK_VERIFY);
            sendIntent.putExtra(AidlService.AIDL_DATA_APPID, appId);
            sendIntent.putExtra(AidlService.AIDL_DATA_APPKEY, appKey);
            sendBroadcast(sendIntent);
        }
        if (!StringUtils.isEmpty(errorStr)) {
            try {
                listener.onError(errorStr);
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * @param
     * @return
     * @Description 发送XMPP数据
     */
//    public void sendData(String keyId, String data) {
//        String errorStr=null;
//        if(TextUtils.isEmpty(keyId)||TextUtils.isEmpty(data)){
//            errorStr="请检查你的发送信息";
//        }else{
//            Intent sendIntent  = new Intent();
//            sendIntent.setAction(AidlService.ACTION_SEND_DATA_TO_SERVICE);
//            sendIntent.putExtra(AidlService.AIDL_KEYID_FROM_THIRD,keyId);
//            sendIntent.putExtra(AidlService.AIDL_SEND_DATA_FROM_THIRD,data);
//            sendBroadcast(sendIntent);
//        }
//
//        if(errorStr!=null){
//            try {
//                listener.onError(errorStr);
//            }catch (RemoteException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//
//    }
    private void registeReceiver() {
        IntentFilter filter = new IntentFilter();
//    	filter.addAction(AidlService.RECEIVE_DATA_FROM_SERVICE);
//        filter.addAction(AidlService.RECEIVE_ERROR_DATA);
        filter.addAction(AidlService.RECEIVE_VERIFY_RESULT);
        registerReceiver(reFreshBroadcastReceiver, filter);
    }

    BroadcastReceiver reFreshBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Logger.d("收到广播" + action);
            switch (action) {
                case AidlService.RECEIVE_VERIFY_RESULT:

                    ThirdToken token = (ThirdToken) intent.getSerializableExtra(IntentConstants.ALPHA_THIRD_RECEIVE_TOKEN);
                    List<RobotInfo> robotInfoModels =  intent.getParcelableArrayListExtra(IntentConstants.ALPHA_THIRD_RECEIVE_DEVICES);
                    String userId = intent.getStringExtra(IntentConstants.ALPHA_THIRD_RECEIVE_USER_ID);
                    Logger.d("BroadcastReceiver receive, robotInfoModels size = " + robotInfoModels.size() + " content = " + robotInfoModels  + " id = "+ Thread.currentThread().getId());
                    if (listener != null) {
                        try {
                            Tools.runAppWithoutRepeat(getApplicationContext(), mThridAppPackageName);
                            robotInfos.clear();
                            robotInfos.addAll(robotInfoModels);
                            notifyCallBack(userId, token.getUserId(), token.getAccessToken());
                            Logger.d("BroadcastReceiver receive, send to third app" + " id = "+ Thread.currentThread().getId());

                        }  catch (Exception e) {
                            e.printStackTrace();
                            Logger.i(e.getMessage());
                        }

                    } else {
                        Logger.i(" listener = null !!");
                    }
                    break;
//                case AidlService.RECEIVE_DATA_FROM_SERVICE:
//                    NotificationIQ iq = (NotificationIQ) intent.getSerializableExtra(XmppConstant.ALPHA_THIRD_RECEIVE_TOKEN);
//                    if(listener!=null){
//                        try {
//
//                            listener.receiveDataFromService(iq.getId(), iq.getMessage());
//                        } catch (RemoteException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
//                    }
//                    break;
//
//                case AidlService.RECEIVE_ERROR_DATA:
//                    String errorCode = intent.getStringExtra(AidlService.AIDL_RECEIVE_ERROR_CODE);
//                    Logger.i("收到广播错误码"+errorCode);
//                    try {
//                        if(listener!=null){
//                            listener.onError(errorCode);
//                            Logger.i("收到广播错误码发送给第三方"+errorCode);
//                        }
//
//                    } catch (RemoteException e) {
////                    e.printStackTrace();
//                        Logger.i("出现异常");
//                    }
//                    break;
            }

        }

    };
    private void notifyCallBack( String alpha2UserId, String thirdUserId, String accessToken) {
        final int len = mCallBacks.beginBroadcast();
        Logger.d("RobotList = " + robotInfos + " RobotList size = " + robotInfos.size()  + " id = "+ Thread.currentThread().getId());
        for (int i = 0; i < len; i++) {
            try {
                // 通知回调
                mCallBacks.getBroadcastItem(i).receiveRobotList(robotInfos);
                mCallBacks.getBroadcastItem(i).receiveDataFromService(null, alpha2UserId);
                mCallBacks.getBroadcastItem(i).receiveVerifyResult(thirdUserId, accessToken);
            }catch (RemoteException e) {
                Logger.i(e.toString());
                e.printStackTrace();
            }

        }
        mCallBacks.finishBroadcast();
    }


}