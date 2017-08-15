package com.ubtechinc.alpha2ctrlapp.ui.fragment.user;

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
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.constants.IntentConstants;
import com.ubtechinc.alpha2ctrlapp.data.user.IUserPwdSource;
import com.ubtechinc.alpha2ctrlapp.data.user.UserPwdReponsitory;
import com.ubtechinc.alpha2ctrlapp.entity.net.VerifyResetPwdCodeModule;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseActivity;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.base.BaseFragment;
import com.ubtechinc.alpha2ctrlapp.util.Tools;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.LoadingDialog;
import com.ubtechinc.nets.http.ThrowableWrapper;

/**
 * @ClassName FindPwdByPhoneFragment
 * @date 6/6/2017
 * @author tanghongyu
 * @Description 通过找回密码
 * @modifier
 * @modify_time
 */
public class FindPwdByPhoneFragment extends BaseFragment implements OnClickListener {
	private TextView txt_note;
	private int totle_time = 60;
	private EditText edt_phone_verification_code;
	private TextView btn_phone_verification_code;
	private TextView btn_submit;
	private int time;
	public static  boolean needResend;
	private String phone;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
//		init(false, getString(R.string.ui_register));
		((BaseActivity)getActivity()).setTopVisible();
		activity.btn_ignore.setVisibility(View.GONE);
		initView();
		initControlListener();
	}
	@Override
	public View onCreateFragmentView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_find_pwd_by_phone, container, false);
	}


	@Override
	public void initView() {
		phone = bundle.getString(IntentConstants.DATA_PHONE_NUMBER);
		txt_note = (TextView) mContentView.findViewById(R.id.txt_note);
		txt_note.setText(txt_note.getText().toString().replace("?", "+"+phone));
		int index_1 = txt_note.getText().toString().indexOf(phone);
		int index_2 = index_1+phone.length();
		SpannableStringBuilder builder = new SpannableStringBuilder(txt_note.getText().toString());
		ForegroundColorSpan greenSpan = new ForegroundColorSpan(this.getResources().getColor(R.color.find_psw_tip_color_red));
		builder.setSpan(greenSpan, index_1-2, index_2+1,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		txt_note.setText(builder);
		btn_phone_verification_code = (TextView) mContentView.findViewById(R.id.btn_phone_verification_code);
		btn_submit = (TextView) mContentView.findViewById(R.id.btn_submit);
		edt_phone_verification_code = (EditText) mContentView.findViewById(R.id.edt_phone_verification_code);
		edt_phone_verification_code.setText("");
		if(needResend)
			countdownTimer.start();
		
		edt_phone_verification_code.addTextChangedListener(new TextWatcher() {

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
				edt_phone_verification_code.setBackgroundResource(R.drawable.input_nomal);
				if(Tools.isNumeric(edt_phone_verification_code.getText().toString())&&edt_phone_verification_code.getText().length()==4){
						btn_submit.setBackgroundResource(R.drawable.btn_button_able);
				}else{
						btn_submit.setBackgroundResource(R.drawable.button_disable);
				}

			}
		});
	}
	   
		@Override
		public void initControlListener() {
			// TODO Auto-generated method stub			
			btn_submit.setOnClickListener(this);
			btn_phone_verification_code.setOnClickListener(this);
		}
		@Override
		public void onResume(){
			super.onResume();
			
		}		
		@Override
		public void onClick(View v) {
			
			switch (v.getId()) {
	
			case R.id.btn_submit:
				checkCode(edt_phone_verification_code.getText().toString());
			    break;
			case R.id.btn_phone_verification_code:
				getMsgCode(phone);
				break;
			default:
				break;
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
				btn_phone_verification_code.setText(getString(R.string.ui_set_verify_send, String.valueOf(time)));
	
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
				btn_phone_verification_code.setText(getString(R.string.ui_set_verify_resend));
				if (countdownTimer != null) {
					countdownTimer.cancel();
				}
	
			}
		};
	public void getMsgCode(String phone) {
			if(TextUtils.isEmpty(phone)){
				ToastUtils.showShortToast( R.string.ui_phone_empty_note);
			}else{
				LoadingDialog.getInstance(mActivity).show();

				UserPwdReponsitory.get().getVerifyCode(phone, BusinessConstants.ACCOUNT_TYPE_PHONE, new IUserPwdSource.GetVerifyCodeCallback() {
					@Override
					public void onSuccess() {

						ToastUtils.showLongToast( R.string.ui_check_msg);
						countdownTimer.start();

					}

					@Override
					public void onFail(ThrowableWrapper e) {
						ToastUtils.showShortToast(e.getMessage());
					}
				});
			}
			
		
	}
	public void checkCode(String code) {
		
		if(TextUtils.isEmpty(code)
				||code.length()!=4){
			ToastUtils.showShortToast( R.string.ui_error_code_input_tips);
		}if(TextUtils.isEmpty(phone)){
			ToastUtils.showShortToast( R.string.ui_phone_empty_note);
		}else{

			UserPwdReponsitory.get().checkVerifyCode(phone, BusinessConstants.ACCOUNT_TYPE_PHONE, code, new IUserPwdSource.CheckVerifyCodeCallback() {
				@Override
				public void onSuccess(VerifyResetPwdCodeModule.Response response) {
					if (countdownTimer != null) {
						countdownTimer.cancel();
					}
					LoadingDialog.dissMiss();
					VerifyResetPwdCodeModule.Result result = response.getData().getResult().get(0);
					;
					;
					bundle = new Bundle();
					bundle.putString(IntentConstants.DATA_USER_ID, result.getUserId());
					bundle.putString(IntentConstants.DATA_UUID, result.getUuid());
					replaceFragment(ResetPwdFragment.class.getName(), bundle);
				}

				@Override
				public void onFail(ThrowableWrapper e) {
					ToastUtils.showShortToast(e.getMessage());
				}
			});
			LoadingDialog.getInstance(mActivity).show();
		}
	}
	@Override
	public void onDestroy(){
		super.onDestroy();
		if(countdownTimer!=null)
			countdownTimer.cancel();
	}	
	
	
}
