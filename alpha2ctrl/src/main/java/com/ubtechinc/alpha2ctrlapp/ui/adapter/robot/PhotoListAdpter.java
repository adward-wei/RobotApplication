package com.ubtechinc.alpha2ctrlapp.ui.adapter.robot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.entity.HttpPhotoList;
import com.ubtechinc.alpha2ctrlapp.widget.PhotoGridView;

import java.util.ArrayList;
import java.util.List;

public class PhotoListAdpter extends BaseAdapter  {

	private Context mContext;
	private LayoutInflater inflater;
	private List<HttpPhotoList> imageList  = new ArrayList<HttpPhotoList>();
	public PhotoListAdpter(Context context, List<HttpPhotoList>  list, boolean isInitMapData) {
		this.imageList = list;
		this.mContext  = context;
		inflater = LayoutInflater.from(context);

 }

	@Override // 详细内容
	public View getView(final int position, View convertView, ViewGroup arg2) {
		final ViewHolder viewHolder;
		if (convertView == null || convertView.getTag() == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.photo_listview_item, null,false);
			viewHolder.type = (TextView)convertView.findViewById(R.id.dataType);
			viewHolder.gridView  = (PhotoGridView)convertView.findViewById(R.id.photo_grid);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.type.setText(imageList.get(position).getDateType());
         PhotoGridAdpter  adperAdpter = new PhotoGridAdpter(mContext, imageList.get(position).getImageDetailList(),position);
         viewHolder.gridView.setAdapter(adperAdpter);
         adperAdpter.notifyDataSetChanged();
		return convertView;
	}

	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return imageList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return imageList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}	

	public void notyfiDataChanged(List<HttpPhotoList> list ){
		this.imageList  = list;
		this.notifyDataSetChanged();
	}
	 
	class ViewHolder {
		
		TextView type;
		PhotoGridView gridView ;
	}
}
