package com.ubtechinc.alpha2ctrlapp.widget.dialog;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.widget.SystemBarTintManager;

public class EditGenderDialog extends Dialog implements View.OnClickListener {

	private OnEditMgenderListener mgenderListener;
	private OnEditFgenderListner  fgenderListener;
	private ImageButton genderFButton,genderMButton;
	public EditGenderDialog(Activity context, int gender) {
		super(context, R.style.deleteDialog);
		 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
	           setTranslucentStatus(true);    
	           SystemBarTintManager tintManager = new SystemBarTintManager(context);
	           tintManager.setStatusBarTintEnabled(true);    
	           tintManager.setStatusBarTintResource(R.color.bg_color_b3);//通知栏所需颜色
	       }
		this.setContentView(R.layout.edit_gender_dialog);
		genderFButton = (ImageButton) this.findViewById(R.id.btn_gender_f);
		genderMButton = (ImageButton)this.findViewById(R.id.btn_gender_m);
		this.setCanceledOnTouchOutside(true);
		genderFButton.setOnClickListener(this);
		genderMButton.setOnClickListener(this);
		refresh(gender);
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
	public void refresh(int gender) {
		if (gender == 1) {
			genderFButton.setBackgroundResource(R.drawable.gender_unselected);
			genderMButton.setBackgroundResource(R.drawable.gender_selected);
		} else {
			genderFButton.setBackgroundResource(R.drawable.gender_selected);
			genderMButton.setBackgroundResource(R.drawable.gender_unselected);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.btn_gender_m:
			EditGenderDialog.this.cancel();
			mgenderListener.onEditM();
			break;
		case R.id.btn_gender_f:
			EditGenderDialog.this.cancel();
			fgenderListener.onEditF();
			break;
		default:
			break;
		}

	}
	public interface OnEditMgenderListener {
		public void onEditM();
	}
	public interface  OnEditFgenderListner{
		public void onEditF();
	}


	public void setMConfirmListener(OnEditMgenderListener mgenderListener) {
		this.mgenderListener = mgenderListener;
	}

	public void setFConfirmListener(OnEditFgenderListner fgenderListener) {
		this.fgenderListener = fgenderListener;
	}
	
}
