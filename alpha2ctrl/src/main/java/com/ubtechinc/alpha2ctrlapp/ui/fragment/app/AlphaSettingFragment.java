package com.ubtechinc.alpha2ctrlapp.ui.fragment.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2ActivityIntent;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.base.BaseHandler;
import com.ubtechinc.alpha2ctrlapp.constants.MessageType;
import com.ubtechinc.alpha2ctrlapp.constants.NetWorkConstant;
import com.ubtechinc.alpha2ctrlapp.constants.PreferenceConstants;
import com.ubtechinc.alpha2ctrlapp.service.RobotManagerService;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseContactActivity;

public class AlphaSettingFragment extends BaseContactActivity implements
		OnClickListener {

	private String TAG = "AlphaSettingFragment";

	private String alphaName, alphaMacaddress;
	private String alphaNo;
	private String[] alpha2MacList = new String[1];
	private ImageButton btn_authorize;
	private RelativeLayout layer_lay, system_update_lay,status_lay,devoloper_lay, smarthome_lay,reset_lay;
	private TextView alphaNameTV, alphaId;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alpha_setiing);
		this.mContext = this;
		initView();

	}

	public void initView() {
		alphaName =getIntent().getStringExtra(Alpha2ActivityIntent.ALPHANAME);
		alphaMacaddress =getIntent().getStringExtra(Alpha2ActivityIntent.ALPHAMACADRESS);
		alphaNo = getIntent().getStringExtra(PreferenceConstants.ROBOT_SERIAL_NO);
		alpha2MacList[0] = alphaMacaddress;
		Alpha2Application.currentAlpha2Mac = alphaMacaddress;
		layer_lay = (RelativeLayout) findViewById(R.id.layer_lay);
		system_update_lay = (RelativeLayout)findViewById(R.id.system_update_lay);
		status_lay = (RelativeLayout)findViewById(R.id.status_lay);
		devoloper_lay = (RelativeLayout)findViewById(R.id.devoloper_lay);
		smarthome_lay = (RelativeLayout)findViewById(R.id.smarthome_lay);
		reset_lay = (RelativeLayout)findViewById(R.id.reset_lay);
		devoloper_lay.setOnClickListener(this);
		smarthome_lay.setOnClickListener(this);
		layer_lay.setOnClickListener(this);
		system_update_lay.setOnClickListener(this);
		status_lay.setOnClickListener(this);
		reset_lay.setOnClickListener(this);
		title = (TextView)findViewById(R.id.authorize_title);
		title.setText(getString(R.string.device_sets_title));

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
			case MessageType.ALPHA_LOST_CONNECTED:
				isCurrentAlpha2MacLostConnection((String) msg.obj);
				break;

			case NetWorkConstant.RESPONSE_RESET_ROBOT_SUCCESS:
				ToastUtils.showShortToast( "回复出厂设置成功");

				reset_lay.setVisibility(View.GONE);
				RobotManagerService.getInstance().clearConnectCacheData();
				break;
			case NetWorkConstant.RESPONSE_RESET_ROBOT_FAILED:
				ToastUtils.showShortToast( "回复出厂设置失败");
				break;
			}
		}
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.layer_lay:

			break;
		case R.id.system_update_lay:
			break;
		case R.id.status_lay:
			break;
		case R.id.devoloper_lay:
			Intent intent  = new Intent(this,DeveloperFragement.class);
			startActivity(intent);
			break;
		case R.id.smarthome_lay:
			Intent intent2  = new Intent(this,SmarthomeFragment.class);
			startActivity(intent2);
			break;

		default:
			break;
		}

	}
	

	
}
