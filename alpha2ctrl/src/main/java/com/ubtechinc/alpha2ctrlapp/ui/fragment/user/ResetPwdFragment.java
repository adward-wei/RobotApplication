package com.ubtechinc.alpha2ctrlapp.ui.fragment.user;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.constants.IntentConstants;
import com.ubtechinc.alpha2ctrlapp.data.user.IUserPwdSource;
import com.ubtechinc.alpha2ctrlapp.data.user.UserPwdReponsitory;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseActivity;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.base.BaseFragment;
import com.ubtechinc.alpha2ctrlapp.util.Tools;
import com.ubtechinc.nets.http.ThrowableWrapper;

/**
 * @ClassName ResetPwdFragment
 * @date 6/6/2017
 * @author tanghongyu
 * @Description 重置密码页
 * @modifier
 * @modify_time
 */
public class ResetPwdFragment extends BaseFragment implements OnClickListener {
	private EditText edt_pwd;
	private EditText edt_re_pwd;
	private TextView btn_start_find;
	private String mUserId;
	private String mUUID;
	private ImageView showPsw;
	private boolean isShowPsw = false;
	private RelativeLayout psw_lay;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// init(false, getString(R.string.ui_register));
		((BaseActivity) getActivity()).setTopVisible();
		init(false, getString(R.string.find_reset_pwd));
		activity.btn_ignore.setVisibility(View.GONE);
		initView();
		initControlListener();
	}

	@Override
	public View onCreateFragmentView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_reset_pwd, container, false);
	}

	@Override
	public void initView() {
		isShowPsw = false;
		mUserId = bundle.getString(IntentConstants.DATA_USER_ID);
		mUUID = bundle.getString(IntentConstants.DATA_UUID);
		edt_pwd = (EditText) mContentView.findViewById(R.id.edt_pwd);
		edt_re_pwd = (EditText) mContentView.findViewById(R.id.edt_re_pwd);
		btn_start_find = (TextView) mContentView
				.findViewById(R.id.btn_start_find);
		showPsw = (ImageView) mContentView.findViewById(R.id.btn_show_psw);
		psw_lay= (RelativeLayout)mContentView.findViewById(R.id.edt_pwd_lay);
	}

	@Override
	public void initControlListener() {
		// TODO Auto-generated method stub
		btn_start_find.setOnClickListener(this);
		showPsw.setOnClickListener(this);
		edt_pwd.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub

					psw_lay.setBackgroundResource(R.drawable.input_nomal);

			}
		});
		
		edt_pwd.addTextChangedListener(new TextWatcher() {

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
				if(!TextUtils.isEmpty(edt_pwd.getText().toString())&&!TextUtils.isEmpty(edt_re_pwd.getText().toString())){
					if(edt_pwd.getText().toString().equals(edt_re_pwd.getText().toString())){
						btn_start_find.setBackgroundResource(R.drawable.btn_button_able);
					}else{
						btn_start_find.setBackgroundResource(R.drawable.button_disable);
					}
					
				}else{
					btn_start_find.setBackgroundResource(R.drawable.button_disable);
				}

			}
		});
		edt_re_pwd.addTextChangedListener(new TextWatcher() {

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
				if(!TextUtils.isEmpty(edt_pwd.getText().toString())&&!TextUtils.isEmpty(edt_re_pwd.getText().toString())){
					if(edt_pwd.getText().toString().equals(edt_re_pwd.getText().toString())){
						btn_start_find.setBackgroundResource(R.drawable.btn_button_able);
					}else{
						btn_start_find.setBackgroundResource(R.drawable.button_disable);
					}
					
				}else{
					btn_start_find.setBackgroundResource(R.drawable.button_disable);
				}

			}
		});
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_start_find:
			resetPsw();
			break;
		case R.id.btn_show_psw:
			if (!isShowPsw) {
				edt_pwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				edt_re_pwd
						.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				showPsw.setImageResource(R.drawable.icon_eye_press);
				isShowPsw =true;
			} else {
				edt_pwd.setInputType(InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_VARIATION_PASSWORD);
				edt_re_pwd
						.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				showPsw.setImageResource(R.drawable.icon_eye_nomal);
				isShowPsw =false;
			}
			break;
		default:
			break;
		}

	}



	public boolean checkPramar() {

		if (TextUtils.isEmpty(edt_pwd.getText().toString())) {
			Toast.makeText(mActivity, R.string.ui_pass_empty_note,
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (TextUtils.isEmpty(edt_re_pwd.toString())) {
			Toast.makeText(mActivity, R.string.ui_re_pass_empty_note,
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (!edt_pwd.getText().toString().trim()
				.equals(edt_re_pwd.getText().toString().trim())) {
			Toast.makeText(mActivity, R.string.ui_passwd_no_equal,
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (edt_re_pwd.getText().toString().length() < 6
				|| edt_re_pwd.getText().toString().length() > 15) {
			Toast.makeText(mActivity, R.string.ui_user_pwd, Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		if (!Tools.isCorrectPswFormat(edt_re_pwd.getText().toString())) {
			Toast.makeText(mActivity, R.string.ui_pwd_wrong, Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		return true;

	}

	public void resetPsw() {
		if (checkPramar()) {
			UserPwdReponsitory.get().resetPwd(mUserId, edt_pwd.getText().toString(), mUUID, new IUserPwdSource.ResetPwdCallback() {
				@Override
				public void onSuccess() {
					ToastUtils.showLongToast( R.string.find_reset_pwd_success);
					replaceFragment(LoginFragement.class.getName(), bundle);
				}

				@Override
				public void onFail(ThrowableWrapper e) {
					ToastUtils.showLongToast( R.string.find_reset_pwd_fail);
				}
			});

		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		FindPwdByPhoneFragment.needResend = false;
	}

}
