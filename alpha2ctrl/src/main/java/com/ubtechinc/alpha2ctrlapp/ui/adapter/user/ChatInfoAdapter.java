package com.ubtechinc.alpha2ctrlapp.ui.adapter.user;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.StringUtils;
import com.ubtech.utilcode.utils.TimeUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.constants.IntentConstants;
import com.ubtechinc.alpha2ctrlapp.entity.business.user.NoticeMessage;
import com.ubtechinc.alpha2ctrlapp.ui.activity.user.ChatInfoListActivity;
import com.ubtechinc.alpha2ctrlapp.ui.activity.user.MessageDetailAcitivtiy;
import com.ubtechinc.alpha2ctrlapp.ui.activity.user.PictureActivity;
import com.ubtechinc.alpha2ctrlapp.util.ImageLoad.LoadImage;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*************************
* @date 2016/6/22
* @author 
* @Description 消息列表适配器
* @modify
* @modify_time
**************************/
public class ChatInfoAdapter extends BaseAdapter implements
        OnItemClickListener {
	private static final String TAG = "ChatInfoAdapter";
	private LayoutInflater inflater;
	private Context mContext;
	private List<NoticeMessage> mItemList;
	private ChatInfoListActivity activity;
	//是否编辑模式
	private boolean isEdit = false;
	private ArrayList<Boolean> selectList = new ArrayList<>();
	private boolean isTotalSelect;
	public ChatInfoAdapter(Context context, List<NoticeMessage> chatList) {
		mContext = context;
		activity = (ChatInfoListActivity)context;
		mItemList = chatList;

		this.inflater = LayoutInflater.from(context);

	}


	/**
	 * 设置是否编辑或完成编辑
	 * @param isEdit
     */
	public void setEditable(boolean isEdit) {
		this.isEdit = isEdit;

	}
	public ArrayList<Boolean> getSelectList() {
		return selectList;
	}
	public void setTotalSelect(boolean isTotalSelect) {

		this.isTotalSelect = isTotalSelect;

		Logger.t(TAG).i( "run setTotalSelect  " + ", selectCount=" + selectCount+ ", isTotalSelect = " +isTotalSelect);
	}
	public void resetCount() {
		if(isTotalSelect) selectCount = mItemList.size();
		else selectCount = 0;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mItemList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mItemList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;
		if (viewHolder == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.chat_info_item, null, false);
			viewHolder.head_iv = (ImageView) convertView
					.findViewById(R.id.head_pic);
			viewHolder.object_tv = (TextView) convertView
					.findViewById(R.id.chat_object);
			viewHolder.time_tv = (TextView) convertView
					.findViewById(R.id.time_tv);
			viewHolder.msg_status= (ImageView)convertView.findViewById(R.id.msg_status);
			viewHolder.select_checkBox= (CheckBox)convertView.findViewById(R.id.select_checkBox);
			viewHolder.arrow_iv =  (ImageView)convertView.findViewById(R.id.arrow_iv);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		NoticeMessage item = mItemList.get(position);
		if(item.getIsRead()== BusinessConstants.NOTICE_STATE_UNREAD){
			viewHolder.msg_status.setVisibility(View.VISIBLE);
		}else {
			viewHolder.msg_status.setVisibility(View.GONE);
		}
		if(isEdit) {
			Logger.t(TAG).i(  "run getView = "+ ", selectList.get(position) = " +selectList.get(position) + " isTotalSelect=" + isTotalSelect);
			viewHolder.select_checkBox.setVisibility(View.VISIBLE);//编辑模式
			if(isTotalSelect  || selectList.get(position)) {
				viewHolder.select_checkBox.setChecked(true);
			}else {
				viewHolder.select_checkBox.setChecked(false);
			}
			viewHolder.arrow_iv.setVisibility(View.GONE);
		} else{
			  viewHolder.select_checkBox.setVisibility(View.GONE);//完成编辑
			viewHolder.arrow_iv.setVisibility(View.VISIBLE);
		}

		viewHolder.time_tv.setText(pareTime(item.getNoticeTime()));
		if(item.getNoticeType() == BusinessConstants.NOTICE_TYPE_AUTHORIZE_MSG) {//授权消息

//			AuthorizeBody	body =	JsonUtils.getInstance().jsonToBean(item.getNoticeContent(), AuthorizeBody.class);

			LoadImage.setRounderConner(activity, viewHolder.head_iv, item.getFromUserImage(),0);
			String prompt = "";
			if(item.getNoticeDetailType() == BusinessConstants.AUTHORIZATION_TYPE_AUTHORIZE) {
				prompt = mContext.getString(R.string.news_authorization_invite);


			}else if(item.getNoticeDetailType() == BusinessConstants.AUTHORIZATION_TYPE_DELETE){
				prompt = mContext.getString(R.string.news_authorization_cancel);

			}else if(item.getNoticeDetailType() == BusinessConstants.AUTHORIZATION_TYPE_ACCEPT){
				prompt = mContext.getString(R.string.news_authorization_agree);

			}else if(item.getNoticeDetailType() == BusinessConstants.AUTHORIZATION_TYPE_CANCEL  ){
				prompt = mContext.getString(R.string.news_authorization_relieve);

			}
			viewHolder.object_tv.setText(prompt.replace("XX", StringUtils.nullStringToDefault(item.getFromUserName())));

			
		}else if(item.getNoticeType() == BusinessConstants.NOTICE_TYPE_SYS_MSG){//系统消息
			viewHolder.head_iv.setImageResource(R.drawable.msg_system);
			viewHolder.object_tv.setText(item.getNoticeDes());

		}else if(item.getNoticeType() == BusinessConstants.NOTICE_TYPE_CHAT_TYPE_PIC) {

			viewHolder.object_tv.setText( item.getNoticeContent().substring(item.getNoticeContent().lastIndexOf("/") + 1));
			Logger.t(TAG).i( "nxy", "url"+item.getNoticeContent());

		}
	
		return convertView;
	}

	class ViewHolder {
		ImageView head_iv;
		ImageView arrow_iv;
		TextView object_tv;
		TextView time_tv;
		ImageView msg_status;
		CheckBox select_checkBox;
	}
	public void onNotifyDataSetChanged(List<NoticeMessage> mItemList) {

		this.mItemList = mItemList;

	}

	public void resetData() {
		selectCount = 0;
		selectList.clear();
		for(int i= 0; i<mItemList.size(); i++ ) {
			selectList.add(false);
		}
	}
	private int selectCount = 0;
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
		if(isEdit) {//编辑模式
			Logger.t(TAG).d( "run onItemClick = "+ ", selectList.get(position) = " +selectList.get(position) + " selectCount=" + selectCount);
			if(selectList.get(position)) {
				selectList.set(position, false);
				selectCount--;
				if(selectCount == mItemList.size()-1)activity.selectAll(false, true);
			} else {
				selectList.set(position, true);
				selectCount++;
				if(selectCount == mItemList.size())activity.selectAll(true, true);
			}
			notifyDataSetChanged();

		}else {

			NoticeMessage item = (NoticeMessage) mItemList.get(position);
			if(item.getNoticeType() == BusinessConstants.NOTICE_TYPE_CHAT_TYPE_PIC) {//图片消息
				Intent intent = new Intent(mContext, PictureActivity.class);
				intent.putExtra(IntentConstants.NOTICE_MESSAGE,item);
				mContext.startActivity(intent);
			}else if(item.getNoticeType() == BusinessConstants.NOTICE_TYPE_AUTHORIZE_MSG){//授权消息
				Intent intent = new Intent(mContext,MessageDetailAcitivtiy.class);
				intent.putExtra(IntentConstants.NOTICE_MESSAGE,(Serializable) item);
				mContext.startActivity(intent);
			}else if(item.getNoticeType() == BusinessConstants.NOTICE_TYPE_SYS_MSG){//系统消息
				Intent intent = new Intent(mContext,MessageDetailAcitivtiy.class);
				intent.putExtra(IntentConstants.NOTICE_MESSAGE,(Serializable) item);
				mContext.startActivity(intent);
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

//	@Override
//	public boolean onItemLongClick(AdapterView<?> parent, View view,
//			int position, long id) {
//
//		NoticeMessage item = (NoticeMessage) mItemList.get(position);
//		activity.deleteNotice(item.getNoticeId());
//		return true;
//	}
//
}