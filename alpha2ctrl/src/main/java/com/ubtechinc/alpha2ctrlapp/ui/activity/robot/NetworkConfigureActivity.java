package com.ubtechinc.alpha2ctrlapp.ui.activity.robot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.collect.Lists;
import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.JsonUtils;
import com.ubtech.utilcode.utils.SPUtils;
import com.ubtech.utilcode.utils.StringUtils;
import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.base.BaseHandler;
import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.constants.IntentConstants;
import com.ubtechinc.alpha2ctrlapp.constants.PreferenceConstants;
import com.ubtechinc.alpha2ctrlapp.data.robot.IRobotAuthorizeDataSource;
import com.ubtechinc.alpha2ctrlapp.data.robot.RobotAuthorizeReponsitory;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.WifiBean;
import com.ubtechinc.alpha2ctrlapp.service.RobotManagerService;
import com.ubtechinc.alpha2ctrlapp.service.SongPlayService;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseActivity;
import com.ubtechinc.alpha2ctrlapp.ui.activity.main.MainPageActivity;
import com.ubtechinc.alpha2ctrlapp.widget.StepsView;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.CommonDiaglog;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.LoadingDialog;
import com.ubtechinc.nets.http.ThrowableWrapper;

import java.util.Map;

import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

/**
 * wifi联网优化：在原有的基础上进行UI修改
 * I am so sorry for the terrible code
 *
 * @author liuliaopu
 */
public class NetworkConfigureActivity extends BaseActivity {
    private static final String TAG = "NetworkConfigureActivity";

    /**
     * 步骤控件
     */
    private StepsView mStepsView;

    /**
     * 第一步控件：WIFI输入
     */
    private View mStepOneContentView;
    /**
     * wifi名称输入控件
     */
    private EditText mNameInputView;
    /**
     * wifi密码输入控件
     */
    private EditText mPasswdInputView;
    /**
     * wifi密码显示控件
     */
    private CheckBox mPasswdDisplayView;
    /**
     * 第一步确定按钮
     */
    private View mConfirmStepView;
    /**
     * 第一步控件：确认按钮
     */
    private TextView mStepOneConfirmView;


    /**
     * 第二步控件：联网模式
     */
    private View mStepTwoContentView;
    /**
     * 第三步控件：二维码扫描
     */
    private View mStepThreeContentView;
    /**
     * 第三步控件：二维码显示控件
     */
    private ImageView mQRCodeImageView;
    /**
     * 第四步控件：连接失败
     */
    private View mStepFourFailedContentView;
    /**
     * 第四步控件：连接成功
     */
    private View mStepFourSuccessContentView;

    /**
     * 当前wifi类型
     */
    private String mCapability = DEFAULT_WIFI_CAPABILITY;

    /**
     * 是否第一次进入
     */
    private boolean mFirstLoad = true;

    /**
     * 退出确认对话框
     */
    public CommonDiaglog mExitDialog;

    /**
     * 上一次的wifiSSID
     */
    private String mOldWifiSSID;

    /**
     * 步骤数，一共四步
     */
    private static final int STEP_COUNT = 4;

    /**
     * 标题字符串数组
     */
    private static final int[] TITLE_IDS = new int[]{
            R.string.go_to_network_mode,
            R.string.connect_to_wifi,
            R.string.scan_qrcode,
            R.string.wifi_connect_failed,
            R.string.wifi_connect_success
    };

    /**
     * 弹出wifi列表界面请求码
     */
    private static final int POP_WIFI_REQUEST_CODE = 1000;

    private static final int SEND_INFO_SUCCESS = 100110;

    private static final int GO_TO_CHECK_STATUS = 100111;

    private static final int SEND_INFO_FAILED = 100112;

    private static final int WAIT_TIMEOUT = 90 * 1000;//等待扫描超时

    private static final String MUSIC_NAME = "scan_hint.mp3";
    private static final int MSG_TIMER_COUNT_DOWN = 1001111;

    private static final int TIMER_MAX_COUNT = 3;
    /**
     * 缺省的wifi类型
     */
    private static final String DEFAULT_WIFI_CAPABILITY = "[WPA-PSK-TKIP+CCMP][ESS]";

    /**
     * 公共wifi(不需要密码)集合
     */
    private static final String[] sPasswdNoNeedWifis = new String[]{
            "WPA",
            "WEP",
            "EAP",
    };

    /**
     * 机器人序列号
     */
    private String mRobotSN = "";

    private static final int STEP_ONE = 0;
    private static final int STEP_TWO = STEP_ONE + 1;
    private static final int STEP_THREE = STEP_TWO + 1;
    private static final int STEP_FOUR = STEP_THREE + 1;//这一步表示失败
    private static final int STEP_FIVE = STEP_FOUR + 1;//这一步表示结束(成功)

    /**
     * 监听文本变化
     */
    private TextWatcher mTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            updateWifiInfoFromInner();
        }
    };

    /**
     * 广播接收器
     */
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d("nxy", "action = " + action);
            if (action.equals(IntentConstants.ACTION_ALPHA_CONNECTED_NET)) {
                sendSuccess();
            }
        }

    };

    /**
     * 在线状态监测
     */
    private BaseHandler mHandler = new BaseHandler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SEND_INFO_SUCCESS:
                    if (mContext == null) {
                        return;
                    }
                    Intent intent2 = new Intent(NetworkConfigureActivity.this, MyDeviceActivity.class);
                    mContext.startActivity(intent2);
                    ((Alpha2Application) NetworkConfigureActivity.this.getApplication()).removeActivity();
                    break;

                case GO_TO_CHECK_STATUS:
                    RobotAuthorizeReponsitory.getInstance().getRobotIMStatus(Lists.newArrayList(mRobotSN), new IRobotAuthorizeDataSource.GetRobotIMStateCallback() {
                        @Override
                        public void onLoadRobotIMState(Map<String, String> stateMap) {
                            String state = stateMap.get(mRobotSN);

                            if(StringUtils.isEmpty(state)) {
                                mHandler.sendEmptyMessageDelayed(GO_TO_CHECK_STATUS, 5000);//  收到不在线的响应继续过5s 请求

                            }else {
                                if (StringUtils.isEquals(BusinessConstants.ROBOT_IM_STATE_ONLINE, state)) {
                                    //成功之后，把消息移除
                                    mHandler.removeMessages(SEND_INFO_FAILED);
                                    sendSuccess();

                                } else if (StringUtils.isEquals(BusinessConstants.ROBOT_IM_STATE_OFFLINE, state)) {
                                    mHandler.sendEmptyMessageDelayed(GO_TO_CHECK_STATUS, 5000);//  收到不在线的响应继续过5s 请求


                                } else {
                                    mHandler.sendEmptyMessageDelayed(GO_TO_CHECK_STATUS, 5000);//  收到不在线的响应继续过5s 请求
                                }
                            }


                        }

                        @Override
                        public void onDataNotAvailable(ThrowableWrapper e) {
                            ToastUtils.showShortToast(e.getMessage());
                        }
                    });
                    break;
                case MSG_TIMER_COUNT_DOWN:
                    int remainingTime = (int) msg.obj;
                    if (remainingTime <= 0) {
                        remainingTime = 0;
                    }

                    if (remainingTime == 0) {
                        mStepOneConfirmView.setText(getString(R.string.confirm_step_1_str));
                        mStepOneConfirmView.setEnabled(true);
                    } else {
                        mStepOneConfirmView.setText(getString(R.string.confirm_step_1_str) + "(" + remainingTime + ")");
                        Message nextMsg = Message.obtain();
                        nextMsg.what = MSG_TIMER_COUNT_DOWN;
                        nextMsg.obj = remainingTime - 1;
                        mHandler.sendMessageDelayed(nextMsg, 1000);
                    }
                    break;
                case SEND_INFO_FAILED: {
                    if (mContext == null) {
                        return;
                    }
                    sendFailed();
                    break;
                }



                default:
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_configure);

        mRobotSN = getIntent().getStringExtra("robot_sn");
        int stepIndex = getIntent().getIntExtra("step", STEP_ONE);

        setupTitleView();
        setupStepsView();

        mStepsView = (StepsView) findViewById(R.id.steps_view);
        mStepsView.setStepChangedListener(new StepsView.OnStepChangedListener() {
            @Override
            public void onStepChanged(int stepIndex) {
                if (stepIndex >= TITLE_IDS.length) {
                    return;
                }

                setTitle(TITLE_IDS[stepIndex]);

                updateStep(stepIndex);
            }
        });
        mStepsView.setStepsCount(STEP_COUNT);

        mStepsView.setCurrentStep(stepIndex);

        registerBroadcastReceiver();
    }

    public void onStart() {
        super.onStart();
        ((Alpha2Application) getApplication()).removeActivity("com.ubtechinc.alpha2ctrlapp.activity.custom.BindDialogActivity");
    }

    /**
     * 这里主要用于检查第三步生成的二维码
     */
    public void onResume() {
        super.onResume();

        if (mStepsView != null) {
            if (mStepsView.getCurrentStepIndex() == STEP_THREE) {//当前是第三步
                //  setupQRCodeImageIfExist();
                createQRCode();
            }
        }
    }

    /**
     * 注册广播
     */
    private void registerBroadcastReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(IntentConstants.ACTION_ALPHA_CONNECTED_NET);
        registerReceiver(mReceiver, filter);
    }

    /**
     * 注销广播
     */
    private void unRegisterReceiver() {
        unregisterReceiver(mReceiver);
    }

    /**
     * 初始化标题栏
     */
    private void setupTitleView() {
        /*设置跳过按钮不可见*/
        if (btn_ignore != null) {
            btn_ignore.setVisibility(View.GONE);
        }

        /*返回按钮需要弹出对话框*/
        if (btn_back != null) {
            btn_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mStepsView.getCurrentStepIndex() >= STEP_THREE) {
                        mExitDialog.show();
                        return;
                    }
                    finish();
                }
            });
        }
    }


    private void sendSuccess() {
        if (mStepsView != null) {
            mStepsView.setCurrentStep(STEP_COUNT);//成功
        }

        Logger.d(TAG, "[sendSuccess]");

        if (SPUtils.get().getBoolean(PreferenceConstants.OPEN_AUTO_CONNECT, true)) {
            RobotManagerService.getInstance().connectRobot(mRobotSN, new IRobotAuthorizeDataSource.IControlRobotCallback() {
                @Override
                public void onSuccess() {
                    LoadingDialog.dissMiss();
                    goMainPage();
                }

                @Override
                public void onFail(ThrowableWrapper e) {
                    LoadingDialog.dissMiss();
                    ToastUtils.showShortToast(R.string.bt_connect_fail);
                }
            });
        }

        mHandler.sendEmptyMessageDelayed(SEND_INFO_SUCCESS, 2000);//2s之后跳转
    }
    /**
     * 连接成功进去主服务
     */
    private void goMainPage() {
        Logger.i("RobotManagerl:跳转");
        Intent intent = new Intent(getApplicationContext(), MainPageActivity.class);
        startActivity(intent);

    }

    private void sendFailed() {
        if (mStepsView != null) {
            mStepsView.setCurrentStep(3);
        }
    }

    /**
     * 初始化每一步控件及对应的事件
     */
    private void setupStepsView() {
        setupStepOneView();
        setupStepTwoView();
        setupStepThreeView();
        setupStepFourFailedView();
        setupStepFourSuccessView();

        mHandler.removeMessages(GO_TO_CHECK_STATUS);
        mHandler.sendEmptyMessageDelayed(GO_TO_CHECK_STATUS, 5000);// 隔5s 查询在线状态

        mExitDialog = new CommonDiaglog(this, false);
        mExitDialog.setMessase(mContext.getString(R.string.connect_exist_send_net_info));
        mExitDialog.setNegsitiveClick(new CommonDiaglog.OnNegsitiveClick() {
            @Override
            public void OnNegsitiveClick() {
                mExitDialog.dismiss();
            }
        });

        mExitDialog.setPositiveClick(new CommonDiaglog.OnPositiveClick() {
            @Override
            public void OnPositiveClick() {
                mExitDialog.dismiss();
                mStepsView.setCurrentStep(STEP_ONE);//直接回到第一步
            }
        });

        mExitDialog.setButtonText(mContext.getString(R.string.common_btn_cancel),
                mContext.getString(R.string.common_btn_confirm));
    }

    /**
     * 初始化第一步
     */
    private void setupStepOneView() {
        mStepOneContentView = findViewById(R.id.step_one_view);
        mStepOneConfirmView = (TextView) mStepOneContentView.findViewById(R.id.confirm_step_1);
        mStepOneConfirmView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mStepsView != null) {
                    mStepsView.nextStep();
                }
            }
        });

        View scanHintView = mStepOneContentView.findViewById(R.id.scan_hint_view);
        scanHintView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NetworkConfigureActivity.this, ListenVoiceGuideActivity.class));
            }
        });

        //开始三秒倒计时
        mStepOneConfirmView.setEnabled(false);
        Message msg = Message.obtain();
        msg.what = MSG_TIMER_COUNT_DOWN;
        msg.obj = TIMER_MAX_COUNT;
        mHandler.sendMessage(msg);
    }

    /**
     * wifi信息内部发生改变:用户输入
     */
    private void updateWifiInfoFromInner() {
        mPasswdInputView.setHint(R.string.wifi_input_psw);
        mPasswdInputView.setEnabled(true);
        mPasswdDisplayView.setVisibility(View.VISIBLE);

        //设置为缺省类型
        mCapability = DEFAULT_WIFI_CAPABILITY;

        String wifiSSIDValue = mNameInputView.getText().toString();
        String passwdValue = mPasswdInputView.getText().toString();
        if (!TextUtils.isEmpty(wifiSSIDValue) && !TextUtils.isEmpty(passwdValue)) {
            mConfirmStepView.setEnabled(true);
        } else {
            mConfirmStepView.setEnabled(false);
        }
    }

    /**
     * wifi信息从外面发生改变
     */
    private void updateWifiInfoFromOutside(String SSID, String capability) {
        if (TextUtils.isEmpty(SSID)) {
            return;
        }

        mCapability = capability;

        //外部改变，强制屏蔽监听内容变化事件
        mNameInputView.removeTextChangedListener(mTextWatcher);
        mPasswdInputView.removeTextChangedListener(mTextWatcher);

        boolean mNeedPasswd = true;
        if (!TextUtils.isEmpty(capability)) {
            for (String type : sPasswdNoNeedWifis) {
                if (SSID.toUpperCase().contains(type)) {
                    mNeedPasswd = false;
                    break;
                }
            }
        }

        mNameInputView.setText(SSID);
        mNameInputView.setSelection(SSID.length());

        if (!mNeedPasswd) {
            mConfirmStepView.setEnabled(true);
            mPasswdInputView.setText("");//清空密码
            mPasswdInputView.setHint(R.string.wifi_free_wifi);
            mPasswdInputView.setEnabled(false);
            mPasswdDisplayView.setVisibility(View.GONE);
        } else {
            mPasswdInputView.setHint(R.string.wifi_input_psw);
            mPasswdInputView.setEnabled(true);
            mPasswdDisplayView.setVisibility(View.VISIBLE);
            String wifiSSIDValue = mNameInputView.getText().toString();
            String passwdValue = mPasswdInputView.getText().toString();
            if (!TextUtils.isEmpty(wifiSSIDValue) && !TextUtils.isEmpty(passwdValue)) {
                mConfirmStepView.setEnabled(true);
            } else {
                mConfirmStepView.setEnabled(false);
            }
        }

        //wifi名称修改时，置空
        if (!TextUtils.equals(mOldWifiSSID, SSID)) {
            mOldWifiSSID = SSID;
            mPasswdInputView.setText("");
        }

        //最后再把监听事件加上
        mNameInputView.addTextChangedListener(mTextWatcher);
        mPasswdInputView.addTextChangedListener(mTextWatcher);
    }

    /**
     * 检查二维码图片是否存在,存在则设置二维码图片
     */
    private boolean setupQRCodeImageIfExist() {
//        String wifiSSIDValue = mNameInputView.getText().toString();
//        String passwdValue = mPasswdInputView.getText().toString();
//
//        WifiBean bean = new WifiBean();
//        bean.setName(wifiSSIDValue);
//        bean.setPwd(passwdValue);
//        bean.setCap(mCapability);
//        bean.setNum(mRobotSN);
//        String wifiInfo = JsonUtils.getInstance().getJson(bean).replace(" ", "");
//        wifiInfo = wifiInfo.replace("\r\n", "");
//        //Logger.d("nxy", "wifi info : " + wifiInfo);
//        String filename=String.valueOf(wifiInfo.hashCode());
//        File file = new File(Constants.PIC_RECEIVE_TEMP, filename);
//        if(file.exists()) {
//            Bitmap qrcodeBm = BitmapFactory.decodeFile(file.getAbsolutePath());
//            if(qrcodeBm != null && mQRCodeImageView != null) {
//                mQRCodeImageView.setImageBitmap(qrcodeBm);
//            }
//
//            Logger.d("nxy", "qrcode image exists");
//            return true;
//        }
//
//        Logger.d("nxy", "qrcode image not exists");

        return false;
    }

    /**
     * 初始化第二步
     */
    private void setupStepTwoView() {
        mStepTwoContentView = findViewById(R.id.step_two_view);
        mConfirmStepView = mStepTwoContentView.findViewById(R.id.confirm_step_2);
        mConfirmStepView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mStepsView != null) {
                    mStepsView.nextStep();
                }
            }
        });

        /*初始时，按钮不可用*/
        mConfirmStepView.setEnabled(false);

        mNameInputView = (EditText) mStepTwoContentView.findViewById(R.id.name_input_view);
        mPasswdInputView = (EditText) mStepTwoContentView.findViewById(R.id.passwd_input_view);
        mPasswdDisplayView = (CheckBox) mStepTwoContentView.findViewById(R.id.passwd_display_view);

        mNameInputView.addTextChangedListener(mTextWatcher);//添加输入监听
        mPasswdInputView.addTextChangedListener(mTextWatcher);//添加输入监听
        String wifiSSIDValue = SPUtils.get().getString(Constants.WIFI_NAME);
        String passwdValue =SPUtils.get().getString(Constants.WIFI_PSW);
        if (mFirstLoad && !TextUtils.isEmpty(wifiSSIDValue) && !TextUtils.isEmpty(passwdValue)) {//第一次加载上次有保存wifi信息，则设置wifi信息
            mFirstLoad = false;


            mNameInputView.setText(wifiSSIDValue);
            mNameInputView.setSelection(wifiSSIDValue.length());
            mPasswdInputView.setText(passwdValue);
            mPasswdInputView.setSelection(passwdValue.length());
        }

        /*弹出wifi列表界面控件*/
        View mPopWifiView = mStepTwoContentView.findViewById(R.id.pop_wifi_view);
        mPopWifiView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(NetworkConfigureActivity.this, WifiActivity.class), POP_WIFI_REQUEST_CODE);
            }
        });

        mPasswdDisplayView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    mPasswdInputView.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    mPasswdInputView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }

                mPasswdInputView.setSelection(mPasswdInputView.getText().toString().length());
            }
        });

        mOldWifiSSID = mNameInputView.getText().toString();
    }

    /**
     * 初始化第三步
     */
    private void setupStepThreeView() {
        mStepThreeContentView = findViewById(R.id.step_three_view);
        mQRCodeImageView = (ImageView) mStepThreeContentView.findViewById(R.id.qrcode_view);
        View hintView = mStepThreeContentView.findViewById(R.id.step_3_hint_view);
        hintView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NetworkConfigureActivity.this, ListenVoiceScanGuideActivity.class));
            }
        });
    }

    /**
     * 初始化第四步:失败
     */
    private void setupStepFourFailedView() {
        mStepFourFailedContentView = findViewById(R.id.step_four_failed_view);
        View confirmStepView = mStepFourFailedContentView.findViewById(R.id.retry_view);
        confirmStepView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mStepsView != null) {
                    //回到初始状态
                    mStepsView.setCurrentStep(STEP_ONE);
                }
            }
        });
    }

    /**
     * 初始化第四步:成功
     */
    private void setupStepFourSuccessView() {
        mStepFourSuccessContentView = findViewById(R.id.step_four_success_view);

    }

    /**
     * 更新界面步骤显示
     *
     * @param stepIndex 步骤索引(从0开始)
     */
    private void updateStep(int stepIndex) {
        if (stepIndex < STEP_ONE) {
            return;
        }

        /*为了简单方便，先隐藏所有的step，再显示特定的步骤界面*/
        mStepOneContentView.setVisibility(View.GONE);
        mStepTwoContentView.setVisibility(View.GONE);
        mStepThreeContentView.setVisibility(View.GONE);
        mStepFourFailedContentView.setVisibility(View.GONE);
        mStepFourSuccessContentView.setVisibility(View.GONE);

        switch (stepIndex) {
            case STEP_ONE: {
                mStepOneContentView.setVisibility(View.VISIBLE);
                break;
            }
            case STEP_TWO: {
                mStepTwoContentView.setVisibility(View.VISIBLE);
                break;
            }
            case STEP_THREE: {
                mStepThreeContentView.setVisibility(View.VISIBLE);

                createQRCode();
                //如果不存在二维码，则会跳到一个瞬变的界面，来生成二维码(这里应该直接生成二维码的)
//                if(!setupQRCodeImageIfExist()) {
//                    String wifiSSIDValue = mNameInputView.getText().toString();
//                    String passwdValue = mPasswdInputView.getText().toString();
//
//                    WifiBean bean = new WifiBean();
//                    bean.setName(wifiSSIDValue);
//                    bean.setPwd(passwdValue);
//                    bean.setCap(mCapability);
//                    bean.setNum(mRobotSN);
//                    String wifiInfo = JsonUtils.getInstance().getJson(bean).replace(" ", "");
//                    wifiInfo = wifiInfo.replace("\r\n", "");
//                    //Log.i("nxy", "wifiInfo = "+wifiInfo);
//                    IntentIntegrator integrator = new IntentIntegrator(this);
//                    List<String> tar = new ArrayList<String>();
//                    EncodeActivity.isHistroy =false;
//                    EncodeActivity.qRText = wifiInfo;
//                    String pkName = getPackageName();
//                    Log.i("nxy", "pack"+pkName);
//                    tar.add(pkName);
//                    integrator.setTargetApplications(tar);
//                    integrator.shareText(wifiInfo, "TEXT_TYPE");
//                }

                mHandler.sendEmptyMessageDelayed(SEND_INFO_FAILED, WAIT_TIMEOUT);//设置失败超时时间
                break;
            }
            case STEP_FOUR: {
                mStepFourFailedContentView.setVisibility(View.VISIBLE);
                break;
            }
            default: {//大于STEP_FOUR的，就认为所有的步骤都成功了
                //保存wifi信息
                String wifiSSIDValue = mNameInputView.getText().toString();
                String passwdValue = mPasswdInputView.getText().toString();
                SPUtils.get().put(Constants.WIFI_NAME, wifiSSIDValue);
                SPUtils.get().put(Constants.WIFI_PSW, passwdValue);
                mStepFourSuccessContentView.setVisibility(View.VISIBLE);
            }
        }

        if (stepIndex == STEP_THREE) {
            playScanHintMusic();
        } else {
            stopScanHintMusic();
        }
    }

    private void createQRCode() {
        String wifiSSIDValue = mNameInputView.getText().toString();
        String passwdValue = mPasswdInputView.getText().toString();
        WifiBean bean = new WifiBean();
        bean.setName(wifiSSIDValue);
        bean.setPwd(passwdValue);
        bean.setCap(mCapability);
        bean.setNum(mRobotSN);
        String wifiInfo = JsonUtils.object2Json(bean).replace(" ", "");
        wifiInfo = wifiInfo.replace("\r\n", "");
        Log.i("nxy", "wifiInfo = " + wifiInfo);
        /*
        这里为了偷懒，就没有处理匿名 AsyncTask 内部类导致 Activity 泄漏的问题
        请开发在使用时自行处理匿名内部类导致Activity内存泄漏的问题，处理方式可参考 https://github.com/GeniusVJR/LearningNotes/blob/master/Part1/Android/Android%E5%86%85%E5%AD%98%E6%B3%84%E6%BC%8F%E6%80%BB%E7%BB%93.md
         */
        final String finalWifiInfo = wifiInfo;
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                return QRCodeEncoder.syncEncodeQRCode(finalWifiInfo, BGAQRCodeUtil.dp2px(mContext, 180), Color.BLACK);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                    mQRCodeImageView.setImageBitmap(bitmap);
                } else {
                    ToastUtils.showShortToast( getString(R.string.encode_code_fail));
                }
            }
        }.execute();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (POP_WIFI_REQUEST_CODE == requestCode && RESULT_OK == resultCode) {
            String SSID = data.getStringExtra("SSID");
            String capability = data.getStringExtra("capabilities");
            updateWifiInfoFromOutside(SSID, capability);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode && mStepsView.getCurrentStepIndex() >= STEP_THREE) {
            mExitDialog.show();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 播放二维码扫描提示音乐
     */
    private void playScanHintMusic() {
        Logger.d("nxy", "play music");
        Intent intent = new Intent(NetworkConfigureActivity.this, SongPlayService.class);
        intent.putExtra(SongPlayService.EXTRA_MUSIC_NAME, MUSIC_NAME);
        startService(intent);
    }

    /**
     * 停止播放扫描提示音乐
     */
    private void stopScanHintMusic() {
        Logger.d("nxy", "stop music");
        stopService(new Intent(NetworkConfigureActivity.this, SongPlayService.class));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        stopScanHintMusic();

        unRegisterReceiver();
    }


}
