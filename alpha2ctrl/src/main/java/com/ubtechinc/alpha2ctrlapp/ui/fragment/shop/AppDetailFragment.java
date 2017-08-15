package com.ubtechinc.alpha2ctrlapp.ui.fragment.shop;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.ubtech.utilcode.utils.StringUtils;
import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.constants.IntentConstants;
import com.ubtechinc.alpha2ctrlapp.entity.AppDetail;
import com.ubtechinc.alpha2ctrlapp.entity.business.shop.CommentInfo;
import com.ubtechinc.alpha2ctrlapp.service.RobotManagerService;
import com.ubtechinc.alpha2ctrlapp.third.AidlService;
import com.ubtechinc.alpha2ctrlapp.third.IWeiXinListener;
import com.ubtechinc.alpha2ctrlapp.ui.activity.main.MainPageActivity;
import com.ubtechinc.alpha2ctrlapp.ui.adapter.base.ImageGalleryAdpter;
import com.ubtechinc.alpha2ctrlapp.ui.adapter.shop.AppCmdAdpater;
import com.ubtechinc.alpha2ctrlapp.ui.adapter.shop.AppCommentAdpter;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.base.BaseContactFragememt;
import com.ubtechinc.alpha2ctrlapp.util.DownloadFileService;
import com.ubtechinc.alpha2ctrlapp.util.ImageLoad.LoadImage;
import com.ubtechinc.alpha2ctrlapp.widget.DownloadActionAnimation;
import com.ubtechinc.alpha2ctrlapp.widget.NoScrollListView;
import com.ubtechinc.alpha2ctrlapp.widget.NumsCountTextView;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.LoadingDialog;
import com.ubtechinc.alpha2ctrlapp.widget.popWindow.SharePopuWindow;
import com.umeng.analytics.MobclickAgent;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * @author nxy
 * @ClassName AppDetailFragment
 * @date 2016/1/15
 * @Description 商店App详情页
 * @modifier tanghongyu
 * @modify_time 2016/10/15
 */
public class AppDetailFragment extends BaseContactFragememt implements IUiListener, IWeiXinListener, IAppDetailView {
    private static final String TAG = "AppDetailFragment";

    private ListView commentListView;

    private TextView txt_send_comment;
    private EditText edt_comment;
    private ImageView btn_collect, btn_share;
    private LinearLayout btn_collect_lay, btn_share_lay;

    private int mAppId;
    private boolean mIsFromDownload;
    private AppCommentAdpter appCommentAdpter;
    private TextView appnameTv, appTypeTv, appSizeTv, appDownload;
    private ImageView appImageView;
    private ImageView btn_download;
    private Gallery gallery;
    private NumsCountTextView appDesTv, appVerTv;
    private TextView btnDetailExpand, appVersionExpand, app_detail_version;
    private ImageGalleryAdpter galleryAdper;
    private NoScrollListView mCmdListView;
    private AppCmdAdpater mAppCmdAdpater;
    private LinearLayout llAppCmd;
    private TextView tvCmdConnect;
    private List<String> cmdList = new ArrayList<>();
    private RelativeLayout llRobotTip;
    private String[] urls;
    private View headerView;
    private SharePopuWindow shareDialog;

    private View footerView;
    private LinearLayout commentLay;
    private AppDetailPresenter mAppDetailPresenter;

    Animation operatingAnim;
    private UnLineBroadcastReceiver mUnLineBroadcastReceiver;
    private AppDetail mDownLoadInfo;
    private boolean isDownLoading = false;

    @Override
    public View onCreateFragmentView(LayoutInflater inflater,
                                     ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.app_detailfragment, container, false);
    }

    private void initHeaderView() {
        appImageView = (ImageView) mContentView.findViewById(R.id.app_icon);
        appnameTv = (TextView) mContentView.findViewById(R.id.app_name);
        appTypeTv = (TextView) mContentView.findViewById(R.id.app_type);
        appDownload = (TextView) mContentView.findViewById(R.id.download_times);
        appSizeTv = (TextView) mContentView.findViewById(R.id.app_size);
        appDesTv = (NumsCountTextView) mContentView.findViewById(R.id.app_details);
        btn_download = (ImageView) mContentView.findViewById(R.id.btn_update);
        btnDetailExpand = (TextView) mContentView.findViewById(R.id.app_detail_zhankai);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mActivity = getActivity();
        mAppDetailPresenter = new AppDetailPresenter(mActivity, this);
        super.onCreate(savedInstanceState);
        IntentFilter intentFilter = new IntentFilter(AidlService.RECEIVE_ERROR_DATA);
        mUnLineBroadcastReceiver = new UnLineBroadcastReceiver();
        mActivity.registerReceiver(mUnLineBroadcastReceiver, intentFilter);
    }


    @Override
    public void initView() {

        commentListView = (ListView) mContentView.findViewById(R.id.lst_cmd);
        initHeaderView();
        mCmdListView = (NoScrollListView) mContentView.findViewById(R.id.lst_cmd);
        mAppCmdAdpater = new AppCmdAdpater(mActivity, cmdList);
        mCmdListView.setAdapter(mAppCmdAdpater);
        llRobotTip = (RelativeLayout) mContentView.findViewById(R.id.ll_cmd_tip);
        llAppCmd = (LinearLayout) mContentView.findViewById(R.id.app_detail_cmd_ll);
        tvCmdConnect = (TextView) mContentView.findViewById(R.id.tv_app_detail_connect);

        title.setText(getString(R.string.app_details));
        title.setVisibility(View.VISIBLE);
        btn_collect = (ImageView) mContentView.findViewById(R.id.btn_collected);
        btn_share = (ImageView) mContentView.findViewById(R.id.btn_share);
        btn_collect_lay = (LinearLayout) mContentView.findViewById(R.id.btn_collected_lay);
        btn_share_lay = (LinearLayout) mContentView.findViewById(R.id.btn_share_lay);

        btn_share_lay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mAppDetailPresenter.getAppShareUrls();
            }
        });

        btn_collect_lay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showLoadingDialog();
                mAppDetailPresenter.doCollector();
            }
        });
//		edt_comment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//
//			@Override
//			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//				// TODO Auto-generated method stub
//
//				if (actionId== EditorInfo.IME_ACTION_SEND ||(event!=null&&event.getKeyCode()== KeyEvent.KEYCODE_ENTER))
//				{
//					//do something;
//					slidingKeyBroad();
//					//mAppDetailPresenter.addComment(edt_comment.getEditableText().toString());
////					return true;
//				}
//				return false;
//
//			}
//		});
//		edt_comment.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before,
//					int count) {
//				// TODO Auto-generated method stub
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count,
//					int after) {
//			}
//
//			@Override
//			public void afterTextChanged(Editable s) {
//				if(!TextUtils.isEmpty(edt_comment.getText().toString())){
//					commentLay.setBackgroundResource(R.drawable.input_nomal_shape);
//					txt_send_comment.setTextColor(mActivity.getResources().getColor(R.color.text_color_t3));
//					txt_send_comment.setBackgroundResource(R.drawable.send_button_able);
//					txt_send_comment.setClickable(true);
//
//				}else{
//					commentLay.setBackgroundResource(R.drawable.input_diable_shape);
//					txt_send_comment.setTextColor(mActivity.getResources().getColor(R.color.text_color_t4));
//					txt_send_comment.setBackgroundResource(R.drawable.send_button_disable);
//					txt_send_comment.setClickable(false);
//				}
//			}
//		});
//		footerView.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				//mAppDetailPresenter.checkComment();
//			}
//		});
        initData();
    }


    private void initData() {
        Bundle bundle = getBundle();
        mAppId = bundle.getInt(IntentConstants.DATA_APP_ID);
        mAppDetailPresenter.getAppDetail(mActivity, mAppId);

        mIsFromDownload = bundle.getBoolean(IntentConstants.DATA_IS_DOWNLOAD);

//		mAppDetailPresenter.setmIsFromDownload(bundle.getBoolean(IntentConstants.DATA_IS_DOWNLOAD)); ;从第三方跳转过来，不允许自动下载，会引起混乱
        mAppDetailPresenter.registerEventBus();
        bindService();
        operatingAnim = DownloadActionAnimation.getDownloadActionAnimation(mActivity);
    }


    /**
     * @param
     * @return
     * @throws
     * @Description 根据下载状态，变更图标
     */


    @Override
    public void addOrRemoveFooterView(boolean isAdd) {
        if (isAdd) {
            commentListView.addFooterView(footerView);
        } else {
            commentListView.removeFooterView(footerView);
        }
    }

    /**
     * @param
     * @return
     * @throws
     * @Description
     */
    @Override
    public void refreshDownloadIcon(int status) {
        switch (status) {
            case BusinessConstants.APP_STATE_ERROR:
                btn_download.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.disable_download));
                //btn_download.setVisibility(View.GONE);
                btn_download.clearAnimation();
                break;
            case BusinessConstants.APP_STATE_INIT:
                btn_download.setVisibility(View.VISIBLE);
                btn_download.clearAnimation();
                btn_download.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.download_state));
                break;
            case BusinessConstants.APP_STATE_ONLY_MOBILE_APP_NEED_INTALL:
                btn_download.clearAnimation();
                btn_download.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.download_state));
                break;
            case BusinessConstants.APP_STATE_ROBOT_APP_HAS_UPDATE:
                btn_download.clearAnimation();
                btn_download.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.app_refresh));
                break;
            case BusinessConstants.APP_STATE_MOBILE_APP_HAS_UPDATE:
                btn_download.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.download_state));
                btn_download.clearAnimation();
                break;
            case BusinessConstants.APP_STATE_DOWNLOADING:
                btn_download.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.app_installing));
                btn_download.startAnimation(operatingAnim);
                break;
            case BusinessConstants.APP_STATE_DOWNLOAD_SUCCESS:
                btn_download.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.app_installing));
                btn_download.startAnimation(operatingAnim);
                break;
            case BusinessConstants.APP_STATE_DOWNLOAD_FAIL:
                btn_download.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.app_failed));
                btn_download.clearAnimation();
                break;
            case BusinessConstants.APP_STATE_INSTALL_SUCCESS:
                btn_download.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.btn_app_open));
                break;
            case BusinessConstants.APP_STATE_INSTALL_FAIL:
                btn_download.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.app_failed));
                btn_download.clearAnimation();
                //  NToast.longToast(mActivity, mActivity.getString(R.string.shop_page_installed_failed));
                break;
            case BusinessConstants.APP_STATE_CAN_NOT_DOWNLOAD:
                btn_download.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.app_failed));
                btn_download.clearAnimation();
                break;

            case BusinessConstants.APP_STATE_MOBILE_APP_NEED_INTALL:
                btn_download.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.app_installing));
                btn_download.clearAnimation();
                break;
            case BusinessConstants.APP_STATE_APP_ALL_INSTALLED:
                btn_download.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.btn_app_open));
                btn_download.clearAnimation();
                break;

            default:
                break;
        }

    }

    @Override
    public void downloadComplete() {
        LoadingDialog.dissMiss();
    }

    @Override
    public void downloadFail() {
        LoadingDialog.dissMiss();
        ToastUtils.showShortToast( R.string.shop_page_download_falied);
    }

    @Override
    public void downloadPause() {
        LoadingDialog.dissMiss();
    }

    @Override
    public void installApkFail(int status) {
        if (status == 0) {
            ToastUtils.showShortToast( "找不到相应的安装程序");
        } else {
            ToastUtils.showShortToast( "安装文件不存在，请重新下载");
        }
    }

    @Override
    public void refreshAppIntroduction(AppDetail appDetail) {
        mDownLoadInfo = appDetail;
//        if (null != appDetail && null != appDetail.getAppLanguage()) {
//            if (mAppDetailPresenter.getAppLanguage(appDetail.getAppLanguage()) == 0) {
//                appnameTv.setText(appDetail.getAppName());
//            } else {
//                appnameTv.setText(mAppDetailPresenter.getAppLanguage(appDetail.getAppLanguage()));
//            }
//        }

        if (!TextUtils.isEmpty(appDetail.getAppName())) {
            appnameTv.setText(appDetail.getAppName());
        }
//		if(TextUtils.isEmpty(appDetail.getAppLanguageDesciber())){
//			if(!TextUtils.isEmpty(appDetail.getAppResume())){
//				appDesTv.setText(appDetail.getAppResume());
//			}else
//				appDesTv.setText(mActivity.getText(R.string.shop_page_no_description));
//		}else{
//			appDesTv.setText(appDetail.getAppLanguageDesciber());
//		}
        if (!StringUtils.isEmpty(appDetail.getAppDesc())) {
            appDesTv.setText(appDetail.getAppDesc());
        } else {
            appDesTv.setText(getText(R.string.shop_page_no_description));
        }

        if (appDetail.getAppDownloadTime() > 0) {
            appDownload.setVisibility(View.VISIBLE);
            appDownload.setText(appDetail.getAppDownloadTime() + "");
        }
        DecimalFormat df = new DecimalFormat("0.00");
        try {
            double d = Double.valueOf(appDetail.getAppSize()) / 1024.0;
            String db = df.format(d);
            appSizeTv.setText(db + "M");
        } catch (Exception e) {
            e.printStackTrace();
        }

        appTypeTv.setText(mAppDetailPresenter.getAppCategory(appDetail.getAppType()));

        LoadImage.setRounderConner(mMainPageActivity, appImageView, appDetail.getAppIcon(), 1);

        if (appDesTv.isOverFlowed()) {//是否超过TextView大小
            btnDetailExpand.setVisibility(View.VISIBLE);
        } else {

            btnDetailExpand.setVisibility(View.GONE);
        }

        mAppDetailPresenter.manageDownloadButton();

        mAppDetailPresenter.setIsInit(true);
        btn_download.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (RobotManagerService.getInstance().isConnectedRobot()) {
                    isDownLoading = true;
                    mAppDetailPresenter.onClickDownload(mMainPageActivity, downloadFileService);
                } else {
                    ToastUtils.showLongToast( mActivity.getString(R.string.main_page_connect_alpha_tips));
                }
            }
        });
        btnDetailExpand.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (appDesTv.getMaxLines() == 2) {
                    appDesTv.setMaxLines(100);
                    btnDetailExpand.setText(getString(R.string.app_detail_shouqi));
                } else {
                    appDesTv.setMaxLines(2);
                    btnDetailExpand.setText(getString(R.string.app_detail_zhankai));
                }
            }
        });
//		appVersionExpand.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if(appVerTv.getMaxLines()==2){
//					appVerTv.setMaxLines(100);
//					appVersionExpand.setText(mActivity.getString(R.string.app_detail_shouqi));
//				}else{
//					appVerTv.setMaxLines(2);
//					appVersionExpand.setText(mActivity.getString(R.string.app_detail_zhankai));
//				}
//			}
//		});
//        if (mIsFromDownload) {
//            btn_download.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.app_loading));
//        }

        if (!RobotManagerService.getInstance().isConnectedRobot() || StringUtils.isEmpty(appDetail.getAppPath())) {
            refreshDownloadIcon(-1);
        } else {
            refreshDownloadIcon(MainPageActivity.dao.queryAppStatus(
                    appDetail.getPackageName(), appDetail.getVersionCode()));

        }

        cmdList = getAppDetailCmd(mActivity, appDetail.getAppName());
        if (null != cmdList && cmdList.size() > 0) {
            mAppCmdAdpater.setCmdList(cmdList);
            llAppCmd.setVisibility(View.VISIBLE);
            if (!RobotManagerService.getInstance().isConnectedRobot()) {
                llRobotTip.setVisibility(View.VISIBLE);
            } else {
                llRobotTip.setVisibility(View.GONE);
            }
            if (appDetail.getPackageName().equals(BusinessConstants.PACKAGENAME_SHOP_ACTION) || appDetail.getPackageName().equals(BusinessConstants.PACKAGENAME_SHOP_ALARM)) {
                llRobotTip.setVisibility(View.GONE);
                refreshDownloadIcon(BusinessConstants.APP_STATE_INSTALL_SUCCESS);
                mAppDetailPresenter.setmAppStatus(BusinessConstants.APP_STATE_INSTALL_SUCCESS);
            }
        } else {
            llAppCmd.setVisibility(View.GONE);
        }

        dismissLoadingDialog();
        //	mAppDetailPresenter.getAppImageUrls();
    }

    @Override
    public void lostConnection(String mac) {
        mMainPageActivity.isCurrentAlpha2MacLostConnection(mac);
    }

    @Override
    public void refreshCommentList(List<CommentInfo> appCommentList) {
        appCommentAdpter.onNotifyDataSetChanged(appCommentList);
    }

    @Override
    public void addAppComment(boolean isSuccess) {
        if (isSuccess) {
            ToastUtils.showShortToast( R.string.shop_page_add_comment_success);
            edt_comment.setText("");
            //mAppDetailPresenter.checkComment(1);

        } else {
            ToastUtils.showShortToast( R.string.shop_page_comment_send_fail);
        }
    }

    @Override
    public void collectRepeat() {
        btn_collect.setBackgroundResource(R.drawable.action_lib_icon_collection_green);
    }

    @Override
    public void collect(boolean isSuccess) {
        if (isSuccess) {
            ToastUtils.showShortToast( R.string.shop_page_collect_success);
            btn_collect.setBackgroundResource(R.drawable.action_lib_icon_collection_green);
        } else {
            ToastUtils.showShortToast( R.string.shop_page_collect_failed);
        }
    }

    @Override
    public void cancelCollect(boolean isSuccess) {
        if (isSuccess) {
            ToastUtils.showShortToast( R.string.shop_page_cancel_collect_success);
            btn_collect.setBackgroundResource(R.drawable.action_lib_icon_collect);
        } else {
            ToastUtils.showShortToast( R.string.shop_page_cancel_collect_failed);
        }
    }

    @Override
    public void getSharedUrlSuccess() {
        LoadingDialog.dissMiss();
    }

    @Override
    public void changeDownLoadStatus(int status) {
        Logger.d(TAG, "run change changeDownLoadStatus status = " + status);
        switch (status) {
            case BusinessConstants.APP_STATE_INIT:
                btn_download.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.download_state));
                btn_download.clearAnimation();
                break;
            case BusinessConstants.APP_STATE_DOWNLOADING:
                // btn_download.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.app_loading));
                // showLoadingDialog();
                btn_download.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.app_installing));
                btn_download.startAnimation(operatingAnim);
                break;
            case BusinessConstants.APP_STATE_DOWNLOAD_SUCCESS:
                //      dismissLoadingDialog();
                btn_download.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.app_installing));
                break;
            case BusinessConstants.APP_STATE_DOWNLOAD_FAIL:
                dismissLoadingDialog();
                btn_download.clearAnimation();
                btn_download.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.app_failed));
                break;

            case BusinessConstants.APP_STATE_INSTALL_SUCCESS:
                if (isDownLoading) {
                    Logger.d("appdown", "appDetailFragment");
                    HashMap<String, String> map = new HashMap<String, String>();
                    if (null != mDownLoadInfo) {
                        map.put("appName", mDownLoadInfo.getAppName());
                        Logger.d("appdown", "appDetailFragment name ==" + mDownLoadInfo.getAppName());
                    }
                    MobclickAgent.onEvent(mActivity, Constants.YOUMENT_SHOP_APP_DOWNLOAD_TIMES, map);
                    isDownLoading = false;
                    mDownLoadInfo = null;
                }
                if (mAppDetailPresenter.downloadMobileApp(downloadFileService)) {
                    //机器人端解压成功,有手机App客户端，则开始下载手机端
                    btn_download.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.app_loading));
                } else {//没有手机客户端或者已经安装了，则下载完毕，可以打开应用
                    mAppDetailPresenter.setmAppStatus(BusinessConstants.APP_STATE_APP_ALL_INSTALLED);
                    btn_download.clearAnimation();
                    dismissLoadingDialog();
                    btn_download.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.btn_app_open));
                }
//			boolean hasApp = false;
//			for(int y=0;y<mMainPageActivity.appList.size();y++){
//				if(mMainPageActivity.appList.get(y).getPackageName().equals(info.getPackageName())){
//					mMainPageActivity.appList.get(y).setVersionCode(mMainApp.getVersionCode()+"");
//					hasApp = true;
//					break;
//				}
//			}
//			if(!hasApp){
//				info.setVersionCode(mMainApp.getVersionCode()+"");
//				mMainPageActivity.appList.add(info);
//			}
                break;
            case BusinessConstants.APP_STATE_INSTALL_FAIL:
                dismissLoadingDialog();
                btn_download.clearAnimation();
                btn_download.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.app_failed));
                break;
            case BusinessConstants.APP_STATE_INSUFFCIENT_SPACE:
                dismissLoadingDialog();
                btn_download.clearAnimation();
                ToastUtils.showShortToast( R.string.news_storage_title);
                break;

            default:
                break;
        }
    }

    @Override
    public void showShareDialog(AppDetail appDetail) {
        if (shareDialog == null)
            shareDialog = new SharePopuWindow(getActivity(), SharePopuWindow.SHARE_TYPE_APP, appDetail, this, this);
        shareDialog.show();
    }

    @Override
    public void downloadButtonClick() {
        LoadingDialog.getInstance(mActivity).show();
        mAppDetailPresenter.onClickDownload(mMainPageActivity, downloadFileService);
    }

    @Override
    public void dismissLoadingDialog() {
        LoadingDialog.getInstance(mActivity).dismiss();
    }

    @Override
    public void showLoadingDialog() {
        LoadingDialog.getInstance(mActivity).show();
    }

    @Override
    public void showLoadingDialog(boolean isCancelable, int timeout) {
        LoadingDialog.getInstance(mActivity).setCancelable(isCancelable);
        LoadingDialog.getInstance(mActivity).setTimeout(timeout);
        LoadingDialog.getInstance(mActivity).show();
    }

    @Override
    public void setCollectButton(boolean isCollect) {
        if (isCollect) {
            btn_collect.setBackgroundResource(R.drawable.action_lib_icon_collection_green);

        } else {
            btn_collect.setBackgroundResource(R.drawable.action_lib_icon_collect);
        }
    }

    @Override
    public void setAppDetailList(String appDetailList) {
//		appVerTv.setText(appDetailList);
//		if(appVerTv.isOverFlowed()){//是否超过TextView大小
//			appVersionExpand.setVisibility(View.VISIBLE);
//		}else{
//			appVersionExpand.setVisibility(View.GONE);
//		}

    }

    /**
     * @param
     * @return
     * @throws
     * @Description 获取App简介图片
     */
    @Override
    public void getAppImageUrls(String stringURL) {

//   		 if(!StringUtils.isEmpty(stringURL)){
//   			 final  int  size = stringURL.split("\\|\\|").length;
//      		  urls = new String[size];
//      		  for(int i=0;i<size;i++){
//      			  urls[i] = stringURL.split("\\|\\|")[i];
//      		  }
//          	 galleryAdper = new ImageGalleryAdpter(mActivity, urls);
//      	 	gallery.setSelection(urls.length/2);
//      	 	if(galleryAdper!=null){
//   			gallery.setAdapter(galleryAdper);
//      	 	}
//      	 	gallery.setSelection(urls.length/2);
//   		 }else{
//   			 gallery.setVisibility(View.GONE);
//   		 }

    }


    private void bindService() {
        Intent intent = new Intent(mActivity, DownloadFileService.class);
        getActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    private void unbindService() {
        getActivity().unbindService(connection);
    }

    @Override
    public void onDestroy() {
        mAppDetailPresenter.unRegisterEventBus();
        unbindService();
        super.onDestroy();
        mAppDetailPresenter.setIsInit(false);
        mActivity.unregisterReceiver(mUnLineBroadcastReceiver);

    }

    @Override
    public void onResume() {
        super.onResume();
        //    mAppDetailPresenter.onResume();
//		mMainPageActivity.btn_swich_active.se
// tVisibility(View.GONE);
        mMainPageActivity.mainTopView.setVisibility(View.GONE);
        mMainPageActivity.bottomLay.setVisibility(View.GONE);
        mMainPageActivity.currentFragment = this;


    }



    @Override
    public void onCancel() {
        LoadingDialog.dissMiss();
    }

    @Override
    public void onComplete(Object arg0) {
        LoadingDialog.dissMiss();
        ToastUtils.showShortToast( R.string.shop_page_share_success);
    }

    @Override
    public void onError(UiError arg0) {
        // TODO Auto-generated method stub
        LoadingDialog.dissMiss();
        ToastUtils.showShortToast( R.string.shop_page_share_error);

    }

    @Override
    public void noteWeixinNotInstalled() {
        // TODO Auto-generated method stub
        ToastUtils.showShortToast( R.string.ui_weixin_not_install);
    }

    private DownloadFileService downloadFileService;
    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DownloadFileService.BinderImpl binder = (DownloadFileService.BinderImpl) service;
            downloadFileService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };


    @Override
    public void onStop() {
        super.onStop();
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
                refreshDownloadIcon(-1);
                mAppDetailPresenter.setmAppStatus(-1);
                LoadingDialog.dissMiss();
            }
        }

    }

    public  List<String> getAppDetailCmd(Context mcontext, String appName) {
        List<String> list = new ArrayList<>();
        if (appName.equals("动作")) {
            list.add(mcontext.getString(R.string.app_detail_action_cmd_one));
            list.add(mcontext.getString(R.string.app_detail_action_cmd_two));
            list.add(mcontext.getString(R.string.app_detail_action_cmd_three));
        } else if (appName.equals("舞蹈")) {
            list.add(mcontext.getString(R.string.app_detail_dance_cmd_one));
            list.add(mcontext.getString(R.string.app_detail_dance_cmd_two));
        } else if (appName.equals("闹钟")) {
            list.add(mcontext.getString(R.string.app_detail_clock_cmd_one));
            list.add(mcontext.getString(R.string.app_detail_clock_cmd_two));
        } else if (appName.equals("闲聊")) {
            list.add(mcontext.getString(R.string.app_detail_chat_cmd_one));
            list.add(mcontext.getString(R.string.app_detail_chat_cmd_two));
            list.add(mcontext.getString(R.string.app_detail_chat_cmd_three));
        } else if (appName.equals("音乐")) {
            list.add(mcontext.getString(R.string.app_detail_music_cmd_two));
            list.add(mcontext.getString(R.string.app_detail_music_cmd_three));
        } else if (appName.equals("故事")) {
            list.add(mcontext.getString(R.string.app_detail_story_cmd_one));
            list.add(mcontext.getString(R.string.app_detail_story_cmd_two));
        } else if (appName.equals("笑话")) {
            list.add(mcontext.getString(R.string.app_detail_joke_cmd_one));
            list.add(mcontext.getString(R.string.app_detail_joke_cmd_two));
        } else if (appName.equals("天气")) {
            list.add(mcontext.getString(R.string.app_detail_weather_cmd_one));
            list.add(mcontext.getString(R.string.app_detail_weather_cmd_two));
        } else if (appName.equals("时间")) {
            list.add(mcontext.getString(R.string.app_detail_time_cmd_one));
            list.add(mcontext.getString(R.string.app_detail_time_cmd_two));
        }
//        else if (appName.equals("汇率")) {
//            list.add(mcontext.getString(R.string.app_detail_parities_cmd_one));
//            list.add(mcontext.getString(R.string.app_detail_parities_cmd_two));
//        }
        else if (appName.equals("计算")) {
            list.add(mcontext.getString(R.string.app_detail_calculate_cmd_one));
            list.add(mcontext.getString(R.string.app_detail_calculate_cmd_two));
            list.add(mcontext.getString(R.string.app_detail_calculate_cmd_three));
        } else if (appName.equals("诗词")) {
            list.add(mcontext.getString(R.string.app_detail_poem_cmd_one));
            list.add(mcontext.getString(R.string.app_detail_poem_cmd_two));
        } else if (appName.equals("百科")) {
            list.add(mcontext.getString(R.string.app_detail_cyclopedia_cmd_one));
            list.add(mcontext.getString(R.string.app_detail_cyclopedia_cmd_two));
        } else if (appName.equals("拍照")) {
            list.add(mcontext.getString(R.string.app_detail_photo_cmd_one));
            list.add(mcontext.getString(R.string.app_detail_photo_cmd_two));
            list.add(mcontext.getString(R.string.app_detail_photo_cmd_three));
        }

        return list;
    }
}


