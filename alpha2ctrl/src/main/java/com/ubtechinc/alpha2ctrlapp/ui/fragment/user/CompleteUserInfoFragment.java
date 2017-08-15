package com.ubtechinc.alpha2ctrlapp.ui.fragment.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ant.country.CountryActivity;
import com.facebook.Profile;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.ubtech.utilcode.utils.JsonUtils;
import com.ubtech.utilcode.utils.SPUtils;
import com.ubtech.utilcode.utils.SizeUtils;
import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.BaseHandler;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.constants.NetWorkConstant;
import com.ubtechinc.alpha2ctrlapp.constants.PreferenceConstants;
import com.ubtechinc.alpha2ctrlapp.data.user.IUserConfigDataSource;
import com.ubtechinc.alpha2ctrlapp.data.user.UserConfigReponsitory;
import com.ubtechinc.alpha2ctrlapp.entity.QQLoginInfo;
import com.ubtechinc.alpha2ctrlapp.entity.QQUserInfo;
import com.ubtechinc.alpha2ctrlapp.entity.WeiXinLoginInfo;
import com.ubtechinc.alpha2ctrlapp.entity.WeiXinUserInfo;
import com.ubtechinc.alpha2ctrlapp.entity.business.user.EditUserInfoRequest;
import com.ubtechinc.alpha2ctrlapp.entity.business.user.UserInfo;
import com.ubtechinc.alpha2ctrlapp.entity.request.GetWeixinUserInfo;
import com.ubtechinc.alpha2ctrlapp.third.FaceBookManager;
import com.ubtechinc.alpha2ctrlapp.third.TencentQQManager;
import com.ubtechinc.alpha2ctrlapp.third.TwitterManager;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseActivity;
import com.ubtechinc.alpha2ctrlapp.ui.activity.main.MainPageActivity;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.base.BaseFragment;
import com.ubtechinc.alpha2ctrlapp.util.ImageLoad.ImageLoader;
import com.ubtechinc.alpha2ctrlapp.util.ImageLoad.LoadImage;
import com.ubtechinc.alpha2ctrlapp.util.Tools;
import com.ubtechinc.alpha2ctrlapp.util.WriteBookUtils;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.LoadingDialog;
import com.ubtechinc.nets.http.ThrowableWrapper;

//import com.sina.weibo.sdk.openapi.robotApps.User;

public class CompleteUserInfoFragment extends BaseFragment implements IUiListener {
	public static enum Gender {
		M, F
	}

	private Bitmap mCurrentHeadimg;
	private Gender mCurrentGender = null;
	private ImageView img_head;
	private EditText edt_nick_name;
	private ImageButton btn_gender_m;
	private ImageButton btn_gender_f;
	private EditText edt_phone_name, edt_email_name;
	private TextView btn_next;
	public static boolean completeUser = false;
	private boolean isThirdLogin = false;
	private String headerUrl;
	private RelativeLayout countrySelect;
	private TextView sexTv,countryTv;
	private String countName="";
	private String countryNum="-1";
	private final static int  MSG_TO_REFRESH_FACEBOOK_INFO =0;
	private final static int MSG_TO_RERESH_TWTTER_INFO = 1;


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		init(false, getString(R.string.private_info_titile));
		((BaseActivity) getActivity()).setTopVisible();
		activity.btn_ignore.setVisibility(View.GONE);
		initView();
		initControlListener();
	}

	@Override
	public View onCreateFragmentView(LayoutInflater inflater,
									 ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.edit_user_info, container, false);
	}

	@Override
	public void initView() {

		edt_phone_name = (EditText) mContentView
				.findViewById(R.id.edt_phone_name);
		edt_email_name = (EditText) mContentView
				.findViewById(R.id.edt_email_name);
		img_head = (ImageView) mContentView.findViewById(R.id.img_head);
		RelativeLayout.LayoutParams lp= new RelativeLayout.LayoutParams(Constants.deviceHeight/6, Constants.deviceHeight/6);
		lp.setMargins(0, SizeUtils.dp2px(15),0,0);


//		lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		lp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		img_head.setLayoutParams(lp);
		edt_nick_name = (EditText) mContentView
				.findViewById(R.id.edt_nick_name);
		btn_gender_f = (ImageButton) mContentView
				.findViewById(R.id.btn_gender_f);
		btn_gender_m = (ImageButton) mContentView
				.findViewById(R.id.btn_gender_m);
		btn_gender_f.setBackgroundResource(R.drawable.gender_unselected);
		btn_gender_m.setBackgroundResource(R.drawable.gender_selected);
		mCurrentGender = Gender.M;
		btn_next = (TextView) mContentView.findViewById(R.id.btn_next);
		sexTv = (TextView) mContentView.findViewById(R.id.txt_gender);
		edt_nick_name.setHorizontallyScrolling(true);
		edt_email_name.setHorizontallyScrolling(true);
		if(getBundle() != null) {
			isThirdLogin = getBundle().getBoolean("isThird");
		}
		activity.btn_ignore.setVisibility(View.GONE);
		countrySelect = (RelativeLayout)mContentView.findViewById(R.id.country);
		countryTv = (TextView)mContentView.findViewById(R.id.txt_country);
		countryNum= SPUtils.get().getString(PreferenceConstants.COUNTRY_NUM, "-1");
		countryTv.setText(SPUtils.get().getString(PreferenceConstants.COUNTRY_NAME, ""));
		if(TextUtils.isEmpty(countryNum)||countryNum.equals("-1")||countryNum.equals("null")){
			countryNum ="86";
			countName = mActivity.getResources().getString(R.string.ui_china);
			countryTv.setText(countName);

		}
		if (!isThirdLogin) {
			// isPhone = getBundle().getBoolean("phone");
			// if (isPhone){

//			if (!SPUtils.get()
//					.get(Constants.USER_PHONE).equals("")) {
//				edt_phone_name.setEnabled(false);
//				edt_phone_name.setText(SPUtils.get()
//						.get(Constants.USER_PHONE));
//
//			}

			// }
			// else {
//			if (!SPUtils.get()
//					.get(Constants.USER_EMAIL).equals("")) {
//				edt_email_name.setText(SPUtils.get()
//						.get(Constants.USER_EMAIL));
//				edt_email_name.setEnabled(false);
//			}
//			edt_phone_name.setVisibility(View.GONE);
//			edt_email_name.setVisibility(View.GONE);
			sexTv.setText(mActivity.getResources().getString(R.string.private_info_gender_m));
			// }
		} else {
//			mMainPageActivity.btn_ignore.setVisibility(View.GONE);
//			edt_phone_name.setVisibility(View.GONE);
			int thirdType = getBundle().getInt("type");
			if (thirdType == 1) {
				getQQuserInfo();
			} else if (thirdType == 2) {
				WeiXinLoginInfo info = (WeiXinLoginInfo) getBundle()
						.getSerializable("thrid_info");
				getWeixinUserInfo(info);
			}
//			else if (thirdType == 3) {
//				getWeiboUserInfo();
//			}

			else if(thirdType == 4){
				FaceBookManager.onGetUserProfile(this);
			}else if(thirdType ==5){
				TwitterManager.doGetUserProfile(this);
			}

		}
		countrySelect.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mActivity, CountryActivity.class);
				activity.startActivityForResult(intent,
						Constants.GET_COUNTRY_REQUEST);
			}
		});

	}

	@Override
	public void initControlListener() {

		activity.btn_ignore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				go();
			}
		});

		// TODO Auto-generated method stub
		btn_next.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				doEditPrivateInfo(edt_nick_name.getText().toString(),
						mCurrentGender);
			}
		});
		img_head.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				WriteBookUtils.choosePic(activity);
			}
		});
		btn_gender_f.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				btn_gender_f.setBackgroundResource(R.drawable.gender_selected);
				btn_gender_m
						.setBackgroundResource(R.drawable.gender_unselected);
				mCurrentGender = Gender.F;
				sexTv.setText(mActivity.getResources().getString(R.string.private_info_gender_f));
			}
		});
		btn_gender_m.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				btn_gender_f
						.setBackgroundResource(R.drawable.gender_unselected);
				btn_gender_m.setBackgroundResource(R.drawable.gender_selected);
				mCurrentGender = Gender.M;
				sexTv.setText(mActivity.getResources().getString(R.string.private_info_gender_m));
			}
		});

		edt_phone_name.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!Tools.isNumeric(edt_phone_name.getText().toString())) {
					Toast.makeText(mActivity, R.string.ui_input_correct_phone,
							Toast.LENGTH_SHORT).show();
				}
				if (edt_phone_name.getText().toString().length() > 20) {
					Toast.makeText(mActivity, R.string.ui_input_correct_phone,
							Toast.LENGTH_SHORT).show();
					edt_phone_name.setText(edt_phone_name
							.getText()
							.toString()
							.substring(
									0,
									edt_phone_name.getText().toString()
											.length() - 1));
				}
			}
		});
		edt_nick_name.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!TextUtils.isEmpty(edt_nick_name.getText().toString())) {
					if (!Tools.isCorrectUserFormat(edt_nick_name.getText()
							.toString())) {
						Toast.makeText(mActivity, R.string.ui_user_wrong,
								Toast.LENGTH_SHORT).show();
						btn_next.setBackgroundResource(R.drawable.button_disable);
					}
					else if (edt_nick_name.getText().toString().length() > 20) {
						Toast.makeText(mActivity, R.string.ui_user_length,
								Toast.LENGTH_SHORT).show();
						edt_nick_name.setText(edt_nick_name
								.getText()
								.toString()
								.substring(
										0,
										edt_nick_name.getText().toString()
												.length() - 1));
						btn_next.setBackgroundResource(R.drawable.button_disable);
					}else{
						btn_next.setBackgroundResource(R.drawable.btn_button_able);
					}
				}
			}
		});
		edt_email_name.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// if (!TextUtils.isEmpty(edt_nick_name.getText().toString())) {
				// if (edt_email_name.getText().toString().length() > 20) {
				// Toast.makeText(mActivity, R.string.reg_user_length,
				// Toast.LENGTH_SHORT).show();
				// edt_email_name.setText(edt_email_name
				// .getText()
				// .toString()
				// .substring(
				// 0,
				// edt_email_name.getText().toString()
				// .length() - 1));
				// }
				// }
			}
		});
	}

	private BaseHandler MHandler = new BaseHandler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// TODO Auto-generated method stub

			switch (msg.what) {


				case NetWorkConstant.RESPONSE_GET_WEIXIN_USER_INFO_SUCCESS:
					LoadingDialog.dissMiss();
					WeiXinUserInfo info = (WeiXinUserInfo) msg.obj;
					if (info != null)
						refreshUserInfo(info.nickname, info.sex, info.headimgurl, 2);
					break;

				case MSG_TO_REFRESH_FACEBOOK_INFO:
					LoadingDialog.dissMiss();
					Profile profile = (Profile)msg.obj;
					refreshUserInfo(profile.getName(), "", headerUrl, 4);
					break;
				case MSG_TO_RERESH_TWTTER_INFO:
					LoadingDialog.dissMiss();
					twitter4j.User user = (twitter4j.User)msg.obj;
					headerUrl =user.getProfileImageURLHttps();
					refreshUserInfo(user.getName(), "", headerUrl, 4);
					break;


			}
		}
	};

	public void onNodeNickNameEmpty() {
		ToastUtils.showShortToast( R.string.private_info_set_nick_name);

	}

	public void onNodeHeadEmpty() {

		ToastUtils.showShortToast( R.string.private_info_set_head);

	}

	public void go() {
		Intent intent = new Intent(getActivity(),MainPageActivity.class);
		startActivity(intent);
	}

	/**
	 * 检查编辑信息，无误则提交编辑用户资料请求
	 *
	 * @param nickName
	 * @param gender
	 */
	public void doEditPrivateInfo(String nickName, Gender gender) {

		if (nickName == null || nickName.equals("")) {
			onNodeNickNameEmpty();
			return;
		}
		if (mCurrentHeadimg == null && !isThirdLogin) {
			onNodeHeadEmpty();
			return;
		} else if (mCurrentHeadimg == null && isThirdLogin) {
			mCurrentHeadimg = ImageLoader.getInstance(mActivity).getBitmap(
					headerUrl, 0);
		}
		if(!isThirdLogin){
			if (edt_nick_name.getText().toString().length() < 3
					|| edt_nick_name.getText().toString().length() > 20) {
				Toast.makeText(mActivity, R.string.ui_user_length,
						Toast.LENGTH_SHORT).show();
				return;
			}
		}

		if (!Tools.isCorrectUserFormat(edt_nick_name.getText().toString())) {
			Toast.makeText(mActivity, R.string.ui_user_wrong,
					Toast.LENGTH_SHORT).show();
			return;
		}

//		if (isThirdLogin) {
//			if (TextUtils.isEmpty(edt_phone_name.getText().toString())) {
//				Toast.makeText(mActivity, R.string.ui_phone_empty_note,
//						Toast.LENGTH_SHORT).show();
//				return;
//			}
//
//			if (!Tools.isNumeric(edt_phone_name.getText().toString())) {
//				Toast.makeText(mActivity, R.string.input_correct_phone,
//						Toast.LENGTH_SHORT).show();
//				return;
//			}
//			if (TextUtils.isEmpty(edt_email_name.getText().toString())) {
//				Toast.makeText(mActivity, R.string.ui_email_empty_note,
//						Toast.LENGTH_SHORT).show();
//				return;
//			}
//			if (!Tools.mailAddressVerify(edt_email_name.getText().toString())) {
//				Toast.makeText(mActivity, R.string.input_correct_mial,
//						Toast.LENGTH_SHORT).show();
//				return;
//			}
//		}

		int gender_value = (gender == Gender.M) ? 1 : 2;
		doEdit(nickName, gender_value);

	}

	/**
	 * 获取qq 用户资料
	 */
	private void getQQuserInfo() {
		TencentQQManager
				.getUserInfo(
						this.getActivity(),
						JsonUtils.jsonToBean(
								getBundle().getString("thrid_info"),
								QQLoginInfo.class), this);
		LoadingDialog.getInstance(mActivity).show();

	}

	/**
	 * 获取微信用户资料
	 *
	 * @param info
	 */
	private void getWeixinUserInfo(WeiXinLoginInfo info) {
		GetWeixinUserInfo request = new GetWeixinUserInfo();
		request.setAccess_token(info.getAccess_token());
		request.setOpenid(info.getOpenid());
//		UserAction action = UserAction.getInstance(mActivity, this);
//		action.setThirdPrama(request);
//		LoadingDialog.getInstance(mActivity).show();
//		action.doRequest(NetWorkConstant.REQUEST_GET_WEIXIN_USER_INFO);

	}

//	/**
//	 * 获取微博用户资料
//	 */
//	private void getWeiboUserInfo() {
//		MyWeiBo.doGetUserInfo(getActivity(), LoginFragement.weiboInfo, this);
//		LoadingDialog.getInstance(mActivity).show();
//	}

	/**
	 * 编辑用户资料
	 *
	 * @param nickName
	 * @param gender_value
	 */
	private void doEdit(String nickName, int gender_value) {
		completeUser = true;
		LoadingDialog.getInstance(mActivity).show();
		EditUserInfoRequest request = new EditUserInfoRequest();
		request.setUserName(nickName);
		request.setUserGender(gender_value);
		request.setCountryCode(countryNum);
		if (mCurrentHeadimg != null)
//			request.setUserImage(PictureHandle.getImgStr(mCurrentHeadimg));
        UserConfigReponsitory.get().modifyUserInfo(request, new IUserConfigDataSource.ModifyUserInfoCallback() {
            @Override
            public void onSuccess(UserInfo userInfo) {
				SPUtils.get().put(PreferenceConstants.USER_NAME, userInfo.getUserName());
				SPUtils.get().put(PreferenceConstants.USER_IMAGE, userInfo.getUserImage());
				SPUtils.get().put(PreferenceConstants.USER_PHONE, userInfo.getUserPhone());
				SPUtils.get().put(PreferenceConstants.USER_EMAIL, userInfo.getUserEmail());
				SPUtils.get().put(PreferenceConstants.COUNTRY_NUM, userInfo.getCountryCode());
				SPUtils.get().put(PreferenceConstants.USER_GENDER, userInfo.getUserGender());
                go();
            }

            @Override
            public void onFail(ThrowableWrapper e) {
                ToastUtils.showShortToast(e.getMessage());
            }
        });

	}

	/**
	 * 刷新第三方登录的用户信息
	 *
	 * @param nickName
	 *            用户名
	 * @param sex
	 *            性别
	 * @param headerUrl
	 *            用户头像url
	 * @param type
	 *            第三方登录类型 1:qq ,2:微信,3:微博
	 */
	private void refreshUserInfo(String nickName, String sex, String headerUrl,
								 int type) {
		edt_nick_name.setText(nickName);
		if (type == 1) {
			if (sex.equals("男")) {
				btn_gender_m.setBackgroundResource(R.drawable.gender_selected);
				btn_gender_f
						.setBackgroundResource(R.drawable.gender_unselected);
				sexTv.setText(mActivity.getResources().getString(R.string.private_info_gender_m));
			} else {
				btn_gender_m
						.setBackgroundResource(R.drawable.gender_unselected);
				btn_gender_f.setBackgroundResource(R.drawable.gender_selected);
				sexTv.setText(mActivity.getResources().getString(R.string.private_info_gender_f));
			}
		} else if (type == 2) {
			if (sex.equals("1")) {
				btn_gender_f
						.setBackgroundResource(R.drawable.gender_unselected);
				btn_gender_m.setBackgroundResource(R.drawable.gender_selected);
				mCurrentGender = Gender.M;
				sexTv.setText(mActivity.getResources().getString(R.string.private_info_gender_m));
			} else {
				btn_gender_f.setBackgroundResource(R.drawable.gender_selected);
				btn_gender_m
						.setBackgroundResource(R.drawable.gender_unselected);
				mCurrentGender = Gender.F;
				sexTv.setText(mActivity.getResources().getString(R.string.private_info_gender_f));
			}
		} else if (type == 3) {
			if (sex.equals("m")) {
				btn_gender_f
						.setBackgroundResource(R.drawable.gender_unselected);
				btn_gender_m.setBackgroundResource(R.drawable.gender_selected);
				mCurrentGender = Gender.M;
				sexTv.setText(mActivity.getResources().getString(R.string.private_info_gender_m));
			} else {
				btn_gender_f.setBackgroundResource(R.drawable.gender_selected);
				btn_gender_m
						.setBackgroundResource(R.drawable.gender_unselected);
				mCurrentGender = Gender.F;
				sexTv.setText(mActivity.getResources().getString(R.string.private_info_gender_f));
			}
		}else if(type ==4){
			btn_gender_f
					.setBackgroundResource(R.drawable.gender_unselected);
			btn_gender_m.setBackgroundResource(R.drawable.gender_selected);
			mCurrentGender = Gender.M;
			sexTv.setText(mActivity.getResources().getString(R.string.private_info_gender_m));
		}
		mCurrentHeadimg = ImageLoader.getInstance(mActivity).getBitmap(
				headerUrl, 0);
		this.headerUrl = headerUrl;
		LoadImage.LoadHeader(this.getActivity(), 0, img_head, headerUrl);
		if (mCurrentHeadimg == null) {
			mCurrentHeadimg = ImageLoader.getInstance(mActivity).getBitmap(
					headerUrl, 0);
		}

	}


	public void onFaceBookProfileInfo(Profile profile, String url){
		headerUrl = url;
		MHandler.obtainMessage(MSG_TO_REFRESH_FACEBOOK_INFO,profile ).sendToTarget();


	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == WriteBookUtils.RESULT_CHOOSE_LOCAL_PITURE) {
			Uri currImageURI = data.getData();
			if(currImageURI!=null){
				mCurrentHeadimg = WriteBookUtils.getBitmap(currImageURI, activity);
				img_head.setImageBitmap(mCurrentHeadimg);
			}

		}else if (requestCode == Constants.GET_COUNTRY_REQUEST) {
			Bundle bundle = data.getExtras();
			countName = bundle.getString("countryName");
			countryNum = bundle.getString("countryNumber").substring(1);
			countryTv.setText(countName);
			SPUtils.get().put(PreferenceConstants.COUNTRY_NAME, countName);
			SPUtils.get().put(PreferenceConstants.COUNTRY_NUM, countryNum);

		}
	}

	@Override
	public void onCancel() {
		LoadingDialog.dissMiss();
	}

	@Override
	public void onComplete(Object arg0) {
		LoadingDialog.dissMiss();
		String result = arg0.toString();
		QQUserInfo info = JsonUtils.jsonToBean(result,
				QQUserInfo.class);
		refreshUserInfo(info.nickname, info.gender, info.figureurl_qq_2, 1);

	}

	@Override
	public void onError(UiError arg0) {
		LoadingDialog.dissMiss();
	}



	public void onTwitterProfileInfo(final twitter4j.User user) {
		System.out.println("Twitter---Profile:" + user.getName());
		MHandler.obtainMessage(MSG_TO_RERESH_TWTTER_INFO, user).sendToTarget();

	}

}
