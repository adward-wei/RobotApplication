package com.ubtechinc.alpha2ctrlapp.ui.activity.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ubtech.utilcode.utils.SPUtils;
import com.ubtech.utilcode.utils.StringUtils;
import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.constants.PreferenceConstants;
import com.ubtechinc.alpha2ctrlapp.service.RobotManagerService;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseContactActivity;
import com.ubtechinc.alpha2ctrlapp.util.DataCleanManager;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.LoadingDialog;

public class AppSettingActivity extends BaseContactActivity implements OnClickListener {

    private String TAG = "AppSettingActivity";
    private RelativeLayout clear_cache_lay, languageLay;
    private TextView cacheSize;
    private CheckBox wifi_tg, noty_tg;
    private TextView languageTv;
    private LinearLayout logLay;//查看异常状态布局
    private CheckBox autoConnectCb;// 自动连接的按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        this.mContext = this;
        initView();
    }

    public void initView() {
        clear_cache_lay = (RelativeLayout) findViewById(R.id.clear_cache_lay);
        wifi_tg = (CheckBox) findViewById(R.id.wifi_setting_tg);
        noty_tg = (CheckBox) findViewById(R.id.noty_tg);
        logLay = (LinearLayout) findViewById(R.id.log_lay);
        autoConnectCb = (CheckBox) findViewById(R.id.auto_connect_tg);
        cacheSize = (TextView) findViewById(R.id.cacheSize);
        languageLay = (RelativeLayout) findViewById(R.id.language_lay);
        languageTv = (TextView) findViewById(R.id.language_tv);
        try {
            cacheSize.setText(DataCleanManager.getCacheSize(mContext));
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        title = (TextView) findViewById(R.id.authorize_title);
        title.setText(getString(R.string.left_menu_setting));//settext(getstringres("left_menu_setting"))
        boolean downloadByWifi = SPUtils.get().getBoolean(Constants.downloadbyWifi, true);
        wifi_tg.setChecked(downloadByWifi);
        wifi_tg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                // TODO Auto-generated method stub
                if (arg1) {
                    SPUtils.get().put(Constants.downloadbyWifi, true);

                } else {
                    SPUtils.get().put(Constants.downloadbyWifi, false);

                }
            }
        });
        boolean msgTips = SPUtils.get().getBoolean(PreferenceConstants.IS_OPEN_MSG_TIPS, true);
        noty_tg.setChecked(msgTips);
        noty_tg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                // TODO Auto-generated method stub
                if (arg1) {
                    SPUtils.get().put(PreferenceConstants.IS_OPEN_MSG_TIPS, true);
                } else {
                    SPUtils.get().put(PreferenceConstants.IS_OPEN_MSG_TIPS, false);

                }
            }
        });
        boolean autoOpen = SPUtils.get().getBoolean(PreferenceConstants.OPEN_AUTO_CONNECT, true);
        autoConnectCb.setChecked(autoOpen);
        autoConnectCb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                // TODO Auto-generated method stub
                if (arg1) {
                    SPUtils.get().put(PreferenceConstants.OPEN_AUTO_CONNECT, true);
                } else {
                    SPUtils.get().put(PreferenceConstants.OPEN_AUTO_CONNECT, false);

                }
            }
        });

        String language = SPUtils.get().getString(Constants.APP_LAUNGUAGE, "");
        if (StringUtils.isEquals(language, getString(R.string.language_system)) || TextUtils.isEmpty(language)) {
            languageTv.setText(R.string.language_system);
        } else {

            if (StringUtils.isEquals(language, BusinessConstants.APP_LANGUAGE_EN) ) {
                languageTv.setText("English");
            } else if (StringUtils.isEquals(language, BusinessConstants.APP_LANGUAGE_CN) ) {

                languageTv.setText("简体中文");
            }else {
                languageTv.setText(language);
            }
        }
        logLay.setOnClickListener(this);
        clear_cache_lay.setOnClickListener(this);
        languageLay.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {

            case R.id.clear_cache_lay:
                LoadingDialog.getInstance(mContext).show();
                if (DataCleanManager.cleanInternalCache(mContext)) {
                    try {

                        cacheSize.setText(DataCleanManager.getCacheSize(mContext));
                        LoadingDialog.dissMiss();
                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        LoadingDialog.dissMiss();
//					e1.printStackTrace();
                    }
                } else if (DataCleanManager.cleanInternalCache2(mContext)) {
                    try {
                        cacheSize.setText(DataCleanManager.getCacheSize(mContext));
                        LoadingDialog.dissMiss();
                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        LoadingDialog.dissMiss();
//					e1.printStackTrace();
                    }
                } else {
                    LoadingDialog.dissMiss();
                    ToastUtils.showShortToast(  R.string.app_setting_clean_data_failed);
                }

                break;
            case R.id.devoloper_lay:
                break;
            case R.id.language_lay: {
                Intent intent = new Intent(mContext, LanguageSetting.class);
//                mActivity.startActivity(intent);
                openActivity(intent,mContext);
            }
            break;
            case R.id.log_lay:{
                if(!RobotManagerService.getInstance().isConnectedRobot()){
                    ToastUtils.showShortToast(  R.string.main_page_connect_alpha_tips);
                }else{
                    Intent intent = new Intent(mContext, CheckAlphaStatusActivity.class);
//                    mActivity.startActivity(intent);
                    openActivity(intent,mContext);
                }

            }
                break;
            default:
                break;
        }

    }

}
