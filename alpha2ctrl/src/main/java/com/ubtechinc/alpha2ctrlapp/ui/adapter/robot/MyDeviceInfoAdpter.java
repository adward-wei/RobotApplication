package com.ubtechinc.alpha2ctrlapp.ui.adapter.robot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.google.common.collect.Lists;
import com.ubtechinc.alpha2ctrlapp.base.BaseHandler;
import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.NewDeviceInfo;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.RobotAndAuthoriserModel;
import com.ubtechinc.alpha2ctrlapp.ui.activity.robot.MyDeviceActivity;
import com.ubtechinc.alpha2ctrlapp.util.ImageLoad.LoadImage;
import com.ubtechinc.alpha2ctrlapp.widget.RoundImageView;

import java.util.List;

/*************************
* @date 2016/7/4
* @author
* @Description Alpha设备列表适配器
* @modify
* @modify_time
**************************/
public class MyDeviceInfoAdpter extends BaseAdapter  {
	private Context context;
	private List<RobotAndAuthoriserModel> mDeviceInfoList = Lists.newArrayList();
	private LayoutInflater inflater;
	private static final String TAG = "MyDeviceInfoAdpter";
//	private MainPageActivity mMainPageActivity;
	public NewDeviceInfo deviceInfo;
	public boolean isSelcted;
	private MyDeviceActivity activity;
	private String to ;
	private BaseHandler mhandler;
	public MyDeviceInfoAdpter(Context context, List<RobotAndAuthoriserModel> mDeviceInfoList, BaseHandler mhandler) {
		this.mDeviceInfoList.addAll(mDeviceInfoList);
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.activity = (MyDeviceActivity)context;
		this.mhandler = mhandler;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDeviceInfoList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mDeviceInfoList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		//初始化一个ImageView对象
		RoundImageView ivLogo = new RoundImageView(activity);
		//设置缩放方式
		ivLogo.setScaleType(ImageView.ScaleType.FIT_XY);
		//设置ImageView的宽高
		if(this.activity.currentPosition ==-1){
			this.activity.currentPosition  =mDeviceInfoList.size()/2;
		}
		if(position ==this.activity.currentPosition){
			ivLogo.setLayoutParams(new Gallery.LayoutParams(Constants.deviceHeight/6, Constants.deviceHeight/6));//当前选中的头像放大
		}else
			ivLogo.setLayoutParams(new Gallery.LayoutParams(Constants.deviceHeight/8, Constants.deviceHeight/8));

		if(mDeviceInfoList.get(position).getRobotInfo().getConnectionState()== BusinessConstants.ROBOT_STATE_ONLINE || mDeviceInfoList.get(position).getRobotInfo().getConnectionState()== BusinessConstants.ROBOT_STATE_CONNECTED ){
			LoadImage.LoadHeader(activity, 5, ivLogo, mDeviceInfoList.get(position).getRobotInfo().getUserImage());

		}else {
			LoadImage.LoadGrayHeader(activity, ivLogo, mDeviceInfoList.get(position).getRobotInfo().getUserImage()); // 不在线的头像灰色
		}
		return ivLogo;
	}

	
	public void onNotifyDataSetChanged(List<RobotAndAuthoriserModel> InfoList) {
		// TODO Auto-generated method stub
		this.mDeviceInfoList.clear();
		mDeviceInfoList.addAll(InfoList);
		this.notifyDataSetChanged();
	}



}