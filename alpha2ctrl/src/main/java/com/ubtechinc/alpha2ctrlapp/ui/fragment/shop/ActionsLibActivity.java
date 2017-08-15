package com.ubtechinc.alpha2ctrlapp.ui.fragment.shop;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.ListUtils;
import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.BaseHandler;
import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.constants.IntentConstants;
import com.ubtechinc.alpha2ctrlapp.constants.MessageType;
import com.ubtechinc.alpha2ctrlapp.constants.NetWorkConstant;
import com.ubtechinc.alpha2ctrlapp.data.shop.ActionLocalDataSource;
import com.ubtechinc.alpha2ctrlapp.data.shop.ActionRemoteDataSource;
import com.ubtechinc.alpha2ctrlapp.data.shop.ActionRepository;
import com.ubtechinc.alpha2ctrlapp.data.shop.IActionDataSource;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.ActionDownLoad;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.ActionFileEntrity;
import com.ubtechinc.alpha2ctrlapp.entity.business.shop.ActionInfo;
import com.ubtechinc.alpha2ctrlapp.events.ActionDownloadStatusChangeEvent;
import com.ubtechinc.alpha2ctrlapp.service.RobotManagerService;
import com.ubtechinc.alpha2ctrlapp.ui.activity.app.ActionsLibPreViewActivity;
import com.ubtechinc.alpha2ctrlapp.ui.activity.main.MainPageActivity;
import com.ubtechinc.alpha2ctrlapp.ui.adapter.shop.ActionListAdpter;
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

import static com.ubtechinc.alpha2ctrlapp.ui.activity.main.MainPageActivity.dao;
/**
 * @ClassName ActionsLibActivity
 * @date 6/6/2017
 * @author tanghongyu
 * @Description 商店动作表
 * @modifier
 * @modify_time
 */
public class ActionsLibActivity extends BaseContactFragememt implements ActionListAdpter.DownLoadListener {

    private String TAG = "ActionsLibActivity";
    private RefreshListView lst_actions;
    private int mActionType;
    private ActionListAdpter mActionAdapter;
    private List<ActionDownLoad> mFileList = new ArrayList<>();
    private boolean isSearch;
    private int page = 1;
    private View footerView;
    private boolean isRefresh = false;
    private boolean isGetLast = false;
    private boolean isGetMore;
    private final int COUNT = 15;
    private boolean hasAddGetMore = false;
    private boolean hasCreate = false;
    private RelativeLayout no_search_tip;
    private ImageButton btn_search_icon;
    ;
    private List<ActionInfo> list;
    private ActionDownLoad mDownLoadingAction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isSearch = bundle.getBoolean("isSearch");
        mActionType = bundle.getInt(IntentConstants.ROBOT_ACTION_TYPE);
        actionRepository = ActionRepository.getInstance(ActionLocalDataSource.getInstance(mActivity),ActionRemoteDataSource.getInstance(mActivity));
        if (isSearch) {
            list = (List<ActionInfo>) bundle.getSerializable("list");
        }
        EventBus.getDefault().register(this);

    }

    @Override
    public View onCreateFragmentView(LayoutInflater inflater,
                                     ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return inflater.inflate(R.layout.activity_actions_lib, container, false);
    }

    @Override
    public void initView() {

        lst_actions = (RefreshListView) mMainPageActivity.findViewById(R.id.lst_actions);
        btn_search_icon = (ImageButton) mContentView.findViewById(R.id.btn_search_icon);
        btn_search_icon.setVisibility(View.VISIBLE);

        mActionAdapter = new ActionListAdpter(mActivity, mFileList);
        lst_actions.setAdapter(mActionAdapter);
        lst_actions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position--;
                Bundle bundle = new Bundle();
                bundle.putInt("actionid", mFileList.get(position).getActionId());
                replaceFragment(
                        ActionsLibPreViewActivity.class.getName(), bundle);
            }
        });
        mActionAdapter.setDownLoadListener(this);
        mMainPageActivity.topLayout.setVisibility(View.VISIBLE);
        no_search_tip = (RelativeLayout) mContentView.findViewById(R.id.no_search_tip);
        footerView = (RelativeLayout) LayoutInflater.from(mActivity).inflate(R.layout.get_more_layout, null, false);

        /**列表的下拉刷新**/
        lst_actions.setonRefreshListener(new OnRefreshListener() {
            public void onRefresh() {
                isRefresh = true;
                isGetMore = false;
                if (isSearch) {
//					searchAction(searchEdit.getText().toString());
                } else {
                    if (mActionType == BusinessConstants.ROBOT_ACTION_TYPE_TOTAL)
                        getLastActionList();
                    else
                        getActionList(1);
                }
            }
        });
        btn_search_icon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                bundle = new Bundle();
                bundle.putBoolean(IntentConstants.DATA_IS_LOCAL_DATA, false);
                bundle.putBoolean(IntentConstants.DATA_IS_ACTION_SEARCH, true);
                bundle.putInt(IntentConstants.ROBOT_ACTION_TYPE, mActionType);
                replaceFragment(SearchFragment.class.getName(), bundle);

            }
        });
        footerView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                getActionList(page + 1);
                isGetMore = true;
            }
        });
        if (isSearch) {
            title.setText(getString(R.string.shop_page_action_list));
            refreshListData(list);
        } else {
            mActionType = bundle.getInt(IntentConstants.ROBOT_ACTION_TYPE);
            if (mActionType == BusinessConstants.ROBOT_ACTION_TYPE_TOTAL) {
                title.setText(getString(R.string.shop_page_action_list));
                getLastActionList();
            } else {
                if (mActionType == BusinessConstants.ROBOT_ACTION_TYPE_BASE)
                    title.setText(getString(R.string.shop_page_base_action));
                if (mActionType == BusinessConstants.ROBOT_ACTION_TYPE_MUSIC_AND_DANCE)
                    title.setText(getString(R.string.shop_page_dance_action));
                if (mActionType == BusinessConstants.ROBOT_ACTION_TYPE_STORY_AND_FABLE)
                    title.setText(getString(R.string.shop_page_story_action));
                page = 1;
                getActionList(page);
            }
        }

    }

    @Override
    public void startDownLoad(ActionDownLoad actionDownLoad) {
        this.mDownLoadingAction = actionDownLoad;
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

                case MessageType.ALPHA_LOST_CONNECTED:
                    Logger.i( "handleMessage ALPHA_LOST_CONNECTED.");
                    // isReBack((String) msg.obj);
                    mMainPageActivity.isCurrentAlpha2MacLostConnection((String) msg.obj);
                    break;

//			case MessageType.ALPHA_DOWNLOAD_ACTIONFILE_STATE_FAILURE:
//				ActionFileEntrity bean2 = (ActionFileEntrity)msg.obj;
//				changeDownLoadStatus(2, bean2);
//				break;

                case NetWorkConstant.RESPONSE_ACTION_SEARCH_SUCCESS:
                    isSearch = true;
                    list = (List<ActionInfo>) msg.obj;
                    refreshListData((List<ActionInfo>) msg.obj);
                    LoadingDialog.dissMiss();
                    break;

            }
        }
    }
    ActionRepository actionRepository;
    private void getLastActionList() {
        isGetLast = true;
        actionRepository.getLastActionList(1, COUNT, new IActionDataSource.LoadActionCallback() {
            @Override
            public void onActionLoaded(List<ActionInfo> tasks) {
                LoadingDialog.dissMiss();
                refreshListData(tasks);
            }

            @Override
            public void onDataNotAvailable(ThrowableWrapper e) {
                ToastUtils.showShortToast(e.getMessage());
                LoadingDialog.dissMiss();
            }
        });
        LoadingDialog.getInstance(mActivity).show();
    }

    private void getActionList(int page) {
        this.page = page;

        LoadingDialog.getInstance(mActivity).show();
        actionRepository.getActionList(mActionType, page, COUNT, new IActionDataSource.LoadActionCallback() {
            @Override
            public void onActionLoaded(List<ActionInfo> tasks) {
                refreshListData(tasks);
                LoadingDialog.getInstance(mActivity).dismiss();
            }

            @Override
            public void onDataNotAvailable(ThrowableWrapper e) {
                ToastUtils.showShortToast(e.getMessage());
                LoadingDialog.getInstance(mActivity).dismiss();
            }
        });

    }



    private void refreshListData(List<ActionInfo> list) {
        ActionDownLoad downloadInfo;

        if (!isGetMore)
            mFileList.clear();
        else
            isGetMore = false;
        if (ListUtils.isEmpty(list) ) {
            if (isSearch) {
                no_search_tip.setVisibility(View.VISIBLE);
                lst_actions.setVisibility(View.GONE);
            }

        } else  {
            lst_actions.setVisibility(View.VISIBLE);
            no_search_tip.setVisibility(View.GONE);
            for (int i = 0; i < list.size(); i++) {
                downloadInfo = new ActionDownLoad();
                downloadInfo.setActionName(list.get(i).getActionName());
                if (!RobotManagerService.getInstance().isConnectedRobot()) {
                    downloadInfo.setStatus(-1);
                } else if (list.get(i).getActionPath() == null || TextUtils.isEmpty(list.get(i).getActionPath())) {
                    downloadInfo.setStatus(-1);
                } else {
                    if (TextUtils.isEmpty(list.get(i).getActionOriginalId()))
                        downloadInfo.setStatus(dao.queryActionStatus(list.get(i).getActionName(), list.get(i).getActionPath()));
                    else {
                        downloadInfo.setStatus(dao.queryActionStatus( list.get(i).getActionOriginalId(), list.get(i).getActionPath()));
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
                mFileList.add(downloadInfo);

            }
        }
        // 当数据有过15条，且不是获取最新的15条的时候，出现加载更多
        if (list.size() >= COUNT && !isGetLast && !hasAddGetMore && !isSearch) {
            lst_actions.addFooterView(footerView);
            hasAddGetMore = true;
            lst_actions.setAddMore(true);
        } else if (list.size() < COUNT && hasAddGetMore) {
            lst_actions.removeFooterView(footerView);
            hasAddGetMore = false;
            lst_actions.setAddMore(false);
        } else {
            lst_actions.setAddMore(true);
        }
        if (isGetLast)
            isGetLast = false;
        if (isRefresh) {
            lst_actions.onRefreshComplete();
            isRefresh = false;
        }
        hasCreate = true;
        mActionAdapter.notifyDataSetChanged();

    }

    private void changeDownLoadStatus( ActionFileEntrity info) {
        String originalId = info.getActionOriginalId();
        for (int i = 0; i < mFileList.size(); i++) {
            if (mFileList.get(i).getActionOriginalId().equals(originalId)) {
                mFileList.get(i).setStatus(info.getDownloadState());
            }
        }

        mActionAdapter.notifyDataSetChanged();

        if (null != mDownLoadingAction) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("actionName", mDownLoadingAction.getActionName());
            MobclickAgent.onEvent(mActivity, Constants.YOUMENT_SHOP_ACTION_DOWNLOAD_TIMES, map);
            mDownLoadingAction = null;
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        hasCreate = false;
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();


        hasAddGetMore = false;
        mMainPageActivity.mainTopView.setVisibility(View.GONE);
        mMainPageActivity.bottomLay.setVisibility(View.GONE);
        //		mMainPageActivity.btn_swich_active.setVisibility(View.GONE);
        mMainPageActivity.currentFragment = this;

        if (hasCreate && mFileList.size() > 0) {
            resetStatus();
//			}
        }

    }

    private void resetStatus() {
        for (int i = 0; i < mFileList.size(); i++) {
            if (TextUtils.isEmpty(mFileList.get(i).getActionOriginalId())) {
                mFileList.get(i).setStatus(dao.queryActionStatus( mFileList.get(i).getActionName(), mFileList.get(i).getActionPath()));
            } else {
                mFileList.get(i).setStatus(dao.queryActionStatus( mFileList.get(i).getActionOriginalId(), mFileList.get(i).getActionPath()));
            }

        }

        mActionAdapter.notifyDataSetChanged();
    }

    @Override
    public void refreshDownLoadData() {
        if (mFileList.size() > 0 && RobotManagerService.getInstance().isConnectedRobot()) {
            for (int i = 0; i < mFileList.size(); i++) {
                if (TextUtils.isEmpty(mFileList.get(i).getActionOriginalId())) {
                    mFileList.get(i).setStatus(MainPageActivity.dao.queryActionStatus(
                            mFileList.get(i).getActionName(), mFileList.get(i).getActionPath()));
                } else {
                    mFileList.get(i).setStatus(MainPageActivity.dao.queryActionStatus(
                            mFileList.get(i).getActionOriginalId(), mFileList.get(i).getActionPath()));
                }

            }

        }
        mActionAdapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ActionDownloadStatusChangeEvent event) {
        Logger.t(TAG).d( "onEvent ActionDownloadStatusChangeEvent state = %s" +event.getActionFileEntrity().toString() );
        ActionFileEntrity entrity = event.getActionFileEntrity();
        changeDownLoadStatus(entrity);
    }

}
