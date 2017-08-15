package com.ubtechinc.alpha2ctrlapp.ui.activity.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.NetworkUtils;
import com.ubtech.utilcode.utils.SPUtils;
import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.BaseHandler;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.constants.NetWorkConstant;
import com.ubtechinc.alpha2ctrlapp.entity.CheckAppUpdate;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseContactActivity;
import com.ubtechinc.alpha2ctrlapp.ui.activity.main.MainPageActivity;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.app.HelpActivity;
import com.ubtechinc.alpha2ctrlapp.util.DownloadTask;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.CommonDiaglog;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.LoadingDialog;

public class AboutActivity extends BaseContactActivity implements View.OnClickListener, CommonDiaglog.OnPositiveClick, CommonDiaglog.OnNegsitiveClick {

    private String TAG = "AboutActivity";
    private TextView appVersionTv;
    private RelativeLayout app_update_lay, feedback_lay, helplay;

    private TextView versionTv;
    private String versionPath = null;
    private ProgressDialog downloadDialog = null;
    private boolean hasClick = false;
    private boolean hasNew = false;
    private CommonDiaglog dialog;
    private String verLocal;
    private DownloadTask downloadTask;
    private ImageView newIcon;// 显示有无更新

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_about);
        this.mContext = this;
        initView();
    }

    public void initView() {
        title = (TextView) findViewById(R.id.authorize_title);
        title.setText(getString(R.string.left_menu_about));
        findViewById(R.id.app_icon).setOnClickListener(this);
        appVersionTv = (TextView) findViewById(R.id.app_version);
        versionTv = (TextView) findViewById(R.id.newVersion);
        newIcon = (ImageView) findViewById(R.id.has_new_icon);
        String packageName = mContext.getPackageName();
        PackageInfo pinfo;
        try {
            pinfo = mContext.getPackageManager().getPackageInfo(packageName, 0);
            appVersionTv.setText(getString(R.string.about_app_version) + "V" + pinfo.versionName);
            verLocal = pinfo.versionName;

        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        dialog = new CommonDiaglog(this, false);
        dialog.setNegsitiveClick(this);
        dialog.setPositiveClick(this);
        app_update_lay = (RelativeLayout) findViewById(R.id.app_update_lay);
        feedback_lay = (RelativeLayout) findViewById(R.id.feedback_lay);
        helplay = (RelativeLayout) findViewById(R.id.lay_help);
        helplay.setOnClickListener(this);
        app_update_lay.setOnClickListener(this);
        feedback_lay.setOnClickListener(this);
        if (SlidingMenu.HAS_NEW_VERSION) {
            versionTv.setText(getString(R.string.app_setting_has_new) + MainPageActivity.LastVersion);
            versionPath = MainPageActivity.versionPath;
            newIcon.setVisibility(View.VISIBLE);
        } else {
            versionTv.setText(getString(R.string.app_setting_newest) + verLocal);
            newIcon.setVisibility(View.GONE);
        }

    }


    private void intdownloadPro() {
        downloadDialog = new ProgressDialog(mContext);
        downloadDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        downloadDialog.setMax(100);
        downloadDialog.setProgress(0);
        // downloadDialog.setTitle(mActivity.getResources().getString(
        // R.string.DownloadUpgradeInit));
        downloadDialog.setCancelable(true);
        downloadDialog.show();
    }

    private void dismissPro() {
        if (downloadDialog != null && downloadDialog.isShowing())
            downloadDialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.app_update_lay:
                if (!hasClick) {
                    if (SlidingMenu.HAS_NEW_VERSION) {
                        if (NetworkUtils.isWifiConnected()) {
                            goDownLoad();
                        } else {
                            if (SPUtils.get().getBoolean(
                                    Constants.downloadbyWifi, true)) {
                                dialog.setMessase(mContext
                                        .getString(R.string.app_setting_mobile_net_download));
                                dialog.show();
                            } else {
                                dialog.setMessase(mContext
                                        .getString(R.string.app_setting_mobile_net));
                                dialog.show();
                            }
                        }

                    } else {
                        ToastUtils.showShortToast(  R.string.app_setting_now_newest);
                    }
                }

                break;
            case R.id.feedback_lay: {
                Intent intent = new Intent(AboutActivity.this, FeedBackActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.lay_help: {
                String url = "";
                if (Constants.SYSTEM_LAN.equalsIgnoreCase("CN")) {
                    url = "http://services.ubtrobot.com/ubx/q2a_CN.html";
                } else {
                    url = "http://services.ubtrobot.com/ubx/q2a_EN.html";
                }
                Intent intent = new Intent(AboutActivity.this, HelpActivity.class);
                intent.putExtra("title", getString(R.string.drawer_help));
                intent.putExtra("url", url);
                startActivity(intent);

            }

        }
    }

    // true表示远程版本大，即需要升级
    public boolean compareVersion(String remote_version) {

        String[] lVersion = verLocal.split("\\.");
        String[] wVersion = remote_version.split("\\.");
        for (int i = 0; i < lVersion.length; i++) {

            if (Integer.parseInt(wVersion[i]) > Integer.parseInt(lVersion[i])) {
                return true;
            } else if (Integer.parseInt(wVersion[i]) == Integer
                    .parseInt(lVersion[i])) {
                continue;
            } else {
                return false;
            }
        }
        return false;

    }

    @Override
    public void OnNegsitiveClick() {

    }

    @Override
    public void OnPositiveClick() {
// TODO Auto-generated method stub
        goDownLoad();
    }

    /***
     * 下载Alpha 应用
     */
    private void goDownLoad() {
        if (downloadTask != null) {
            downloadTask.setExitDoaloading(true);
            downloadTask = null;
        }
        downloadTask = new DownloadTask(mContext, mHandler, versionPath);
        downloadTask.setExitDoaloading(false);
        downloadTask.start();
    }

    BaseHandler mHandler = new BaseHandler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // TODO Auto-generated method stub
            switch (msg.what) {
                case NetWorkConstant.RESPONSE_CHECK_APP_UPDATE_SUCCESS:
                    hasClick = false;
                    LoadingDialog.dissMiss();
                    CheckAppUpdate updateRsp = (CheckAppUpdate) msg.obj;
                    if (updateRsp != null) {
                        // netLocal = Integer.valueOf(updateRsp.getVersionName()
                        // .replace(".", ""));
                        String webVersion = updateRsp.getVersionName();
                        boolean hasNewVersion = compareVersion(webVersion);
                        if (hasNewVersion/* netLocal>appLocalVer */) {
                            versionTv.setText(getString(R.string.app_setting_has_new) + updateRsp.getVersionName());
                            versionPath = updateRsp.getVersionPath();
                            hasNew = true;
                        } else {
                            Logger.d("nxy", "null");
                        }
                    } else {
                        ToastUtils.showShortToast(  getString(R.string.app_setting_find_version_failed));
                    }
                    break;

                case NetWorkConstant.RESPONSE_CHECK_APP_UPDATE_FAILED:
                    LoadingDialog.dissMiss();
                    hasClick = false;
                    ToastUtils.showShortToast(  getString(R.string.app_setting_find_version_failed));
                    break;
                case 0:
                    ToastUtils.showShortToast(  getString(R.string.app_setting_apk_unnomal));
                    break;
                case 1:
                    intdownloadPro();
                    break;
                case 2:
                    downloadDialog.setProgress(msg.arg1 * 100 / msg.arg2);
                    break;
                case 3:
                    dismissPro();
                    // Logger.i( "下载成功");
                    String fileName = mContext.getString(R.string.app_name) + ".apk";
                    break;
                case 4:
                    dismissPro();
                    ToastUtils.showShortToast(
                            getString(R.string.app_setting_apk_download_falied));
                    break;
                case 5:
                    dismissPro();
                    ToastUtils.showShortToast(  getString(R.string.app_setting_low_size));
                    break;
                case 6:
                    dismissPro();
                    ToastUtils.showShortToast(
                            getString(R.string.app_setting_read_file_exception));
                    break;
            }
        }
    };

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.right_out, 0);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            if (downloadTask != null) {
                downloadTask.setExitDoaloading(true);
                downloadTask = null;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


}
