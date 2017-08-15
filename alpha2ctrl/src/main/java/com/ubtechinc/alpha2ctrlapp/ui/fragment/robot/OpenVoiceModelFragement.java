package com.ubtechinc.alpha2ctrlapp.ui.fragment.robot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.base.BaseFragment;

public class OpenVoiceModelFragement extends BaseFragment {
	private Button btn_start;
	private String name, psw, capa;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		init(false, getString(R.string.sonic_open_voice));
		initView();
		if(activity.btn_ignore!=null)
			activity.btn_ignore.setVisibility(View.VISIBLE);
		initControlListener();
	}
	@Override
	public View onCreateFragmentView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.voice_open, container, false);
	
	}
	
	@Override
	public void initView() {
		
		
		btn_start = (Button) getActivity().findViewById(R.id.btn_start);
		name = bundle.getString(Constants.WIFI_NAME);
		psw = bundle.getString(Constants.WIFI_PSW);
		capa = bundle.getString(Constants.WIFI_CAPABILITIY);
	}

	@Override
	public void initControlListener() {
		// TODO Auto-generated method stub
		btn_start.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(bundle==null)
							bundle = new Bundle();
						bundle.putString(Constants.WIFI_NAME, name);
						bundle.putString(Constants.WIFI_PSW, psw);
						bundle.putString(Constants.WIFI_CAPABILITIY, capa);
						replaceFragment(SendVoiceFragment.class.getName(), bundle);
					}
				});
	}

}
