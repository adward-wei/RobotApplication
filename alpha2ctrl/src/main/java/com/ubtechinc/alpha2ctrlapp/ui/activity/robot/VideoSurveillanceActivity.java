package com.ubtechinc.alpha2ctrlapp.ui.activity.robot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.erz.joysticklibrary.JoyStick;
import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.BaseHandler;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.constants.MessageType;
import com.ubtechinc.alpha2ctrlapp.constants.SocketCmdId;
import com.ubtechinc.alpha2ctrlapp.data.robot.IRobotAppDataSource;
import com.ubtechinc.alpha2ctrlapp.data.robot.IRobotInitDataSource;
import com.ubtechinc.alpha2ctrlapp.data.robot.RobotAppRepository;
import com.ubtechinc.alpha2ctrlapp.data.robot.RobotInitRepository;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseContactActivity;
import com.ubtechinc.alpha2ctrlapp.widget.LoadingImageView;
import com.ubtechinc.nets.http.ThrowableWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;

/**
 * Created by liuliaopu on 2016/10/11.
 *
 * __________________________
 * |     |            |     |
 * |    |              |    |
 * |   |                |   |
 * |__|                  |__|
 * |                        |
 * |      A W S O M E       |
 * |                        |
 * |       UBT 2016         |
 * |                        |
 * |________________________|
 *       视频监控界面
 */

public class VideoSurveillanceActivity extends BaseContactActivity {
    private static final String TAG = "SurveillanceActivity";

    /**整个内容容器*/
    private View mContentView;
    /**loading 控件*/
    private View mLoadingView;

    /**视频监控容器控件*/
    private FrameLayout mVideoContainer;
    /**视频监控控件*/
    private FrameLayout mSurfaceViewContainer;

    /**返回按钮*/
    private View mTitleBackView;

    /**电量容器*/
    private View mPowerContainer;
    /**显示电量状态*/
    private TextView mPowerValueView;
    /**方向控制盘*/
    private JoyStick mControlPanel;
    /**拥抱图片控件，需要特殊处理*/
    private LoadingImageView mHugImageView;
    /**问候图片控件*/
    private LoadingImageView mHiImageView;
    /**卖萌图片控件*/
    private LoadingImageView mActingCuteImageView;
    /**握手图片控件*/
    private LoadingImageView mHandshakeImageView;
    /**语音容器控件*/
    private View mAudioView;
    /**语音mic图标控件*/
    private ImageView mAudioMicView;
    /**语音文本控件*/
    private TextView mAudioTextView;
    /**是否关闭本地语音*/
    private boolean mIsMuteAudio = true;

    /**视频监控引擎*/
    private RtcEngine mRtcEngine;
    /**视频监控事件处理*/
    private RtcEngineEventHandler mRtcHandler;
    /**房间号*/
    private String mChannelID = null;
    /**动态key*/
    //private String mChannelKey = null;
    /**定义旋转1度需要移动方向盘多少距离*/
    private static final int DISTANCE_ONE_ANGLE = 20;

    private static final int CMD_SEND_INTERVAL = 250;//250ms
    private long mLastCmdSendTime = 0;

    /*各种动作消息类型定义*/
    private static final int MESSAGE_ACTION_HUG = 1000;
    private static final int MESSAGE_ACTION_HI = MESSAGE_ACTION_HUG + 1;
    private static final int MESSAGE_ACTION_PLAYING_CUTE = MESSAGE_ACTION_HI + 1;
    private static final int MESSAGE_ACTION_HANDSHAKE = MESSAGE_ACTION_PLAYING_CUTE + 1;
    /*各种动作延迟时间*/
    private static final int DELAY_ACTION_HUG = 4000;
    private static final int DELAY_ACTION_HI = 8050;
    private static final int DELAY_ACTION_PLAYING_CUTE = 6250;
    private static final int DELAY_ACTION_HANDSHAKE = 6550;

    private static final int EVENT_NETWORK_UNAVAILABLE = 1000;
    private static final int EVENT_NETWORK_AVAILABLE = 1001;

    /**是否繁忙，处于动画交互中*/
    private boolean mIsBusy = true;
    /**是否key失效来获取ChannelKey的*/
    private boolean mIsKeyExpired = false;

    /**没有获取到电量时，短间隔尝试获取电量次数*/
    private int mTryQueryPowerCount = 5;

    private Timer mChannelRequestTimer;
    private static final int REQUEST_CHANNEL_INTERVAL = 6000;//6s内没有获取到房间号，重新获取一次
    private int mChannelTryCount = 3;//获取房间号机会次数

    private static final int FIRST_FRAME_CHECK_INTERVAL = 8000;//8s内没有获取到视频画面，重新走整个流程

    /**视频监控请求加入房间分为三个状态，请求房间号、获取到房间号，获取到第一帧视频画面，因此定义三个状态，便于异常处理*/
    private enum Status{
        CHANNEL_REQUEST, CHANNEL_OBTAINED, FIRST_FRAME_OBTAINED
    };

    private Status mStatus = Status.CHANNEL_REQUEST;

    private int mVideoWidth;
    private int mVideoHeight;
    private SurfaceView mRemoteView;

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

    private BaseHandler mHandler = new BaseHandler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg == null) {
                Logger.i( "handleMessage msg is null.");
                return;
            }

            switch (msg.what) {
                case MessageType.ALPHA_LOST_CONNECTED:{
                    isCurrentAlpha2MacLostConnection((String) msg.obj);
                    break;
                }



                case SocketCmdId.ALEXA_MSG_RSP_VIDEO_SURVEILLANCE:{//获取房间号
                    Logger.d( "received video surveillance info");
                    byte[] infoBytes = (byte[])msg.obj;
                    if(infoBytes != null) {
                        try {
                            String infoStr = new String(infoBytes);
                            Logger.d( "received video surveillance info = " + infoStr);
                            if(infoStr != null) {
                                Logger.d( infoStr.substring(infoStr.indexOf("{")));//机器人端发过来的数据有乱码，暂时这样处理
                                JSONObject object = new JSONObject(infoStr.substring(infoStr.indexOf("{")));

                                /*Type=1，表示返回房间号，例如：{"Type":1,"RoomNumber":"4b101c6ca1ffff1ffffb53c7448"}*/
                                if("1".equals((String)object.get("Type"))) {
                                    String roomNumber = (String)object.get("RoomNumber");
                                    //String channelKey = (String)object.get("ChannelKey");
                                    if(!TextUtils.isEmpty(roomNumber) /*&& !TextUtils.isEmpty(channelKey)*/) {
                                        mStatus = Status.CHANNEL_OBTAINED;//更改状态为获取到房间号
                                        Logger.d( "change status robotSerialNo CHANNEL_OBTAINED");
                                        if(mIsKeyExpired) {
                                            mIsKeyExpired = false;
                                            //mChannelKey = channelKey;
                                            if(mRtcEngine != null) {
                                                //int value = mRtcEngine.renewChannelKey(mChannelKey);
                                                //Logger.d( "value = " + value);
                                            }
                                        } else {
                                            mChannelID = roomNumber;
                                            //mChannelKey = channelKey;
                                            setupRtcEngine();
                                            setupChannel();
                                        }

                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                }
            }
        }
    };

    /*电量查询计时器*/
    private Timer mPowerRequestTimer;
    /*是否查询到电量*/
    private boolean mIsPowerObtained = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // keep screen on - turned on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_video_surveillance);

        mContentView = findViewById(R.id.content);

        mLoadingView = findViewById(R.id.loading_view);

        mTitleBackView = findViewById(R.id.title_back_view);
        mVideoContainer = (FrameLayout) findViewById(R.id.video_container);
        mSurfaceViewContainer = (FrameLayout) findViewById(R.id.surface_view_container);
        mPowerContainer = findViewById(R.id.power_container);
        mPowerValueView = (TextView) findViewById(R.id.power_value_view);
        mControlPanel = (JoyStick) findViewById(R.id.control_panel);
        mControlPanel.setPadBackground(R.drawable.img_rocker_bg_in);
        mControlPanel.setButtonDrawable(R.drawable.ic_rocker_in);
        mControlPanel.setButtonRadiusScale(40);//按照比例调节方向盘和摇杆的大小(25~50)

        mHugImageView = (LoadingImageView)findViewById(R.id.hug_image_view);
        mHiImageView = (LoadingImageView)findViewById(R.id.hi_image_view);
        mActingCuteImageView = (LoadingImageView)findViewById(R.id.playing_cute_image_view);
        mHandshakeImageView = (LoadingImageView)findViewById(R.id.handshake_image_view);
        mAudioView = findViewById(R.id.audio_view);
        mAudioMicView = (ImageView)findViewById(R.id.mic_icon);
        mAudioTextView = (TextView) findViewById(R.id.audio_text_view);

        adjustVideoLayout();

        registerReceiver();

        setupInteractiveEvent();
        setupControlPanel();

        startPowerRequestTimer();

        startTryToJoinChannel();

        enableAllWidgets(false);
    }

    /**
     * 自适应video容器控件宽高
     * */
    private void adjustVideoLayout() {
        mVideoWidth = getWindowManager().getDefaultDisplay().getWidth();
        /*视频监控的默认宽高是640x360*/
        mVideoHeight = (int)(mVideoWidth/640.0 * 360);

        Logger.d( "[adjustVideoLayout] mVideoHeight = " + mVideoHeight);
        mVideoContainer.getLayoutParams().height = mVideoHeight;
        mVideoContainer.requestLayout();

        mRemoteView = RtcEngine.CreateRendererView(getApplicationContext());
        mSurfaceViewContainer.addView(mRemoteView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        mRemoteView.setZOrderOnTop(true);
        mRemoteView.setZOrderMediaOverlay(true);
    }

    /**
     * 注册广播接收器
     * */
    private void registerReceiver() {
        if(mNetworkReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(mNetworkReceiver, intentFilter);
        }
    }

    /**
     * 注销广播接收器
     * */
    private void unregisterReceiver() {
        if(mNetworkReceiver != null) {
            unregisterReceiver(mNetworkReceiver);
        }
    }

    /**
     * 开始尝试获取房间号，加入房间
     * */
    private void startTryToJoinChannel() {
        mStatus = Status.CHANNEL_REQUEST;
        requestChannelId();//开始请求房间
        startChannelRequestTimer();//启动计时器
    }

    /**
     * 获取房间号(ChannelId)
     * */
    private void requestChannelId() {
        if(Status.CHANNEL_REQUEST != mStatus) {
            return;
        }



        RobotAppRepository.getInstance().startApp(Constants.MAIN_SERVICE_SURVEILLANCE_NAME, Constants.MAIN_SERVICE_SURVEILLANCE_PACKAGE_NAME, new IRobotAppDataSource.ControlAppCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFail(ThrowableWrapper e) {

            }
        });
    }

    /**
     * 启动获取房间号计时器
     * */
    private void startChannelRequestTimer() {
        stopChannelRequestTimer();

        if(Status.CHANNEL_REQUEST != mStatus) {
            return;
        }

        mChannelRequestTimer = new Timer();
        mChannelRequestTimer.schedule(new ChannelRequestTask(), REQUEST_CHANNEL_INTERVAL, REQUEST_CHANNEL_INTERVAL);
    }

    /**
     * 停止获取房间号计时器
     * */
    private void stopChannelRequestTimer() {
        Logger.d( "stopChannelRequestTimer");
        if(mChannelRequestTimer != null) {
            mChannelRequestTimer.cancel();
        }

        if(mChannelTryCount<0 && Status.FIRST_FRAME_OBTAINED != mStatus) {//尝试的次数超过限次还未获取到，则退出
            Logger.d( "request channel id failed");
            Toast.makeText(this, R.string.connect_failed, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /**
     * 延迟检测第一帧视频画面是否获取到
     * */
    private void startCheckFirstFrameVideo() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(Status.FIRST_FRAME_OBTAINED != mStatus) { //获取到房间号后，未获取到第一帧画面，则重新获取房间号
                    leaveChannel();
                    stopAllTimer();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mStatus = Status.CHANNEL_REQUEST;
                            startTryToJoinChannel();
                        }
                    }, 2000);
                }
            }
        }, FIRST_FRAME_CHECK_INTERVAL);
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
                    } else if(powerValue == -52) {
                        mPowerValueView.setText(R.string.home_battery_charge);
                    }
                } else if(powerValue <= 100){
                    mPowerValueView.setText(""+powerValue+"%");

                    if(powerValue <= 20) {
                        mPowerContainer.setVisibility(View.VISIBLE);
                    } else {
                        mPowerContainer.setVisibility(View.GONE);
                    }
                }else if(powerValue>=1000){//兼容4.1麦的电量
                    if(powerValue==1100){
                        mPowerValueView.setText(mContext.getResources().getString(R.string.home_battery_charge_full));
                    }else if(powerValue<1100){
                        mPowerValueView.setText(mContext.getResources().getString(R.string.home_battery_charge)+" "+(powerValue-1000)+"%");
                    }else{
                        mPowerValueView.setText(mContext.getResources().getString(R.string.home_battery_charge));
                    }
                }

                mIsPowerObtained = true;
                mTryQueryPowerCount = 0;//已经获取到电量时剩余次数置0
                startPowerRequestTimer();//接收到电量之后，更改延迟时间
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
     * 启动电量查询计时器,如果存在，则先停止计时器
     * */
    private void startPowerRequestTimer() {
        if(mPowerRequestTimer != null) {
            mPowerRequestTimer.cancel();
        }
        mPowerRequestTimer = new Timer();
        int delay = (mIsPowerObtained||mTryQueryPowerCount==0)?Constants.POWER_REQUEST_INTERVAL:Constants.POWER_REQUEST_INTERVAL_WITHOUT_OBTAINED;
        mPowerRequestTimer.schedule(new PowerRequestTask(), mIsPowerObtained ? delay : 0, delay);
    }

    private void setupRtcEngine() {
        if(mRtcHandler == null) {
            mRtcHandler = new RtcEngineEventHandler();
        }

        if(mRtcEngine == null) {
            mRtcEngine = RtcEngine.create(this, Constants.APP_ID, mRtcHandler);
        }

        mRtcEngine.setLogFile(getApplicationContext().getExternalFilesDir(null).toString() + "/alexaVideo.log");

        mRtcEngine.enableVideo();
        mRtcEngine.muteLocalVideoStream(true);//不显示本地摄像头视频
        mRtcEngine.muteLocalAudioStream(mIsMuteAudio);//是否显示本地声音，初始时不显示
        mRtcEngine.muteAllRemoteVideoStreams(false);
    }

    /**
     * 加入视频房间
     * */
    private void setupChannel(){
        if(mRtcEngine != null) {
            int status = mRtcEngine.joinChannel(/*mChannelKey*/null, mChannelID, "", 0);

            if(status < 0) {
                Logger.d( "join channel failed");
                Toast.makeText(this, "try again", Toast.LENGTH_SHORT).show();
                startTryToJoinChannel();
            } else {
                startCheckFirstFrameVideo();
            }
        }
    }

    public synchronized void onFirstRemoteVideoDecoded(final int uid, final int width, final int height, final int elapsed){
        mStatus = Status.FIRST_FRAME_OBTAINED;//更改状态为获取到第一帧视频画面，此时才算真正成功
        int status = mRtcEngine.setEnableSpeakerphone(true);
        Logger.d( "[onFirstRemoteVideoDecoded] status=" + status);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // ensure remote video view setup
                final SurfaceView remoteView = mRemoteView;

                int containerWidth = mVideoContainer.getMeasuredWidth();

                final int adjustHeight = (int)(height * (containerWidth*1.0/width));

                mVideoContainer.getLayoutParams().height = adjustHeight;

                mVideoContainer.requestLayout();

                mLoadingView.setVisibility(View.GONE);
                enableAllWidgets(true);

                mRtcEngine.enableVideo();
                int successCode = mRtcEngine.setupRemoteVideo(new VideoCanvas(remoteView, VideoCanvas.RENDER_MODE_ADAPTIVE, uid));

                if (successCode < 0) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mRtcEngine.setupRemoteVideo(new VideoCanvas(remoteView, VideoCanvas.RENDER_MODE_ADAPTIVE, uid));
                            remoteView.invalidate();
                        }
                    }, 500);
                }
            }
        });
    }

    /**
     * 初始化各种交互控件事件
     * */
    private void setupInteractiveEvent() {

        mTitleBackView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mHugImageView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                try{
                    JSONObject hugObj = packInteractiveControl("3", "hug");
                    sendControlCommand(hugObj);
                    sendActionMessage(MESSAGE_ACTION_HUG);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mHiImageView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                try{
                    JSONObject sayHiObj = packInteractiveControl("3", "lifthand");
                    sendControlCommand(sayHiObj);
                    sendActionMessage(MESSAGE_ACTION_HI);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mActingCuteImageView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                try{
                    JSONObject actingCuteObj = packInteractiveControl("3", "happy");
                    sendControlCommand(actingCuteObj);
                    sendActionMessage(MESSAGE_ACTION_PLAYING_CUTE);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mHandshakeImageView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                try{
                    JSONObject handshakeObj = packInteractiveControl("3", "handshake");
                    sendControlCommand(handshakeObj);
                    sendActionMessage(MESSAGE_ACTION_HANDSHAKE);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mAudioView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(mRtcEngine != null) {
                    mIsMuteAudio = !mIsMuteAudio;
                    mRtcEngine.muteLocalAudioStream(mIsMuteAudio);
                    //切换语音控件显示状态
                    if(!mIsMuteAudio) {
                        mAudioTextView.setText(R.string.turn_off_phone_mic);
                        mAudioMicView.setImageResource(R.drawable.ic_phonemic_turnoff);
                        mAudioView.setBackground(getResources().getDrawable(R.drawable.surveillance_phone_mic_on));
                    } else {
                        mAudioTextView.setText(R.string.turn_on_phone_mic);
                        mAudioMicView.setImageResource(R.drawable.ic_phonemic_turnon);
                        mAudioView.setBackground(getResources().getDrawable(R.drawable.surveillance_phone_mic_off));
                    }
                }
            }
        });
    }

    /**
     * 初始化方向盘控制事件向机器人发送转动命令
     * */
    private void setupControlPanel() {
        mControlPanel.setListener(new JoyStick.JoyStickListener(){

            /**
             * @param joyStick  方向控制盘
             * @param angle 范围：-pi~pi
             * @param power 范围：0~100
             * */
            @Override
            public void onMove(JoyStick joyStick, double angle, double power) {
                if(mIsBusy) {//繁忙状态不能操作方向盘
                    return;
                }

                long currentCmdTime = System.currentTimeMillis();
                if(currentCmdTime - mLastCmdSendTime <= CMD_SEND_INTERVAL) {
                    return;
                }

                mLastCmdSendTime = currentCmdTime;

                double xOffset = Math.cos(angle)*power;
                double yOffset = Math.sin(angle)*power;

                int xAngle = (int)(xOffset/DISTANCE_ONE_ANGLE);
                int yAngle = (int)(yOffset/DISTANCE_ONE_ANGLE);

                try{
                    JSONObject cmdObject = packHeadControl("2", xAngle, yAngle);
                    sendControlCommand(cmdObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 延迟发送交互指令
     * @param actionType 交互类型
     * */
    private void sendActionMessage(int actionType) {
        mControlPanel.setPadBackground(R.drawable.img_rocker_bg_out);
        mControlPanel.setButtonDrawable(R.drawable.ic_rocker_out);
        mControlPanel.setEnabled(false);

        mIsBusy = true;

        switch (actionType) {
            case MESSAGE_ACTION_HUG:{//hug
                mActionHandler.sendEmptyMessageDelayed(actionType, DELAY_ACTION_HUG);
                mHugImageView.setClickable(false);
                mHugImageView.startAnimation(DELAY_ACTION_HUG);
                mHiImageView.setEnabled(false);
                mActingCuteImageView.setEnabled(false);
                mHandshakeImageView.setEnabled(false);
                break;
            }

            case MESSAGE_ACTION_HI:{//hi
                mActionHandler.sendEmptyMessageDelayed(actionType, DELAY_ACTION_HI);
                mHugImageView.setEnabled(false);
                mHiImageView.setClickable(false);
                mHiImageView.startAnimation(DELAY_ACTION_HI);
                mActingCuteImageView.setEnabled(false);
                mHandshakeImageView.setEnabled(false);
                break;
            }

            case MESSAGE_ACTION_PLAYING_CUTE:{//playing cute
                mActionHandler.sendEmptyMessageDelayed(actionType, DELAY_ACTION_PLAYING_CUTE);
                mHugImageView.setEnabled(false);
                mHiImageView.setEnabled(false);
                mActingCuteImageView.setClickable(false);
                mActingCuteImageView.startAnimation(DELAY_ACTION_PLAYING_CUTE);
                mHandshakeImageView.setEnabled(false);
                break;
            }

            case MESSAGE_ACTION_HANDSHAKE:{//handshake
                mActionHandler.sendEmptyMessageDelayed(actionType, DELAY_ACTION_HANDSHAKE);
                mHugImageView.setEnabled(false);
                mHiImageView.setEnabled(false);
                mActingCuteImageView.setEnabled(false);
                mHandshakeImageView.setClickable(false);
                mHandshakeImageView.startAnimation(DELAY_ACTION_HANDSHAKE);
                break;
            }
        }
    }

    /**
     * 是否让所有控件可操作
     * */
    private void enableAllWidgets(boolean enable) {
        if(enable) {
            mIsBusy = false;
            mControlPanel.setPadBackground(R.drawable.img_rocker_bg_in);
            mControlPanel.setButtonDrawable(R.drawable.ic_rocker_in);
            mControlPanel.setEnabled(true);

            mHugImageView.setEnabled(true);
            mHiImageView.setEnabled(true);
            mActingCuteImageView.setEnabled(true);
            mHandshakeImageView.setEnabled(true);
        } else {
            mIsBusy = true;
            mControlPanel.setPadBackground(R.drawable.img_rocker_bg_out);
            mControlPanel.setButtonDrawable(R.drawable.ic_rocker_out);
            mControlPanel.setEnabled(false);

            mHugImageView.setEnabled(false);
            mHiImageView.setEnabled(false);
            mActingCuteImageView.setEnabled(false);
            mHandshakeImageView.setEnabled(false);
        }
    }

    /**
     * 通过json打包头部旋转命令
     * e.g.
     * {
     *     "Type" : "2", --->头部旋转命令类型
     *     "Control" : "head",
     *     "Orientation" : "["left", "up"]",
     *     "Angle" : ["2", "3"]
     * }
     * @param type 命令类型
     * @param xAngle 左右旋转角度
     * @param yAngle 上下旋转角度
     * */
    private JSONObject packHeadControl(String type, int xAngle, int yAngle) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Type", type);
        jsonObject.put("Control", "head");

        String[] orientation = new String[2];

        if(xAngle < 0) {
            orientation[0] = "left";
        }else {
            orientation[0] = "right";
        }
        if(yAngle < 0) {
            orientation[1] = "down";
        } else {
            orientation[1] = "up";
        }

        JSONArray orientationJsonArr = new JSONArray();
        orientationJsonArr.put(0, orientation[0]);
        orientationJsonArr.put(1, orientation[1]);
        jsonObject.put("Orientation", orientationJsonArr);

        JSONArray angleJSONArr = new JSONArray();
        angleJSONArr.put(0, "" + Math.abs(xAngle));
        angleJSONArr.put(1, "" + Math.abs(yAngle));

        jsonObject.put("Angle", angleJSONArr);
        return jsonObject;
    }

    /**
     * 通过json打包交互(握手、卖萌等)命令
     * e.g.
     * {
     *     "Type" : "3", --->交互命令类型
     *     "Action_Name" : "handshake"
     * }
     * @param type  命令类型
     * @param actionName 命令名称
     * */
    private JSONObject packInteractiveControl(String type, String actionName) throws JSONException{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Type", type);
        jsonObject.put("Control", "body");
        jsonObject.put("Action_Name", actionName);

        return jsonObject;
    }

    /**
     * 发送离开消息
     * @param type 退出房间消息，通知机器人端视频监控退出
     * e.g.
     * {
     *     "Type" : "4";
     * }
     * */
    private JSONObject packLeaveChannelControl(String type) throws JSONException{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Type", type);
        return jsonObject;
    }

    private void sendControlCommand(JSONObject jsonObj) {
        try {

//            ConstactsTools.createThirdNotificationIQ(Constants.MAIN_SERVICE_SURVEILLANCE_APP_KEY,
//                    new String(Base64.encode(jsonObj.toString().getBytes(), Base64.DEFAULT), "UTF-8"), Alpha2Application.getRobotSerialNo(), this);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 离开房间
     * */
    private void leaveChannel(){

        if(mPowerRequestTimer != null) {
            mPowerRequestTimer.cancel();
        }

        try {
            JSONObject leaveControlObj = packLeaveChannelControl("4");
            sendControlCommand(leaveControlObj);
        }catch (Exception e) {
            e.printStackTrace();
        }

        if(mRtcEngine != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mRtcEngine.leaveChannel();
                }
            }).run();
        }
    }

    /**
     * 更新视频监控提示信息 e.g.:网络不可连接
     * @param type 事件类型
     * */
    private void updateMessagePrompt(int type) {
        switch (type) {
            case EVENT_NETWORK_UNAVAILABLE : {
                mIsBusy = true;
                mLoadingView.setVisibility(View.VISIBLE);

                mControlPanel.setPadBackground(R.drawable.img_rocker_bg_out);
                mControlPanel.setButtonDrawable(R.drawable.ic_rocker_out);
                mControlPanel.setEnabled(false);

                mHugImageView.setEnabled(false);
                mHiImageView.setEnabled(false);
                mActingCuteImageView.setEnabled(false);
                mHandshakeImageView.setEnabled(false);
                break;
            }

            case EVENT_NETWORK_AVAILABLE :{
                mIsBusy = false;
                //只有已经获取到了第一帧图片的时候，才隐藏，不然进去不会显示loading
                if(Status.FIRST_FRAME_OBTAINED == mStatus) {
                    mLoadingView.setVisibility(View.GONE);
                }

                mControlPanel.setPadBackground(R.drawable.img_rocker_bg_in);
                mControlPanel.setButtonDrawable(R.drawable.ic_rocker_in);
                mControlPanel.setEnabled(true);

                mHugImageView.setEnabled(true);
                mHiImageView.setEnabled(true);
                mActingCuteImageView.setEnabled(true);
                mHandshakeImageView.setEnabled(true);
                break;
            }
        }
    }

    /**
     * 停止所有计时器
     * */
    private void stopAllTimer() {
        if(mChannelRequestTimer != null) {
            mChannelRequestTimer.cancel();
        }

        if(mPowerRequestTimer != null) {
            mPowerRequestTimer.cancel();
        }

    }

    /**
     * 返回事件处理
     * */
    public void onBackPressed() {
        if(Status.FIRST_FRAME_OBTAINED != mStatus) {
            return;
        }

        super.onBackPressed();
    }

    /**
     * 不可见的情况下都直接退出
     * */
    public void onStop() {
        super.onStop();
        finish();
    }

    @Override
    public void onDestroy() {
        // keep screen on - turned off
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        stopAllTimer();
        leaveChannel();
        unregisterReceiver();
        super.onDestroy();
    }


    private class RtcEngineEventHandler extends IRtcEngineEventHandler {
        //显示房间内其他用户的视频
        @Override
        public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {
            Logger.d( "onFirstRemoteVideoDecoded: uid: " + uid + ", width: " + width + ", height: " + height);
            VideoSurveillanceActivity.this.onFirstRemoteVideoDecoded(uid, width, height, elapsed);
        }

        //用户进入
        @Override
        public void onUserJoined(int uid, int elapsed){
            Logger.d( "onUserJoined");
        }

        //用户退出
        @Override
        public void onUserOffline(int uid, int reason) {
            Logger.d( "onUserOffline");
            //Toast.makeText(VideoSurveillanceActivity.this, "it is finished", Toast.LENGTH_SHORT).show();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Toast.makeText(VideoSurveillanceActivity.this, "it is finished", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }

        //监听其他用户是否关闭视频
        @Override
        public void onUserMuteVideo(int uid,boolean muted){
            Logger.d( "onUserMuteVideo");
        }

        @Override
        public void onLeaveChannel(RtcStats stats) {
            Logger.d( "onLeaveChannel");
        }

        @Override
        public void onError(int err) {
            Logger.d( "onError : err=" + err);
            if(err == 109) { //ERR_CHANNEL_KEY_EXPIRED, channel key 失效重新获取
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mIsKeyExpired = true;

                        RobotAppRepository.getInstance().startApp(Constants.MAIN_SERVICE_SURVEILLANCE_NAME, Constants.MAIN_SERVICE_SURVEILLANCE_PACKAGE_NAME, new IRobotAppDataSource.ControlAppCallback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onFail(ThrowableWrapper e) {

                            }
                        });

                    }
                });
            }
        }
    }

    private Handler mActionHandler = new Handler(){

        public void handleMessage(Message msg) {
            mIsBusy = false;
            mControlPanel.setPadBackground(R.drawable.img_rocker_bg_in);
            mControlPanel.setButtonDrawable(R.drawable.ic_rocker_in);
            mControlPanel.setEnabled(true);

            mHugImageView.setEnabled(true);
            mHugImageView.setClickable(true);
            mHiImageView.setEnabled(true);
            mHiImageView.setClickable(true);
            mActingCuteImageView.setEnabled(true);
            mActingCuteImageView.setClickable(true);
            mHandshakeImageView.setEnabled(true);
            mHandshakeImageView.setClickable(true);
        }
    };

    /*电量查询task*/
    private class PowerRequestTask extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                public void run() {
                    requestPowerStatus();
                }
            });
        }
    }

    /**
     * 房间号请求任务
     * */
    private class ChannelRequestTask extends TimerTask{

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mChannelTryCount --;
                    if(Status.CHANNEL_REQUEST != mStatus) {//如果获取到了，则停止房间号
                        stopChannelRequestTimer();
                    } else {
                        if(mChannelTryCount >= 0) {
                            requestChannelId();
                            Logger.d( "left chance : " + mChannelTryCount);
                        } else {//尝试的次数超过限制
                            stopChannelRequestTimer();
                        }
                    }
                }
            });
        }
    }
}
