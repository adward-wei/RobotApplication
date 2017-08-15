package com.ubtechinc.alpha2ctrlapp.ui.fragment.robot;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.common.collect.Lists;
import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.ListUtils;
import com.ubtech.utilcode.utils.StringUtils;
import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.base.BaseHandler;
import com.ubtechinc.alpha2ctrlapp.base.CallbackListener;
import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.constants.IntentConstants;
import com.ubtechinc.alpha2ctrlapp.constants.MessageType;
import com.ubtechinc.alpha2ctrlapp.constants.NetWorkConstant;
import com.ubtechinc.alpha2ctrlapp.data.Injection;
import com.ubtechinc.alpha2ctrlapp.data.robot.IRobotActionDataSource;
import com.ubtechinc.alpha2ctrlapp.data.robot.IRobotAppDataSource;
import com.ubtechinc.alpha2ctrlapp.data.robot.IRobotInitDataSource;
import com.ubtechinc.alpha2ctrlapp.data.robot.RobotActionRepository;
import com.ubtechinc.alpha2ctrlapp.data.robot.RobotAppRepository;
import com.ubtechinc.alpha2ctrlapp.data.robot.RobotInitRepository;
import com.ubtechinc.alpha2ctrlapp.data.shop.AppReponsitory;
import com.ubtechinc.alpha2ctrlapp.data.shop.IAppDataSource;
import com.ubtechinc.alpha2ctrlapp.database.RobotAppEntrity;
import com.ubtechinc.alpha2ctrlapp.entity.AppInstalledInfo;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.AlphaParam;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.ButtonConfig;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.RobotApp;
import com.ubtechinc.alpha2ctrlapp.entity.business.shop.AppInfo;
import com.ubtechinc.alpha2ctrlapp.events.AppDownloadStatusChangeEvent;
import com.ubtechinc.alpha2ctrlapp.presenter.ISpotifyLoginPresenter;
import com.ubtechinc.alpha2ctrlapp.presenter.SpotifyLoginPresenterImpl;
import com.ubtechinc.alpha2ctrlapp.service.RobotManagerService;
import com.ubtechinc.alpha2ctrlapp.ui.activity.main.MainPageActivity;
import com.ubtechinc.alpha2ctrlapp.ui.activity.robot.AlarmEntryActivity;
import com.ubtechinc.alpha2ctrlapp.ui.activity.robot.ImageGallyActivity;
import com.ubtechinc.alpha2ctrlapp.ui.activity.robot.MyDeviceActivity;
import com.ubtechinc.alpha2ctrlapp.ui.activity.robot.SurveillanceStartActivity;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.base.BaseContactFragememt;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.shop.adapter.AppInfoAdapter;
import com.ubtechinc.alpha2ctrlapp.util.BeanUtils;
import com.ubtechinc.alpha2ctrlapp.util.Tools;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.CommonDiaglog;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.LoadingDialog;
import com.ubtechinc.nets.http.ThrowableWrapper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.ubtechinc.alpha2ctrlapp.ui.activity.main.MainPageActivity.alphaParam;


/**
 * @ClassName AppFragMent
 * @date 5/17/2017
 * @author tanghongyu
 * @Description 机器人操作页，包含动作表、相册、闹钟等
 * @modifier
 * @modify_time
 */
public class AppFragMent extends BaseContactFragememt implements View.OnClickListener {
    private GridView listView;
    public AppInfoAdapter appInfoAdapter;
    private List<RobotApp> mAppList = Lists.newArrayList();
    private View connected_robot_tips;
    private View mConnectDeviceBtn;
    private RelativeLayout updateLay;
    public List<AppInfo> robotApps = Lists.newArrayList();
    private boolean hasGetUpdateList = false;
    private boolean hasGetBottomMenu = false;
    private boolean hasGetAppList = false;
    private boolean hasGetCurrentApp = false;
    private boolean hasGetPower = false;
    private RelativeLayout powerLay;
    private TextView powerContent;
    private TextView tvChargeStatu;
    private View powerBg;
    private boolean mIsNeedGetApp = false;
    private CommonDiaglog diaglog;

    /**
     * 启动视频监控
     */
    private static final int REQUEST_SURVEILLANCE_CODE = 1000;
    private static final String TAG = "AppFragMent";
    private AppReponsitory mAppReponsitory;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mAppReponsitory = Injection.provideAppRepository(mApplication);
    }

    @Override
    public View onCreateFragmentView(LayoutInflater inflater,
                                     ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_setting, container, false);
    }

    @Override
    public void initView() {
        listView = (GridView) mContentView.findViewById(R.id.actionlistView);
        mConnectDeviceBtn = mContentView.findViewById(R.id.connect_device_btn);
        connected_robot_tips = mContentView.findViewById(R.id.connected_robot_tips);
        mConnectDeviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, MyDeviceActivity.class);
                intent.putExtra("isFirst", false);
                startActivity(intent);
            }
        });

        updateLay = (RelativeLayout) mContentView.findViewById(R.id.updateLay);
        powerLay = (RelativeLayout) mContentView.findViewById(R.id.pwower_stastuLay);
        powerBg = mContentView.findViewById(R.id.pwower_bg);
        powerContent = (TextView) mContentView.findViewById(R.id.pwower_stastus);
        tvChargeStatu = (TextView) mContentView.findViewById(R.id.pwower_charge_statu);
        appInfoAdapter = new AppInfoAdapter(mActivity, AppFragMent.this, new CallbackListener<RobotApp>() {

            @Override
            public void callback(final RobotApp robotApp) {
                RobotAppRepository.getInstance().uninstallApp( robotApp.getPackageName(), robotApp.getAppKey(), new IRobotAppDataSource.ControlAppCallback() {
                    @Override
                    public void onSuccess() {
                        LoadingDialog.dissMiss();
                        if (mMainPageActivity.currentAppInfo != null) {
                            if (robotApp.getPackageName().equals(mMainPageActivity.currentAppInfo.getPackageName())) {
                                mMainPageActivity.currentAppInfo = null;
                                mMainPageActivity.changeCurrentApp();
                                getMenuDiag(false);
                            }
                        }
                        MainPageActivity.dao.deleteApp(robotApp.getPackageName());

                        mMainPageActivity.appInfoList.clear();
                        appInfoAdapter.removeEntrity(robotApp.getPackageName());
                    }

                    @Override
                    public void onFail(ThrowableWrapper e) {
                        LoadingDialog.dissMiss();
                        ToastUtils.showShortToast(  R.string.app_uninstall_failed);


                    }
                });
            }
        });
        updateLay.setOnClickListener(this);
        listView.setAdapter(appInfoAdapter);
        listView.setOnItemClickListener(appInfoAdapter);
        listView.setOnItemLongClickListener(appInfoAdapter);
        SpotifyLoginPresenterImpl.getInstance().setmFragment(this);
        SpotifyLoginPresenterImpl.getInstance().onStart();

    }
    @Override
    public void onResume() {
        super.onResume();
        Logger.t(TAG).d( " onResume");
        /*获取机器人信息*/
        if (RobotManagerService.getInstance().isConnectedRobot()) {
            getAlphaParam();
        }
        mMainPageActivity.mainTopView.setVisibility(View.VISIBLE);
        mMainPageActivity.currentFragment = this;

        refreshView();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void refreshView(List<RobotApp> appList) {

        mAppList.addAll(appList);
        appInfoAdapter.onNotifyDataSetChanged();
        if (!ListUtils.isEmpty(mAppList)) {
            String appKeys = "";
            mAppList.get(0).setName(mActivity.getString(R.string.shop_page_action_list));
            mAppList.get(1).setName(mActivity.getString(R.string.alarm_title));

            //因为动作库占用了一个空间所以从一开始
            for (int i = 2; i < mAppList.size(); i++) {
                String key = mAppList.get(i).getAppKey();
                if (!StringUtils.isEmpty(key)) {
                    appKeys += key;
                }

                if (i < mAppList.size() - 1 && !TextUtils.isEmpty(appKeys)) {
                    appKeys += ",";
                }
            }
            mAppReponsitory.batchGetAppInfo(appKeys, new IAppDataSource.AppListDataCallback() {
                @Override
                public void onLoadAppList(List<AppInfo> appInfos) {
                    mMainPageActivity.updateApps.clear();
                    if (!ListUtils.isEmpty(appInfos)) {
                        robotApps.clear();
                        robotApps.addAll(appInfos);
                        Logger.d(TAG, "batchGetAppInfo apps = " + robotApps);
                        for (AppInfo info : robotApps) {

                                for (RobotApp appEntrityInfo : mAppList) {
                                    if(StringUtils.isEquals(info.getAppKey(), appEntrityInfo.getAppKey()) && Integer.valueOf(info.getAppVersion()) > Integer.valueOf(appEntrityInfo.getVersionCode())) {
                                        if (info.getAppName().equals(Constants.app_chat_baike)
                                                || info.getAppName().equals(Constants.app_chat_count)
                                                || info.getAppName().equals(Constants.app_chat_huil)
                                                || info.getAppName().equals(Constants.app_chat_joke)
                                                || info.getAppName().equals(Constants.app_chat_music)
                                                || info.getAppName().equals(Constants.app_chat_poem)
                                                || info.getAppName().equals(Constants.app_chat_story)
                                                || info.getAppName().equals(Constants.app_chat_weather)
                                                || info.getAppName().equals(Constants.app_chat_time)
                                                || info.getAppName().equals(Constants.app_chat_action)
                                                || info.getAppName().equals(Constants.app_chat_clock)) {
                                            continue;
                                        } else {
                                            mMainPageActivity.updateApps.add(info);
                                        }
                                    }

                            }

                        }
                    }
                    if (mMainPageActivity.updateApps.size() > 0) {
                        updateLay.setVisibility(View.VISIBLE);
                    } else {
                        updateLay.setVisibility(View.GONE);
                    }
                    appInfoAdapter.checkUpdate(appInfos);
                    getRobotCurrentRunningApp();
                    hasGetUpdateList = true;
                    mHandler.sendEmptyMessage(-1010);
                }

                @Override
                public void onDataNotAvailable(ThrowableWrapper e) {

                }
            });


        }

    }




    public void refreshView() {
        if (!RobotManagerService.getInstance().isConnectedRobot()) {
            connected_robot_tips.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            updateLay.setVisibility(View.GONE);
            powerLay.setVisibility(View.GONE);
            mMainPageActivity.bottomLay.setVisibility(View.GONE);
        } else {
            connected_robot_tips.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            initData();
        }
    }

    public BaseHandler mHandler = new BaseHandler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            if (msg == null) {
                Logger.i( "handleMessage msg is null.");
                return;
            }
            Logger.i( "handleMessage" + msg.what);
            switch (msg.what) {

                case MessageType.ALPHA_LOST_CONNECTED:
                    Logger.i( "handleMessage ALPHA_LOST_CONNECTED.");
                    mMainPageActivity.isCurrentAlpha2MacLostConnection((String) msg.obj);
                    break;

                case NetWorkConstant.RESPONSE_APP_UPDATE_FAILED:
                    updateLay.setVisibility(View.GONE);
                    hasGetUpdateList = true;
                    mHandler.sendEmptyMessage(-1010);
                    break;



                case -1010:
                    if (hasGetAppList && hasGetBottomMenu && hasGetCurrentApp && hasGetUpdateList && hasGetPower) {
                        LoadingDialog.dissMiss();
                        if (alphaParam == null) {
                            getAlphaParam();// 未获取配置信息的，则需要获取配置信息
                        }
                        mMainPageActivity.showAppGuide();
                    }

                    break;




            }
        }
    };
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AppDownloadStatusChangeEvent event) {
        Logger.d( event.getAppEntrityInfo().toString());
        RobotApp appEntrityInfo =  event.getAppEntrityInfo();
        Logger.t(TAG).d( "onEvent AppDownloadStatusChangeEvent state = %d" , appEntrityInfo.getDownloadState() );
        if (null != appEntrityInfo && appEntrityInfo.getDownloadState() == BusinessConstants.APP_STATE_INSTALL_SUCCESS) {
            getAllInstalledApps();
            List<AppInfo> list = mMainPageActivity.updateApps;
            if (null != mMainPageActivity.updateApps && mMainPageActivity.updateApps.size() > 0) {
                for (AppInfo appInfo : list) {
                    if (appInfo.getPackageName().equals(appEntrityInfo.getPackageName())) {
                        mMainPageActivity.updateApps.remove(appInfo);
                        break;
                    }
                }
                if (mMainPageActivity.updateApps.size() > 0) {
                    updateLay.setVisibility(View.VISIBLE);
                } else {
                    updateLay.setVisibility(View.GONE);
                }
            }

        }

    }

    /**
     * 获取的菜单
     *
     * @param deLay 是否需要延时
     */
    private void getMenuDiag(boolean deLay) {
        if (mMainPageActivity.currentAppInfo == null || mMainPageActivity.currentAppInfo == null) {// 当前运行的app null时，直接将底部菜单清空
            if (mMainPageActivity.menuWinDow != null) {
                mMainPageActivity.menuWinDow = null;
                mMainPageActivity.refreshBottom(false);
            }
        } else {
            if (mMainPageActivity.currentAppInfo != null && mMainPageActivity.currentAppInfo.isButtonEvent()) {
                if (!deLay) {
                    getAppButtonAction();
                } else {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getAppButtonAction();
                        }
                    },1000);
                }

            } else {
                hasGetBottomMenu = true;
                if ((mMainPageActivity.currentAppInfo != null && mMainPageActivity.currentAppInfo.isSetting())
                    //  || mMainPageActivity.currentAppInfo.getPackageName().equals(ISpotifyLoginPresenter.COM_UBTECHINC_ALPHAENGLISHCHAT)
                        ) {
                    mMainPageActivity.showMenuDiag(null, mMainPageActivity.currentFragment, mMainPageActivity.currentAppInfo.isSetting());
                }
                mHandler.sendEmptyMessage(-1010);
            }
        }

    }

    private void getAppButtonAction() {

        RobotAppRepository.getInstance().getAppButton(0, mMainPageActivity.currentAppInfo.getPackageName(), "en".getBytes(), new IRobotAppDataSource.GetAppButtonConfigCallback() {
            @Override
            public void onLoadButtonConfig(ButtonConfig buttonConfig) {
                if (buttonConfig != null &&(buttonConfig.getModels().size() > 0 || mMainPageActivity.currentAppInfo.isSetting())) {
                    mMainPageActivity.showMenuDiag(buttonConfig, AppFragMent.this,
                            mMainPageActivity.currentAppInfo.isSetting());
                }
                hasGetBottomMenu = true;
                mHandler.sendEmptyMessage(-1010);
            }

            @Override
            public void onDataNotAvailable(ThrowableWrapper e) {

            }
        });


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
                }
            }

            @Override
            public void onDataNotAvailable(ThrowableWrapper e) {
                ToastUtils.showShortToast(e.getMessage());
            }
        });

    }

    private void getPowerValue(boolean isNeedGetApp) {

        this.mIsNeedGetApp = isNeedGetApp;
        if (mIsNeedGetApp) {
            LoadingDialog.getInstance(mActivity).show();
            hasGetUpdateList = false;
            hasGetBottomMenu = false;
            hasGetAppList = false;
            hasGetCurrentApp = false;
            hasGetPower = false;
        }
        RobotInitRepository.getInstance().getPower(new IRobotInitDataSource.GetPowerDataCallback() {
            @Override
            public void onLoadPowerData(int power) {
                hasGetPower = true;
                Log.d(TAG, "powerValue==" + power);
                powerLay.setVisibility(View.VISIBLE);
                tvChargeStatu.setVisibility(View.GONE);
                if (power < 0) {
                    if (power == -4) {
                        powerBg.setBackgroundColor(mActivity.getResources().getColor(R.color.text_color_t5));
                        powerContent.setTextColor(mActivity.getResources().getColor(R.color.text_color_t5));
                        powerContent.setText(mActivity.getResources().getString(R.string.home_battery_charge_full));
                    } else if (power == -52) {
                        powerBg.setBackgroundColor(mActivity.getResources().getColor(R.color.text_color_t6));
                        powerContent.setTextColor(mActivity.getResources().getColor(R.color.text_color_t6));
                        powerContent.setText(mActivity.getResources().getString(R.string.home_battery_charge));
                    }
                } else if (power <= 100) {
                    if (power <= 20) {
                        powerBg.setBackgroundColor(mActivity.getResources().getColor(R.color.text_color_t6));
                        powerContent.setTextColor(mActivity.getResources().getColor(R.color.text_color_t6));
                        powerContent.setText(mActivity.getResources().getString(R.string.home_battery_need_charge));
                    } else {
                        powerBg.setBackgroundColor(mActivity.getResources().getColor(R.color.text_color_t5));
                        powerContent.setTextColor(mActivity.getResources().getColor(R.color.text_color_t5));
                        powerContent.setText(mActivity.getResources().getString(R.string.home_battery) + " " + power + "%");
                    }
                } else if (power >= 1000) {//兼容4.1麦的电量
                    powerBg.setBackgroundColor(mActivity.getResources().getColor(R.color.text_color_t5));
                    powerContent.setTextColor(mActivity.getResources().getColor(R.color.text_color_t5));
                    if (power == 1100) {
                        powerContent.setText(mActivity.getResources().getString(R.string.home_battery_charge_full));
                    } else if (power < 1100) {
                        powerContent.setText(mActivity.getResources().getString(R.string.home_battery) + " " + (power - 1000) + "%");
                        tvChargeStatu.setVisibility(View.VISIBLE);
                    } else {
                        powerContent.setText(mActivity.getResources().getString(R.string.home_battery_charge));
                    }
                }

                if (mIsNeedGetApp) {
                    mIsNeedGetApp = false;
                    getAllInstalledApps();
                }
                if (Tools.isShowBottom(alphaParam, mActivity) && null != mMainPageActivity.currentFragment && mMainPageActivity.currentFragment.getClass().getName().equals(AppFragMent.class.getName())) {
                    mMainPageActivity.bottomLay.setVisibility(View.VISIBLE);
                } else {
                    mMainPageActivity.bottomLay.setVisibility(View.GONE);
                }
            }

            @Override
            public void onDataNotAvailable(ThrowableWrapper e) {
                ToastUtils.showShortToast(e.getMessage());
            }
        });




    }

    /**
     * 获取机器人已安装的应用列表
     */
    public void getAllInstalledApps() {
        LoadingDialog.getInstance(mActivity).show();
        RobotAppRepository.getInstance().getAllInstalledApp(new IRobotAppDataSource.GetInstalledAppCallback() {
            @Override
            public void onLoadAppList(List<RobotApp> robotList) {
                //手动添加相册
                RobotApp imageInfo = new RobotApp(); // 相册
                imageInfo.setName(mApplication.getString(R.string.image_galley));
                imageInfo.setPackageName(BusinessConstants.PACKAGENAME_SHOP_GALLARY);
                robotList.add(2, imageInfo);
                List<RobotAppEntrity> robotAppEntrities = new ArrayList<>();

                //存储App列表到本地
                for(RobotApp appEntrityInfo : robotList) {
                    RobotAppEntrity robotAppEntrity1 = new RobotAppEntrity();
                    BeanUtils.copyBean(appEntrityInfo, robotAppEntrity1);
                    robotAppEntrity1.setDownloadState(BusinessConstants.APP_STATE_INSTALL_SUCCESS);
                    robotAppEntrity1.setSerialNo(Alpha2Application.getRobotSerialNo());
                    robotAppEntrities.add(robotAppEntrity1);
                }

                MainPageActivity.dao.insertToAppInstalledList(robotAppEntrities);
                    /*授权用户不能使用视频监控，如果是授权用户，则从数据库中删除这个应用的信息*/
                if (!RobotManagerService.getInstance().isRobotOwner()) {
                    MainPageActivity.dao.deleteApp( Constants.MAIN_SERVICE_SURVEILLANCE_PACKAGE_NAME);
                }
                refreshView(robotList);
                Logger.i("设置不刷新");
                hasGetAppList = true;
                mHandler.sendEmptyMessage(-1010);
                Logger.i( "ActionList==========");
            }

            @Override
            public void onDataNotAvailable(ThrowableWrapper e) {

                ToastUtils.showShortToast(e.getMessage());

            }
        });
        hasGetUpdateList = false;
        hasGetBottomMenu = false;
        hasGetAppList = false;
        hasGetCurrentApp = false;

    }

    /**
     * 获取当前运行的App
     */
    public void getRobotCurrentRunningApp() {

        RobotAppRepository.getInstance().getCurrentRunningApp(new IRobotAppDataSource.GetRunningAppCallback() {
            @Override
            public void onLoadApp(RobotApp app) {
                Logger.t(TAG).d(  "获取当前的app " + app);
                if (mMainPageActivity.currentAppInfo == null)
                    mMainPageActivity.currentAppInfo = new AppInstalledInfo();
                  BeanUtils.copyBean(app, mMainPageActivity.currentAppInfo);
                if (mMainPageActivity.currentAppInfo != null && !TextUtils.isEmpty(mMainPageActivity.currentAppInfo.getPackageName())) {
                    if (mMainPageActivity.currentAppInfo.getPackageName().equals(BusinessConstants.PACKAGENAME_ALPHA_TRANSLATION)) {
                        replaceFragment(TranslateFragment.class.getName(), new Bundle());
                    }
                    refreshBottom(false);
                } else {

                    mMainPageActivity.noApp();
                    mMainPageActivity.currentAppInfo = null;
                    hasGetBottomMenu = true;
                }
                if (null != appInfoAdapter.getFileList() && null != mMainPageActivity.currentAppInfo ) {
                    for (int i = 0; i < appInfoAdapter.getFileList().size(); i++) {
                        if (appInfoAdapter.getFileList().get(i).getPackageName().equals(mMainPageActivity.currentAppInfo.getPackageName())) {
                            appInfoAdapter.setClicktemp(i);
                            appInfoAdapter.notifyDataSetChanged();
                            break;
                        }
                    }
                }
                hasGetCurrentApp = true;
                mHandler.sendEmptyMessage(-1010);
            }

            @Override
            public void onDataNotAvailable(ThrowableWrapper e) {
                Logger.t(TAG).w( "获取当前的app 空");
                mMainPageActivity.noApp();
                mMainPageActivity.currentAppInfo = null;
                hasGetBottomMenu = true;
            }
        });

    }

    /**
     * 设置并刷新底部菜单
     *
     * @needDelay 是否需要延时
     */
    private void refreshBottom(boolean needDelay) {
        if (mMainPageActivity.currentAppInfo != null && mMainPageActivity.currentAppInfo != null) {
            mMainPageActivity.currentAppInfo = MainPageActivity.dao.queryApk( mMainPageActivity.currentAppInfo.getPackageName());
            if (mMainPageActivity.currentAppInfo != null && mMainPageActivity.currentAppInfo != null) {
                mMainPageActivity.changeCurrentApp();
            }
        }

        getMenuDiag(needDelay);
    }

    public void startApp(AppInstalledInfo appInfo) {
        Logger.i( "startApp ");
        /*是否是视频监控应用*/
        if (appInfo != null &&
                Constants.MAIN_SERVICE_SURVEILLANCE_PACKAGE_NAME.equals(appInfo.getPackageName())) {
            startActivityForResult(new Intent(mActivity, SurveillanceStartActivity.class), REQUEST_SURVEILLANCE_CODE);
            return;
        }
        if (mMainPageActivity.currentAppInfo != null && mMainPageActivity.currentAppInfo != null) {
            if (StringUtils.isEquals(mMainPageActivity.currentAppInfo.getPackageName(),appInfo.getPackageName())) {
                if (appInfo.getPackageName().equals(BusinessConstants.PACKAGENAME_TEMP_ALARM)) {
                    startIntent(AlarmEntryActivity.class);
                } else if (appInfo.getPackageName().equals(BusinessConstants.PACKAGENAME_TEMP_ACTION)) {
                    replaceFragment(LocalActionFragment.class.getName(), new Bundle());
                } else if (appInfo.getPackageName().equals(BusinessConstants.PACKAGENAME_SHOP_GALLARY)) {
                    startIntent(ImageGallyActivity.class);
                } else if ((mMainPageActivity.currentAppInfo.getPackageName().equals(BusinessConstants.PACKAGENAME_CH_CHAT)
                        || mMainPageActivity.currentAppInfo.getPackageName().equals(BusinessConstants.PACKAGENAME_EN_CHAT)
                ) && Alpha2Application.getInstance().isShopAction()) {
                    Alpha2Application.getInstance().setShopAction(false);
                    startNewApp(appInfo);
                } else {
                    if ((mMainPageActivity.currentAppInfo.isButtonEvent()
                            || mMainPageActivity.currentAppInfo.isSetting())
                            || mMainPageActivity.currentAppInfo.getPackageName().equals(ISpotifyLoginPresenter.COM_UBTECHINC_ALPHAENGLISHCHAT)) {
                        if (mMainPageActivity.menuWinDow != null && !mMainPageActivity.menuWinDow.isShowing()) {
                            mMainPageActivity.menuWinDow.show();
                        }

                    }
                }
            } else {
                startNewApp(appInfo);
            }

        } else {
            startNewApp(appInfo);
        }
    }

    /**
     * 切换Activity
     *
     * @param class1
     */
    public void startIntent(Class class1) {
        Intent intent = new Intent(this.getActivity(), class1);
        startActivity(intent);
    }

    private void startNewApp(AppInstalledInfo appInfo) {


        if (StringUtils.isEquals(appInfo.getPackageName(),BusinessConstants.PACKAGENAME_TEMP_ALARM)) {
            startIntent(AlarmEntryActivity.class);
        } else if (StringUtils.isEquals(appInfo.getPackageName(),BusinessConstants.PACKAGENAME_SHOP_GALLARY)) {
            startIntent(ImageGallyActivity.class);
        } else {
            if (mMainPageActivity.currentAppInfo != null && mMainPageActivity.currentAppInfo != null) {
                // 停止当前的App
                if (mMainPageActivity.currentAppInfo.getPackageName().equals(BusinessConstants.PACKAGENAME_TEMP_ACTION)) {
                    RobotActionRepository.getInstance().stopPlayAction(new IRobotActionDataSource.ControlActionCallback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onFail(ThrowableWrapper e) {

                        }
                    });

                } else {
                    RobotAppRepository.getInstance().stopApp(mMainPageActivity.currentAppInfo.getName(), mMainPageActivity.currentAppInfo.getPackageName(), new IRobotAppDataSource.ControlAppCallback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onFail(ThrowableWrapper e) {

                        }
                    });
                }
                if (mMainPageActivity.menuWinDow != null) {
                    mMainPageActivity.menuWinDow = null;
                }
            }
            mMainPageActivity.currentAppInfo = appInfo;
            if (StringUtils.isEquals(appInfo.getPackageName(),BusinessConstants.PACKAGENAME_TEMP_ACTION)) {
                replaceFragment(LocalActionFragment.class.getName(), new Bundle());
            } else {
                LoadingDialog.getInstance(mActivity).show();
                RobotAppRepository.getInstance().startApp(mMainPageActivity.currentAppInfo.getName(), mMainPageActivity.currentAppInfo.getPackageName(), new IRobotAppDataSource.ControlAppCallback() {
                    @Override
                    public void onSuccess() {
                        if (null != mMainPageActivity.currentAppInfo && mMainPageActivity.currentAppInfo.getPackageName().equals(BusinessConstants.PACKAGENAME_ALPHA_TRANSLATION)) {
                            replaceFragment(TranslateFragment.class.getName(), new Bundle());
                        }
                        refreshBottom(true);
                    }

                    @Override
                    public void onFail(ThrowableWrapper e) {

                    }
                });
            }


        }


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.updateLay:
                bundle = new Bundle();
                bundle.putSerializable(IntentConstants.DATA_APP_LIST, (Serializable) mMainPageActivity.updateApps);
                replaceFragment(AppUpdateFragment.class.getName(), bundle);
                break;

            default:
                break;
        }
    }

    /**
     * 收到应用列表数据之后刷新数据
     * //	 @param actionList
     * <p>
     * public void receiveListData(AppEntrityList actionList){
     * Logger.i( "handleMessage ALPHA_ACTIONLIST_RESPONSE.");
     * refreshView(actionList);
     * getRobotCurrentRunningApp();
     * }
     **/

    public void onRefreshAfterEdit() {
        if (!RobotManagerService.getInstance().isConnectedRobot()) {
            connected_robot_tips.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            updateLay.setVisibility(View.GONE);
            mMainPageActivity.bottomLay.setVisibility(View.GONE);
        } else {
            connected_robot_tips.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            appInfoAdapter.onNotifyDataSetChanged();
            if (Tools.isShowBottom(alphaParam, mActivity)) {
                mMainPageActivity.bottomLay.setVisibility(View.VISIBLE);
            } else {
                mMainPageActivity.bottomLay.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 初始化界面数据
     */

    private void initData() {
        List<AppInstalledInfo> list = MainPageActivity.dao.queryAppList();
        if (!ListUtils.isEmpty(list) && !MainPageActivity.hasConnectedNewRobot) {//本地已经缓存了app列表
            robotApps = mMainPageActivity.updateApps;
            if (robotApps.size() > 0) {
                updateLay.setVisibility(View.VISIBLE);
            } else {
                updateLay.setVisibility(View.GONE);
            }
            appInfoAdapter.onNotifyDataSetChanged();
            getPowerValue(false);
            List<AppInstalledInfo> installedInfos = appInfoAdapter.getFileList();
            boolean isServiceLanEn = false;
            boolean isServiceLanCH = false;
            if (null != alphaParam) {
                isServiceLanEn = MainPageActivity.alphaParam.getServiceLanguage().equalsIgnoreCase(BusinessConstants.ROBOT_SERVICE_LANGUAGE_ENGLISH);
                isServiceLanCH = MainPageActivity.alphaParam.getServiceLanguage().equalsIgnoreCase(BusinessConstants.ROBOT_SERVICE_LANGUAGE_CHINESE);

            }
            if (null != installedInfos) {
                for (int i = 0; i < installedInfos.size(); i++) {
                    if (isServiceLanCH && installedInfos.get(i).getPackageName().equals(BusinessConstants.PACKAGENAME_CH_CHAT)) {
                        if (Alpha2Application.getInstance().isFromAction()) {
                            listView.performItemClick(listView.getChildAt(i), i, listView.getItemIdAtPosition(i));
                            Alpha2Application.getInstance().setFromAction(false);
                        } else {
                            appInfoAdapter.setClicktemp(i);
                            appInfoAdapter.notifyDataSetChanged();
                        }
                        break;
                    } else if (isServiceLanEn && installedInfos.get(i).getPackageName().equals(BusinessConstants.PACKAGENAME_EN_CHAT)) {
                        if (Alpha2Application.getInstance().isFromAction()) {
                            listView.performItemClick(listView.getChildAt(i), i, listView.getItemIdAtPosition(i));
                            Alpha2Application.getInstance().setFromAction(false);
                        } else {
                            appInfoAdapter.setClicktemp(i);
                            appInfoAdapter.notifyDataSetChanged();
                        }
                        break;
                    }
                }
            }
            if (Tools.isShowBottom(alphaParam, mActivity)) {
                mMainPageActivity.bottomLay.setVisibility(View.VISIBLE);
            } else {
                mMainPageActivity.bottomLay.setVisibility(View.GONE);
            }
        } else {
            // 默认首次进来清除所有表中数据
            MainPageActivity.dao.deleteAllData();
            getPowerValue(true);
            MainPageActivity.hasConnectedNewRobot = false;
            alphaParam = null;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

		/*视频监控之后，直接启动闲聊*/
        if (requestCode == REQUEST_SURVEILLANCE_CODE) {
            List<AppInstalledInfo> list = MainPageActivity.dao.queryAppList();
            boolean isChatAppExist = false;
            for (AppInstalledInfo appInfo : list) {
                if (appInfo != null && appInfo != null) {
                    /*如果列表中有中英文闲聊，启动闲聊*/
                    if (BusinessConstants.PACKAGENAME_CH_CHAT.equals(appInfo.getPackageName()) ||
                            BusinessConstants.PACKAGENAME_EN_CHAT.equals(appInfo.getPackageName())) {
                        startNewApp(appInfo);
                        isChatAppExist = true;
                        break;
                    }
                }
            }

			/*如果闲聊不存在，则延迟5s获取当前正在运行的应用，然后更新底部栏*/
            if (!isChatAppExist) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getRobotCurrentRunningApp();
                    }
                }, 5000);
            }

            return;
        }

        SpotifyLoginPresenterImpl.getInstance().onActivityResult(requestCode, resultCode, data);
    }
}
