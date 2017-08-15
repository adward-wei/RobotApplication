package com.ubtechinc.alpha2ctrlapp.ui.activity.robot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.SPUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.BaseHandler;
import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.data.robot.IRobotActionDataSource;
import com.ubtechinc.alpha2ctrlapp.data.robot.RobotActionRepository;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.NewActionInfo;
import com.ubtechinc.alpha2ctrlapp.service.RobotManagerService;
import com.ubtechinc.alpha2ctrlapp.third.AidlService;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseContactActivity;
import com.ubtechinc.alpha2ctrlapp.ui.adapter.robot.VoiceActionAdapter;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.ConnectRobotDialog;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.LoadingDialog;
import com.ubtechinc.nets.http.ThrowableWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class VoiceAddActionActivity extends BaseContactActivity implements AdapterView.OnItemClickListener, ConnectRobotDialog.OnPositiveClick {

    private static final String TAG = VoiceAddActionActivity.class.getSimpleName();

    private BaseHandler mHandler;
    private List<NewActionInfo> mInfoList = new ArrayList<>();
    private ListView mListView;
    private VoiceActionAdapter mAdapter;
    private LinearLayout llBack;
    private TextView tvTitle;
    private int selectId = -1;
    private String selectedActionName;
    private String seleceActionId;
    private ConnectRobotDialog mConnectRobotDialog;
    private UnLineBroadcastReceiver mUnLineBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_add_action);
        initView();
        IntentFilter intentFilter = new IntentFilter(AidlService.RECEIVE_ERROR_DATA);
        mUnLineBroadcastReceiver = new UnLineBroadcastReceiver();
        registerReceiver(mUnLineBroadcastReceiver, intentFilter);
    }

    /**
     * 初始化View
     */
    private void initView() {
        mConnectRobotDialog = new ConnectRobotDialog(this);
        mConnectRobotDialog.setCanceledOnTouchOutside(false);
        mConnectRobotDialog.setPositiveClick(this);
        selectedActionName = getIntent().getStringExtra(Constants.intent_voice_compound_position_data);
        tvTitle = (TextView) findViewById(R.id.authorize_title);
        tvTitle.setText(this.getString(R.string.voice_compound_tv_action));
        tvTitle.setTextSize(18);
        mListView = (ListView) findViewById(R.id.listView_action);
        mAdapter = new VoiceActionAdapter(this, mInfoList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        llBack = (LinearLayout) findViewById(R.id.ll_back);
        llBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });
        if (!RobotManagerService.getInstance().isConnectedRobot()) {
            mConnectRobotDialog.show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        onGetActionList();
    }

    /**
     * [获取动作列表]
     *
     * @author zengdengyi
     */

    public void onGetActionList() {
        LoadingDialog.getInstance(VoiceAddActionActivity.this).show();


        RobotActionRepository.getInstance().getActionList(Constants.SYSTEM_LAN, 1, new IRobotActionDataSource.GetActionListCallback() {
            @Override
            public void onLoadActionList(List<NewActionInfo> actionInfoList) {
                LoadingDialog.dissMiss();
                searchActionLocal(actionInfoList);
            }

            @Override
            public void onDataNotAvailable(ThrowableWrapper e) {

            }
        });


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mAdapter.setSelectedItem(position);
        selectedActionName = mAdapter.getList().get(position).getActionName();
        if (position != 0) {
            seleceActionId = mAdapter.getList().get(position).getActionId();
        }
    }

    /**
     * 机器人掉线时点击重新连接时从合成页面跳转到
     */
    @Override
    public void OnPositiveClick() {
        Intent intent = new Intent();
//        if (!TextUtils.isEmpty(selectedActionName) && mAdapter.getList().size() > 0) {
//            intent.putExtra(Constants.intent_voice_compound_data_action_name, selectedActionName);
//
//        } else {
//            intent.putExtra(Constants.intent_voice_compound_data_action_name, "");
//        }

        setResult(3, intent);
        finish();
    }



    /**
     * 根据本地的数据筛选动作列表
     */
    private void searchActionLocal(List<NewActionInfo> list) {
        List<NewActionInfo> mCompareList = new ArrayList<>();
        String[] arrays = null;
        String mCurrentSetLanguage = "";

             mCurrentSetLanguage = SPUtils.get().getString(Constants.APP_LAUNGUAGE);

        if (mCurrentSetLanguage.equals(BusinessConstants.APP_LANGUAGE_EN) || !isZh()) {
            arrays = getResources().getStringArray(R.array.voice_action_en);
        } else if (mCurrentSetLanguage.equals(BusinessConstants.APP_LANGUAGE_CN) || (TextUtils.isEmpty(mCurrentSetLanguage) && isZh())) {
            arrays = getResources().getStringArray(R.array.voice_action_ch);
        }
        for (int i = 0; i < arrays.length; i++) {
            Logger.i(TAG, "localAction====  " + arrays[i]);
            for (NewActionInfo newActionInfo : list) {
                if (arrays[i].equalsIgnoreCase(newActionInfo.getActionName())) {
                    mCompareList.add(newActionInfo);
                }
            }
        }

        for (NewActionInfo newActionInfo : list) {
            Logger.i(TAG, "newActionInfo====== " + newActionInfo.getActionName());

        }
        NewActionInfo newActionInfo = new NewActionInfo();
        newActionInfo.setActionName(this.getString(R.string.voice_compound_tv_action_none));
        mCompareList.add(0, newActionInfo);
        mAdapter.setList(mCompareList);
        if (!TextUtils.isEmpty(selectedActionName)) {
            for (int i = 0; i < mAdapter.getList().size(); i++) {
                if (mAdapter.getList().get(i).getActionName().equalsIgnoreCase(selectedActionName)) {
                    mAdapter.setSelectedItem(i);
                    break;
                }
            }
        }
    }

    /**
     * 是否中文系统
     *
     * @return
     */
    private boolean isZh() {
        Locale locale = getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.endsWith("zh"))
            return true;
        else
            return false;
    }

    private void finishActivity() {
        Intent intent = new Intent();
        if (!TextUtils.isEmpty(selectedActionName) && mAdapter.getList().size() > 0) {
            intent.putExtra(Constants.intent_voice_compound_data_action_name, selectedActionName);
        } else {
            intent.putExtra(Constants.intent_voice_compound_data_action_name, "");
        }
        intent.putExtra(Constants.intent_voice_compound_data_action_id, seleceActionId);
        setResult(Constants.intent_voice_compound_resultcode, intent);
        finish();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mUnLineBroadcastReceiver);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishActivity();
        }

        return super.onKeyDown(keyCode, event);

    }

    /**
     * 广播接收者机器人掉线通知
     *
     * @author weijiang204321
     */
    class UnLineBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra(AidlService.AIDL_RECEIVE_ERROR_CODE).equals(context.getString(R.string.main_page_alpha2_offline))) {
                mConnectRobotDialog.show();
            }
        }

    }


}
