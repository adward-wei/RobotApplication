package com.ubtechinc.alpha2ctrlapp.ui.activity.robot;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ubtechinc.alpha2ctrlapp.R;

public class ConnectNetGuideAcitivty extends Activity {
	private ImageView btn_close;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connect_net_guide);
		btn_close = (ImageView)findViewById(R.id.btn_close);
		btn_close.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	
	}


}
