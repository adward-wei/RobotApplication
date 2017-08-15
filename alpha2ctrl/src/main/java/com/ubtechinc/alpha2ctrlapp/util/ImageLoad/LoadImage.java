package com.ubtechinc.alpha2ctrlapp.util.ImageLoad;
/**
 * Title:包括图片，头像异步加载，和网络加载图片方法
 * Description:
 * Copyright:Copyright(c) 2012
 * Company:走吧网
 * 
 * @author tom_liang
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.widget.DetailImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class LoadImage {
	/**
	 * 异步加载图片
	 * @param image
	 * @param picureURL
	 */
	public static void LoadPicture(final Context context,final ImageView image, final String picureURL,final int type) {
		
		ImageLoader.getInstance(context).displayImage(picureURL, image, type);
		
		
//		Drawable cachedImage = DandanAsynchImageLoader.loadDrawable(picureURL,new DandanAsynchImageLoader.ImageCallback() {
//					public void imageLoaded(Drawable imageDrawable,String imageUrl) {
//						setImageDrawble(context, image, imageDrawable, type);
//					}
//				});
//		setImageDrawble(context, image, cachedImage, type);
	}
	/**
	 * 异步加载图片
	 * @param image
	 * @param picureURL
	 */
	public static void LoadAlumPicture(final Context context, final DetailImageView image, final String picureURL, final int type) {
		
		ImageLoader.getInstance(context).DisplayAlumImage(picureURL, image, type);
		
		
//		Drawable cachedImage = DandanAsynchImageLoader.loadDrawable(picureURL,new DandanAsynchImageLoader.ImageCallback() {
//					public void imageLoaded(Drawable imageDrawable,String imageUrl) {
//						setImageDrawble(context, image, imageDrawable, type);
//					}
//				});
//		setImageDrawble(context, image, cachedImage, type);
	}
	

	/**
	  *  异步加载头像
	 * @param activity
	 * @param image
	 * @param picureURL
	 */
	public static void LoadHeader(final Activity activity,final int whichActivity, final ImageView image,	final String picureURL) {
		ImageLoader.getInstance(activity).displayImage(picureURL, image, whichActivity);

	}
	/**
	  *  异步灰色加载头像
	 * @param activity
	 * @param image
	 * @param picureURL
	 */
	public static void LoadGrayHeader(final Activity activity, final ImageView image,	final String picureURL) {
		Bitmap grayBitmap = ImageLoader.getInstance(activity).grey(ImageLoader.getInstance(activity).getBitmap(picureURL,5));
		if(grayBitmap!=null)
			image.setImageBitmap(grayBitmap);
		else
			image.setImageDrawable(activity.getResources().getDrawable(R.drawable.no_robot));

	}
	/**
	  *  异步加载头像
	 * @param activity
	 * @param image
	 * @param picureURL
	 */
	public static void setRounderConner(final Activity activity, final ImageView image,	final String picureURL,final int type) {
//		Drawable cachedImage = DandanAsynchImageLoader.loadDrawable(picureURL,new DandanAsynchImageLoader.ImageCallback() {
//					public void imageLoaded(Drawable imageDrawable,String imageUrl) {
//						setRoundCornerImage(mMainPageActivity,image,imageDrawable,type);
//					}
//				});
//		setRoundCornerImage(mMainPageActivity, image,cachedImage,type);
		 ImageLoader.getInstance(activity).DisplayRounderImage(picureURL, image, type);

	}

	/**
	 *   加载网络图片
	 * @param url
	 * @return
	 */
	public static Bitmap getHttpBitmap(String url) {  
		Log.i("LoadImage ","getHttpBitmap url"+url);
		   
		    Bitmap bitmap = null;  
		    try {  
		        InputStream is = new URL(url).openStream();   
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
		   is = new URL(url).openStream();  
		   bitmap = BitmapFactory.decodeStream(is, null, bitmapOptions);  
		   is.close();  
		    return bitmap;  
		    } catch (IOException e) {  
		        e.printStackTrace();  
		    }  
		    return bitmap;  
		}  


}
