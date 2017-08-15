package com.ubtechinc.alpha2ctrlapp.ui.activity.app;

import android.os.Bundle;
import android.os.Message;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.BaseHandler;
import com.ubtechinc.alpha2ctrlapp.data.robot.RobotStatusInfo;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseContactActivity;

public class AlphaStatusDetailActivity extends BaseContactActivity {

	private String TAG = "AlphaWareInfoActivity";
	private TextView speechengine_tv,speechengineState_tv,speechengineDetail_tv,appName_tv,speechASREngine_tv;
	private RobotStatusInfo info;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alpha_status_detail);
		this.mContext = this;
		initView();

	}

	public void initView() {
		speechengine_tv = (TextView)findViewById(R.id.speechengine_tv);
		speechengineState_tv = (TextView)findViewById(R.id.speechengineState_tv);
		speechengineDetail_tv = (TextView)findViewById(R.id.speechengineDetail_tv);
		appName_tv = (TextView)findViewById(R.id.appName_tv);
		speechASREngine_tv = (TextView)findViewById(R.id.speechASREngine_tv);
		title = (TextView)findViewById(R.id.authorize_title);
		title.setText(R.string.abnormal_nav);
		info = (RobotStatusInfo)getIntent().getSerializableExtra("info");
		refreshInfo(info);
		
	
	}

	private class FunctionHandler extends BaseHandler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// TODO Auto-generated method stub
			if (msg == null) {
				Logger.i( "handleMessage msg is null.");
				return;
			}
			switch (msg.what) {
			}
		}
	}

	private void refreshInfo(RobotStatusInfo data){
		speechengine_tv .setText(data.getSpeechEngine());
		speechengineState_tv.setText(data.getSpeechEngineState());
		speechengineDetail_tv.setText(data.getSpeechEngineDetail());
		appName_tv.setText(data.getAppName());
		speechASREngine_tv.setText(data.getSpeechASREngine());
	}



	
}
