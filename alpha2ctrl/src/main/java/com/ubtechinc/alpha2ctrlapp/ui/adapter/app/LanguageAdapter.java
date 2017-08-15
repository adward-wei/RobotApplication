package com.ubtechinc.alpha2ctrlapp.ui.adapter.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.ubtech.utilcode.utils.StringUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;

/**
 * [AlarmActivity listView adapter]
 * 
 * @author zengdengyi
 * @version 1.0
 * @date 2015年4月21日 上午10:13:22O
 * 
 **/

public class LanguageAdapter extends BaseAdapter implements OnItemClickListener {

	private LayoutInflater inflater;
	private Context context;
	private String[] mAlarmEntrityList;
	public int clickItem = -1;
	public String[] getmAlarmEntrityList() {
		return mAlarmEntrityList;
	}

	public LanguageAdapter(Context context, String[] mAlarmEntrityList) {
		this.mAlarmEntrityList = mAlarmEntrityList;
		this.context = context;
		this.inflater = LayoutInflater.from(context);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mAlarmEntrityList.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mAlarmEntrityList[arg0];
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
		if (viewHolder == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.language_item, null, false);
			viewHolder.time_tv = (TextView) convertView.findViewById(R.id.repeate);
			viewHolder.checkbox = (CheckBox) convertView.findViewById(R.id.repeat_check);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (clickItem == position) {
			viewHolder.checkbox.setChecked(true);
			viewHolder.checkbox.setVisibility(View.VISIBLE);
		} else {
			viewHolder.checkbox.setChecked(false);
			viewHolder.checkbox.setVisibility(View.INVISIBLE);

		}
		if(position==0){
			viewHolder.time_tv.setText(mAlarmEntrityList[position]);
		}else{
			if (StringUtils.isEquals(mAlarmEntrityList[position], BusinessConstants.APP_LANGUAGE_EN) ) {
				viewHolder.time_tv.setText("English");
			} else if (StringUtils.isEquals(mAlarmEntrityList[position], BusinessConstants.APP_LANGUAGE_CN) ) {

				viewHolder.time_tv.setText("简体中文");
			}else {
				viewHolder.time_tv.setText(mAlarmEntrityList[position]);
			}

		}
		
		return convertView;
	}

	public void onNotifyDataSetChanged(String[] mAlarmEntrity) {
		// TODO Auto-generated method stub
		clickItem = -1;
		this.mAlarmEntrityList = mAlarmEntrity;
		this.notifyDataSetChanged();
	}

	class ViewHolder {
		TextView time_tv;
		CheckBox checkbox;
		ToggleButton alarm_usable_tg;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		clickItem = position;
		notifyDataSetChanged();

	}

	public void onViewItemClick(int position) {
		// TODO Auto-generated method stub
		clickItem = position;
		notifyDataSetChanged();

	}

	public void setDefualt(int position) {
		clickItem = position;
		notifyDataSetChanged();
	}
}
