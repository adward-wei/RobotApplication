package com.ubtechinc.alpha2ctrlapp.widget.popWindow;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.widget.SystemBarTintManager;

public class BindGuidePopView extends Dialog implements
		View.OnClickListener {

	private Button btn_bottom;

	public BindGuidePopView(Activity context) {
		super(context, R.style.deleteDialog);
		 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    
	           setTranslucentStatus(true);    
	           SystemBarTintManager tintManager = new SystemBarTintManager(context);
	           tintManager.setStatusBarTintEnabled(true);    
	           tintManager.setStatusBarTintResource(R.color.bg_color_b3);//通知栏所需颜色
	       }
		this.setContentView(R.layout.bind_guide);
		btn_bottom = (Button)findViewById(R.id.guide_bottom_next);
		btn_bottom.setOnClickListener(this);
		this.setCancelable(false);
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		
		case R.id.guide_bottom_next:
			BindGuidePopView.this.dismiss();
			break;
		default:
			break;
		}

	}
	public interface OnConfirmListener {
		public void onConfirm();
	}
	
}
