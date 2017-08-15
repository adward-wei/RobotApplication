package com.ubtechinc.alpha2ctrlapp.ui.fragment.shop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.ActivityUtils;
import com.ubtech.utilcode.utils.ListUtils;
import com.ubtech.utilcode.utils.SPUtils;
import com.ubtech.utilcode.utils.StringUtils;
import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.constants.IntentConstants;
import com.ubtechinc.alpha2ctrlapp.data.shop.ADPromotionReponsitory;
import com.ubtechinc.alpha2ctrlapp.data.shop.ActionRepository;
import com.ubtechinc.alpha2ctrlapp.data.shop.AppReponsitory;
import com.ubtechinc.alpha2ctrlapp.data.shop.IADDataSource;
import com.ubtechinc.alpha2ctrlapp.data.shop.IActionDataSource;
import com.ubtechinc.alpha2ctrlapp.data.shop.IAppDataSource;
import com.ubtechinc.alpha2ctrlapp.database.ActionEntrityInfo;
import com.ubtechinc.alpha2ctrlapp.database.ActionInfoProvider;
import com.ubtechinc.alpha2ctrlapp.database.AppInfoProvider;
import com.ubtechinc.alpha2ctrlapp.database.RobotAppEntrity;
import com.ubtechinc.alpha2ctrlapp.entity.AppUpdate;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.ActionDownLoad;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.AlphaParam;
import com.ubtechinc.alpha2ctrlapp.entity.business.shop.ActionInfo;
import com.ubtechinc.alpha2ctrlapp.entity.business.shop.AppInfo;
import com.ubtechinc.alpha2ctrlapp.entity.business.shop.RecommenedPageInfo;
import com.ubtechinc.alpha2ctrlapp.service.RobotManagerService;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.app.HelpActivity;
import com.ubtechinc.alpha2ctrlapp.util.BeanUtils;
import com.ubtechinc.nets.http.ThrowableWrapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author：tanghongyu
 * @date：5/17/2017 5:21 PM
 * @modifier：tanghongyu
 * @modify_date：5/17/2017 5:21 PM
 * [A brief description]
 * version
 */

public class ShopPresenter implements ShopContract.Presenter {
    private List<ActionDownLoad> mActionList = new ArrayList<ActionDownLoad>();
    ActionRepository actionRepository;
    Alpha2Application mContext;
    ShopContract.View mView;
    ADPromotionReponsitory adReponsitory;
    AppReponsitory appReponsitory;
    //主程序列表，可能包含
    private List<AppUpdate> mAppMainList = new ArrayList<AppUpdate>();

    //子程序列表
    private List<AppUpdate> mAppLinkedList = new ArrayList<AppUpdate>();
    private List<RecommenedPageInfo> mPageList = new ArrayList<RecommenedPageInfo>();
    private String[] mADImageList;
    private boolean mIsCreate = false;
    private boolean mIsRefresh = false;
    private final int TYPE_APP = 0;
    private final int TYPE_ACTION = 1;
    private   int mShopType = TYPE_APP; // 0 表示应用 1表示动作
    private AppInfo downloadingAppinfo;
    private ActionDownLoad mAcitonDownLoadInfo;
    private Activity activity;
    public ShopPresenter(@NonNull Activity activity, @NonNull ShopContract.View view, @NonNull ActionRepository actionRepository , @NonNull ADPromotionReponsitory adReponsitory , @NonNull AppReponsitory appReponsitory) {
        Preconditions.checkNotNull(activity);
        Preconditions.checkNotNull(view);
        Preconditions.checkNotNull(actionRepository);
        this.mContext = (Alpha2Application) activity.getApplicationContext();
        this.mView = view;
        this.activity = activity;
        this.actionRepository = actionRepository;
        this.adReponsitory = adReponsitory;
        this.appReponsitory = appReponsitory;
        mView.setPresenter(this);

    }
    @Override
    public void initData() {

    }

    @Override
    public void getLastActionList() {
        actionRepository.getLastActionList(1, 15,new IActionDataSource.LoadActionCallback() {
            @Override
            public void onActionLoaded(List<ActionInfo> tasks) {
                parseActionList(tasks);
                getFrontPagePic();
            }

            @Override
            public void onDataNotAvailable(ThrowableWrapper e) {
                ToastUtils.showShortToast(e.getMessage());
            }


        });
    }

    @Override
    public void getFrontPagePic() {
        adReponsitory.getAdvertisingPromotion("CN", new IADDataSource.ADDataCallback() {
            @Override
            public void onLoadADData(List<RecommenedPageInfo> loginResponses) {
                mPageList.clear();
                mPageList = loginResponses;
                getImageUrl();
            }

            @Override
            public void onDataNotAvailable(ThrowableWrapper e) {
                ToastUtils.showShortToast(e.getMessage());
            }

        });
    }

    @Override
    public void getAppList() {
        appReponsitory.loadAppList(1, 20, new IAppDataSource.AppListDataCallback() {
            @Override
            public void onLoadAppList(List<AppInfo> loginResponses) {
                refreshListData(loginResponses);
                getFrontPagePic();
            }

            @Override
            public void onDataNotAvailable(ThrowableWrapper e) {

            }


        });
    }

    @Override
    public void parseActionList(List<ActionInfo> list) {
            ActionDownLoad downloadInfo;
            mActionList.clear();
            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    downloadInfo = new ActionDownLoad();
                    ActionInfo acitonListInfo = list.get(i);
                    BeanUtils.copyBean(acitonListInfo, downloadInfo);
                    if (!RobotManagerService.getInstance().isConnectedRobot() || TextUtils.isEmpty(acitonListInfo.getActionPath())) {
                        downloadInfo.setStatus(BusinessConstants.APP_STATE_ERROR);
                    }else {
                        ActionEntrityInfo actionEntrityInfo = null;
                        if (TextUtils.isEmpty(acitonListInfo.getActionOriginalId())) {
                            actionEntrityInfo = ActionInfoProvider.get().findActionByParam(ImmutableMap.of("actionName", list.get(i).getActionName(), "actionPath", (Object)acitonListInfo.getActionPath()));
                        } else {
                            actionEntrityInfo =  ActionInfoProvider.get().findActionByParam(ImmutableMap.of("actionName", list.get(i).getActionName(), "actionPath", (Object)acitonListInfo.getActionPath()));

                        }
                        downloadInfo.setStatus(actionEntrityInfo != null ? actionEntrityInfo.getDownloadState(): BusinessConstants.APP_STATE_ERROR);

                    }

                    mActionList.add(downloadInfo);

                }
                mView.setMoreLayVisiable(true);
            } else {
                mView.setMoreLayVisiable(false);
            }
            mIsCreate = true;
            if (mIsRefresh) {
                mView.refreshComplete();
                mIsRefresh = false;
            }

        }

    @Override
    public void pageClick(int position) {
        if (!ListUtils.isEmpty(mPageList)) {
            RecommenedPageInfo recommenedPageInfo = mPageList.get(position);
            if (null != recommenedPageInfo) {

                String recomdUrl = recommenedPageInfo.getRecommendUrl();
                JSONObject object = JSONObject.parseObject(recomdUrl);
                JSONArray array = object.getJSONArray("EN");
                JSONObject itemObject = array.getJSONObject(0);
                int type = itemObject.getIntValue("bannerType");
                if (type == 1) {
                    String url = itemObject.getString("bannerDetail");
                    Logger.d( url.toString());
                    if (!TextUtils.isEmpty(url)) {
                        Intent intent = new Intent(mContext, HelpActivity.class);
                        intent.putExtra("title",mContext.getString(R.string.event_title_name));
                        intent.putExtra("url", url);
                        mContext.startActivity(intent);
                    }
                }
            }

        }
    }

    @Override
    public void searchLayClick() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(IntentConstants.DATA_IS_LOCAL_DATA, false);
        if (mShopType == TYPE_ACTION) {
            bundle.putBoolean(IntentConstants.DATA_IS_ACTION_SEARCH, true);
        } else
            bundle.putBoolean(IntentConstants.DATA_IS_ACTION_SEARCH, false);
        ActivityUtils.replaceFragment(activity, activity.getFragmentManager(), SearchFragment.class.getName(),R.id.layout_fragment_contanier, bundle);
    }

    @Override
    public void refreshGridView() {
        mIsRefresh = true;
        if (mShopType == TYPE_APP) {
            getAppList();
        } else {
            getLastActionList();
        }
    }
    @Override
    public int getShopType() {
        return mShopType;
    }

    @Override
    public void onChangeActive(int index) {
        if (mShopType == index) {
            return;
        } else {
            mShopType = index;
        }
        switch (index) {
            case TYPE_APP:
                mView.initAppView();
                break;
            case TYPE_ACTION:
                mView.initActionView();
                break;
            default:
                break;
        }
    }

    @Override
    public void setAppDownload(AppInfo appInfo) {
        this.downloadingAppinfo = appInfo;
    }

    @Override
    public void setActionDownload(ActionDownLoad appInfo) {
        this.mAcitonDownLoadInfo = appInfo;
    }

    @Override
    public List<AppUpdate> getAppMainList() {
        return mAppMainList;
    }
    @Override
    public List<ActionDownLoad> getActionList() {
        return mActionList;
    }

    @Override
    public void resume() {
        if (mShopType == TYPE_APP) {
            if (mIsCreate && ListUtils.getSize(mAppMainList) > 0) {
                resetStatus();
            }
        } else {
            if (mIsCreate && ListUtils.getSize(mActionList) > 0) {
                resetActionStatus();
            }
        }
    }

    @Override
    public void onDestory() {
        mIsCreate = false;
    }


    private void refreshListData(List<AppInfo> list) {
        AppInfo info;
        AppUpdate downloadInfo;
        mAppMainList.clear();
        mAppLinkedList.clear();
        if (!ListUtils.isEmpty(list)) {
            mView.setMoreAppVisiable(true);
            for (int i = 0; i < list.size(); i++) {
                info = list.get(i);
                downloadInfo = new AppUpdate();
                downloadInfo.setVersionCode(info.getVersionCode());
                downloadInfo.setStatus(BusinessConstants.APP_STATE_INIT);
                downloadInfo.setStatusTv(mContext.getString(R.string.shop_page_download));
                downloadInfo.setAppImagePath(info.getAppImagePath());
                downloadInfo.setAppName(info.getAppName());
                downloadInfo.setAppSize(info.getAppSize());
                downloadInfo.setAppVersion(info.getAppVersion());
                downloadInfo.setAppVersionName(info.getAppVersionName());
                downloadInfo.setAppPackage(info.getAppPackage());
                downloadInfo.setAppLanguageDesciber(info.getAppLanguageDesciber());
                downloadInfo.setAppLanguageName(info.getAppLanguageName());
                downloadInfo.setAppPath(info.getAppPath());
                downloadInfo.setAppCategory(info.getAppCategory());
                downloadInfo.setAppId(info.getAppId());
                downloadInfo.setAppResume(info.getAppResume());
                downloadInfo.setAppHeadImage(info.getAppHeadImage());
                /********************新SDK平台属性***********************/
                downloadInfo.setAppIcon(info.getAppIcon());
                downloadInfo.setAppDesc(info.getAppDesc());
                downloadInfo.setAppLinkId(info.getAppLinkId());
                downloadInfo.setSdkType(info.getSdkType());
                downloadInfo.setPackageName(info.getPackageName());
                downloadInfo.setAppLanguageDesciber(info.getAppLanguageDesciber());
                if (!RobotManagerService.getInstance().isConnectedRobot() || StringUtils.isEmpty(info.getAppPath())) {
                    downloadInfo.setStatus(BusinessConstants.APP_STATE_ERROR);
                } else {

                    RobotAppEntrity appInfo = AppInfoProvider.get().findAppByParam(ImmutableMap.of("packageName", list.get(i).getPackageName(), "appVersion", (Object) list.get(i).getAppVersion()));
                    if (appInfo != null) downloadInfo.setStatus(appInfo.getDownloadState());
                }
                if (downloadInfo.getPackageName().equals(BusinessConstants.PACKAGENAME_SHOP_ACTION) || downloadInfo.getPackageName().equals(BusinessConstants.PACKAGENAME_SHOP_ALARM)) {
                    downloadInfo.setStatus(BusinessConstants.APP_STATE_INSTALL_SUCCESS);
                }
                AlphaParam alphaParam = (AlphaParam) SPUtils.get().getObject( AlphaParam.class);

                if (null != alphaParam) {
                    if (alphaParam.getServiceLanguage().equalsIgnoreCase(BusinessConstants.ROBOT_SERVICE_LANGUAGE_ENGLISH)) {
                        if (downloadInfo.getPackageName().equals(BusinessConstants.PACKAGENAME_CH_CHAT)
                                || downloadInfo.getAppName().equals(Constants.app_chat_baike)
                                || downloadInfo.getAppName().equals(Constants.app_chat_count)
                                || downloadInfo.getAppName().equals(Constants.app_chat_huil)
                                || downloadInfo.getAppName().equals(Constants.app_chat_joke)
                                || downloadInfo.getAppName().equals(Constants.app_chat_music)
                                || downloadInfo.getAppName().equals(Constants.app_chat_poem)
                                || downloadInfo.getAppName().equals(Constants.app_chat_story)
                                || downloadInfo.getAppName().equals(Constants.app_chat_weather)
                                || downloadInfo.getAppName().equals(Constants.app_chat_time)
                                || downloadInfo.getAppName().equals(Constants.app_chat_clock)
                                ) {
                            continue;
                        }
                    } else if (alphaParam.getServiceLanguage().equalsIgnoreCase(BusinessConstants.ROBOT_SERVICE_LANGUAGE_CHINESE)) {
                        if (downloadInfo.getPackageName().equals(BusinessConstants.PACKAGENAME_EN_CHAT)) {
                            continue;
                        }
                    }
                }

                if (StringUtils.isEquals(downloadInfo.getAppLinkId(), BusinessConstants.APP_LINK_MAIN)) {//主程序
                    mAppMainList.add(downloadInfo);
                } else {//子程序
                    if (StringUtils.isEquals(downloadInfo.getSdkType(), BusinessConstants.SHOP_SDK_TYPE_ANDROID))//只取Android手机客户端
                        mAppLinkedList.add(downloadInfo);

                }

            }

        } else {
            mView.setMoreAppVisiable(false);
        }
        manageLinkedAppList();
        mView.notifyAppAdapter(mAppMainList);
        mIsCreate = true;
        if (mIsRefresh) {

            mView.refreshComplete();
            mIsRefresh = false;
        }
    }

    /**
     * @param
     * @return
     * @throws
     * @Description 将有关联关系的子程序加入到主程序中
     */
    private void manageLinkedAppList() {
        //关系可能是 IOS、IOS-(Android+Robot)、IOS-(Robot)、IOS-(Android)、Android、Android-(IOS+Robot)、Android-(IOS)、Android-(Robot)、Robot、Robot-(Android+IOS)、Robot-(IOS)、Robot-(Android)
        //同一功能性App会由开发者发布时做关联，如果未关联，人工审核时也会做关联。
        for (AppUpdate app : mAppMainList) {
            app.setMainApp(app);
            Iterator<AppUpdate> iterator = mAppLinkedList.iterator();
            while (iterator.hasNext()) {
                AppUpdate linkApp = iterator.next();
                if (StringUtils.isEquals(linkApp.getAppLinkId(), String.valueOf(app.getAppId()))) {
                    if (isMobileAndroidApp(linkApp)) {
                        app.setAndroidApp(linkApp);
                    } else if (isRobotApp(linkApp)) {
                        app.setRobotApp(linkApp);
                    } else {
                        app.setIosApp(linkApp);
                    }
                    app.getLinkedAppList().add(linkApp);
                    iterator.remove();//减少遍历次数
                }
            }

        }

    }


    private boolean isMobileAndroidApp(AppUpdate app) {

        return StringUtils.isEquals(BusinessConstants.SHOP_SDK_TYPE_ANDROID, app.getSdkType());
    }

    private boolean isRobotApp(AppUpdate app) {

        return StringUtils.isEquals(BusinessConstants.SHOP_SDK_TYPE_ONBOARD, app.getSdkType());
    }

    /**
     * @Description 设置动作下载状态
     * @param
     * @return
     * @throws
     */
    private void resetActionStatus() {
        for (int i = 0; i < mActionList.size(); i++) {
            ActionEntrityInfo actionEntrityInfo = null;
            ActionDownLoad actionDownLoad =  mActionList.get(i);
            if (TextUtils.isEmpty(actionDownLoad.getActionOriginalId())) {

                actionEntrityInfo =  ActionInfoProvider.get().findActionByParam(ImmutableMap.of("actionName", actionDownLoad.getActionName(), "actionPath", (Object) actionDownLoad.getActionPath()));
            } else {
                actionEntrityInfo =  ActionInfoProvider.get().findActionByParam(ImmutableMap.of("actionOriginalId", actionDownLoad.getActionOriginalId(), "actionPath", (Object) actionDownLoad.getActionPath()));
            }
            if(actionEntrityInfo != null ) {
                actionDownLoad.setStatus(actionEntrityInfo.getDownloadState());
            }
        }
        mView.notifyActionAdapter(mActionList);

    }


    /**
     * @param
     * @return
     * @throws
     * @Description 重置下载状态
     */
    private void resetStatus() {
        for (AppUpdate app : mAppMainList) {
            if (!RobotManagerService.getInstance().isConnectedRobot() || StringUtils.isEmpty(app.getAppPath())) {
                app.setStatus(BusinessConstants.APP_STATE_ERROR);
            } else {
                if (app.getStatus() == BusinessConstants.APP_STATE_ERROR && RobotManagerService.getInstance().isConnectedRobot() && !StringUtils.isEmpty(app.getAppPath())) {
                        app.setStatus(BusinessConstants.APP_STATE_INIT);
                } else {

                    RobotAppEntrity appInfo = AppInfoProvider.get().findAppByParam(ImmutableMap.of("packageName", app.getPackageName(), "appVersion", (Object) app.getAppVersion()));
                    if (appInfo != null) app.setStatus(appInfo.getDownloadState());

                }


            }

        }
        mView.notifyAppAdapter(mAppMainList);
    }

    private void getImageUrl() {
        mADImageList = new String[mPageList.size()];
        for (int i = 0; i < mPageList.size(); i++) {
            mADImageList[i] = mPageList.get(i).getRecommendImage();
            if (i == mPageList.size() - 1) {
                mView.initAD(mADImageList);
                break;
            }
        }
    }

}
