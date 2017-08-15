package com.ubtechinc.alpha2ctrlapp.widget.dialog;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.widget.SystemBarTintManager;

public class SingleButtonDialog extends Dialog {
	private Activity mContext;
	private TextView btn_confirm;
	private  TextView messageTV;
	private SingleButtonOnPositiveClick positiveClick;
	private SingleButtonOnNegativeClick negtiveClick;
	private TextView messagetTitle;
	private ImageView  btn_close;

	 public SingleButtonDialog(Activity context) {
		 super(context, R.style.deleteDialog);
		 this.mContext = context;
		 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    
	           setTranslucentStatus(true);    
	           SystemBarTintManager tintManager = new SystemBarTintManager(context);
	           tintManager.setStatusBarTintEnabled(true);    
	           tintManager.setStatusBarTintResource(R.color.bg_color_b3);//通知栏所需颜色
	       }
		
			 this.setContentView(R.layout.single_button_dialog);
		btn_confirm = (TextView) findViewById(R.id.btn_comfirm);
		messageTV = (TextView) findViewById(R.id.dialog_msg);
		messagetTitle = (TextView)findViewById(R.id.dialog_tips);
		btn_close = (ImageView)findViewById(R.id.btn_close);
		initClick(context);
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
	 public void initClick(Activity context){
		
		 btn_confirm.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(positiveClick!=null)
						positiveClick.OnSingleButtonPositiveClick();
			
					SingleButtonDialog.this.dismiss();
				}
			});
		 btn_close.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(negtiveClick!=null)
					negtiveClick.onSingleButtonOnNegativeClick();
				SingleButtonDialog.this.dismiss();
			}
		});
	 }

	public void  setMessase(String message){
		messageTV.setText(message);
	}
   
    public void setTitle(String title){
    	messagetTitle.setVisibility(View.VISIBLE);
    	messagetTitle.setText(title);
    }
    public void setButtonText(String nTv,String pTv){
    	btn_confirm.setText(pTv);
    }
    
	public SingleButtonOnPositiveClick getPositiveClick() {
		return positiveClick;
	}

	public void setPositiveClick(SingleButtonOnPositiveClick positiveClick) {
		this.positiveClick = positiveClick;
	}

	
	public SingleButtonOnNegativeClick getNegtiveClick() {
		return negtiveClick;
	}
	public void setNegtiveClick(SingleButtonOnNegativeClick negtiveClick) {
		this.negtiveClick = negtiveClick;
	}


	public interface SingleButtonOnPositiveClick {
		public void OnSingleButtonPositiveClick();
	}
	public interface SingleButtonOnNegativeClick{
		public void onSingleButtonOnNegativeClick();
	}
}
