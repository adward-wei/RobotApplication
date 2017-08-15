package com.ubtechinc.alpha2ctrlapp.ui.adapter;

import android.content.Context;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.ButtonConfigItem;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.TransferAppData;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.base.BaseContactFragememt;

import java.util.List;

public class MenuAdpter extends BaseAdapter  {

	private static final String TAG = "MenuAdpter";
	private Context context;
	private List<ButtonConfigItem> fileList;
	private LayoutInflater inflater;
	public int clicktemp = -1;
	private long mlLastClickTime = SystemClock.uptimeMillis();
	/** 动作名称 **/
	private String mPlayActionFileName = "";
	private BaseContactFragememt mfragement;
	private boolean isEdit = false;
	public String packgeName;

	public String getmPlayActionFileName() {
		return mPlayActionFileName;
	}

	public void setmPlayActionFileName(String mPlayActionFileName) {
		this.mPlayActionFileName = mPlayActionFileName;
	}

	public MenuAdpter(Context context, List<ButtonConfigItem> fileList,
                      BaseContactFragememt fragement) {
		this.fileList = fileList;
		this.context = context;
		this.mfragement = fragement;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fileList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return fileList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	public void onNotifyDataSetChanged(List<ButtonConfigItem> fileList,BaseContactFragememt frament) {
		// TODO Auto-generated method stub
		this.fileList = fileList;
		this.mfragement = frament;
		clicktemp = -1;
		this.notifyDataSetChanged();
	}

	public void onClear() {
		this.fileList.clear();
		clicktemp = -1;
		this.notifyDataSetChanged();
	}


	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if (viewHolder == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.menu_item, null, false);
			
			viewHolder.btn_menu_item = (TextView) convertView
					.findViewById(R.id.btn_menu_item);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
			
			if( (clicktemp==-1&& position==0)||clicktemp==position){
				viewHolder.btn_menu_item.setTextColor(context.getResources().getColor(R.color.text_color_t5));
				viewHolder.btn_menu_item.setBackgroundResource(R.drawable.menu_item_selecte_bg);
				
			}else{	
				viewHolder.btn_menu_item.setTextColor(context.getResources().getColor(R.color.text_color_t4));
				viewHolder.btn_menu_item.setBackgroundResource(R.drawable.menu_item_unselect_bg);
				
			}
			Logger.i( "wenzi "+position +fileList.get(position).getText());
		viewHolder.btn_menu_item.setText(fileList.get(position).getText());
		
		viewHolder.btn_menu_item.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				clicktemp = position;
				notifyDataSetChanged();
				TransferAppData entrity = new TransferAppData();
				entrity.setDatas(fileList.get(position).getIndex().getBytes());
				entrity.setPackageName(packgeName);
				//发送消息到Alpha，配置相关信息

			}
		});
			
		return convertView;
	}


	class ViewHolder {
		
		TextView btn_menu_item;
	}

    public String getPackgetName(){
    	return  packgeName;
    }

}