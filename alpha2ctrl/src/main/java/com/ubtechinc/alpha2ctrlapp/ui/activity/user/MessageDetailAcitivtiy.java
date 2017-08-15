package com.ubtechinc.alpha2ctrlapp.ui.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ubtech.utilcode.utils.StringUtils;
import com.ubtech.utilcode.utils.TimeUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.constants.IntentConstants;
import com.ubtechinc.alpha2ctrlapp.database.NoticeManager;
import com.ubtechinc.alpha2ctrlapp.entity.business.user.NoticeMessage;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseContactActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.tencent.qalsdk.service.QalService.context;

/*************************
 * @author
 * @date 2016/6/22
 * @Description 消息详情
 * @modify
 * @modify_time
 **************************/
public class MessageDetailAcitivtiy extends BaseContactActivity {
    private TextView msgContent, timeTv;

    private NoticeManager noticeManager;
    //同意或拒绝授权按钮
    LinearLayout authorize_lay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_detail);
        initView();
    }

    NoticeMessage notice;

    private void initView() {
        noticeManager = NoticeManager.getInstance();
        msgContent = (TextView) findViewById(R.id.msg_content);
        timeTv = (TextView) findViewById(R.id.time_tv);
        this.title = (TextView) findViewById(R.id.authorize_title);

        authorize_lay = (LinearLayout) findViewById(R.id.authorize_lay);
        Intent intent = getIntent();
        if (null != intent) {
            notice = (NoticeMessage) intent.getSerializableExtra(IntentConstants.NOTICE_MESSAGE);

            if (notice != null) {
                timeTv.setText(pareTime(notice.getNoticeTime()));
                if(notice.getNoticeType() == BusinessConstants.NOTICE_TYPE_AUTHORIZE_MSG) {//授权消息
                    title.setText(R.string.msg_authorize);
                    String prompt = "";
                    if(notice.getNoticeDetailType() == BusinessConstants.AUTHORIZATION_TYPE_AUTHORIZE) {
                        prompt = mContext.getString(R.string.news_describe_authorization_invite);
                        authorize_lay.setVisibility(View.VISIBLE);
                        Button sureBtn = (Button) findViewById(R.id.btn_accept);
                        Button cancelBtn = (Button) findViewById(R.id.btn_ignose);

                        msgContent.setText(notice.getFromUserName() + this.getString(R.string.authorize_invate_tips));

                        sureBtn.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {

                            }
                        });
                        cancelBtn.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                finish();
                            }
                        });



                    }else if(notice.getNoticeDetailType() == BusinessConstants.AUTHORIZATION_TYPE_DELETE){//删除授权

                        prompt = mContext.getString(R.string.news_describe_authorization_cancel);

                    }else if(notice.getNoticeDetailType() == BusinessConstants.AUTHORIZATION_TYPE_ACCEPT  ){
                        prompt = mContext.getString(R.string.news_describe_authorization_agree);

                    }else if(notice.getNoticeDetailType() == BusinessConstants.AUTHORIZATION_TYPE_CANCEL  ){
                        prompt = mContext.getString(R.string.news_describe_authorization_relieve);

                    }
                    msgContent.setText(prompt.replace("XX",  StringUtils.nullStringToDefault(notice.getFromUserName())));


                }else if(notice.getNoticeType() == BusinessConstants.NOTICE_TYPE_SYS_MSG){//系统消息

                    msgContent.setText(notice.getNoticeDes());
                    title.setText(R.string.msg_system);
                }
                noticeManager.updateNoticeStatus(notice.getNoticeId(), true); // 改为已读

            }

        }

    }
    private String pareTime(String dateTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(dateTime);
            return TimeUtils.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private void pareDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse("2015-11-11 18:35:35");
            System.out.println(TimeUtils.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }



}
