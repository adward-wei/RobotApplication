package com.ubtechinc.alpha2ctrlapp.ui.adapter.robot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;

import com.ubtechinc.alpha2ctrlapp.base.BaseHandler;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.DownLoadStatus;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.ImageModel;
import com.ubtechinc.alpha2ctrlapp.util.ImageLoad.LoadImage;
import com.ubtechinc.alpha2ctrlapp.widget.DetailImageView;
import com.ubtechinc.alpha2ctrlapp.widget.ScanImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScanPicAdpeter extends BaseAdapter  implements OnItemSelectedListener {
	private Context context;
	private ArrayList<ScanImageView> imageViews = new ArrayList<ScanImageView>();
	private List<ImageModel> mItems;
	private LayoutInflater inflater;
	public int currentPosition;
	private BaseHandler mHandler;
	private Map<String , DownLoadStatus> downLoadMap  = new HashMap<String, DownLoadStatus>();
	public void setData(List<ImageModel> data,Map<String,DownLoadStatus >downloadMap) {
		this.mItems = data;
		this.downLoadMap = downloadMap;
		notifyDataSetChanged();
	}
	public ScanPicAdpeter(Context context, List<ImageModel>picList, Map<String,DownLoadStatus> downLoadMap, BaseHandler mHandler) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.mItems = picList;
		this.mHandler = mHandler;
		this.downLoadMap = downLoadMap;
	}

	@Override
	public int getCount() {
		return mItems != null ? mItems.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}



	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		DetailImageView detailView = new DetailImageView(context);
//		Bitmap bmp = ImageLoader.getInstance(context).DisplayRobotImage(mItems.get(position).replace("_thumb", ""));
//		if(bmp!=null){
//			detailView.getImageView().setImageBitmap(bmp);
//			detailView.getProgressLay().setVisibility(View.GONE);
//		}else{
//			Bitmap tbmp =BitmapUtils.getBitmap(Constants.PIC_RECEIVE_THUMB_PATH+mItems.get(position),70,70);
//			LoadImage.LoadPicture(context, detailView.getImageView(), mItems.get(position).getImage_original_url(), 4);
			LoadImage.LoadAlumPicture(context, detailView, mItems.get(position).getImage_original_url(), 8);
//			if(tbmp!=null){
//				detailView.getImageView().setImageBitmap(tbmp);
//				detailView.getProgressLay().setVisibility(View.GONE);
//			}
//            DownLoadStatus status = downLoadMap.get(mItems.get(position).replace("_thumb", ""));
//            if(status!=null){
//            	switch (status.getDownloadStaStatus()) {
//				case 0:
//					mHandler.obtainMessage(ImageDetailActivity.TO_GET_PHOTO, mItems.get(position)).sendToTarget();
//					status.setDownloadStaStatus(1);
//					downLoadMap.put(mItems.get(position).replace("_thumb", ""), status);
//					notifyDataSetChanged();
//					break;
//				case 1:
//					detailView.getProgressLay().setVisibility(View.VISIBLE);
//					detailView.getProTv().setText(status.getDownloadProgress());
//					Logger.i("progress", "ppp "+status.getDownloadProgress());
//					break;
//				case 2:
//					String path = Constants.PIC_RECEIVE_PATH+mItems.get(position).replace("_thumb", ""); 
//					bmp =BitmapUtils.getBitmap(path,Constants.appDetailImageHeight,Constants.appImageWidth);
//					if(bmp!=null){
//						detailView.getImageView().setImageBitmap(bmp);
//						detailView.getProgressLay().setVisibility(View.GONE);
//						
//					}else{
//						DataCleanManager.deleteGeneralFile(path);
//						detailView.getImageView().setImageDrawable(context.getResources().getDrawable(R.drawable.no_photo));
////						NToast.shortToast(context, R.string.load_photo_failed);
//						detailView.getProgressLay().setVisibility(View.GONE);
//						detailView.getProTv().setText("0.0%");
//						status.setDownloadStaStatus(0);
//						downLoadMap.put(mItems.get(position).replace("_thumb", ""), status);
//						notifyDataSetChanged();
//					}
//					break;
//				case 3:
//					String tips = context.getString(R.string.image_get_photo_failed).replace("%", mItems.get(position).replace("_thumb", ""));
//					ToastUtils.showLongToast(context, tips);
//					detailView.getProgressLay().setVisibility(View.GONE);
//					detailView.getProTv().setText("0.0%");
//					break;
//				case 4:
//					NToast.shortToast(context, R.string.image_photo_no_exist);
//					break;
//
//				default:
//					break;
//				}
//            }
			
//		}

		return detailView;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		currentPosition = position;
		ScanPicAdpeter.this.notifyDataSetChanged();
	}
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}

}
