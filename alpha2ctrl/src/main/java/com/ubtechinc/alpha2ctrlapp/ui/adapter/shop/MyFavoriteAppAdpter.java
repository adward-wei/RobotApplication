package com.ubtechinc.alpha2ctrlapp.ui.adapter.shop;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.NewDeviceInfo;
import com.ubtechinc.alpha2ctrlapp.entity.business.user.MyFavoriteInfo;
import com.ubtechinc.alpha2ctrlapp.ui.activity.main.MainPageActivity;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.shop.AppDetailFragment;
import com.ubtechinc.alpha2ctrlapp.util.ImageLoad.LoadImage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyFavoriteAppAdpter  extends BaseAdapter implements OnItemClickListener {
	private Context context;
	private List<MyFavoriteInfo> appInfoList;
	private LayoutInflater inflater;
	public int clicktemp = -1;
	private String TAG = "Alpha2Adapter";
	public static Map<Integer, Boolean> isSelected = new HashMap<Integer, Boolean>();
	public NewDeviceInfo deviceInfo;
	private MainPageActivity activity;
	public boolean isSelcted;
	public MyFavoriteAppAdpter(Context context, List<MyFavoriteInfo> actionInfoList) {
		this.appInfoList = actionInfoList;
		this.context = context;
		this.activity = (MainPageActivity)context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return appInfoList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return appInfoList.get(arg0);
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
			convertView = inflater.inflate(R.layout.my_favorite_app_item, null,false);
			viewHolder.appImage = (ImageView)convertView.findViewById(R.id.app_image);
			viewHolder.apptype  = (TextView)convertView.findViewById(R.id.app_type);
			viewHolder.appName =(TextView)convertView.findViewById(R.id.app_name);
			viewHolder.appDsc = (TextView)convertView.findViewById(R.id.app_dsc);
			 viewHolder.btn_update = (ImageView)convertView.findViewById(R.id.btn_update);
		
			// isSelected.put(position, viewHolder.checkBox.isChecked());
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
			 
			viewHolder.appName.setText(appInfoList.get(position).getCollectName());
			viewHolder.apptype.setText(appInfoList.get(position).getCollectObjectType());
			if(!TextUtils.isEmpty(appInfoList.get(position).getCollectDescriber()))
				viewHolder.appDsc.setText(appInfoList.get(position).getCollectDescriber());
			else{
				viewHolder.appDsc.setText(context.getText(R.string.shop_page_no_description));
			}
//			viewHolder.apptype.setText(appInfoList.get(position).getCollectObjectType());
			LoadImage.setRounderConner(activity, viewHolder.appImage, appInfoList.get(position).getCollectImage(),1);
		return convertView;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long arg3) {
		ViewHolder viewHolder = (ViewHolder) view.getTag();
//		AppInfo mDeviceInfo = appInfoList.get(position);
		
		// TODO Auto-generated method stub
		Logger.i( "dianji ");
		Bundle  bundle = new Bundle();
		bundle.putInt("appId", appInfoList.get(position).getCollectRelationId());
		activity.currentFragment.replaceFragment(AppDetailFragment.class.getName(), bundle);
		
		
	}
	public class ViewHolder {
		TextView appName;
		TextView apptype;
		ImageView appImage;
		TextView appDsc;
		ImageView btn_update;
	}

    public void setConnectedDevice(NewDeviceInfo deviceInfo){
    	this.deviceInfo = deviceInfo;
    }
    public NewDeviceInfo  getConnectedDevice (){
    	return this.deviceInfo;
    }
	
	
	public void onNotifyDataSetChanged(List<MyFavoriteInfo> infoList) {
		// TODO Auto-generated method stub
		clicktemp = -1;
		appInfoList = infoList;
		
		this.notifyDataSetChanged();
	}

	public void onClear() {
		appInfoList.clear();
		clicktemp = -1;
		this.notifyDataSetChanged();
	}
}
