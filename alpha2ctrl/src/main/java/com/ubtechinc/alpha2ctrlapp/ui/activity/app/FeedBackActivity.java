package com.ubtechinc.alpha2ctrlapp.ui.activity.app;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.data.user.IUserConfigDataSource;
import com.ubtechinc.alpha2ctrlapp.data.user.UserConfigReponsitory;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseContactActivity;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.LoadingDialog;
import com.ubtechinc.nets.http.ThrowableWrapper;

/**
 * @ClassName FeedBackActivity
 * @date 5/25/2017
 * @author tanghongyu
 * @Description 意见反馈
 * @modifier
 * @modify_time
 */
public class FeedBackActivity extends BaseContactActivity {
	private EditText feedBackEd;
//	private UserAction action;
	private Button btn_feedBack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feed_back_layout);
		this.mContext = this;
		initView();
	}


	public void initView() {
		// TODO Auto-generated method stub
//		action = UserAction.getInstance(mContext, null);
		feedBackEd = (EditText)findViewById(R.id.input_feedback)	;
		title= (TextView) findViewById(R.id.authorize_title);
		title.setText(getString(R.string.feed_back_title));
		btn_feedBack = (Button)findViewById(R.id.btn_feedBack);
		btn_feedBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				doFeedBack();
			}
		});
	}


	@Override
	public void onResume(){
		super.onResume();
	}
    public void doFeedBack(){
    	if(TextUtils.isEmpty(feedBackEd.getText().toString())){
    		ToastUtils.showShortToast(  R.string.feed_back_tips);
    	}else if(feedBackEd.getText().toString().length()>200){
    		ToastUtils.showShortToast(  R.string.feed_back_tips_to_long);
    	}else{
			UserConfigReponsitory.get().feedback(feedBackEd.getText().toString(), new IUserConfigDataSource.FeedbackCallback() {
				@Override
				public void onSuccess() {
					ToastUtils.showShortToast(  R.string.feedback_success);
					onBack(null);
				}

				@Override
				public void onFail(ThrowableWrapper e) {
					ToastUtils.showShortToast(  e.getMessage());
				}
			});
        	LoadingDialog.getInstance(mContext).show();
    	}
    	
    }



}
