package com.ubtechinc.alpha2ctrlapp.ui.activity.robot;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ubtech.utilcode.utils.ListUtils;
import com.ubtech.utilcode.utils.SPUtils;
import com.ubtech.utilcode.utils.TimeUtils;
import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2ActivityIntent;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.constants.IntentConstants;
import com.ubtechinc.alpha2ctrlapp.constants.PreferenceConstants;
import com.ubtechinc.alpha2ctrlapp.constants.RequestCodeConstants;
import com.ubtechinc.alpha2ctrlapp.data.robot.IRobotAuthorizeDataSource;
import com.ubtechinc.alpha2ctrlapp.data.robot.IRobotConfigDataSource;
import com.ubtechinc.alpha2ctrlapp.data.robot.IRobotInitDataSource;
import com.ubtechinc.alpha2ctrlapp.data.robot.RobotAuthorizeReponsitory;
import com.ubtechinc.alpha2ctrlapp.data.robot.RobotConfigRepository;
import com.ubtechinc.alpha2ctrlapp.data.robot.RobotInitRepository;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.AlphaParam;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.BluetoothDevice;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.RobotInfo;
import com.ubtechinc.alpha2ctrlapp.entity.request.DeleRequest;
import com.ubtechinc.alpha2ctrlapp.events.UpdateRobotInfoEvent;
import com.ubtechinc.alpha2ctrlapp.service.RobotManagerService;
import com.ubtechinc.alpha2ctrlapp.ui.activity.app.AlphaWareInfoActivity;
import com.ubtechinc.alpha2ctrlapp.ui.activity.app.ServiceLanguageActivity;
import com.ubtechinc.alpha2ctrlapp.ui.activity.app.SetAlphaOwnerNameActivity;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseContactActivity;
import com.ubtechinc.alpha2ctrlapp.ui.activity.main.MainPageActivity;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.app.AlphaSettingFragment;
import com.ubtechinc.alpha2ctrlapp.util.ImageLoad.LoadImage;
import com.ubtechinc.alpha2ctrlapp.util.WriteBookUtils;
import com.ubtechinc.alpha2ctrlapp.util.upload.IUploadResultListener;
import com.ubtechinc.alpha2ctrlapp.util.upload.QiniuUploader;
import com.ubtechinc.alpha2ctrlapp.util.upload.UploadCBHandler;
import com.ubtechinc.alpha2ctrlapp.util.upload.UploadType;
import com.ubtechinc.alpha2ctrlapp.widget.RoundImageView;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.CommonDiaglog;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.LoadingDialog;
import com.ubtechinc.nets.http.ThrowableWrapper;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.tencent.qalsdk.service.QalService.context;
import static com.ubtechinc.alpha2ctrlapp.ui.activity.main.MainPageActivity.alphaParam;

/*************************
 * @author
 * @date 2016/6/30
 * @Description Alpha设备功能管理
 * @modify
 * @modify_time
 **************************/
public class RobotSettingActivity extends BaseContactActivity implements
        OnClickListener, CommonDiaglog.OnPositiveClick, CommonDiaglog.OnNegsitiveClick {

    private String TAG = "RobotSettingActivity";
    private String alphaUserId;
    //Alpha2 Mac地址 目前等于JID
    String alphaMacAddress;
    private String mRobotSerialNo;
    private String[] alpha2MacList = new String[1];
    private TextView btn_release;
    private RelativeLayout settingNameLay, settingAlpha, setting_netLay;
    private EditText alphaNameTV;
    private TextView alphaId;
    public Bitmap mCurrentHeadimg;
    public String mCurrentHeadimgPath;
    private RoundImageView headerImage;
    private TextView btn_finish;
    private ImageView alphaNameImage;
    public CommonDiaglog dialog;
    private RobotInfo mRobotInfo;
    private int robotState;
    private TextView stastus;
    private boolean isEdit = false;
    private RelativeLayout playchargeLay;
    private RelativeLayout alphaSoftinfoLay;
    private TextView tvHardWare;
    private ImageView setChageButton;
    private ImageView setChatActionButton;
    private TextView tvChataction;

    private TextView playChargeTips, connect_bluetooth_tv;
    private TextView tv_robot_version;
    private boolean isOpen = false;
    private boolean isChatOpen = false;

    private RelativeLayout setOwnerNameLay;
    private TextView ownerNameTv;
    private TextView owerNameTipsTv;


    //机器人内存管理
    private RelativeLayout rlStorageManager;
    private ImageView ivLineStorageManager;
    private TextView tvStorageManager;
    //蓝牙设备连接状态
    private TextView connected_state_tv, tv_robot_version_prompt;
    private boolean isBluetoothConnected;
    private List<BluetoothDevice> mBluetoothDevices = new ArrayList<>();
    public static boolean isForeground = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robot_setting);
        this.mContext = this;
        initView();
    }

    private void initView() {

        robotState = getIntent().getIntExtra(IntentConstants.ROBOT_STATE, BusinessConstants.ROBOT_STATE_OFFLINE);
        mRobotInfo = (RobotInfo) getIntent().getSerializableExtra(IntentConstants.DATA_ROBOT_INFO);
        tv_robot_version = (TextView) findViewById(R.id.tv_robot_version);
        alphaUserId = getIntent().getStringExtra(PreferenceConstants.ALPHA_USER_ID);
        alphaMacAddress = getIntent().getStringExtra(Alpha2ActivityIntent.ALPHAMACADRESS);

        mRobotSerialNo = getIntent().getStringExtra(PreferenceConstants.ROBOT_SERIAL_NO);

        alpha2MacList[0] = alphaMacAddress;
        Alpha2Application.currentAlpha2Mac = alphaMacAddress;

        settingNameLay = (RelativeLayout) findViewById(R.id.setting_alpha_name);
        if (SPUtils.get().getBoolean(PreferenceConstants.IS_SHOW_DEVELOPER_SETTING, false)) {
            settingAlpha = (RelativeLayout) findViewById(R.id.setting_alpha);
            settingAlpha.setOnClickListener(this);
            settingAlpha.setVisibility(View.VISIBLE);
        }

        settingNameLay.setOnClickListener(this);
        alphaNameTV = (EditText) findViewById(R.id.alpha_name);
        alphaId = (TextView) findViewById(R.id.alpha_id);
        title = (TextView) findViewById(R.id.authorize_title);
        tv_robot_version_prompt = (TextView) findViewById(R.id.tv_robot_version_prompt);
        title.setText(mRobotInfo.getUserOtherName());
        title.setEms(5);
        alphaId.setText(mRobotSerialNo);
        alphaId.setOnClickListener(this);
        btn_release = (TextView) findViewById(R.id.btn_release);
        btn_release.setOnClickListener(this);
//		btn_back = (TextView) mContentView.findViewById(R.id.btn_back);
//		btn_back.setOnClickListener(this);
        headerImage = (RoundImageView) findViewById(R.id.header);
        btn_finish = (TextView) findViewById(R.id.btn_top_right);
        btn_finish.setText(R.string.common_btn_edit);
        connect_bluetooth_tv = (TextView) findViewById(R.id.connect_bluetooth_tv);
        alphaNameImage = (ImageView) findViewById(R.id.alpha_name_image);
        setting_netLay = (RelativeLayout) findViewById(R.id.setting_net);
        setting_netLay.setOnClickListener(this);
        setOwnerNameLay = (RelativeLayout) findViewById(R.id.setting_alpha_owner);
        setOwnerNameLay.setOnClickListener(this);
        owerNameTipsTv = (TextView) findViewById(R.id.ower_name_tips);
        ownerNameTv = (TextView) findViewById(R.id.ower_name);
        connected_state_tv = (TextView) findViewById(R.id.connected_state_tv);
        alphaNameTV.setEnabled(false);
        if (Constants.isOwner) {
            btn_finish.setVisibility(View.VISIBLE);
            alphaNameImage.setVisibility(View.INVISIBLE);
            btn_release.setText(getString(R.string.release_text));
        } else {
            btn_finish.setVisibility(View.GONE);
            alphaNameImage.setVisibility(View.INVISIBLE);
            alphaNameTV.setTextColor(mContext.getResources().getColor(R.color.edit_hint));
            btn_release.setText(getString(R.string.authorize_delete));
        }
        alphaNameTV.setText(mRobotInfo.getUserOtherName());
        LoadImage.LoadHeader(RobotSettingActivity.this, 5, headerImage, mRobotInfo.getUserImage());
        playchargeLay = (RelativeLayout) findViewById(R.id.playchargelay);
        findViewById(R.id.connet_bluetooth_layer).setOnClickListener(this);
        findViewById(R.id.rl_robot_version).setOnClickListener(this);
        alphaSoftinfoLay = (RelativeLayout) findViewById(R.id.alpha_device_lay);
        tvHardWare = (TextView) findViewById(R.id.tv_hardwareinfo);
        setChageButton = (ImageView) findViewById(R.id.playcharge_tg);
        playChargeTips = (TextView) findViewById(R.id.playchage_tips);

        rlStorageManager = (RelativeLayout) findViewById(R.id.rl_robot_storage_manager);
        ivLineStorageManager = (ImageView) findViewById(R.id.line_robot_storage_manager);
        tvStorageManager = (TextView) findViewById(R.id.tv_robot_storage_manager);

        rlStorageManager.setOnClickListener(this);
        setChatActionButton = (ImageView) findViewById(R.id.chat_action_tg);
        tvChataction = (TextView) findViewById(R.id.chat_action_tips);

        alphaSoftinfoLay.setOnClickListener(this);
        stastus = (TextView) findViewById(R.id.stastus);
        setChageButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (RobotManagerService.getInstance().isConnectedRobot()) {
                    if (isOpen) {
                        setChargeWhilePlayingState(false);
                        setChageButton.setImageResource(R.drawable.butn_close);
                        isOpen = false;
                    } else {
                        setChargeWhilePlayingState(true);
                        setChageButton.setImageResource(R.drawable.butn_open);
                        isOpen = true;
                    }
                } else {
                    ToastUtils.showShortToast(R.string.main_page_connect_alpha_tips);
                    setChageButton.setImageResource(R.drawable.butn_close);
                }

            }
        });

        setChatActionButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (RobotManagerService.getInstance().isConnectedRobot()) {
                    if (isChatOpen) {
                        setTTSWhileActiveState(false);
                        setChatActionButton.setImageResource(R.drawable.butn_close);
                        isChatOpen = false;
                    } else {
                        setTTSWhileActiveState(true);
                        setChatActionButton.setImageResource(R.drawable.butn_open);
                        isChatOpen = true;
                    }
                } else {
                    ToastUtils.showShortToast(R.string.main_page_connect_alpha_tips);
                    setChatActionButton.setImageResource(R.drawable.butn_close);
                }

            }
        });

        switch (robotState) {
            case BusinessConstants.ROBOT_STATE_OFFLINE:
                stastus.setText(R.string.devices_offline);
                stastus.setTextColor(getResources().getColor(R.color.device_offline));
                break;
            case BusinessConstants.ROBOT_STATE_ONLINE:
                stastus.setText(R.string.devices_online_status);
                stastus.setTextColor(getResources().getColor(R.color.device_online));
                break;
            case BusinessConstants.ROBOT_STATE_CONNECTED:
                stastus.setText(R.string.devices_using_status); // 自己用
                stastus.setTextColor(getResources().getColor(R.color.device_busy));
                break;

            default:
                break;
        }

        btn_finish.setOnClickListener(this);
        headerImage.setOnClickListener(this);
        dialog = new CommonDiaglog(this, false);
        if (Constants.isOwner) {
            dialog.setMessase(mContext.getString(R.string.release_alpha_tips));
        } else {
            dialog.setMessase(mContext.getString(R.string.authorize_delete_tip));
        }
        dialog.setNegsitiveClick(this);
        dialog.setPositiveClick(this);
        if (RobotManagerService.getInstance().isConnectedRobot() && alphaParam == null) {
            getAlphaParam();
        } else {
            getServiceLanguage();
        }

    }


    private void getServiceLanguage() {
        if (RobotManagerService.getInstance().isConnectedRobot() && alphaParam != null) {
            tv_robot_version_prompt.setTextColor(mContext.getResources().getColor(R.color.text_color_t2));
            if (alphaParam.getServiceLanguage().equalsIgnoreCase(BusinessConstants.ROBOT_SERVICE_LANGUAGE_CHINESE)) {
                tv_robot_version.setText(R.string.language_cn);
            } else {
                tv_robot_version.setText(R.string.language_en);
            }
        }

    }

    /**
     * 获取Alpha 的一些配置信息
     */
    private void getAlphaParam() {


        RobotInitRepository.getInstance().getRobotInitParam(new IRobotInitDataSource.GetInitDataCallback() {
            @Override
            public void onLoadInitData(AlphaParam alphaParam) {
                if (null != alphaParam) {
                    MainPageActivity.alphaParam = alphaParam;
                    if (isForeground) {
                        getServiceLanguage();
                    }
                }
            }

            @Override
            public void onDataNotAvailable(ThrowableWrapper e) {

            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        isForeground = true;
        if (RobotManagerService.getInstance().isConnectedRobot()) {
            connect_bluetooth_tv.setTextColor(mContext.getResources().getColor(R.color.text_color_t2));
            playChargeTips.setTextColor(mContext.getResources().getColor(R.color.text_color_t2));
            tvStorageManager.setTextColor(mContext.getResources().getColor(R.color.text_color_t2));
            tvHardWare.setTextColor(mContext.getResources().getColor(R.color.text_color_t2));
            getChargeWhilePlayingState();
            getTTSWhileActiveState();
            owerNameTipsTv.setTextColor(mContext.getResources().getColor(R.color.text_color_t2));
            tvChataction.setTextColor(mContext.getResources().getColor(R.color.text_color_t2));
            if (alphaParam != null) {
                ownerNameTv.setText(alphaParam.getMasterName());
            } else {
                ownerNameTv.setText("");
            }

        } else {
            connect_bluetooth_tv.setTextColor(mContext.getResources().getColor(R.color.text_color_t4));
            playChargeTips.setTextColor(mContext.getResources().getColor(R.color.text_color_t4));
            owerNameTipsTv.setTextColor(mContext.getResources().getColor(R.color.text_color_t4));
            tvChataction.setTextColor(mContext.getResources().getColor(R.color.text_color_t4));
            tvStorageManager.setTextColor(mContext.getResources().getColor(R.color.text_color_t4));
            tvHardWare.setTextColor(mContext.getResources().getColor(R.color.text_color_t4));
        }

        if (!Constants.isOwner) {
            rlStorageManager.setVisibility(View.GONE);
            ivLineStorageManager.setVisibility(View.GONE);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        isForeground = false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_top_right:
                doEdit();
                break;
            case R.id.btn_cancel:
                RobotSettingActivity.this.finish();
                break;
            case R.id.header:
                if (Constants.isOwner && isEdit) {
                    btn_finish.setVisibility(View.VISIBLE);
                    WriteBookUtils.choosePic(this);
                }
                break;
            case R.id.setting_alpha: {
                Intent intent = new Intent(this, AlphaSettingFragment.class);
                intent.putExtra(PreferenceConstants.ALPHA_USER_ID, alphaUserId);
                intent.putExtra(Alpha2ActivityIntent.ALPHAMACADRESS, alphaMacAddress);
                intent.putExtra(PreferenceConstants.ROBOT_SERIAL_NO, mRobotSerialNo);
                mContext.startActivity(intent);
            }

            break;
            case R.id.setting_alpha_name: {
            }
            break;
            case R.id.btn_release:
                if (Constants.isOwner) {
                    dialog.setMessase(mContext.getString(R.string.release_alpha_tips));
                } else {
                    dialog.setMessase(mContext.getString(R.string.authorize_delete_tip));
                }
                dialog.show();
                break;
            case R.id.btn_back:
                onBack(v);
                break;
            case R.id.alpha_name:
                break;
            case R.id.setting_net: {
                Intent intent = new Intent(mContext, ConfigureRobotNetworkActivity.class);
                intent.putExtra("isFromDevice", true);
                intent.putExtra(Constants.ROBOTSN, mRobotSerialNo);
                if (mRobotInfo.getMacAddress() != null && !TextUtils.isEmpty(mRobotInfo.getMacAddress())) {
                    intent.putExtra(Constants.ROBOT_MAC, (Serializable) mRobotInfo.getMacAddress());
                }
                startActivity(intent);
            }
            break;
            case R.id.alpha_device_lay:
                if (RobotManagerService.getInstance().isConnectedRobot()) {
                    Intent intent = new Intent(this, AlphaWareInfoActivity.class);
                    intent.putExtra(PreferenceConstants.ALPHA_USER_ID, alphaUserId);
                    intent.putExtra(Alpha2ActivityIntent.ALPHAMACADRESS, alphaMacAddress);
                    intent.putExtra(PreferenceConstants.ROBOT_SERIAL_NO, mRobotSerialNo);
                    mContext.startActivity(intent);
                } else {
                    ToastUtils.showShortToast(R.string.main_page_connect_alpha_tips);
                }
                break;
            case R.id.setting_alpha_owner:
                if (RobotManagerService.getInstance().isConnectedRobot()) {
                    Intent intent = new Intent(this, SetAlphaOwnerNameActivity.class);
                    intent.putExtra(PreferenceConstants.ALPHA_USER_ID, alphaUserId);
                    intent.putExtra(Alpha2ActivityIntent.ALPHAMACADRESS, alphaMacAddress);
                    intent.putExtra(PreferenceConstants.ROBOT_SERIAL_NO, mRobotSerialNo);
                    startActivityForResult(intent, RequestCodeConstants.EDIT_OWNER_NAME);
                } else {
                    ToastUtils.showShortToast(R.string.main_page_connect_alpha_tips);
                }
                break;
            case R.id.connet_bluetooth_layer:
                if (RobotManagerService.getInstance().isConnectedRobot()) {
                    Intent intent = new Intent(this, ConnectBluetoothActivity.class);
                    intent.putExtra(IntentConstants.ROBOT_BLUETOOTH_CONNECTION_STATE, isBluetoothConnected);
                    if (isBluetoothConnected && !ListUtils.isEmpty(mBluetoothDevices)) {
                        intent.putExtra(IntentConstants.ROBOT_BLUETOOTH_DEVICE, (Serializable) mBluetoothDevices.get(0));
                    }
                    startActivityForResult(intent, 100);
                } else {
                    ToastUtils.showShortToast(R.string.main_page_connect_alpha_tips);
                }

                break;
            case R.id.rl_robot_version:
                if (RobotManagerService.getInstance().isConnectedRobot() && alphaParam != null) {
                    Intent intent = new Intent(context, ServiceLanguageActivity.class);
                    startActivity(intent);
                } else {
                    ToastUtils.showShortToast(R.string.main_page_connect_alpha_tips);
                }
                break;
            case R.id.alpha_id:
                if (SPUtils.get().getBoolean(PreferenceConstants.IS_SHOW_DEVELOPER_SETTING, false)) {
                    ToastUtils.showShortToast("已经打开Alpha2设置");
                } else {
                    showDeveloperSetting();
                }
                break;
            case R.id.rl_robot_storage_manager:
                if (RobotManagerService.getInstance().isConnectedRobot()) {
                    startActivity(new Intent(this, StorageManagerActivity.class));
                } else {
                    ToastUtils.showShortToast(R.string.main_page_connect_alpha_tips);
                }
                break;
            default:
                break;
        }

    }

    private long lastClickTime = 0;
    private int count = 6;

    private void showDeveloperSetting() {

        if (lastClickTime <= 0) lastClickTime = TimeUtils.getCurrentTimeInLong();
        long current = TimeUtils.getCurrentTimeInLong();
        if (current - lastClickTime < 1000) {
            if (count <= 0) {
                SPUtils.get().put(PreferenceConstants.IS_SHOW_DEVELOPER_SETTING, true);
                ToastUtils.showShortToast("已经打开Alpha2设置");
                count = 0;
            } else {

                if (count <= 4) ToastUtils.showShortToast("再点击" + count + "次，打开Alpha2设置");
                count--;
            }

        } else {
            count = 6;
        }
        lastClickTime = current;
    }



    private void doEdit() {
        if (isEdit) {
            if (!TextUtils.isEmpty(alphaNameTV.getText().toString())) {
                if (alphaNameTV.getText().toString().length() < 3 || alphaNameTV.getText().toString().length() > 20) {
                    Toast.makeText(mContext, R.string.ui_user_length, Toast.LENGTH_SHORT).show();
                    return;
                }
                final String alphaName = alphaNameTV.getText().toString();
                LoadingDialog.getInstance(mContext).show();
                if (!TextUtils.isEmpty(mCurrentHeadimgPath)) {
                    uploadHeadImage(mCurrentHeadimgPath, new IUploadResultListener() {
                        @Override
                        public void onUploadSuccess(String url, UploadCBHandler uploadCBHandler) {
                            updateInfo(alphaName, url);
                        }

                        @Override
                        public void onUploadFail(String respInfo, UploadCBHandler uploadCBHandler) {
                            ToastUtils.showShortToast("头像上传失败");
                            LoadingDialog.dissMiss();
                        }
                    });
                } else {
                    updateInfo(alphaName, null);
                }

            } else {
                ToastUtils.showShortToast(R.string.ui_user_length);
            }
        } else {
            isEdit = true;
            btn_finish.setText(R.string.common_btn_finish);
            alphaNameTV.setEnabled(true);
        }


    }

    public void uploadHeadImage(String imagePath, IUploadResultListener listener) {
        UploadCBHandler handler = new UploadCBHandler();
        handler.filePath = imagePath;
        handler.type = UploadType.TYPE_ROBOT_HEAD_IMAGE;
        QiniuUploader.get().upload(handler, listener);
    }

    public void updateInfo(String alphaName, String userImage) {
        RobotConfigRepository.getInstance().updateInfo(mRobotSerialNo, alphaName, userImage,
                new IRobotConfigDataSource.UpdateInfoCallback() {
                    @Override
                    public void onSuccess(String robotSerialNo, String alphaName, String userImage) {
                        LoadingDialog.dissMiss();
                        ToastUtils.showShortToast(R.string.edit_info_success);
                        isEdit = false;
                        mCurrentHeadimgPath = null;
                        btn_finish.setText(R.string.common_btn_edit);
                        alphaNameTV.setEnabled(false);
                        if ((alphaName != null && !alphaName.equals(mRobotInfo.getUserOtherName()))
                                || (userImage != null && !userImage.equals(mRobotInfo.getUserImage()))) {
                            mRobotInfo.setUserOtherName(alphaName);
                            mRobotInfo.setUserImage(userImage);
                            EventBus.getDefault().post(new UpdateRobotInfoEvent(mRobotInfo));
                        }
                    }

                    @Override
                    public void onFail(ThrowableWrapper e) {
                        LoadingDialog.dissMiss();
                        ToastUtils.showShortToast(R.string.edit_info_failed);
                    }
                });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == WriteBookUtils.RESULT_CHOOSE_LOCAL_PITURE) {
            if (data != null) {
                Uri currImageURI = data.getData();
                if (currImageURI != null) {
                    mCurrentHeadimgPath = WriteBookUtils.getPath(this, currImageURI);
                    if (TextUtils.isEmpty(mCurrentHeadimgPath)) {
                        mCurrentHeadimgPath = currImageURI.getPath();
                    }
                    mCurrentHeadimg = WriteBookUtils.getBitmap(currImageURI, RobotSettingActivity.this);
                    headerImage.setImageBitmap(mCurrentHeadimg);
                }

            }

        } else if (requestCode == 100) {
            if (data.getBooleanExtra("isConnected", false)) {
                isBluetoothConnected = true;
                connected_state_tv.setText(R.string.bt_connected);
                mBluetoothDevices.clear();
                BluetoothDevice device = (BluetoothDevice) data.getSerializableExtra("device");
                if (device != null)
                    mBluetoothDevices.add(device);
            } else {
                isBluetoothConnected = false;
                connected_state_tv.setText(R.string.bt_tip);
            }
        } else if (requestCode == RequestCodeConstants.EDIT_OWNER_NAME && resultCode == RESULT_OK) {
            if (alphaParam != null) {
                alphaParam.setMasterName(data.getStringExtra(SetAlphaOwnerNameActivity.OWNER_NAME));
            }
        }
    }

    @Override
    public void OnNegsitiveClick() {
        // TODO Auto-generated method stub
    }

    @Override
    public void OnPositiveClick() {
        if (Constants.isOwner)
            doRelease(); // 主人解绑
        else {
            if (mRobotInfo != null) {
                if (robotState == 2) {// 用户现在正在使用，则先断开

                    RobotManagerService.getInstance().disConnect2Robot(mRobotSerialNo, new IRobotAuthorizeDataSource.IControlRobotCallback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onFail(ThrowableWrapper e) {

                        }
                    });
                    doDelete(mRobotInfo); // 授权者删除授权
                } else {
                    doDelete(mRobotInfo); // 授权者删除授权
                }
            }

        }

    }

    public void doDelete(RobotInfo info) {
        DeleRequest request = new DeleRequest();
        request.setRelationId(info.getRelationId());
        request.setToken(SPUtils.get().getString(PreferenceConstants.TOKEN));
        request.setUserId(info.getUserId());
        request.setEquipmentId(info.getEquipmentId());
        request.setEquipmentUserId(info.getEquipmentUserId() + "");
//        UserAction action = UserAction.getInstance(mActivity, null);
//        action.setParamerObj(request);
//        LoadingDialog.getInstance(mActivity).show();
//        UserAction.getInstance(mActivity, null).setHandler(mHandler);
//        action.doRequest(NetWorkConstant.REUQEST_DELETE_MY_AUTHORIZE, NetWorkConstant.deleteMyDevice);

    }

    public void doRelease() {
        // 接触授权前先Stop 机器人
        if (robotState == BusinessConstants.ROBOT_STATE_CONNECTED) {// 机器人忙碌状态就先断开
            RobotManagerService.getInstance().disConnect2Robot(mRobotSerialNo, new IRobotAuthorizeDataSource.IControlRobotCallback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFail(ThrowableWrapper e) {

                }
            });
        }


        RobotAuthorizeReponsitory.getInstance().unbindRobot(mRobotInfo.getEquipmentId(), Integer.valueOf(Alpha2Application.getAlpha2().getUserId()), new IRobotAuthorizeDataSource.UnBindRobotCallback() {
            @Override
            public void onSuccess() {
                LoadingDialog.dissMiss();
                ToastUtils.showShortToast(R.string.release_succes);
                if (RobotManagerService.getInstance().isConnectedRobot()) { // 连接状态下，将机器状态断开
                    RobotManagerService.getInstance().clearConnectCacheData();

                }
                RobotManagerService.getInstance().refreshDevices();// 解除绑定之后，刷新。如果不删除，能监听到花名册的变化也刷新的
                RobotSettingActivity.this.finish();
            }

            @Override
            public void onFail(ThrowableWrapper e) {
                LoadingDialog.dissMiss();
                ToastUtils.showShortToast(R.string.release_failed);
            }
        });

    }


    /**
     * 边冲边玩操作
     *
     * @param isOpen true 表示打开边冲边玩，false 表示关闭边冲边玩
     */
    private void setChargeWhilePlayingState(boolean isOpen) {
        LoadingDialog.getInstance(mContext).show();
        RobotConfigRepository.getInstance().setChargeWhilePlaying(isOpen, new IRobotConfigDataSource.SetChargeWhilePlayingCallback() {
            @Override
            public void onSuccess() {
                LoadingDialog.dissMiss();
            }

            @Override
            public void onFail(ThrowableWrapper e) {
                LoadingDialog.dissMiss();
            }
        });

    }


    private void getChargeWhilePlayingState() {
        LoadingDialog.getInstance(mContext).show();
        RobotConfigRepository.getInstance().getChargeWhilePlayingState(new IRobotConfigDataSource.GetChargeWhilePlayingStateCallback() {
            @Override
            public void onLoadChargeWhilePlayingState(boolean isOpen) {
                LoadingDialog.dissMiss();
                if (isOpen) {
                    setChageButton.setImageResource(R.drawable.butn_open);
                } else {
                    setChageButton.setImageResource(R.drawable.butn_close);
                }
            }

            @Override
            public void onDataNotAvailable(ThrowableWrapper e) {
                LoadingDialog.dissMiss();
            }
        });
    }

    private void setTTSWhileActiveState(boolean isOpen) {
        LoadingDialog.getInstance(mContext).show();
        RobotConfigRepository.getInstance().setTTSWhileActive(isOpen, new IRobotConfigDataSource.SetTTSActionStateCallback() {
            @Override
            public void onSuccess() {
                LoadingDialog.dissMiss();
                tvChataction.setTextColor(mContext.getResources().getColor(R.color.text_color_t2));
                setChatActionButton.setClickable(true);
            }

            @Override
            public void onFail(ThrowableWrapper e) {
                LoadingDialog.dissMiss();
            }
        });

    }


    private void getTTSWhileActiveState() {
        LoadingDialog.getInstance(mContext).show();
        RobotConfigRepository.getInstance().getTTSWhileActiveState(new IRobotConfigDataSource.GetTTSActionStateCallback() {
            @Override
            public void onLoadTTSActionState(boolean isOpen) {
                LoadingDialog.dissMiss();
                tvChataction.setTextColor(mContext.getResources().getColor(R.color.text_color_t2));
                setChatActionButton.setClickable(true);


                isChatOpen = isOpen;
                if (isChatOpen) {
                    setChatActionButton.setImageResource(R.drawable.butn_open);
                } else {
                    setChatActionButton.setImageResource(R.drawable.butn_close);
                }


            }

            @Override
            public void onDataNotAvailable(ThrowableWrapper e) {
                LoadingDialog.dissMiss();
            }
        });
    }


    @Override
    public boolean onKeyDown(int KeyCode, KeyEvent event) {

        if (KeyCode == event.KEYCODE_BACK) {
            onBack(null);
            return false;
        }

        return super.onKeyDown(KeyCode, event);
    }

    public void onBack(View v) {
        this.finish();

        /**
         * 隐藏键盘
         */
        if (getCurrentFocus() != null) {
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(getCurrentFocus()
                                    .getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
