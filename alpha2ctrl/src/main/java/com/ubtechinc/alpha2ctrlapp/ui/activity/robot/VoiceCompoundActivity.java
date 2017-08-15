package com.ubtechinc.alpha2ctrlapp.ui.activity.robot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.StringUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.base.BaseHandler;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.constants.MessageType;
import com.ubtechinc.alpha2ctrlapp.data.robot.IRobotVoiceDataSource;
import com.ubtechinc.alpha2ctrlapp.data.robot.RobotVoiceRepository;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.TranslationBean;
import com.ubtechinc.alpha2ctrlapp.service.RobotManagerService;
import com.ubtechinc.alpha2ctrlapp.third.AidlService;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseContactActivity;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.ConnectRobotDialog;
import com.ubtechinc.nets.http.ThrowableWrapper;

import java.util.Timer;
import java.util.TimerTask;

public class VoiceCompoundActivity extends BaseContactActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, ConnectRobotDialog.OnPositiveClick {

    private static final String TAG = VoiceCompoundActivity.class.getSimpleName();
    private EditText mEditText;
    private TextView tvEditSize;
    private TextView tvVoiceTip;
    private RadioGroup mRadioGroup;
    private RadioButton mChRadioButton;
    private RadioButton mEnRadioButton;
    private BaseHandler mHandler;
    private TextView tvActionName;
    private String selectedActionName;//动作名称
    private String selectedActionId = "";//动作ID
    private boolean isTranslate;

    private int contentType;// 0默认 1中文 2英文
    private static final int radio_show_type_none = 0; //radio置灰
    private static final int radio_show_type_ch = 1;//radio选中中文
    private static final int radio_show_type_en = 2;//radio选中英文

    private final static int REQUESTCODE = 1; // 返回的结果码
    private ConnectRobotDialog mConnectRobotDialog;
    private UnLineBroadcastReceiver mUnLineBroadcastReceiver;
    private RelativeLayout rlSend;
    private TextView tvSend;
    private LinearLayout llSendWait;
    private TextView tvWaitTime;
    private Timer mTimer = null;
    private TimerTask mTimerTask = null;
    private static int delay = 1000;  //1s
    private static int period = 1000;  //1s
    private static int SEND_TIME=15;
    private static int count = 15;
    private boolean isSending = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_compound);
        initView();
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        IntentFilter intentFilter = new IntentFilter(AidlService.RECEIVE_ERROR_DATA);
        mUnLineBroadcastReceiver = new UnLineBroadcastReceiver();
        registerReceiver(mUnLineBroadcastReceiver, intentFilter);
    }

    @Override
    public void onResume() {
        super.onResume();
        Alpha2Application.getInstance().setFromAction(true);
        Alpha2Application.getInstance().setShopAction(true);
    }

    /**
     * 初始化数据
     */
    private void initView() {
        mConnectRobotDialog = new ConnectRobotDialog(this);
        mConnectRobotDialog.setCanceledOnTouchOutside(false);
        mConnectRobotDialog.setPositiveClick(this);
        title = (TextView) findViewById(R.id.authorize_title);
        title.setText(this.getString(R.string.voice_compound_title));
        title.setTextSize(18);
        rlSend = (RelativeLayout) findViewById(R.id.rl_send);
        rlSend.setEnabled(false);
        tvSend = (TextView) findViewById(R.id.tv_send);
        llSendWait = (LinearLayout) findViewById(R.id.ll_send_wait);
        tvWaitTime = (TextView) findViewById(R.id.tv_wait_time);
        tvWaitTime.setText("(20" + getString(R.string.voice_compound_tv_btn_time) + ")");
        rlSend.setOnClickListener(this);
        mEditText = (EditText) findViewById(R.id.edit_content);
        tvEditSize = (TextView) findViewById(R.id.tv_edit_size);
        tvVoiceTip = (TextView) findViewById(R.id.tv_voice_tip);
        mRadioGroup = (RadioGroup) findViewById(R.id.lan_radiogroup);
        mChRadioButton = (RadioButton) findViewById(R.id.radio_ch);
        mEnRadioButton = (RadioButton) findViewById(R.id.radio_en);
        mRadioGroup.setEnabled(false);
        mChRadioButton.setEnabled(false);
        mEnRadioButton.setEnabled(false);
        tvActionName = (TextView) findViewById(R.id.tv_voice_action_name);
        tvActionName.setOnClickListener(this);
        mEditText.clearFocus();
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String content = mEditText.getText().toString();
                tvEditSize.setText(content.length() + "/50字");

                if (content.length() == 0) {
                    setRadioType(radio_show_type_none);
                    rlSend.setEnabled(false);
                }
                if (content.length() == 50) {
                    tvEditSize.setTextColor(VoiceCompoundActivity.this.getResources().getColor(R.color.txt_title_red));
                } else {
                    tvEditSize.setTextColor(VoiceCompoundActivity.this.getResources().getColor(R.color.bg_color_b1));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = mEditText.getText().toString();
                mRadioGroup.clearCheck();
//                if (!StringUtil.isContainChinese(content) && !StringUtil.isContainsEnglish(content) && !StringUtil.isContainsNumeric(content)) {
//                    setRadioType(radio_show_type_none);
//                    rlSend.setEnabled(false);
//                } else if (!StringUtil.isContainChinese(content) && StringUtil.isContainsEnglish(content)) {
//                    setRadioType(radio_show_type_en);
//                    rlSend.setEnabled(true);
//                } else if (StringUtil.isContainChinese(content) && !StringUtil.isContainsEnglish(content)) {
//                    setRadioType(radio_show_type_ch);
//                    rlSend.setEnabled(true);
//                } else if (!StringUtil.isContainChinese(content) && !StringUtil.isContainsEnglish(content) && StringUtil.isContainsNumeric(content)) {
//                    setRadioType(radio_show_type_ch);
//                    rlSend.setEnabled(true);
//                } else if (StringUtil.isContainChinese(content) && StringUtil.isContainsEnglish(content) && StringUtil.isContainsNumeric(content)) {
//                    setRadioType(radio_show_type_none);
//                    rlSend.setEnabled(true);
//                }else if (StringUtil.isContainChinese(content) && StringUtil.isContainsEnglish(content)) {
//                    setRadioType(radio_show_type_none);
//                    rlSend.setEnabled(true);
//                }

                if (!StringUtils.isContainChinese(content) && StringUtils.isContainsEnglish(content)) {
                    setRadioType(radio_show_type_en);

                } else if (StringUtils.isContainChinese(content) && !StringUtils.isContainsEnglish(content)) {
                    setRadioType(radio_show_type_ch);
                } else {
                    setRadioType(radio_show_type_none);
                }
            }
        });

        mRadioGroup.setOnCheckedChangeListener(this);
        if (!RobotManagerService.getInstance().isConnectedRobot()) {
            mConnectRobotDialog.show();
        }
    }

    private void setRadioType(int type) {
        if (!isSending) {
            rlSend.setEnabled(true);
        }
        switch (type) {
            case radio_show_type_none: //禁止点击
                contentType = radio_show_type_none;
                mRadioGroup.setEnabled(false);
                mChRadioButton.setEnabled(false);
                mEnRadioButton.setEnabled(false);
                mChRadioButton.setChecked(false);
                mEnRadioButton.setChecked(false);
                break;

            case 1://选中中文
                mRadioGroup.setEnabled(true);
                mChRadioButton.setEnabled(true);
                mEnRadioButton.setEnabled(true);
                mChRadioButton.setChecked(true);
                mEnRadioButton.setChecked(false);
                break;

            case 2://选中英文
                mRadioGroup.clearCheck();
                mRadioGroup.setEnabled(true);
                mChRadioButton.setEnabled(true);
                mEnRadioButton.setEnabled(true);
                mChRadioButton.setChecked(false);
                mEnRadioButton.setChecked(true);
                break;
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == mChRadioButton.getId()) {
            contentType = radio_show_type_ch;
        } else if (checkedId == mEnRadioButton.getId()) {
            contentType = radio_show_type_en;
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_voice_action_name:
                Intent intent = new Intent(this, VoiceAddActionActivity.class);
                if (TextUtils.isEmpty(selectedActionName)) {
                    selectedActionName = getString(R.string.voice_compound_tv_action_none);
                }
                intent.putExtra(Constants.intent_voice_compound_position_data, selectedActionName);
                startActivityForResult(intent, Constants.intent_voice_compound_resultcode);
                break;
            case R.id.rl_send:
                String content = mEditText.getText().toString();
                if (!RobotManagerService.getInstance().isConnectedRobot()) {
                    if (null == mConnectRobotDialog) {
                        mConnectRobotDialog = new ConnectRobotDialog(this);
                        mConnectRobotDialog.setPositiveClick(this);
                    }
                    mConnectRobotDialog.show();
                } else {
                    if (!TextUtils.isEmpty(content)) {
                        sendData(content);
                    }
                }
                break;
        }
    }

    private void sendData(String content) {
        isSending = true;
        tvSend.setVisibility(View.GONE);
        llSendWait.setVisibility(View.VISIBLE);
        rlSend.setEnabled(false);
        tvActionName.setEnabled(false);
        count =SEND_TIME;
        stopTimer();
        startTimer();
        if (!StringUtils.isContainChinese(content) && StringUtils.isContainsEnglish(content) && contentType == radio_show_type_ch) {
            isTranslate = true;
        } else if (StringUtils.isContainChinese(content) && !StringUtils.isContainsEnglish(content) && contentType == radio_show_type_en) {
            isTranslate = true;
        } else {
            isTranslate = false;
        }
        TranslationBean translationBean = new TranslationBean();

        translationBean.setContent(content);
        if (contentType == 2) {
            translationBean.setLanguage("en");
        } else {
            translationBean.setLanguage("zh");
        }
        if (isTranslate) {
            translationBean.setNeedTranslate("1");
        } else {
            translationBean.setNeedTranslate("0");
        }
        if (!TextUtils.isEmpty(selectedActionId)) {
            translationBean.setActionId(selectedActionId);
        }

        RobotVoiceRepository.getInstance().startVoiceCompound(translationBean, new IRobotVoiceDataSource.StartVoiceCompoundCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFail(ThrowableWrapper e) {

            }
        });

        Logger.i(TAG, content + "是否要翻译" + isTranslate + "  type" + contentType + "id==" + selectedActionId);
    }

    /**
     * 动作列表回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constants.intent_voice_compound_resultcode) {
            selectedActionName = data.getStringExtra(Constants.intent_voice_compound_data_action_name);
            String id = data.getStringExtra(Constants.intent_voice_compound_data_action_id);
            if (!TextUtils.isEmpty(id)) {
                selectedActionId = id;
            }
            if (!TextUtils.isEmpty(selectedActionName)) {
                tvActionName.setText(selectedActionName);
            }
        } else if (resultCode == Constants.intent_voice_compound_resultcode_three) {
            OnPositiveClick();
        }
    }

    @Override
    public void OnPositiveClick() {
        Intent intent2 = new Intent(VoiceCompoundActivity.this, MyDeviceActivity.class);
        mContext.startActivity(intent2);
        ((Alpha2Application) VoiceCompoundActivity.this.getApplication()).removeActivity();
    }


    private class MovementHandler extends BaseHandler {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            if (msg == null) {

                return;
            }
            switch (msg.what) {
                case MessageType.ALPHA_LOST_CONNECTED:
                    Logger.i(TAG, "handleMessage ALPHA_LOST_CONNECTED.");
                    isCurrentAlpha2MacLostConnection((String) msg.obj);
                    break;
                case 1001:
                    tvWaitTime.setText("(" + count + VoiceCompoundActivity.this.getString(R.string.voice_compound_tv_btn_time) + ")");
                    if (count == 0) {
                        tvSend.setVisibility(View.VISIBLE);
                        tvWaitTime.setText("("+SEND_TIME + getString(R.string.voice_compound_tv_btn_time) + ")");
                        llSendWait.setVisibility(View.GONE);
                        rlSend.setEnabled(true);
                        isSending = false;
                        tvActionName.setEnabled(true);
                        stopTimer();
                        if (TextUtils.isEmpty(mEditText.getText().toString())) {
                            rlSend.setEnabled(false);
                        }
                    }
                    break;
            }
        }
    }

    /**
     * 广播接收者机器人掉线通知
     *
     * @author weijiang204321
     */
    class UnLineBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra(AidlService.AIDL_RECEIVE_ERROR_CODE).equals(context.getString(R.string.main_page_alpha2_offline))) {
                mConnectRobotDialog.show();
            }
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mUnLineBroadcastReceiver);
        stopTimer();
    }

    private void startTimer() {
        if (mTimer == null) {
            mTimer = new Timer();
        }

        if (mTimerTask == null) {
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    Log.i(TAG, "count: " + String.valueOf(count));
                    mHandler.sendEmptyMessage(1001);
                    count--;
                }
            };
        }

        if (mTimer != null && mTimerTask != null)
            mTimer.schedule(mTimerTask, delay, period);

    }

    private void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
        count = SEND_TIME;
    }
}
