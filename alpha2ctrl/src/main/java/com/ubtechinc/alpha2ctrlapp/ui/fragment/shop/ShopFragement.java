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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.ListUtils;
import com.ubtech.utilcode.utils.StringUtils;
import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.base.BaseHandler;
import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.constants.IntentConstants;
import com.ubtechinc.alpha2ctrlapp.constants.MessageType;
import com.ubtechinc.alpha2ctrlapp.data.Injection;
import com.ubtechinc.alpha2ctrlapp.data.shop.ADPromotionReponsitory;
import com.ubtechinc.alpha2ctrlapp.data.shop.ActionRepository;
import com.ubtechinc.alpha2ctrlapp.data.shop.AppReponsitory;
import com.ubtechinc.alpha2ctrlapp.data.shop.IADDataSource;
import com.ubtechinc.alpha2ctrlapp.data.shop.IActionDataSource;
import com.ubtechinc.alpha2ctrlapp.data.shop.IAppDataSource;
import com.ubtechinc.alpha2ctrlapp.entity.AppUpdate;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.ActionDownLoad;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.ActionFileEntrity;
import com.ubtechinc.alpha2ctrlapp.entity.business.shop.ActionInfo;
import com.ubtechinc.alpha2ctrlapp.entity.business.shop.AppInfo;
import com.ubtechinc.alpha2ctrlapp.entity.business.shop.RecommenedPageInfo;
import com.ubtechinc.alpha2ctrlapp.events.ActionDownloadStatusChangeEvent;
import com.ubtechinc.alpha2ctrlapp.events.AppDownloadStatusChangeEvent;
import com.ubtechinc.alpha2ctrlapp.service.RobotManagerService;
import com.ubtechinc.alpha2ctrlapp.third.AidlService;
import com.ubtechinc.alpha2ctrlapp.ui.activity.main.MainPageActivity;
import com.ubtechinc.alpha2ctrlapp.ui.adapter.shop.ActionLastAdpter;
import com.ubtechinc.alpha2ctrlapp.ui.adapter.shop.AppListInfoAdpter;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.app.HelpActivity;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.base.BaseContactFragememt;
import com.ubtechinc.alpha2ctrlapp.widget.RefreshListView;
import com.ubtechinc.alpha2ctrlapp.widget.RefreshListView.OnRefreshListener;
import com.ubtechinc.alpha2ctrlapp.widget.ScollViewPager;
import com.ubtechinc.alpha2ctrlapp.widget.SwitchChangeActiveButton;
import com.ubtechinc.alpha2ctrlapp.widget.SwitchChangeActiveButton.OnSwitchChangedActiveListener;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.LoadingDialog;
import com.ubtechinc.nets.http.ThrowableWrapper;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static com.ubtechinc.alpha2ctrlapp.ui.activity.main.MainPageActivity.appInfoList;

//import com.sina.weibo.sdk.api.CmdObject;
/**
 * @ClassName ShopFragement
 * @date 7/6/2017
 * @author tanghongyu
 * @Description 商店页
 * @modifier
 * @modify_time
 */
public class ShopFragement extends BaseContactFragememt implements OnSwitchChangedActiveListener, OnClickListener, AppListInfoAdpter.DownLoadListener, ActionLastAdpter.DownLoadListener, ScollViewPager.OnViewPageSelected {
    private String TAG = "ShopFragment";
    //主程序列表，可能包含
    private List<AppUpdate> mAppMainList = new ArrayList<AppUpdate>();

    //子程序列表
    private List<AppUpdate> mAppLinkedList = new ArrayList<AppUpdate>();

    private AppListInfoAdpter mAppApdater;
    private RefreshListView appGridView;
    private List<RecommenedPageInfo> pageList = new ArrayList<RecommenedPageInfo>();
    private String[] drwableList;
    private ImageView[] img_logo_index;
    private LinearLayout lay_new_more_app;
    private TextView btn_get_more_app;
    private ScollViewPager viewPage;
    public static int mSlidePoint = 1;
    public static int load_cur_screen = 0;
    private View shopHeader;
    private boolean hasCreate = false;
    private boolean isRefresh = false;
    public SwitchChangeActiveButton btn_swich_active;
    private RelativeLayout search_lay;
    public static int shopType = BusinessConstants.SHOP_TYPE_APP; // 0 表示应用 1表示动作
    private View appHeader;
    private View actionHeader;
    private LinearLayout moreLay;
    private TextView getMoreLay;
    private ImageView img_type_base_action;
    private ImageView img_type_dance_action;
    private ImageView img_type_story_action;
    private boolean hasGetActionList = true;
    private List<ActionDownLoad> mActionList = new ArrayList<ActionDownLoad>();
    private ActionLastAdpter mActionAdapter;
    private AppInfo downloadingAppinfo;
    private ActionDownLoad mAcitonDownLoadInfo;
    private UnLineBroadcastReceiver mUnLineBroadcastReceiver;

    @Override
    public View onCreateFragmentView(LayoutInflater inflater,
                                     ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return inflater.inflate(R.layout.app_fragement, container, false);
    }

    @Override
    public void initView() {

        appReponsitory = Injection.provideAppRepository(mApplication);
        advertisingPromotionReponsitory = Injection.provideADPromotionRepository(mApplication);
        actionRepository = Injection.provideActionRepository(mApplication);

        mHandler = new ShopHandler();
        shopHeader = (LinearLayout) LayoutInflater.from(mActivity).inflate(R.layout.shop_header, null, false);
        appHeader = (LinearLayout) LayoutInflater.from(mActivity).inflate(R.layout.shop_app_header, null, false);
        actionHeader = (LinearLayout) LayoutInflater.from(mActivity).inflate(R.layout.shop_action_heaer, null, false);
        search_lay = (RelativeLayout) shopHeader.findViewById(R.id.search_lay);
        appGridView = (RefreshListView) mContentView.findViewById(R.id.app_list);
        appGridView.addHeaderView(shopHeader);
        appGridView.setSearch_lay(search_lay);
        img_logo_index = new ImageView[]{
                (ImageView) shopHeader.findViewById(R.id.img_1),
                (ImageView) shopHeader.findViewById(R.id.img_2),
                (ImageView) shopHeader.findViewById(R.id.img_3),
                (ImageView) shopHeader.findViewById(R.id.img_4),
                (ImageView) shopHeader.findViewById(R.id.img_5)};
        viewPage = (ScollViewPager) shopHeader.findViewById(R.id.app_view_pager);
        viewPage.setImg_logo_index(img_logo_index);
        viewPage.setOnViewPageSelected(this);
        viewPage.setHandler(mHandler);
        if (shopType == BusinessConstants.SHOP_TYPE_APP) {
            initAppView();
        } else {
            initActionView();
        }

        if (mMainPageActivity.appdrwableList != null
                && mMainPageActivity.appdrwableList.length > 0) {
            drwableList = mMainPageActivity.appdrwableList;
            intscreen();
        }
        search_lay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                bundle = new Bundle();
                bundle.putBoolean(IntentConstants.DATA_IS_LOCAL_DATA, false);
                if (shopType == BusinessConstants.SHOP_TYPE_ACTION) {
                    bundle.putBoolean(IntentConstants.DATA_IS_ACTION_SEARCH, true);
                } else
                    bundle.putBoolean(IntentConstants.DATA_IS_ACTION_SEARCH, false);
                replaceFragment(SearchFragment.class.getName(), bundle);
            }
        });
        /**列表的下拉刷新**/
        appGridView.setonRefreshListener(new OnRefreshListener() {
            public void onRefresh() {
                isRefresh = true;
                if (shopType == BusinessConstants.SHOP_TYPE_APP) {
                    getAppList();
                } else {
                    getLastActionList();
                }
            }
        });
        btn_swich_active = (SwitchChangeActiveButton) shopHeader.findViewById(R.id.btn_swich_active);
        btn_swich_active.changeActive(shopType);
        btn_swich_active.setChangedLanListener(this);
    }

    private void initAppView() {
        appGridView.removeHeaderView(actionHeader);
        appGridView.addHeaderView(appHeader);
        btn_get_more_app = (TextView) appHeader.findViewById(R.id.btn_get_more_app);
        lay_new_more_app = (LinearLayout) appHeader.findViewById(R.id.lay_new_more_app);
        btn_get_more_app.setOnClickListener(this);
        mAppApdater = new AppListInfoAdpter(mActivity, mAppMainList, this);
        appGridView.setAdapter(mAppApdater);
        appGridView.setOnItemClickListener(mAppApdater);
        if (!ListUtils.isEmpty(appInfoList)) {//先取之前缓存的应用列表
            mAppMainList = appInfoList;
            mAppApdater.onNotifyDataSetChanged(mAppMainList);
        } else {
            LoadingDialog.getInstance(mActivity).show();
            getAppList();
        }

    }

    private void   initActionView() {
        appGridView.removeHeaderView(appHeader);
        appGridView.addHeaderView(actionHeader);
        moreLay = (LinearLayout) actionHeader.findViewById(R.id.lay_new_more_acion);
        getMoreLay = (TextView) actionHeader.findViewById(R.id.get_all_action_lay);
        getMoreLay.setOnClickListener(this);
        img_type_base_action = (ImageView) actionHeader.findViewById(R.id.img_type_base_action);
        img_type_dance_action = (ImageView) actionHeader.findViewById(R.id.img_type_dance_action);
        img_type_story_action = (ImageView) actionHeader.findViewById(R.id.img_type_story_action);
        img_type_base_action.setOnClickListener(this);
        img_type_dance_action.setOnClickListener(this);
        img_type_story_action.setOnClickListener(this);
        mActionAdapter = new ActionLastAdpter(mActivity, mActionList, false);
        appGridView.setAdapter(mActionAdapter);
        appGridView.setOnItemClickListener(mActionAdapter);
        mActionAdapter.setDownLoadListener(this);
        if (ListUtils.isEmpty(mMainPageActivity.actionList)) {
            getLastActionList();

        } else {
            mActionList = mMainPageActivity.actionList;
            mActionAdapter.onNotifyDataSetChanged(mActionList);
        }
        List<String> listA = MainPageActivity.dao.queryActionList();
        if (ListUtils.isEmpty(listA) && RobotManagerService.getInstance().isConnectedRobot()) {
            mMainPageActivity.onGetActionList();
        } else {
            MainPageActivity.hasGetLocolList = true;
        }
    }
    AppReponsitory appReponsitory;
    ADPromotionReponsitory advertisingPromotionReponsitory;
    ActionRepository actionRepository;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter intentFilter = new IntentFilter(AidlService.RECEIVE_ERROR_DATA);
        mUnLineBroadcastReceiver = new UnLineBroadcastReceiver();
        mActivity.registerReceiver(mUnLineBroadcastReceiver, intentFilter);
        EventBus.getDefault().register(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_get_more_app:
                replaceFragment(AppMoreListFragement.class.getName(), new Bundle());
                break;
            case R.id.get_all_action_lay: {
                Bundle bundle = new Bundle();
                bundle.putInt(IntentConstants.ROBOT_ACTION_TYPE, BusinessConstants.ROBOT_ACTION_TYPE_TOTAL);
                replaceFragment(ActionsLibActivity.class.getName(), bundle);
            }
            break;
            case R.id.img_type_base_action: {
                Bundle bundle = new Bundle();
                bundle.putInt(IntentConstants.ROBOT_ACTION_TYPE, BusinessConstants.ROBOT_ACTION_TYPE_BASE);
                replaceFragment(ActionsLibActivity.class.getName(), bundle);
            }
            break;
            case R.id.img_type_dance_action: {
                Bundle bundle = new Bundle();
                bundle.putInt(IntentConstants.ROBOT_ACTION_TYPE, BusinessConstants.ROBOT_ACTION_TYPE_MUSIC_AND_DANCE);
                replaceFragment(ActionsLibActivity.class.getName(), bundle);
            }
            break;
            case R.id.img_type_story_action: {
                Bundle bundle = new Bundle();
                bundle.putInt(IntentConstants.ROBOT_ACTION_TYPE, BusinessConstants.ROBOT_ACTION_TYPE_STORY_AND_FABLE);
                replaceFragment(ActionsLibActivity.class.getName(), bundle);
            }
            break;
        }
    }



    @Override
    public void startDownLoad(AppInfo appInfo) {
        this.downloadingAppinfo = appInfo;
    }

    @Override
    public void startDownLoad(ActionDownLoad actionDownLoad) {
        this.mAcitonDownLoadInfo = actionDownLoad;
    }

    @Override
    public void onPageClick(int position) {
        Logger.d( pageList.get(position).toString());
        if (null != pageList && pageList.size() > 0) {
            RecommenedPageInfo recommenedPageInfo = pageList.get(position);
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
                        Intent intent = new Intent(mActivity, HelpActivity.class);
                        intent.putExtra("title", getString(R.string.event_title_name));
                        intent.putExtra("url", url);
                        mActivity.startActivity(intent);
                    }
                }
            }

        }
    }

    @Override
    public void onPageSelected(int position) {


    }

    private class ShopHandler extends BaseHandler {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub

            switch (msg.what) {
                case -1:
                    viewPage.setTouch(false);
                    break;
                case MessageType.ALPHA_LOST_CONNECTED:
                    Logger.i( "handleMessage ALPHA_LOST_CONNECTED.");
                    mMainPageActivity.isCurrentAlpha2MacLostConnection((String) msg.obj);
                    break;





            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AppDownloadStatusChangeEvent event) {

        Logger.t(TAG).d( "onEvent AppDownloadStatusChangeEvent state = %s" +event.getAppEntrityInfo().toString() );
        if ( event.getAppEntrityInfo().getDownloadState() == BusinessConstants.APP_STATE_INSTALL_SUCCESS) {
            if (null != downloadingAppinfo) {
                Logger.t(TAG).d( "appdown shopfragment下载成功");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("appName", downloadingAppinfo.getAppName());
                MobclickAgent.onEvent(mActivity, Constants.YOUMENT_SHOP_APP_DOWNLOAD_TIMES, map);
                downloadingAppinfo = null;
            }
            LoadingDialog.getInstance(mActivity).show();
            getAppList();
        } else if ( event.getAppEntrityInfo().getDownloadState() == BusinessConstants.APP_STATE_INSTALL_FAIL) {

            if (downloadingAppinfo != null) {
                Logger.t(TAG).d( "安装失败 app = %s", downloadingAppinfo.getPackageName());
                LoadingDialog.getInstance(mActivity).show();
                getAppList();
                downloadingAppinfo = null;
            }
            ToastUtils.showLongToast( mActivity.getString(R.string.shop_page_installed_failed));
        }

    }
    private void getImageUrl() {
        drwableList = new String[pageList.size()];
        for (int i = 0; i < pageList.size(); i++) {
            drwableList[i] = pageList.get(i).getRecommendImage();
            if (i == pageList.size() - 1) {
                mMainPageActivity.appdrwableList = drwableList;
                viewPage.setDrwableList(drwableList);
                viewPage.initView();
                intscreen();
                break;
            }
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        hasCreate = false;
        mActivity.unregisterReceiver(mUnLineBroadcastReceiver);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        mMainPageActivity.mainTopView.setVisibility(View.VISIBLE);
        mMainPageActivity.bottomLay.setVisibility(View.GONE);
        mMainPageActivity.currentFragment = this;
        if (shopType == BusinessConstants.SHOP_TYPE_APP) {
            if (hasCreate && ListUtils.getSize(mAppMainList) > 0) {
                resetAppDownloadState();
            }
        } else {
            if (hasCreate && ListUtils.getSize(mActionList) > 0) {
                resetActionDownloadState();
            }
        }


    }

    @Override
    public void onChangeActive(Integer index) {
        if (shopType == index) {
            return;
        } else {
            shopType = index;
        }
        switch (index) {
            case BusinessConstants.SHOP_TYPE_APP:
                initAppView();
                break;
            case BusinessConstants.SHOP_TYPE_ACTION:
                initActionView();
                break;
            default:
                break;
        }
    }

    /**
     * @param
     * @return
     * @throws
     * @Description 重置下载状态
     */
    private void resetAppDownloadState() {
        for (AppUpdate app : mAppMainList) {
            if (!RobotManagerService.getInstance().isConnectedRobot() || StringUtils.isEmpty(app.getAppPath())) {
                app.setStatus(BusinessConstants.APP_STATE_ERROR);
            } else {
                if (app.getStatus() == BusinessConstants.APP_STATE_ERROR && RobotManagerService.getInstance().isConnectedRobot()) {
                    if (!StringUtils.isEmpty(app.getAppPath())) {
                        app.setStatus(BusinessConstants.APP_STATE_INIT);
                    }
                } else {
                    app.setStatus(MainPageActivity.dao.queryAppStatus(          app.getPackageName(), Integer.parseInt(app.getAppVersion())));

                }
            }


        }
        mAppApdater.onNotifyDataSetChanged(mAppMainList);
    }

    private void resetActionDownloadState() {
        for (int i = 0; i < mActionList.size(); i++) {
            if (TextUtils.isEmpty(mActionList.get(i).getActionOriginalId())) {
                mActionList.get(i).setStatus(MainPageActivity.dao.queryActionStatus( mActionList.get(i).getActionName(), mActionList.get(i).getActionPath()));
            } else {
                mActionList.get(i).setStatus(MainPageActivity.dao.queryActionStatus( mActionList.get(i).getActionOriginalId(), mActionList.get(i).getActionPath()));
            }
        }

        mActionAdapter.onNotifyDataSetChanged(mActionList);
    }



    private void getAppList() {

        appReponsitory.loadAppList(1, 20, new IAppDataSource.AppListDataCallback() {
            @Override
            public void onLoadAppList(List<AppInfo> loginResponses) {
                LoadingDialog.dissMiss();
                refreshListData(loginResponses);
                getFrontPagePic();
            }

            @Override
            public void onDataNotAvailable(ThrowableWrapper e) {

            }
        });


    }

    private void getLastActionList() {
        hasGetActionList = false;
        LoadingDialog.getInstance(mActivity).show();

        actionRepository.getLastActionList(1, 15, new IActionDataSource.LoadActionCallback() {
            @Override
            public void onActionLoaded(List<ActionInfo> tasks) {
                refreshActionListData(tasks);
                getFrontPagePic();
            }

            @Override
            public void onDataNotAvailable(ThrowableWrapper e) {
                ToastUtils.showShortToast(e.getMessage());
            }
        });



    }

    private void getFrontPagePic() {

        advertisingPromotionReponsitory.getAdvertisingPromotion("CN", new IADDataSource.ADDataCallback() {

            @Override
            public void onLoadADData(List<RecommenedPageInfo> loginResponses) {
                LoadingDialog.dissMiss();
                pageList.clear();
                pageList.addAll(loginResponses);
                getImageUrl();
            }

            @Override
            public void onDataNotAvailable(ThrowableWrapper e) {
                ToastUtils.showShortToast(e.getMessage());
            }
        });

    }

    private void refreshListData(List<AppInfo> list) {
        AppInfo info;
        AppUpdate downloadInfo;
        mAppMainList.clear();
        mAppLinkedList.clear();
        if (!ListUtils.isEmpty(list)) {
            lay_new_more_app.setVisibility(View.VISIBLE);
            for (int i = 0; i < list.size(); i++) {
                info = list.get(i);
                downloadInfo = new AppUpdate();
                downloadInfo.setVersionCode(info.getVersionCode());
                downloadInfo.setStatus(BusinessConstants.APP_STATE_INIT);
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
                if (!RobotManagerService.getInstance().isConnectedRobot()|| StringUtils.isEmpty(info.getAppPath())) {
                    downloadInfo.setStatus(BusinessConstants.APP_STATE_ERROR);
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
                                || downloadInfo.getAppName().equals(Constants.app_chat_clock)
                                ) {
                            continue;
                        }
                    } else if (MainPageActivity.alphaParam.getServiceLanguage().equalsIgnoreCase(BusinessConstants.ROBOT_SERVICE_LANGUAGE_CHINESE)) {
                        if (downloadInfo.getPackageName().equals(BusinessConstants.PACKAGENAME_EN_CHAT)) {
                            continue;
                        }
                    }
                }

                if (downloadInfo.getAppName().equals(Constants.app_chat_name)) {
                    Alpha2Application.getInstance().setChatIconPath(downloadInfo.getAppIcon());
                }
                if (StringUtils.isEquals(downloadInfo.getAppLinkId(), BusinessConstants.APP_LINK_MAIN)) {//主程序
                    mAppMainList.add(downloadInfo);
                } else {//子程序
                    if (StringUtils.isEquals(downloadInfo.getSdkType(), BusinessConstants.SHOP_SDK_TYPE_ANDROID))//只取Android手机客户端
                        mAppLinkedList.add(downloadInfo);

                }

            }

        } else {
            lay_new_more_app.setVisibility(View.GONE);

        }
        manageLinkedAppList();
        //缓存App列表
        appInfoList = mAppMainList;
        mAppApdater.onNotifyDataSetChanged(mAppMainList);
        hasCreate = true;
        if (isRefresh) {
            appGridView.onRefreshComplete();
            isRefresh = false;
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

    private void refreshActionListData(List<ActionInfo> list) {
        ActionDownLoad downloadInfo;
        mActionList.clear();
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                downloadInfo = new ActionDownLoad();
                downloadInfo.setActionName(list.get(i).getActionName());
                if (!RobotManagerService.getInstance().isConnectedRobot() || TextUtils.isEmpty(list.get(i).getActionPath())) {
                    downloadInfo.setStatus(-1);
                } else {

                    if (TextUtils.isEmpty(list.get(i).getActionOriginalId()))
                        downloadInfo.setStatus(MainPageActivity.dao.queryActionStatus(list.get(i).getActionName(), list.get(i).getActionPath()));
                    else {
                        downloadInfo.setStatus(MainPageActivity.dao.queryActionStatus( list.get(i).getActionOriginalId(), list.get(i).getActionPath()));
                    }
                }
                downloadInfo.setActionId(list.get(i).getActionId());
                downloadInfo.setActionImagePath(list.get(i).getActionImagePath());
                downloadInfo.setActionOriginalId(list.get(i).getActionOriginalId());
                downloadInfo.setActionLangDesciber(list.get(i).getActionLangDesciber());
                downloadInfo.setActionLangName(list.get(i).getActionLangName());
                downloadInfo.setActionDesciber(list.get(i).getActionDesciber());
                downloadInfo.setActionDate(list.get(i).getActionDate());
                downloadInfo.setActionPath(list.get(i).getActionPath());
                downloadInfo.setActionTitle(list.get(i).getActionTitle());
                downloadInfo.setActionType(list.get(i).getActionType());
                downloadInfo.setActionTime(list.get(i).getActionTime());
                mActionList.add(downloadInfo);

            }
            moreLay.setVisibility(View.VISIBLE);
        } else {
            moreLay.setVisibility(View.GONE);
        }
        hasCreate = true;
        if (isRefresh) {
            appGridView.onRefreshComplete();
            isRefresh = false;
        }
        mMainPageActivity.actionList = mActionList;
        mActionAdapter.onNotifyDataSetChanged(mActionList);
    }





    private void intscreen() {
        viewPage.setDrwableList(drwableList);
        viewPage.initView();
        mHandler.sendEmptyMessageDelayed(-1, 2000);
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
                getAppList();
            }
        }

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ActionDownloadStatusChangeEvent event) {
        Logger.t(TAG).d( "onEvent ActionDownloadStatusChangeEvent state = %s" +event.getActionFileEntrity().toString() );
       ActionFileEntrity entrity = event.getActionFileEntrity();
        if (entrity.getDownloadState() == BusinessConstants.APP_STATE_INSTALL_SUCCESS) {
            if (null != mAcitonDownLoadInfo) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("actionName", mAcitonDownLoadInfo.getActionName());
                MobclickAgent.onEvent(mActivity, Constants.YOUMENT_SHOP_ACTION_DOWNLOAD_TIMES, map);
                mAcitonDownLoadInfo = null;
            }
            getLastActionList();
        }
    }

}
