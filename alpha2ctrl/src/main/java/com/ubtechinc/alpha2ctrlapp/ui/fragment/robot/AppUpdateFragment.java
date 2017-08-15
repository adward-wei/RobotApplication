package com.ubtechinc.alpha2ctrlapp.ui.fragment.robot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.constants.IntentConstants;
import com.ubtechinc.alpha2ctrlapp.entity.AppUpdate;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.RobotApp;
import com.ubtechinc.alpha2ctrlapp.entity.business.shop.AppInfo;
import com.ubtechinc.alpha2ctrlapp.events.AppDownloadStatusChangeEvent;
import com.ubtechinc.alpha2ctrlapp.ui.adapter.shop.AppUpdateAdpter;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.base.BaseContactFragememt;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class AppUpdateFragment extends BaseContactFragememt implements View.OnClickListener {

    private String TAG = "AppUpdateFragment";
    private ListView lst_actions;
    private AppUpdateAdpter listapter;
    private List<AppInfo> list1 = new ArrayList<AppInfo>();
    private List<AppUpdate> mFileList = new ArrayList<AppUpdate>();
    private TextView btn_updateAll;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateFragmentView(LayoutInflater inflater,
                                     ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return inflater.inflate(R.layout.app_update, container, false);
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        listapter = new AppUpdateAdpter(mActivity, mFileList);
        lst_actions = (ListView) mContentView.findViewById(R.id.lst_app);
        lst_actions.setAdapter(listapter);

//		lst_actions.setAdapter(lst_actions_adapter);
        list1 = (List<AppInfo>) bundle.getSerializable(IntentConstants.DATA_APP_LIST);
        refreshListData(list1);
        btn_updateAll = (TextView) mContentView.findViewById(R.id.btn_top_right);
        btn_updateAll.setVisibility(View.VISIBLE);
        btn_updateAll.setOnClickListener(this);
        title.setText(getString(R.string.app_setting_app_update));
        lst_actions.setOnItemClickListener(listapter);
        mMainPageActivity.topLayout.setVisibility(View.VISIBLE);

    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AppDownloadStatusChangeEvent event) {
        Logger.t(TAG).d("onEvent AppDownloadStatusChangeEvent " + event.getAppEntrityInfo());
        changeDownLoadStatus(event.getAppEntrityInfo());
    }
    private void remove(RobotApp info) {
        for (int i = 0; i < mMainPageActivity.updateApps.size(); i++) {
            if (mMainPageActivity.updateApps.get(i).getAppPackage().equals(info.getPackageName())) {
                mMainPageActivity.updateApps.remove(i);
                mFileList.remove(i);
                listapter.onNotifyDataSetChanged(mFileList);
            }
        }
        if (mFileList.size() == 0) {
            ToastUtils.showShortToast(  R.string.app_updated_all_app);
            mMainPageActivity.onBack(null);
        }

    }

    private void refreshListData(List<AppInfo> list) {
        AppUpdate downloadInfo;
        mFileList.clear();
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                downloadInfo = new AppUpdate();
                downloadInfo.setStatus(7);
                downloadInfo.setStatusTv(getActivity().getString(R.string.main_page_update));
//					downloadInfo.seta(list.get(i).getActionId());
                downloadInfo.setAppHeadImage(list.get(i).getAppIcon());
                downloadInfo.setAppDesciber(list.get(i).getAppDesc());
                downloadInfo.setAppImagePath(list.get(i).getAppImagePath());
                downloadInfo.setAppKey(list.get(i).getAppKey());
                downloadInfo.setAppName(list.get(i).getAppName());
                downloadInfo.setAppPackage(list.get(i).getPackageName());
                downloadInfo.setAppLanguageDesciber(list.get(i).getAppLanguageDesciber());
                downloadInfo.setAppLanguageName(list.get(i).getAppLanguageName());
                downloadInfo.setAppPath(list.get(i).getAppPath());
                downloadInfo.setAppSize(list.get(i).getAppSize());
                downloadInfo.setAppVersion(list.get(i).getAppVersionName());
                downloadInfo.setAppId(list.get(i).getAppId());
                downloadInfo.setAppResume(list.get(i).getAppResume());
                mFileList.add(downloadInfo);

            }
        }
        listapter.onNotifyDataSetChanged(mFileList);
    }

    private void changeDownLoadStatus(RobotApp info) {
        String pakageName = info.getPackageName();
        for (int i = 0; i < mFileList.size(); i++) {
            if (mFileList.get(i).getAppPackage().equals(pakageName)) {
                mFileList.get(i).setStatus(info.getDownloadState());
                switch (info.getDownloadState()) {
                    case 0:
                        mFileList.get(i).setStatusTv(mActivity.getString(R.string.main_page_update));
                        break;
                    case 1:
                        mFileList.get(i).setStatusTv(mActivity.getString(R.string.shop_page_downloading));
                        break;
                    case 2:
                        mFileList.get(i).setStatusTv(mActivity.getString(R.string.shop_page_installeding));
                        break;
                    case 3:
                        mFileList.get(i).setStatusTv(mActivity.getString(R.string.shop_page_download_falied));
                        break;
                    case 4:
                        mFileList.get(i).setStatusTv(mActivity.getString(R.string.shop_page_installed_success));
                        List<AppInfo> list = mMainPageActivity.updateApps;
                        if (null != mMainPageActivity.updateApps && mMainPageActivity.updateApps.size() > 0) {
                            for (AppInfo appInfo : list) {
                                if (appInfo.getPackageName().equals(pakageName)) {
                                    mMainPageActivity.updateApps.remove(appInfo);
                                    break;
                                }
                            }
                        }
                        break;
                    case 5:
                        mFileList.get(i).setStatusTv(mActivity.getString(R.string.shop_page_installed_failed));
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
        listapter.notifyDataSetChanged();

    }

    @Override
    public void onResume() {
        super.onResume();
        mMainPageActivity.mainTopView.setVisibility(View.GONE);
//		mMainPageActivity.btn_swich_active.setVisibility(View.GONE);
        mMainPageActivity.currentFragment = this;
    }



    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_top_right:
                if (mFileList.size() > 0) {
                    updateAll();
                }

                break;

            default:
                break;
        }
    }

    private void updateAll() {
        for (int position = 0; position < mFileList.size(); position++) {
            if (mFileList.get(position).getStatus() == 0
                    || mFileList.get(position).getStatus() == 3
                    || mFileList.get(position).getStatus() == 5 || mFileList.get(position).getStatus() == 7) {

                RobotApp app = new RobotApp();
                app.setName(mFileList.get(position).getAppName());
                app.setPackageName(mFileList.get(position).getAppPackage());
                app.setUrl(mFileList.get(position).getAppPath());
                app.setAppKey(mFileList.get(position).getAppKey());
                mMainPageActivity.downLoadApp(app, mFileList.get(position).getAppHeadImage(), mFileList.get(position).getVersionCode());
                mFileList.get(position).setStatusTv(mActivity.getResources().getString(R.string.shop_page_downloading));
                mFileList.get(position).setStatus(1);
                listapter.onNotifyDataSetChanged(mFileList);
            }
        }

    }


}
