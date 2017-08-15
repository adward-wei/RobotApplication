package com.ubtechinc.alpha2ctrlapp.ui.adapter.robot;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.constants.PreferenceConstants;
import com.ubtechinc.alpha2ctrlapp.entity.SearchRelationInfo;
import com.ubtechinc.alpha2ctrlapp.ui.activity.robot.MyDeviceActivity;
import com.ubtechinc.alpha2ctrlapp.ui.activity.user.PersonInfoActivity;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.robot.SearchAuthorizeActivity;
import com.ubtechinc.alpha2ctrlapp.util.ImageLoad.LoadImage;
import com.ubtechinc.alpha2ctrlapp.widget.RoundImageView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthorizeUserAdpeter extends BaseAdapter implements OnItemClickListener {
	private Context context;
	private List<SearchRelationInfo> infoList;
	private LayoutInflater inflater;
	public int clicktemp = -1;
	private String TAG = "AuthorizeUserAdpeter";
	private MyDeviceActivity activity;
	public static Map<Integer, Boolean> isSelected = new HashMap<Integer, Boolean>();
	public boolean isSelcted;
	public AuthorizeUserAdpeter(Context context, List<SearchRelationInfo> mInfoList) {
		this.infoList = mInfoList;
		this.context = context;
		this.activity = (MyDeviceActivity)context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return infoList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return infoList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		final ViewHolder viewHolder ;
		if (convertView == null || convertView.getTag() == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.authorize_user_item, null,false);
			viewHolder.header = (RoundImageView) convertView.findViewById(R.id.user_header);
			viewHolder.username = (TextView) convertView.findViewById(R.id.user_name);
			viewHolder.userStastus = (TextView)convertView.findViewById(R.id.user_status);
			viewHolder.usingImage = (ImageView)convertView.findViewById(R.id.image_using);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}		
		if(position == 0){
			viewHolder.header.setImageResource(R.drawable.add_authori);
			viewHolder.username.setText(R.string.authorize_add);
			viewHolder.username.setTextColor(0xFF9c9c9c);
			viewHolder.usingImage.setVisibility(View.GONE);
			viewHolder.userStastus.setVisibility(View.GONE);
		}else{
			if(!TextUtils.isEmpty(infoList.get(position).getUserName())){
				viewHolder.username.setText(infoList.get(position).getUserName());
			}else if(!TextUtils.isEmpty(infoList.get(position).getUserEmail())){
				viewHolder.username.setText(infoList.get(position).getUserEmail());
			}else {
				viewHolder.username.setText(infoList.get(position).getUserPhone());
			}
			
			if(!TextUtils.isEmpty(infoList.get(position).getUserId())){
				LoadImage.LoadHeader(activity,0, viewHolder.header,infoList.get(position).getUserImage());
			}else{
				viewHolder.header.setImageDrawable(context.getResources().getDrawable(R.drawable.no_head));
			}
			if(Integer.valueOf(infoList.get(position).getUserId())==activity.currentRobot.getRobotInfo().getControlUserId()){
				viewHolder.username.setTextColor(0xFF2E86EF);
				viewHolder.usingImage.setVisibility(View.VISIBLE);
				viewHolder.userStastus.setText("("+context.getString(R.string.devices_using_status)+")");
				viewHolder.userStastus.setVisibility(View.VISIBLE);
				viewHolder.userStastus.setTextColor(0xFF2E86EF);
			}else{
				viewHolder.usingImage.setVisibility(View.GONE);
				viewHolder.username.setTextColor(0xFF9c9c9c);
				if(infoList.get(position).getRelationStatus()==0){
					viewHolder.userStastus.setText("("+context.getString(R.string.devices_authorize_not_pass)+")");
					viewHolder.userStastus.setVisibility(View.VISIBLE);
					viewHolder.userStastus.setTextColor(0xFF9c9c9c);
				}else{
					viewHolder.userStastus.setVisibility(View.GONE);
				}
			}
		}
		
			return convertView;
	}
	
	public class ViewHolder {
		RoundImageView header;
		TextView  username;
		TextView  userStastus;
		ImageView usingImage;
		
	}



	public void onNotifyDataSetChanged(List<SearchRelationInfo> list) {
		infoList = list;
		clicktemp = -1;
		this.notifyDataSetChanged();
	}

	public void onClear() {
		infoList.clear();
		clicktemp = -1;
		this.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if(position==0){
			doAdd();
		}else{
			Intent intent  = new Intent(activity,PersonInfoActivity.class);
			intent.putExtra(PreferenceConstants.ROBOT_SERIAL_NO,activity.currentRobot.getRobotInfo().getEquipmentId());
			intent.putExtra("userId",infoList.get(position).getUserId());
			intent.putExtra("userInfo", (Serializable)(infoList.get(position)));
			activity.startActivity(intent);
		}
	}
	private void doAdd() {
		Intent intent = new Intent(activity, SearchAuthorizeActivity.class);
		intent.putExtra(PreferenceConstants.ROBOT_SERIAL_NO, activity.currentRobot.getRobotInfo().getEquipmentId());
		activity.startActivity(intent);
	}
}