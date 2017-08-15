package com.ubtechinc.alpha2ctrlapp.ui.fragment.app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseContactActivity;

/**
 * @ClassName SmarthomeFragment
 * @date 6/12/2017
 * @author tanghongyu
 * @Description 智能家居
 * @modifier
 * @modify_time
 */
public class SmarthomeFragment extends BaseContactActivity {
	public String TAG = "SmarthomeFragment";
	private Button configButton;
	private EditText wifiEditText, passwordEditText;

	//private ForBroadlink forBroadlink;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_smarthome_settings);
		this.mContext = this;
		initView();
	}

	public void initView() {
		// TODO Auto-generated method stub
		wifiEditText = (EditText) findViewById(R.id.et_wifi_ssid);
		passwordEditText = (EditText) findViewById(R.id.et_wifi_password);
		configButton = (Button) findViewById(R.id.config_network);

		title.setText("智能家居网络配置");
		configButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				new Thread(new Runnable() {

					@Override
					public void run() {
						String ssid = wifiEditText.getText().toString();
						String password = passwordEditText.getText().toString();
						if(ssid.equals("") || password.equals("")) {
							SmarthomeFragment.this.runOnUiThread(new Runnable(){
								@Override
								public void run() {
									Toast.makeText(SmarthomeFragment.this, "wifi/密码不能为空", Toast.LENGTH_LONG);
								}
							});
						} else {
							//forBroadlink.easyConfig(ssid, password, 1);
						}
					}
				}).start();
			}
		});

		initEditText();

		//forBroadlink = ForBroadlink.getForBroadlinkInstance(SmarthomeFragment.this);
	}

	private void initEditText() {
//		NetworkUtil networkUtil = new NetworkUtil(this);
//		networkUtil.startScan();
//		String ssid = networkUtil.getWiFiSSID();
//		wifiEditText.setText(ssid);
	}


	
}