

package com.ubtechinc.alpha.download;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.ubtech.utilcode.utils.AppUtils;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtech.utilcode.utils.thread.ThreadPool;
import com.ubtechinc.alpha.im.IMHeaderField;
import com.ubtechinc.alpha.im.IMsgResponseCallback;
import com.ubtechinc.alpha.utils.Unzip;
import com.ubtechinc.nets.DownloadInfo;

import java.io.File;

import retrofit2.http.HEAD;

/**
 * [A brief description]
 *
 * @author zengdengyi
 * @version 1.0
 * @modifier: logic.peng
 **/

public class FileDownload implements IFileDownloadListener {
	private static final String TAG = "FileDownload";
	// download type 0：developer app 1：action file 2；alpha2s service
	public static final int DOWNLOAD_TYPE_APP = 0;
	public static final int DOWNLOAD_TYPE_ACTION_FILE = 1;
	public static final int DOWNLOAD_TYPE_ALPHA_SERVICE = 2;


	private Context mContext;
	private AppEntrityInfo mApp;

	private int mType;
	private ActionFileEntrity mActionEntity;

	private IMHeaderField headerField;
	private IMsgResponseCallback msgResponseCallback;
	private IFileDownLoadListener listener;
	private IDownloadStrategy downloadStrategy;

	public FileDownload(Context mContext) {
		this(mContext, 0);
	}

	public FileDownload(Context mContext, int type) {
		this(mContext,type,null,null);
	}

	public FileDownload(Context mContext, int type,
						IMHeaderField headerField, IMsgResponseCallback msgResponseCallback) {
		this.mContext = mContext;
		this.mType = type;
		this.headerField = headerField;
		this.msgResponseCallback = msgResponseCallback;
		downloadStrategy = new MixThreadDownloadStrategy(mContext,this);
		downloadStrategy.initDownload(true);
	}

	//test api
	public FileDownload(Context mContext, int type,
						IMHeaderField headerField, IMsgResponseCallback msgResponseCallback,int test) {
		this.mContext = mContext;
		this.mType = type;
		this.headerField = headerField;
		this.msgResponseCallback = msgResponseCallback;
		if(test ==1){
			downloadStrategy = new SingleThreadDownloadStrategy(mContext,this);
			downloadStrategy.initDownload(false);
		}
		else if(test == 2){
			downloadStrategy = new MixThreadDownloadStrategy(mContext,this);
			downloadStrategy.initDownload(false);
		}
		else if(test == 3){
			downloadStrategy = new MixThreadDownloadStrategy(mContext,this);
			downloadStrategy.initDownload(true);
		}
	}

	/**
	 * app 下载App
	 *
	 * @author zengdengyi
	 * @param app
	 */
	public void download(AppEntrityInfo app) {
		this.mApp = app;
		download(app.getUrl());
	}

	/**
	 *下载动作
	 * @param entrity
	 */
	public void download(ActionFileEntrity entrity) {
		this.mActionEntity = entrity;
		download(entrity.getActionFilePath());
	}

	public void download(String downUrl) {
		downloadStrategy.download(downUrl);
	}

	public void cancelRequest(String url) {
		downloadStrategy.cancel(url);
	}

	public void pauseRequest(String url){
		downloadStrategy.pause(url);
	}

	private void serviceType(final DownloadInfo info) {
		ThreadPool.runOnNonUIThread(new Runnable() {
			@Override
			public void run() {
				final boolean ret = AppUtils.installAppSilent(info.getFilePath());
				if (ret) {
					LogUtils.d(TAG,"install success...");
				}else {
					LogUtils.d(TAG, "install fail...");
				}
			}
		});
	}

	/**
	 * download app
	 *
	 * @author zengdengyi
	 * @param info
	 * @date 2016年5月23日 下午2:39:58
	 */
	public void appType(final DownloadInfo info) {
		if (mApp != null) {
			mApp.setDownLoadState(BusinessConstant.DOWNLOAD_STATE_SUCCESS);
			msgResponseCallback.onResult(headerField, mApp);
		}
		if (mApp != null) {
			ThreadPool.runOnNonUIThread(new Runnable() {
				@Override
				public void run() {
					final boolean ret = AppUtils.installAppSilent(info.getFilePath());
					if (ret) {
						LogUtils.d(TAG,"install success...");
						setNewInfo();

						mApp.setDownLoadState(BusinessConstant.INSTALL_STATE_INSTALL_SUCCESS);
						msgResponseCallback.onResult(headerField, mApp);
					}else {
						LogUtils.d(TAG, "install fail...");

						mApp.setDownLoadState( BusinessConstant.INSTALL_STATE_INSTALL_FAIL);
						msgResponseCallback.onResult(headerField, mApp);
					}
				}
			});
		}
	}

	/**
	 * downlaod action file
	 *
	 * @author zengdengyi
	 * @param result
	 * @date 2016年5月23日 下午2:40:18
	 */
	public void actionType(DownloadInfo result) {


		mActionEntity.setDownloadState(BusinessConstant.DOWNLOAD_STATE_SUCCESS);
		msgResponseCallback.onResult(headerField, mActionEntity);

		if (mActionEntity != null) {

			new Unzip(new Unzip.UnzipListener() {

				@Override
				public void onResult(int code, String msg) {
					if (code == 1) {//解压动作文件成功
						mActionEntity.setDownloadState(BusinessConstant.INSTALL_STATE_INSTALL_SUCCESS);
						// 重构先注释掉
						//EventUtil.postActionDownloadResult(mActionEntity, true, "");
						msgResponseCallback.onResult(headerField, mActionEntity);

					} else if(code == 0){//解压动作文件失败
						// 重构先注释掉
						// EventUtil.postActionDownloadResult(mActionEntity, false, TextUtils.isEmpty(msg) ? StatisticsConstant.REASON_IO_EXCEPTION : msg);

						mActionEntity.setDownloadState( BusinessConstant.INSTALL_STATE_INSTALL_FAIL);

						msgResponseCallback.onResult(headerField, mActionEntity);
					}else {
						// 重构先注释掉
						// EventUtil.postActionDownloadResult(mActionEntity, false, StatisticsConstant.REASON_FILE_IS_NOT_EXIST);
					}

				}
			}).unZip( result.getFilePath(), getUnzipPath());

		}
	}

	public String getUnzipPath() {
		int actionType = mActionEntity.getActionType();
		String subPath = "";
		return FilePath.actionsPath + subPath;

	}

	public void onUpdate(String path) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(path)),
				"application/vnd.android.package-archive");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		mContext.startActivity(intent);
	}

	public void setNewInfo() {

		PackageInfo packageInfo = null;

		try {

			packageInfo = mContext.getPackageManager().getPackageInfo(
					mApp.getPackageName(), 0);

		} catch (NameNotFoundException e) {

			e.printStackTrace();

		}
		if (packageInfo == null)
			return;
		ApplicationInfo appInfo = null;
		try {
			appInfo = mContext.getPackageManager().getApplicationInfo(
					packageInfo.packageName, PackageManager.GET_META_DATA);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (appInfo == null)
			return;
		Bundle meta = appInfo.metaData;
		if (meta == null)
			return;
		String appKey = meta.getString("alpha2_appid");
		if (!TextUtils.isEmpty(appKey)) {

			// AppEntrityInfo bean = new AppEntrityInfo();
			mApp.setPackageName(packageInfo.packageName);
			mApp.setName(packageInfo.applicationInfo.loadLabel(
					mContext.getPackageManager()).toString());
			mApp.setAppKey(appKey);
			mApp.setVersionCode("" + packageInfo.versionCode);
			mApp.setVersionName(packageInfo.versionName);

			String config = meta.getString("alpha2_appconfig");
			if (config != null) {
				if (config.equals("config")) {
					mApp.setSetting(true);
				}
			}
			String buttonEvent = meta.getString("alpha2_buttonevent");
			if (buttonEvent != null) {//
				if (buttonEvent.equals("buttonevent")) {
					mApp.setButtonEvent(true);
				}

			}
		}

	}

	public void setListener(IFileDownLoadListener listener) {
		this.listener = listener;
	}

	@Override
	public void onCompleted(DownloadInfo info) {
		LogUtils.d(TAG, "onSuccess = " + info.getFilePath());
		switch (mType) {
			case DOWNLOAD_TYPE_APP:
				if (listener != null) {
					listener.onResult(true, info.getFilePath());
					return;
				}
				appType(info);
				break;
			case DOWNLOAD_TYPE_ACTION_FILE:
				actionType(info);
				break;
			case DOWNLOAD_TYPE_ALPHA_SERVICE:
				serviceType(info);
				break;
			default:
				break;
		}
	}

	@Override
	public void onError(Throwable e) {
		if (mApp != null) {
			mApp.setDownLoadState( BusinessConstant.DOWNLOAD_STATE_FAIL);
			msgResponseCallback.onResult(headerField, mApp);
		}
		if (mActionEntity != null) {
			mActionEntity.setDownloadState(BusinessConstant.DOWNLOAD_STATE_FAIL);
			msgResponseCallback.onResult(headerField, mActionEntity);
		}
	}

	@Override
	public void onProcess(int progress) {
		if (progress % 5 == 0) {
			LogUtils.d(TAG, "onProgress " + progress);
			if(mApp != null) {

				mApp.setDownLoadState( BusinessConstant.DOWNLOAD_STATE_DOWNLOADING);
				mApp.setDownloadProgress(progress);
				msgResponseCallback.onResult(headerField, mApp);
			}


		}

	}

	public interface IFileDownLoadListener {
		 void onResult(boolean ret, String info);
	}

}