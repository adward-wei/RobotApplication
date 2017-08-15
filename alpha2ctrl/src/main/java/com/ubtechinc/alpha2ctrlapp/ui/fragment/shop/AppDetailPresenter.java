package com.ubtechinc.alpha2ctrlapp.ui.fragment.shop;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.FileUtils;
import com.ubtech.utilcode.utils.ListUtils;
import com.ubtech.utilcode.utils.StringUtils;
import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.data.shop.AppDetailModel;
import com.ubtechinc.alpha2ctrlapp.data.shop.IAppDataSource;
import com.ubtechinc.alpha2ctrlapp.data.shop.IAppDetailModel;
import com.ubtechinc.alpha2ctrlapp.entity.AppDetail;
import com.ubtechinc.alpha2ctrlapp.entity.business.app.ApkDownLoad;
import com.ubtechinc.alpha2ctrlapp.entity.business.shop.CommentInfo;
import com.ubtechinc.alpha2ctrlapp.events.AppDownloadStatusChangeEvent;
import com.ubtechinc.alpha2ctrlapp.events.DownloadFileEvent;
import com.ubtechinc.alpha2ctrlapp.service.RobotManagerService;
import com.ubtechinc.alpha2ctrlapp.ui.activity.main.MainPageActivity;
import com.ubtechinc.alpha2ctrlapp.util.DownloadFileService;
import com.ubtechinc.alpha2ctrlapp.util.DownloadFileUtils;
import com.ubtechinc.alpha2ctrlapp.util.Tools;
import com.ubtechinc.nets.http.ThrowableWrapper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants.APP_STATE_INIT;
import static com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants.APP_STATE_MOBILE_APP_HAS_UPDATE;
import static com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants.APP_STATE_MOBILE_APP_INSTALLED;

/**
 * Created by tanghongyu on 2016/10/16.
 */

public class AppDetailPresenter {
    private static final String TAG = "AppDetailPresenter";
    private boolean hasCollected = false;
    private List<CommentInfo> commentList = new ArrayList<CommentInfo>();
    IAppDetailModel mAppDetailModel;
    IAppDetailView mAppDetailView;
    private List<CommentInfo> appCommentList = new ArrayList<>();
    private AppDetail mMainApp;
    private AppDetail mAndroidMobileApp;
    private AppDetail mRobotApp;
    private AppDetail mIOSApp;
    private int page = 1;
    private final int COUNT = 15;
    private int mAppStatus = APP_STATE_INIT;
    private Context mContext;
    private boolean hasAddGetMore = false;
    private boolean mIsFromDownload;

    public void setmIsFromDownload(boolean mIsFromDownload) {
        this.mIsFromDownload = mIsFromDownload;
    }

    public List<CommentInfo> getCommentList() {
        return commentList;
    }

    public AppDetailPresenter(Context context, final IAppDetailView appDetailView) {
        this.mAppDetailView = appDetailView;
        mAppDetailModel = new AppDetailModel(context);

        mContext = context;
    }


    public void getAppDetail(Context context, int appId) {
        mAppDetailView.showLoadingDialog();
        mAppDetailModel.getAppDetail(context, appId, new IAppDataSource.LoadAppDetailDataCallback() {
            @Override
            public void onLoadAppDetail(List<AppDetail> appDetails) {

                for (AppDetail app : appDetails) {
                    app.setAppPackage(app.getPackageName());
                    app.setAppImagePath(app.getAppIcon());
                    app.setAppHeadImage(app.getAppHeadImage());
                    int versionCode = 0;
                    try {
                        versionCode = Integer.valueOf(app.getAppVersion());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    app.setVersionCode(versionCode);
                    if (StringUtils.isEmpty(app.getAppVersionName()))
                        app.setAppVersionName(mContext.getString(R.string.error_none));
                    if (StringUtils.isEquals(app.getAppLinkId(), BusinessConstants.APP_LINK_MAIN)) {//当前查询到的主应用
                        mMainApp = app;
                    }
                    if (isMobileAndroidApp(app)) {
                        mAndroidMobileApp = app;
                    } else if (isRobotApp(app)) {
                        mRobotApp = app;
                    } else if (isIOSApp(app)) {
                        mIOSApp = app;
                    }
                }

                mAppDetailView.refreshAppIntroduction(mMainApp);
                // checkComment(1);

                if (mIsFromDownload) mAppDetailView.downloadButtonClick();
            }

            @Override
            public void onDataNotAvailable(ThrowableWrapper e) {

            }
        });
    }

    public void getAppShareUrls() {
        if (mMainApp != null) {
            getAppShareUrl();
        }
    }

    private void getAppShareUrl() {
        if (StringUtils.isEmpty(Constants.SHARE_APP_URL)) {
            mAppDetailView.showLoadingDialog();
            mAppDetailModel.getAppShareUrl(new IAppDetailModel.IGetShareUrlListener() {
                @Override
                public void onGetShareUrlResult(String url) {
                    Constants.SHARE_APP_URL = url;
                    mAppDetailView.getSharedUrlSuccess();
                    mAppDetailView.showLoadingDialog();
                    mAppDetailView.showShareDialog(mMainApp);
                }
            });
        } else {
            mAppDetailView.showShareDialog(mMainApp);
        }

    }

    public void getAppImageUrls() {
        if (mAndroidMobileApp != null && !StringUtils.isEmpty(mAndroidMobileApp.getAppScanshot())) {
            mAppDetailView.getAppImageUrls(mAndroidMobileApp.getAppScanshot());
        } else if (mIOSApp != null && !StringUtils.isEmpty(mIOSApp.getAppScanshot())) {
            mAppDetailView.getAppImageUrls(mIOSApp.getAppScanshot());
        } else if (mRobotApp != null && !StringUtils.isEmpty(mRobotApp.getAppScanshot())) {
            mAppDetailView.getAppImageUrls(mRobotApp.getAppScanshot());
        }

    }


    public void manageDownloadButton() {
        StringBuffer appListDesc = new StringBuffer();
        if (mAndroidMobileApp != null) {
            try {
                DecimalFormat df = new DecimalFormat("0.00");
                double d = Double.valueOf(mAndroidMobileApp.getAppSize()) / 1024.0;
                String db = df.format(d);
                appListDesc.append("Android -- 文件大小 :  " + db + "M" + "      " + mContext.getString(R.string.app_detail_version) + StringUtils.nullStringToDefault(mAndroidMobileApp.getAppVersionName()) + "\n");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (mRobotApp != null) {
            try {
                DecimalFormat df = new DecimalFormat("0.00");
                double d = Double.valueOf(mRobotApp.getAppSize()) / 1024.0;
                String db = df.format(d);
                appListDesc.append("Robot -- 文件大小 :  " + db + "M" + "      " + mContext.getString(R.string.app_detail_version) + StringUtils.nullStringToDefault(mRobotApp.getAppVersionName()) + "\n");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        if (mIOSApp != null) {
            try {
//                DecimalFormat df = new DecimalFormat("0.00");
//                double d = Double.valueOf(mIOSApp.getAppSize())/1024.0;
//                String db = df.format(d);
                appListDesc.append("IOS --  " + mContext.getString(R.string.app_detail_version) + StringUtils.nullStringToDefault(mIOSApp.getAppVersionName()) + "\n");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        mAppDetailView.setAppDetailList(appListDesc.toString());
        caculateDownloadStatusAndRefreshBtn();
        if (mMainApp.getIsCollect() == 0) {
            mAppDetailView.setCollectButton(false);
            hasCollected = false;
        } else {
            mAppDetailView.setCollectButton(true);
            hasCollected = true;
        }
    }



    public void registerEventBus() {
        EventBus.getDefault().register(this);

    }

    public void robotLostConnection(String macAddress) {
        mAppDetailView.lostConnection(macAddress);
    }

    public void unRegisterEventBus() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DownloadFileEvent event) {
        ApkDownLoad apkDownLoad = event.getDownLoad();
        switch (apkDownLoad.getDownloadStatus()) {
            case DownloadFileUtils.STATUS_COMPLETE:

                instanllApk(apkDownLoad);
                mAppDetailView.downloadComplete();
                break;
            case DownloadFileUtils.STATUS_FAIL:
                mAppDetailView.downloadFail();
                break;
            case DownloadFileUtils.STATUS_PAUSE:
                mAppDetailView.downloadComplete();
                break;
            case DownloadFileUtils.STATUS_IS_DOWNLOADING:
                break;
        }
    }


    /**
     * @param
     * @return
     * @throws
     * @Description 调起Apk安装程序安装App
     */
    private void instanllApk(ApkDownLoad apkDownLoad) {

//        String fileName = apkDownLoad.getFileName() + apkDownLoad.getVersion() + ".apk";
        String fileName = apkDownLoad.getFileName();
        File updateFile = FileUtils.getFileByPath(Constants.apk_dir + fileName);
        if (FileUtils.isFileExists(updateFile)) {
            try {
                Uri uri = Uri.fromFile(updateFile);

                Intent installIntent = new Intent(Intent.ACTION_VIEW);
                installIntent.setDataAndType(uri,
                        "application/vnd.android.package-archive");
                installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Alpha2Application.getAlpha2().startActivity(installIntent);
            } catch (Exception e) {
                mAppDetailView.installApkFail(0);
                e.printStackTrace();
            }

        } else {
            mAppDetailView.installApkFail(1);
        }
    }

    private boolean isMobileAndroidApp(AppDetail app) {

        return StringUtils.isEquals(BusinessConstants.SHOP_SDK_TYPE_ANDROID, app.getSdkType());
    }

    private boolean isRobotApp(AppDetail app) {

        return StringUtils.isEquals(BusinessConstants.SHOP_SDK_TYPE_ONBOARD, app.getSdkType());
    }

    private boolean isIOSApp(AppDetail app) {

        return StringUtils.isEquals(BusinessConstants.SHOP_SDK_TYPE_IOS, app.getSdkType());
    }

    public void setmAppStatus(int mAppStatus) {
        this.mAppStatus = mAppStatus;
    }

    public void onClickDownload(MainPageActivity activity, DownloadFileService downloadFileService) {

        if (mAppStatus == APP_STATE_INIT || mAppStatus == BusinessConstants.APP_STATE_INSTALL_FAIL || mAppStatus == BusinessConstants.APP_STATE_DOWNLOAD_FAIL || mAppStatus == BusinessConstants.APP_STATE_ROBOT_APP_HAS_UPDATE) {
         //   mAppDetailView.showLoadingDialog();
            downloadRobotApp(activity);

        } else if (mAppStatus == BusinessConstants.APP_STATE_ERROR || mAppStatus == BusinessConstants.APP_STATE_CAN_NOT_DOWNLOAD) {
            ToastUtils.showShortToast( R.string.shop_page_disable_download);
        } else if (mAppStatus == BusinessConstants.APP_STATE_APP_ALL_INSTALLED) {//手机端App已经安装，打开机器人端应用
//                if(mAndroidMobileApp != null) {
//                    try {
//                        Tools.runAppWithoutRepeat(mActivity, mAndroidMobileApp.getPackageName());
//                    } catch (PackageManager.NameNotFoundException e) {
////								ToastUtils.showShortToast( );
//                        e.printStackTrace();
//                    }
//                }
//                if(mRobotApp != null) {
//                    RobotApp app = new RobotApp();
//                    app.setName(mRobotApp.getAppName());
//                    app.setPackageName(mRobotApp.getPackageName());
//                    AppInstalledInfo newInfo = new AppInstalledInfo();
//                    newInfo.setApkInfo(app);
//                    newInfo.setAppImagePath(mRobotApp.getAppHeadImage());
//                    mMainPageActivity.startApp(newInfo);
//                }

        } else if (mAppStatus == BusinessConstants.APP_STATE_INSUFFCIENT_SPACE) {
            ToastUtils.showShortToast( R.string.news_storage_title);
        } else if (mAppStatus == BusinessConstants.APP_STATE_ONLY_MOBILE_APP_NEED_INTALL || mAppStatus == BusinessConstants.APP_STATE_MOBILE_APP_NEED_INTALL || mAppStatus == APP_STATE_MOBILE_APP_HAS_UPDATE) {
            downloadMobileApp(downloadFileService);
        }


    }

    IAppDetailModel.IDownloadListener listener = new IAppDetailModel.IDownloadListener() {
        @Override
        public void onDownloadResult(int code) {

        }
    };

    public void downloadRobotApp(MainPageActivity activity) {

        if (mRobotApp != null) {

            //没有机器人不可以下载App
            if (!RobotManagerService.getInstance().isConnectedRobot()) {
                ToastUtils.showShortToast( R.string.main_page_connect_alpha_tips);

            } else {
                mAppDetailModel.downloadRobotApp(mRobotApp, activity, listener);
                mAppDetailView.changeDownLoadStatus(BusinessConstants.APP_STATE_DOWNLOADING);
            }

        }

    }

    /**
     * @param
     * @return
     * @throws
     * @Description 下载手机端App
     */
    public boolean downloadMobileApp(DownloadFileService downloadFileService) {

        if (downloadFileService != null && mAndroidMobileApp != null && (isPkgInstalled(mAndroidMobileApp.getPackageName()) == APP_STATE_INIT || Tools.isAppHasUpdate(mContext, mAndroidMobileApp.getPackageName(), mAndroidMobileApp.getVersionCode()))) {
            mAppDetailView.showLoadingDialog(true, 40);
            ApkDownLoad apkDownLoad = new ApkDownLoad();
            apkDownLoad.setDownloadUrl(mAndroidMobileApp.getAppPath());
            apkDownLoad.setFileName(mAndroidMobileApp.getAppName() + ".apk");
            apkDownLoad.setFileSaveFolder(Constants.apk_dir);
            apkDownLoad.setVersion(mAndroidMobileApp.getAppVersion());
            apkDownLoad.setPackageName(mAndroidMobileApp.getPackageName());
            downloadFileService.startDownload(apkDownLoad);
            return true;


        }
        return false;
    }


    /**
     * @param
     * @return
     * @throws
     * @Description 处理App下载00
     */
    //关系可能是 IOS、IOS-(Android+Robot)、IOS-(Robot)、IOS-(Android)、Android、Android-(IOS+Robot)、Android-(IOS)、Android-(Robot)、Robot、Robot-(Android+IOS)、Robot-(IOS)、Robot-(Android)
    //同一功能性App会由开发者发布时做关联，如果未关联，人工审核时也会做关联。
    //优先判断机器人端App状态，只有机器人端和手机端同时安装完才算OK
    public void caculateDownloadStatusAndRefreshBtn() {

        if (ListUtils.isEmpty(RobotManagerService.getInstance().getRobotModelList()) || (mAndroidMobileApp == null && mRobotApp == null) || (mAndroidMobileApp != null && StringUtils.isEmpty(mAndroidMobileApp.getAppPath())) || (mRobotApp != null && StringUtils.isEmpty(mRobotApp.getAppPath()))) {
            //没有机器人和Android手机客户端，则不可以下载（只有IOS客户端）
            mAppStatus = BusinessConstants.APP_STATE_ERROR;


        } else {

            if (isRobotApp(mMainApp)) {//机器人客户端为主程序
                caculateAppPackageStatusWithRobotApp();
            } else if (isMobileAndroidApp(mMainApp)) {//Android为主程序
                if (mRobotApp != null) {//以RobotApp状态优先为准
                    caculateAppPackageStatusWithRobotApp();
                } else {//没有机器人客户端

                    caculateAppPackageStatusWithoutRobotApp();
                }

            } else {//IOS为主程序
                if (mRobotApp != null) {
                    caculateAppPackageStatusWithRobotApp();
                } else {////没有机器人客户端，取Android App的安装状态

                    caculateAppPackageStatusWithoutRobotApp();
                }
            }


        }
        mAppDetailView.refreshDownloadIcon(mAppStatus);
    }

    private void parseMobileAppStatus() {
        if (isPkgInstalled(mAndroidMobileApp.getPackageName()) == APP_STATE_MOBILE_APP_INSTALLED) {
            if (Tools.isAppHasUpdate(mContext, mAndroidMobileApp.getPackageName(), mAndroidMobileApp.getVersionCode())) {
                mAppStatus = APP_STATE_MOBILE_APP_HAS_UPDATE;
            } else {
                mAppStatus = APP_STATE_MOBILE_APP_INSTALLED;
            }

        } else {
            mAppStatus = BusinessConstants.APP_STATE_MOBILE_APP_NEED_INTALL;
        }

    }

    private void caculateAppPackageStatusWithoutRobotApp() {
        if (mAndroidMobileApp != null) {//有则要判断手机端App安装
            parseMobileAppStatus();
            if (mAppStatus == APP_STATE_MOBILE_APP_INSTALLED) {
                mAppStatus = BusinessConstants.APP_STATE_APP_ALL_INSTALLED;
            } else if (mAppStatus == APP_STATE_MOBILE_APP_HAS_UPDATE) {
                mAppStatus = BusinessConstants.APP_STATE_ROBOT_APP_HAS_UPDATE;
            } else {
                mAppStatus = BusinessConstants.APP_STATE_ONLY_MOBILE_APP_NEED_INTALL;
            }
        }

    }

    private void caculateAppPackageStatusWithRobotApp() {
        mAppStatus = MainPageActivity.dao.queryAppStatus(mMainApp.getPackageName(), mMainApp.getVersionCode());
        if (mAppStatus == BusinessConstants.APP_STATE_INSTALL_SUCCESS) {//如果是机器人已经安装App，则要改变它的状态
            if (mAndroidMobileApp != null) {//有则要判断手机端App安装
                caculateAppPackageStatusWithoutRobotApp();
            } else {
                mAppStatus = BusinessConstants.APP_STATE_APP_ALL_INSTALLED;
            }
        }
    }


    private int isPkgInstalled(String packageName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = mContext.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return APP_STATE_INIT;
        } else {
            return APP_STATE_MOBILE_APP_INSTALLED;
        }
    }

    /**
     * @param
     * @return
     * @throws
     * @Description 收藏或不收藏
     */
    public void doCollector() {
        if (mMainApp != null) {

            if (hasCollected) {
                mAppDetailModel.cancleCollect(mMainApp.getAppId(), new IAppDetailModel.ICancelCollectListener() {
                    @Override
                    public void onCancelCollectResult(boolean isSuccess) {
                        if (isSuccess) {
                            hasCollected = false;
                            mAppDetailView.cancelCollect(true);
                        } else {
                            mAppDetailView.cancelCollect(false);
                        }

                    }
                });
            } else {

                mAppDetailModel.addCollector(mMainApp.getAppId(), mMainApp, new IAppDetailModel.ICollectListener() {
                    @Override
                    public void onCollectResult(boolean isSuccess) {
                        if (isSuccess) hasCollected = true;
                        mAppDetailView.collect(isSuccess);
                    }

                    @Override
                    public void onRepeat() {
                        hasCollected = true;
                        mAppDetailView.collectRepeat();
                    }
                });
            }
        }
    }


    private void refreshCommentList(List<CommentInfo> commentList) {
        this.commentList.clear();
//		Collections.sort(commentList);
        this.commentList = commentList;
//	   this.appCommentList.clear();
        if (mMainApp != null) {
//		   appCommentList.add(mMainApp);
            for (int i = 0; i < commentList.size(); i++) {
                appCommentList.add(commentList.get(i));
            }

            // 当数据有过15条，且不是获取最新的15条的时候，出现加载更多
            if (appCommentList.size() >= COUNT && !hasAddGetMore) {
                mAppDetailView.addOrRemoveFooterView(true);
                hasAddGetMore = true;
            } else if (commentList.size() < COUNT && hasAddGetMore) {
                mAppDetailView.addOrRemoveFooterView(false);
                hasAddGetMore = false;
            }
            mAppDetailView.refreshCommentList(appCommentList);
        }


    }

    private boolean mIsInit = false;

    public void setIsInit(boolean mIsInit) {
        this.mIsInit = mIsInit;
    }

    public void onResume() {

        if (mIsInit && mMainApp != null) {
            caculateDownloadStatusAndRefreshBtn();

        }

    }


    public int getAppCategory(String appType) {
        int resource = R.string.shop_app_type_family_entertainment;
        if (StringUtils.isEmpty(appType)) return resource;
        switch (appType) {

            case BusinessConstants.SHOP_APP_TYPE_FAMILY_ENTERTAILMENT:
                resource = R.string.shop_app_type_family_entertainment;
                break;
            case BusinessConstants.SHOP_APP_TYPE_EDUCATION:
                resource = R.string.shop_app_type_education;
                break;
            case BusinessConstants.SHOP_APP_TYPE_COMMERCIAL_PERFOR:
                resource = R.string.shop_app_type_commercial_perfor;
                break;
            case BusinessConstants.SHOP_APP_MEIDICAL_CARE:
                resource = R.string.shop_app_type_medical_care;
                break;
            case BusinessConstants.SHOP_APP_TYPE_OFFIC_ASSISTANT:
                resource = R.string.shop_app_type_office_assistant;
                break;
            case BusinessConstants.SHOP_APP_TYPE_OTHER:
                resource = R.string.shop_app_type_other;
                break;

        }

        return resource;

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AppDownloadStatusChangeEvent event) {
        Logger.t(TAG).d( "onEvent AppDownloadStatusChangeEvent state = %d" +event.getAppEntrityInfo().getDownloadState() );
        mAppStatus = event.getAppEntrityInfo().getDownloadState();
        mAppDetailView.changeDownLoadStatus(mAppStatus);

    }
}
