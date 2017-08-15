package com.ubtechinc.alpha2ctrlapp.ui.adapter.robot;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.ubtech.utilcode.utils.StringUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.CallbackListener;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.BluetoothDevice;

import java.util.List;

/*************************
* @date 2016/7/4
* @author 唐宏宇
* @Description 蓝牙设备连接适配器
* @modify
* @modify_time
**************************/
public class BluetoothDeviceAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private Context mContext;
	private List<BluetoothDevice> mItemList;
	CallbackListener<Bundle> listener;
	public boolean isCanConnect = true;
	public BluetoothDeviceAdapter(Context context, List<BluetoothDevice>chatList, CallbackListener<Bundle> listener) {
		mContext = context;
		this.listener = listener;
		mItemList = chatList;

		this.inflater = LayoutInflater.from(context);

	}

	public boolean isCanConnect() {
		return isCanConnect;
	}

	public void setCanConnect(boolean canConnect) {
		isCanConnect = canConnect;
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
		final int posi = position;
		ViewHolder viewHolder = null;
		if (viewHolder == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_bluetooth_device, null, false);
			viewHolder.device_name_tv = (TextView) convertView
					.findViewById(R.id.device_name_tv);
			viewHolder.btn_connect = (Button) convertView
					.findViewById(R.id.btn_connect);


		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final BluetoothDevice device = mItemList.get(position);
		if(isCanConnect) {//正在连接的时候不能连接

			viewHolder.btn_connect.setEnabled(true);
			viewHolder.btn_connect.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Bundle bundle = new Bundle();
					bundle.putSerializable("device",device );
					bundle.putInt("position", posi);
					listener.callback(bundle);
				}
			});
		}else {
			viewHolder.btn_connect.setEnabled(false);
		}
		if(device.isConnected()) {
			viewHolder.btn_connect.setVisibility(View.GONE);
		}else {

			viewHolder.btn_connect.setVisibility(View.VISIBLE);
		}
		viewHolder.device_name_tv.setText(StringUtils.isEmpty(device.getDeviceName())? device.getDeviceAddress() : device.getDeviceName());


		return convertView;
	}

	class ViewHolder {
		TextView device_name_tv;
		Button btn_connect;

	}




}