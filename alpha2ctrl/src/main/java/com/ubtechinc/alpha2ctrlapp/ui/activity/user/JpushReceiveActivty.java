package com.ubtechinc.alpha2ctrlapp.ui.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.collect.ImmutableMap;
import com.ubtech.utilcode.utils.ListUtils;
import com.ubtech.utilcode.utils.TimeUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.BaseHandler;
import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.constants.IntentConstants;
import com.ubtechinc.alpha2ctrlapp.constants.NetWorkConstant;
import com.ubtechinc.alpha2ctrlapp.data.Injection;
import com.ubtechinc.alpha2ctrlapp.data.user.MessageRepository;
import com.ubtechinc.alpha2ctrlapp.database.NoticeManager;
import com.ubtechinc.alpha2ctrlapp.database.NoticeMessageProvider;
import com.ubtechinc.alpha2ctrlapp.entity.business.user.NoticeMessage;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseContactActivity;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.LoadingDialog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.tencent.qalsdk.service.QalService.context;

/**
 * @author：tanghongyu
 * @date：5/17/2017 2:54 PM
 * @modifier：tanghongyu
 * @modify_date：5/17/2017 2:54 PM
 * [A brief description]
 * version
 */

public class JpushReceiveActivty extends BaseContactActivity implements View.OnClickListener {

    private NoticeManager noticeManager;


    private TextView authoriTv, photoTv, systemTv, photo_time_tv, system_time_tv, authorization_time_tv;
    private ImageView msg_authourise_new, msg_photo_new, msg_system_new;
    MessageRepository messageRepository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_layout);
        initView();
        messageRepository = Injection.provideMessageRepository(mApplication);
    }

    @SuppressWarnings("unchecked")
    public void initView() {
        title = (TextView) findViewById(R.id.authorize_title);
        title.setText(R.string.msg_title);
        noticeManager = NoticeManager.getInstance();
        authoriTv = (TextView) findViewById(R.id.authorize_summary);
        photoTv = (TextView) findViewById(R.id.photo_summary);
        systemTv = (TextView) findViewById(R.id.system_summary);
        msg_authourise_new = (ImageView) findViewById(R.id.msg_authorise_new);
        msg_photo_new = (ImageView) findViewById(R.id.msg_photo_new);
        msg_system_new = (ImageView) findViewById(R.id.msg_system_new);
        photo_time_tv = (TextView) findViewById(R.id.photo_time_tv);
        system_time_tv = (TextView) findViewById(R.id.system_time_tv);
        authorization_time_tv = (TextView) findViewById(R.id.authorization_time_tv);

        findViewById(R.id.authrise_lay).setOnClickListener(this);
        findViewById(R.id.photo_lay).setOnClickListener(this);
        findViewById(R.id.system_lay).setOnClickListener(this);
    }






    private void initData() {

        //获取所有未读图片消息
        List<NoticeMessage> unReadPic = noticeManager.getUnReadNoticeListByType(BusinessConstants.NOTICE_TYPE_CHAT_TYPE_PIC);
        if (ListUtils.isEmpty(unReadPic)) {

            photoTv.setText(R.string.news_new_photos_none);
            msg_photo_new.setVisibility(View.GONE);
        } else {

            photo_time_tv.setText(pareTime(unReadPic.get(0).getNoticeTime()));
            photoTv.setText(mContext.getString(R.string.news_new_photos).replace("N", unReadPic.size() + ""));
            //有未读消息则显示提醒
            msg_photo_new.setVisibility(View.VISIBLE);
        }
        //获取所有未读授权消息
        List<NoticeMessage> unReadAuthorize = noticeManager.getUnReadNoticeListByType(BusinessConstants.NOTICE_TYPE_AUTHORIZE_MSG);

        if (ListUtils.isEmpty(unReadAuthorize)) {
            msg_authourise_new.setVisibility(View.GONE);
            authoriTv.setText(R.string.news_authorization_none);

        } else {

            NoticeMessage authoriztion = unReadAuthorize.get(0);
            switch (authoriztion.getNoticeDetailType()) {
                case BusinessConstants.AUTHORIZATION_TYPE_AUTHORIZE://授权
                    //取第一条授权消息显示到内容中
                    authoriTv.setText(getString(R.string.news_authorization_invite).replace("XX", authoriztion.getFromUserName()));
                    break;
                case BusinessConstants.AUTHORIZATION_TYPE_DELETE://删除授权
                    //取第一条授权消息显示到内容中
                    authoriTv.setText(getString(R.string.news_authorization_cancel).replace("XX", authoriztion.getFromUserName()));
                    break;
                case BusinessConstants.AUTHORIZATION_TYPE_ACCEPT://同意授权
                    //取第一条授权消息显示到内容中
                    authoriTv.setText(getString(R.string.news_authorization_agree).replace("XX", authoriztion.getFromUserName()));
                    break;
                case BusinessConstants.AUTHORIZATION_TYPE_CANCEL://取消授权

                    authoriTv.setText(getString(R.string.news_authorization_relieve).replace("XX", authoriztion.getFromUserName()));
                    break;
            }

            authorization_time_tv.setText(pareTime(authoriztion.getNoticeTime()));

            msg_authourise_new.setVisibility(View.VISIBLE);
        }

        List<NoticeMessage> unReadSystemMsg = noticeManager.getUnReadNoticeListByType(BusinessConstants.NOTICE_TYPE_SYS_MSG);
        if (ListUtils.isEmpty(unReadSystemMsg)) {
            msg_system_new.setVisibility(View.GONE);
            systemTv.setText(R.string.news_system_none);

        } else {
            NoticeMessage systemMsg = unReadSystemMsg.get(0);
            //取第一条系统消息显示到内容中
            systemTv.setText(systemMsg.getNoticeDes());
            //获取所有未读系统消息
            system_time_tv.setText(pareTime(systemMsg.getNoticeTime()));
            ;
            //有未读消息则显示提醒
            msg_system_new.setVisibility(View.VISIBLE);

        }

    }

    private String pareTime(String dateTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = null;
        try {
            date = format.parse(dateTime);
            return TimeUtils.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.authrise_lay:
                getToMsgList(BusinessConstants.NOTICE_TYPE_AUTHORIZE_MSG);
                break;
            case R.id.photo_lay:
                getToMsgList(BusinessConstants.NOTICE_TYPE_CHAT_TYPE_PIC);
                break;
            case R.id.system_lay:
                getToMsgList(BusinessConstants.NOTICE_TYPE_SYS_MSG);
                break;
            default:
                break;
        }
    }

    /**
     * @param msgType 消息类型
     * @return void
     * @Description 跳转到消息列表
     */
    private void getToMsgList(int msgType) {
        Intent intent = new Intent(JpushReceiveActivty.this, ChatInfoListActivity.class);
        intent.putExtra(IntentConstants.NOTICE_TYPE, msgType);
        startActivity(intent);
    }

    private class MHandler extends BaseHandler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);


            switch (msg.what) {
                case NetWorkConstant.RESPONSE_GET_MESSAGE_SUCCESS:
                    List<NoticeMessage> list = (List<NoticeMessage>) msg.obj;
//					//先删除本地的授权和系统消息
                    for (NoticeMessage message : list) {
                        message.setUserId(mApplication.getUserId());
                    }
//
                    NoticeMessageProvider.get().deleteNoticeByParam(ImmutableMap.of("noticeType", (Object)BusinessConstants.NOTICE_TYPE_SYS_MSG));
                    NoticeMessageProvider.get().deleteNoticeByParam(ImmutableMap.of("noticeType", (Object)BusinessConstants.NOTICE_TYPE_AUTHORIZE_MSG));
                    //将取到的消息存本地
                    NoticeMessageProvider.get().saveAll(list);
                    break;
                case NetWorkConstant.RESPONSE_GET_NOTICIMG_SUCCESS:
                    LoadingDialog.getInstance(context).dissMiss();
                    break;
            }
            initData();
        }
    }


}
