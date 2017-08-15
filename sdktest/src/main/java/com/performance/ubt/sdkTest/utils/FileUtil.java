package com.performance.ubt.sdkTest.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtil {
	
	public static final int MEDIA_TYPE_IMAGE = 1;

	public static Uri getOutputMediaFileUri(int type,Context mContext){
	      return Uri.fromFile(getOutputMediaFile(type,mContext));
	}

	@SuppressLint("ShowToast")
	public static File getOutputMediaFile(int type,Context mContext){
		File mediaStorageDir;
		if(Environment.getExternalStorageState()!=null){
		    mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "ubtech/Camera");
		    if (! mediaStorageDir.exists()){
		        if (! mediaStorageDir.mkdirs()){
		            Log.d("MyCameraApp", "failed to create directory");
		            return null; 
		        }
		    }
		}else{
			 mediaStorageDir=new File(mContext.getCacheDir(),"TestCameraFile");
			 if (! mediaStorageDir.exists()){
			        if (! mediaStorageDir.mkdirs()){
			            Log.d("MyCameraApp", "failed to create directory");
			            return null;
			        }
			    }
			 Log.d("MyCameraApp","路径为"+mediaStorageDir);
		}
	    String timeStamp =  new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile;
	    if (type == MEDIA_TYPE_IMAGE){
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "IMG_"+ timeStamp + ".jpg");
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
}
