package com.ubtechinc.zh_chat.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.ubtech.iflytekmix.R;
import com.ubtech.utilcode.utils.AppUtils;
import com.ubtechinc.alpha.model.StaticValue;
import com.ubtechinc.alpha.model.app.UbtAppButtonEventData;
import com.ubtechinc.alpha.model.app.UbtAppConfigData;
import com.ubtechinc.alpha.model.app.UbtAppData;
import com.ubtechinc.alpha.sdk.AlphaRobotApi;
import com.ubtechinc.alpha.sdk.listener.RobotInterruptListener;
import com.ubtechinc.alpha.sdk.listener.SavePowerChangeListener;
import com.ubtechinc.alpha.sdk.speech.SpeechRobotApi;
import com.ubtechinc.alpha.sdk.sys.SysApi;
import com.ubtechinc.zh_chat.play.PlayThread;
import com.ubtechinc.zh_chat.robot.UBTSemanticRobot;
import com.ubtechinc.zh_chat.ui.camera.CameraActivity;

import timber.log.Timber;

/**
 *   @author: Logic.peng
 *   @email : 2091843903@qq.com
 *   @time  : 2017/3/27
 *   @desc  :
 */
public class SpeechMainActivity extends Activity implements UBTSemanticRobot.UBTSemanticRobotStateObserver, UBTSemanticRobot.RobotRequestOpenCameraHandler {
    private static final String TAG = SpeechMainActivity.class.getSimpleName();
    public static final String ALPHA_CAMERA_EXIT_ACTION = "com.example.alphademo.cameraExit";
    private UBTSemanticRobot mRobot;
    private ExitBroadcast mExitBroadcast;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlphaRobotApi.get().initializ(this);
        setContentView(R.layout.activity_main);
        mRobot = new UBTSemanticRobot(this);
        mRobot.registerStateObserver(this);
        mRobot.setCameraHandler(this);
        mRobot.start();
        Bundle bundle = getIntent().getExtras();
        Timber.i("bundle " + bundle);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ALPHA_CAMERA_EXIT_ACTION);
        filter.addAction(SpeechMainActivity.this.getPackageName() + StaticValue.APP_CONFIG);
        filter.addAction(SpeechMainActivity.this.getPackageName() + StaticValue.APP_BUTTON_EVENT);
        filter.addAction(StaticValue.ACTION_REPLAY_BUSINESS);
        mExitBroadcast = new ExitBroadcast();
        registerReceiver(mExitBroadcast, filter);
        AlphaRobotApi.get().registerSavePowerListener(new SavePowerChangeListener() {
            @Override
            public void savePower(boolean enable) {
                mRobot.setSavePower(enable);
            }
        });

        AlphaRobotApi.get().registerRobotInterruptListener(new RobotInterruptListener() {
            @Override
            public void onInterrupt() {
                mRobot.reset();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mExitBroadcast != null) {
            unregisterReceiver(mExitBroadcast);
            mExitBroadcast = null;
        }
        AlphaRobotApi.get().destroy();
        mRobot.unregisterStateObserver(this);
        mRobot.quit();
        mRobot.setCameraHandler(null);
        System.exit(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SpeechRobotApi.get().setSpeechMode(1);
    }

    @Override
    public void notifyRobotStateChange(@UBTSemanticRobot.SemaRobotState int state) {
    }

    @Override
    public void requestOpenCamera() {
        if (AppUtils.isInstallApp(this, StaticValue.SMARTCAMERA_PACKAGE_NAME)) {
            Log.d(TAG,"...installed smartcamera...");
            changeSartCamera();
        } else {
            Log.d(TAG,"...not installed smartcamera ...");
            Intent intent = new Intent(this, CameraActivity.class);
            startActivity(intent);
        }
    }

    private void changeSartCamera() {
        Uri uri = Uri.parse(StaticValue.SCHEME +
                "://"+ StaticValue.HOST+"/"+
                StaticValue.SMARTCAMERA_PACKAGE_NAME + "?"+
                "clientIp=&srcApp="+getPackageName());
        SysApi.get().startApp(uri);
    }

    public class ExitBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            String action = intent.getAction();
            Timber.d("receiver : %s", action);
            if (ALPHA_CAMERA_EXIT_ACTION.equals(action)) {
                mRobot.reset();
            }else if((SpeechMainActivity.this.getPackageName() + StaticValue.APP_CONFIG).equals(action)) {
                Timber.d(StaticValue.APP_CONFIG);
                sendConfig2Server(intent, SpeechMainActivity.this.getPackageName(), "");
            } else if((SpeechMainActivity.this.getPackageName() + StaticValue.APP_BUTTON_EVENT).equals(action)) {
                Timber.d(StaticValue.APP_BUTTON_EVENT);
                sendButtonEvent2Server(intent, SpeechMainActivity.this.getPackageName(), "");
            }else if (StaticValue.ACTION_REPLAY_BUSINESS.equals(action)){
                handleReplayBusiness(intent);
            }
        }
    }

    private void handleReplayBusiness(Intent intent) {
        String url = intent.getStringExtra(StaticValue.ACTION_REPLAY_BUSINESS);
        if (TextUtils.isEmpty(url)) return;
        mRobot.reset();
        new PlayThread(SpeechMainActivity.this, new PlayThread.MusicCallBack() {
            @Override
            public void musicCallBack(int i) {
                switch (i) {
                    case 0://准备
                        mRobot.getHandler().stop_TTS();
                        mRobot.getHandler().stop_Grammar();
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            Timber.w(e.getMessage());
                        }
                        break;
                    case 1://完成
                        mRobot.getHandler().start_TTS(getApplication().getString(R.string.play_end),false);
                        break;
                    case -1:// 异常
                        mRobot.getHandler().start_TTS(getApplication().getString(R.string.play_fail),false);
                        break;
                    default:
                        break;
                }
            }
        }, url).start();
    }


    public boolean sendConfig2Server(Intent intent, String packageName,
                                     String code) {
        Bundle bundle = intent.getExtras();
        UbtAppData ubtAppData = (UbtAppData) bundle
                .getSerializable("appdata");
        byte[] data = ubtAppData != null ? ubtAppData.getDatas() : new byte[0];
        String lauange = new String(data);
        Log.i("appdata", "lauange= " + lauange);
        String[] json = {"config1", "config2"};

        UbtAppConfigData appConfig = new UbtAppConfigData();
        appConfig.setCmd(ubtAppData != null ? ubtAppData.getCmd() : 0);
        appConfig.setTags(json[0].getBytes());
        appConfig.setDatas(json[1].getBytes());
        appConfig.setPackageName(packageName);
        // Intent intent2 = new Intent(packageName
        // + ThirdPartyValues.APP_CONFIG_BACK);
        Intent intent2 = new Intent(StaticValue.APP_CONFIG_BACK);
        Bundle bundle2 = new Bundle();
        bundle2.putSerializable(StaticValue.APP_CONFIG, appConfig);
        intent2.putExtras(bundle2);
        this.sendBroadcast(intent2);
        return true;
    }


    public boolean sendButtonEvent2Server(Intent intent, String packageName,
                                          String code) {
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            UbtAppData ubtAppData = (UbtAppData) bundle
                    .getSerializable("appdata");
            byte[] data = ubtAppData != null ? ubtAppData.getDatas() : new byte[0];
            String lauange = new String(data);
            Log.i("appdata", "lauange= " + lauange);
        }
        String json = "button1";

        UbtAppButtonEventData appEvent = new UbtAppButtonEventData();
        appEvent.setDatas(json.getBytes());
        appEvent.setPackageName(packageName);
        // Intent intent2 = new Intent(packageName
        // + ThirdPartyValues.APP_BUTOON_EVENT_BACK);
        Intent intent2 = new Intent(
                StaticValue.APP_BUTOON_EVENT_BACK);
        Bundle bundle2 = new Bundle();
        bundle2.putSerializable("appbutton", appEvent);
        intent2.putExtras(bundle2);
        this.sendBroadcast(intent2);
        return true;
    }
}
