package com.ubtechinc.alpha2ctrlapp.ui.activity.user;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ubtech.utilcode.utils.BarUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.base.BaseHandler;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.data.robot.IRobotVoiceDataSource;
import com.ubtechinc.alpha2ctrlapp.data.robot.RobotVoiceRepository;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.RecordResultInfo;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.ReplaySpeechRcord;
import com.ubtechinc.alpha2ctrlapp.entity.request.GetTranlateRequest;
import com.ubtechinc.alpha2ctrlapp.service.RobotManagerService;
import com.ubtechinc.alpha2ctrlapp.third.AidlService;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseContactActivity;
import com.ubtechinc.alpha2ctrlapp.ui.activity.robot.MyDeviceActivity;
import com.ubtechinc.alpha2ctrlapp.ui.adapter.robot.UserRecordAdapter;
import com.ubtechinc.alpha2ctrlapp.widget.MyBottomRelativeLayout;
import com.ubtechinc.alpha2ctrlapp.widget.RefreshListView;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.ConnectRobotDialog;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.LoadingDialog;
import com.ubtechinc.nets.http.ThrowableWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * @author：tanghongyu
 * @date：5/16/2017 9:17 PM
 * @modifier：tanghongyu
 * @modify_date：5/16/2017 9:17 PM
 * [A brief description]
 * version
 */

public class UserRecordActivity extends BaseContactActivity implements View.OnClickListener, RefreshListView.OnRefreshListener, UserRecordAdapter.OnItemRecordListener, MyBottomRelativeLayout.setOnSlideListener {

    private static final String TAG = UserRecordActivity.class.getName();
    private static final int HANDLE_POLLING = 10000;
    private static final int HANDLE_FIRST_ENTER = 10001;
    private ImageView ivClose;
    private LinearLayout llNoneLayout;
    private MyBottomRelativeLayout rlTopLayout;
    private View mView;
    private TextView tvEmpty;
    private RefreshListView mRefreshListView;
    private UserRecordAdapter mUserRecordAdapter;
    private List<RecordResultInfo> mUserRecordInfos = new ArrayList<>();
    private ConnectRobotDialog mConnectRobotDialog;
    private int firstPoll = 0;

    /**
     * 手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
     */
    private float x1 = 0;
    private float x2 = 0;
    private float y1 = 0;
    private float y2 = 0;

    /**
     * 往左边滑动的最小距离，大于这个值时显示悬浮按钮
     */
    private static final int MIN_SCROLL_LEGTH = 60;


    private UnLineBroadcastReceiver mUnLineBroadcastReceiver;
    private Timer mTimer = null;
    private TimerTask mTimerTask = null;
    private static int delay = 1;  //0s
    private static int period = 10000;  //10s

    private int requestType = 0; //0表示轮询 1 表示下拉 2表示上拉
    private int id = -1;
    private View footerView;
    private boolean isRefreshing = true;
    private boolean isLoading = false;
    private boolean hasAddMore = false;//是否还有获取更多
    private int pageSize = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_record);
        BarUtils.setColor(UserRecordActivity.this, getResources().getColor(R.color.transparent), 0);
        initView();
        mHandler.sendEmptyMessageDelayed(5, 650);
        IntentFilter intentFilter = new IntentFilter(AidlService.RECEIVE_ERROR_DATA);
        mUnLineBroadcastReceiver = new UnLineBroadcastReceiver();
        mContext.registerReceiver(mUnLineBroadcastReceiver, intentFilter);
    }


    private void initView() {
        ivClose = (ImageView) findViewById(R.id.iv_close);
        ivClose.setOnClickListener(this);
        llNoneLayout = (LinearLayout) findViewById(R.id.ll_none_layout);
        rlTopLayout = (MyBottomRelativeLayout) findViewById(R.id.bottom_status);
        rlTopLayout.setmSetOnSlideListener(this);
        mView = findViewById(R.id.view_background);
        tvEmpty = (TextView) findViewById(R.id.tv_empty);
        tvEmpty.setText(Html.fromHtml(Constants.record_none));
        mRefreshListView = (RefreshListView) findViewById(R.id.list_record);
        mUserRecordAdapter = new UserRecordAdapter(this, mUserRecordInfos);
        mUserRecordAdapter.setOnItemRecordListener(this);
        mRefreshListView.setAdapter(mUserRecordAdapter);
        mRefreshListView.setonRefreshListener(this);
        footerView = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.get_more_layout, null, false);
        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLoading = true;
                requestType = 2;
                getTranlateDate(mUserRecordInfos.get(mUserRecordInfos.size() - 1).getId(), 0, false);
            }
        });
        //机器人掉线
        mConnectRobotDialog = ConnectRobotDialog.getInstance((Activity) mContext);
        mConnectRobotDialog.setCanceledOnTouchOutside(false);
        mConnectRobotDialog.setPositiveClick(new ConnectRobotDialog.OnPositiveClick() {
            @Override
            public void OnPositiveClick() {
                mConnectRobotDialog.dismiss();
                mConnectRobotDialog = null;
                Intent intent2 = new Intent(mContext, MyDeviceActivity.class);
                mContext.startActivity(intent2);
                finish();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        Alpha2Application.getInstance().setFromAction(true);
        getTranlateDate(-1, 1, false);
        stopTimer();
        startTimer();
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
                        firstPoll++;
                        if (null != mUserRecordAdapter.getAppInfos() && mUserRecordAdapter.getAppInfos().size() > 0) {
                            getTranlateDate(mUserRecordAdapter.getAppInfos().get(0).getId(), 1, false);
                        } else {
                            getTranlateDate(-1, 1, false);
                        }
                    }
                    break;
                case 5:
                    // StatusBarUtil.setColor(UserRecordActivity.this, getResources().getColor(R.color.transparent), 0);
                    ivClose.setVisibility(View.VISIBLE);
                    mView.setBackgroundColor(UserRecordActivity.this.getResources().getColor(R.color.viewfinder_mask));
//                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rlTopLayout.getLayoutParams();
//                    params.setMargins(0, 50, 0, 0);
//                    rlTopLayout.setLayoutParams(params);
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
                        mUserRecordInfos.add(0, list.get(i));
                    }
                    break;
                case 1://下拉
                    mUserRecordInfos.clear();
                    mUserRecordInfos.addAll(list);
                    break;
                case 2://上拉
                    mUserRecordInfos.addAll(list);
                    break;
            }
        }

        if (mUserRecordInfos.size() == 0) {
            mRefreshListView.setVisibility(View.GONE);
            llNoneLayout.setVisibility(View.VISIBLE);
        } else {
            mRefreshListView.setVisibility(View.VISIBLE);
            llNoneLayout.setVisibility(View.GONE);
        }
        mUserRecordAdapter.setAppInfos(mUserRecordInfos);
        if ((isRefreshing || isLoading || firstPoll == 1) && null != list && list.size() == pageSize && !hasAddMore) {
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
     */

    private void getTranlateDate(int id, int direction,boolean isLoading) {
        if (!RobotManagerService.getInstance().isConnectedRobot()) {
            return;
        }
        if (isLoading) {
            LoadingDialog.getInstance(mContext).show();
        }
        GetTranlateRequest getTranlateRequest = new GetTranlateRequest();
        getTranlateRequest.setUserId(Alpha2Application.getAlpha2().getUserId());
        getTranlateRequest.setRobotId(Alpha2Application.getRobotSerialNo());
        getTranlateRequest.setPageSize(pageSize);
        // getTranlateRequest.setLabelId("16");
        getTranlateRequest.setDirection(direction);
        if (id != -1) {
            getTranlateRequest.setId(id);
        }

        RobotVoiceRepository.getInstance().getSpeechRecord(getTranlateRequest, new IRobotVoiceDataSource.GetSpeechRecordCallback() {
            @Override
            public void onLoadSpeechRecords(List<RecordResultInfo> speechRecordList) {
                LoadingDialog.dissMiss();
                if (requestType == 1) {
                }
                RefreshListData(speechRecordList);
            }

            @Override
            public void onDataNotAvailable(ThrowableWrapper e) {
                LoadingDialog.dissMiss();
            }
        });
    }


    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(0, R.anim.activity_close);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                mView.setBackgroundColor(UserRecordActivity.this.getResources().getColor(R.color.transparent));
                ivClose.setVisibility(View.GONE);
                finish();
                break;
        }
    }

    /**
     * 点击播放按钮回调
     *
     * @param recordResultInfo
     */
    @Override
    public void playRecord(RecordResultInfo recordResultInfo) {
        ReplaySpeechRcord replaySpeechRcord = new ReplaySpeechRcord();
        replaySpeechRcord.setLabelId(recordResultInfo.getLabelId());
        replaySpeechRcord.setContent(recordResultInfo.getPushContent());
        replaySpeechRcord.setContentLinks(recordResultInfo.getPushLinks());
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
            if (intent.getStringExtra(AidlService.AIDL_RECEIVE_ERROR_CODE).equals(context.getString(R.string.main_page_alpha2_offline)) && null != mConnectRobotDialog) {
                mConnectRobotDialog.show();
                stopTimer();
            }
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopTimer();
        unregisterReceiver(mUnLineBroadcastReceiver);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (null != mConnectRobotDialog) {
            mConnectRobotDialog.dismiss();
        }
    }


    @Override
    public void onBottomToTop() {

    }

    @Override
    public void onTopToBottom() {
        mView.setBackgroundColor(UserRecordActivity.this.getResources().getColor(R.color.transparent));
        ivClose.setVisibility(View.GONE);
        finish();
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
