package com.ubtechinc.alpha2ctrlapp.ui.fragment.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ant.country.CountryActivity;
import com.ubtech.utilcode.utils.SPUtils;
import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.BaseHandler;
import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.constants.IntentConstants;
import com.ubtechinc.alpha2ctrlapp.constants.NetWorkConstant;
import com.ubtechinc.alpha2ctrlapp.constants.PreferenceConstants;
import com.ubtechinc.alpha2ctrlapp.data.user.IUserPwdSource;
import com.ubtechinc.alpha2ctrlapp.data.user.UserPwdReponsitory;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseActivity;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.base.BaseFragment;
import com.ubtechinc.alpha2ctrlapp.util.Tools;
import com.ubtechinc.nets.http.ThrowableWrapper;

public class FindPwdFragment extends BaseFragment implements OnClickListener  {
	private EditText edt_account;
	private TextView btn_start_find,btn_email, btn_phone;;
	private MHandler handler ;
	private String countName,countryNum;
	private LinearLayout edt_countryCode ;
	private int currentModel = BusinessConstants.ACCOUNT_TYPE_PHONE;
	private TextView countryNameTv, countryNumTv;
	private LinearLayout edt_phone_num_Lay;
	private ImageView btn_delete;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		init(false, getString(R.string.find_psw_title));
		((BaseActivity)getActivity()).setTopVisible();
		activity.btn_ignore.setVisibility(View.GONE);
		initView();
		initControlListener();
	}
	@Override
	public View onCreateFragmentView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_find_pwd, container, false);
	}

	

	@Override
	public void initView() {
		handler = new MHandler();
		btn_start_find = (TextView) mContentView.findViewById(R.id.btn_start_find);
		edt_account = (EditText) mContentView.findViewById(R.id.edt_account);
		edt_countryCode	= (LinearLayout) mContentView.findViewById(R.id.choose_country);
		btn_delete = (ImageView)mContentView.findViewById(R.id.btn_delete);
		edt_account.setHorizontallyScrolling(true);
		btn_phone = (TextView) mContentView.findViewById(
				R.id.btn_login_by_phone);
		btn_email = (TextView) mContentView.findViewById(
				R.id.btn_login_by_email);
		countryNameTv = (TextView) mContentView.findViewById(
				R.id.countryNameTv);
		countryNumTv = (TextView) mContentView.findViewById(R.id.countryNumTv);
		btn_phone.setOnClickListener(this);
		btn_email.setOnClickListener(this);
		countryNum= SPUtils.get().getString(PreferenceConstants.COUNTRY_NUM, "-1");
		countryNameTv.setText(SPUtils.get().getString(PreferenceConstants.COUNTRY_NAME, ""));
		if(!countryNum.equals("null") &&!countryNum.equals("")&&!countryNum.equals("-1")){
			countryNumTv.setText("+"+countryNum);
		}else{
			countryNum ="86";
			countName = "CN";
			countryNameTv.setText(countName);
			countryNumTv.setText("+"+countryNum);
					
		}
		if(TextUtils.isEmpty(countName)){
			countryNum ="86";
			countName = "CN";
			countryNameTv.setText(countName);
			countryNumTv.setText("+"+countryNum);
		}
		edt_phone_num_Lay = (LinearLayout)mContentView.findViewById(R.id.edt_phone_num_Lay);
		refreshMode(1);
	}
	@Override
	public void initControlListener() {
		btn_start_find.setOnClickListener(this);
		edt_countryCode.setOnClickListener(this);
		btn_delete.setOnClickListener(this);
		edt_account.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {


					edt_phone_num_Lay.setBackgroundResource(R.drawable.input_nomal);
			}
		});
		edt_account.addTextChangedListener(new TextWatcher() {

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
				if(!TextUtils.isEmpty(edt_account.getText().toString())){
					if(currentModel==BusinessConstants.ACCOUNT_TYPE_PHONE){
						if(!Tools.isNumeric(edt_account.getText().toString())){
							btn_start_find.setBackgroundResource(R.drawable.button_disable);
						}else{
							btn_start_find.setBackgroundResource(R.drawable.btn_button_able);
						}
					}else {
						if(!Tools.isEmail(edt_account.getText().toString())){
							btn_start_find.setBackgroundResource(R.drawable.button_disable);
						}else{
							btn_start_find.setBackgroundResource(R.drawable.btn_button_able);
						}
					}
					btn_delete.setVisibility(View.VISIBLE);

				}else{
					btn_start_find.setBackgroundResource(R.drawable.button_disable);
					btn_delete.setVisibility(View.GONE);
				}
			}
		});
	}
	
	private class MHandler extends BaseHandler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// TODO Auto-generated method stub
			
			switch (msg.what) {

			case NetWorkConstant.RESPONSE_FIND_PSW_FAILED:
				break;
			case NetWorkConstant.RESPONSE_FIND_PSW_SUCCESS:
				bundle = new Bundle();
				bundle.putString("email", edt_account.getText().toString());
				replaceFragment(FindPwdByEmailFragment.class.getName(), bundle);
				break;

			}
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_start_find:
			doStartFind(edt_account.getText().toString());
			break;
		case R.id.find_psw_tv:
			break;
		case R.id.choose_country:
			Intent intent = new Intent();
			intent.setClass(mActivity,
					CountryActivity.class);
			activity.startActivityForResult(intent, Constants.GET_COUNTRY_REQUEST);
			break;
		case R.id.btn_login_by_phone:
			if (currentModel != BusinessConstants.ACCOUNT_TYPE_PHONE)
				refreshMode(BusinessConstants.ACCOUNT_TYPE_PHONE);
			break;
		case R.id.btn_login_by_email:
			if (currentModel != BusinessConstants.ACCOUNT_TYPE_EMAIL)
				refreshMode(BusinessConstants.ACCOUNT_TYPE_EMAIL);
			break;
		case R.id.btn_delete:
			edt_account.setText("");
			btn_delete.setVisibility(View.GONE);
			break;
		default:
			break;
		}
		
	}
	private void refreshMode(int type) {
		currentModel = type;
		if (type == BusinessConstants.ACCOUNT_TYPE_PHONE) {
			edt_account.setHint(R.string.ui_phone_input);
			btn_phone.setTextColor(getResources().getColor(R.color.text_color_t5));
			btn_email.setTextColor(getResources().getColor(R.color.text_color_t4));
			edt_countryCode.setVisibility(View.VISIBLE);

			
		} else {
			edt_account.setHint(R.string.ui_email_input);
			edt_countryCode.setVisibility(View.GONE);
			btn_phone.setTextColor(getResources().getColor(R.color.text_color_t4));
			btn_email.setTextColor(getResources().getColor(R.color.text_color_t5));
		}

	}
	
	private boolean CheckPramar(String name) {
		if(currentModel==BusinessConstants.ACCOUNT_TYPE_EMAIL){
			if (TextUtils.isEmpty(name)) {
				Toast.makeText(mActivity, R.string.ui_phone_empty_note,
						Toast.LENGTH_SHORT).show();
				edt_phone_num_Lay.setBackgroundResource(R.drawable.input_error_shape);
				return false;
			} 
			else if (!Tools.isNumeric(name)) {
				Toast.makeText(mActivity, R.string.ui_input_correct_phone,
						Toast.LENGTH_SHORT).show();
				edt_phone_num_Lay.setBackgroundResource(R.drawable.input_error_shape);
				return false;
			} else {
				if (countryNum.equals("")||TextUtils.isEmpty(countryNum)||countryNum.equals("-1")) {
					Toast.makeText(mActivity, R.string.ui_sel_contory,
							Toast.LENGTH_SHORT).show();
					edt_phone_num_Lay.setBackgroundResource(R.drawable.input_error);
					return false;
				}
			}
		}else if(currentModel ==BusinessConstants.ACCOUNT_TYPE_EMAIL){
			if (TextUtils.isEmpty(name)) {
				Toast.makeText(mActivity, R.string.ui_email_empty_note,
						Toast.LENGTH_SHORT).show();
				edt_phone_num_Lay.setBackgroundResource(R.drawable.input_error_shape);
				return false;
			} else
			if(!Tools.isEmail(name)){
				Toast.makeText(mActivity, R.string.ui_input_correct_mial,
						Toast.LENGTH_SHORT).show();
				edt_phone_num_Lay.setBackgroundResource(R.drawable.input_error_shape);
				return false;
			}
		}
		return true;
	}
	public void doStartFind(String account) {
		if(CheckPramar(account)){
			if(currentModel== BusinessConstants.ACCOUNT_TYPE_PHONE){
				// 如果用手机号码找回
				if(Tools.isNumeric(account)&& account.length()>5){
					getMsgCode(countryNum+account);
				}else{
					ToastUtils.showShortToast( R.string.find_psw_intput_name);
				}
			}else{
				// 如果通过邮箱找回
//				findByEmail(account);
			}
		}
	}
//	private void findByEmail(String account){
//		FindPswByEmailRequest request  = new FindPswByEmailRequest();
//		request.setUserEmail(account);
//		UserAction action  =  UserAction.getInstance(mContext, this);
//		action.SetFragent(this);
//		action.setHandler(handler);
//		action.setParamerObj(request);
//		action.doRequest(NetWorkConstant.REQUEST_FIND_PSW,NetWorkConstant.find_psw);
//		LoadingDialog.getInstance(mContext).show();
//	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == Constants.GET_COUNTRY_REQUEST) {
			Bundle bundle = data.getExtras();
			countName = bundle.getString("countryName");
			countryNum = bundle.getString("countryNumber").substring(1);
			countryNameTv.setText(countName);
			countryNumTv.setText(bundle.getString("countryNumber"));
		}
	}
	
	public void getMsgCode(String phone) {
		if(TextUtils.isEmpty(phone)){
			ToastUtils.showShortToast( R.string.ui_phone_empty_note);
		}else{

			UserPwdReponsitory.get().getVerifyCode(phone, BusinessConstants.ACCOUNT_TYPE_PHONE, new IUserPwdSource.GetVerifyCodeCallback() {
				@Override
				public void onSuccess() {
					ToastUtils.showShortToast( R.string.ui_check_msg);
					bundle = new Bundle();
					bundle.putString(IntentConstants.DATA_PHONE_NUMBER, countryNum+edt_account.getText().toString());
					FindPwdByPhoneFragment.needResend = true;
					replaceFragment(FindPwdByPhoneFragment.class.getName(), bundle);
				}

				@Override
				public void onFail(ThrowableWrapper e) {
					ToastUtils.showShortToast(e.getMessage());
				}
			});
		}
	
}
	
	
}

