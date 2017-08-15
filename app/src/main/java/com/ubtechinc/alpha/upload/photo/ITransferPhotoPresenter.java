package com.ubtechinc.alpha.upload.photo;

import com.ubtechinc.alpha.im.IMHeaderField;
import com.ubtechinc.alpha.im.IMsgResponseCallback;

import java.util.List;

public interface ITransferPhotoPresenter {
	  /**获得缩略图总数的请求*/
      public void handleRequestOfGetTotalThumbnail(IMsgResponseCallback msgResponseCallback, IMHeaderField headerField);
      /**处理批量删除图片的请求*/
      public void handleRequestOfPatchDelPic(List<String> picUrl, IMsgResponseCallback msgResponseCallback, IMHeaderField headerField);
      /**处理根据缩略图获取原图的请求*/
      public void handleRequestOfGetPicByThumbnail(String thumbFileName, IMsgResponseCallback msgResponseCallback, IMHeaderField headerField);
      /**发送所有缩略图的请求*/
      public void handleRequestOfSendAllThumbPics(List<String> picUrl, String path, IMsgResponseCallback msgResponseCallback, IMHeaderField headerField);
}
