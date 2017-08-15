package com.ubtechinc.alpha2ctrlapp.ui.fragment.shop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.StringUtils;
import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.BaseHandler;
import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.constants.IntentConstants;
import com.ubtechinc.alpha2ctrlapp.data.shop.AppRemoteDataSource;
import com.ubtechinc.alpha2ctrlapp.data.shop.AppReponsitory;
import com.ubtechinc.alpha2ctrlapp.data.shop.IAppDataSource;
import com.ubtechinc.alpha2ctrlapp.entity.AppUpdate;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.RobotApp;
import com.ubtechinc.alpha2ctrlapp.entity.business.shop.AppInfo;
import com.ubtechinc.alpha2ctrlapp.entity.request.AppSearchRequest;
import com.ubtechinc.alpha2ctrlapp.events.AppDownloadStatusChangeEvent;
import com.ubtechinc.alpha2ctrlapp.service.RobotManagerService;
import com.ubtechinc.alpha2ctrlapp.third.AidlService;
import com.ubtechinc.alpha2ctrlapp.ui.activity.main.MainPageActivity;
import com.ubtechinc.alpha2ctrlapp.ui.adapter.shop.AppListInfoAdpter;
import com.ubtechinc.alpha2ctrlapp.ui.adapter.shop.AppMoreAdpter;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.base.BaseContactFragememt;
import com.ubtechinc.alpha2ctrlapp.widget.RefreshListView;
import com.ubtechinc.alpha2ctrlapp.widget.RefreshListView.OnRefreshListener;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.LoadingDialog;
import com.ubtechinc.nets.http.ThrowableWrapper;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AppMoreListFragement extends BaseContactFragememt  {

    private String TAG = "AppMoreListFragement";
    private RefreshListView list_app;
    private AppMoreAdpter listapter;
    //主程序列表，可能包含
    private List<AppUpdate> mAppMainList = new ArrayList<AppUpdate>();

    //子程序列表
    private List<AppUpdate> mAppLinkedList = new ArrayList<AppUpdate>();
    private AppInfo downloadingAppinfo;
    private View footerView;
    private boolean isRefresh = false;
    private boolean isSearch;
    private int page = 1;
    private String searchKey;
    private boolean isGetMore = false;
    private boolean hasAddMore = false;
    private final int COUNT = 15;
    private boolean hasCreate = false;
    private RelativeLayout no_search_tip;
    private ImageButton btn_search_icon;
    private List<AppInfo> appSearchList = new ArrayList<AppInfo>();
    private UnLineBroadcastReceiver mUnLineBroadcastReceiver;
    AppReponsitory appReponsitory;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        isSearch = bundle.getBoolean("isSearch");
        if (isSearch) {
            searchKey = bundle.getString("name");
            appSearchList = (List<AppInfo>) bundle.getSerializable("list");
        }
        IntentFilter intentFilter = new IntentFilter(AidlService.RECEIVE_ERROR_DATA);
        mUnLineBroadcastReceiver = new UnLineBroadcastReceiver();
        mActivity.registerReceiver(mUnLineBroadcastReceiver, intentFilter);

        EventBus.getDefault().register(this);
    }



    @Override
    public View onCreateFragmentView(LayoutInflater inflater,
                                     ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return inflater.inflate(R.layout.app_more, container, false);
    }

    @Override
    public void initView() {
        appReponsitory = AppReponsitory.getInstance(AppRemoteDataSource.getInstance(mActivity));
        list_app = (RefreshListView) mContentView.findViewById(R.id.lst_app);
        listapter = new AppMoreAdpter(mActivity, mAppMainList, list_app, new AppListInfoAdpter.DownLoadListener() {
            @Override
            public void startDownLoad(AppInfo appInfo) {
                downloadingAppinfo = appInfo;
            }
        });
        btn_search_icon = (ImageButton) mContentView.findViewById(R.id.btn_search_icon);
        btn_search_icon.setVisibility(View.VISIBLE);

        list_app.setAdapter(listapter);
        title.setText(getString(R.string.app_list));
        list_app.setOnItemClickListener(listapter);
        mMainPageActivity.topLayout.setVisibility(View.VISIBLE);
        no_search_tip = (RelativeLayout) mContentView.findViewById(R.id.no_search_tip);
        footerView = (RelativeLayout) LayoutInflater.from(mActivity).inflate(R.layout.get_more_layout, null, false);
        /**列表的下拉刷新**/
        list_app.setonRefreshListener(new OnRefreshListener() {
            public void onRefresh() {
                isRefresh = true;
                if (isSearch) {
                    searchApp(searchKey);
                } else {
                    getAppList(1);
                }
            }
        });
        footerView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                LoadingDialog.getInstance(mActivity).show();
                getAppList(page + 1);
                isGetMore = true;
            }
        });

        btn_search_icon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                bundle = new Bundle();
                bundle.putBoolean(IntentConstants.DATA_IS_LOCAL_DATA, false);
                bundle.putBoolean(IntentConstants.DATA_IS_ACTION_SEARCH, false);
                replaceFragment(SearchFragment.class.getName(), bundle);

            }
        });
        if (isSearch) {
            refreshListData(appSearchList);
        } else {
            LoadingDialog.getInstance(mActivity).show();
            getAppList(1);
        }
    }




    private class MHandler extends BaseHandler {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            if (msg == null) {
                Logger.i( "handleMessage msg is null.");
                return;
            }
            switch (msg.what) {





            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AppDownloadStatusChangeEvent event) {
        Logger.t(TAG).d( "onEvent AppDownloadStatusChangeEvent state = %s" +event.getAppEntrityInfo().toString() );
        if (event.getAppEntrityInfo().getDownloadState() == 4) {
            if (null != downloadingAppinfo) {
                Logger.d("appdown", "appListFragment下载成功");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("appName", downloadingAppinfo.getAppName());
                MobclickAgent.onEvent(mActivity, Constants.YOUMENT_SHOP_APP_DOWNLOAD_TIMES, map);
                downloadingAppinfo = null;
            }
            LoadingDialog.getInstance(mActivity).show();
            getAppList(1);
        } else if (event.getAppEntrityInfo().getDownloadState() == 5) {
            if (null != downloadingAppinfo) {
                LoadingDialog.getInstance(mActivity).show();
                getAppList(1);
                downloadingAppinfo = null;
            }
            ToastUtils.showLongToast( mActivity.getString(R.string.shop_page_installed_failed));
        }
    }
    private void refreshListData(List<AppInfo> list) {
        AppInfo info;
        AppUpdate downloadInfo;
        if (!isGetMore) {
            mAppMainList.clear();
            mAppLinkedList.clear();
        } else {
            isGetMore = false;
        }

        if (list.size() > 0) {
            no_search_tip.setVisibility(View.GONE);
            list_app.setVisibility(View.VISIBLE);
            for (int i = 0; i < list.size(); i++) {
                info = list.get(i);
                downloadInfo = new AppUpdate();
                downloadInfo.setVersionCode(info.getVersionCode());
                downloadInfo.setStatus(0);
                downloadInfo.setStatusTv(getString(R.string.shop_page_download));
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

                if (!RobotManagerService.getInstance().isConnectedRobot() || TextUtils.isEmpty(info.getAppPath())) {
                    downloadInfo.setStatus(-1);
                } else {
                    downloadInfo.setStatus(MainPageActivity.dao.queryAppStatus(
                            list.get(i).getPackageName(), Integer.parseInt(list.get(i).getAppVersion())));
                }

                if (downloadInfo.getPackageName().equals(BusinessConstants.PACKAGENAME_SHOP_ACTION) || downloadInfo.getPackageName().equals(BusinessConstants.PACKAGENAME_SHOP_ALARM)) {
                    downloadInfo.setStatus(BusinessConstants.APP_STATE_INSTALL_SUCCESS);
                }
                if (null != MainPageActivity.alphaParam) {
                    if (MainPageActivity.alphaParam.getServiceLanguage().equalsIgnoreCase(BusinessConstants.ROBOT_SERVICE_LANGUAGE_ENGLISH)) {
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
                                || downloadInfo.getAppName().equals(Constants.app_chat_clock)) {
                            continue;
                        }
                    } else if (MainPageActivity.alphaParam.getServiceLanguage().equalsIgnoreCase(BusinessConstants.ROBOT_SERVICE_LANGUAGE_CHINESE)) {
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
            if (list.size() == 0 && isSearch) {
                no_search_tip.setVisibility(View.VISIBLE);
                list_app.setVisibility(View.GONE);
            }
        }
        if (list.size() >= COUNT && !isSearch && !hasAddMore) {
            list_app.addFooterView(footerView);
            hasAddMore = true;
        } else if (list.size() < COUNT && hasAddMore) {
            list_app.removeFooterView(footerView);
            hasAddMore = false;
        }

        if (isRefresh) {
            list_app.onRefreshComplete();
            isRefresh = false;
        }
        hasCreate = true;
        listapter.onNotifyDataSetChanged(mAppMainList, list_app);
    }

    private void changeDownLoadStatus(int downloadStatus, RobotApp info) {
        String pakageName = info.getPackageName();
        for (int i = 0; i < mAppMainList.size(); i++) {
            if (mAppMainList.get(i).getAppPackage().equals(pakageName)) {
                mAppMainList.get(i).setStatus(downloadStatus);
                switch (downloadStatus) {
                    case 0:
                        mAppMainList.get(i).setStatusTv(mActivity.getString(R.string.shop_page_download));
                        break;
                    case 1:
                        mAppMainList.get(i).setStatusTv(mActivity.getString(R.string.shop_page_downloading));
                        break;
                    case 2:
                        mAppMainList.get(i).setStatusTv(mActivity.getString(R.string.shop_page_installeding));
                        break;
                    case 3:
                        mAppMainList.get(i).setStatusTv(mActivity.getString(R.string.shop_page_download_falied));
                        break;
                    case 4:
                        mAppMainList.get(i).setStatusTv(mActivity.getString(R.string.shop_page_installed_success));
                        break;
                    case 5:
                        mAppMainList.get(i).setStatusTv(mActivity.getString(R.string.shop_page_installed_failed));
                        break;
                    case BusinessConstants.APP_STATE_INSUFFCIENT_SPACE:
                        ToastUtils.showShortToast(  R.string.news_storage_title);
                        break;
                    default:
                        break;
                }
                break;
            }
        }
        listapter.onNotifyDataSetChanged(mAppMainList, list_app);

    }

    public void getAppList(int page) {
        this.page = page;

        appReponsitory.loadAppList(page, COUNT, new IAppDataSource.AppListDataCallback() {
            @Override
            public void onLoadAppList(List<AppInfo> loginResponses) {
                LoadingDialog.dissMiss();
                refreshListData(loginResponses);
            }

            @Override
            public void onDataNotAvailable(ThrowableWrapper e) {
                LoadingDialog.dissMiss();
            }
        });

    }

    private void searchApp(String name) {
        LoadingDialog.getInstance(mActivity).show();
        AppSearchRequest request = new AppSearchRequest();
        request.setAppName(name);
        request.setAppType("2");//针对Alpha2开发的应用
        appReponsitory.searchApp(name, 1, 100, new IAppDataSource.AppListDataCallback() {
            @Override
            public void onLoadAppList(List<AppInfo> appInfos) {
                LoadingDialog.dissMiss();
                isSearch = true;
                appSearchList = appInfos;
                refreshListData(appSearchList);
            }

            @Override
            public void onDataNotAvailable(ThrowableWrapper e) {

            }
        });
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        hasCreate = false;
        EventBus.getDefault().unregister(this);
        mActivity.unregisterReceiver(mUnLineBroadcastReceiver);

    }

    @Override
    public void onResume() {
        super.onResume();

        hasAddMore = false;
        mMainPageActivity.mainTopView.setVisibility(View.GONE);
        mMainPageActivity.bottomLay.setVisibility(View.GONE);
        mMainPageActivity.currentFragment = this;
        if (mAppMainList.size() > 0 && hasCreate)
            resetStatus();
    }

    private void resetStatus() {

        for (int i = 0; i < mAppMainList.size(); i++) {
            if (!RobotManagerService.getInstance().isConnectedRobot() ||  TextUtils.isEmpty(mAppMainList.get(i).getAppPath())) {
                mAppMainList.get(i).setStatus(-1);
            } else {
                if (mAppMainList.get(i).getStatus() == -1 && RobotManagerService.getInstance().isConnectedRobot()) {
                    if (mAppMainList.get(i).getAppPath() != null && !TextUtils.isEmpty(mAppMainList.get(i).getAppPath())) {
                        mAppMainList.get(i).setStatus(0);
                    }
                } else {

                    mAppMainList.get(i).setStatus(MainPageActivity.dao.queryAppStatus(
                            mAppMainList.get(i).getAppPackage(), mAppMainList.get(i).getVersionCode()));
                }
            }


        }
        listapter.onNotifyDataSetChanged(mAppMainList, list_app);
    }




    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * 广播接收者
     *
     * @author weijiang204321
     */
    class UnLineBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra(AidlService.AIDL_RECEIVE_ERROR_CODE).equals(context.getString(R.string.main_page_alpha2_offline))) {
                LoadingDialog.getInstance(mActivity).show();
                getAppList(1);
            }
        }

    }
}
