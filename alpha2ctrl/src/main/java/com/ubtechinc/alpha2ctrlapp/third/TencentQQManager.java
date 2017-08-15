package com.ubtechinc.alpha2ctrlapp.third;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.orhanobut.logger.Logger;
import com.tencent.connect.UserInfo;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.entity.QQLoginInfo;

import java.util.ArrayList;

/**
 * @ClassName TencentQQManager
 * @date 4/13/2017
 * @author tanghongyu
 * @Description 腾讯QQ逻辑处理（登录、分享）
 * @modifier
 * @modify_time
 */
public class TencentQQManager {
	private static final String TAG = "TencentQQManager";
	private final static String APP_ID = "1104953608";
	private final static String RIGHT = "all";
	private static Tencent mTencent = null;

	private static void initTencent(Context mContext) {
		if (mTencent == null)
			mTencent = Tencent.createInstance(APP_ID, mContext);
	}

	public static void doLogin(Activity act, IUiListener listener) {
		Logger.d(
				"com.ubtechinc.alpha2ctrlapp.business.thrid_party.TencentQQManager.doLogin");
		initTencent(act);
		mTencent.login(act, RIGHT, listener);
	}

	public static void getUserInfo(Activity act, QQLoginInfo info,
                                   IUiListener listener) {
		initTencent(act);
		mTencent.setOpenId(info.openid);
		mTencent.setAccessToken(info.access_token, info.expires_in + "");
		UserInfo userInfo = new UserInfo(act, mTencent.getQQToken());
		userInfo.getUserInfo(listener);
	}
	/**
	 * 分享到qq
	 * @param act 上下文环境
	 * @param listener 分享监听
	 * @param type 分享类型 1表示动作，2表示应用
	 * @param id 动作或者应用id
	 * @param name 分享名称
	 * @param des 分享描述
	 * @param imagePath 图片地址
	 */
	public static void doShareToQQ(Activity act, IUiListener listener, int type, int id, String name, String des, String imagePath) {
		if (mTencent == null) {
			initTencent(act);
		}
		final Bundle params = new Bundle();
		params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
		params.putString(QQShare.SHARE_TO_QQ_TITLE, name);
		if(type==1)	
		params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, Constants.SHARE_ACTION_URL+id);
		else if(type ==2)
			params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, Constants.SHARE_APP_URL+id);
		else if(type ==3)
			params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, imagePath);
		params.putString(QQShare.SHARE_TO_QQ_SUMMARY, des);
		params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imagePath);
		params.putString(QQShare.SHARE_TO_QQ_APP_NAME, act.getString(R.string.app_name));
		mTencent.shareToQQ(act, params, listener);

	}
	/**
	 * 分享到qq
	 * @param act 上下文环境
	 * @param listener 分享监听
	 * @param imagePath 图片地址
	 */
	public static void doSharePicToQQ(Activity act, IUiListener listener, String imagePath) {
		if (mTencent == null) {
			initTencent(act);
		}
		final Bundle params = new Bundle();
		params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,QQShare.SHARE_TO_QQ_TYPE_IMAGE);
		params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, imagePath);
		mTencent.shareToQQ(act, params, listener);

	}
	/**
	 * 分享到qq 空间
	 * @param act 上下文环境
	 * @param listener 分享监听
	 * @param type 分享类型 1表示动作，2表示应用
	 * @param id 动作或者应用id
	 * @param name 分享名称
	 * @param des 分享描述
	 * @param imagePath 图片地址
	 */
	public static void doShareQQKongjian(Activity act, IUiListener listener, int type, int id, String name, String des, String imagePath) {
		if (mTencent == null) {
			initTencent(act);
		}
		final Bundle params = new Bundle();

		params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,
				QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
		params.putString(QzoneShare.SHARE_TO_QQ_TITLE, name);
		params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, des);
		if(type==1)
			params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, Constants.SHARE_ACTION_URL+id);
		else if(type ==2)
			params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, Constants.SHARE_APP_URL+id);
		else if(type==3){
			params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, imagePath);
		}
		ArrayList<String> imageUrls = new ArrayList<String>();
		imageUrls.add(imagePath);
		params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);

		mTencent.shareToQzone(act, params, listener);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (null != mTencent)
		mTencent.onActivityResult(requestCode, resultCode, data);
		} 
}
