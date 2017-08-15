package com.ubtechinc.alpha2ctrlapp.ui.activity.robot;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.ListUtils;
import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtech.utilcode.utils.ZipUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.base.BaseHandler;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.constants.MessageType;
import com.ubtechinc.alpha2ctrlapp.constants.NetWorkConstant;
import com.ubtechinc.alpha2ctrlapp.constants.SocketCmdId;
import com.ubtechinc.alpha2ctrlapp.data.robot.IRobotGalleryDataSource;
import com.ubtechinc.alpha2ctrlapp.data.robot.RobotGalleryRepository;
import com.ubtechinc.alpha2ctrlapp.entity.HttpPhotoList;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.ImageDetail;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.ImageModel;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.ImageTypeModel;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.PhotoList;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.TransferPhotoInfo;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseContactActivity;
import com.ubtechinc.alpha2ctrlapp.ui.adapter.robot.PhotoListAdpter;
import com.ubtechinc.alpha2ctrlapp.util.DataCleanManager;
import com.ubtechinc.alpha2ctrlapp.util.Tools;
import com.ubtechinc.alpha2ctrlapp.widget.RefreshListView;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.DeletePhotoDiaglog;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.LoadingDialog;
import com.ubtechinc.nets.http.ThrowableWrapper;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
/**
 * @ClassName ImageGallyActivity
 * @date 6/27/2017
 * @author tanghongyu
 * @Description 相册类
 * @modifier
 * @modify_time
 */
public class ImageGallyActivity extends BaseContactActivity implements
		View.OnClickListener ,DeletePhotoDiaglog.OnConfirmListener, RefreshListView.OnRefreshListener {
	private RefreshListView gv;
	private TextView btn_choice;

	public static boolean isEdit = false;
	private PhotoListAdpter adpter;
	private List<PhotoList> photoImageList = new ArrayList<PhotoList>();
	@SuppressLint("UseSparseArrays")
	public static HashMap<String, Boolean> isSelected = new HashMap<String, Boolean>();
	private List<ImageTypeModel> typeList = new ArrayList<ImageTypeModel>();
	private List<ImageDetail> imageDetailList = new ArrayList<ImageDetail>();
	
	private List<ImageModel> httpImageList = new ArrayList<ImageModel>(); // http请求的图片列表
	private List<HttpPhotoList> mImageList = new ArrayList<HttpPhotoList>(); // 已经根据时间分类号的http 图标列表
	private LinearLayout btn_delete;
	private DeletePhotoDiaglog deleteDialog;
	private boolean isRefresh = false;
	private  List<String >deleteString = new ArrayList<String>();
	private LinearLayout noPhotoLay;
	private TextView loadTips;
	private  List<String >newPhotoList = new ArrayList<String>();
	private View footerView;
	private boolean hasAddGetMore =false;
	private final int PAGE_COUNT =30;
	private ProgressBar listgetMorePro ;
	private TextView listgetMoreTv;
	private int page=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.iamge_galley);
		initView();
	}

	private void initView() {
		btn_choice = (TextView) findViewById(R.id.btn_top_right);
		btn_choice.setText(R.string.common_btn_edit);
		btn_choice.setVisibility(View.VISIBLE);
		btn_choice.setOnClickListener(this);
		btn_delete = (LinearLayout) findViewById(R.id.btn_delete);
		btn_delete.setOnClickListener(this);
		this.title = (TextView) findViewById(R.id.authorize_title);
		title.setText(getString(R.string.image_galley));
		gv = (RefreshListView) findViewById(R.id.sgv);
		gv.onRefreshComplete();
		adpter = new PhotoListAdpter(mContext, mImageList, true);
		gv.setAdapter(adpter);
	
		loadTips = (TextView)findViewById(R.id.loadTips);
		noPhotoLay = (LinearLayout)findViewById(R.id.no_photo);

		getHttpImageThumb();
		
		footerView = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.get_more_layout, null, false);
		listgetMorePro = (ProgressBar)footerView.findViewById(R.id.head_progressBar);
		listgetMoreTv = (TextView)footerView.findViewById(R.id.head_tip);
		/**列表的下拉刷新**/
		gv.setonRefreshListener(this);

		footerView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getHttpImageThumb();
				listgetMorePro.setVisibility(View.VISIBLE);
				listgetMoreTv.setVisibility(View.GONE);
			}
		});
	}

	/**
	 * 刷新界面
	 * @param list
	 */
	private void refreshData(List<ImageDetail> list) {
		typeList.clear();
		isSelected.clear();
		photoImageList.clear();
		for (int y = 0; y < list.size(); y++) {
			ImageDetail detail = list.get(y);
			if (photoImageList.size() == 0) {
				PhotoList photo = new PhotoList();
				photo.setDateType(detail.getDateType());
				List<ImageDetail> photoDetail = new ArrayList<ImageDetail>();
				photoDetail.add(detail);
				photo.setImageDetailList(photoDetail);
				photoImageList.add(photo);
			} else {
				boolean hasType = false;
				for (int i = 0; i < photoImageList.size(); i++) {
					if (photoImageList.get(i).getDateType().equals(detail.getDateType())) {
						boolean hasDetail = false;
						for(int z=0;z<photoImageList.get(i).getImageDetailList().size();z++){
							if(photoImageList.get(i).getImageDetailList().get(z).equals(detail.getImageName())){
								hasDetail = true;
								break;
							}
						}
						if(!hasDetail){
							photoImageList.get(i).getImageDetailList().add(detail);
						}
						hasType = true;
						break;
					}
				}
				if (!hasType) {
					PhotoList photo = new PhotoList();
					photo.setDateType(detail.getDateType());
					List<ImageDetail> photoDetail = new ArrayList<ImageDetail>();
					photoDetail.add(detail);
					photo.setImageDetailList(photoDetail);
					photoImageList.add(photo);
				}
			}

		}
		
		LoadingDialog.dissMiss();
		Collections.sort(photoImageList);
		for(int y=0;y<photoImageList.size();y++){
			for(int z =0;z<photoImageList.get(y).getImageDetailList().size();z++){
				isSelected.put(photoImageList.get(y).getImageDetailList().get(z).getImageName(), false);
			}
		}
//		adpter.notyfiDataChanged(photoImageList); 修改为http 时屏蔽
	}
	/**
	 * 查询本地已有的图片
	 * @param refresh
	 * @param rspInfo
	 * @return
	 */
	public List<ImageDetail>  checkSdPhoto(boolean refresh,TransferPhotoInfo rspInfo){
		newPhotoList  =rspInfo.getDelPics();
		File mfile = new File(Constants.PIC_RECEIVE_THUMB_PATH);
		File[] files = mfile.listFiles();
		if (files == null)
			return imageDetailList;
		// 将所有的文件存入ArrayList中,并过滤所有图片格式的文件
		for (int y = 0; y < files.length; y++) {
			File file = files[y];
			if (checkIsImageFile(file.getPath())) {
				if (!file.getName().startsWith("IMG")
						|| file.getName().length() != 29)
					Logger.i( file.getName());
				else {
					String fileName = file.getName().replace(".jpg", "").substring(4).split("_")[0].replace("_thumb", "");
					if (Tools.isNumeric(fileName)) {
						ImageDetail detail = new ImageDetail();
						detail.setDateType(fileName);
						detail.setImageName(file.getName());
						detail.setImagePath(file.getPath());
						boolean hasPic = false;
						for (int z = 0; z < rspInfo.getDelPics().size(); z++) {
							if(file.getName().equals(rspInfo.getDelPics().get(z))){
								imageDetailList.add(detail);// 添加机器人中有的图片
								hasPic = true;
								newPhotoList.remove(z);
								break;
							}
						}
						if(!hasPic){
							DataCleanManager.deleteGeneralFile(file.getAbsolutePath()); // 删除机器人中不存在的图片
						}
						
					}

				}
			}
		}
		
		
		return imageDetailList;

	
	}
	/***
	 *  从sd 卡重新加载
	 * @param refresh
	 */
	public void loadPicture(boolean refresh) {
		imageDetailList.clear();
		typeList.clear();
		photoImageList.clear();
		isSelected.clear();
		File mfile = new File(Constants.PIC_RECEIVE_THUMB_PATH);
		File[] files = mfile.listFiles();
		if (files == null)
			return;
		// 将所有的文件存入ArrayList中,并过滤所有图片格式的文件
		for (int y = 0; y < files.length; y++) {
			File file = files[y];
			if (checkIsImageFile(file.getPath())) {
				if (!file.getName().startsWith("IMG")
						|| file.getName().length() != 29)
					Logger.i( file.getName());
				else {
					String fileName = file.getName().replace(".jpg", "").substring(4).split("_")[0].replace("_thumb", "");
					if (Tools.isNumeric(fileName)) {
						ImageDetail detail = new ImageDetail();
						detail.setDateType(fileName);
						detail.setImageName(file.getName());
						detail.setImagePath(file.getPath());
						imageDetailList.add(detail);
						if (typeList.size() == 0) {
							ImageTypeModel tpye = new ImageTypeModel();
							tpye.setTypeDate(detail.getDateType());
							tpye.setPositon(1);
							typeList.add(tpye);
						} else {
							boolean hasExist = false;
							for (int i = 0; i < typeList.size(); i++) {
								if (typeList.get(i).getTypeDate()
										.equals(detail.getDateType())) {
									typeList.get(i).setPositon(typeList.get(i).getPositon() + 1);
									hasExist = true;
									break;
								}
							}
							if (!hasExist) {
								ImageTypeModel tpye = new ImageTypeModel();
								tpye.setTypeDate(detail.getDateType());
								tpye.setPositon(1);
								typeList.add(tpye);
							}
						}
					}

				}
			}
		}
		for (int z = 0; z < typeList.size(); z++) {
			PhotoList photoItem = new PhotoList();
			photoItem.setDateType(typeList.get(z).getTypeDate());
			photoItem.setImageDetailList(new ArrayList<ImageDetail>());
			for (int y = 0; y < imageDetailList.size(); y++) {
				if (typeList.get(z).getTypeDate()
						.equals(imageDetailList.get(y).getDateType())) {
					photoItem.getImageDetailList().add(imageDetailList.get(y));
					if(newPhotoList!=null){
						newPhotoList.remove(imageDetailList.get(y).getImageName());
					}
						
					// break;
				}
			}
			photoImageList.add(photoItem);
		}
		if (isRefresh) {
			gv.onRefreshComplete();
			isRefresh = false;
		}
		// 还有数据没有加载完就显示出加载更多
		if(newPhotoList.size()>0&& !hasAddGetMore){
				gv.addFooterView(footerView);
				hasAddGetMore = true;
				gv.setAddMore(true);
				listgetMorePro.setVisibility(View.GONE);
				listgetMoreTv.setVisibility(View.VISIBLE);
		}else if(newPhotoList.size()<=0 && hasAddGetMore){
				gv.removeFooterView(footerView);
				hasAddGetMore = false;
				gv.setAddMore(false);
		}else{
			gv.setAddMore(true);
		}
		Collections.sort(photoImageList);
		for(int y=0;y<photoImageList.size();y++){
			for(int z =0;z<photoImageList.get(y).getImageDetailList().size();z++){
				isSelected.put(photoImageList.get(y).getImageDetailList().get(z).getImageName(), false);
			}
		}
//		adpter.notyfiDataChanged(photoImageList);//修改为http 时屏蔽

	}
	private void refreshHttpPicData(List<ImageModel> imageList){
		// 将所有的文件存入ArrayList中,并过滤所有图片格式的文件
		httpImageList.addAll(imageList);
		mImageList.clear();
		isSelected.clear();
		for (int y = 0; y < imageList.size(); y++) {
				ImageModel detail = imageList.get(y);
						if (typeList.size() == 0) {
							ImageTypeModel tpye = new ImageTypeModel();
							tpye.setTypeDate(detail.getImage_upload_time().substring(0, 10));
							tpye.setPositon(1);
							typeList.add(tpye);
						} else {
							boolean hasExist = false;
							for (int i = 0; i < typeList.size(); i++) {
								if (typeList.get(i).getTypeDate().equals(detail.getImage_upload_time().substring(0, 10))) {
									typeList.get(i).setPositon(typeList.get(i).getPositon() + 1);
									hasExist = true;
									break;
								}
							}
							if (!hasExist) {
								ImageTypeModel tpye = new ImageTypeModel();
								tpye.setTypeDate(detail.getImage_upload_time().substring(0, 10));
								tpye.setPositon(1);
								typeList.add(tpye);
							}
						}

		}
		for (int z = 0; z < typeList.size(); z++) {
			HttpPhotoList photoItem = new HttpPhotoList();
			photoItem.setDateType(typeList.get(z).getTypeDate());
			photoItem.setImageDetailList(new ArrayList<ImageModel>());
			for (int y = 0; y < httpImageList.size(); y++) {
				if (typeList.get(z).getTypeDate()
						.equals(httpImageList.get(y).getImage_upload_time().substring(0, 10))) {
					photoItem.getImageDetailList().add(httpImageList.get(y));
				}
			}
			mImageList.add(photoItem);
		}
		if (isRefresh) {
			gv.onRefreshComplete();
			isRefresh = false;
		}
		if(imageList.size() >= PAGE_COUNT){
			if(!hasAddGetMore){
				gv.addFooterView(footerView);	
			}
			hasAddGetMore = true;
			gv.setAddMore(true);
			listgetMorePro.setVisibility(View.GONE);
			listgetMoreTv.setVisibility(View.VISIBLE);
		}else{
			if(hasAddGetMore){
				gv.removeFooterView(footerView);
				hasAddGetMore = false;
			}
			gv.setAddMore(false);
		}
		
		Collections.sort(mImageList);
		for(int y = 0; y< mImageList.size(); y++){
			for(int z = 0; z< mImageList.get(y).getImageDetailList().size(); z++){
				isSelected.put(mImageList.get(y).getImageDetailList().get(z).getImage_id(), false);
			}
		}
		adpter.notyfiDataChanged(mImageList);

	
	}
	private void refreshHttpPicDataAfterDelete(List<ImageModel>list){
		for(int i =0;i<list.size();i++){
			for(int y= 0;y<deleteString.size();y++){
				if(list.get(i).getImage_id().equals(deleteString.get(y))){
					list.remove(i);
				}
			}
		}
		mImageList.clear();
		isSelected.clear();
		typeList.clear();
		for (int y = 0; y < httpImageList.size(); y++) {
				ImageModel detail = httpImageList.get(y);
						if (typeList.size() == 0) {
							ImageTypeModel tpye = new ImageTypeModel();
							tpye.setTypeDate(detail.getImage_upload_time().substring(0, 10));
							tpye.setPositon(1);
							typeList.add(tpye);
						} else {
							boolean hasExist = false;
							for (int i = 0; i < typeList.size(); i++) {
								if (typeList.get(i).getTypeDate().equals(detail.getImage_upload_time().substring(0, 10))) {
									typeList.get(i).setPositon(typeList.get(i).getPositon() + 1);
									hasExist = true;
									break;
								}
							}
							if (!hasExist) {
								ImageTypeModel tpye = new ImageTypeModel();
								tpye.setTypeDate(detail.getImage_upload_time().substring(0, 10));
								tpye.setPositon(1);
								typeList.add(tpye);
							}
						}

		}
		for (int z = 0; z < typeList.size(); z++) {
			HttpPhotoList photoItem = new HttpPhotoList();
			photoItem.setDateType(typeList.get(z).getTypeDate());
			photoItem.setImageDetailList(new ArrayList<ImageModel>());
			for (int y = 0; y < httpImageList.size(); y++) {
				if (typeList.get(z).getTypeDate()
						.equals(httpImageList.get(y).getImage_upload_time().substring(0, 10))) {
					photoItem.getImageDetailList().add(httpImageList.get(y));
				}
			}
			mImageList.add(photoItem);
		}
		if (isRefresh) {
			gv.onRefreshComplete();
			isRefresh = false;
		}
		// 还有数据没有加载完就显示出加载更多
		if(list.size()>= page&& !hasAddGetMore){
				gv.addFooterView(footerView);
				hasAddGetMore = true;
				gv.setAddMore(true);
				listgetMorePro.setVisibility(View.GONE);
				listgetMoreTv.setVisibility(View.VISIBLE);
		}else if(list.size()<page && hasAddGetMore){
				gv.removeFooterView(footerView);
				hasAddGetMore = false;
				gv.setAddMore(false);
		}else{
			gv.setAddMore(true);
		}
		Collections.sort(mImageList);
		for(int y = 0; y< mImageList.size(); y++){
			for(int z = 0; z< mImageList.get(y).getImageDetailList().size(); z++){
				isSelected.put(mImageList.get(y).getImageDetailList().get(z).getImage_id(), false);
			}
		}
		adpter.notyfiDataChanged(mImageList);

	
	}

	// 检查扩展名，得到图片格式的文件
	private boolean checkIsImageFile(String fName) {
		boolean isImageFile = false;

		// 获取扩展名
		String FileEnd = fName.substring(fName.lastIndexOf(".") + 1,
				fName.length()).toLowerCase();
		if (FileEnd.equals("jpg") || FileEnd.equals("gif")
				|| FileEnd.equals("png") || FileEnd.equals("jpeg")
				|| FileEnd.equals("bmp")) {
			isImageFile = true;
		} else {
			isImageFile = false;
		}
		return isImageFile;
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
			case MessageType.ALPHA_GET_THUMB_IMAGE: {
				FlieTransInfo info = (FlieTransInfo) msg.obj;
				Logger.i("fileNxy: name " + info.getFileName());
				Logger.i("fileNxy: pro " + info.getFileProgress());
			}
				break;
			case MessageType.ALPHA_GET_THUMB_SUCCEESS: {
				FlieTransInfo info = (FlieTransInfo) msg.obj;
				Logger.i("fileNxy:  缩略图压缩包成功 " + info.getFileName());
//				if(newPhotoList!=null)
//					newPhotoList.remove(info.getFileName());
//                  	if (count == requestCount) {				                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            
//                  		LoadingDialog.dissMiss();
//        				loadPicture(false);
//        				loadTips.setVisibility(View.GONE);
//                  	}
				SaveDataRunnable  thread = new SaveDataRunnable(mContext, Constants.PIC_RECEIVE_THUMB_PATH+info.getFileName(), Constants.PIC_RECEIVE_THUMB_PATH, mHandler);
				thread.start();

			}
				break;
			case MessageType.ALPHA_UNZIP_SUCCESS:
				// 解压成功
				LoadingDialog.dissMiss();
				loadPicture(false);
				loadTips.setVisibility(View.GONE);
				break;
			case MessageType.ALPHA_UNZIP_FAILED:
				// 解压失败
				break;

			case SocketCmdId.ALPHA_MSG_RSP_TRANSFER_PHOTO:
				TransferPhotoInfo rspInfo = (TransferPhotoInfo) msg.obj;
				if(rspInfo.getType()==1){
					LoadingDialog.dissMiss();
					if (rspInfo.getAmount() == 0) {// 机器人中没有图片
						noPhotoLay.setVisibility(View.VISIBLE);
						loadTips.setVisibility(View.GONE);
						btn_choice.setVisibility(View.GONE);
						gv.onRefreshComplete();
						gv.setVisibility(View.GONE);
					} else {
						gv.setVisibility(View.VISIBLE);
						imageDetailList = checkSdPhoto(false, rspInfo);
						noPhotoLay.setVisibility(View.GONE);
						if(imageDetailList.size()>0){
							refreshData(imageDetailList);// 本地有图片，先进行本地加载
						}else{
//							gv.setResultSize(0);// 本地没有图片
						}
						// 判断有无新的图片
						if(newPhotoList.size()==0){
							loadTips.setVisibility(View.GONE); // 没有新图片
						}else{
							loadTips.setText(mContext.getString(R.string.image_has_new_photo).replace("%", newPhotoList.size()+""));
							loadTips.setVisibility(View.VISIBLE);
						}						

					}
				}else if(rspInfo.getType()==2){
					LoadingDialog.dissMiss();
					for(int i=0;i<rspInfo.getDelPics().size();i++){
						if(DataCleanManager.deleteGeneralFile(Constants.PIC_RECEIVE_THUMB_PATH+rspInfo.getDelPics().get(i))){
							DataCleanManager.deleteGeneralFile(Constants.PIC_RECEIVE_PATH+rspInfo.getDelPics().get(i).replace("_thum", ""));
						}
						for(int y=0;y<imageDetailList.size();y++){
							if(rspInfo.getDelPics().get(i).equals(imageDetailList.get(y).getImageName())){
								imageDetailList.remove(y);
								y--;
							}
						}
					}
					refreshData(imageDetailList);
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
		case R.id.btn_delete:
			detete();
			break;
		default:
			break;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		isEdit = false;
	}

	public void setSelect(String name, boolean check) {
		
		isSelected.put(name, check);
		boolean hasChoose = check;
		if (!check) {
			for (int y = 0; y < isSelected.size(); y++) {
				if (isSelected.get(httpImageList.get(y).getImage_id())) {
					hasChoose = true;
					break;
				}
			}
		}
		if (!hasChoose) {
			btn_delete.setVisibility(View.GONE);
		} else {
			btn_delete.setVisibility(View.VISIBLE);
		}
		adpter.notifyDataSetChanged();
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
			 if(resultCode == NetWorkConstant.REFRESH_PHOTO&& data!=null){
//				 loadPicture(false);
				 
//					LoadingDialog.getInstance(mActivity).show();
//					httpImageList.clear();
//					page =0;
//					getHttpImageThumb();
				 // 进入相册详情之后根据编辑情况，刷新数据
				 int position =data.getIntExtra("pisition", -1);
				 if(position!=-1){
					 List<ImageModel >picList  = (List<ImageModel >)data.getSerializableExtra("picList");
					 String type = mImageList.get(position).getDateType();
					 mImageList.remove(position);// 移除先前编辑的图片内容
					 if(picList.size()>0){
						HttpPhotoList list = new HttpPhotoList();
						list.setDateType(type);
						list.setImageDetailList(picList);
						mImageList.add(position, list);//重新添加未删除的内容
						
					 }else{
						 for(int y =0;y<typeList.size();y++){
							 if(typeList.get(y).equals(type)){
								 typeList.remove(y); // 全部删除某一项，类别也需要删除
								 break;
							 }
						 }
					 }
					 httpImageList.clear();
					 for(int y = 0; y< mImageList.size(); y++){
							for(int z = 0; z< mImageList.get(y).getImageDetailList().size(); z++){
								isSelected.put(mImageList.get(y).getImageDetailList().get(z).getImage_id(), false);
								httpImageList.add(mImageList.get(y).getImageDetailList().get(z));
							}
						}
					 adpter.notyfiDataChanged(mImageList);
					 
				 }
			 }
		}
	private void doEdit() {
		if (isEdit) {
			isEdit = false;
			btn_choice.setText(R.string.common_btn_edit);
			for (int i = 0; i < httpImageList.size(); i++) {
				isSelected.put(httpImageList.get(i).getImage_id(), false);
			}
			btn_delete.setVisibility(View.GONE);
		} else {
			isEdit = true;
			btn_choice.setText(R.string.common_btn_cancel);
		}
		Collections.sort(mImageList);
		adpter.notyfiDataChanged(mImageList);
	}

	public HashMap<String, Boolean> getIsSelected() {
		return isSelected;
	}

	public boolean getCheck(String name) {
		int position = 0;
		return isSelected.get(name);
	}

	private void detete() {
		int count = 0;
		List<ImageModel> deleteList = new ArrayList<ImageModel>();
		deleteString.clear();
		for (int i = 0; i < httpImageList.size(); i++) {
			if (isSelected.get(httpImageList.get(i).getImage_id())) {
				count++;
				deleteList.add(httpImageList.get(i));
				deleteString.add(httpImageList.get(i).getImage_id());
			}
			
		}
		if (count > 0) {
			showdeleteDialog(deleteList);
		}
	}

	private void showdeleteDialog(List<ImageModel> list) {
		if (deleteDialog == null)
			deleteDialog = new DeletePhotoDiaglog(this, list, btn_delete);
		else
			deleteDialog.refresh(list, btn_delete);
		deleteDialog.show();
		deleteDialog.setConfirmListener(this);
		btn_delete.setVisibility(View.GONE);
	}


	
	public void getHttpImageThumb(){

		LoadingDialog.getInstance(mContext).show();
		RobotGalleryRepository.getInstance().getGallery(Alpha2Application.getRobotSerialNo(), ++page, PAGE_COUNT, new IRobotGalleryDataSource.GetGalleryCallback() {
			@Override
			public void onGallery(List<ImageModel> imageList) {
				LoadingDialog.getInstance(mContext).dismiss();
				if(ListUtils.isEmpty(imageList)) {
					noPhotoLay.setVisibility(View.VISIBLE);
					loadTips.setVisibility(View.GONE);
					btn_choice.setVisibility(View.GONE);
					gv.onRefreshComplete();
					gv.setVisibility(View.GONE);
				}else {
					refreshHttpPicData(imageList);
				}
			}

			@Override
			public void onDataNotAvailable(ThrowableWrapper e) {
				LoadingDialog.getInstance(mContext).dismiss();
				ToastUtils.showShortToast(e.getMessage());
				if (isRefresh) {
					gv.onRefreshComplete();
					isRefresh = false;
				}
				if(hasAddGetMore){
					listgetMorePro.setVisibility(View.GONE);
					listgetMoreTv.setVisibility(View.VISIBLE);
				}
			}
		});

		
	}
	private void confirmDelete(){
		TransferPhotoInfo info = new TransferPhotoInfo();
		info.setType(2);
		info.setDelPics(deleteString);
		sendRequest(info, SocketCmdId.ALPHA_MSG_TRANSFER_PHOTO);
		LoadingDialog.getInstance(mContext).show();
	}
	 private void deleteHttpPic(){
			String id="";
			for(int i=0;i<deleteString.size();i++){
				id += deleteString.get(i);
				if(i!=deleteString.size()-1)
					id+=",";
			}

		 RobotGalleryRepository.getInstance().deleteImage(id, new IRobotGalleryDataSource.DeleteImageCallback() {
			 @Override
			 public void onSuccess() {
				 refreshHttpPicDataAfterDelete(httpImageList);
			 }

			 @Override
			 public void onFail(ThrowableWrapper e) {
				 ToastUtils.showShortToast(e.getMessage());
			 }
		 });

}
	@Override
	public void onConfirm() {
		deleteHttpPic();
	}

	@Override
	public void onRefresh() {
		 isRefresh = true;
		getHttpImageThumb();

	
	}

	
	
	class SaveDataRunnable extends  Thread {
		/** 压缩文件所在路径 用于解压 **/
		private String zipPath;
		/** 解压后文件所在路径 用于解压 **/
		private String unzipPath;
	    private Context context;
	    private static final boolean D = false;
	 	private Handler handler;
	
	 /***
	 * 
	 * @param context
	 * @param zipPath	压缩文件所在路径 用于解压
	 * @param unzipPath  解压后文件所在路径 用于解压 
	 * @param handler
	 */
		public SaveDataRunnable(Context context,String zipPath, String unzipPath,  Handler handler) {
			this.zipPath = zipPath;
			this.unzipPath = unzipPath;
			this.context=context;
			this.handler=handler;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub'
			super.run();
			String message = ZipUtils.unZip(zipPath, unzipPath, true);
			Logger.i("执行解压");
			File file = new File(zipPath);
			if (message.equals("success")) {
				handler.sendEmptyMessage(MessageType.ALPHA_UNZIP_SUCCESS);
			}else{
				handler.sendEmptyMessage(MessageType.ALPHA_UNZIP_FAILED);
				System.out.println("------解压失败");
			}
			if (file.exists()) {
				boolean isSuccess = file.delete();
				if (isSuccess) {
					Logger.i("删除" + zipPath + "成功");
				} else {
					Logger.i("删除" + zipPath + "失败");
				}
			} else {
				Logger.i(zipPath + "该文件夹不存在");
			}
		}

	}

}
