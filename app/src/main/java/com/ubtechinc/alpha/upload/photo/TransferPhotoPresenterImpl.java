package com.ubtechinc.alpha.upload.photo;

import android.text.TextUtils;
import android.util.Log;

import com.ubtechinc.alpha.entity.TransferPhotoInfo;
import com.ubtechinc.alpha.entity.UploadFileUrl;
import com.ubtechinc.alpha.im.IMHeaderField;
import com.ubtechinc.alpha.im.IMsgResponseCallback;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 处理传输图片相关的业务处理类
 * @author Administrator
 *
 */
public class TransferPhotoPresenterImpl implements ITransferPhotoPresenter {
	 public static final String TAG=TransferPhotoPresenterImpl.class.getSimpleName();
	 private final static int UPLOAD_TYPE_PHOTO_FILE = 2;
	 private List<String> delPicUrls=new ArrayList<String>();
	 /**主动传文件类型*/
	 private final static int TYPE_TRANSFER_FILE_ACTIVE=0; 
	 /**被动传文件类型*/
	 private final static int TYPE_TRANSFER_FILE_UNACTIVE=1;
	 /**图片类型：原图*/
	 private final static int TYPE_PIC_ORIGINAL=0; 
	 /**图片类型：缩略图*/
	 private final static int TYPE_PIC_THUMBNAIL=1;  
	 /**获取所有缩略图的类型*/
	 private final static String ALL_FILE_REQUEST="1";
     
	 private TransferPhotoPresenterImpl() {
    	 
     }


     private static class TransferPhotoPresenterImplInstance {
    	 private static TransferPhotoPresenterImpl INSTANCE=new TransferPhotoPresenterImpl();
     }


     public static TransferPhotoPresenterImpl getInstance() {
    	 return TransferPhotoPresenterImplInstance.INSTANCE;
     }


	@Override
	public void handleRequestOfGetTotalThumbnail(IMsgResponseCallback msgResponseCallback, IMHeaderField headerField) {
		Log.i("ken", "handleRequestOfGetTotalThumbnail");
		int amount=0;
		delPicUrls.clear();
		File[] thumbFiles=PhotoUtil.getFileArrOfThumbnail();
		if(thumbFiles==null||thumbFiles.length==0) {
			 Log.e(TAG, "thumbFiles==null||thumbFiles.length==0");
			 amount=0;
		} else {
		     amount=thumbFiles.length;
		     for (File file : thumbFiles) {
		    	Log.e("ken", "GetTotal+file.getName()="+file.getName());
				delPicUrls.add(file.getName());
			 }
		}
		Log.e("ken", "amount="+amount);
		TransferPhotoInfo info=new TransferPhotoInfo();
		info.setType(1);
		info.setAmount(amount);
		info.setPath("");
		info.setDelPics(delPicUrls);/**所有存在的缩略图文件名集合*/

		msgResponseCallback.onResult(headerField, info);
		//XmppMsgParseThread.sendData(pckId, sendTo, nID, info);
	}


	@Override
	public void handleRequestOfPatchDelPic(List<String> picUrls, IMsgResponseCallback msgResponseCallback, IMHeaderField headerField) {
		Log.e(TAG, "handleRequestOfPatchDelPic");
		boolean result=false;
		delPicUrls.clear();
		for (String url : picUrls) {
			Log.e("ken", "handleRequestOfPatchDelPic+url="+url);
			result=PhotoUtil.delPicByThumbnail(url);
			if(result) {
				delPicUrls.add(url);
			}
		}
		
		TransferPhotoInfo info=new TransferPhotoInfo();
		info.setType(2);
		/**ken*/
		info.setDelPics(delPicUrls);
		info.setPath("");
		msgResponseCallback.onResult(headerField, info);
//		XmppMsgParseThread.sendData(pckId, sendTo, nID, info);
	}


	@Override
	public void handleRequestOfGetPicByThumbnail(String thumbFileName, IMsgResponseCallback msgResponseCallback, IMHeaderField headerField) {
		Log.e(TAG, "handleRequestOfGetPicByThumbnail+thumbFileName=="+thumbFileName);
		File picFile=PhotoUtil.getPicByThumbnail(thumbFileName);
		if(picFile==null) {
			UploadFileUrl uploadFileUrl = new UploadFileUrl();
			uploadFileUrl.setFileUrl("");

			msgResponseCallback.onResult(headerField, uploadFileUrl);
//			XmppMsgParseThread.sendData(pckId, sendTo, nID, info);
		} else {
//			UploadFileThread sendPhoto = xmpt.new UploadFileThread(UPLOAD_TYPE_PHOTO_FILE, picFile.getAbsolutePath(), sendTo,TYPE_TRANSFER_FILE_UNACTIVE+","+TYPE_PIC_ORIGINAL);
//			sendPhoto.start();
		}
	}


	@Override
	public void handleRequestOfSendAllThumbPics(List<String> picUrl, String path, IMsgResponseCallback msgResponseCallback, IMHeaderField headerField) {
		Log.e(TAG, "handleRequestOfSendAllThumbPics==>>>>");
		File[] thumbFiles=PhotoUtil.getFileArrOfThumbnail();
		if(thumbFiles==null||thumbFiles.length==0) {
			 Log.e(TAG, "thumbFiles==null||thumbFiles.length==0");
			return;
		}
		if(!TextUtils.isEmpty(path)&&ALL_FILE_REQUEST.equals(path))/**获取所有缩略图片的请求*/ {
			toZipAndSendFileArrToClient(thumbFiles, msgResponseCallback, headerField);
			
		} else {
			/**获取指定缩略图的请求*/
			ArrayList<File> fileList=new ArrayList<File>();
			for (int i = 0; i < picUrl.size(); i++) {
				for (File file : thumbFiles) {
				    if(file.getName().equals(picUrl.get(i))) {
				    	Log.e(TAG, "SendAllThumbPics++file.getName()=="+file.getName());
				    	fileList.add(file);
				    }
				}
			}
			int len=fileList.size();
			if(len>0) {
			  File[] desFiles=fileList.toArray(new File[len]);
			  toZipAndSendFileArrToClient(desFiles, msgResponseCallback, headerField);
			}
		}
	}


	private void toZipAndSendFileArrToClient(File[] fileArr, IMsgResponseCallback msgResponseCallback, IMHeaderField headerField) {
		String timeStamp =  new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String toZipFile=TransferPhotoConstants.DIR_PIC_THUMBNAIL+"_"+timeStamp+".zip";
		boolean zipResult=PhotoUtil.toZipFilesList(fileArr, toZipFile);
		Log.e("zip_result", "zipResult=="+zipResult);
		if(zipResult) {
			File file=new File(toZipFile);
			if(file.exists()) {
//				UploadFileThread sendPhoto = xmpt.new UploadFileThread(UPLOAD_TYPE_PHOTO_FILE, file.getAbsolutePath(), sendTo,TYPE_TRANSFER_FILE_UNACTIVE+","+TYPE_PIC_THUMBNAIL);
//				sendPhoto.start();
			}
		}
	}
    public static void testZip() {
    	String timeStamp =  new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String toZipFile=TransferPhotoConstants.DIR_PIC_THUMBNAIL+"_"+timeStamp+".zip";
		File[] thumbFiles=PhotoUtil.getFileArrOfThumbnail();
		boolean zipResult=PhotoUtil.toZipFilesList(thumbFiles, toZipFile);
		Log.e("zip_result", "zipResult=="+zipResult);
    }
   
}
