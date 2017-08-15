package com.ubtechinc.alpha2ctrlapp.ui.fragment.app;

import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.ubtechinc.alpha.CmrAppConfigData;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.BaseHandler;
import com.ubtechinc.alpha2ctrlapp.data.robot.IRobotAppDataSource;
import com.ubtechinc.alpha2ctrlapp.data.robot.RobotAppRepository;
import com.ubtechinc.alpha2ctrlapp.entity.business.app.AppConfig;
import com.ubtechinc.alpha2ctrlapp.entity.business.app.AppDatas;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.base.BaseContactFragememt;
import com.ubtechinc.alpha2ctrlapp.widget.ConfigView;
import com.ubtechinc.alpha2ctrlapp.widget.ConfigView.ConfigListener;
import com.ubtechinc.nets.http.ThrowableWrapper;
import com.ubtechinc.nets.phonerobotcommunite.ICallback;
import com.ubtechinc.nets.utils.JsonUtil;

public class AppSettingConfigFragment extends BaseContactFragememt implements
        ConfigListener, OnClickListener {

	private ConfigView configView;
	private Button savaBt;
	public String TAG = "AppSettingConfigFragment";

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
		
	}

	@Override
	public View onCreateFragmentView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.activity_setting_config, container,
				false);
		
	}

	public void initView() {
		configView = (ConfigView) getActivity().findViewById(R.id.configView);
		configView.setListener(this);
		savaBt = (Button) getActivity().findViewById(R.id.savaBt);
		title.setText(getString(R.string.app_setting_appconfig));
		setListenerEvent();
		getAppConfig();
	}

	public void getAppConfig() {


		RobotAppRepository.getInstance().getThirdAppConfig(0, mMainPageActivity.currentAppInfo.getPackageName(), "en".getBytes(), new ICallback<CmrAppConfigData.CmrAppConfigDataResponse>() {
			@Override
			public void onSuccess(CmrAppConfigData.CmrAppConfigDataResponse data) {
				String json = new String(data.getTags());
				AppConfig config = JsonUtil.getObject(json,
						AppConfig.class);
				String json2 = new String(data.getDatas().toByteArray());
				AppDatas datas = JsonUtil.getObject(json2,
						AppDatas.class);
				configView.setData(config, datas);
			}

			@Override
			public void onError(ThrowableWrapper e) {

			}
		});
	}
	
	public void setListenerEvent(){
		savaBt.setOnClickListener(this);
	}

	public void onSave() {
		AppDatas datas = configView.getData2();
		String json = JsonUtil.object2Json(datas);
		RobotAppRepository.getInstance().saveThirdAppConfig(0,mMainPageActivity.currentAppInfo.getPackageName(),json.getBytes(), new IRobotAppDataSource.ControlAppCallback(){

			@Override
			public void onSuccess() {
				Toast.makeText(getActivity(), "ALPHA_MSG_RSP_SAVE_APPCONFIG", Toast.LENGTH_SHORT).show();

			}

			@Override
			public void onFail(ThrowableWrapper e) {

			}
		});

	}

	@Override
	public void onResume() {
		super.onResume();

		mMainPageActivity.currentFragment = this;
//		mMainPageActivity.btn_swich_active.setVisibility(View.GONE);
		mMainPageActivity.mainTopView.setVisibility(View.GONE);
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



			}
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}


	@Override
	public void isInitSuccess(boolean success) {
		// TODO Auto-generated method stub
		if (success) {
			savaBt.setClickable(success);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.savaBt:
			onSave();
			break;

		default:
			break;
		}
	}

}
