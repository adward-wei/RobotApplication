package com.ubtechinc.alpha2ctrlapp.ui.adapter.robot;

import android.content.Context;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha2ctrlapp.R;

import java.util.List;

/**
 * [wifi listview ]
 * 
 * @author zengdengyi
 * @version 1.0
 * @date 2014-9-27
 * 
 **/

public class Alpha2WifiAdapter extends BaseAdapter  {
	private Context context;
	private List<ScanResult> mWifiInfoList;
	private LayoutInflater inflater;
	public int clicktemp = -1;
	private String TAG = "Alpha2Adapter";
	private String alpha2Mac;
	private String wifiMac;
    private  int  wpaStr;
	public Alpha2WifiAdapter(Context context, List<ScanResult> mWifiInfoList, String wifiMac) {
		this.mWifiInfoList = mWifiInfoList;
		this.context = context;
		this.wifiMac = wifiMac;
		inflater = LayoutInflater.from(context);
	}

	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mWifiInfoList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mWifiInfoList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.setting_wifi_listview_item,null);
			viewHolder.wifi_name = (TextView) convertView.findViewById(R.id.wifi_name);
			viewHolder.wifi_signal_level = (ImageView) convertView.findViewById(R.id.wifi_signal_level);
			viewHolder.wifi_lock = (ImageView) convertView.findViewById(R.id.wifi_lock);
			convertView.setTag(viewHolder);
			
		} else {
			viewHolder =(ViewHolder) convertView.getTag();
		}
		String name = mWifiInfoList.get(position).SSID;//
		if(wifiMac!= null){
			if(wifiMac.equals(mWifiInfoList.get(position).BSSID)){
				viewHolder.wifi_name.setTextColor(Color.RED);
			}else{
				viewHolder.wifi_name.setTextColor(Color.GRAY);
			}
		}else{
			viewHolder.wifi_name.setTextColor(Color.GRAY);
		}
//		if(TextUtils.isEmpty()){
//			viewHolder.wifi_lock.setVisibility(View.GONE);
//		}else{
//			viewHolder.wifi_lock.setVisibility(View.VISIBLE);
//		}
		if(!mWifiInfoList.get(position).capabilities.contains("WPA")
				&&!mWifiInfoList.get(position).capabilities.contains("wpa")
				&&!mWifiInfoList.get(position).capabilities.contains("WEP")
				&&!mWifiInfoList.get(position).capabilities.contains("wep")
				&&!mWifiInfoList.get(position).capabilities.contains("EAP")
				&&!mWifiInfoList.get(position).capabilities.contains("EAP")){
			Logger.i("wifi", "capability"+mWifiInfoList.get(position).capabilities);
			viewHolder.wifi_lock.setVisibility(View.GONE);
		}else{
			viewHolder.wifi_lock.setVisibility(View.VISIBLE);
		}
//		    (1).WPA-PSK/WPA2-PSK(目前最安全家用加密)
//		    (2).WPA/WPA2(较不安全)
//		    (3).WEP(安全较差)
//		    (4).EAP(迄今最安全的)
		viewHolder.wifi_name.setText(name);
		viewHolder.wifi_signal_level.setImageResource(R.drawable.wifi_sel);
		viewHolder.wifi_signal_level.setImageLevel(Math.abs(mWifiInfoList.get(position).level));  
		return convertView;
	}

	public class ViewHolder {
		TextView wifi_name;
		ImageView wifi_signal_level;
		ImageView wifi_lock;

	}



//	public void onNotifyDataSetChanged(List<ScanResult> mWifiInfoList) {
//		// TODO Auto-generated method stub
//		this.mWifiInfoList = mWifiInfoList;
//		clicktemp = -1;
//		this.notifyDataSetChanged();
//	}
//
//	public void onClear() {
//		this.mWifiInfoList.clear();
//		clicktemp = -1;
//		this.notifyDataSetChanged();
//	}
//
//	public void setAlphaMacList(String alpha2Mac) {
//		// TODO Auto-generated method stub
//		this.alpha2Mac = alpha2Mac;
//	}
}
