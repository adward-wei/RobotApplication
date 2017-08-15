package com.ubtechinc.alpha2ctrlapp.ui.activity.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.base.BaseHandler;
import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.data.robot.IRobotInitDataSource;
import com.ubtechinc.alpha2ctrlapp.data.robot.RobotInitRepository;
import com.ubtechinc.alpha2ctrlapp.entity.ServiceLanguage;
import com.ubtechinc.alpha2ctrlapp.service.RobotManagerService;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseContactActivity;
import com.ubtechinc.alpha2ctrlapp.ui.activity.main.MainPageActivity;
import com.ubtechinc.alpha2ctrlapp.ui.adapter.app.ServiceLanguageAdapter;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.CommonDiaglog;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.LoadingDialog;
import com.ubtechinc.nets.http.ThrowableWrapper;

import java.util.ArrayList;

/**
 * @ClassName ServiceLanguageActivity
 * @date 2016/7/22
 * @author tanghongyu
 * @Description 主服务语言版本
 * @modifier
 * @modify_time
 */
public class ServiceLanguageActivity extends BaseContactActivity implements
        View.OnClickListener,CommonDiaglog.OnNegsitiveClick,CommonDiaglog.OnPositiveClick {
    ListView lv_language;
    private  int dilogType;

    @Override
    public void OnNegsitiveClick() {
        dialog.dismiss();
    }

    @Override
    public void OnPositiveClick() {
        if(dilogType!=RESE_STATE_SUCCESS){
            setServiceLanguage(mCheckedLanguage.getLanguage());
        }else{
            dialog.dismiss();
            LoadingDialog.getInstance(mContext).show();
            mHandler.sendEmptyMessageDelayed(-1,10*1000);// 避免延时问题，让用户等待10s
        }
        dialog.dismiss();
    }

    ServiceLanguageAdapter historyConnectionAdapter;
    ArrayList<ServiceLanguage> mHistoryConnections;
    ServiceLanguage mCheckedLanguage;
    //选中的位置
    private int mCheckedPos = -1;
    TextView btn_top_right;
    private CommonDiaglog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_language);
        initView();
        initData();
    }

    private void initView() {
        dialog = new CommonDiaglog(this,false);
        title= (TextView)findViewById(R.id.authorize_title);
        title.setText(R.string.versionchange_version);
        lv_language = (ListView) findViewById(R.id.lv_language);
        btn_top_right = (TextView) findViewById(R.id.btn_top_right);
        btn_top_right.setVisibility(View.VISIBLE);
        btn_top_right.setText(R.string.common_btn_confirm);
        btn_top_right.setOnClickListener(this);
        dialog.setNegsitiveClick(this);
        dialog.setPositiveClick(this);
    }
    private ServiceLanguage mCurrentLanguage;
    private void initData() {
        //查询本地历史连接记录
        mHandler = new MyHandler();
        mHistoryConnections = new ArrayList<>();
        Intent intent = getIntent();
        ServiceLanguage chinese = new ServiceLanguage();
        chinese.setLanguage(BusinessConstants.ROBOT_SERVICE_LANGUAGE_CHINESE);
        chinese.setLanguageShow(BusinessConstants.ROBOT_SERVICE_LANGUAGE_SHOW_CHINESE);
        ServiceLanguage english = new ServiceLanguage();
        english.setLanguage(BusinessConstants.ROBOT_SERVICE_LANGUAGE_ENGLISH);
        english.setLanguageShow(BusinessConstants.ROBOT_SERVICE_LANGUAGE_SHOW_ENGLISH);

        if(MainPageActivity.alphaParam.getServiceLanguage() != null && BusinessConstants.ROBOT_SERVICE_LANGUAGE_CHINESE.equalsIgnoreCase(MainPageActivity.alphaParam.getServiceLanguage())) {
            chinese.setChecked(true);
            mCheckedPos = 0;
            mCurrentLanguage = chinese;
           ToastUtils.showShortToast( R.string.versionchange_version_chinese);
        }else {
           ToastUtils.showShortToast( R.string.versionchange_version_english);

            english.setChecked(true);
            mCurrentLanguage = english;
            mCheckedPos = 1;
        }
        mHistoryConnections.add(chinese);
        mHistoryConnections.add(english);
        historyConnectionAdapter = new ServiceLanguageAdapter(mApplication, mHistoryConnections);
        lv_language.setAdapter(historyConnectionAdapter);
        lv_language.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCheckedLanguage =  mHistoryConnections.get(position);
                mCheckedLanguage.setChecked(true);
                if(mCheckedPos >= 0) mHistoryConnections.get(mCheckedPos).setChecked(false);
                mCheckedPos = position;
                historyConnectionAdapter.notifyDataSetChanged();

            }
        });
    }
    private void setServiceLanguage(final String language){
        LoadingDialog.getInstance(mContext).show();
        RobotInitRepository.getInstance().setServiceLanguage(language, new IRobotInitDataSource.SetInitDataCallback() {
            @Override
            public void onSuccess() {
                LoadingDialog.dissMiss();
                MainPageActivity.alphaParam.setServiceLanguage(language);
                showSetDialog(RESE_STATE_SUCCESS);
            }

            @Override
            public void onFail(ThrowableWrapper e) {
                showSetDialog(RESE_STATE_FAIL);
            }
        });


    }
    private class MyHandler extends BaseHandler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
          if(msg.what==-1){
                LoadingDialog.dissMiss();

                Intent intent  = new Intent(ServiceLanguageActivity.this,MainPageActivity.class);
                startActivity(intent);
                RobotManagerService.getInstance().clearConnectCacheData();
            }
        }
    }
    private final int RESE_STATE_CONFIM = 0; //确认
    private final int RESE_STATE_SUCCESS = 1;//成功
    private final int RESE_STATE_FAIL = 2;//失败
    private void showSetDialog(int state) {
        dilogType = state;
        if(dialog==null){
            dialog = new CommonDiaglog(ServiceLanguageActivity.this,false);
            dialog.setNegsitiveClick(this);
            dialog.setPositiveClick(this);
        }
         if(state == RESE_STATE_SUCCESS) {
            dialog.setTitle(getResources().getString(R.string.versionchange_change_reset_success));
            dialog.setMessase(getResources().getString(R.string.versionchange_change_reset));
        }else if(state == RESE_STATE_FAIL){
            dialog.setTitle(getResources().getString(R.string.versionchange_change_reset_fail));
            dialog.setButtonText(getResources().getString(R.string.common_btn_cancel), getResources().getString(R.string.versionchange_change_retry));
            dialog.setNegsitiveClick(new CommonDiaglog.OnNegsitiveClick() {
                @Override
                public void OnNegsitiveClick() {
                    dialog.dismiss();
                }
            });

        }else {
            dialog.setTitle(getResources().getString(R.string.versionchange_change_require));
            dialog.setMessase(getResources().getString(R.string.versionchange_change_reset_disconnected));
            dialog.setButtonText(getResources().getString(R.string.common_btn_cancel), getResources().getString(R.string.common_btn_confirm));

        }
        dialog.show();
    }





    @Override
    public void onDestroy(){
        super.onDestroy();

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_top_right :
                if(!mCheckedLanguage.equals(mCurrentLanguage)) {
                    showSetDialog(RESE_STATE_CONFIM);
                }

                break;
        }
    }
}
