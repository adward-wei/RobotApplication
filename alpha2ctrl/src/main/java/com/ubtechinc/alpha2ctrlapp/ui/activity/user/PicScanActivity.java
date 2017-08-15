package com.ubtechinc.alpha2ctrlapp.ui.activity.user;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.util.ImageLoad.ImageLoader;
import com.ubtechinc.alpha2ctrlapp.widget.OnSlideListener;
import com.ubtechinc.alpha2ctrlapp.widget.ViewSwitcherOpreater;

public class PicScanActivity extends Activity implements OnSlideListener {

	
	private String[] urls;
//	private int  posion;
//	private PicScanView  picScanView;
	private ViewSwitcher vws_introduce;
	private ViewSwitcherOpreater vws_introduce_opreater;
	private int screenNo = -1;
	private TextView currentNo, totalNo;
	private RelativeLayout btn_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.pic_scan);
		initView();

	}

	@SuppressWarnings("unchecked")
	public void initView() {
		urls = getIntent().getStringArrayExtra("urls");
//		posion = getIntent().getIntExtra("position", 1);
//		picScanView = (PicScanView)findViewById(R.id.picScanView);
//		picScanView.setDrwableList(urls);
//		picScanView.initView();
		currentNo = (TextView) findViewById(R.id.current_pic_num);
		totalNo = (TextView) findViewById(R.id.pic_count);
		vws_introduce = (ViewSwitcher)findViewById(R.id.picScanView);
		vws_introduce_opreater = new ViewSwitcherOpreater(vws_introduce, this,this);
		vws_introduce_opreater.setImgs(urls);
		totalNo.setText(urls.length + "");
		btn_back = (RelativeLayout)findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PicScanActivity.this.finish();
			}
		});
	}

	@Override
	public void getNext(ViewSwitcher _switcher, String[] _img_ids) {
		// TODO Auto-generated method stub
		if (screenNo < _img_ids.length - 1) {
			screenNo++;
			_switcher.setInAnimation(this , R.anim.slide_in_right);
			_switcher
					.setOutAnimation(this, R.anim.slide_out_left);
			ImageView img = (ImageView) _switcher.getNextView();
			ImageLoader.getInstance(PicScanActivity.this).displayImage(urls[screenNo], img, 4);
//			img.setImageResource(_img_ids[screenNo]);
//			setImageIndex(screenNo);
			currentNo.setText(screenNo+ 1 + "");
		
			_switcher.showNext();
		}
	}

	@Override
	public void getPrev(ViewSwitcher _switcher, String[] _img_ids) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		if (screenNo > 0) {
			screenNo--;
			_switcher.setInAnimation(this, R.anim.slide_in_lef);
			_switcher.setOutAnimation(this, R.anim.slide_out_right);
			ImageView img = (ImageView) _switcher.getNextView();
			ImageLoader.getInstance(PicScanActivity.this).displayImage(urls[screenNo], img, 4);
			currentNo.setText(screenNo+ 1 + "");
			_switcher.showPrevious();

		}
	
	}

}