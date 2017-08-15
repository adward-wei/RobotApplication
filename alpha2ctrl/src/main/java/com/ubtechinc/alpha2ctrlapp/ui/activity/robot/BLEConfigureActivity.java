package com.ubtechinc.alpha2ctrlapp.ui.activity.robot;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.SPUtils;
import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.BuildConfig;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.base.BaseHandler;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.constants.PreferenceConstants;
import com.ubtechinc.alpha2ctrlapp.data.robot.IRobotAuthorizeDataSource;
import com.ubtechinc.alpha2ctrlapp.service.RobotManagerService;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseActivity;
import com.ubtechinc.alpha2ctrlapp.ui.activity.main.MainPageActivity;
import com.ubtechinc.alpha2ctrlapp.util.bluetooth.BLECommandTool;
import com.ubtechinc.alpha2ctrlapp.widget.StepsView;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.CommonDiaglog;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.LoadingDialog;
import com.ubtechinc.blelibrary.BLEManager;
import com.ubtechinc.nets.http.ThrowableWrapper;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;


/**
 * @author liuliaopu
 *         BLE 蓝牙配网
 *         <p>
 *         ||         //======\\    =========    ||=======   ||========||
 *         ||         ||      ||   ||            ||          ||        ||
 *         ||         ||      ||   ||========    ||=======   ||========||
 *         ||         ||      ||            ||   ||          ||=======\\
 *         =======     \\====//     ========||   ||=======   ||        \\
 */

public class BLEConfigureActivity extends BaseActivity {
    private static final String TAG = "BLEConfigureActivity";

    private BLEManager mBLEManager;

    private static String DEVICE_PREFIX_UP = "ALPHA2";
    private static String DEVICE_PREFIX = "Alpha2";

    /**
     * 显示序列号长度
     */
    public static final int SN_COUNT = 15;

    /**
     * 步骤控件
     */
    private StepsView mStepsView;

    /**
     * 第一步控件：联网提示
     */
    private View mStepOneContentView;
    /**
     * 第一步控件：确认按钮
     */
    private TextView mStepOneConfirmView;

    /**
     * 第二步控件：蓝牙列表
     */
    private View mStepTwoContentView;
    private DeviceAdapter mDeviceAdapter;
    /**
     * 设备列表
     */
    private ArrayList<DeviceInfo> mDevices = new ArrayList<>();
    /**
     * 保留设备mac地址，避免重复添加
     */
    private HashSet<String> mDeviceAddr = new HashSet<String>();
    /**
     * 设备列表控件
     */
    private ListView mDeviceListView;
    private ViewHolder mCurrentViewHolder;
    /**
     * 尝试其他方式控件
     */
    private View mTryOtherView;
    /**
     * 机器人图标
     */
    private View mRobotLisenceView;

    /**
     * 第三步控件：wifi输入
     */
    private View mStepThreeContentView;
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
     * 第三版确定按钮
     */
    private View mConfirmStepThreeView;
    /**
     * 弹出wifi列表控件
     */
    private PopupWindow mWifiPopupView;
    /**
     * wifi列表控件
     */
    private ListView mWifiListView;
    /**
     * wifi适配器
     */
    private WifiAdapter mWifiAdapter;
    /**
     * wifi列表
     */
    private List<BLECommandTool.WifiInfo> mWifiList = new ArrayList<>();
    private TextView mConnectTitleView;
    private View mConnectErrorView;
    /**
     * 连接动画控件
     */
    private View mConnectingView;
    /**
     * 连接失败显示空
     */
    private View mConnectFailedView;

    /**
     * 机器人序列号
     */
    private String mRobotSN = "";

    /**
     * 第四步控件：连接动画
     */
    private View mStepConnectionContentView;
    /**
     * 第四步控件：发送状态
     */
    private View mStepFourContentView;

    /**连接失败对话框*/
    private CommonDiaglog mConnectFailedDialog;

    /**退出确认对话框*/
    public CommonDiaglog mExitDialog;

    /**
     * 是否第一次进入
     */
    private boolean mFirstLoad = true;

    private static final int SEND_INFO_SUCCESS = 100110;

    private static final int MSG_TIMER_COUNT_DOWN = 100111;

    private static final int BACK_TO_STEP_THREE = 100113;

    private static final int TIMER_MAX_COUNT = 3;

    /**
     * 步骤数，一共四步
     */
    private static final int STEP_COUNT = 4;

    private static final int STEP_ONE = 0;
    private static final int STEP_TWO = STEP_ONE + 1;
    private static final int STEP_THREE = STEP_TWO + 1;
    private static final int STEP_FOUR = STEP_THREE + 1;//这一步表示失败
    private static final int STEP_FIVE = STEP_FOUR + 1;//这一步表示结束(成功)

    /**
     * 产品定义的缺省扫描蓝牙时间
     * */
    private static final int DEFAULT_SCAN_TIME_OUT = 15*1000;

    /**
     * 缺省的wifi类型
     */
    private static final String DEFAULT_WIFI_CAPABILITY = "[WPA-PSK-TKIP+CCMP][ESS]";

    /**
     * 标题字符串数组
     */
    private static final int[] TITLE_IDS = new int[]{
            R.string.ble_connect_to_the_internet,
            R.string.ble_connect_to_device,
            R.string.ble_connect_to_wifi,
            R.string.ble_connect_to_wifi,
            R.string.connection_successful
    };

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

    private BLEManager.DeviceScanCallback mScanCallback = new BLEManager.DeviceScanCallback() {

        @Override
        public void onDeviceFound(BluetoothDevice bluetoothDevice, int rssi, byte[] scanRecord) {
            String deviceName = bluetoothDevice.getName();
            if (TextUtils.isEmpty(deviceName) || !deviceName.toUpperCase().startsWith(DEVICE_PREFIX_UP)) {
                return;
            }

            if (!mDeviceAddr.contains(bluetoothDevice.getAddress())) {
                mDeviceAddr.add(bluetoothDevice.getAddress());

                DeviceInfo deviceInfo = new DeviceInfo();
                deviceInfo.device = bluetoothDevice;
                try {
                    int length = mRobotSN.length();
                    byte []snByte =new byte[length];
                    int startIndex = 7;
                    if(scanRecord.length >= startIndex + length) {
                        for(int i=startIndex; i<length+startIndex; i++) {
                            snByte[i-7]=scanRecord[i];
                        }
                    }

                    String SN = new String(snByte,"UTF-8");
                    Logger.d(TAG, "current SN = " + mRobotSN + ", scan SN = " + SN);
                    if (!mRobotSN.equals(SN)) {//只选择当前序列号的机器
                        return;
                    }
                    SN = SN.substring(0, SN.length() - 4);
                    deviceInfo.SN = SN;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                /*String[] deviceSegments = deviceName.split("_");
                if(deviceSegments.length >= 2) {
                    String SN = deviceSegments[deviceSegments.length - 1];
                    Logger.d(TAG, "current SN = " + mRobotSN + ", scan SN = " + SN);
                    if (!mRobotSN.equals(SN)) {//只选择当前序列号的机器
                        return;
                    }

                    if (SN.length() >= SN_COUNT) {
                        SN = SN.substring(0, SN.length() - 4);//隐藏后4位序列号
                    }
                    deviceInfo.SN = SN;
                }*/

                mDevices.add(deviceInfo);
                if (mDeviceAdapter != null) {
                    mDeviceAdapter.notifyDataSetChanged();
                }

                mRobotLisenceView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onScanTimeOut() {
            //没有找到蓝牙跳转到二维码联网
            Logger.d(TAG, "[onScanTimeOut]");
            if (mDevices.size() > 0) {
                return;
            }
            Intent intent = new Intent(BLEConfigureActivity.this, NetworkConfigureActivity.class);
            intent.putExtra("step", STEP_TWO);
            intent.putExtra("robot_sn", mRobotSN);

            startActivity(intent);
            finish();
        }
    };

    private BLEManager.ConnectCallback mConnectCallback = new BLEManager.ConnectCallback() {

        @Override
        public void onConnectFailed() {
            if (isFinishing() || isDestroyed()) {
                return;
            }

            if (mStepsView.getCurrentStepIndex() >= STEP_FIVE) {
                return;
            }

            Logger.d(TAG, "[connectFailed]");
            if (mConnectFailedDialog == null) {
                mConnectFailedDialog = new CommonDiaglog(BLEConfigureActivity.this, false);
                mConnectFailedDialog.setMessase(getString(R.string.ble_connect_failed));
                mConnectFailedDialog.setPositiveClick(new CommonDiaglog.OnPositiveClick() {
                    @Override
                    public void OnPositiveClick() {
                        mConnectFailedDialog.dismiss();

                        mDeviceListView.setEnabled(true);
                        if (mCurrentViewHolder != null) {
                            mCurrentViewHolder.gifImageView.setVisibility(View.GONE);
                            mCurrentViewHolder.textView.setTextColor(getResources().getColor(R.color.text_black));
                        }

                        mTryOtherView.setVisibility(View.VISIBLE);
                    }
                });
                mConnectFailedDialog.setNegsitiveVisible(false);
                mConnectFailedDialog.setButtonText("", getString(R.string.ble_dialog_ok));
            }
            mConnectFailedDialog.show();
        }

        @Override
        public void onConnectSuccess(BluetoothDevice device) {
        }

        @Override
        public void onServiceSubscribed(boolean ready) {
            if (isFinishing() || isDestroyed()) {
                return;
            }
            Logger.d(TAG, "[connectSuccess]");
            if (mStepsView.getCurrentStepIndex() == STEP_TWO) {
                mStepsView.nextStep();
                LoadingDialog loadingDialog = LoadingDialog.getInstance(BLEConfigureActivity.this);
                loadingDialog.setCancelable(false);
                loadingDialog.show();
            }
        }

        @Override
        public void onReceive(String data) {
            if (isFinishing() || isDestroyed()) {
                return;
            }
            Logger.d(TAG, "[onReceive] data = " + data);
            BLECommandTool.CommonRsp commonRsp = BLECommandTool.unpackResponse(data);
            if (BLECommandTool.COMMAND_WIFI_LIST.equals(commonRsp.getCommand())) {
                if (mStepsView.getCurrentStepIndex() == STEP_THREE) {
                    LoadingDialog.dissMiss();
                    mWifiList = commonRsp.getAPList();
                    if (mWifiAdapter != null) {
                        mWifiAdapter.notifyDataSetChanged();
                    }
                }
            } else if (BLECommandTool.COMMAND_WIFI_SEND.equals(commonRsp.getCommand())) {
                if (mStepsView.getCurrentStepIndex() == STEP_FOUR) {
                    if ("1".equals(commonRsp.getResponseCode())) {
                        sendSuccess();
                    } else {
                        sendWifiInfoError();
                    }
                }
            }
        }
    };

    private static class DeviceInfo {
        BluetoothDevice device;
        String SN;
    }

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
                    Intent intent2 = new Intent(BLEConfigureActivity.this, MyDeviceActivity.class);
                    mContext.startActivity(intent2);

                    finish();
                    break;

                case BACK_TO_STEP_THREE: {
                    mStepsView.setCurrentStep(STEP_THREE);
                    break;
                }

                case MSG_TIMER_COUNT_DOWN: {
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
                }

                default:
                    break;
            }

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRobotSN = getIntent().getStringExtra("robot_sn");

        BLEManager.enableDebug(BuildConfig.DEBUG);//将log开关设置为一致
        mBLEManager = BLEManager.getInstance();
        mBLEManager.setConnectCallback(mConnectCallback);

        setContentView(R.layout.activity_ble_configure);

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

        mBLEManager.enableBluetoothIfDisabled(this);

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

                exitBLE();
                //等待蓝牙成功发送退出命令
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 500);

            }
        });

        mExitDialog.setButtonText(mContext.getString(R.string.common_btn_cancel),
                mContext.getString(R.string.common_btn_confirm));
    }

    public void onStart() {
        super.onStart();
        ((Alpha2Application) getApplication()).removeActivity(BindDialogActivity.class.getName());
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
                    onBackPressed();
                }
            });
        }
    }

    /**
     * 初始化所有步骤
     */
    private void setupStepsView() {
        setupStepOneView();
        setupStepTwoView();
        setupStepThreeView();
        setupStepFourView();
    }

    /**
     * 初始化步骤一
     */
    private void setupStepOneView() {
        mStepOneContentView = findViewById(R.id.step_one_view);
        mStepOneConfirmView = (TextView) findViewById(R.id.confirm_step_1);
        mStepOneConfirmView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBLEManager.isBluetoothEnable()) {
                    mStepsView.nextStep();
                } else {
                    mBLEManager.enableBluetoothIfDisabled(BLEConfigureActivity.this);
                }
            }
        });


        View scanHintView = mStepOneContentView.findViewById(R.id.scan_hint_view);
        scanHintView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BLEConfigureActivity.this, ListenVoiceGuideActivity.class));
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
     * 初始化步骤二
     */
    private void setupStepTwoView() {
        mStepTwoContentView = findViewById(R.id.step_two_view);
        View emptyView = mStepTwoContentView.findViewById(R.id.empty_view);
        mDeviceListView = (ListView) mStepTwoContentView.findViewById(R.id.device_list_view);
        mDeviceListView.setEmptyView(emptyView);

        mDeviceAdapter = new DeviceAdapter();
        mDeviceListView.setAdapter(mDeviceAdapter);
        mDeviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                DeviceInfo deviceInfo = mDevices.get(position);
                mBLEManager.connect(deviceInfo.device);

                mCurrentViewHolder = (ViewHolder) view.getTag();
                if (mCurrentViewHolder != null) {
                    mCurrentViewHolder.gifImageView.setVisibility(View.VISIBLE);
                }

                mCurrentViewHolder.textView.setTextColor(getResources().getColor(R.color.text_color_selected));

                mDeviceListView.setEnabled(false);

                //设置第三步的连接标题
                mConnectTitleView.setText(String.format(getString(R.string.ble_connect_title), mCurrentViewHolder.textView.getText()));
            }
        });

        mTryOtherView = mStepTwoContentView.findViewById(R.id.try_other_ways);
        mTryOtherView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BLEConfigureActivity.this, NetworkConfigureActivity.class);
                intent.putExtra("step", STEP_TWO);
                intent.putExtra("robot_sn", mRobotSN);

                startActivity(intent);
                finish();
            }
        });

        mRobotLisenceView = mStepTwoContentView.findViewById(R.id.robot_license);
    }

    /**
     * 初始化步三
     */
    private void setupStepThreeView() {
        mStepThreeContentView = findViewById(R.id.step_three_view);
        mNameInputView = (EditText) mStepThreeContentView.findViewById(R.id.name_input_view);
        mPasswdInputView = (EditText) mStepThreeContentView.findViewById(R.id.passwd_input_view);
        mPasswdDisplayView = (CheckBox) mStepThreeContentView.findViewById(R.id.passwd_display_view);

        mNameInputView.addTextChangedListener(mTextWatcher);//添加输入监听
        mPasswdInputView.addTextChangedListener(mTextWatcher);//添加输入监听

        mPasswdDisplayView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    mPasswdInputView.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mPasswdDisplayView.setButtonDrawable(getResources().getDrawable(R.drawable.ic_password_close));
                } else {
                    mPasswdInputView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mPasswdDisplayView.setButtonDrawable(getResources().getDrawable(R.drawable.btn_check_selector));
                }

                mPasswdInputView.setSelection(mPasswdInputView.getText().toString().length());

               SPUtils.get().put(Constants.WIFI_PSW_VISIBLE, isChecked);
            }
        });

        mConfirmStepThreeView = findViewById(R.id.confirm_step_3);
        mConfirmStepThreeView.setEnabled(false);
        mConfirmStepThreeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String wifiSSID = mNameInputView.getText().toString();
                String wifiPasswd = mPasswdInputView.getText().toString();
                String wifiCap = (String) mNameInputView.getTag();
                if (null != mWifiList && TextUtils.isEmpty(wifiCap)) {
                    for (int i = 0; i < mWifiList.size(); i++) {
                        if (wifiSSID.equals(mWifiList.get(i).getSSID())) {
                            wifiCap = mWifiList.get(i).getCapabilities();
                            break;
                        }
                    }
                }
                String wifiInfo = BLECommandTool.packSendWifiRequest(wifiSSID, wifiPasswd,
                        wifiCap == null ? DEFAULT_WIFI_CAPABILITY : wifiCap);
                mBLEManager.sendData(wifiInfo);

                mConnectErrorView.setVisibility(View.GONE);

                mStepsView.nextStep();
            }
        });

        View popupWifiView = mStepThreeContentView.findViewById(R.id.pop_wifi_view);
        popupWifiView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showWifiPopupView();
            }
        });

        mConnectTitleView = (TextView) mStepThreeContentView.findViewById(R.id.connect_title);
        mConnectErrorView = mStepThreeContentView.findViewById(R.id.connect_error_title);

        if ( SPUtils.get().getBoolean(Constants.WIFI_PSW_VISIBLE, false)) {
            mPasswdDisplayView.setChecked(false);
        } else {
            mPasswdDisplayView.setChecked(true);
        }

        if (mFirstLoad) {//第一次加载上次有保存wifi信息，则设置wifi信息
            mFirstLoad = false;
            String wifiSSIDValue = SPUtils.get().getString(Constants.WIFI_NAME,"");
            String passwdValue =  SPUtils.get().getString(Constants.WIFI_PSW,"");

            mNameInputView.setText(wifiSSIDValue);
            if(!TextUtils.isEmpty(wifiSSIDValue)) {
                mNameInputView.setSelection(wifiSSIDValue.length());
            }
            mPasswdInputView.setText(passwdValue);
            if(!TextUtils.isEmpty(passwdValue)) {
                mPasswdInputView.setSelection(passwdValue.length());
            }

        }

    }

    /**
     * wifi信息内部发生改变:用户输入
     */
    private void updateWifiInfoFromInner() {
        mPasswdInputView.setHint(R.string.wifi_input_psw);
        mPasswdInputView.setEnabled(true);
        mPasswdDisplayView.setVisibility(View.VISIBLE);

        String wifiSSIDValue = mNameInputView.getText().toString();
        String passwdValue = mPasswdInputView.getText().toString();
        if (!TextUtils.isEmpty(wifiSSIDValue) && !TextUtils.isEmpty(passwdValue)) {
            mConfirmStepThreeView.setEnabled(true);
        } else {
            mConfirmStepThreeView.setEnabled(false);
        }
    }

    private void showWifiPopupView() {
        if (isSoftShowing()) {
            /*软键盘消失后才能弹出popupwindow，不然会被顶上去*/
            final View decorView = getWindow().getDecorView();
            decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    Rect rect = new Rect();
                    decorView.getWindowVisibleDisplayFrame(rect);
                    int displayHight = rect.bottom - rect.top;
                    int hight = decorView.getHeight();
                    boolean visible = (double) displayHight / hight < 0.8;

                    if (!visible) {
                        decorView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        realShowWifiPopupView();
                    }
                }
            });

            hideSoftInput();
        } else {
            realShowWifiPopupView();
        }
    }

    private void realShowWifiPopupView() {
        Logger.d(TAG, "[showWifiPopupView] wifi size = " + mWifiList.size());
        if (mWifiList.size() == 0) {
            return;
        }

        if (mWifiPopupView == null) {
            View contentView = getLayoutInflater().inflate(R.layout.layout_popup_wifi_view, null);
            mWifiListView = (ListView) contentView;
            int width = mNameInputView.getMeasuredWidth();
            int height = getResources().getDimensionPixelOffset(R.dimen.wifi_popup_window_height);
            int totalItemHeight = getResources().getDimensionPixelOffset(R.dimen.input_wifi_edittext_height) * mWifiList.size();
            mWifiPopupView = new PopupWindow(contentView, width, Math.min(height, totalItemHeight), true);
            mWifiPopupView.setTouchable(true);
            mWifiPopupView.setOutsideTouchable(true);

            mWifiPopupView.setTouchInterceptor(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });

            mWifiPopupView.setBackgroundDrawable(getResources().getDrawable(R.drawable.spinner_shape));
        } else {
            int height = getResources().getDimensionPixelOffset(R.dimen.wifi_popup_window_height);
            int totalItemHeight = getResources().getDimensionPixelOffset(R.dimen.input_wifi_edittext_height) * mWifiList.size();
            mWifiPopupView.setHeight(Math.min(height, totalItemHeight));
        }

        if (mWifiAdapter == null) {
            mWifiAdapter = new WifiAdapter();
            mWifiListView.setAdapter(mWifiAdapter);

            mWifiListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> view, View itemView, int position, long id) {
                    mWifiPopupView.dismiss();
                    String wifiSSID = mWifiList.get(position).getSSID();

                    String wifiPrefSSID =  SPUtils.get().getString(Constants.WIFI_NAME);
                    String passwdPrefValue = SPUtils.get().getString(Constants.WIFI_PSW);
                    if(TextUtils.equals(wifiPrefSSID, wifiSSID)) {
                        mPasswdInputView.setText(passwdPrefValue);
                    }else if (!TextUtils.equals(wifiSSID, mNameInputView.getText().toString())) {
                        mPasswdInputView.setText("");
                    }

                    mNameInputView.setText(wifiSSID);
                    mNameInputView.setSelection(wifiSSID.length());

                    mNameInputView.setTag(mWifiList.get(position).getCapabilities());
                }
            });
        }

        mWifiPopupView.showAsDropDown(mNameInputView);
    }

    private void hideSoftInput() {
        Logger.d(TAG, "hideSoftInput");
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private boolean isSoftShowing() {
        //获取当前屏幕内容的高度
        int screenHeight = getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);

        return screenHeight - rect.bottom != 0;
    }


    /**
     * 初始化步骤四
     */
    private void setupStepFourView() {
        mStepFourContentView = findViewById(R.id.step_four_view);

        mStepConnectionContentView = findViewById(R.id.step_four_connecting_view);

        mConnectingView = mStepConnectionContentView.findViewById(R.id.connecting_view);
        mConnectFailedView = mStepConnectionContentView.findViewById(R.id.connect_failed_view);
    }

    @Override
    public void onBackPressed() {
        if(STEP_TWO == mStepsView.getCurrentStepIndex()) {
            mBLEManager.stopScan();
            mBLEManager.stopConnection();
            mStepsView.setCurrentStep(STEP_ONE);
            return;
        } else if (STEP_THREE == mStepsView.getCurrentStepIndex()) {
            mBLEManager.stopConnection();
            mStepsView.setCurrentStep(STEP_TWO);
            mPasswdDisplayView.setChecked(true);
            return;
        } else if(STEP_FOUR <= mStepsView.getCurrentStepIndex()) {
            mExitDialog.show();
            return;
        }

        exitBLE();
        //等待蓝牙成功发送退出命令
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 500);
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
        mStepConnectionContentView.setVisibility(View.GONE);
        mStepFourContentView.setVisibility(View.GONE);

        switch (stepIndex) {
            case STEP_ONE: {
                mStepOneContentView.setVisibility(View.VISIBLE);
                break;
            }
            case STEP_TWO: {
                mStepTwoContentView.setVisibility(View.VISIBLE);

                //设置列表可点击
                mDeviceListView.setEnabled(true);

                break;
            }
            case STEP_THREE: {
                mStepThreeContentView.setVisibility(View.VISIBLE);

                break;
            }
            case STEP_FOUR: {
                mStepConnectionContentView.setVisibility(View.VISIBLE);
                mConnectingView.setVisibility(View.VISIBLE);
                mConnectFailedView.setVisibility(View.GONE);
                break;
            }
            case STEP_FIVE: {
                LoadingDialog.dissMiss();
                mStepFourContentView.setVisibility(View.VISIBLE);
                String wifiSSIDValue = mNameInputView.getText().toString();
                String passwdValue = mPasswdInputView.getText().toString();
                SPUtils.get().put(Constants.WIFI_NAME, wifiSSIDValue);
                SPUtils.get().put(Constants.WIFI_PSW, passwdValue);
                break;
            }
        }

        /*第二步：扫描设备*/
        if (STEP_TWO == stepIndex) {
            mDevices.clear();
            mDeviceAddr.clear();
            if (mDeviceAdapter != null) {
                mDeviceAdapter.notifyDataSetChanged();
            }
            mBLEManager.scanDevices(DEFAULT_SCAN_TIME_OUT, mScanCallback);
        } else {
            mBLEManager.stopScan();
        }

        if (STEP_THREE == stepIndex) {
            String requestData = BLECommandTool.packWifiListRequest();
            mBLEManager.sendData(requestData);
        }
    }

    private void sendSuccess() {
        if (mStepsView != null) {
            mStepsView.setCurrentStep(STEP_COUNT);//成功
        }

        Logger.d("nxy", "[sendSuccess]");

        if ( SPUtils.get().getBoolean(PreferenceConstants.OPEN_AUTO_CONNECT, true)) {

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

        exitBLE();

        mHandler.sendEmptyMessageDelayed(SEND_INFO_SUCCESS, 3000);//3s之后跳转
    }

    /**
     * 连接成功进去主服务
     */
    private void goMainPage() {
        Logger.i("RobotManagerl:跳转");
        Intent intent = new Intent(mContext, MainPageActivity.class);
        startActivity(intent);

    }


    /**
     * 退出蓝牙
     * */
    private void exitBLE() {
        //发送退出蓝牙
        if(mBLEManager !=null) {
            mBLEManager.sendData(BLECommandTool.packExitRequest());
        }
    }

    /**
     * wifi信息有误
     */
    private void sendWifiInfoError() {
        mConnectErrorView.setVisibility(View.VISIBLE);

        mConnectingView.setVisibility(View.GONE);
        mConnectFailedView.setVisibility(View.VISIBLE);

        mHandler.sendEmptyMessageDelayed(BACK_TO_STEP_THREE, 1500);
    }

    private class DeviceAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mDevices == null ? 0 : mDevices.size();
        }

        @Override
        public Object getItem(int position) {
            return mDevices.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup group) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = getLayoutInflater().inflate(R.layout.ble_device_item_view, null);
                viewHolder.textView = (TextView) convertView.findViewById(R.id.device_view);
                viewHolder.gifImageView = (GifImageView) convertView.findViewById(R.id.device_loading_view);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.textView.setText(DEVICE_PREFIX + "_" + mDevices.get(position).SN);

            return convertView;
        }
    }

    private static class ViewHolder {
        TextView textView;
        GifImageView gifImageView;
    }

    private class WifiAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mWifiList == null ? 0 : mWifiList.size();
        }

        @Override
        public Object getItem(int position) {
            return mWifiList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup group) {
            WifiViewHolder wifiViewHolder;
            if (convertView == null) {
                wifiViewHolder = new WifiViewHolder();
                convertView = getLayoutInflater().inflate(R.layout.wifi_list_item_view, null);

                wifiViewHolder.textView = (TextView) convertView.findViewById(R.id.wifi_item_view);
                wifiViewHolder.imageView = (ImageView) convertView.findViewById(R.id.wifi_image_view);

                convertView.setTag(wifiViewHolder);
            } else {
                wifiViewHolder = (WifiViewHolder) convertView.getTag();
            }

            wifiViewHolder.textView.setText(mWifiList.get(position).getSSID());

            String capabilities = mWifiList.get(position).getCapabilities();

            if (!capabilities.contains("WPA") && !capabilities.contains("wpa")
                    && !capabilities.contains("WEP") && !capabilities.contains("wep")
                    && !capabilities.contains("EAP") && !capabilities.contains("EAP")) {
                wifiViewHolder.imageView.setImageDrawable(getResources().getDrawable(R.drawable.img_wifi_unlock));
            } else {
                wifiViewHolder.imageView.setImageDrawable(getResources().getDrawable(R.drawable.img_wifi_lock));
            }
            return convertView;
        }
    }

    private static class WifiViewHolder {
        TextView textView;
        ImageView imageView;
    }

    public void onDestroy() {
        super.onDestroy();
        if (mBLEManager != null) {
            mBLEManager.closeConnection();
        }

        if (mConnectFailedDialog != null) {
            mConnectFailedDialog.dismiss();
        }

        mHandler.removeCallbacksAndMessages(null);
    }
}
