package com.ubtechinc.alpha2ctrlapp.ui.activity.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.ubtech.utilcode.utils.SPUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseContactActivity;
import com.ubtechinc.alpha2ctrlapp.ui.activity.main.MainPageActivity;
import com.ubtechinc.alpha2ctrlapp.ui.adapter.app.LanguageAdapter;

public class LanguageSetting extends BaseContactActivity {

	private String TAG = "AlarmRepeateFrament";
	private ListView alarmListView;
	public final static String REPEATE = "repeate";
	public final static String TIP = "tips";
	private String[] arrayLanguage;
	private LanguageAdapter listapter;
	private TextView btnSave;
	private String mCurrentSetLanguage = "";
	private String skinPath = Constants.CHANGE_LANGUAGE_APK_PATH;
	private 	String mLanguage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clock_layout);
		this.mContext =this;
		initView();
	}


	public void initView() {
		this.title = (TextView) findViewById(R.id.authorize_title);
		title.setText(mContext.getString(R.string.language_setting));
		alarmListView = (ListView) findViewById(R.id.alarmList);

		mLanguage = SPUtils.get().getString(Constants.APP_LAUNGUAGE);
		btnSave = (TextView)findViewById(R.id.btn_top_right);
		btnSave.setText(R.string.alarm_sava);
		btnSave.setVisibility(View.VISIBLE);
		btnSave.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(listapter.clickItem==-1||listapter.clickItem==0){
					SPUtils.get().put(Constants.APP_LAUNGUAGE,"");
				}else {
					String language = arrayLanguage[listapter.clickItem];
					SPUtils.get().put(Constants.APP_LAUNGUAGE, language);

				}

				MainPageActivity.isFromChangeLan =true;
				if(	Alpha2Application.getAlpha2().removeActivity(MainPageActivity.class.getName())){//重新加载MainPageActivity
					Intent intent = new Intent(mContext,MainPageActivity.class);
					mContext.startActivity(intent);

				}



			}
		});
		initLanguageList();

	}



	public void initLanguageList() {
		mLanguage =SPUtils.get().getString(Constants.APP_LAUNGUAGE, "");
		arrayLanguage = mContext.getResources().getStringArray(R.array.language_choose);
		listapter = new LanguageAdapter(mContext, arrayLanguage);

		if(TextUtils.isEmpty(mLanguage)){
			listapter.setDefualt(0);
		}else{
			for(int i =0;i<arrayLanguage.length;i++){
				if(mLanguage.equalsIgnoreCase(arrayLanguage[i])){
					listapter.setDefualt(i);
					break;
				}
			}
		}

		alarmListView.setAdapter(listapter);
		alarmListView.setOnItemClickListener(listapter);

	}




	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	
	}

}
