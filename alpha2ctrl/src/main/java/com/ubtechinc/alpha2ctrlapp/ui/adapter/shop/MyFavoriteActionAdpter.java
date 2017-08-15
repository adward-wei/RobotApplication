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
import com.ubtechinc.alpha2ctrlapp.ui.activity.app.ActionsLibPreViewActivity;
import com.ubtechinc.alpha2ctrlapp.ui.activity.main.MainPageActivity;
import com.ubtechinc.alpha2ctrlapp.util.ImageLoad.LoadImage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyFavoriteActionAdpter  extends BaseAdapter implements OnItemClickListener {
	private Context context;
	private List<MyFavoriteInfo> appInfoList;
	private LayoutInflater inflater;
	public int clicktemp = -1;
	private String TAG = "Alpha2Adapter";
	public static Map<Integer, Boolean> isSelected = new HashMap<Integer, Boolean>();
	public NewDeviceInfo deviceInfo;
	private MainPageActivity activity;
	public boolean isSelcted;
	public MyFavoriteActionAdpter(Context context, List<MyFavoriteInfo> actionInfoList) {
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
			convertView = inflater.inflate(R.layout.layout_action_item, null,
					false);
			viewHolder.appImage = (ImageView)convertView.findViewById(R.id.action_logo);
			viewHolder.appName  = (TextView)convertView.findViewById(R.id.txt_action_name);
			viewHolder.disc =(TextView)convertView.findViewById(R.id.txt_disc);
			 viewHolder.btn_update = (ImageView)convertView.findViewById(R.id.img_state);
			 viewHolder.txt_time =  (TextView)convertView.findViewById(R.id.txt_time);
			// isSelected.put(position, viewHolder.checkBox.isChecked());
			 viewHolder.img_type_logo = (ImageView)convertView.findViewById(R.id.img_type_logo);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if(appInfoList.get(position).getCollectObjectType()!=null){
			if (appInfoList.get(position).getCollectObjectType().equals("1")) 
				 viewHolder.img_type_logo.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_small_base));
			else if (appInfoList.get(position).getCollectObjectType().equals("2"))
				 	viewHolder.img_type_logo.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_small_dance));
			 else if (appInfoList.get(position).getCollectObjectType().equals("3"))
				 	viewHolder.img_type_logo.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_small_story));
				
		}
		

			viewHolder.appName.setText(appInfoList.get(position).getCollectName());
			if(!TextUtils.isEmpty(appInfoList.get(position).getCollectDescriber()))
				viewHolder.disc.setText(appInfoList.get(position).getCollectDescriber());
			else{
				viewHolder.disc.setText(context.getText(R.string.shop_page_no_description));
			}
//			viewHolder.disc.setText(appInfoList.get(position).get);
			viewHolder.txt_time.setText(appInfoList.get(position).getCollectDate());
				LoadImage.LoadPicture(context, viewHolder.appImage, appInfoList.get(position).getCollectImage(),2);
		viewHolder.btn_update.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bundle  bundle = new Bundle();
				bundle.putInt("actionid", appInfoList.get(position).getCollectRelationId());
				activity.currentFragment.replaceFragment(ActionsLibPreViewActivity.class.getName(), bundle);
				
			}
		});
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
		bundle.putInt("actionid", appInfoList.get(position).getCollectRelationId());
		activity.currentFragment.replaceFragment(ActionsLibPreViewActivity.class.getName(), bundle);
		
		
	}
	public class ViewHolder {
		TextView appName;
		TextView disc;
		ImageView appImage;
		ImageView btn_update;
		TextView txt_time;
		ImageView img_type_logo;
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
