package com.ubtechinc.alpha2ctrlapp.ui.fragment.app;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.BaseHandler;
import com.ubtechinc.alpha2ctrlapp.constants.MessageType;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseContactActivity;
import com.ubtechinc.alpha2ctrlapp.widget.ConfigView;

/**
 * 开发者选项页
 */
public class DeveloperFragement extends BaseContactActivity {

	private ConfigView configView;
	private Button savaBt;
	public String TAG = "AppSettingConfigFragment";
	private TextView alarm2;
	private ImageView alarm_usable_tg;
	private boolean isOpen=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_develper_setting);
		this.mContext = this;
		initView();
	}
	

	public void initView() {

		alarm2 = (TextView)findViewById(R.id.alarm2);
		alarm_usable_tg = (ImageView) findViewById(R.id.alarm_usable_tg);
		if(isOpen){
			alarm_usable_tg.setImageResource(R.drawable.butn_close);
		}else{
			alarm_usable_tg.setImageResource(R.drawable.butn_open);
		}
		alarm_usable_tg.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isOpen) {
					alarm2.setText(getString(R.string.alarm_usable_on));
					isOpen =true;
					openDebug();
				} else {
					alarm2.setText(getString(R.string.alarm_usable_off));
					closeDebug();
					isOpen =false;
				}
				if(isOpen){
					alarm_usable_tg.setImageResource(R.drawable.butn_close);
				}else{
					alarm_usable_tg.setImageResource(R.drawable.butn_open);
				}
			}
		});

		title = (TextView)findViewById(R.id.authorize_title);
		title.setText(getString(R.string.device_developer));

	}

	public void openDebug() {
//		BaseRequest br = new BaseRequest();
//		br.setCmd((short) 1);
//		sendRequest(br, MessageType.ALPHA_SEND_DEBUG );
	}

	public void closeDebug() {
//		BaseRequest br = new BaseRequest();
//		br.setCmd((short) 0);
//		sendRequest(br, MessageType.ALPHA_SEND_DEBUG
//			);
	}
  



	private class SettingHandler extends BaseHandler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg == null) {
				Logger.i( "handleMessage msg is null.");
				return;
			}
			switch (msg.what) {
			case MessageType.ALPHA_SEND_RSP_DEBUG:
				ToastUtils.showShortToast( "ALPHA_SEND_RSP_DEBUG");
				break;
			}
		}
	}



}
