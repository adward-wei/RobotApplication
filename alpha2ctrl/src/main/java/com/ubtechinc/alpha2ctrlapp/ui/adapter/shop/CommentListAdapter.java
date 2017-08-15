package com.ubtechinc.alpha2ctrlapp.ui.adapter.shop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ubtech.utilcode.utils.TimeUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.NewDeviceInfo;
import com.ubtechinc.alpha2ctrlapp.entity.business.shop.CommentInfo;
import com.ubtechinc.alpha2ctrlapp.ui.activity.main.MainPageActivity;
import com.ubtechinc.alpha2ctrlapp.util.ImageLoad.LoadImage;
import com.ubtechinc.alpha2ctrlapp.widget.RoundImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentListAdapter extends BaseAdapter {
	private Context context;
	private List<CommentInfo> commentInfoList;
	private LayoutInflater inflater;
	public int clicktemp = -1;
	private String TAG = "Alpha2Adapter";
	private MainPageActivity activity;
	public static Map<Integer, Boolean> isSelected = new HashMap<Integer, Boolean>();
	public NewDeviceInfo deviceInfo;
	public boolean isSelcted;

	public CommentListAdapter(Context context, List<CommentInfo> commentInfoList) {
		this.commentInfoList = commentInfoList;
		this.context = context;
		this.activity = (MainPageActivity) context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return commentInfoList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return commentInfoList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if (viewHolder == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.layout_commment_item, null, false);
			viewHolder.userImage = (RoundImageView) convertView
					.findViewById(R.id.img_user_head);
			viewHolder.userName = (TextView) convertView
					.findViewById(R.id.txt_user_name);
			viewHolder.content = (TextView) convertView
					.findViewById(R.id.txt_comment);
			viewHolder.time = (TextView) convertView
					.findViewById(R.id.txt_time);
			viewHolder.txt_floor = (TextView) convertView
					.findViewById(R.id.txt_floor);
			// isSelected.put(position, viewHolder.checkBox.isChecked());
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		CommentInfo commentInfo = commentInfoList.get(position);
		try {
			long[] time = TimeUtils.countTime(TimeUtils.getCurrentTimeInString(TimeUtils.DEFAULT_DATE_FORMAT),commentInfo .getCommentTime());
			if(time[0]>0||time[1]>0||time[2]>10){
				viewHolder.time.setText(commentInfo.getCommentTime());
			}else if(time[2]>0){
				viewHolder.time.setText(context.getString(R.string.comment_inssued)+time[2]+context.getString(R.string.comment_day)+context.getString(R.string.comment_before));
			}else {
				viewHolder.time.setText(context.getString(R.string.comment_inssued)+commentInfo.getCommentTime().split(" ")[1]);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}

		viewHolder.userName.setText(commentInfo.getUserName());
		viewHolder.content.setText(commentInfo.getCommentContext());
		viewHolder.txt_floor.setText(commentInfoList.size()-position+context.getString(R.string.comment_floor));
		LoadImage.setRounderConner(activity, viewHolder.userImage, commentInfo.getUserImage(),0);
		return convertView;
	}


	public class ViewHolder {
		TextView userName;
		TextView content;
		TextView txt_floor;
		RoundImageView userImage;
		TextView time;
	}


	public void setConnectedDevice(NewDeviceInfo deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	public NewDeviceInfo getConnectedDevice() {
		return this.deviceInfo;
	}

	public void onNotifyDataSetChanged(List<CommentInfo> infoList) {
		// TODO Auto-generated method stub
		clicktemp = -1;
		commentInfoList = infoList;

		this.notifyDataSetChanged();
	}

	public void onClear() {
		commentInfoList.clear();
		clicktemp = -1;
		this.notifyDataSetChanged();
	}
}
