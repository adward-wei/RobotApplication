package com.ubtechinc.alpha2ctrlapp.ui.adapter.base;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.ui.activity.user.PicScanActivity;
import com.ubtechinc.alpha2ctrlapp.util.ImageLoad.LoadImage;

public class ImageGalleryAdpter extends BaseAdapter  {
	private Context context;
	private String[] urls;
	private LayoutInflater inflater;

	public ImageGalleryAdpter(Context context, String[] url) {
		this.context = context;
		this.urls  =url;
		inflater = LayoutInflater.from(context);
	}

	public int getCount() { // 取得要显示内容的数量
		return urls.length;
	}

	public Object getItem(int position) { // 每个资源的位置
		return urls[position];
	}

	public long getItemId(int position) { // 取得每个项的ID
		return position;
	}

	class ViewHolder {
		
		ImageView Photo;
		CheckBox chooseIcon ;
		boolean isEdit  =false;
	}
	// 将资源设置到一个组件之中，很明显这个组件是ImageView
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null || convertView.getTag() == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.photo_item, null,false);
			viewHolder.Photo = (ImageView)convertView.findViewById(R.id.photo);
			viewHolder.chooseIcon  = (CheckBox)convertView.findViewById(R.id.choice_icon);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.chooseIcon.setVisibility(View.GONE);
		LoadImage.LoadPicture(context, viewHolder.Photo, urls[position],6);
		viewHolder.Photo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, PicScanActivity.class);
				// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
				intent.putExtra("urls", urls);
				intent.putExtra("position", position);
				context.startActivity(intent);
			}
		});
		return convertView;
	}
}
