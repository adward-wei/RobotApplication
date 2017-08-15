package com.ubtechinc.alpha2ctrlapp.widget.popWindow;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.tencent.tauth.IUiListener;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.entity.AppDetail;
import com.ubtechinc.alpha2ctrlapp.entity.business.shop.ActionDetail;
import com.ubtechinc.alpha2ctrlapp.third.FaceBookManager;
import com.ubtechinc.alpha2ctrlapp.third.IWeiXinListener;
import com.ubtechinc.alpha2ctrlapp.third.TencentQQManager;
import com.ubtechinc.alpha2ctrlapp.third.TwitterManager;
import com.ubtechinc.alpha2ctrlapp.third.WeiXinManager;

public class SharePopuWindow extends Dialog implements View.OnClickListener {
	private Activity context;
	
	private ImageButton btn_to_qq;
	private ImageButton btn_to_kongjian;
	private ImageButton btn_to_weixin;
	private ImageButton btn_to_pengyouquan;
	private ImageButton btn_to_weibo;
	private ImageButton btn_to_facebook;
	private ImageButton btn_to_twitter;
	private Button btn_cancel_share;
	private int type=SHARE_TYPE_NONE; // 1表示动作分享，2：表示应用分享3:表示相册
	public static final int SHARE_TYPE_NONE = 0;
	public static final int SHARE_TYPE_ACTION = 1;
	public static final int SHARE_TYPE_APP = 2;
	public static final int SHARE_TYPE_PICTURE = 3;
	private IUiListener listener;
	private IWeiXinListener mweixinListener;
	private Object mObj;
	private boolean isFromLocal = false;
	private String imagePath ;

	public SharePopuWindow(Activity context, int type, Object obj, IUiListener listener, IWeiXinListener weixinListener) {
		super(context, R.style.menutyle);
		this.context = context;
		this.setContentView(R.layout.activity_actions_lib_pre_view_share);
		Window dialogWindow = this.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.BOTTOM);
		int screenWidth = context.getWindowManager().getDefaultDisplay()
				.getWidth();
		int screenHeight = context.getWindowManager().getDefaultDisplay()
				.getHeight();
		Resources resources = context.getResources();
		int resourceId = resources.getIdentifier("navigation_bar_height",
				"dimen", "android");
		if (resourceId > 0) {
			screenHeight = screenHeight
					- resources.getDimensionPixelSize(resourceId);// 减去底部操作栏的高度
		}
		if (screenWidth < screenHeight) {
			lp.width = screenWidth;
			lp.height = (int) (screenHeight * (5.0 / 10.0));
		} else {
			lp.width = (int) (screenWidth * (4.0 / 5.0));
			lp.height = (int) (screenHeight * (3.0 / 4.0));
		}
		dialogWindow.setAttributes(lp);
		btn_cancel_share = (Button) findViewById(R.id.btn_cancel_share);
		btn_to_qq = (ImageButton) findViewById(R.id.btn_to_qq);
		btn_to_kongjian = (ImageButton) findViewById(R.id.btn_to_kongjian);
		btn_to_weixin = (ImageButton) findViewById(R.id.btn_to_weixin);
		btn_to_pengyouquan = (ImageButton) findViewById(R.id.btn_to_pengyouquan);
		btn_to_weibo = (ImageButton) findViewById(R.id.btn_to_weibo);
		btn_to_twitter = (ImageButton)findViewById(R.id.btn_to_twitter);
		btn_to_facebook = (ImageButton)findViewById(R.id.btn_to_facebook);
		this.setCanceledOnTouchOutside(true);
		btn_cancel_share.setOnClickListener(this);
		btn_to_qq.setOnClickListener(this);
		btn_to_kongjian.setOnClickListener(this);
		btn_to_weixin.setOnClickListener(this);
		btn_to_pengyouquan.setOnClickListener(this);
		btn_to_weibo.setOnClickListener(this);
		btn_to_facebook.setOnClickListener(this);
		btn_to_twitter.setOnClickListener(this);
		this.listener = listener;
		this.mweixinListener = weixinListener;
		this.mObj = obj;
		this.type = type;

	}

	public SharePopuWindow(Activity context, int type, String path, IUiListener listener, IWeiXinListener weixinListener) {
		super(context, R.style.menutyle);
		this.context = context;
		this.setContentView(R.layout.activity_actions_lib_pre_view_share);
		Window dialogWindow = this.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.BOTTOM);
		int screenWidth = context.getWindowManager().getDefaultDisplay()
				.getWidth();
		int screenHeight = context.getWindowManager().getDefaultDisplay()
				.getHeight();
		Resources resources = context.getResources();
		int resourceId = resources.getIdentifier("navigation_bar_height",
				"dimen", "android");
		if (resourceId > 0) {
			screenHeight = screenHeight
					- resources.getDimensionPixelSize(resourceId);// 减去底部操作栏的高度
		}
		if (screenWidth < screenHeight) {
			lp.width = screenWidth;
			lp.height = (int) (screenHeight * (5.0 / 10.0));
		} else {
			lp.width = (int) (screenWidth * (4.0 / 5.0));
			lp.height = (int) (screenHeight * (3.0 / 4.0));
		}
		dialogWindow.setAttributes(lp);
		btn_cancel_share = (Button) findViewById(R.id.btn_cancel_share);
		btn_to_qq = (ImageButton) findViewById(R.id.btn_to_qq);
		btn_to_kongjian = (ImageButton) findViewById(R.id.btn_to_kongjian);
		btn_to_weixin = (ImageButton) findViewById(R.id.btn_to_weixin);
		btn_to_pengyouquan = (ImageButton) findViewById(R.id.btn_to_pengyouquan);
		btn_to_weibo = (ImageButton) findViewById(R.id.btn_to_weibo);
		btn_to_twitter = (ImageButton)findViewById(R.id.btn_to_twitter);
		btn_to_facebook = (ImageButton)findViewById(R.id.btn_to_facebook);
		this.setCanceledOnTouchOutside(true);
		btn_cancel_share.setOnClickListener(this);
		btn_to_qq.setOnClickListener(this);
		btn_to_kongjian.setOnClickListener(this);
		btn_to_weixin.setOnClickListener(this);
		btn_to_pengyouquan.setOnClickListener(this);
		btn_to_weibo.setOnClickListener(this);
		btn_to_facebook.setOnClickListener(this);
		btn_to_twitter.setOnClickListener(this);
		this.listener = listener;
		this.mweixinListener = weixinListener;
		this.type = type;
		imagePath = path;

	}
	public SharePopuWindow(Activity context, int type, String path , boolean isFromLocal, IUiListener listener, IWeiXinListener weixinListener) {
		super(context, R.style.menutyle);
		this.context = context;
		this.isFromLocal = isFromLocal;
		imagePath = path;
		this.setContentView(R.layout.local_pic_share);
		Window dialogWindow = this.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.BOTTOM);
		int screenWidth = context.getWindowManager().getDefaultDisplay()
				.getWidth();
		int screenHeight = context.getWindowManager().getDefaultDisplay()
				.getHeight();
		Resources resources = context.getResources();
		int resourceId = resources.getIdentifier("navigation_bar_height",
				"dimen", "android");
		if (resourceId > 0) {
			screenHeight = screenHeight
					- resources.getDimensionPixelSize(resourceId);// 减去底部操作栏的高度
		}
		if (screenWidth < screenHeight) {
			lp.width = screenWidth;
			lp.height = (int) (screenHeight * (5.0 / 10.0));
		} else {
			lp.width = (int) (screenWidth * (4.0 / 5.0));
			lp.height = (int) (screenHeight * (3.0 / 4.0));
		}
		dialogWindow.setAttributes(lp);
		btn_cancel_share = (Button) findViewById(R.id.btn_cancel_share);
		btn_to_qq = (ImageButton) findViewById(R.id.btn_to_qq);

		btn_to_weixin = (ImageButton) findViewById(R.id.btn_to_weixin);
		btn_to_pengyouquan = (ImageButton) findViewById(R.id.btn_to_pengyouquan);
		this.setCanceledOnTouchOutside(true);
		btn_cancel_share.setOnClickListener(this);
		btn_to_qq.setOnClickListener(this);
		btn_to_weixin.setOnClickListener(this);
		btn_to_pengyouquan.setOnClickListener(this);
		this.listener = listener;
		this.mweixinListener = weixinListener;
		this.type = type;

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
	
		case R.id.btn_cancel_share:
			this.dismiss();
			break;
		case R.id.btn_to_qq:
			if(isFromLocal){
				TencentQQManager.doSharePicToQQ(context, listener, imagePath);
			}else{
				if(type == SHARE_TYPE_ACTION){
					ActionDetail mAction = (ActionDetail)mObj;
					TencentQQManager.doShareToQQ(context, listener, type, mAction.getActionId(), mAction.getActionName(),
							mAction.getActionDesciber(), mAction.getActionImagePath());
				}else if(type ==SHARE_TYPE_APP){
					AppDetail appInfo= (AppDetail)mObj;
					TencentQQManager.doShareToQQ(context, listener,type,appInfo.getAppId(),appInfo.getAppName(),
							appInfo.getAppDesciber(),appInfo.getAppHeadImage());
				}else{
					TencentQQManager.doShareToQQ(context, listener,type,0,imagePath,
							imagePath,imagePath);
				}	
			}
			
			this.dismiss();
			break;
		case R.id.btn_to_kongjian:
			if(isFromLocal){
				
			}else{
				if(type==SHARE_TYPE_ACTION){
					ActionDetail mAction = (ActionDetail)mObj;
					TencentQQManager.doShareQQKongjian(context, listener, type, mAction.getActionId(), mAction.getActionName(),
							mAction.getActionDesciber(), mAction.getActionImagePath());
				}else if(type ==SHARE_TYPE_APP){
					AppDetail  appInfo= (AppDetail)mObj;
					TencentQQManager.doShareQQKongjian(context, listener,type,appInfo.getAppId(),appInfo.getAppName(),
							appInfo.getAppDesciber(),appInfo.getAppHeadImage());
				}else{
					TencentQQManager.doShareQQKongjian(context, listener,type,0,imagePath,
							imagePath,imagePath);
				}	
			}
			this.dismiss();
			break;
		case R.id.btn_to_weixin:
			if(isFromLocal){
				WeiXinManager.doSharePicToWeiXin(context, mweixinListener, 0, imagePath);
			}else{
				if(type==SHARE_TYPE_ACTION){
					ActionDetail mAction = (ActionDetail)mObj;
					WeiXinManager.doShareToWeiXin(context, mweixinListener, 0, type, mAction.getActionName(),

							mAction.getActionDesciber(), mAction.getActionId(),mAction.getActionImagePath());
				}else if(type ==SHARE_TYPE_APP){

					AppDetail  appInfo= (AppDetail)mObj;
					WeiXinManager.doShareToWeiXin(context, mweixinListener, 0, type, appInfo.getAppName(),
							appInfo.getAppDesciber(), appInfo.getAppId(),appInfo.getAppHeadImage());
				}else{
					WeiXinManager.doShareToWeiXin(context, mweixinListener, 0, type, imagePath,
							imagePath,0,"" );
				}
			
			}
			this.dismiss();
		
			break;
		case R.id.btn_to_pengyouquan:
			if(isFromLocal){
				WeiXinManager.doSharePicToWeiXin(context, mweixinListener, 1, imagePath);
			}else{
				if(type==SHARE_TYPE_ACTION){
					ActionDetail mAction = (ActionDetail)mObj;
					WeiXinManager.doShareToWeiXin(context, mweixinListener, 1, type, mAction.getActionName(),

							mAction.getActionDesciber(), mAction.getActionId(),mAction.getActionImagePath());
				}else if(type ==SHARE_TYPE_APP){

					AppDetail  appInfo= (AppDetail)mObj;
					WeiXinManager.doShareToWeiXin(context, mweixinListener, 1, type, appInfo.getAppName(),
							appInfo.getAppDesciber(), appInfo.getAppId(),appInfo.getAppHeadImage());
				}else{
					WeiXinManager.doShareToWeiXin(context, mweixinListener, 1, type, imagePath,
							imagePath,0,"" );
				}
			}
			
			this.dismiss();
			break;
		case R.id.btn_to_facebook:
			if(isFromLocal){
				
			}else{
				if(type==SHARE_TYPE_ACTION){
					ActionDetail mAction = (ActionDetail)mObj;
					FaceBookManager.doShareFaceBook(context, mAction.getActionName(), mAction.getActionDesciber(),mAction.getActionId(),type);
				}else if(type ==SHARE_TYPE_APP){
					AppDetail  appInfo= (AppDetail)mObj;

					FaceBookManager.doShareFaceBook(context, appInfo.getAppName(), appInfo.getAppDesciber(),appInfo.getAppId(),type);
				}else{
					FaceBookManager.doShareFaceBook(context, imagePath, imagePath,0,type);
				}
			}
			
			this.dismiss();
		
			break;
		case R.id.btn_to_twitter:
			if(isFromLocal){
				
			}else{
				if(type==SHARE_TYPE_ACTION){
					ActionDetail mAction = (ActionDetail)mObj;
					TwitterManager.doShareTwitter(context, mAction.getActionName(), mAction.getActionDesciber(),mAction.getActionId(),type);
				}else if(type ==SHARE_TYPE_APP){
					AppDetail  appInfo= (AppDetail)mObj;
					TwitterManager.doShareTwitter(context, appInfo.getAppName(), appInfo.getAppDesciber(),appInfo.getAppId(),type);
				}else{
					TwitterManager.doShareTwitter(context, imagePath, imagePath,0,type);
				}
			}
			
			this.dismiss();
			break;
		default:
			break;
		}

	}

}
