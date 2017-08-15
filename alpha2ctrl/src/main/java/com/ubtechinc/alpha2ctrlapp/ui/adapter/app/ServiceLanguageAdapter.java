package com.ubtechinc.alpha2ctrlapp.ui.adapter.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.entity.ServiceLanguage;

import java.util.List;

/*************************
* @date 2016/7/4
* @author 唐宏宇
* @Description 蓝牙设备连接适配器
* @modify
* @modify_time
**************************/
public class ServiceLanguageAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private Context mContext;
	private List<ServiceLanguage> mItemList;

	public ServiceLanguageAdapter(Context context, List<ServiceLanguage>chatList) {
		mContext = context;

		mItemList = chatList;

		this.inflater = LayoutInflater.from(context);

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
			convertView = inflater.inflate(R.layout.item_service_language, null, false);
			viewHolder.tv_language = (TextView) convertView
					.findViewById(R.id.tv_language);
			viewHolder.btn_connect = (ImageView) convertView
					.findViewById(R.id.iv_check);


		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final ServiceLanguage device = mItemList.get(position);

		if(device.isChecked()) {
			viewHolder.btn_connect.setVisibility(View.VISIBLE);
		}else {

			viewHolder.btn_connect.setVisibility(View.GONE);
		}
		viewHolder.tv_language.setText(device.getLanguageShow());


		return convertView;
	}

	class ViewHolder {
		TextView tv_language;
		ImageView btn_connect;

	}




}