package com.ubtechinc.alpha2ctrlapp.ui.fragment.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ant.country.CountryActivity;
import com.google.common.collect.ImmutableMap;
import com.ubtech.utilcode.utils.SPUtils;
import com.ubtech.utilcode.utils.StringUtils;
import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.constants.IntentConstants;
import com.ubtechinc.alpha2ctrlapp.constants.PreferenceConstants;
import com.ubtechinc.alpha2ctrlapp.data.user.IRegisterDataSource;
import com.ubtechinc.alpha2ctrlapp.data.user.RegisterReponsitory;
import com.ubtechinc.alpha2ctrlapp.entity.business.user.UserInfo;
import com.ubtechinc.alpha2ctrlapp.entity.net.RegisterModule;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseActivity;
import com.ubtechinc.alpha2ctrlapp.ui.activity.main.MainPageActivity;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.base.BaseFragment;
import com.ubtechinc.alpha2ctrlapp.util.StatisticsReportUtil;
import com.ubtechinc.alpha2ctrlapp.util.Tools;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.LoadingDialog;
import com.ubtechinc.nets.http.ThrowableWrapper;
/**
 * @ClassName RegistFragement
 * @date 6/12/2017
 * @author tanghongyu
 * @Description 账号注册页面
 * @modifier
 * @modify_time
 */
public class RegistFragement extends BaseFragment implements OnClickListener {
	private EditText edt_regisetN;
	private EditText edt_passwd;
	private EditText edt_re_passwd;
	private EditText edt_phone_verification_code;
	private TextView btn_regiter, btn_email, btn_phone,
			btn_phone_verification_code;
	public static int currentModel = BusinessConstants.ACCOUNT_TYPE_PHONE;
	private int time;
	public static boolean needLogin = false;
	private LinearLayout edt_countryCode;
	private String countryName, mCountryNum;
	private TextView countryNameTv, countryNumTv,tipsTV;
	private boolean isOk = false;
	private LinearLayout edt_phone_num_Lay;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		init(false, getString(R.string.ui_register));
		((BaseActivity) getActivity()).setTopVisible();
		activity.btn_ignore.setVisibility(View.GONE);
		initView();
		initControlListener();
	}

	@Override
	public View onCreateFragmentView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.activity_register, container, false);
	}

	@Override
	public void initView() {
		
		btn_regiter = (TextView) mContentView.findViewById(R.id.btn_regiter);
		edt_regisetN = (EditText) mContentView
				.findViewById(R.id.edt_phone_num);
		edt_passwd = (EditText)mContentView.findViewById(R.id.edt_passwd);
		edt_re_passwd = (EditText) mContentView.findViewById(
				R.id.edt_re_passwd);
		edt_phone_verification_code = (EditText)mContentView.findViewById(
				R.id.edt_phone_verification_code);
		btn_phone = (TextView) mContentView.findViewById(
				R.id.btn_login_by_phone);
		btn_email = (TextView)mContentView.findViewById(
				R.id.btn_login_by_email);
		btn_phone_verification_code = (TextView) mContentView.findViewById(
				R.id.btn_phone_verification_code);
		edt_countryCode = (LinearLayout)mContentView.findViewById(
				R.id.edt_phone_1);
		countryNameTv = (TextView) mContentView.findViewById(
				R.id.countryNameTv);
		countryNumTv = (TextView) mContentView.findViewById(R.id.countryNumTv);
		edt_regisetN.setHorizontallyScrolling(true);
		tipsTV = (TextView)mContentView.findViewById(R.id.tips);
		edt_phone_num_Lay = (LinearLayout)mContentView.findViewById(R.id.edt_phone_num_Lay);
		mCountryNum = SPUtils.get().getString(PreferenceConstants.COUNTRY_NUM, "-1");
		countryNameTv.setText(SPUtils.get().getString(PreferenceConstants.COUNTRY_NAME, ""));
		if(!mCountryNum.equals("-1")){
			countryNumTv.setText("+"+ mCountryNum);
			
		}else{
			mCountryNum ="86";
			countryName = mActivity.getResources().getString(R.string.ui_china);
			countryNumTv.setText("+"+ mCountryNum);
			countryNameTv.setText(countryName);
					
		}
		if(TextUtils.isEmpty(countryName)){
			mCountryNum ="86";
			countryName = mActivity.getResources().getString(R.string.ui_china);
			countryNumTv.setText("+"+ mCountryNum);
			countryNameTv.setText(countryName);
		}
		refreshMode(1);
	}

	@Override
	public void initControlListener() {
		// TODO Auto-generated method stub
		btn_phone.setOnClickListener(this);
		btn_email.setOnClickListener(this);
		btn_regiter.setOnClickListener(this);
		edt_countryCode.setOnClickListener(this);
		btn_phone_verification_code.setOnClickListener(this);
		edt_regisetN.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub


					edt_phone_num_Lay.setBackgroundResource(R.drawable.input_nomal);
			}
		});
		edt_regisetN.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

					edt_phone_num_Lay.setBackgroundResource(R.drawable.input_nomal);
				if(!TextUtils.isEmpty(edt_passwd.getText().toString())&& !TextUtils.isEmpty(edt_regisetN.getText().toString())
						&&!TextUtils.isEmpty(edt_re_passwd.getText().toString())){
					btn_regiter.setBackgroundResource(R.drawable.btn_button_able);
					tipsTV.setText("");
				}else{
					btn_regiter.setBackgroundResource(R.drawable.button_disable);
				}
			
			}
		});
		edt_passwd.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
					edt_passwd.setBackgroundResource(R.drawable.input_nomal);
					if(!TextUtils.isEmpty(edt_passwd.getText().toString())&& !TextUtils.isEmpty(edt_regisetN.getText().toString())
							&&!TextUtils.isEmpty(edt_re_passwd.getText().toString())){
						tipsTV.setText("");
						btn_regiter.setBackgroundResource(R.drawable.btn_button_able);
						return;
					}else{
						btn_regiter.setBackgroundResource(R.drawable.button_disable);;
					}
					
			}
		});
		edt_re_passwd.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			
					edt_re_passwd.setBackgroundResource(R.drawable.input_nomal);
					if(!TextUtils.isEmpty(edt_passwd.getText().toString())&& !TextUtils.isEmpty(edt_regisetN.getText().toString())
							&&!TextUtils.isEmpty(edt_re_passwd.getText().toString())){
						tipsTV.setText("");
						btn_regiter.setBackgroundResource(R.drawable.btn_button_able);
						return;
					}else{
						btn_regiter.setBackgroundResource(R.drawable.button_disable);;
					}
				}
		});
	}


	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_login_by_phone:
			if (currentModel != BusinessConstants.ACCOUNT_TYPE_PHONE)
				refreshMode(BusinessConstants.ACCOUNT_TYPE_PHONE);
			break;
		case R.id.btn_login_by_email:
			if (currentModel != BusinessConstants.ACCOUNT_TYPE_EMAIL)
				refreshMode(BusinessConstants.ACCOUNT_TYPE_EMAIL);
			break;
		case R.id.btn_regiter: {
			doRegister();
		}
			break;
		case R.id.btn_phone_verification_code:
			getMsgCode(edt_regisetN.getText().toString());
			break;
		case R.id.edt_phone_1:
			Intent intent = new Intent();
			intent.setClass(mActivity, CountryActivity.class);
			activity.startActivityForResult(intent,
					Constants.GET_COUNTRY_REQUEST);
			break;
		default:
			break;
		}

	}


	private void refreshMode(int type) {
		currentModel = type;
		if (type == BusinessConstants.ACCOUNT_TYPE_PHONE) {
			tipsTV.setText("");
			edt_passwd.setText("");
			edt_regisetN.setText("");
			edt_re_passwd.setText("");
			edt_regisetN.setHint(R.string.ui_phone_input);
//			btn_phone.setBackgroundDrawable(getResources().getDrawable(
//					R.drawable.btn_wifi_select));
			btn_phone.setTextColor(getResources().getColor(R.color.text_color_t5));
//			btn_email.setBackgroundDrawable(getResources().getDrawable(
//					R.drawable.btn_person_wifi_unselect));
			btn_email.setTextColor(getResources().getColor(R.color.text_color_t2));
			edt_phone_verification_code.setVisibility(View.VISIBLE);
			btn_phone_verification_code.setVisibility(View.VISIBLE);
			edt_countryCode.setVisibility(View.VISIBLE);

		} else {
			tipsTV.setText("");
			edt_passwd.setText("");
			edt_regisetN.setText("");
			edt_re_passwd.setText("");
			edt_regisetN.setHint(R.string.ui_email_input);
//			btn_phone.setBackgroundDrawable(getResources().getDrawable(
//					R.drawable.btn_wifi_unselect));
//			btn_email.setBackgroundDrawable(getResources().getDrawable(
//					R.drawable.btn_person_wifi_select));
			edt_phone_verification_code.setVisibility(View.GONE);
			btn_phone_verification_code.setVisibility(View.GONE);
			edt_countryCode.setVisibility(View.GONE);
			btn_phone.setTextColor(getResources().getColor(R.color.text_gray2));
			btn_email.setTextColor(getResources().getColor(R.color.blue));
			if (countdownTimer != null)
				countdownTimer.cancel();
			btn_phone_verification_code.setEnabled(true);
			btn_phone_verification_code.setTextSize(20);
			btn_phone_verification_code
					.setText(getString(R.string.regist_verification_get));
		}

	}
	
	private boolean checkUser(){

		if (currentModel == BusinessConstants.ACCOUNT_TYPE_PHONE) {
			if (TextUtils.isEmpty(edt_regisetN.getText().toString())) {
				tipsTV.setText(mActivity.getResources().getString(R.string.ui_phone_empty_note));
				edt_phone_num_Lay.setBackgroundResource(R.drawable.input_error_shape);
				return false;
				
			} else if (!Tools.isNumeric(edt_regisetN.getText().toString())) {
				tipsTV.setText(mActivity.getResources().getString(R.string.ui_input_correct_phone));
				edt_phone_num_Lay.setBackgroundResource(R.drawable.input_error_shape);
				return false;
			}
			if (TextUtils.isEmpty(mCountryNum)) {
				tipsTV.setText(mActivity.getResources().getString(R.string.ui_sel_contory));
				edt_phone_num_Lay.setBackgroundResource(R.drawable.input_error_shape);
				return false;
			}

			if (TextUtils.isEmpty(edt_phone_verification_code.getText()
					.toString())
					|| edt_phone_verification_code.getText().toString()
							.length() != 4) {
				tipsTV.setText(mActivity.getResources().getString(R.string.ui_error_code_input_tips));
				edt_phone_num_Lay.setBackgroundResource(R.drawable.input_error_shape);
				return false;
			}

		} else {
			if (TextUtils.isEmpty(edt_regisetN.getText().toString())) {
				tipsTV.setText(mActivity.getResources().getString(R.string.ui_email_empty_note));
				edt_phone_num_Lay.setBackgroundResource(R.drawable.input_error_shape);
				return false;
			} else if (!Tools.mailAddressVerify(edt_regisetN.getText()
					.toString())) {
				tipsTV.setText(mActivity.getResources().getString(R.string.ui_input_correct_mial));
				edt_phone_num_Lay.setBackgroundResource(R.drawable.input_error_shape);
				return false;
			}
		}
		tipsTV.setText("");
		return true;
	
	}
	private boolean checkPsw(){

		if (TextUtils.isEmpty(edt_passwd.toString())) {
			tipsTV.setText(mActivity.getResources().getString(R.string.ui_re_pass_empty_note));
			return false;
		}
		else if (!Tools.isCorrectPswFormat(edt_passwd.getText().toString())) {
			tipsTV.setText(mActivity.getResources().getString(R.string.ui_pwd_wrong));
			edt_passwd.setBackgroundResource(R.drawable.input_error_shape);
			return false;
		}
		else if (edt_passwd.getText().toString().length() < 6
				|| edt_passwd.getText().toString().length() > 15) {
			tipsTV.setText(mActivity.getResources().getString(R.string.ui_user_pwd));
			edt_passwd.setBackgroundResource(R.drawable.input_error_shape);
			return false;
			
		}else if(TextUtils.isEmpty(edt_re_passwd.getText().toString())){
			tipsTV.setText(mActivity.getResources().getString(R.string.ui_re_pass_empty_note));
			edt_re_passwd.setBackgroundResource(R.drawable.input_error_shape);
			return false;
		}else if(!edt_passwd.getText().toString().equals(edt_re_passwd.getText().toString())){
			tipsTV.setText(mActivity.getResources().getString(R.string.ui_passwd_no_equal));
			edt_re_passwd.setBackgroundResource(R.drawable.input_error_shape);
			return false;
		}
		tipsTV.setText("");
		return true;
	
		
	}

	public boolean checkPramar(){
		if(checkUser()){
			return checkPsw();
		}else{
			return false;
		}
		
	
    }

	/**
	 * 倒计时
	 */
	CountDownTimer countdownTimer = new CountDownTimer(60 * 1000, 1000) {
		public void onTick(long millisUntilFinished) {
			btn_phone_verification_code.setEnabled(false);
			time = (int) (millisUntilFinished / 1000);
			btn_phone_verification_code.setTextSize(10);
			btn_phone_verification_code.setText(getString(
					R.string.ui_set_verify_send, String.valueOf(time)));
			int index_1 = btn_phone_verification_code.getText().toString().indexOf(String.valueOf(time));
			int index_2 = index_1+String.valueOf(time).length();
			SpannableStringBuilder builder = new SpannableStringBuilder(btn_phone_verification_code.getText().toString());
			ForegroundColorSpan greenSpan = new ForegroundColorSpan(mActivity.getResources().getColor(R.color.find_psw_tip_color_red));
			builder.setSpan(greenSpan, index_1, index_2,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			btn_phone_verification_code.setText(builder);
		}

		public void onFinish() {
			btn_phone_verification_code.setEnabled(true);
			btn_phone_verification_code.setTextSize(15);
			btn_phone_verification_code
					.setText(getString(R.string.ui_set_verify_resend));
			if (countdownTimer != null) {
				countdownTimer.cancel();
			}

		}
	};

	/**
	 * 注册成功后逻辑处理
	 * @param userName
	 */
	private void registerSuccess(String userName, String psw,RegisterModule.Response response) {

		StatisticsReportUtil.reportRegisterCount(ImmutableMap.of("UserName", userName));
		Bundle bundle = new Bundle();
		LoadingDialog.dissMiss();
		if (RegistFragement.currentModel == BusinessConstants.ACCOUNT_TYPE_PHONE) {
			bundle.putBoolean(IntentConstants.DATA_IS_PHONE_REGISTER, true);
		} else {
			bundle.putBoolean(IntentConstants.DATA_IS_PHONE_REGISTER, false);
		}
		replaceFragment(CompleteUserInfoFragment.class.getName(), bundle);

		UserInfo result = response.getData().getResult().get(0);
		if(Tools.isEmail(userName)) {
			SPUtils.get().put(PreferenceConstants.USER_ACCOUNT, userName);
		}else {
			SPUtils.get().put(PreferenceConstants.USER_ACCOUNT, userName.substring(userName.indexOf(mCountryNum)));
		}

		SPUtils.get().put(PreferenceConstants.USER_PASSWORD, psw);
		SPUtils.get().put(PreferenceConstants.USER_ID, result.getUserId());
		Alpha2Application.getAlpha2().setUserId(result.getUserId());
		SPUtils.get().put(PreferenceConstants.USER_NAME, result.getUserName());
		SPUtils.get().put(PreferenceConstants.USER_IMAGE, result.getUserImage());
		SPUtils.get().put(PreferenceConstants.USER_PHONE, result.getUserPhone());
		SPUtils.get().put(PreferenceConstants.USER_EMAIL, result.getUserEmail());
		SPUtils.get().put(PreferenceConstants.COUNTRY_NUM, result.getCountryCode());

		SPUtils.get().put(PreferenceConstants.USER_GENDER, result.getUserGender());
		SPUtils.get().put(PreferenceConstants.APP_RUN_TIMES, 2);
		loginSuccess();

	}
	public void doRegistByPhone(final String userName, final String psw, String verifyCode) {
		RegisterReponsitory.get().doRegister(userName, psw, BusinessConstants.ACCOUNT_TYPE_PHONE, verifyCode, new IRegisterDataSource.RegisterCallback() {


			@Override
			public void onSuccess(RegisterModule.Response response) {
				registerSuccess(userName, psw, response);
			}

			@Override
			public void onFail(ThrowableWrapper e) {

				LoadingDialog.dissMiss();
				ToastUtils.showShortToast(e.getMessage());
			}
		});
	}

	public void doRegistByEmail(final String userName, final String psw) {
		LoadingDialog.getInstance(mActivity).show();
		RegisterReponsitory.get().doRegister(userName, psw, BusinessConstants.ACCOUNT_TYPE_EMAIL, null, new IRegisterDataSource.RegisterCallback() {

			@Override
			public void onSuccess(RegisterModule.Response response) {
				registerSuccess(userName,psw, response);
			}

			@Override
			public void onFail(ThrowableWrapper e) {
				ToastUtils.showShortToast(e.getMessage());
			}
		});

	}

	public void doRegister() {
		if (checkPramar()) {
			if (currentModel == BusinessConstants.ACCOUNT_TYPE_PHONE) {
				doRegistByPhone(mCountryNum + edt_regisetN.getText().toString(),edt_passwd.getText().toString(),
						edt_phone_verification_code.getText().toString());

			} else {
				doRegistByEmail(edt_regisetN.getText().toString(), edt_passwd
						.getText().toString());
			}
		}

	}

	public void getMsgCode(String phone) {
		if (TextUtils.isEmpty(phone)) {
			ToastUtils.showShortToast( R.string.ui_phone_empty_note);
			return;
		}
		if (TextUtils.isEmpty(mCountryNum)) {
			Toast.makeText(mActivity, R.string.ui_sel_contory,
					Toast.LENGTH_SHORT).show();
			return;
		} else {
			LoadingDialog.getInstance(mActivity).show();
			RegisterReponsitory.get().getRegisterCode(mCountryNum + phone, String.valueOf(BusinessConstants.ACCOUNT_TYPE_PHONE), new IRegisterDataSource.GetRegisterCodeCallback() {
				@Override
				public void onSuccess() {
					LoadingDialog.dissMiss();
					ToastUtils.showLongToast( R.string.ui_check_msg);
					countdownTimer.start();
				}

				@Override
				public void onFail(ThrowableWrapper e) {
					LoadingDialog.dissMiss();
					ToastUtils.showShortToast(e.getMessage());
				}
			});

		}

	}



	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == Constants.GET_COUNTRY_REQUEST) {
			Bundle bundle = data.getExtras();
			countryName = bundle.getString("countryName");
			mCountryNum = bundle.getString("countryNumber").substring(1);
			countryNumTv.setText(bundle.getString("countryNumber"));
			countryNameTv.setText(countryName);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (countdownTimer != null)
			countdownTimer.cancel();
	}


	private void loginSuccess() {
		Constants.hasLoginOut = false;
		SPUtils.get().put(PreferenceConstants.LOGIN_TIME, Tools.getCurrentTime());
		String countryNum = SPUtils.get().getString(PreferenceConstants.COUNTRY_NUM, "");
		if (RegistFragement.needLogin
				|| StringUtils.isEmpty(countryNum)
				|| countryNum.equals("-1")) {
			RegistFragement.needLogin = false;
			Bundle bundle = new Bundle();
			LoadingDialog.dissMiss();
			if (RegistFragement.currentModel == BusinessConstants.ACCOUNT_TYPE_PHONE) {
				bundle.putBoolean(IntentConstants.DATA_IS_PHONE_REGISTER, true);
			} else {
				bundle.putBoolean(IntentConstants.DATA_IS_PHONE_REGISTER, false);
			}
			replaceFragment(CompleteUserInfoFragment.class.getName(), bundle);
		} else {
			   Alpha2Application.getInstance().removeActivity();
				Intent intent = new Intent(mActivity, MainPageActivity.class);
				startActivity(intent);


		}
	}

}
