package com.ubtechinc.alpha.upload.photo;

import android.os.Environment;

/**
 * 相册相关的常量类
 * @author Administrator
 *
 */
public class TransferPhotoConstants {
	  /**获得缩略图的总数*/
      public static final int TYPE_GET_THUMBNAIL_PIC_TOTAL=1;
      /**批量删除图片*/
      public static final int TYPE_PATCH_DEL_PICS=2;
      /**根据缩略图获得大图*/
      public static final int TYPE_GET_PICS_BY_THUMBNAIL=3;
      /**发送所有缩略图到手机端*/
      public static final int TYPE_SEND_ALL_THUMB_PICS=4;
      /**拍照图片处理为缩略图后存放的路径*/
      public static final String DIR_PIC_THUMBNAIL= Environment.getExternalStorageDirectory()+"/ubtech/image/thumbs";
}
