package com.ubtechinc.alpha2ctrlapp.ui.activity.robot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.SPUtils;
import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.BaseHandler;
import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.constants.NetWorkConstant;
import com.ubtechinc.alpha2ctrlapp.data.robot.IRobotInitDataSource;
import com.ubtechinc.alpha2ctrlapp.data.robot.RobotInitRepository;
import com.ubtechinc.alpha2ctrlapp.events.RobotControlEvent;
import com.ubtechinc.alpha2ctrlapp.events.RobotRefreshEvent;
import com.ubtechinc.alpha2ctrlapp.service.RobotManagerService;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseContactActivity;
import com.ubtechinc.alpha2ctrlapp.widget.RoundImageView;
import com.ubtechinc.nets.http.ThrowableWrapper;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by liuliaopu on 2016/11/17.
 *
 * 开启监控界面
 */

public class SurveillanceStartActivity extends BaseContactActivity {
    private static final String TAG = "SurveillanceStartActivity";

    /*返回控件*/
    private View mBackView;
    /*介绍控件*/
    private View mIntroduceView;
    private View mRobotIconContainer;
    /*机器人头像*/
    private RoundImageView mRobotIconView;
    /*离线显示信息*/
    private View mRobotOfflineView;
    /*在线显示信息*/
    private View mRobotOnlineView;
    /*显示机器人电量*/
    private TextView mPowerValueView;
    /*启动监控界面按钮容器*/
    private View mSurveillanceContainer;
    /*启动监控界面按钮*/
    private TextView mLaunchSurveillanceBtn;
    /*第一次启动提示控件*/
    private View mFirstUseHintView;

    /*添加没有机器人的状态*/
    private static final int ROBOT_STATE_NO_ROBOT = -1;

    /*电量查询计时器*/
    private Timer mPowerRequestTimer;
    /*是否查询到电量*/
    private boolean mIsPowerObtained = false;

    /*网络断开连接显示控件*/
    private View mNetworkDisconnectView;

    /**没有获取到电量时，短间隔尝试获取电量次数*/
    private int mTryQueryPowerCount = 5;

    /**是否第一次使用视频监控*/
    private boolean mFirstUse;

    private static final int EVENT_NETWORK_UNAVAILABLE = 1000;
    private static final int EVENT_NETWORK_AVAILABLE = 1001;

    private BaseHandler mHandler = new BaseHandler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case NetWorkConstant.RESPONSE_NOT_BIND: {

                    Logger.i( "没有设备");
                    updateRobotStatus();
                    break;
                }
                case NetWorkConstant.REFRESH_AUTHOURIZE: {//刷新当前设备的信息
                    updateRobotStatus();
                    break;
                }

                case NetWorkConstant.REQPONSE_FIND_QUTHORIZE_SUCCESS:{
                    Logger.i( "不支持授权用户功能，不做处理");
                    break;
                }



                case NetWorkConstant.RESPONSE_RELEASE_SUCCESS:{//取消绑定机器人
                    updateButtonStatus(ROBOT_STATE_NO_ROBOT);
                    break;
                }
            }
        }
    };


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RobotRefreshEvent event) {
        updateRobotStatus();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RobotControlEvent event) {

        if(event.getAction() == RobotControlEvent.DISCONNECT_SUCCESS) {
            updateButtonStatus(BusinessConstants.ROBOT_STATE_ONLINE);// 断开机器人成功，直接将界面置为在线空闲状态

        }
    }


    /**网络连接状态监听*/
    private BroadcastReceiver mNetworkReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if(networkInfo == null || !networkInfo.isConnected()) {
                updateMessagePrompt(EVENT_NETWORK_UNAVAILABLE);
            } else {
                updateMessagePrompt(EVENT_NETWORK_AVAILABLE);
            }
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surveillance_start);
        mBackView = findViewById(R.id.title_back_view);

        mIntroduceView = findViewById(R.id.introduce_view);
        mRobotIconContainer = findViewById(R.id.robot_icon_container);
        mRobotIconView = (RoundImageView) findViewById(R.id.robot_icon_view);
        mRobotOfflineView = findViewById(R.id.robot_offline_view);
        mRobotOnlineView = findViewById(R.id.robot_online_view);
        mPowerValueView = (TextView) findViewById(R.id.power_value_view);
        mSurveillanceContainer = findViewById(R.id.launch_surveillance_container);
        mLaunchSurveillanceBtn = (TextView) findViewById(R.id.launch_surveillance_btn);

        mFirstUseHintView = findViewById(R.id.first_use_hint_view);

        mNetworkDisconnectView = findViewById(R.id.network_disconnect_view);

        mFirstUse = SPUtils.get().getBoolean(Constants.SURVEILLANCE_FIRST_USE, true);
        if(mFirstUse) {
            mFirstUseHintView.setVisibility(View.VISIBLE);
        } else {
            mFirstUseHintView.setVisibility(View.GONE);
        }

        mBackView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mLaunchSurveillanceBtn.setOnClickListener(new View.OnClickListener(){
            /*启动机器人端视频监控应用*/
            public void onClick(View v) {
                startActivity(new Intent(SurveillanceStartActivity.this, VideoSurveillanceActivity.class));
                if(mFirstUse) {
                    mFirstUseHintView.setVisibility(View.INVISIBLE);
                    SPUtils.get().put(Constants.SURVEILLANCE_FIRST_USE, false);
                }
            }
        });

        updateRobotStatus();

        registerBroadcastReceiver();
    }

    /**
     * 获取机器人电量
     * */
    private void requestPowerStatus() {

        RobotInitRepository.getInstance().getPower(new IRobotInitDataSource.GetPowerDataCallback() {
            @Override
            public void onLoadPowerData(int powerValue) {

                if(powerValue < 0) {
                    if(powerValue == -4) {
                        mPowerValueView.setText(R.string.home_battery_charge_full);
                        mPowerValueView.setTextColor(getResources().getColor(R.color.surveillance_color_green));
                    } else if(powerValue == -52) {
                        mPowerValueView.setText(R.string.home_battery_charge);
                        mPowerValueView.setTextColor(getResources().getColor(R.color.surveillance_color_red));
                    }
                } else if(powerValue <= 100 && powerValue > 20){
                    mPowerValueView.setText(powerValue+"%");
                    mPowerValueView.setTextColor(getResources().getColor(R.color.surveillance_color_green));

                } else if(powerValue < 20) {
                    mPowerValueView.setText(getString(R.string.home_battery_need_charge));
                    mPowerValueView.setTextColor(getResources().getColor(R.color.surveillance_color_red));
                }else if(powerValue>=1000){//兼容4.1麦的电量
                    int realPowerValue = powerValue - 1000;
                    if(realPowerValue==100){
                        mPowerValueView.setText(mContext.getResources().getString(R.string.home_battery_charge_full));
                        mPowerValueView.setTextColor(getResources().getColor(R.color.surveillance_color_green));
                    }else if(realPowerValue<100 && realPowerValue > 20){
                        mPowerValueView.setText(getString(R.string.home_battery_charge)+" "+(realPowerValue)+"%");
                        mPowerValueView.setTextColor(getResources().getColor(R.color.surveillance_color_green));
                    }else if(realPowerValue < 20) {//电量低于20则显示需充电
                        mPowerValueView.setText(getString(R.string.home_battery_need_charge));
                        mPowerValueView.setTextColor(getResources().getColor(R.color.surveillance_color_red));
                    } else {//其他未知数值显示充电中
                        mPowerValueView.setText(getString(R.string.home_battery_charge));
                    }
                }

                mIsPowerObtained = true;
                mTryQueryPowerCount = 0;//已经获取到电量时剩余次数置0
                startPowerRequestTimer();
            }

            @Override
            public void onDataNotAvailable(ThrowableWrapper e) {

            }
        });

        /*如果没有获取到电量，并且剩余尝试的次数大于0*/
        if(!mIsPowerObtained && mTryQueryPowerCount>0) {
            mTryQueryPowerCount --;
            if(mTryQueryPowerCount == 0) {//次数用完的情况下，则采用长时间获取电量的策略，避免阻塞事件
                startPowerRequestTimer();
            }
        }
    }

    /**
     * 更新机器人列表，默认只有一个
     * */
    private void updateRobotStatus() {

        if(!RobotManagerService.getInstance().isConnectedRobot()) { //没有机器人
            updateButtonStatus(ROBOT_STATE_NO_ROBOT);
        } else {
            updateButtonStatus(BusinessConstants.ROBOT_STATE_ONLINE);
        }
    }

    /**
     * 根据机器人状态更新按钮
     * */
    public void updateButtonStatus(int robotStatus) {
        Logger.d( "[updateButtonStatus] robotStatus = " + robotStatus);
        switch (robotStatus) {
            case ROBOT_STATE_NO_ROBOT:{
                mIntroduceView.setVisibility(View.GONE);

                mRobotIconContainer.setVisibility(View.GONE);
                mRobotOfflineView.setVisibility(View.GONE);
                mRobotOnlineView.setVisibility(View.GONE);
                mSurveillanceContainer.setVisibility(View.GONE);

                finish();
                break;
            }

            case BusinessConstants.ROBOT_STATE_ONLINE:{
                mIntroduceView.setVisibility(View.VISIBLE);
                mRobotIconContainer.setVisibility(View.VISIBLE);
                mRobotOfflineView.setVisibility(View.GONE);
                mRobotOnlineView.setVisibility(View.VISIBLE);
                mSurveillanceContainer.setVisibility(View.VISIBLE);
                mLaunchSurveillanceBtn.setEnabled(false); //按钮不可点击

                mRobotIconView.setImageResource(R.drawable.img_monitor_connect);

                break;
            }

            case BusinessConstants.ROBOT_STATE_OFFLINE:{
                mIntroduceView.setVisibility(View.VISIBLE);
                mRobotIconContainer.setVisibility(View.VISIBLE);
                mRobotOfflineView.setVisibility(View.VISIBLE);
                mRobotOnlineView.setVisibility(View.GONE);
                mSurveillanceContainer.setVisibility(View.VISIBLE);
                mLaunchSurveillanceBtn.setEnabled(false); //按钮不可点击

                mRobotIconView.setImageResource(R.drawable.img_monitor_disconnect);

                break;
            }

            case BusinessConstants.ROBOT_STATE_CONNECTED:{
                mIntroduceView.setVisibility(View.VISIBLE);
                mRobotIconContainer.setVisibility(View.VISIBLE);
                mRobotOfflineView.setVisibility(View.GONE);
                mRobotOnlineView.setVisibility(View.VISIBLE);
                mSurveillanceContainer.setVisibility(View.VISIBLE);
                mLaunchSurveillanceBtn.setEnabled(true); //按钮可点击

                mRobotIconView.setImageResource(R.drawable.img_monitor_connect);

                startPowerRequestTimer();//接收到电量之后，更改延迟时间
                break;
            }

            default:{
                //是否需要处理按后续开发决定
            }
        }
    }

    /**
     * 注册Alpha设备状态监听广播
     */
    public void registerBroadcastReceiver() {
        Logger.d( "registerBroadcastReceiver");

        if(mNetworkReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(mNetworkReceiver, intentFilter);
        }
    }

    public void unRegisterBroadcastReceiver() {

        if(mNetworkReceiver != null) {
            unregisterReceiver(mNetworkReceiver);
        }
    }



    /**
     * 启动电量查询计时器,如果存在，则先停止计时器
     * */
    private void startPowerRequestTimer() {
        if(mPowerRequestTimer != null) {
            mPowerRequestTimer.cancel();
        }
        mPowerRequestTimer = new Timer();
        int delay = (mIsPowerObtained||mTryQueryPowerCount==0)?Constants.POWER_REQUEST_INTERVAL: Constants.POWER_REQUEST_INTERVAL_WITHOUT_OBTAINED;
        mPowerRequestTimer.schedule(new PowerRequestTask(), mIsPowerObtained ? delay : 0, delay);
    }

    /**
     * 更新视频监控提示信息 e.g.:网络不可连接
     * @param type 事件类型
     * */
    private void updateMessagePrompt(int type) {
        switch (type) {
            case EVENT_NETWORK_UNAVAILABLE : {
                mNetworkDisconnectView.setVisibility(View.VISIBLE);

                mIntroduceView.setVisibility(View.GONE);

                mRobotIconContainer.setVisibility(View.GONE);
                mRobotOfflineView.setVisibility(View.GONE);
                mRobotOnlineView.setVisibility(View.GONE);
                mSurveillanceContainer.setVisibility(View.GONE);
                ToastUtils.showShortToast(  R.string.surveillance_network_unavailable_prompt);
                finish();
                break;
            }

            case EVENT_NETWORK_AVAILABLE :{
                mNetworkDisconnectView.setVisibility(View.GONE);

                if(RobotManagerService.getInstance().isConnectedRobot() ) {
                    mIntroduceView.setVisibility(View.VISIBLE);
                    mRobotIconContainer.setVisibility(View.VISIBLE);
                    mRobotOfflineView.setVisibility(View.GONE);
                    mRobotOnlineView.setVisibility(View.VISIBLE);
                    mSurveillanceContainer.setVisibility(View.VISIBLE);
                    mLaunchSurveillanceBtn.setEnabled(true); //按钮可点击

                    mRobotIconView.setImageResource(R.drawable.img_monitor_connect);
                } else {
                    mIntroduceView.setVisibility(View.VISIBLE);
                    mRobotIconContainer.setVisibility(View.VISIBLE);
                    mRobotOfflineView.setVisibility(View.VISIBLE);
                    mRobotOnlineView.setVisibility(View.GONE);
                    mSurveillanceContainer.setVisibility(View.VISIBLE);
                    mLaunchSurveillanceBtn.setEnabled(false); //按钮不可点击

                    mRobotIconView.setImageResource(R.drawable.img_monitor_disconnect);
                }

                break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.d( "[onDestroy] unRegisterBroadcastReceiver");
        unRegisterBroadcastReceiver();
        if(mPowerRequestTimer != null) {
            mPowerRequestTimer.cancel();
        }
    }

    /*电量查询task*/
    private class PowerRequestTask extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                public void run() {
                        if(RobotManagerService.getInstance().isConnectedRobot()) {
                            requestPowerStatus();
                        }
                }
            });
        }
    }
}
