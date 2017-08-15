package com.ubtechinc.alpha2ctrlapp.ui.activity.robot;

import android.os.Bundle;
import android.view.View;

import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseActivity;
/**
 * @ClassName ListenVoiceGuideActivity
 * @date 6/8/2017
 * @author tanghongyu
 * @Description 机器人如何进入联网引导页
 * @modifier
 * @modify_time
 */
public class ListenVoiceGuideActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listen_net_guide);

		setTitle(R.string.ble_connect_to_the_internet);

		setupTitleView();
	}

	/**
	 * 初始化标题栏
	 * */
	private void setupTitleView() {
        /*设置跳过按钮不可见*/
		if(btn_ignore != null) {
			btn_ignore.setVisibility(View.GONE);
		}

        /*返回按钮需要弹出对话框*/
		if(btn_back != null) {
			btn_back.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					finish();
				}
			});
		}
	}
}
