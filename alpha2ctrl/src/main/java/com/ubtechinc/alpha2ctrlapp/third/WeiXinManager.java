package com.ubtechinc.alpha2ctrlapp.third;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Date;
/**
 * @ClassName WeiXinManager
 * @date 4/13/2017
 * @author tanghongyu
 * @Description 微信登录逻辑类
 * @modifier
 * @modify_time
 */
public class WeiXinManager {

	private static final String TAG = "WeiXinManager";
	public final static String WEIXIN_APP_ID = "wxc8062c8f8d49fae4";
	public final static String WEIXIN_APP_SECRET = "d4624c36b6795d1d99dcf0547af5443d";
	private final static String SCOPE = "snsapi_userinfo";
	public final static String GRANTTYPE = "authorization_code";
	public static final String ACTION_WEIXIN_API_CALLBACK = "ACTION_WEIXIN_API_CALLBACK";
	// ---------------------------------------------------------
	private static long DO_LOGIN_REQUEST = -1;
	private static IWXAPI mIWXAPI = null;

	private static void initWeiXinAPI(Context mContext) {
		if (mIWXAPI == null) {
			mIWXAPI = WXAPIFactory.createWXAPI(mContext, WEIXIN_APP_ID, false);
			mIWXAPI.registerApp(WEIXIN_APP_ID);
		}
	}

	public static void doLogin(Context mContext, IWeiXinListener mListener) {

		initWeiXinAPI(mContext);
		if (!mIWXAPI.isWXAppInstalled()) {
			mListener.noteWeixinNotInstalled();
			return;
		}
		DO_LOGIN_REQUEST = new Date().getTime();
		SendAuth.Req req = new SendAuth.Req();
		req.scope = SCOPE;
		req.state = DO_LOGIN_REQUEST + "";
		Logger.i("com.ubtechic.alpha1blooth.thrid_party.WeiXinManager.doLogin："
						+ mIWXAPI.toString());
		mIWXAPI.sendReq(req);
	}

	public static SendAuth.Resp handleIntent(Intent inte, Context mContext) {
		initWeiXinAPI(mContext);
		SendAuth.Resp resp = new SendAuth.Resp(inte.getExtras());
		Logger.i("微信登录: 收到回调MyWeiXin.handleIntent：" + resp.errCode);
		if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
			Logger.i("微信登录: 收到回调，用户同意登录");
			return resp;
		}
		return null;
	}

	/**
	 * 分享到微信
	 * @param act
	 * @param mListener 监听
	 * @param flag 0 是微信，1是朋友圈
	 * @param type 1是动作，2是应用
	 * @param name 动作或者应用的名称
	 * @param des 动作或者应用的描述
	 * @param id 动作或者应用的id
	 * @param  imageUrl 应用图标或者动作图标
	 */
	public static void doShareToWeiXin(
            Activity act, IWeiXinListener mListener, int flag, int type, String name, String des, int id, String imageUrl) {
		initWeiXinAPI(act);
		if (!mIWXAPI.isWXAppInstalled()) {
			mListener.noteWeixinNotInstalled();
			return;
		}
		Logger.i("微信分享");
		WXWebpageObject webpage = new WXWebpageObject();
		if(type==1)
			webpage.webpageUrl = Constants.SHARE_ACTION_URL+id;
		else if(type ==2)
			webpage.webpageUrl = Constants.SHARE_APP_URL+id;
		else if(type ==3)
			webpage.webpageUrl = name;
		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = name;
		msg.description = des;
		Bitmap thumb =null;
		if(!TextUtils.isEmpty(imageUrl)){
//			 thumb = ImageLoader.getInstance(act).getBitmap(imageUrl,1);
		}
		if(thumb==null){
			 thumb = BitmapFactory.decodeResource(act.getResources(), R.drawable.alpha2_ic_launcher);
		}
		msg.setThumbImage(thumb);
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.message = msg;
		req.scene = flag;
		mIWXAPI.sendReq(req);

	}
	
	/**
	 * 分享图片到微信
	 * @param act
	 * @param mListener 监听
	 * @param flag 0 是微信，1是朋友圈
	 * @param imagePath 动作或者应用的名称
	 */
	public static void doSharePicToWeiXin(
            Activity act, IWeiXinListener mListener, int flag, String imagePath) {
		initWeiXinAPI(act);
		if (!mIWXAPI.isWXAppInstalled()) {
			mListener.noteWeixinNotInstalled();
			return;
		}
		Logger.i("微信分享图片");
		File file = new File(imagePath);
		if (!file.exists()) {
			Logger.i("文件不存在");
			return;
		}
		WXImageObject imgObj = new WXImageObject();
		imgObj.setImagePath(imagePath);
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = imgObj;
		Bitmap bmp = BitmapFactory.decodeFile(imagePath);
		Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 20, 20, true);
		bmp.recycle();
		msg.thumbData = bmpToByteArray(thumbBmp, true);
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("img");
		req.message = msg;
		req.scene = flag;	
		mIWXAPI.sendReq(req);
	
	}
	
	
	private static String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}
	
	
	
	
	public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}
		
		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	
	
	
	
	
	
	

}
