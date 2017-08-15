package com.ubtechinc.alpha.upload.photo;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.ubtechinc.alpha.entity.TransferPhotoInfo;
import com.ubtechinc.alpha.entity.UploadFileUrl;
import com.ubtechinc.alpha.im.IMHeaderField;
import com.ubtechinc.alpha.im.IMsgResponseCallback;


import com.ubtechinc.alpha.provider.PhotoInfoVisitor;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 处理传输图片相关的业务处理类
 * @author wanghzhengtian
 *
 */
public class TransferPhotoInHttpImpl implements ITransferPhotoPresenter {
	 public static final String TAG=TransferPhotoInHttpImpl.class.getSimpleName();
	 private final static String PIC_BASE_DIRECTORY = "/storage/emulated/0/TestCameraFile/";
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
	 
	 private final static int MSG_UPLOAD_SUCCESS = 0;
	 private final static int MSG_UPLOAD_FAIL = 1;
	 
	 private Context mContext = null;

	 private static TransferPhotoInHttpImpl transferPhotoInHttpImplInstance;

     
	 private TransferPhotoInHttpImpl(Context context)
     {
		 mContext = context;
     }

	 public void setContext(Context context) {
		 mContext = context;
	 }

	 private static class TransferPhotoInHttpImplInstance {
    	 //public static TransferPhotoInHttpImpl INSTANCE = new TransferPhotoInHttpImpl();
     }

	 public static TransferPhotoInHttpImpl getInstance() {
    	 //return TransferPhotoInHttpImplInstance.INSTANCE;
		 return null;
     }

	 public static TransferPhotoInHttpImpl getInstance(Context context) {
		if(transferPhotoInHttpImplInstance == null) {
			transferPhotoInHttpImplInstance = new TransferPhotoInHttpImpl(context);
		}
		return transferPhotoInHttpImplInstance;
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
		 msgResponseCallback.onResult(headerField, info);
//		XmppMsgParseThread.sendData(pckId, sendTo, nID, info);
	}

	@Override
	public void handleRequestOfGetPicByThumbnail(String thumbFileName, IMsgResponseCallback msgResponseCallback, IMHeaderField headerField) {
		Log.e(TAG, "handleRequestOfGetPicByThumbnail+thumbFileName=="+thumbFileName);
  	  	File picFile=new File(thumbFileName);

		if(picFile==null || !picFile.exists()) {
			TransferPhotoInfo info=new TransferPhotoInfo();
			info.setType(3);
			info.setAmount(0);

			msgResponseCallback.onResult(headerField, info);
//			XmppMsgParseThread.sendData(pckId, sendTo, nID, info);
		} else {
			String fileName = filePath2FileName(picFile.getAbsolutePath());
			PhotoInfoVisitor provider = PhotoInfoVisitor.getInstance();
			PhotoInfo photoInfo = provider.query(fileName);
//			Alpha2Photo p = PhotoDao.query(mContext, fileName);
			if(photoInfo != null && !TextUtils.isEmpty(photoInfo.url)) {

				sendPhotoUrl(photoInfo.url, msgResponseCallback, headerField);

			} else {
//				uploadPhotoOnServer(picFile.getAbsolutePath(), msgResponseCallback, headerField);
			}
		}
	}

	@Override
	public void handleRequestOfSendAllThumbPics(List<String> picUrl, String path, IMsgResponseCallback msgResponseCallback, IMHeaderField headerField)
	{
		Log.e(TAG, "handleRequestOfSendAllThumbPics==>>>>");
		File[] thumbFiles=PhotoUtil.getFileArrOfThumbnail();
		if(thumbFiles==null||thumbFiles.length==0)
		{
			 Log.e(TAG, "thumbFiles==null||thumbFiles.length==0");
			return;
		}
		if(!TextUtils.isEmpty(path)&&ALL_FILE_REQUEST.equals(path))/**获取所有缩略图片的请求*/
		{
			toZipAndSendFileArrToClient(thumbFiles, msgResponseCallback, headerField);
			
		}else/**获取指定缩略图的请求*/
		{
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
			if(len>0)
			{
			  File[] desFiles=fileList.toArray(new File[len]);
			  toZipAndSendFileArrToClient(desFiles, msgResponseCallback, headerField);
			}
		}
	}

	private void toZipAndSendFileArrToClient(File[] fileArr, IMsgResponseCallback msgResponseCallback, IMHeaderField headerField)
	{
		String timeStamp =  new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String toZipFile=TransferPhotoConstants.DIR_PIC_THUMBNAIL+"_"+timeStamp+".zip";
		boolean zipResult=PhotoUtil.toZipFilesList(fileArr, toZipFile);
		Log.e("zip_result", "zipResult=="+zipResult);
		if(zipResult)
		{
			File file=new File(toZipFile);
			if(file.exists())
			{
//				uploadPhotoOnServer(file.getAbsolutePath(), msgResponseCallback, headerField);
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
/*
	private void uploadPhotoOnServer(String filePath, final IMsgResponseCallback msgResponseCallback, final IMHeaderField headerField) {
		final String fPath = filePath;
		UploadFileRequest  ufRequest = new UploadFileRequest();
		ufRequest.setSerialNumber(AlphaMainSeviceImpl.serialNumber);
		UserAction userAction = new UserAction(mContext, new ClientAuthorizeListener() {
			@Override
			public void onResult(int code, String info) {
				if(code == 1) {
					//TransferPhotoInfo tpInfo = new TransferPhotoInfo();
					//tpInfo.setType(20);
					//tpInfo.setPath(info);
					LogUtils.d("", "!!! url=" + info);

					String url = info;
					String fileName = filePath2FileName(fPath);

					sendPhotoUrl(url, msgResponseCallback, headerField);

					Alpha2Photo p = PhotoDao.query(mContext, fileName);
					if(p != null && !p.url.equals("")) {
						PhotoDao.update(mContext, fileName, p.url, fileName, url);
					} else {
						PhotoDao.insert(mContext, fileName, url);
					}
				} else {
					LogUtils.d("", "!!! uploadPhotoOnServer fail");
					String fileName = filePath2FileName(fPath);

					sendPhotoUrl("", msgResponseCallback, headerField);

					PhotoDao.insert(mContext, fileName, "");
				}
			}
		});

		userAction.setParamerObj(ufRequest);
		userAction.setFilePath(filePath);
		userAction.doRequest(NetWorkConstant.REQUEST_UPLOAD_PHOTO,
				NetWorkConstant.upload_photo);
	}
*/
/*	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String filePath = null;
			String fileName = null;
			switch(msg.what) {
			case MSG_UPLOAD_SUCCESS:
				ArrayList<String> list = (ArrayList<String>) msg.obj;
				filePath = list.get(0);
				String url = list.get(1);
				fileName = filePath2FileName(filePath);
				if(sendTo != null && !sendTo.equals("")) {
					sendPhotoUrl(url);
				}
				PhotoUrl p = PhotoDao.query(mContext, fileName);
				if(p != null && !p.getUrl().equals("")) {
					PhotoDao.update(mContext, fileName, p.getUrl(), fileName, url);
				} else {
					PhotoDao.insert(mContext, fileName, url);
				}
				break;
			case MSG_UPLOAD_FAIL:
				filePath = (String) msg.obj;
				fileName = filePath2FileName(filePath);
				sendPhotoUrl("");
				PhotoDao.insert(mContext, fileName, "");
				break;
			}
		}
		
	};
*/
	private String filePath2FileName(String filePath) {
		int length = filePath.length();
		int posi = filePath.lastIndexOf("/");
		String fileName = filePath.substring(posi+1);
		return fileName;
	}

	private void sendPhotoUrl(String url, IMsgResponseCallback msgResponseCallback, IMHeaderField headerField) {
		UploadFileUrl uploadFileUrl = new UploadFileUrl();
		uploadFileUrl.setFileUrl(url);

		msgResponseCallback.onResult(headerField, uploadFileUrl);
//		XmppMsgParseThread.sendData(pckId, sendTo, nID, uploadFileUrl);
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
