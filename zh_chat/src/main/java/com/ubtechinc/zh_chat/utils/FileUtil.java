package com.ubtechinc.zh_chat.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.ubtech.utilcode.utils.CloseUtils;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtil {
	private static final String TAG = "FileUtil";
	public static final int MEDIA_TYPE_IMAGE = 1;
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

	/**
	 * 字符串读取assets目录下的文件
	 *
	 * @param cxt Context
	 * @param file 文件名
	 * @param charset 字符集
	 * @return
	 */
	public static String readAssetsFile2String(Context cxt, String file, String charset){
		int len ;
		byte[] buf;
		String result = "";
		InputStream in = null;
		try {
			in = cxt.getAssets().open(file);
			len  = in.available();
			buf = new byte[len];
			int ret = in.read(buf, 0, len);
			if (ret == len)
				result = new String(buf, charset);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			CloseUtils.closeIOQuietly(in);
		}
		return result;
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
