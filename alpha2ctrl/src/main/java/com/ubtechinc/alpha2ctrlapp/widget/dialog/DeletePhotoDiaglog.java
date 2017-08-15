package com.ubtechinc.alpha2ctrlapp.widget.dialog;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.ImageModel;
import com.ubtechinc.alpha2ctrlapp.widget.SystemBarTintManager;

import java.util.List;

public class DeletePhotoDiaglog extends Dialog implements
		View.OnClickListener {

	private TextView deleteTips;
	private TextView btn_delete;
	private LinearLayout btn_cancel;
	private Activity context;
	private List<ImageModel>list;
	private View view;
	private OnConfirmListener confirmListener;

	public DeletePhotoDiaglog(Activity context, List<ImageModel>list, View v) {
		super(context, R.style.deleteDialog);
		this.context = context;
		 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    
	           setTranslucentStatus(true);    
	           SystemBarTintManager tintManager = new SystemBarTintManager(context);
	           tintManager.setStatusBarTintEnabled(true);    
	           tintManager.setStatusBarTintResource(R.color.bg_color_b3);//通知栏所需颜色
	       }
		this.setContentView(R.layout.delete_photo_diag);
		btn_delete = (TextView) this.findViewById(R.id.dialog_delete);
		btn_cancel = (LinearLayout)this.findViewById(R.id.btn_cancel);
		deleteTips = (TextView)this.findViewById(R.id.deletetips);
		this.setCanceledOnTouchOutside(true);
		btn_delete.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
		this.view = v;
		refresh(list,v);
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
	public void refresh(List<ImageModel>list,View view) {
		this.list = list;
		deleteTips.setText(context.getString(R.string.image_delete_phototips).replace("%", list.size()+""));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.btn_cancel:
			DeletePhotoDiaglog.this.cancel();
			view.setVisibility(View.VISIBLE);
			break;
		case R.id.dialog_delete:
			DeletePhotoDiaglog.this.cancel();
			view.setVisibility(View.GONE);
			confirmListener.onConfirm();
			break;
		default:
			break;
		}

	}
	public interface OnConfirmListener {
		public void onConfirm();
	}
	public OnConfirmListener getConfirmListener() {
		return confirmListener;
	}

	public void setConfirmListener(OnConfirmListener confirmListener) {
		this.confirmListener = confirmListener;
	}

	
}
