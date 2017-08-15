package com.ubtechinc.alpha.upload.photo;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.ubtech.utilcode.utils.FileUtils;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtech.utilcode.utils.thread.ThreadPool;
import com.ubtechinc.alpha.entity.TransferPhotoInfo;
import com.ubtechinc.alpha.entity.UploadFileUrl;
import com.ubtechinc.alpha.im.IMHeaderField;
import com.ubtechinc.alpha.im.IMsgResponseCallback;
import com.ubtechinc.alpha.provider.PhotoInfoVisitor;
import com.ubtechinc.alpha.upload.IUploadResultListener;
import com.ubtechinc.alpha.upload.IUploader;
import com.ubtechinc.alpha.upload.UploadCBHandler;
import com.ubtechinc.alpha.upload.UploadType;
import com.ubtechinc.alpha.upload.UrlUpload2SelfServer;
import com.ubtechinc.alpha.upload.qiniu.QiniuUploader;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 处理图片上传相关的业务处理类，上传到七牛
 * @author wanghzhengtian
 *
 */
public class PhotoUploadInHttpImpl implements ITransferPhotoPresenter, IUploadResultListener {
	 public static final String TAG = PhotoUploadInHttpImpl.class.getSimpleName();
	 private final static int UPLOAD_TYPE_PHOTO_FILE = 2;
	 private List<String> delPicUrls=new ArrayList<String>();

	 /**图片类型：原图*/
	 private final static int TYPE_PIC_ORIGINAL=0;
	 /**图片类型：缩略图*/
	 private final static int TYPE_PIC_THUMBNAIL=1;
	 /**获取所有缩略图的类型*/
	 private final static String ALL_FILE_REQUEST="1";

	 private Context mContext = null;


	 private static PhotoUploadInHttpImpl photoUploadInHttpImplInstance;
     
	 private PhotoUploadInHttpImpl(Context context)
     {
		 mContext = context;
     }

	 public void setContext(Context context) {
		 mContext = context;
	 }

	private static class PhotoUploadInHttpImplInstance {
    	 //public static TransferPhotoInHttpImpl INSTANCE = new TransferPhotoInHttpImpl();
     }

	 public static PhotoUploadInHttpImpl getInstance()
     {
    	 //return TransferPhotoInHttpImplInstance.INSTANCE;
		 return null;
     }

	 public static PhotoUploadInHttpImpl getInstance(Context context) {
		if(photoUploadInHttpImplInstance == null) {
			photoUploadInHttpImplInstance = new PhotoUploadInHttpImpl(context);
		}
		return photoUploadInHttpImplInstance;
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
		info.setDelPics(delPicUrls);/**所有存在的缩略图文件名集合*/

		 if(msgResponseCallback != null)
		 	msgResponseCallback.onResult(headerField, info);
//		XmppMsgParseThread.sendData(pckId, sendTo, nID, info);
		
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

		 if(msgResponseCallback != null)
			 msgResponseCallback.onResult(headerField, info);
//		XmppMsgParseThread.sendData(pckId, sendTo, nID, info);
	}

	@Override
	public void handleRequestOfGetPicByThumbnail(String thumbFilePath, IMsgResponseCallback msgResponseCallback, IMHeaderField headerField) {
		Log.e(TAG, "handleRequestOfGetPicByThumbnail+thumbFilePath=="+thumbFilePath);

		File picFile = PhotoUtil.getPicByThumbnail(thumbFilePath);

		if(!FileUtils.isFileExists(picFile)) {
			UploadFileUrl uploadFileUrl = new UploadFileUrl();
			uploadFileUrl.setFileUrl("");

			if(msgResponseCallback != null)
				msgResponseCallback.onResult(headerField, uploadFileUrl);
//			XmppMsgParseThread.sendData(pckId, sendTo, nID, info);
		} else {
			String fileName = filePath2FileName(picFile.getAbsolutePath());
			PhotoInfoVisitor provider = PhotoInfoVisitor.getInstance();
			PhotoInfo photoInfo = provider.query(fileName);
			if(photoInfo != null && !TextUtils.isEmpty(photoInfo.url)) {

				sendPhotoUrl(photoInfo.url, msgResponseCallback, headerField);

			} else {
				uploadPhoto2Server(picFile.getAbsolutePath(), msgResponseCallback, headerField);
			}
		}
	}

	@Override
	public void handleRequestOfSendAllThumbPics(List<String> picUrl, String path, IMsgResponseCallback msgResponseCallback, IMHeaderField headerField)
	{
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
				    if(file.getName().equals(picUrl.get(i)))
				    {
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
				//Alexa project DONOT USE THE UBTECH SERVER, USE QINIU SERVER TO STORAGE PICTURES BEGINNING
				uploadPhoto2Server(file.getAbsolutePath(), msgResponseCallback, headerField);
				//Alexa project DONOT USE THE UBTECH SERVER, USE QINIU SERVER TO STORAGE PICTURES ENDING
			}
		}
	}

	public static void testZip()
    {
    	String timeStamp =  new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String toZipFile=TransferPhotoConstants.DIR_PIC_THUMBNAIL+"_"+timeStamp+".zip";
		File[] thumbFiles=PhotoUtil.getFileArrOfThumbnail();
		boolean zipResult=PhotoUtil.toZipFilesList(thumbFiles, toZipFile);
		Log.e("zip_result", "zipResult=="+zipResult);
    }

	private void uploadPhoto2Server(String filePath, final IMsgResponseCallback msgResponseCallback, final IMHeaderField headerField){
		UploadCBHandler uploadCBHandler = new UploadCBHandler();
		uploadCBHandler.type = UploadType.TYPE_PHOTO;
		uploadCBHandler.filePath = filePath;
		uploadCBHandler.msgResponseCallback = msgResponseCallback;
		uploadCBHandler.headerField = headerField;

		IUploader qiniuUploader = QiniuUploader.get();
		qiniuUploader.upload(uploadCBHandler, this);
	}


	@Override
	public void onUploadSuccess(final String url, final UploadCBHandler uploadCBHandler) {
		if(!TextUtils.isEmpty(url)) {
			sendPhotoUrl(url, uploadCBHandler.msgResponseCallback, uploadCBHandler.headerField);

			ThreadPool.runOnNonUIThread(new Runnable() {
				@Override
				public void run() {
					UrlUpload2SelfServer.get().upload(url);
				}
			});

			//Brian DELETE THE PICTURE FROM ROBOT DEVICE BEGINNING
			ThreadPool.runOnNonUIThread(new Runnable() {
				@Override
				public void run() {
					deletePictureFromRobot(uploadCBHandler.filePath);
				}
			});

			PhotoInfoVisitor provider = PhotoInfoVisitor.getInstance();
			PhotoInfo photoInfo = new PhotoInfo();
			photoInfo.filePath = uploadCBHandler.filePath;
			photoInfo.url = url;

			provider.saveOrUpdate(photoInfo);

			writeLog("--------------end------------------");
		}
	}

	@Override
	public void onUploadFail(String respInfo, final UploadCBHandler uploadCBHandler) {
		sendPhotoUrl("", uploadCBHandler.msgResponseCallback, uploadCBHandler.headerField);

		writeLog(respInfo.toString());
		writeLog("--------------------------------");
	}


	private void writeLog(final String msg) {
				Log.d(TAG, msg);
				Log.d(TAG,"\r\n");
	}

	private void deletePictureFromRobot(String filePath){
		LogUtils.d("delete Picture "+filePath);
		File deleteFile = new File(filePath);
		deleteFile.delete();
	}


	private String filePath2FileName(String filePath) {
		int length = filePath.length();
		int posi = filePath.lastIndexOf("/");
		String fileName = filePath.substring(posi+1);
		return fileName;
	}

	private void sendPhotoUrl(String url, IMsgResponseCallback msgResponseCallback, IMHeaderField headerField) {
		UploadFileUrl uploadFileUrl = new UploadFileUrl();
		uploadFileUrl.setFileUrl(url);

		if(msgResponseCallback != null)
			msgResponseCallback.onResult(headerField, uploadFileUrl);
	}
/*
	public void uploadSurplusPhoto() {
		final ArrayList<String> list = PhotoDao.FindPicNotUpload(mContext);
		if(list != null) {
			new Thread() {
				@Override
				public void run() {
					for(String item : list) {
						String filePath = PIC_BASE_DIRECTORY + item;
						handleRequestOfGetPicByThumbnail(filePath, null, null);
						try {
							Thread.sleep(15000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();

		}
	}
*/


}
