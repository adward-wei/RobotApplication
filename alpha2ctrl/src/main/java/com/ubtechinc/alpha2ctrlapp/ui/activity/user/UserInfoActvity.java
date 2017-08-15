package com.ubtechinc.alpha2ctrlapp.ui.activity.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ubtech.utilcode.utils.SPUtils;
import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.BaseHandler;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.constants.MessageType;
import com.ubtechinc.alpha2ctrlapp.constants.PreferenceConstants;
import com.ubtechinc.alpha2ctrlapp.data.user.IUserConfigDataSource;
import com.ubtechinc.alpha2ctrlapp.data.user.UserConfigReponsitory;
import com.ubtechinc.alpha2ctrlapp.entity.business.user.EditUserInfoRequest;
import com.ubtechinc.alpha2ctrlapp.entity.business.user.UserInfo;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseContactActivity;
import com.ubtechinc.alpha2ctrlapp.util.ImageLoad.LoadImage;
import com.ubtechinc.alpha2ctrlapp.util.ImageLoad.PictureHandle;
import com.ubtechinc.alpha2ctrlapp.util.Tools;
import com.ubtechinc.alpha2ctrlapp.util.WriteBookUtils;
import com.ubtechinc.alpha2ctrlapp.util.upload.IUploadResultListener;
import com.ubtechinc.alpha2ctrlapp.util.upload.QiniuUploader;
import com.ubtechinc.alpha2ctrlapp.util.upload.UploadCBHandler;
import com.ubtechinc.alpha2ctrlapp.util.upload.UploadType;
import com.ubtechinc.alpha2ctrlapp.widget.RoundImageView;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.EditGenderDialog;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.LoadingDialog;
import com.ubtechinc.nets.http.ThrowableWrapper;

public class UserInfoActvity extends BaseContactActivity implements OnClickListener,EditGenderDialog.OnEditFgenderListner,EditGenderDialog.OnEditMgenderListener {

	private String TAG = "MainFunctionActivity";

	@Override
	public void onEditF() {
        refreshGender(2,true);
	}

	@Override
	public void onEditM() {
		refreshGender(1,true);
	}

	public static enum Gender {
		M, F
	}

	public Bitmap mCurrentHeadimg;

	private Gender mCurrentGender = null;

	public RoundImageView edit_head;
	private TextView edt_nick_name;

	private ImageButton btn_gender_m;
	private ImageButton btn_gender_f;

	private TextView edt_phone_name, edt_email_name;
	private Button btn_finish, btn_cancel,btn_get_history;
	private boolean isPhoeFirstCome = true;
	private boolean isEmailFirstCome = true;
	private boolean isUserFirstCome = true;
	private TextView sexTv;
	private RelativeLayout userNameLay,emailLay,phoneLay,genderLay;
	private EditGenderDialog editGenderDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_user_info);
		this.mContext = this;
		initView();
	}

	public void initView() {
		this.title = (TextView) findViewById(R.id.authorize_title);
		title.setText(R.string.devices_person_info);
		edt_phone_name = (TextView) findViewById(R.id.edt_phone_name);
		edt_email_name = (TextView) findViewById(R.id.edt_email_name);
		edit_head = (RoundImageView) findViewById(R.id.user_header);
		RelativeLayout.LayoutParams lp= new RelativeLayout.LayoutParams(Constants.deviceHeight/6, Constants.deviceHeight/6);
		lp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		edit_head.setLayoutParams(lp);
		edt_nick_name = (TextView) findViewById(R.id.edt_nick_name);
		btn_gender_f = (ImageButton) findViewById(R.id.btn_gender_f);
		btn_gender_m = (ImageButton) findViewById(R.id.btn_gender_m);
		mCurrentGender = Gender.M;
		btn_finish = (Button) findViewById(R.id.btn_finish);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		sexTv = (TextView) findViewById(R.id.txt_gender);
		btn_get_history= (Button)findViewById(R.id.get_history);
		userNameLay = (RelativeLayout)findViewById(R.id.user_name_lay);
		emailLay = (RelativeLayout)findViewById(R.id.email_lay);
		phoneLay = (RelativeLayout)findViewById(R.id.phone_lay);
		genderLay = (RelativeLayout)findViewById(R.id.lay_gender_show) ;
		initControlListener();
		editGenderDialog = new EditGenderDialog(this,1);
		editGenderDialog.setMConfirmListener(this);
		editGenderDialog.setFConfirmListener(this);

	}

	private class FunctionHandler extends BaseHandler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MessageType.ALPHA_LOST_CONNECTED:
				isCurrentAlpha2MacLostConnection((String) msg.obj);
				break;


			}
		}
	}

	private void refreshView(){
			if(mCurrentHeadimg!=null){
				edit_head.setImageBitmap(mCurrentHeadimg);
			}else
				LoadImage.LoadHeader(this, 0, edit_head, SPUtils.get().getString(PreferenceConstants.USER_IMAGE));
			String userEmail= SPUtils.get().getString(PreferenceConstants.USER_EMAIL);
			if (!TextUtils.isEmpty(userEmail)) {
				edt_email_name.setText(userEmail);
				edt_email_name.setEnabled(false);
			}
			String userPhone= SPUtils.get().getString(PreferenceConstants.USER_PHONE);

			if (!TextUtils.isEmpty(userPhone)) {
				edt_phone_name.setText(userPhone);
				edt_phone_name.setEnabled(false);
			}
			edt_nick_name.setText(SPUtils.get().getString(PreferenceConstants.USER_NAME).trim());
		    int gender =SPUtils.get().getInt(PreferenceConstants.USER_GENDER,1);
			refreshGender(gender,false);
		String loginKey = SPUtils.get().getString(PreferenceConstants.USER_ACCOUNT, "");
		if(TextUtils.isEmpty(loginKey)){
			emailLay.setClickable(true);
			phoneLay.setClickable(true);
		}else{
			if(!TextUtils.isEmpty(userEmail)){
				emailLay.setClickable(false);
				edt_email_name.setCompoundDrawables(null,null,null,null);
			}
			if(!TextUtils.isEmpty(userPhone)){
				phoneLay.setClickable(false);
				edt_phone_name.setCompoundDrawables(null,null,null,null);
			}
			/*if(Tools.isNumeric(loginKey)){//本地缓存的用户名是否是手机号码
				phoneLay.setClickable(false);
				emailLay.setClickable(true);
				edt_phone_name.setCompoundDrawables(null,null,null,null);
			}else{
				phoneLay.setClickable(true);
				emailLay.setClickable(false);
				edt_email_name.setCompoundDrawables(null,null,null,null);
			}*/
		}


	}

	/**
	 * 更新性别信息
	 * @param gender 性别 1男，2女
	 * @param isEditGender 是否向服务器更新用户性别信息
     */
	public void refreshGender(int gender,boolean isEditGender){
		if (gender == 1) {
			btn_gender_f.setBackgroundResource(R.drawable.gender_unselected);
			btn_gender_m.setBackgroundResource(R.drawable.gender_selected);
			mCurrentGender = Gender.M;
			sexTv.setText(mContext.getResources().getString(R.string.private_info_gender_m));
		} else {
			btn_gender_f.setBackgroundResource(R.drawable.gender_selected);
			btn_gender_m.setBackgroundResource(R.drawable.gender_unselected);
			mCurrentGender = Gender.F;
			sexTv.setText(mContext.getResources().getString(R.string.private_info_gender_f));
		}
		if(isEditGender){
			EditUserInfoRequest request = new EditUserInfoRequest();
			request.setUserGender(gender);
			doEdit(request);
		}
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}


	@Override
	public void onResume() {
		super.onResume();
		if (mHandler == null)
			mHandler = new FunctionHandler();
		refreshView();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.btn_finish:
			doEditPrivateInfo(edt_nick_name.getText().toString(),
					mCurrentGender);
			break;
		case R.id.btn_cancel:

			break;
		case R.id.user_header:
			WriteBookUtils.choosePic(UserInfoActvity.this);
			break;
		case R.id.btn_gender_f:
			btn_gender_f.setBackgroundResource(R.drawable.gender_selected);
			btn_gender_m.setBackgroundResource(R.drawable.gender_unselected);
			mCurrentGender = Gender.F;
			sexTv.setText(mContext.getResources().getString(R.string.private_info_gender_f));
			break;
		case R.id.btn_gender_m:
			btn_gender_f.setBackgroundResource(R.drawable.gender_unselected);
			btn_gender_m.setBackgroundResource(R.drawable.gender_selected);
			mCurrentGender = Gender.M;
			sexTv.setText(mContext.getResources().getString(R.string.private_info_gender_m));
			break;
		case R.id.get_history:
			getHistory();
			break;
		case R.id.user_name_lay:
			goToEditInfo(1);
			break;
		case R.id.phone_lay:
			goToEditInfo(2);
			break;
		case R.id.email_lay:
			goToEditInfo(3);
			break;
		case R.id.lay_gender_show:
			if(mCurrentGender == Gender.M)
				editGenderDialog.refresh(1);
			else{
				editGenderDialog.refresh(2);
			}
			editGenderDialog.show();
			break;
		default:
			break;
		}

	}

	/**
	 * 跳转到编辑页面
	 * @param type 编辑类型，1用户名，2手机，3邮箱
     */
	private void goToEditInfo(int type){
		Intent intent  = new Intent(this,EditUserInfoActivity.class);
		intent.putExtra("type",type);
		int gender_value = (mCurrentGender == Gender.M) ? 1 : 2;
		intent.putExtra("gender_value",gender_value);
		startActivity(intent);
	}
	public void initControlListener() {
		btn_finish.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
		edit_head.setOnClickListener(this);
		btn_gender_f.setOnClickListener(this);
		btn_gender_m.setOnClickListener(this);
		btn_get_history.setOnClickListener(this);
		userNameLay.setOnClickListener(this);
		emailLay.setOnClickListener(this);
		phoneLay.setOnClickListener(this);
		genderLay.setOnClickListener(this);
//		edt_phone_name.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before,
//                                      int count) {
//				// TODO Auto-generated method stub
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count,
//                                          int after) {
//				// TODO Auto-generated method stub
//			}
//
//			@Override
//			public void afterTextChanged(Editable s) {
//				if (!isPhoeFirstCome) {
//					if (!Tools.isNumeric(edt_phone_name.getText().toString())) {
//						Toast.makeText(mContext, R.string.ui_input_correct_phone,
//								Toast.LENGTH_SHORT).show();
//					}
//					if (edt_phone_name.getText().toString().length() > 20) {
//						Toast.makeText(mContext, R.string.ui_input_correct_phone,
//								Toast.LENGTH_SHORT).show();
//						edt_phone_name.setText(edt_phone_name
//								.getText()
//								.toString()
//								.substring(
//										0,
//										edt_phone_name.getText().toString()
//												.length() - 1));
//					}
//				} else {
//					isPhoeFirstCome = false;
//				}
//
//			}
//		});
//		edt_nick_name.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before,
//                                      int count) {
//				// TODO Auto-generated method stub
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count,
//                                          int after) {
//				// TODO Auto-generated method stub
//			}
//
//			@Override
//			public void afterTextChanged(Editable s) {
//				if (!isUserFirstCome) {
//
//					if (!TextUtils.isEmpty(edt_nick_name.getText().toString())) {
//						if (!Tools.isCorrectUserFormat(edt_nick_name.getText().toString())) {
//							Toast.makeText(mContext, R.string.ui_user_wrong, Toast.LENGTH_SHORT).show();
//						}
//						if (edt_nick_name.getText().toString().length() > 20) {
//							Toast.makeText(mContext, R.string.ui_user_length, Toast.LENGTH_SHORT).show();
//							edt_nick_name.setText(edt_nick_name.getText().toString().substring(0, edt_nick_name.getText().toString().length() - 1));
//						}
//					}
//
//				} else {
//					isUserFirstCome = false;
//				}
//			}
//		});
//		edt_email_name.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before,
//                                      int count) {
//				// TODO Auto-generated method stub
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count,
//                                          int after) {
//				// TODO Auto-generated method stub
//			}
//
//			@Override
//			public void afterTextChanged(Editable s) {
//
//			}
//		});

	}

	public void doEditPrivateInfo(String nickName, Gender gender) {

		if (nickName == null || nickName.equals("")) {
			ToastUtils.showShortToast( R.string.private_info_set_nick_name);
			return;
		}
		if (edt_nick_name.getText().toString().length() < 3
				|| edt_nick_name.getText().toString().length() > 20) {
			Toast.makeText(mContext, R.string.ui_user_length,
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (!Tools.isCorrectUserFormat(edt_nick_name.getText().toString())) {
			Toast.makeText(mContext, R.string.ui_user_wrong,
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (!TextUtils.isEmpty(edt_email_name.getText().toString())&&!Tools.mailAddressVerify(edt_email_name.getText().toString())) {
			Toast.makeText(mContext, R.string.ui_input_correct_mial,
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (!TextUtils.isEmpty(edt_phone_name.getText().toString())&&!Tools.isNumeric(edt_phone_name.getText().toString())) {
			Toast.makeText(mContext, R.string.ui_input_correct_phone,
					Toast.LENGTH_SHORT).show();
			return;
		}
		int gender_value = (gender == Gender.M) ? 1 : 2;
		doEdit(nickName, gender_value);

	}

	private void doEdit(String nickName, int gender_value) {

		EditUserInfoRequest request = new EditUserInfoRequest();
		request.setUserName(nickName);
		request.setUserGender(gender_value);
		if(!TextUtils.isEmpty(edt_email_name.getText().toString()))
				request.setUserEmail(edt_email_name.getText().toString());
		if(!TextUtils.isEmpty(edt_phone_name.getText().toString()))
				request.setUserPhone(edt_phone_name.getText().toString());
		if (mCurrentHeadimg != null)
			request.setUserImage(PictureHandle.getImgStr(mCurrentHeadimg));
		UserConfigReponsitory.get().modifyUserInfo(request, new IUserConfigDataSource.ModifyUserInfoCallback() {


			@Override
			public void onSuccess(UserInfo userInfo) {
				SPUtils.get().put(PreferenceConstants.USER_GENDER, mCurrentGender == Gender.M ? 1 : 0);
				ToastUtils.showShortToast( R.string.edit_info_success);
			}

			@Override
			public void onFail(ThrowableWrapper e) {
				ToastUtils.showShortToast(e.getMessage());
			}
		});

	}

	/**
	 * 编辑用户资料
     */
	private void doEdit(EditUserInfoRequest request) {


		UserConfigReponsitory.get().modifyUserInfo(request, new IUserConfigDataSource.ModifyUserInfoCallback() {


			@Override
			public void onSuccess(UserInfo userInfo) {
				SPUtils.get().put(PreferenceConstants.USER_GENDER, mCurrentGender == Gender.M ? 1 : 0);
				SPUtils.get().put(PreferenceConstants.USER_IMAGE, userInfo.getUserImage());
				ToastUtils.showShortToast( R.string.edit_info_success);
			}

			@Override
			public void onFail(ThrowableWrapper e) {
				ToastUtils.showShortToast(e.getMessage());
			}
		});

	}


	private void getHistory(){
		Intent intent2 = new Intent(this,HistoryDeviceActivity.class);
		startActivity(intent2);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == WriteBookUtils.RESULT_CHOOSE_LOCAL_PITURE) {
			if (data != null) {
				Uri currImageURI = data.getData();
				if(currImageURI!=null){
				String retResult = WriteBookUtils.getPath(UserInfoActvity.this, currImageURI);
				mCurrentHeadimg = WriteBookUtils.getBitmap(currImageURI, UserInfoActvity.this);
				edit_head.setImageBitmap(mCurrentHeadimg);
					if(TextUtils.isEmpty(retResult))return;
					LoadingDialog.getInstance(UserInfoActvity.this).show();
					UploadCBHandler handler = new UploadCBHandler();
					handler.filePath = retResult;
					handler.type = UploadType.TYPE_HEAD_IMAGE;
					QiniuUploader.get().upload(handler, new IUploadResultListener() {
						@Override
						public void onUploadSuccess(String url, UploadCBHandler uploadCBHandler) {
							ToastUtils.showShortToast("头像上传成功");
							EditUserInfoRequest editUserInfoRequest = new EditUserInfoRequest();
							editUserInfoRequest.setUserImage(url);
							doEdit(editUserInfoRequest);
							LoadingDialog.dissMiss();
						}

						@Override
						public void onUploadFail(String respInfo, UploadCBHandler uploadCBHandler) {
							ToastUtils.showShortToast("头像上传失败");
							LoadingDialog.dissMiss();
						}
					});

				}
			}

		}
	}

	
}
