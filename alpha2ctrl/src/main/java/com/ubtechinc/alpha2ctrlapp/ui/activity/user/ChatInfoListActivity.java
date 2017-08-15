package com.ubtechinc.alpha2ctrlapp.ui.activity.user;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.common.collect.ImmutableMap;
import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.BaseHandler;
import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.constants.IntentConstants;
import com.ubtechinc.alpha2ctrlapp.constants.NetWorkConstant;
import com.ubtechinc.alpha2ctrlapp.database.NoticeManager;
import com.ubtechinc.alpha2ctrlapp.database.NoticeMessageProvider;
import com.ubtechinc.alpha2ctrlapp.entity.business.user.NoticeMessage;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseContactActivity;
import com.ubtechinc.alpha2ctrlapp.ui.adapter.user.ChatInfoAdapter;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.CommonDiaglog;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

import static com.tencent.qalsdk.service.QalService.context;

/*************************
* @date 2016/6/22
* @author 
* @Description 消息列表页，展示某类消息列表
* @modify 唐宏宇
* @modify_time 2016/6/22
**************************/
public class
ChatInfoListActivity extends BaseContactActivity implements View.OnClickListener,CommonDiaglog.OnPositiveClick, CommonDiaglog.OnNegsitiveClick {

	private ListView listView;
	private ChatInfoAdapter chatInfoAdapter;
	private List<NoticeMessage> messageList = new ArrayList<NoticeMessage>();
	private NoticeManager noticeManager;
	private RelativeLayout no_message_tip;
	private int mNoticeType;

	private TextView editTv, no_message_tv;
	private ImageView noMessageIv;
	boolean isEdit = false;

	private LinearLayout edit_lay;
	CheckBox selectAllCheBox;
	public CommonDiaglog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_info_list);
		mNoticeType = getIntent().getIntExtra(IntentConstants.NOTICE_TYPE, BusinessConstants.NOTICE_TYPE_AUTHORIZE_MSG);

		initView();

	}

	@SuppressWarnings("unchecked")
	public void initView() {
		noticeManager = NoticeManager.getInstance();
		findViewById(R.id.del_btn).setOnClickListener(this);
		noMessageIv = (ImageView) findViewById(R.id.no_message_iv);
		no_message_tv = (TextView) findViewById(R.id.no_message_tv);


		listView = (ListView) findViewById(R.id.list_info_view);
		chatInfoAdapter = new ChatInfoAdapter(this, messageList);
		listView.setAdapter(chatInfoAdapter);
		listView.setOnItemClickListener(chatInfoAdapter);
//		listView.setOnItemLongClickListener(chatInfoAdapter);
		edit_lay =  (LinearLayout) findViewById(R.id.edit_lay);
		selectAllCheBox = (CheckBox) findViewById(R.id.select_checkBox);
		selectAllCheBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(mIsFromAdapter)  {//防止循环执行

					mIsFromAdapter = false;
					return;
				}
				selectAll(isChecked, false);
				chatInfoAdapter.resetCount();
			}
		});
		no_message_tip = (RelativeLayout)findViewById(R.id.no_message_tip);
		editTv = (TextView) findViewById(R.id.edit_tv);
		editTv.setOnClickListener(this);
		dialog = new CommonDiaglog(this, false);
		dialog.setTitle( getString(R.string.news_warning_title));
		dialog.setMessase(getString(R.string.news_warning_describe));
		dialog.setButtonText(getString(R.string.common_btn_cancel), getString(R.string.news_button_delete));
		dialog.setNegsitiveClick(this);
		dialog.setPositiveClick(this);

		setPromptTip();
	}
	private void setPromptTip() {

		switch (mNoticeType) {
			case BusinessConstants.NOTICE_TYPE_AUTHORIZE_MSG :
				noMessageIv.setImageResource(R.drawable.no_authority_message);
				no_message_tv.setText(R.string.news_authorization_none);
				break;
			case BusinessConstants.NOTICE_TYPE_CHAT_TYPE_PIC:
				noMessageIv.setImageResource(R.drawable.no_photos);
				no_message_tv.setText(R.string.news_new_photos_none);
				break;
			case BusinessConstants.NOTICE_TYPE_SYS_MSG:
				noMessageIv.setImageResource(R.drawable.no_system_message);
				no_message_tv.setText(R.string.news_system_none);
				break;
			case BusinessConstants.NOTICE_TYPE_CHAT_MSG:
				noMessageIv.setImageResource(R.drawable.no_comment_reply);
				no_message_tv.setText(R.string.news_comment_reply_none);
				break;
		}
	}





	@Override
	public void onResume(){
		super.onResume();
		initData();
	}
	/**
	 * @Description 数据初始化
	 * @param
	 * @return void
	 */
	private void initData(){
		//从本地取某类消息列表
		messageList.clear();
		messageList.addAll(NoticeMessageProvider.get().findNoticeListByParam(ImmutableMap.of("noticeType=?", (Object) mNoticeType)));

		chatInfoAdapter.onNotifyDataSetChanged(messageList);
		chatInfoAdapter.resetData();
		chatInfoAdapter.notifyDataSetChanged();
		refreshUI();
	}
	private void refreshUI() {
		if(messageList.size()>0){
			no_message_tip.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
			if(isEdit)edit_lay.setVisibility(View.VISIBLE);
			else edit_lay.setVisibility(View.GONE);
			editTv.setVisibility(View.VISIBLE);
		}else {
			no_message_tip.setVisibility(View.VISIBLE);
			listView.setVisibility(View.GONE);
			edit_lay.setVisibility(View.GONE);
			editTv.setVisibility(View.GONE);
		}
	}


	public void deleteNotice() {
		if(mNoticeType == BusinessConstants.NOTICE_TYPE_CHAT_TYPE_PIC) {
			deleteLocalDataAndRefreshUI();
		}else {
			StringBuffer delMessageIds  = new StringBuffer();
			if(selectAllCheBox.isChecked()) {//选中全部
				for(NoticeMessage message : messageList) {
					delMessageIds.append(message.getNoticeId());
					delMessageIds.append(",");
				}

			}else{//未选中全部

				ArrayList<Boolean> delList = chatInfoAdapter.getSelectList();
				for(int i=0; i<delList.size(); i++) {
					if(delList.get(i)) {
						delMessageIds.append(messageList.get(i).getNoticeId());
						delMessageIds.append(",");
					}
				}
			}
			if(delMessageIds.length() == 0)  {
				ToastUtils.showShortToast( R.string.news_please_select_delete);
				return;
			}
			LoadingDialog.getInstance(context).show();
			delMessageIds.deleteCharAt(delMessageIds.length() - 1);
//			request.setNoticeIds(delMessageIds.toString());

			//防止删除系统消息时，把所有人的系统消息都删了
//			request.setToId(mApplication.getUserId());
//			UserAction.getInstance(mContext, null).setHandler(mHandler);
//			UserAction.getInstance(mContext, null).setParamerObj(request);
//			UserAction.getInstance(mContext, null).doRequest(NetWorkConstant.REQUEST_DELETE_NOTICE_MESSAGE, NetWorkConstant.notice_delete);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.edit_tv :

				if(isEdit) {//编辑中，完成->编辑按钮
					editTv.setText(R.string.common_btn_edit);
					chatInfoAdapter.resetData();
					isEdit = false;
				}else{//未编辑，编辑->完成按钮
					editTv.setText(R.string.common_btn_finish);
					isEdit = true;
				}
				if(isEdit)edit_lay.setVisibility(View.VISIBLE);
				else edit_lay.setVisibility(View.GONE);
				chatInfoAdapter.setEditable(isEdit);
				chatInfoAdapter.onNotifyDataSetChanged(messageList);
				chatInfoAdapter.notifyDataSetChanged();
				break;

			case R.id.del_btn :

				dialog.show();
				break;
		}
	}
	private boolean mIsFromAdapter = false;
	/**
	 * 选择所有删除
	 */
	private static final String TAG = "ChatInfoListActivity";
	public void selectAll(boolean isSelectAll, boolean isFromAdapter) {
		Logger.t(TAG).i( "run selectAll = " + isSelectAll + ", isFromAdapter = " +isFromAdapter);

		chatInfoAdapter.setTotalSelect(isSelectAll);

		if(isFromAdapter) {
			mIsFromAdapter = isFromAdapter;
			selectAllCheBox.setChecked(isSelectAll);
		}else {
			for(int i=0; i<chatInfoAdapter.getSelectList().size(); i++) {
				chatInfoAdapter.getSelectList().set(i,isSelectAll);
			}
			chatInfoAdapter.onNotifyDataSetChanged(messageList);
			chatInfoAdapter.notifyDataSetChanged();
		}

	}

	@Override
	public void OnNegsitiveClick() {
		dialog.dismiss();
	}

	@Override
	public void OnPositiveClick() {
		dialog.dismiss();
		deleteNotice();
	}
	private void deleteLocalDataAndRefreshUI() {
		if(selectAllCheBox.isChecked()) {//选中全部
			messageList.clear();
			NoticeMessageProvider.get().deleteNoticeByParam(ImmutableMap.of("noticeType=?", (Object) mNoticeType));

		}else {//未选中全部
			int size = chatInfoAdapter.getSelectList().size();
			for(int i=0; i<size; i++) {
				if(chatInfoAdapter.getSelectList().get(i))

					NoticeMessageProvider.get().deleteNoticeByParam(ImmutableMap.of("noticeId=?", (Object) messageList.get(i).getNoticeId()));
			}


		}
		messageList.clear();

		messageList.addAll(NoticeMessageProvider.get().findNoticeListByParam(ImmutableMap.of("noticeType=?", (Object) mNoticeType)));
		refreshUI();
		chatInfoAdapter.setEditable(isEdit);
		chatInfoAdapter.onNotifyDataSetChanged(messageList);
		chatInfoAdapter.resetData();
		editTv.setText(R.string.common_btn_edit);

		isEdit =false;
		chatInfoAdapter.notifyDataSetChanged();
	}
	private class MHandler extends BaseHandler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
				case NetWorkConstant.RESPONSE_DELETE_MESSAGE_SUCCESS:

					deleteLocalDataAndRefreshUI();

					break;



			}

		}
	}
}