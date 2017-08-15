package com.ubtechinc.alpha2ctrlapp.ui.fragment.shop;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.StringUtils;
import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.BaseHandler;
import com.ubtechinc.alpha2ctrlapp.base.IDataCallback;
import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.constants.IntentConstants;
import com.ubtechinc.alpha2ctrlapp.constants.MessageType;
import com.ubtechinc.alpha2ctrlapp.data.robot.IRobotActionDataSource;
import com.ubtechinc.alpha2ctrlapp.data.robot.RobotActionRepository;
import com.ubtechinc.alpha2ctrlapp.data.shop.ActionLocalDataSource;
import com.ubtechinc.alpha2ctrlapp.data.shop.ActionRemoteDataSource;
import com.ubtechinc.alpha2ctrlapp.data.shop.ActionRepository;
import com.ubtechinc.alpha2ctrlapp.data.shop.AppRemoteDataSource;
import com.ubtechinc.alpha2ctrlapp.data.shop.AppReponsitory;
import com.ubtechinc.alpha2ctrlapp.data.shop.IActionDataSource;
import com.ubtechinc.alpha2ctrlapp.data.shop.IAppDataSource;
import com.ubtechinc.alpha2ctrlapp.entity.AppUpdate;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.ActionDownLoad;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.ActionFileEntrity;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.NewActionInfo;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.RobotApp;
import com.ubtechinc.alpha2ctrlapp.entity.business.shop.ActionInfo;
import com.ubtechinc.alpha2ctrlapp.entity.business.shop.AppInfo;
import com.ubtechinc.alpha2ctrlapp.events.ActionDownloadStatusChangeEvent;
import com.ubtechinc.alpha2ctrlapp.events.AppDownloadStatusChangeEvent;
import com.ubtechinc.alpha2ctrlapp.service.RobotManagerService;
import com.ubtechinc.alpha2ctrlapp.ui.activity.app.ActionsLibPreViewActivity;
import com.ubtechinc.alpha2ctrlapp.ui.activity.main.MainPageActivity;
import com.ubtechinc.alpha2ctrlapp.ui.adapter.shop.ActionListAdpter;
import com.ubtechinc.alpha2ctrlapp.ui.adapter.shop.AppListInfoAdpter;
import com.ubtechinc.alpha2ctrlapp.ui.adapter.shop.AppMoreAdpter;
import com.ubtechinc.alpha2ctrlapp.ui.adapter.shop.SearchActionAdpter;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.base.BaseContactFragememt;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.LoadingDialog;
import com.ubtechinc.nets.http.ThrowableWrapper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 通用搜索页
 * @ClassName SearchFragment
 * @date 2016/10/18
 * @Description
 * @modifier tanghongyu
 * @modify_time 2016/10/18
 */
public class SearchFragment extends BaseContactFragememt {

    private String TAG = "SearchFragment";
    ;

    private ListView listView;
    private List<NewActionInfo> list = new ArrayList<NewActionInfo>();
    private EditText input_text;
    private SearchActionAdpter adapter;
    private TextView searchResultTip;
    private boolean isFromLocal = false;
    private boolean isFromAcion = false;
    private int mActionType;
    private ActionListAdpter actionlistapter;
    private AppMoreAdpter appListapter;
    private List<AppUpdate> mAppMainList = new ArrayList<AppUpdate>();
    private List<ActionDownLoad> mActionList = new ArrayList<ActionDownLoad>();
    private TextView tvEmpty;
    //子程序列表
    private List<AppUpdate> mAppLinkedList = new ArrayList<AppUpdate>();
    ActionRepository actionRepository;
    AppReponsitory appReponsitory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateFragmentView(LayoutInflater inflater,
                                     ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.search_action_layout, container, false);
    }


    @Override
    public void initView() {

        listView = (ListView) mContentView.findViewById(R.id.actionlistView);
        View emptyView = mContentView.findViewById(android.R.id.empty);
        listView.setEmptyView(emptyView);
        listView.getEmptyView().setVisibility(View.GONE);
        tvEmpty = (TextView) emptyView.findViewById(R.id.tv_search_result);
        input_text = (EditText) mContentView.findViewById(R.id.input_text);
        input_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // TODO Auto-generated method stub

                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    //do something;
                    slidingKeyBroad();
                    if (isFromLocal) {
                        list = MainPageActivity.dao.queryActionListByName( input_text.getText().toString(), mActionType);
                        if (list.size() > 0) {
                            searchResultTip.setVisibility(View.VISIBLE);
                        } else {
                            searchResultTip.setVisibility(View.GONE);
                        }
                        adapter.onNotifyDataSetChanged(list);
                    } else if (isFromAcion) {
                        searchAction(input_text.getText().toString());
                    } else {
                        searchApp(input_text.getText().toString());
                    }

                    return true;
                }
                return false;

            }
        });

        isFromLocal = bundle.getBoolean(IntentConstants.DATA_IS_LOCAL_DATA);
        if (!isFromLocal) {
            isFromAcion = bundle.getBoolean(IntentConstants.DATA_IS_ACTION_SEARCH);
            if (isFromAcion) {
                mActionType = bundle.getInt(IntentConstants.DATA_SEARCH_TYPE);
                actionlistapter = new ActionListAdpter(mActivity, mActionList);
                listView.setAdapter(actionlistapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("actionid", mActionList.get(position).getActionId());
                        replaceFragment(
                                ActionsLibPreViewActivity.class.getName(), bundle);
                    }
                });
            } else {
                appListapter = new AppMoreAdpter(mActivity, mAppMainList, null, new AppListInfoAdpter.DownLoadListener() {
                    @Override
                    public void startDownLoad(AppInfo appInfo) {

                    }
                });
                listView.setAdapter(appListapter);
                listView.setOnItemClickListener(appListapter);
            }
        } else {
            adapter = new SearchActionAdpter(mActivity, this, list, new IDataCallback<String>() {
                @Override
                public void onCallback(String data) {
                    RobotActionRepository.getInstance().deleteActionById(data, new IRobotActionDataSource.ControlActionCallback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onFail(ThrowableWrapper e) {

                        }
                    });

                }
            });
            mActionType = bundle.getInt(IntentConstants.DATA_SEARCH_TYPE);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(adapter);
            listView.setOnItemLongClickListener(adapter);
        }
        searchResultTip = (TextView) mContentView.findViewById(R.id.search_result_tips);
        actionRepository = ActionRepository.getInstance(ActionLocalDataSource.getInstance(mApplication), ActionRemoteDataSource.getInstance(mApplication));
        appReponsitory = AppReponsitory.getInstance(AppRemoteDataSource.getInstance(mApplication));

    }

    private class MHandler extends BaseHandler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // TODO Auto-generated method stub
            switch (msg.what) {
                case MessageType.ALPHA_LOST_CONNECTED:
                    mMainPageActivity.isCurrentAlpha2MacLostConnection((String) msg.obj);
                    break;



            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AppDownloadStatusChangeEvent event) {
        changeAppDownLoadStatus(event.getAppEntrityInfo());
    }

    private void refreshAppListData(List<AppInfo> list) {
        mAppMainList.clear();
        mAppLinkedList.clear();
        AppInfo info;
        AppUpdate downloadInfo;

        if (list.size() > 0) {

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

        }
        if (mAppMainList.size() == 0) {
            tvEmpty.setText(mActivity.getString(R.string.apps_action_list_blank_result));
        }

        appListapter.onNotifyDataSetChanged(mAppMainList, null);
    }

    private void changeAppDownLoadStatus( RobotApp info) {
        String pakageName = info.getPackageName();
        for (int i = 0; i < mAppMainList.size(); i++) {
            if (mAppMainList.get(i).getPackageName().equals(pakageName)) {
                mAppMainList.get(i).setStatus(info.getDownloadState());
            }
        }
        appListapter.onNotifyDataSetChanged(mAppMainList, null);

    }

    private void refreshActionListData(List<ActionInfo> list) {
        ActionDownLoad downloadInfo;

        mActionList.clear();
        if (list.size() > 0) {

            for (int i = 0; i < list.size(); i++) {
                downloadInfo = new ActionDownLoad();
                downloadInfo.setActionName(list.get(i).getActionName());
                if (!RobotManagerService.getInstance().isConnectedRobot()) {
                    downloadInfo.setStatus(-1);
                } else if (list.get(i).getActionPath() == null || TextUtils.isEmpty(list.get(i).getActionPath())) {
                    downloadInfo.setStatus(-1);
                } else {
                    if (TextUtils.isEmpty(list.get(i).getActionOriginalId()))
                        downloadInfo.setStatus(MainPageActivity.dao.queryActionStatus( list.get(i).getActionName(), list.get(i).getActionPath()));
                    else {
                        downloadInfo.setStatus(MainPageActivity.dao.queryActionStatus( list.get(i).getActionOriginalId(), list.get(i).getActionPath()));
                    }
                }
                downloadInfo.setActionId(list.get(i).getActionId());
                downloadInfo.setActionImagePath(list.get(i).getActionImagePath());
                downloadInfo.setActionOriginalId(list.get(i).getActionOriginalId());
                downloadInfo.setActionLangDesciber(list.get(i).getActionLangDesciber());
                downloadInfo.setActionLangName(list.get(i).getActionLangName());
                downloadInfo.setActionTime(list.get(i).getActionTime());
                downloadInfo.setActionDesciber(list.get(i).getActionDesciber());
                downloadInfo.setActionDate(list.get(i).getActionDate());
                downloadInfo.setActionPath(list.get(i).getActionPath());
                downloadInfo.setActionTitle(list.get(i).getActionTitle());
                downloadInfo.setActionType(list.get(i).getActionType());
                mActionList.add(downloadInfo);

            }
        }
        actionlistapter.notifyDataSetChanged();

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ActionDownloadStatusChangeEvent event) {

        ActionFileEntrity entrity = event.getActionFileEntrity();
        Logger.t(TAG).d("ActionDownloadStatusChangeEvent = " + entrity);
        changeActionDownLoadStatus(entrity);
    }

    private void changeActionDownLoadStatus( ActionFileEntrity info) {
        String originalId = info.getActionOriginalId();

        for (int i = 0; i < mActionList.size(); i++) {
            if (mActionList.get(i).getActionOriginalId().equals(originalId)) {
                mActionList.get(i).setStatus(info.getDownloadState());

            }
        }

        actionlistapter.notifyDataSetChanged();

    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }



    @Override
    public void onResume() {
        super.onResume();
        mMainPageActivity.currentFragment = this;
        mMainPageActivity.mainTopView.setVisibility(View.GONE);

    }

    private void searchApp(String name) {
        LoadingDialog.getInstance(mActivity).show();

        appReponsitory.searchApp(name, 1, 100, new IAppDataSource.AppListDataCallback() {
            @Override
            public void onLoadAppList(List<AppInfo> loginResponses) {
                LoadingDialog.dissMiss();
                refreshAppListData(loginResponses);
            }

            @Override
            public void onDataNotAvailable(ThrowableWrapper e) {
                LoadingDialog.dissMiss();
            }
        });

    }

    private void searchAction(String name) {
        LoadingDialog.getInstance(mActivity).show();
        actionRepository.searchAction(name, mActionType, 1, 100, new IActionDataSource.LoadActionCallback() {
            @Override
            public void onActionLoaded(List<ActionInfo> tasks) {
                refreshActionListData(tasks);
                LoadingDialog.dissMiss();
            }


            @Override
            public void onDataNotAvailable(ThrowableWrapper e) {

                ToastUtils.showShortToast(e.getMessage());
                LoadingDialog.dissMiss();
            }
        });
    }
}
