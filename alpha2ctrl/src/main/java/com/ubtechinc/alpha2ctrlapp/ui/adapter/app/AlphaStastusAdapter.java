package com.ubtechinc.alpha2ctrlapp.ui.adapter.app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.data.robot.RobotStatusInfo;
import com.ubtechinc.alpha2ctrlapp.ui.activity.app.AlphaStatusDetailActivity;
import com.ubtechinc.alpha2ctrlapp.ui.activity.app.CheckAlphaStatusActivity;

import java.io.Serializable;
import java.util.List;

/**
 * [CheckAlphaStatusActivity listView adapter]
 Created by nixiaoyan on 2016/7/6.
 **/

public class AlphaStastusAdapter extends BaseAdapter implements OnItemClickListener {

	private LayoutInflater inflater;
	private Context context;
	private List<RobotStatusInfo>  statusList;
	public int clickItem = -1;
	private CheckAlphaStatusActivity activity;
	public List<RobotStatusInfo> getmAlarmEntrityList() {
		return statusList;
	}

	public AlphaStastusAdapter(Context context, List<RobotStatusInfo>  statusList) {
		this.statusList = statusList;
		this.context = context;
		this.activity = (CheckAlphaStatusActivity)context;
		this.inflater = LayoutInflater.from(context);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return statusList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return statusList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		 final ViewHolder viewHolder ;
		if (convertView == null || convertView.getTag() == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.alpha_status_item, null, false);
			viewHolder.time_tv = (TextView) convertView.findViewById(R.id.time_lay);
			viewHolder.deleteImage =(CheckBox) convertView.findViewById(R.id.deleteImage);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.time_tv.setText(statusList.get(position).getTime());
		if(activity.isEdit){
			if(activity.isSelected.get(statusList.get(position).getFilePath())){
				viewHolder.deleteImage.setChecked(true);
			}else{
				viewHolder.deleteImage.setChecked(false);
			}
			viewHolder.deleteImage.setVisibility(View.VISIBLE);
		}else{
			viewHolder.deleteImage.setVisibility(View.GONE);
		}
		viewHolder.deleteImage.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				if(activity.isSelected.get(statusList.get(position).getFilePath())){
					viewHolder.deleteImage.setChecked(false);
					activity.isSelected.put(statusList.get(position).getFilePath(),false);
				}else{
					viewHolder.deleteImage.setChecked(true);
					activity.isSelected.put(statusList.get(position).getFilePath(),true);
				}
			}
		});
		return convertView;
	}

	public void onNotifyDataSetChanged(List<RobotStatusInfo>  mAlarmEntrity) {
		// TODO Auto-generated method stub
		clickItem = -1;
		this.statusList = mAlarmEntrity;
		this.notifyDataSetChanged();
	}

	class ViewHolder {
		TextView time_tv;
		CheckBox  deleteImage;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		clickItem = position;
		position--;
		if(activity.isEdit){
			if(activity.isSelected.get(statusList.get(position).getFilePath())){
				activity.isSelected.put(statusList.get(position).getFilePath(),false);
			}else{
				activity.isSelected.put(statusList.get(position).getFilePath(),true);
			}
			notifyDataSetChanged();
		}else{
			Intent intent = new Intent(context, AlphaStatusDetailActivity.class);
			intent.putExtra("info",(Serializable) statusList.get(position));
			context.startActivity(intent);
		}


	}

}
