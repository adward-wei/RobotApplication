package com.ubtechinc.alpha2ctrlapp.ui.activity.robot;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.SPUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.base.BaseHandler;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.constants.IntentConstants;
import com.ubtechinc.alpha2ctrlapp.constants.NetWorkConstant;
import com.ubtechinc.alpha2ctrlapp.constants.PreferenceConstants;
import com.ubtechinc.alpha2ctrlapp.database.NoticeManager;
import com.ubtechinc.alpha2ctrlapp.entity.business.user.NoticeMessage;
import com.ubtechinc.alpha2ctrlapp.entity.request.AcceptAuthorizeRequest;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseContactActivity;
import com.ubtechinc.alpha2ctrlapp.ui.activity.main.MainPageActivity;
import com.ubtechinc.alpha2ctrlapp.util.DialogUtil;
import com.ubtechinc.alpha2ctrlapp.util.ImageLoad.LoadImage;
import com.ubtechinc.alpha2ctrlapp.util.Tools;
import com.ubtechinc.alpha2ctrlapp.widget.RoundImageView;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.LoadingDialog;

import java.util.ArrayList;
import java.util.List;


/**
 * 同意或取消Alpha设备授权页
 */
public class AcceptAuthorizeActivity extends BaseContactActivity {
    private String TAG = "AcceptAuthorizeActivity";
    private NoticeMessage notice;
    private NoticeManager noticeManager;
    private boolean isOffline = false;
    private List<NoticeMessage> authorizeList = new ArrayList<NoticeMessage>();
    private long existTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.atuthorize_invate_diag);
        DialogUtil.setDialogFourSize(this);
        initView();
    }

    private void initView() {
        noticeManager = NoticeManager.getInstance();
        TextView user_name = (TextView) findViewById(R.id.user_name);
        TextView alpha2_name = (TextView) findViewById(R.id.alpha2_name);
        TextView alpha2_id = (TextView) findViewById(R.id.alpha2_id);
        RoundImageView header = (RoundImageView) findViewById(R.id.user_header);
        Button sureBtn = (Button) findViewById(R.id.btn_accept);
        Button cancelBtn = (Button) findViewById(R.id.btn_ignose);
        isOffline = getIntent().getBooleanExtra(IntentConstants.IS_OFFLINE_MESSAGE, false);
        if (isOffline) {//离线消息
            authorizeList = noticeManager.getUnReadAuthorizeNoticeList(Alpha2Application.getAlpha2().getUserId());
            notice = authorizeList.get(0);
        } else {//在线授权消息
            boolean isOnline = getIntent().getBooleanExtra(IntentConstants.IS_ONLINE_MESSAGE, false);
            if (isOnline) {
                notice = (NoticeMessage) getIntent().getSerializableExtra(IntentConstants.NOTICE_MESSAGE);
//                new GetNoticeMessageTask(this,true, notice).execute();//取一次信息

            } else {
                authorizeList =  noticeManager.getUnReadAuthorizeNoticeList(Alpha2Application.getAlpha2().getUserId());
                notice = authorizeList.get(0);
            }

        }
        if (notice != null) {
            noticeManager.updateNoticeStatus(notice.getNoticeId(), true); // 改为已读
        } else {
            AcceptAuthorizeActivity.this.finish();
        }

        alpha2_id.setText(notice.getRobotSeq());
        user_name.setText(notice.getFromUserName() + this.getString(R.string.authorize_invate_tips));
        LoadImage.LoadHeader(this, 0, header, notice.getFromUserImage());

        sureBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                doAccept();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isOffline) {
                        Intent intent = new Intent(mContext,MainPageActivity.class);
                    mContext.startActivity(intent);

                        Alpha2Application.getInstance().removeActivity();

                } else
                    AcceptAuthorizeActivity.this.finish();
            }
        });


    }

    private class MHandler extends BaseHandler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg == null) {
                Logger.i(TAG, "handleMessage msg is null.");
                return;
            }
            switch (msg.what) {
                case NetWorkConstant.RESPONSE_ACCEPT_QUTHORIZE_SUCCESS:
                    noticeManager.delById(Alpha2Application.getAlpha2().getUserId(),notice.getNoticeId());

                    String robotjid ="";
                    /**后台取得消息不带@，没有@ 需要加上**/
                    if(Tools.isNumeric(notice.getRobotId())){
                        robotjid = Tools.getJidByName(notice.getRobotId());
                    }else{
                        robotjid = notice.getRobotId();

                    }
                    Logger.i("nxy", "id" + robotjid);
                    ((Alpha2Application) AcceptAuthorizeActivity.this.getApplication()).removeActivity();
//                    UserAction userAction = new UserAction(AcceptAuthorizeActivity.this.getApplication(), null);
//                    userAction.setHandler(mHandler);
//                    userAction.doXmppReLogin();//重新登录xmpp才能看到它在线
                    if (isOffline) {
                        LoadingDialog.dissMiss();
                        Constants.hasShowDiag = false;
                        Intent intent = new Intent(mContext, MainPageActivity.class);
                        mContext.startActivity(intent);
                    } else {
                        AcceptAuthorizeActivity.this.finish();
                        LoadingDialog.dissMiss();
                    }

                    break;

            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }



    public void doAccept() {
        if ((System.currentTimeMillis() - existTime) > 2000) {
            existTime = System.currentTimeMillis();
            LoadingDialog.getInstance(mContext).show();
            AcceptAuthorizeRequest request = new AcceptAuthorizeRequest();
            request.setToken(SPUtils.get().getString(PreferenceConstants.TOKEN));
            request.setRelationStatus(1);
            request.setUserId(Alpha2Application.getAlpha2().getUserId());
            request.setUpUserId(String.valueOf(notice.getFromId()));
            request.setEquipmentId(notice.getRobotSeq());
//            UserAction.getInstance(mActivity, null).setHandler(mHandler);
//            UserAction.getInstance(mActivity, null).setParamerObj(request);
//            UserAction.getInstance(mActivity, null).doRequest(NetWorkConstant.REUQEST_ACCEPT_QUTHORIZE, NetWorkConstant.accept);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

