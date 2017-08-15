package com.ubtechinc.alpha2ctrlapp.ui.activity.robot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.data.robot.IRobotAuthorizeDataSource;
import com.ubtechinc.alpha2ctrlapp.data.robot.RobotAuthorizeReponsitory;
import com.ubtechinc.alpha2ctrlapp.entity.request.RegistRobotRequest;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseContactActivity;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.LoadingDialog;
import com.ubtechinc.nets.http.ThrowableWrapper;


/**
 * Created by ubt on 2017/2/14.
 */

public class BindDialogActivity extends BaseContactActivity {
    private static final String TAG = "BindDialogActivity";

    private String robotMac = "";

    /**
     * 机器人序列号
     */
    private String mRobotSerialNo;

    /**
     * 绑定容器控件
     */
    private View mBindContentView;
    /**
     * 被绑定容器控件
     */
    private View mBindFailedContentView;
    /**
     * 其他异常容器控件
     */
    private View mUnknownErrorContentView;
    /**
     * 其他异常提示控件
     */
    private TextView mUnknownTipsView;
    /**
     * loading框控件
     */
    private View mLoadingDialogView;

    /**
     * 被绑定用户
     */
    private TextView mBindErrorTipsView;
    private View mBindContainer;


    private boolean mWillFinishParent = false;


    private static final String FINISH_ACTION = "com.ubt.action_finish";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_bind_dialog);
        mBindContainer = findViewById(R.id.bind_container);
        mBindContentView = findViewById(R.id.bind_content_view);
        mBindFailedContentView = findViewById(R.id.bind_failed_content_view);
        mUnknownErrorContentView = findViewById(R.id.unknown_error_content_view);

        mLoadingDialogView = findViewById(R.id.loading_dialog);

        Intent data = getIntent();
        String info = (String) data.getExtras().get(
                Constants.Result_text);
        /*序列号控件*/
        TextView robotSNView = (TextView) findViewById(R.id.robot_sn_view);
        try {
            if (info.split("robotSeq=").length == 2) {
                mRobotSerialNo = info.split("robotSeq=")[1].trim();
                if (mRobotSerialNo.length() >= 5) {
                    robotSNView.setText(mRobotSerialNo.substring(0, mRobotSerialNo.length() - 4));
                }
            } else {
                ToastUtils.showShortToast(R.string.bind_scan_robot_qr);
                finish();
            }
        }catch (Exception e) {
            ToastUtils.showShortToast(R.string.bind_scan_robot_qr);
            finish();
            e.printStackTrace();
        }


        /*取消按钮*/
        View cancelBtn = findViewById(R.id.btn_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /*绑定按钮*/
        View bindBtn = findViewById(R.id.btn_bind_robot);
        bindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerRobot();
            }
        });

        mBindErrorTipsView = (TextView) findViewById(R.id.bind_error_tips);

        View mOkBtn = findViewById(R.id.btn_ok);
        mOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }
        });

        mUnknownTipsView = (TextView) findViewById(R.id.unknown_error_tips);
        View mCancelView = findViewById(R.id.btn_error_cancel);
        mCancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer errorCode = (Integer) mUnknownTipsView.getTag();
                if (errorCode != null) {
                    if (errorCode.intValue() != 10002) {
                        setResult(RESULT_OK);
                    }
                }
                finish();
            }
        });

        mWillFinishParent = false;
        Alpha2Application.registerActivity(this);
    }

    /**
     * 显示未知错误对话框
     *
     * @param errorCode 2001 或 5003 (设备)序列号不存在 10002 未知异常
     */
    private void showUnknownErrorTips(int errorCode) {
        mBindContentView.setVisibility(View.GONE);
        mBindFailedContentView.setVisibility(View.GONE);
        mUnknownErrorContentView.setVisibility(View.VISIBLE);
        mLoadingDialogView.setVisibility(View.GONE);

        mUnknownTipsView.setTag(errorCode);

        switch (errorCode) {
            case 2001:
            case 5003: {
                mUnknownTipsView.setText(R.string.bind_invalid_sn);
                break;
            }
            case 5005: {
                mUnknownTipsView.setText(R.string.error_message_5005);
                break;
            }
            default: {
                mUnknownTipsView.setText(R.string.bind_unknown_error);
            }
        }
    }

    private void showErrorTips(String name) {
        String newName = "";
        int length = name.length();
        if (length >= 1) {
            newName += name.charAt(0);
        }
        for (int i = 1; i < length - 1; i++) {
            newName += "*";
        }
        newName += name.charAt(length - 1);
        mBindContentView.setVisibility(View.GONE);
        mBindFailedContentView.setVisibility(View.VISIBLE);
        mUnknownErrorContentView.setVisibility(View.GONE);
        mLoadingDialogView.setVisibility(View.GONE);
        mBindErrorTipsView.setText(String.format(getString(R.string.contact_to_deregister), newName));
    }

    private void registerRobot() {
        mBindContentView.setVisibility(View.GONE);
        mBindContainer.setVisibility(View.INVISIBLE);
        LoadingDialog.getInstance(this).show();
        RegistRobotRequest request = new RegistRobotRequest();
        request.setUserName(mRobotSerialNo);
        request.setActiveArea(Constants.geoLat + "," + Constants.geoLng);

        RobotAuthorizeReponsitory.getInstance().bindRobot(mRobotSerialNo, Alpha2Application.getAlpha2().getUserId(), new IRobotAuthorizeDataSource.BindRobotCallback() {
            @Override
            public void onSuccess(String macAddress) {
                robotMac = macAddress;
                ToastUtils.showShortToast(R.string.bind_success);
                LoadingDialog.dissMiss();
                alphaConnectNet();
                mWillFinishParent = true;
            }

            @Override
            public void onFail(ThrowableWrapper e) {
                ToastUtils.showShortToast(e.getMessage());
                LoadingDialog.dissMiss();
                finish();
            }
        });

    }

    private void alphaConnectNet() {
        Intent intent = new Intent(mContext, ConfigureRobotNetworkActivity.class);
        intent.putExtra(Constants.ROBOTSN, mRobotSerialNo);
        intent.putExtra(Constants.ROBOT_MAC, robotMac);
        startActivity(intent);
    }


    public void onDestroy() {
        super.onDestroy();
        if (mWillFinishParent) {
            sendBroadcast(new Intent(FINISH_ACTION));
        }
    }
}
