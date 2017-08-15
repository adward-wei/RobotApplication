package com.ubtechinc.alpha2ctrlapp.widget.dialog;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.widget.SystemBarTintManager;

public class CommonDiaglog extends Dialog {
	private Activity mContext;
	private Button btn_confirm, btn_cancel;
	private  TextView messageTV;
	private OnPositiveClick positiveClick;
	private OnNegsitiveClick negsitiveClick;
	private ImageView msgPic;
	private TextView messagetTitle;
	private View verticalDivider;

	 public CommonDiaglog(Activity context, boolean hasImage) {
		 super(context, R.style.deleteDialog);
		 this.mContext = context;
		 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    
	           setTranslucentStatus(true);    
	           SystemBarTintManager tintManager = new SystemBarTintManager(context);
	           tintManager.setStatusBarTintEnabled(true);    
	           tintManager.setStatusBarTintResource(R.color.bg_color_b3);//通知栏所需颜色
	       }
		 if(hasImage){
			 this.setContentView(R.layout.common_dialog_has_image);
		 }
		 else
			 this.setContentView(R.layout.common_dialog);
		 btn_confirm = (Button) findViewById(R.id.btn_comfirm);
		 btn_cancel = (Button) findViewById(R.id.btn_cancel);
		 messageTV = (TextView) findViewById(R.id.dialog_msg);
		 messagetTitle = (TextView)findViewById(R.id.dialog_tips);
		 msgPic = (ImageView)findViewById(R.id.dialog_image);
		 verticalDivider = findViewById(R.id.vertical_divider);
		initClick(context);
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
						positiveClick.OnPositiveClick();
			
					CommonDiaglog.this.dismiss();
				}
			});
			btn_cancel.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if(negsitiveClick!=null){
						negsitiveClick.OnNegsitiveClick();
						
					}

					CommonDiaglog.this.dismiss();
				}
			});
	 }

	public void  setMessase(String message){
		messageTV.setText(message);
	}
    public void setImageView (Bitmap bitmap){
    	msgPic.setImageBitmap(bitmap);
    }
    public void setImageView (Drawable bitmap){
    	msgPic.setImageDrawable(bitmap);
    }
    public void setTitle(String title){
    	messagetTitle.setVisibility(View.VISIBLE);
    	messagetTitle.setText(title);
    }
    public void setButtonText(String nTv,String pTv){
    	btn_cancel.setText(nTv);
    	btn_confirm.setText(pTv);
    }
    public void setImageView (String  uString){
//    	LoadImage.setRounderConner(mActivity, msgPic, uString,1);
    }
    public void setImageViewVisble (){
    	msgPic.setVisibility(View.VISIBLE);
    }
	public OnPositiveClick getPositiveClick() {
		return positiveClick;
	}

	public void setPositiveClick(OnPositiveClick positiveClick) {
		this.positiveClick = positiveClick;
	}

	public OnNegsitiveClick getNegsitiveClick() {
		return negsitiveClick;
	}

	public void setNegsitiveClick(OnNegsitiveClick negsitiveClick) {
		this.negsitiveClick = negsitiveClick;
	}

	public void setNegsitiveVisible(boolean visible) {
		if(visible) {
			btn_cancel.setVisibility(View.VISIBLE);
			verticalDivider.setVisibility(View.VISIBLE);
		} else {
			btn_cancel.setVisibility(View.GONE);
			verticalDivider.setVisibility(View.GONE);
		}
	}

	public interface OnPositiveClick {
		public void OnPositiveClick();
	}
	public interface OnNegsitiveClick {
		public void OnNegsitiveClick();
	}
}
