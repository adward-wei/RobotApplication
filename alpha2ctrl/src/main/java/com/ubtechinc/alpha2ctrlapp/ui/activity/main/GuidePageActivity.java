package com.ubtechinc.alpha2ctrlapp.ui.activity.main;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ubtech.utilcode.utils.SPUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.ActivitySupport;
import com.ubtechinc.alpha2ctrlapp.constants.PreferenceConstants;
import com.ubtechinc.alpha2ctrlapp.widget.GuiderPager;

public class GuidePageActivity extends ActivitySupport {

	private ImageView[] img_logo_index;
	private GuiderPager viewPage;
	private Drawable[] drwableList;
	private TextView guideTip1,guideTip2;
	private RelativeLayout goLay;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_guider_page);

		img_logo_index = new ImageView[] {
				(ImageView) findViewById(R.id.img_1),
				(ImageView) findViewById(R.id.img_2),
				(ImageView) findViewById(R.id.img_3),
				(ImageView)	findViewById(R.id.img_4) };
		drwableList	 = new Drawable[] {
				getResources().getDrawable(R.drawable.guide_page1),
				getResources().getDrawable(R.drawable.guide_page2),
				getResources().getDrawable(R.drawable.guide_page3),
				getResources().getDrawable(R.drawable.guide_page4)};
		guideTip1 = (TextView)findViewById(R.id.guide_tip1);
		guideTip2 = (TextView)findViewById(R.id.guide_tip2);
		goLay = (RelativeLayout)findViewById(R.id.go_lay);
		viewPage = (GuiderPager)findViewById(R.id.guide_pager);
		viewPage.setImg_logo_index(img_logo_index);
		viewPage.setDrwableList(drwableList);
		viewPage.initView(goLay,guideTip1,guideTip2);
		goLay.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(GuidePageActivity.this, MainActivity.class);
				GuidePageActivity.this.startActivity(intent);
				GuidePageActivity.this.finish();
				SPUtils.get().put(PreferenceConstants.APP_RUN_TIMES, 1);
			}
		});
	}
	 
	
	 @Override
		public void onResume(){
			super.onResume();
			
			
		}
	 @Override
		public void onDestroy(){
			super.onDestroy();
			
		}
		
}
