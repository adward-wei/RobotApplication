package com.ubtechinc.alpha2ctrlapp.util.ImageLoad;

/**
 * Title:图片处理相关类
 * Description:
 * Copyright:Copyright(c) 2012
 * Company:走吧网
 * 
 * @author tom_liang
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import com.ubtechinc.alpha2ctrlapp.R;

import java.io.ByteArrayOutputStream;

import Decoder.BASE64Encoder;

public class PictureHandle {
	/**
	 *     获取圆角图片方法
	 * @paramroundPx
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Activity activity,Drawable drawable,int type, int width, float roundPx) {	
		
		Bitmap bitmap = zoomImage(activity, drawable, width, width,type);
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(),bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	public static Bitmap getRoundedCorner(Bitmap bitmap,int type, float roundPx) {	
		
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(),bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	/**
	 * drawable转换为bitmap
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Context context,Drawable drawable,int type) {
		if (drawable == null) {
			if(type ==0)
			drawable = context.getResources().getDrawable(R.drawable.no_head);
			if(type ==1)
				drawable = context.getResources().getDrawable(R.drawable.no_app);
			if(type ==2)
				drawable = context.getResources().getDrawable(R.drawable.no_action);
		}
		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),	drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888: Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

       /**
        *  回收内存
   * @param bitmap
        */
	public static void recycleBitmap(Bitmap bitmap) {
		if (!bitmap.isRecycled()) {
			 // 回收图片所占的内存
			bitmap.recycle();
			// 提醒系统及时回收
			System.gc();
		}
	}

	/**
	 * drawable头像转换为bitmap
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableHeaderToBitmap(Context activity,Drawable drawable,int type) {
		if (drawable == null) {
			if(type ==0)
			drawable = activity.getResources().getDrawable(R.drawable.no_head);
			if(type ==1)
				drawable = activity.getResources().getDrawable(R.drawable.no_app);
			if(type ==2)
				drawable = activity.getResources().getDrawable(R.drawable.no_action);
		}
		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888: Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;

	}

	/**
	 *    图片压缩到指定大小
	 * @param bgimage
	 * @param newWidth
	 * @param newHeight
	 * @return
	 */
	public static Bitmap zoomImage(Context activity, Drawable drawale,double newWidth, double newHeight,int type) {
		Bitmap bgimage = drawableHeaderToBitmap(activity, drawale,type);
		// 获取这个图片的宽和高
		float width = bgimage.getWidth();
		float height = bgimage.getHeight();
		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();
		// 计算宽高缩放率
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,(int) height, matrix, true);
		return bitmap;
	}
	
	/**
	 *    图片压缩到指定大小
	 * @param bgimage
	 * @param newWidth
	 * @param newHeight
	 * @return
	 */
	public static Bitmap zoomappImage(Context activity, Drawable drawale,double newWidth, double newHeight) {
		Bitmap bgimage = drawableToBitmap(activity, drawale,1);
		// 获取这个图片的宽和高
		float width = bgimage.getWidth();
		float height = bgimage.getHeight();
		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();
		// 计算宽高缩放率
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,(int) height, matrix, true);
		return bitmap;
	}
	// 压缩后的最大大小（kb）
		private double MAX_SIZE = 20;

	public static String getImgStr(Bitmap img) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			img.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			byte[] img_bytes = baos.toByteArray();
			double the_rate = (400 * 1024) / (img_bytes.length * 8);
			if (the_rate < 1) {
				img.compress(Bitmap.CompressFormat.JPEG, (int) (the_rate * 100),
						baos);
				img_bytes = baos.toByteArray();
			}
			BASE64Encoder encoder = new BASE64Encoder();
			return encoder.encode(img_bytes).replace("\n", "");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
		
	}

}
