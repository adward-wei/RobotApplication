package com.ubtechinc.alpha2ctrlapp.ui.fragment.robot;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.base.BaseHandler;
import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.data.robot.IRobotAppDataSource;
import com.ubtechinc.alpha2ctrlapp.data.robot.IRobotVoiceDataSource;
import com.ubtechinc.alpha2ctrlapp.data.robot.RobotAppRepository;
import com.ubtechinc.alpha2ctrlapp.data.robot.RobotVoiceRepository;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.RecordResultInfo;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.ReplaySpeechRcord;
import com.ubtechinc.alpha2ctrlapp.entity.request.GetTranlateRequest;
import com.ubtechinc.alpha2ctrlapp.service.RobotManagerService;
import com.ubtechinc.alpha2ctrlapp.third.AidlService;
import com.ubtechinc.alpha2ctrlapp.ui.activity.robot.MyDeviceActivity;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.base.BaseContactFragememt;
import com.ubtechinc.alpha2ctrlapp.util.Tools;
import com.ubtechinc.alpha2ctrlapp.widget.RefreshListView;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.ConnectRobotDialog;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.LoadingDialog;
import com.ubtechinc.nets.http.ThrowableWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/*************************
 * @author liuhai
 * @date 2017/4/14
 * @Description 中英互译
 * @modify
 * @modify_time
 **************************/
public class TranslateFragment extends BaseContactFragememt implements View.OnClickListener, RefreshListView.OnRefreshListener, TranlateAdapter.OnItemRecordListener {
    private static final String TAG = TranslateFragment.class.getName();
    private static final int HANDLE_POLLING = 10000;
    private TextView tvLeft;
    private TextView tvRight;
    private TextView tvChTip;
    private TextView tvEnTip;
    private ImageView ivChange;
    private boolean isChType = true;
    private RefreshListView mRefreshListView;
    private TranlateAdapter mTranlateAdapter;
    private List<RecordResultInfo> mTranlateInfoList = new ArrayList<>();
    private ConnectRobotDialog mConnectRobotDialog;

    private UnLineBroadcastReceiver mUnLineBroadcastReceiver;
    private Timer mTimer = null;
    private TimerTask mTimerTask = null;
    private static int delay = 10000;  //0s
    private static int period = 5000;  //10s

    private int requestType = 0; //0表示轮询 1 表示下拉 2表示上拉
    private int id = -1;
    private View footerView;
    private boolean isRefreshing = true;
    private boolean isLoading = false;
    private boolean hasAddMore = false;//是否还有获取更多
    private int pageSize = 20;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter intentFilter = new IntentFilter(AidlService.RECEIVE_ERROR_DATA);
        mUnLineBroadcastReceiver = new UnLineBroadcastReceiver();
        mActivity.registerReceiver(mUnLineBroadcastReceiver, intentFilter);
    }

    @Override
    public View onCreateFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_translate, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMainPageActivity.mainTopView.setVisibility(View.GONE);
        mMainPageActivity.bottomLay.setVisibility(View.GONE);
        mMainPageActivity.currentFragment = this;
        Alpha2Application.getInstance().setFromAction(true);
        //每次进入页面都要发送
       // sendTranlateType(0);
        //只有主人才能查看记录
        if (RobotManagerService.getInstance().isRobotOwner()) {
            getTranlateDate(-1, 1, true);
            startTimer();
        } else {
            mRefreshListView.setVisibility(View.GONE);
        }
    }

    @Override
    public void initView() {
        title.setText(getString(R.string.app_tranlate_title));
        title.setVisibility(View.VISIBLE);
        tvLeft = (TextView) mContentView.findViewById(R.id.tv_left);
        tvRight = (TextView) mContentView.findViewById(R.id.tv_right);
        tvChTip = (TextView) mContentView.findViewById(R.id.tv_ch_tip);
        tvEnTip = (TextView) mContentView.findViewById(R.id.tv_en_tip);
        ivChange = (ImageView) mContentView.findViewById(R.id.iv_exchange);
        tvChTip.setText(Html.fromHtml(Constants.app_tranlate_ch_true));
        tvEnTip.setText(Html.fromHtml(Constants.app_tranlate_en_true));
        ivChange.setOnClickListener(this);
        mRefreshListView = (RefreshListView) mContentView.findViewById(R.id.list_tranlate);
        mTranlateAdapter = new TranlateAdapter(mActivity, mTranlateInfoList);
        mTranlateAdapter.setOnItemRecordListener(this);
        mRefreshListView.setAdapter(mTranlateAdapter);
        mRefreshListView.setonRefreshListener(this);
        footerView =  LayoutInflater.from(mActivity).inflate(R.layout.get_more_layout, null, false);
        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.d( "上拉加载");
                isLoading = true;
                requestType = 2;
                getTranlateDate(mTranlateInfoList.get(mTranlateInfoList.size() - 1).getId(), 0, false);
            }
        });
        //机器人掉线
        mConnectRobotDialog = ConnectRobotDialog.getInstance((Activity) mActivity);
        mConnectRobotDialog.setCanceledOnTouchOutside(false);
        mConnectRobotDialog.setPositiveClick(new ConnectRobotDialog.OnPositiveClick() {
            @Override
            public void OnPositiveClick() {
                mConnectRobotDialog.dismiss();
                mConnectRobotDialog = null;
                Intent intent2 = new Intent(mActivity, MyDeviceActivity.class);
                mActivity.startActivity(intent2);
            }
        });
    }

    @Override
    public void onRefresh() {
        isRefreshing = true;
        requestType = 1;
        mHandler.removeMessages(HANDLE_POLLING);
        getTranlateDate(-1, 1, false);
    }


    public BaseHandler mHandler = new BaseHandler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case HANDLE_POLLING:
                    if (!(isRefreshing || isLoading)) {
                        requestType = 0;
                        if (null != mTranlateAdapter.getAppInfos() && mTranlateAdapter.getAppInfos().size() > 0) {
                            getTranlateDate(mTranlateAdapter.getAppInfos().get(0).getId(), 1, false);
                        } else {
                            getTranlateDate(-1, 1, false);
                        }
                    }
                    break;
            }
        }
    };

    /**
     * 刷新数据
     *
     * @param list
     */
    private void RefreshListData(List<RecordResultInfo> list) {
        if (null != list && list.size() > 0) {
            switch (requestType) {
                case 0://轮询
                    for (int i = list.size() - 1; i >= 0; i--) {
                        mTranlateInfoList.add(0, list.get(i));
                    }
                    break;
                case 1://下拉
                    mTranlateInfoList.clear();
                    mTranlateInfoList.addAll(list);
                    break;
                case 2://上拉
                    mTranlateInfoList.addAll(list);
                    break;
            }
        }
        mTranlateAdapter.setAppInfos(mTranlateInfoList);
        if ((isRefreshing || isLoading) && null != list && list.size() == pageSize && !hasAddMore) {
            mRefreshListView.addFooterView(footerView);
            hasAddMore = true;
        } else if ((isRefreshing || isLoading) && null != list && list.size() < pageSize && hasAddMore) {
            mRefreshListView.removeFooterView(footerView);
            hasAddMore = false;
        }
        if (isRefreshing) {
            isRefreshing = false;
            mRefreshListView.onRefreshComplete();
        }
        if (isLoading) {
            isLoading = false;
        }
    }

    /**
     * 根据id获取数据
     *
     * @param id
     */
    private void getTranlateDate(int id, int direction, boolean isLoading) {
        if (!RobotManagerService.getInstance().isConnectedRobot()) {
            return;
        }

        if (isLoading) {
            LoadingDialog.getInstance(mActivity).show();
        }
        GetTranlateRequest getTranlateRequest = new GetTranlateRequest();
        getTranlateRequest.setUserId(Alpha2Application.getAlpha2().getUserId());
        getTranlateRequest.setRobotId(RobotManagerService.getInstance().getConnectedRobot().getEquipmentId());
        getTranlateRequest.setPageSize(pageSize);
        getTranlateRequest.setLabelId("16");
        getTranlateRequest.setDirection(direction);
        if (id != -1) {
            getTranlateRequest.setId(id);
        }

        RobotVoiceRepository.getInstance().getSpeechRecord(getTranlateRequest, new IRobotVoiceDataSource.GetSpeechRecordCallback() {
            @Override
            public void onLoadSpeechRecords(List<RecordResultInfo> speechRecordList) {

            }

            @Override
            public void onDataNotAvailable(ThrowableWrapper e) {

            }
        });
//        UserAction action = UserAction.getInstance(mActivity, null);
//        action.setParamerObj(getTranlateRequest);
//        action.doRequest(NetWorkConstant.REQUEST_GET_APPRECORD_LIST, NetWorkConstant.app_get_record_list);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_exchange:
                if (Tools.isFastClick(2000)) {
                    return;
                }
                int type = -1;
                if (isChType) {
                    isChType = false;
                    tvLeft.setText(getString(R.string.voice_compound_tv_speack_en));
                    tvRight.setText(getString(R.string.voice_compound_tv_speack_ch));
                    tvChTip.setText(Html.fromHtml(Constants.app_tranlate_ch_false));
                    tvEnTip.setText(Html.fromHtml(Constants.app_tranlate_en_false));
                    type = 1;
                } else {
                    tvLeft.setText(getString(R.string.voice_compound_tv_speack_ch));
                    tvRight.setText(getString(R.string.voice_compound_tv_speack_en));
                    tvChTip.setText(Html.fromHtml(Constants.app_tranlate_ch_true));
                    tvEnTip.setText(Html.fromHtml(Constants.app_tranlate_en_true));
                    isChType = true;
                    type = 0;
                }
                sendTranlateType(type);
                break;
        }
    }


    /**
     * 切换翻译语言类型
     *
     * @param type 0中译英  英译中
     */
    private void sendTranlateType(int type) {

        RobotAppRepository.getInstance().clickAppButton(0, BusinessConstants.PACKAGENAME_ALPHA_TRANSLATION, (String.valueOf(type)).getBytes(), new IRobotAppDataSource.ControlAppCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFail(ThrowableWrapper e) {

            }
        });
    }

    /**
     * 播放按钮接口回调
     *
     * @param recordResultInfo
     */
    @Override
    public void playRecord(RecordResultInfo recordResultInfo) {
        ReplaySpeechRcord replaySpeechRcord = new ReplaySpeechRcord();
        replaySpeechRcord.setLabelId(16);
        replaySpeechRcord.setContent(recordResultInfo.getPushContent());
        replaySpeechRcord.setMsgLanguage(recordResultInfo.getMsgLanguage());
        replaySpeechRcord.setRecordId(String.valueOf(recordResultInfo.getId()));

        RobotVoiceRepository.getInstance().replayTTSContent(replaySpeechRcord, new IRobotVoiceDataSource.ReplayTTSContentCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFail(ThrowableWrapper e) {

            }
        });

    }


    /**
     * 广播接收者机器人掉线通知
     *
     * @author weijiang204321
     */
    class UnLineBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra(AidlService.AIDL_RECEIVE_ERROR_CODE).equals(context.getString(R.string.main_page_alpha2_offline))
                    && null != mConnectRobotDialog) {
                Logger.d( "掉线对话框");
                mConnectRobotDialog.show();
                stopTimer();
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity.unregisterReceiver(mUnLineBroadcastReceiver);
    }


    @Override
    public void onStop() {
        super.onStop();
        if (null != mConnectRobotDialog) {
            mConnectRobotDialog.dismiss();
        }
        stopTimer();
    }

    private void startTimer() {
        if (mTimer == null) {
            mTimer = new Timer();
        }

        if (mTimerTask == null) {
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    mHandler.sendEmptyMessage(HANDLE_POLLING);
                }
            };
        }

        if (mTimer != null && mTimerTask != null)
            mTimer.schedule(mTimerTask, delay, period);

    }

    private void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }
}
