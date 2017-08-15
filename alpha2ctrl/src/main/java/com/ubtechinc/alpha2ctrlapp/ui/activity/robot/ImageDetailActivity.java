package com.ubtechinc.alpha2ctrlapp.ui.activity.robot;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.base.BaseHandler;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.constants.MessageType;
import com.ubtechinc.alpha2ctrlapp.constants.NetWorkConstant;
import com.ubtechinc.alpha2ctrlapp.constants.SocketCmdId;
import com.ubtechinc.alpha2ctrlapp.data.robot.IRobotGalleryDataSource;
import com.ubtechinc.alpha2ctrlapp.data.robot.RobotGalleryRepository;
import com.ubtechinc.alpha2ctrlapp.database.NoticeManager;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.DownLoadStatus;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.ImageModel;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.TransferPhotoInfo;
import com.ubtechinc.alpha2ctrlapp.entity.request.DeletephotoRequest;
import com.ubtechinc.alpha2ctrlapp.third.IWeiXinListener;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseContactActivity;
import com.ubtechinc.alpha2ctrlapp.ui.adapter.robot.ScanPicAdpeter;
import com.ubtechinc.alpha2ctrlapp.util.FileReceive;
import com.ubtechinc.alpha2ctrlapp.util.ImageLoad.ImageLoader;
import com.ubtechinc.alpha2ctrlapp.widget.ScanPicGalley;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.DeletePhotoDiaglog;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.LoadingDialog;
import com.ubtechinc.alpha2ctrlapp.widget.popWindow.SharePopuWindow;
import com.ubtechinc.nets.http.ThrowableWrapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @ClassName ImageDetailActivity
 * @date 7/6/2017
 * @author tanghongyu
 * @Description 机器人图片详情页
 * @modifier
 * @modify_time
 */
public class ImageDetailActivity extends BaseContactActivity implements View.OnClickListener,DeletePhotoDiaglog.OnConfirmListener, IUiListener,IWeiXinListener {
	private RelativeLayout editLay;
	private DeletePhotoDiaglog deleteDialog;
	private NoticeManager noticeManager;
	private FileReceive mFileReceiVe;
	private TextView  btn_choice;
	private boolean isEdit =false;

	public static final int TO_GET_PHOTO=0;
	private ImageButton btn_share,btn_delete,btn_download;
	private Map<String , DownLoadStatus>downLoadMap = new HashMap<String, DownLoadStatus>();
	private List<ImageModel>picList = new ArrayList<ImageModel>();
	private ScanPicAdpeter adpeter  ;
	private ScanPicGalley galley;
	private String imageName;
	private SharePopuWindow shareDialog;
	private int position;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);  
		setContentView(R.layout.image_detail);
		initView();
	}
	
	private void initView() {
		imageName = getIntent().getStringExtra("imageName");
		picList = (List<ImageModel>)getIntent().getSerializableExtra("picList");
		position = getIntent().getIntExtra("pisition", -1);
		btn_choice = (TextView)findViewById(R.id.btn_top_right);
		btn_choice.setText(R.string.common_btn_edit);
		btn_choice.setVisibility(View.VISIBLE);
		btn_choice.setOnClickListener(this);
		editLay = (RelativeLayout)findViewById(R.id.btn_deleteLay);
		this.title = (TextView) findViewById(R.id.authorize_title);
		title.setText(getString(R.string.image_galley));
		noticeManager = NoticeManager.getInstance();
		mFileReceiVe = FileReceive.getInstance(mContext, noticeManager,mHandler);

		btn_share = (ImageButton)findViewById(R.id.btn_share_icon);
		btn_delete = (ImageButton)findViewById(R.id.btn_delete_icon);
		btn_download = (ImageButton)findViewById(R.id.btn_download_icon);
		btn_share.setOnClickListener(this);
		btn_delete.setOnClickListener(this);
		btn_download.setOnClickListener(this);
		int position=0;
		for(int i =0;i<picList.size();i++){
			DownLoadStatus ds = new DownLoadStatus();
			ds.setDownloadProgress("0.0%");
			ds.setDownloadStaStatus(0);
				downLoadMap.put(picList.get(i).getImage_id(), ds);
			if(picList.get(i).getImage_id().equals(imageName)){
				position=i;
			}
		}

		galley = (ScanPicGalley)findViewById(R.id.galley);
		adpeter = new ScanPicAdpeter(mContext, picList, downLoadMap,mHandler);
		galley.setAdapter(adpeter);
		galley.setSelection(position);
		galley.setOnItemSelectedListener(adpeter);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	public final BaseHandler mHandler = new BaseHandler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TO_GET_PHOTO:
				String fileName = (String)msg.obj;
				getPhoto(3,fileName);
				break;
			case MessageType.ALPHA_GET_IMAGE_DETAIL:{
				FlieTransInfo info =  (FlieTransInfo)msg.obj;
				if(info!=null){
					if(downLoadMap.get(info.getFileName())!=null){
						DownLoadStatus status = new DownLoadStatus();
						status.setDownloadProgress((int)(info.getFileProgress()*100)+"%");
						status.setDownloadStaStatus(1);
						downLoadMap.put(info.getFileName(), status);
					}
					Logger.i("progress: p"+info.getFileProgress());
					adpeter.notifyDataSetChanged();
				
				}
				
			}	
				break;

			case MessageType.ALPHA_GET_IMAGE_DETAIL_FAILED:{
				FlieTransInfo info =  (FlieTransInfo)msg.obj;
				Logger.i("fileNxy: 失败"+info.getFileName());
				
				if(info!=null){
					if(downLoadMap.get(info.getFileName())!=null){
						DownLoadStatus status = new DownLoadStatus();
						status.setDownloadProgress("0.0%");
						status.setDownloadStaStatus(3);
						downLoadMap.put(info.getFileName(), status);
						
					}
					adpeter.notifyDataSetChanged();
				}
			}
				
				break;
			case SocketCmdId.ALPHA_MSG_RSP_TRANSFER_PHOTO:
				TransferPhotoInfo rspInfo  = (TransferPhotoInfo)msg.obj;
				if(rspInfo.getType()==3){
					if(rspInfo.getAmount()==0){
						LoadingDialog.dissMiss();
//						ToastUtils.showShortToast(  R.string.photo_no_exist);
						if(rspInfo!=null){
							if(downLoadMap.get(rspInfo.getPath().replace("_thum", ""))!=null){
								DownLoadStatus status = new DownLoadStatus();
								status.setDownloadProgress("0.0%");
								status.setDownloadStaStatus(4);
								downLoadMap.put(rspInfo.getPath().replace("_thum", ""), status);
							}
							adpeter.notifyDataSetChanged();
						}
					}else{
						LoadingDialog.dissMiss();
						if(downLoadMap.get(rspInfo.getPath())!=null){
							DownLoadStatus status = new DownLoadStatus();
							status.setDownloadProgress("0.0%");
							status.setDownloadStaStatus(1);
							downLoadMap.put(rspInfo.getPath().replace("_thum", ""), status);
						}
						adpeter.notifyDataSetChanged();

					}
				}

				else if(rspInfo.getType()==2){
					/** 修改http 屏蔽 LoadingDialog.dissMiss();
					if(rspInfo.getDelPics().size()>0){
						for(int i=0;i<rspInfo.getDelPics().size();i++){
							if(DataCleanManager.deleteGeneralFile(Constants.PIC_RECEIVE_THUMB_PATH+rspInfo.getDelPics().get(i))){
								DataCleanManager.deleteGeneralFile(Constants.PIC_RECEIVE_PATH+rspInfo.getDelPics().get(i).replace("_thum", ""));
							}
							downLoadMap.remove(rspInfo.getDelPics().get(i).replace("_thum", ""));
							Logger.i("hasMap", "移除" +rspInfo.getDelPics().get(i).replace("_thum", ""));
							
						}
						picList.removeAll(rspInfo.getDelPics());
						adpeter.setData(picList, downLoadMap);
					}else{
						ToastUtils.showShortToast(  R.string.image_delete_photo_failed);
					}
					if(picList.size()==0){
						setResult(NetWorkConstant.REFRESH_PHOTO);
						ImageDetailActivity.this.finish();
					}**/ 
				
				}
				break;

		
				
			}
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_top_right:
			doEdit();
			break;
		case R.id.btn_delete_icon:
			detete();
			break;
		case R.id.btn_download_icon:
//			moveFile();
			savePic();
			break;
		case R.id.btn_share_icon:
			showShareDialog();
			break;
		default:
			break;
		}
	}
	@Override
	public void onDestroy(){
		super.onDestroy();
		isEdit = false;
	}
	private void doEdit(){
		if(isEdit){
			isEdit = false;
			btn_choice.setText(R.string.common_btn_edit);
			
			editLay.setVisibility(View.GONE);
		}else{
			isEdit = true;
			btn_choice.setText(R.string.common_btn_cancel);
			
			editLay.setVisibility(View.VISIBLE);
		}
	}

	 private void detete(){
		 int count = 0;
		 List<ImageModel> deleteList = new ArrayList<ImageModel>();
		 deleteList.add(new ImageModel());
		 showdeleteDialog(deleteList);
	 }
	 private void showdeleteDialog(List<ImageModel>list){
	    	if(deleteDialog==null)
	    		deleteDialog = new DeletePhotoDiaglog(this,list,editLay);
	    	else 
	    		deleteDialog.refresh(list,editLay);
	    	deleteDialog.show();
	    	deleteDialog.setConfirmListener(this);
	    	editLay.setVisibility(View.GONE);
	    	isEdit = false;
			btn_choice.setText(R.string.common_btn_edit);
	    }
	 private void getPhoto(int type,String name){
		 TransferPhotoInfo info  = new TransferPhotoInfo();
//		 List<String >list = new ArrayList<String>();
//		 list.add(imageName);
		 info.setPath(name);
		 info.setType(type);
		 sendRequest(info, SocketCmdId.ALPHA_MSG_TRANSFER_PHOTO);
		
	 }
	 private void confirmDelete(){
			TransferPhotoInfo info = new TransferPhotoInfo();
			info.setType(2);
			List<String>list  = new ArrayList<String>();
			list.add(picList.get(adpeter.currentPosition).getImage_original_url().split(Alpha2Application.getRobotSerialNo())[1].substring(1)+"_thum");
			info.setDelPics(list);
			sendRequest(info, SocketCmdId.ALPHA_MSG_TRANSFER_PHOTO);
//			LoadingDialog.getInstance(mActivity).show();
		}

	 private void deleteHttpPic(){
				DeletephotoRequest request = new DeletephotoRequest();
				request.setImageId(picList.get(adpeter.currentPosition).getImage_id());
		 RobotGalleryRepository.getInstance().deleteImage(picList.get(adpeter.currentPosition).getImage_id(), new IRobotGalleryDataSource.DeleteImageCallback() {
			 @Override
			 public void onSuccess() {
				 confirmDelete();
				 picList.remove(adpeter.currentPosition);
				 if(picList.size()==0){
					 doBack();
				 }else{
					 adpeter.setData(picList, downLoadMap);
				 }
			 }

			 @Override
			 public void onFail(ThrowableWrapper e) {
				ToastUtils.showShortToast(e.getMessage());
			 }
		 });

	 }
	@Override
	public void onConfirm() {
		// TODO Auto-generated method stub
//		confirmDelete();
		deleteHttpPic();
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == event.KEYCODE_BACK) {
			doBack();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public void onBack(View v){
		doBack();
	}
	private void doBack(){
		Intent intent  = new Intent();
		intent.putExtra("picList", (Serializable)picList);
		intent.putExtra("pisition", position);
		setResult(NetWorkConstant.REFRESH_PHOTO,intent);
		this.finish();
	}
	 private void showShareDialog(){
	    	if(shareDialog==null)
				shareDialog = new SharePopuWindow(this, 3, picList.get(adpeter.currentPosition).getImage_original_url(),this, this);
			shareDialog.show();
	    }

	 @Override
		public void onCancel() {
			// TODO Auto-generated method stub
			LoadingDialog.dissMiss();
		}

		@Override
		public void onComplete(Object arg0) {
			// TODO Auto-generated method stub
			LoadingDialog.dissMiss();
			ToastUtils.showShortToast(  R.string.shop_page_share_success);
		}

		@Override
		public void onError(UiError arg0) {
			// TODO Auto-generated method stub
			LoadingDialog.dissMiss();
			ToastUtils.showShortToast(  R.string.shop_page_share_error);
			
		}

		@Override
		public void noteWeixinNotInstalled() {
			// TODO Auto-generated method stub
			ToastUtils.showShortToast(  R.string.ui_weixin_not_install);
		}
		
		public  void savePic() {
		     String filename=String.valueOf(picList.get(adpeter.currentPosition).getImage_original_url().hashCode());
		     Bitmap bmp = ImageLoader.getInstance(this).getBitmap(picList.get(adpeter.currentPosition).getImage_original_url(), 8);
			 File dir = new File(Constants.PIC_DCIM_PATH);//保证路径包含的文件夹都存在
			 if(!dir.exists()) {
				dir.mkdirs();
			 }
		     boolean failed = false;
		      File f = new File(Constants.PIC_DCIM_PATH, filename+".jpg");
		      if(f.exists()){
		    	  String tip = mContext.getString(R.string.image_save_pic).replace("%", Constants.PIC_DCIM_PATH);
			    	 ToastUtils.showShortToast(  tip);
			    	 return;
		      }else{

		          try {
		              FileOutputStream out = new FileOutputStream(f);
		              bmp.compress(CompressFormat.PNG, 100, out);
		              out.flush();
		              out.close();
		              
		           } catch (Throwable ex){
		                   ex.printStackTrace();
		                   failed = true;
		           }
		             
		          if(failed){
		        	  ToastUtils.showShortToast(  R.string.image_save_failed);
		          }else{
		        	  String tip = mContext.getString(R.string.image_save_pic).replace("%", Constants.PIC_DCIM_PATH);
		        	  ToastUtils.showShortToast(  tip);
		          }
		      } 
		    }
		/**
		* @return 文件移动成功返回true，否则返回false
		*/    
		public void  moveFile() { 
//			String fileName= picList.get(adpeter.currentPosition).replace("_thumb", "");
//			String srcFileName =Constants.PIC_RECEIVE_PATH+fileName ;
//			String destDirName =Constants.PIC_DCIM_PATH;
//		    File srcFile = new File(srcFileName);  
//		    if(!srcFile.exists() || !srcFile.isFile()) {
//		    	ToastUtils.showShortToast(  R.string.image_no_pic);
//		    	return;
//		    }  
//		      
//		    File dFile =new File(destDirName + srcFile.getName());
//		    if(dFile.exists()){
//		    	 String tip = context.getString(R.string.image_save_pic).replace("%", destDirName);
//		    	 ToastUtils.showShortToast(  tip);
//		    	 return;
//		    }
//		    File destDir = new File(destDirName);  
//		    if (!destDir.exists())  
//		        destDir.mkdirs();  
//		     if(srcFile.renameTo(new File(destDirName + srcFile.getName()))){
//		    	 if(new File(destDirName + srcFile.getName()).exists()){
//		    		 String tip = context.getString(R.string.image_save_pic).replace("%", destDirName);
//			    	 ToastUtils.showShortToast(  tip);
//		    	 }
//		    		 
//		     }else{
//		    	 ToastUtils.showShortToast(  R.string.image_save_failed);
//		     }
		} 
		public void copyFile() {
			
//			boolean failed =true;
//			String fileName= picList.get(adpeter.currentPosition).replace("_thumb", "");
//			String oldPath =Constants.PIC_RECEIVE_PATH+fileName ;
//			String newPath =Constants.PIC_DCIM_PATH;
//		    File srcFile = new File(oldPath);  
//		    if(!srcFile.exists() || !srcFile.isFile()) {
//		    	ToastUtils.showShortToast(  R.string.image_no_pic);
//		    	return;
//		    }    
//		    File dFile =new File(newPath + srcFile.getName());
//		    if(dFile.exists()&&dFile.length()==srcFile.length()){
//		    	 String tip = context.getString(R.string.image_save_pic).replace("%", newPath);
//		    	 ToastUtils.showShortToast(  tip);
//		    	 return;
//		    }
//		    File destDir = new File(newPath);  
//		    if (!destDir.exists())  
//		        destDir.mkdirs(); 
//		    
//		       try {   
//		           int bytesum = 0;   
//		           int byteread = 0;   
//		           File oldfile = new File(oldPath);   
//		           if (oldfile.exists()) { //文件存在时   
//		               InputStream inStream = new FileInputStream(oldPath); //读入原文件   
//		               FileOutputStream fs = new FileOutputStream(newPath);   
//		               byte[] buffer = new byte[1444];   
//		               int length;   
//		               while ( (byteread = inStream.read(buffer)) != -1) {   
//		                   bytesum += byteread; //字节数 文件大小   
//		                   fs.write(buffer, 0, byteread);   
//		               }   
//		               inStream.close();  
//		               failed = false;
//		           }   
//		       }   
//		       catch (Exception e) {   
//		           e.printStackTrace();   
//		           failed = true;
//		       }   
//		     if(failed){
//		    	 ToastUtils.showShortToast(  R.string.image_save_failed);
//		     }else{
//		    	 String tip = context.getString(R.string.image_save_pic).replace("%", newPath);
//		    	 ToastUtils.showShortToast(  tip);
//		     }
		     
		   }   
}
