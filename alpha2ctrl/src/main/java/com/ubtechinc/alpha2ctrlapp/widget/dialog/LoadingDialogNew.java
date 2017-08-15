//package com.ubtechinc.alpha2ctrlapp.widget.dialog;
//
//import android.app.Activity;
//import android.app.Dialog;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Build;
//import android.support.annotation.NonNull;
//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
//
//import com.ubtechinc.alpha2ctrlapp.R;
//import com.ubtechinc.alpha2ctrlapp.widget.SystemBarTintManager;
//
//
//public class LoadingDialog extends Dialog {
//
//
//	private  ThreePointLoadingView imgLogo;
//	private Activity activity;
//	private LoadingDialog(@NonNull Activity activity) {
//
//		super(activity, R.style.deleteDialog);
//		this.mMainPageActivity = mMainPageActivity;
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//			setTranslucentStatus(true);
//			SystemBarTintManager tintManager = new SystemBarTintManager(activity);
//			tintManager.setStatusBarTintEnabled(true);
//			tintManager.setStatusBarTintResource(R.color.bg_color_b3);//通知栏所需颜色
//		}
//		init();
//	}
//
//	private void setTranslucentStatus(boolean on) {
//		Window win = getWindow();
//		WindowManager.LayoutParams winParams = win.getAttributes();
//		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//		if (on) {
//			winParams.flags |= bits;
//		} else {
//			winParams.flags &= ~bits;
//		}
//		win.setAttributes(winParams);
//	}
//
//
//	private void init() {
//
//		View root = View.inflate(activity, R.layout.dialog_loading, null);
////		txt_dia_msg = (TextView) root.findViewById(R.id.txt_dia_msg);
//		imgLogo = (ThreePointLoadingView) root.findViewById(R.id.img_dia_logo);
//		this.setContentView(root);
//		ColorDrawable colorDrawable = new ColorDrawable(Color.argb(0, 0, 0, 0));
//		this.getWindow().setBackgroundDrawable(colorDrawable);
//		this.setCancelable(false);
//	}
//
//	@Override
//	public void dismiss() {
//		if(imgLogo != null) imgLogo.onDetachedFromWindow();
//		super.dismiss();
//	}
//}
