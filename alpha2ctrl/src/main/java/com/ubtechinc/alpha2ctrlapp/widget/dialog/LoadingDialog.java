package com.ubtechinc.alpha2ctrlapp.widget.dialog;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.widget.SystemBarTintManager;

public class LoadingDialog extends Dialog {

	private Context mContext;
	private static ThreePointLoadingView img_dia_logo;
	public static LoadingDialog mDia;
	public static final int CHECK_TIME_OUT=1000;
	private int mTimeout = 20000;
	public void setTimeout(int timeout) {

		this.mTimeout = timeout*1000;
	}
	public void setCancelable(boolean cancelable) {
//		mDia.setCancelable(cancelable);

	}
	public void show() {
		try {
			super.show();
//			Animation operatingAnim = AnimationUtils.loadAnimation(mActivity,
//					R.anim.turn_around_anim);
//			operatingAnim.setInterpolator(new LinearInterpolator());
//			img_dia_logo.startAnimation(operatingAnim);
			if(mHandler.hasMessages(CHECK_TIME_OUT)){
				mHandler.removeMessages(CHECK_TIME_OUT);
			}
			mHandler.sendEmptyMessageDelayed(CHECK_TIME_OUT, mTimeout);// 20s 秒后检查加载框是否还在
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
//	public void showMessage(String msg) {
//		if (mDia.isShowing()) {
//			// img_dia_logo.setVisibility(View.INVISIBLE);
//			if (msg.equals("")) {
//				txt_dia_msg.setVisibility(View.GONE);
//			} else {
//				txt_dia_msg.setVisibility(View.VISIBLE);
//				txt_dia_msg.setText(msg);
//			}
//			// img_dia_logo.setVisibility(View.VISIBLE);
//		}
//
//	}

	private LoadingDialog(Context context) {	
//		super(context,R.style.MyDialog);
//		super(context);
		super(context, R.style.deleteDialog);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
			SystemBarTintManager tintManager = new SystemBarTintManager((Activity) context);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setStatusBarTintResource(R.color.bg_color_b3);//通知栏所需颜色
		}
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
	public static LoadingDialog getInstance(Context _context) {
		if (mDia != null && mDia.isShowing()){
			/*在dimiss一个dialog之前，必须确保所在的Activity没有被销毁，否则会出现崩溃异常*/
			if(mDia.mContext != null && mDia.mContext instanceof Activity) {
				if(!((Activity) mDia.mContext).isFinishing()) {
					mDia.cancel();
					mDia.mContext = null;
				}
			}
		}

		mDia = new LoadingDialog(_context);
		mDia.mContext = _context;
		mDia.initDia();
		return mDia;
	}
    public static  void dissMiss(){
    	try {
    		if (mDia != null && mDia.isShowing())
    			mDia.cancel();
    		img_dia_logo.onDetachedFromWindow();
    		if(mHandler.hasMessages(CHECK_TIME_OUT)){
				mHandler.removeMessages(CHECK_TIME_OUT);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
    	
    }
	private void initDia() {

		View root = View.inflate(mContext, R.layout.dialog_loading, null);
//		txt_dia_msg = (TextView) root.findViewById(R.id.txt_dia_msg);
		img_dia_logo = (ThreePointLoadingView) root.findViewById(R.id.img_dia_logo);
		this.setContentView(root);
		ColorDrawable colorDrawable = new ColorDrawable(Color.argb(0, 0, 0, 0));
		this.getWindow().setBackgroundDrawable(colorDrawable);
		this.setCancelable(false);
	}
	private  static Handler mHandler = new Handler(){  
        
        public void handleMessage(Message msg){  
              
            switch(msg.what){
            case CHECK_TIME_OUT:
            	if (mDia != null && mDia.isShowing()){
					try{
						mDia.cancel();
						img_dia_logo.onDetachedFromWindow();
					}catch (Exception e){

					}

				}
            	break;
            }  
        }  
    };  
      
  }
