package com.alpha2.camera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtil {
	
	public static final int MEDIA_TYPE_IMAGE = 1;
	private static final String TAG = "FileUtil";
	private static String DIR_PIC =  "ubtech/Camera";
	public static Uri getOutputMediaFileUri(int type,Context mContext){
	      return Uri.fromFile(getOutputMediaFile(type,mContext));
	}

	@SuppressLint("ShowToast")
	public static File getOutputMediaFile(int type,Context mContext){
		File mediaStorageDir = new File(getSDCardPath(), DIR_PIC);
		if (! mediaStorageDir.exists()){
			if (! mediaStorageDir.mkdirs()){
				Log.e(TAG, "failed to create directory in ExternalStorageDirectory");
				return null;
			}
		}
		Log.d(TAG,"路径为"+mediaStorageDir);

	    String timeStamp =  new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile;
	    if (type == MEDIA_TYPE_IMAGE){
			String filePath = mediaStorageDir.getPath() + File.separator +
					"IMG_"+ timeStamp + ".jpg";
	        mediaFile = new File(filePath);
			Log.d(TAG,"文件路径："+ filePath);
	    } else {
	        return null;
	    }

	    return mediaFile;
	}

	public static String getSDCardPath() {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())
				|| !Environment.isExternalStorageRemovable()) {
			return Environment.getExternalStorageDirectory().getAbsolutePath();
		} else {
			return getPhoneCardPath();
		}

	}

	/**
	 * 获取手机自身内存路径
	 *
	 */
	public static String getPhoneCardPath() {
		return Environment.getDataDirectory().getPath();

	}
}
