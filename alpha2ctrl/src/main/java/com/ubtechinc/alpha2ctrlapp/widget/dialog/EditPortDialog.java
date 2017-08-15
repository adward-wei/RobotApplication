package com.ubtechinc.alpha2ctrlapp.widget.dialog;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.widget.SystemBarTintManager;


public class EditPortDialog extends Dialog implements View.OnClickListener {

	private ImageButton test14Button,test12MButton,officalButton;
	private Context mContext;
	public EditPortDialog(@NonNull Activity activity) {
		super(activity, R.style.deleteDialog);
		mContext = activity.getApplicationContext();
		 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
	           setTranslucentStatus(true);    
	           SystemBarTintManager tintManager = new SystemBarTintManager(activity);
	           tintManager.setStatusBarTintEnabled(true);    
	           tintManager.setStatusBarTintResource(R.color.bg_color_b3);//通知栏所需颜色
	       }
		this.setContentView(R.layout.edit_port_dialog);
		test14Button = (ImageButton) this.findViewById(R.id.btn14);
		test12MButton = (ImageButton)this.findViewById(R.id.btn12);
		officalButton = (ImageButton)this.findViewById(R.id.btn_offical) ;
		findViewById(R.id.rl_14).setOnClickListener(this);
		findViewById(R.id.rl_12).setOnClickListener(this);
		findViewById(R.id.rl_offical).setOnClickListener(this);

		this.setCanceledOnTouchOutside(true);
		test14Button.setOnClickListener(this);
		test12MButton.setOnClickListener(this);
		officalButton.setOnClickListener(this);
		refresh();
	}
	 @TargetApi(19)
	   private void setTranslucentStatus(boolean on) {    
	       Window win = getWindow();
	       WindowManager.LayoutParams winParams = win.getAttributes();
	       final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
	       if (on) {    
	           winParams.flags |= bits;    
	       } else {    
	           winParams.flags &= ~bits;    
	       }    
	       win.setAttributes(winParams);    
	   }  
	public void refresh() {
//		String host = SPUtils.get().getString(XmppConstant.HOST);
//		if(TextUtils.isEmpty(host)){
//			host = XmppConstant.HOST;
//		}
//		if(host.equals("10.10.1.14")){
//			test14Button.setBackgroundResource(R.drawable.gender_selected);
//			test12MButton.setBackgroundResource(R.drawable.gender_unselected);
//			officalButton.setBackgroundResource(R.drawable.gender_unselected);
//		}else if(host.equals("services.ubtrobot.com")){
//
//			officalButton.setBackgroundResource(R.drawable.gender_selected);
//			test14Button.setBackgroundResource(R.drawable.gender_unselected);
//			test12MButton.setBackgroundResource(R.drawable.gender_unselected);
//
//		}else{
//			test14Button.setBackgroundResource(R.drawable.gender_unselected);
//			test12MButton.setBackgroundResource(R.drawable.gender_selected);
//			officalButton.setBackgroundResource(R.drawable.gender_unselected);
//		}

	}
	private void save(int type){
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rl_14:
			EditPortDialog.this.cancel();
			save(14);
			break;
		case R.id.rl_12:
			EditPortDialog.this.cancel();
			save(12);
			break;
		case R.id.rl_offical:
			EditPortDialog.this.cancel();
			save(0);
			break;
		default:
			break;
		}

	}

	
}
