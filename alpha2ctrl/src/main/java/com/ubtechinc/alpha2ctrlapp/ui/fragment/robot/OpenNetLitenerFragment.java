package com.ubtechinc.alpha2ctrlapp.ui.fragment.robot;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.ui.activity.robot.ListenVoiceGuideActivity;
import com.ubtechinc.alpha2ctrlapp.ui.activity.robot.NetworkConfigureActivity;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.base.BaseFragment;

public class OpenNetLitenerFragment extends BaseFragment {
	private TextView btn_start;
	 private TextView btn_go_guide;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		init(false, getString(R.string.net_listener_title));
		initView();
		if(activity.btn_ignore!=null)
			activity.btn_ignore.setVisibility(View.GONE);
		
		initControlListener();
		
	}
	@Override
	public View onCreateFragmentView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.open_alpha_wifi_listener, container, false);
	
	}
	
	@Override
	public void initView() {
		
		
		btn_start = (TextView) getActivity().findViewById(R.id.btn_start);
		btn_go_guide = (TextView)getActivity().findViewById(R.id.btn_go_guide);
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
						Intent intent  = new Intent(getActivity(), /*InputWifiActivity.class*/NetworkConfigureActivity.class);
						intent.putExtras(bundle);
						getActivity().startActivity(intent);
					}
				});
		btn_go_guide.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),ListenVoiceGuideActivity.class);
				getActivity().startActivity(intent);
			}
		});
	}

}
