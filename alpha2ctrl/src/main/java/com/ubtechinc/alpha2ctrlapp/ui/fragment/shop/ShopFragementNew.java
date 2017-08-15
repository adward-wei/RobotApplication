//package com.ubtechinc.alpha2ctrlapp.ui.fragment.shop;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.Bundle;
//import android.os.Message;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.ubtech.utilcode.utils.ListUtils;
//import com.ubtechinc.alpha2ctrlapp.R;
//import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
//import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
//import com.ubtechinc.alpha2ctrlapp.constants.Constants;
//import com.ubtechinc.alpha2ctrlapp.constants.IntentConstants;
//import com.ubtechinc.alpha2ctrlapp.constants.MessageType;
//import com.ubtechinc.alpha2ctrlapp.constants.NetWorkConstant;
//import com.ubtechinc.alpha2ctrlapp.entity.business.shop.ActionInfo;
//import com.ubtechinc.alpha2ctrlapp.entity.AppInfo;
//import com.ubtechinc.alpha2ctrlapp.entity.AppUpdate;
//import com.ubtechinc.alpha2ctrlapp.entity.RecommenedPageInfo;
//import com.ubtechinc.alpha2ctrlapp.entity.local.ActionDownLoad;
//import com.ubtechinc.alpha2ctrlapp.third.AidlService;
//import com.ubtechinc.alpha2ctrlapp.ui.activity.main.MainPageActivity;
//import com.ubtechinc.alpha2ctrlapp.ui.fragment.base.BaseContactFragememt;
//import com.ubtechinc.alpha2ctrlapp.ui.adapter.shop.ActionLastAdpter;
//import com.ubtechinc.alpha2ctrlapp.ui.adapter.shop.AppListInfoAdpter;
//import com.ubtechinc.alpha2ctrlapp.util.ActivityUtil;
//import com.ubtechinc.alpha2ctrlapp.util.NLog;
//import com.ubtechinc.alpha2ctrlapp.util.NToast;
//import com.ubtechinc.alpha2ctrlapp.util.StringUtils;
//import com.ubtechinc.alpha2ctrlapp.widget.RefreshListView;
//import com.ubtechinc.alpha2ctrlapp.widget.ScollViewPager;
//import com.ubtechinc.alpha2ctrlapp.widget.SwitchChangeActiveButton;
//import com.ubtechinc.alpha2ctrlapp.widget.dialog.LoadingDialog;
//import com.ubtechinc.service.model.ActionFileEntrity;
//import com.ubtechinc.service.model.RobotApp;
//import com.umeng.analytics.MobclickAgent;
//
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//
//
///**
// * 商店页
// */
//public class ShopFragement extends BaseContactFragememt implements ShopContract.View, OnClickListener{
//    private String TAG = "ShopFragment";
//    private AppListInfoAdpter mAppApdater;
//    private RefreshListView appGridView;
//    private ImageView[] imgLogos;
//    private LinearLayout layMoreApp;
//    private TextView btn_get_more_app;
//    private ScollViewPager viewPage;
//    private View shopHeader;
//
//    public SwitchChangeActiveButton btn_swich_active;
//    private RelativeLayout search_lay;
//
//    private View appHeader;
//    private View actionHeader;
//    private LinearLayout moreLay;
//    private TextView getMoreLay;
//    private ImageView img_type_base_action;
//    private ImageView img_type_dance_action;
//    private ImageView img_type_story_action;
//    private ActionLastAdpter mActionListAdapter;
//    private UnLineBroadcastReceiver mUnLineBroadcastReceiver;
//    ShopContract.Presenter mPresenter;
//
//    @Override
//    public View onCreateFragmentView(LayoutInflater inflater,
//                                     ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.app_fragement, container, false);
//    }
//
//    @Override
//    public void initView() {
//        shopHeader = LayoutInflater.from(mContext).inflate(R.layout.shop_header, null, false);
//        appHeader =  LayoutInflater.from(mContext).inflate(R.layout.shop_app_header, null, false);
//        actionHeader =  LayoutInflater.from(mContext).inflate(R.layout.shop_action_heaer, null, false);
//        search_lay = (RelativeLayout) shopHeader.findViewById(R.id.search_lay);
//        appGridView = (RefreshListView) mContentView.findViewById(R.id.app_list);
//        appGridView.addHeaderView(shopHeader);
//        appGridView.setSearch_lay(search_lay);
//        imgLogos = new ImageView[]{
//                (ImageView) shopHeader.findViewById(R.id.img_1),
//                (ImageView) shopHeader.findViewById(R.id.img_2),
//                (ImageView) shopHeader.findViewById(R.id.img_3),
//                (ImageView) shopHeader.findViewById(R.id.img_4),
//                (ImageView) shopHeader.findViewById(R.id.img_5)};
//        viewPage = (ScollViewPager) shopHeader.findViewById(R.id.app_view_pager);
//        viewPage.setImg_logo_index(imgLogos);
//
//        viewPage.setOnViewPageSelected(new ScollViewPager.OnViewPageSelected() {
//
//            @Override
//            public void onPageClick(int position) {
//               mPresenter.pageClick(position);
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//
//            }
//        });
//
//        initAppView();
//        mPresenter.getFrontPagePic();
//
//        search_lay.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//             mPresenter.searchLayClick();
//            }
//        });
//        /**列表的下拉刷新**/
//        appGridView.setonRefreshListener(new RefreshListView.OnRefreshListener() {
//            public void onRefresh() {
//            mPresenter.refreshGridView();
//            }
//        });
//        btn_swich_active = (SwitchChangeActiveButton) shopHeader.findViewById(R.id.btn_swich_active);
//        btn_swich_active.changeActive(mPresenter.getShopType());
//        btn_swich_active.setChangedLanListener(new SwitchChangeActiveButton.OnSwitchChangedActiveListener() {
//            @Override
//            public void onChangeActive(Integer index) {
//              mPresenter.onChangeActive(index);
//            }
//        });
//    }
//    /**
//     * @Description 初始化App商店页面
//     * @param
//     * @return
//     * @throws
//     */
//    @Override
//    public void initAppView() {
//        appGridView.removeHeaderView(actionHeader);
//        appGridView.addHeaderView(appHeader);
//        btn_get_more_app = (TextView) appHeader.findViewById(R.id.btn_get_more_app);
//        layMoreApp = (LinearLayout) appHeader.findViewById(R.id.lay_new_more_app);
//        btn_get_more_app.setOnClickListener(this);
//        mAppApdater = new AppListInfoAdpter(mContext, mPresenter.getAppMainList());
//        mAppApdater.setDownLoadListener(new AppListInfoAdpter.DownLoadListener() {
//            @Override
//            public void startDownLoad(AppInfo appInfo) {
//
//                mPresenter.setAppDownload(appInfo);
//            }
//        });
//        appGridView.setAdapter(mAppApdater);
//        appGridView.setOnItemClickListener(mAppApdater);
//
//        showLoadingDialog();
//        mPresenter.getAppList();
//    }
//
//    @Override
//    public void initActionView() {
//        appGridView.removeHeaderView(appHeader);
//        appGridView.addHeaderView(actionHeader);
//        moreLay = (LinearLayout) actionHeader.findViewById(R.id.lay_new_more_acion);
//        getMoreLay = (TextView) actionHeader.findViewById(R.id.get_all_action_lay);
//        getMoreLay.setOnClickListener(this);
//        img_type_base_action = (ImageView) actionHeader.findViewById(R.id.img_type_base_action);
//        img_type_dance_action = (ImageView) actionHeader.findViewById(R.id.img_type_dance_action);
//        img_type_story_action = (ImageView) actionHeader.findViewById(R.id.img_type_story_action);
//        img_type_base_action.setOnClickListener(this);
//        img_type_dance_action.setOnClickListener(this);
//        img_type_story_action.setOnClickListener(this);
//        mActionListAdapter = new ActionLastAdpter(mContext, mPresenter.getActionList(), false);
//        appGridView.setAdapter(mActionListAdapter);
//        appGridView.setOnItemClickListener(mActionListAdapter);
//        mActionListAdapter.setDownLoadListener(new ActionLastAdpter.DownLoadListener() {
//            @Override
//            public void startDownLoad(ActionDownLoad acitonDownLoad) {
//                mPresenter.setActionDownload(acitonDownLoad);
//            }
//        });
//        mPresenter.getLastActionList();
//    }
//
//    @Override
//    public void setMoreLayVisiable(boolean isVisiable) {
//        moreLay.setVisibility(isVisiable ?View.VISIBLE : View.GONE);
//    }
//
//    @Override
//    public void setMoreAppVisiable(boolean isVisiable) {
//        layMoreApp.setVisibility(isVisiable ?View.VISIBLE : View.GONE);
//    }
//
//    @Override
//    public void refreshComplete() {
//        appGridView.onRefreshComplete();
//        mActionListAdapter.onNotifyDataSetChanged(mPresenter.getActionList());
//    }
//
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        IntentFilter intentFilter = new IntentFilter(AidlService.RECEIVE_ERROR_DATA);
//        mUnLineBroadcastReceiver = new UnLineBroadcastReceiver();
//        mContext.registerReceiver(mUnLineBroadcastReceiver, intentFilter);
//    }
//
//
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btn_get_more_app:
//                ActivityUtil.replaceFragment(getActivity(), getFragmentManager(), AppMoreListFragement.class.getName(),R.id.layout_fragment_contanier, new Bundle());
//                break;
//            case R.id.get_all_action_lay: {
//                Bundle bundle = new Bundle();
//                bundle.putInt(IntentConstants.ROBOT_ACTION_TYPE, BusinessConstants.ROBOT_ACTION_TYPE_TOTAL);
//                ActivityUtil.replaceFragment(getActivity(), getFragmentManager(), ActionsLibActivity.class.getName(),R.id.layout_fragment_contanier, bundle);
//            }
//            break;
//            case R.id.img_type_base_action: {
//                Bundle bundle = new Bundle();
//                bundle.putInt(IntentConstants.ROBOT_ACTION_TYPE, BusinessConstants.ROBOT_ACTION_TYPE_BASE);
//                ActivityUtil.replaceFragment(getActivity(), getFragmentManager(), ActionsLibActivity.class.getName(),R.id.layout_fragment_contanier, bundle);
//            }
//            break;
//            case R.id.img_type_dance_action: {
//                Bundle bundle = new Bundle();
//                bundle.putInt(IntentConstants.ROBOT_ACTION_TYPE, BusinessConstants.ROBOT_ACTION_TYPE_MUSIC_AND_DANCE);
//                ActivityUtil.replaceFragment(getActivity(), getFragmentManager(), ActionsLibActivity.class.getName(),R.id.layout_fragment_contanier, bundle);
//            }
//            break;
//            case R.id.img_type_story_action: {
//                Bundle bundle = new Bundle();
//                bundle.putInt(IntentConstants.ROBOT_ACTION_TYPE, BusinessConstants.ROBOT_ACTION_TYPE_STORY_AND_FABLE);
//                ActivityUtil.replaceFragment(getActivity(), getFragmentManager(), ActionsLibActivity.class.getName(),R.id.layout_fragment_contanier, bundle);
//            }
//            break;
//        }
//    }
//
//
//
//    @Override
//    public void dismissLoadingDialog() {
//
//    }
//
//    @Override
//    public void showLoadingDialog() {
//
//    }
//
//
//
//
//
//    @Override
//    public void setPresenter(ShopContract.Presenter presenter) {
//        mPresenter = presenter;
//    }
//
//
//
//
//    private class ShopHandler extends BaseHandler {
//
//        @Override
//        public void handleMessage(Message msg) {
//            // TODO Auto-generated method stub
//
//            switch (msg.what) {
//
//
//                case MessageType.ALPHA_DOWNLOAD_ACTIONFILE_STATE:
//                    ActionFileEntrity bean1 = (ActionFileEntrity) msg.obj;
//                    if (bean1.getDownloadState() == 4) {
//                        if (null != mAcitonDownLoadInfo) {
//                            HashMap<String, String> map = new HashMap<String, String>();
//                            map.put("actionName", mAcitonDownLoadInfo.getActionName());
//                            MobclickAgent.onEvent(mContext, Constants.YOUMENT_SHOP_ACTION_DOWNLOAD_TIMES, map);
//                            mAcitonDownLoadInfo = null;
//                        }
//                        mPresenter.getLastActionList();
//                    }
//                    Logger.d(TAG, "*******" + bean1.getDownloadState() + "");
//                    break;
//
//                case MessageType.ALPHA_MSG_DOWNLOAD_APP:
//                    Logger.d( msg.obj.toString());
//                    if (((RobotApp) msg.obj).getDownloadState() == 4) {
//                        if (null != downloadingAppinfo) {
//                            Logger.d("appdown", "shopfragment下载成功");
//                            HashMap<String, String> map = new HashMap<String, String>();
//                            map.put("appName", downloadingAppinfo.getAppName());
//                            MobclickAgent.onEvent(mContext, Constants.YOUMENT_SHOP_APP_DOWNLOAD_TIMES, map);
//                            downloadingAppinfo = null;
//                        }
//                        LoadingDialog.getInstance(mContext).show();
//                        mPresenter.getAppList();
//                    } else if (((RobotApp) msg.obj).getDownloadState() == 5) {
//                        if (downloadingAppinfo != null) {
//                            LoadingDialog.getInstance(mContext).show();
//                            mPresenter.getAppList();
//                            downloadingAppinfo = null;
//                        }
//                        ToastUtils.showLongToast( mContext.getString(R.string.shop_page_installed_failed));
//                    }
//
//            }
//        }
//    }
//
//
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mContext.unregisterReceiver(mUnLineBroadcastReceiver);
//
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        mPresenter.resume();
//
//    }
//
//
//    /**
//     * @Description 初始化广告推介信息
//     * @param
//     * @return
//     * @throws
//     */
//    @Override
//    public void initAD(  String[] mADImageList) {
//
//        viewPage.setDrwableList(mADImageList);
//        viewPage.initView();
//    }
//
//    @Override
//    public void notifyActionAdapter(List<ActionDownLoad> mActionList) {
//        mActionListAdapter.onNotifyDataSetChanged(mActionList);
//    }
//
//
//    @Override
//    public void notifyAppAdapter(List<AppUpdate> mAppMainList) {
//        mAppApdater.onNotifyDataSetChanged(mAppMainList);
//    }
//
//    /**
//     * 广播接收者
//     *
//     * @author weijiang204321
//     */
//    class UnLineBroadcastReceiver extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (intent.getStringExtra(AidlService.AIDL_RECEIVE_ERROR_CODE).equals(context.getString(R.string.main_page_alpha2_offline))) {
//                showLoadingDialog();
//                mPresenter.getAppList();
//            }
//        }
//
//    }
//
//
//}
