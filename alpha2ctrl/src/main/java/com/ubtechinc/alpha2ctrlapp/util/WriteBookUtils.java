package com.ubtechinc.alpha2ctrlapp.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WriteBookUtils {
	private static final int RESULT_TAKE_PICUTRE = 4;//拍照
	public static final int RESULT_CHOOSE_LOCAL_PITURE = 5;//选择本地照片
	private static final int RESULT_TAKE_VIDEO = 6;//拍摄视频
	private static final int RESULT_CHOOSE_LOCAL_VIDEO = 7;//选择本地视频
	private static final File MEDIA_DIR = new File(Environment.getExternalStorageDirectory()+ "/DCIM/Camera");
	/** 选择本地图片 **/
	public  static void   choosePic(Activity activity) {
		try {
			Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI);
			activity.startActivityForResult(intent,
					RESULT_CHOOSE_LOCAL_PITURE);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(activity, "没有相册", Toast.LENGTH_LONG).show();
		}
	}
	/** 选择本地视频方法 **/
	public  static void  chooseVideo(Activity activity) {
		try {
			Intent intent = new Intent(Intent.ACTION_PICK, null);
			intent.setType("video/*");
			activity.startActivityForResult(intent,RESULT_CHOOSE_LOCAL_VIDEO);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(activity, "没有视频", Toast.LENGTH_LONG).show();
		}
	}
//	/** 拍照方法* */
//	public  static void takePic(Activity activity) {
//		try {
//			String status = Environment.getExternalStorageState();
//			if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡
//				if (!MEDIA_DIR.exists()) {
//					MEDIA_DIR.mkdirs();// 创建照片的存储目录
//				}
//				String fileName = getPhotoFileName();
//				activity.setCurrentFile(new File(MEDIA_DIR, fileName));// 给新照的照片文件命名
//				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE,null);
//				intent.putExtra(MediaStore.EXTRA_OUTPUT,	Uri.fromFile(activity.getCurrentFile()));
//				activity.startActivityForResult(intent,RESULT_TAKE_PICUTRE);
//			} else {
//				Toast.makeText(activity, "没有SD卡", Toast.LENGTH_LONG)
//						.show();
//			}
//		} catch (ActivityNotFoundException e) {
//		}
//	}
//	/** 拍摄视频方法* */
//	public  static void takeVideo(Activity activity) {
//		try {
//			String status = Environment.getExternalStorageState();
//			if (status.equals(Environment.MEDIA_MOUNTED)) {
//				String path = Constants.MEDIA_PATH;// 创建文件夹存放视频
//				Log.i("RecordVideoActivity", "video path> "+ path);
//				if (!MEDIA_DIR.exists()) {
//					MEDIA_DIR.mkdirs();// 创建照片的存储目录
//				}
//				Intent intent = new Intent();
//				intent.setAction(MediaStore.ACTION_VIDEO_CAPTURE);
//				intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,	1); // 设置为低质量
//				intent.putExtra(MediaStore.EXTRA_OUTPUT,MEDIA_DIR);
//				activity.startActivityForResult(intent,RESULT_TAKE_VIDEO);
//			}
//		} catch (ActivityNotFoundException e) {
//		}
//	}
	/** 用当前时间给图片命名 **/
	public  static String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}

	/** 获取文件路径方法 **/
	public static String getPath(Activity activity, Uri contentUri) {
		String filePath = "";

		String[] projection = { MediaColumns.DATA };
		Cursor cursor = activity.managedQuery(contentUri, projection,
				null, null, null);

		if (cursor != null) {
			int columnIndex = cursor
					.getColumnIndexOrThrow(MediaColumns.DATA);
			cursor.moveToFirst();
			filePath = cursor.getString(columnIndex);
			try {
				// 4.0以上的版本会自动关闭 (4.0--14;; 4.0.3--15)
				if (Integer.parseInt(Build.VERSION.SDK) < 14) {
					cursor.close();
				}
			} catch (Exception e) {
			  e.printStackTrace();
			}
		}

		return filePath;
	}
	/**
	 *   获取本地图片（拍照图片）的缩略图
	 * @return
	 */
	public static Bitmap getBitmap(Uri uri ,Activity activity) {  
		   Bitmap bitmap = null;  
		    try {  
		        InputStream is = activity.getContentResolver().openInputStream(uri);
		        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();  
		        onlyBoundsOptions.inJustDecodeBounds = true; // // 不生成真正的点阵图像，只获得图像基本信息（默认为false）  
		        onlyBoundsOptions.inDither=true;  
		        onlyBoundsOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;  
		        BitmapFactory.decodeStream(is, null, onlyBoundsOptions);  
		        is.close();  
		        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1))  
		        	   return null;  
		        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;  
		                         
		           //计算缩放比例  
		   double ratio = (originalSize > 512) ? (originalSize / 512) : 1.0;  
		   BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();  
		   int  k = Integer.highestOneBit((int) Math.floor(ratio));  
		   if (k == 0)  
		       k=1;  
		   bitmapOptions.inSampleSize = k;  
		   bitmapOptions.inDither=true;//optional  
		   bitmapOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//optional  
		   is = activity.getContentResolver().openInputStream(uri); 
		   bitmap = BitmapFactory.decodeStream(is, null, bitmapOptions);  
		   is.close();  
		    return bitmap;  
		    } catch (IOException e) {  
		        e.printStackTrace();  
		    }  
		    return bitmap;  
		}  
}
