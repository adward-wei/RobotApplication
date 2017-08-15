package com.ubtechinc.alpha2ctrlapp.ui.adapter.robot;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;

import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.constants.NetWorkConstant;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.ImageModel;
import com.ubtechinc.alpha2ctrlapp.ui.activity.robot.ImageDetailActivity;
import com.ubtechinc.alpha2ctrlapp.ui.activity.robot.ImageGallyActivity;
import com.ubtechinc.alpha2ctrlapp.util.ImageLoad.LoadImage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PhotoGridAdpter extends BaseAdapter  {
	private List<ImageModel>  imageList  = new ArrayList<ImageModel>();
	private Context mContext;
	private LayoutInflater inflater;
    private int typePosition;
    private ImageGallyActivity activity;
	public PhotoGridAdpter(Context context, List<ImageModel> list, int position) {
		this.imageList = list;
		this.mContext  = context;
		 activity = (ImageGallyActivity)context;
		inflater = LayoutInflater.from(context);
		this.typePosition = position;
	}

	@Override // 详细内容
	public View getView(final int position, View convertView, ViewGroup arg2) {
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
		if(ImageGallyActivity.isEdit){
			viewHolder.chooseIcon.setVisibility(View.VISIBLE);
			viewHolder.chooseIcon.setChecked(activity.getCheck(imageList.get(position).getImage_id()));
		}else{
			viewHolder.chooseIcon.setVisibility(View.GONE);
		}
//		Bitmap bmp = BitmapUtils.getBitmap(Constants.PIC_RECEIVE_THUMB_PATH+imageList.get(position).getImageName(),70,70);
		LoadImage.LoadPicture(mContext, viewHolder.Photo, imageList.get(position).getImage_thumbnail_url(), 7);
//		viewHolder.Photo.setImageBitmap(bmp);
		
		viewHolder.Photo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(ImageGallyActivity.isEdit){
					viewHolder.chooseIcon.toggle();
				}else{
					Intent intent  = new Intent(mContext,ImageDetailActivity.class);
					intent.putExtra("picList", (Serializable)imageList);
					intent.putExtra("pisition", typePosition);
					intent.putExtra("imageName", imageList.get(position).getImage_id());
					activity.startActivityForResult(intent, NetWorkConstant.REFRESH_PHOTO);
				}
			}
		});
		viewHolder.chooseIcon.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(ImageGallyActivity.isEdit){
					activity.setSelect(imageList.get(position).getImage_id(), viewHolder.chooseIcon.isChecked());
				}
			}
		});
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


	
   
	public void notyfiDataChanged(List<ImageModel>  list){
		this.imageList  = list;
		this.notifyDataSetChanged();
	}

	class ViewHolder {
		
		ImageView Photo;
		CheckBox chooseIcon ;
		boolean isEdit  =false;
	}
}
