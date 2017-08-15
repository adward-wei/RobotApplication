package com.ubtechinc.alpha2ctrlapp.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ubtechinc.alpha2ctrlapp.R;

public class DetailImageView extends LinearLayout {
	
	private Context mContext;
	private ScanImageView imageView;
	private TextView  proTv;
	private ProgressBar proBar;
	private RelativeLayout progressLay;
	public DetailImageView(Context context) {
		super(context);
		this.mContext = context;
		init();
	}
	
	public DetailImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init();
		
	}
	private void init(){
		LayoutInflater.from(mContext).inflate(R.layout.scan_pic_detail, this, true);
		imageView = (ScanImageView) findViewById(R.id.detail_image);
		
		proTv = (TextView) findViewById(R.id.progress_tv);
		proBar = (ProgressBar) findViewById(R.id.bar);
		progressLay = (RelativeLayout)findViewById(R.id.progressLay);
	}

	public Context getmContext() {
		return mContext;
	}
	public void setmContext(Context mContext) {
		this.mContext = mContext;
	}
	public ScanImageView getImageView() {
		return imageView;
	}
	public void setImageView(ScanImageView imageView) {
		this.imageView = imageView;
	}
	public TextView getProTv() {
		return proTv;
	}
	public void setProTv(TextView proTv) {
		this.proTv = proTv;
	}
	public ProgressBar getProBar() {
		return proBar;
	}
	public void setProBar(ProgressBar proBar) {
		this.proBar = proBar;
	}
	public RelativeLayout getProgressLay() {
		return progressLay;
	}
	public void setProgressLay(RelativeLayout progressLay) {
		this.progressLay = progressLay;
	}
	
}
