package com.ubtechinc.alpha2ctrlapp.ui.activity.app;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.SPUtils;
import com.ubtech.utilcode.utils.TimeUtils;
import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha.im.IMCmdId;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.BaseHandler;
import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.data.robot.IRobotConfigDataSource;
import com.ubtechinc.alpha2ctrlapp.data.robot.IRobotInitDataSource;
import com.ubtechinc.alpha2ctrlapp.data.robot.RobotConfigRepository;
import com.ubtechinc.alpha2ctrlapp.data.robot.RobotInitRepository;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.AlphaParam;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.GetLogByMobileEntrity;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseContactActivity;
import com.ubtechinc.alpha2ctrlapp.ui.activity.main.MainPageActivity;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.LoadingDialog;
import com.ubtechinc.nets.http.ThrowableWrapper;

import java.util.Locale;

public class AlphaWareInfoActivity extends BaseContactActivity implements
        OnClickListener {

    private String TAG = "AlphaWareInfoActivity";
    private TextView serviceVersion, chestVersion, headerVersion, androidVersion, sdInfo;
    private TextView headerTv;
    private TextView tvUploadLog;
    private TextView title;
    private LinearLayout back;
    private static final String UPLOAD_LOG_TIME = "uploadtime";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alpha_soft_info);
        this.mContext = this;
        initView();

    }

    public void initView() {
        serviceVersion = (TextView) findViewById(R.id.sevice_version);
        chestVersion = (TextView) findViewById(R.id.chest_version);
        headerVersion = (TextView) findViewById(R.id.head_version);
        androidVersion = (TextView) findViewById(R.id.android_version);
        sdInfo = (TextView) findViewById(R.id.sd_info);
        title = (TextView) findViewById(R.id.authorize_title);
        title.setText(R.string.device_soft_hardwareinfo);
        back = (LinearLayout) findViewById(R.id.btn_back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        headerTv = (TextView) findViewById(R.id.head_tv);
        tvUploadLog = (TextView) findViewById(R.id.btn_top_right);
        tvUploadLog.setText(R.string.tv_upload_log_cmd);
        tvUploadLog.setVisibility(View.VISIBLE);
        tvUploadLog.setEnabled(true);
        tvUploadLog.setTextColor(AlphaWareInfoActivity.this.getResources().getColor(R.color.app_detail_ll));
        tvUploadLog.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tvUploadLog.setEnabled(false);
                tvUploadLog.setTextColor(AlphaWareInfoActivity.this.getResources().getColor(R.color.white));
                doUploadLogAction();
            }
        });
    }

    private class FunctionHandler extends BaseHandler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // TODO Auto-generated method stub
            if (msg == null) {
                Logger.i( "handleMessage msg is null.");
                return;
           }
            switch (msg.what) {
                case  IMCmdId.IM_GET_ROBOT_INIT_STATUS_REQUEST:
                    LoadingDialog.dissMiss();
                    AlphaParam param = (AlphaParam) msg.obj;
                    refreshInfo(param);
                    break;

            }
        }
    }


    private boolean isZh() {
        Locale locale = getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.endsWith("zh"))
            return true;
        else
            return false;
    }

    private void refreshInfo(AlphaParam data) {
        serviceVersion.setText(data.getServiceVersionName());
        chestVersion.setText(data.getChestVersion());
        headerVersion.setText(data.getHeaderVersion());
        androidVersion.setText(data.getAndroidVersion());
        sdInfo.setText(data.getSDTotalVolume() / 1024 / 1024 / 1024 + "G" + "/" + data.getSDAvailableVolume() / 1024 / 1024 / 1024 + "G");
        if (TextUtils.isEmpty(data.getHeaderVersion())) {
            headerTv.setVisibility(View.GONE);
        } else {
            headerTv.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        getInfo();
    }


    private void getInfo() {

        if (MainPageActivity.alphaParam != null && MainPageActivity.alphaParam.getServiceVersionName() != null) {
            refreshInfo(MainPageActivity.alphaParam);
        } else {
            RobotInitRepository.getInstance().getRobotInitParam(new IRobotInitDataSource.GetInitDataCallback() {
                @Override
                public void onLoadInitData(AlphaParam alphaParam) {
                    LoadingDialog.dissMiss();
                    MainPageActivity.alphaParam = alphaParam;

                    refreshInfo(alphaParam);
                }

                @Override
                public void onDataNotAvailable(ThrowableWrapper e) {

                }
            });
        }
    }


    private void doUploadLogAction() {


        RobotConfigRepository.getInstance().startUploadLog( new IRobotConfigDataSource.StartUploadLogCallback() {
            @Override
            public void onSuccess(GetLogByMobileEntrity getLogByMobileEntrity) {
                int time = getLogByMobileEntrity.getMinute2wait();
                if (time > 5) {
                    Toast.makeText(AlphaWareInfoActivity.this, AlphaWareInfoActivity.this.getString(R.string.upload_success), Toast.LENGTH_LONG).show();
                } else {
                    String mCurrentSetLanguage = SPUtils.get().getString( Constants.APP_LAUNGUAGE, "");
                    if (mCurrentSetLanguage.equals(BusinessConstants.APP_LANGUAGE_EN) || !isZh()) {

                        ToastUtils.showLongToast( getResources().getString(R.string.upload_log_error) + getLogByMobileEntrity.getMinute2wait()
                                + "minutes");
                    } else if (mCurrentSetLanguage.equals(BusinessConstants.APP_LANGUAGE_CN) || (TextUtils.isEmpty(mCurrentSetLanguage) && isZh())) {
                        ToastUtils.showLongToast(getString(R.string.upload_log_error_top) + getLogByMobileEntrity.getMinute2wait()
                                + getString(R.string.upload_log_error_bottom));
                    }
                    tvUploadLog.setEnabled(true);
                    tvUploadLog.setTextColor(AlphaWareInfoActivity.this.getResources().getColor(R.color.app_detail_ll));
                }
            }

            @Override
            public void onFail(ThrowableWrapper e) {

            }
        });

       SPUtils.get().put(UPLOAD_LOG_TIME, TimeUtils.getCurrentTimeInLong());
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }


}
