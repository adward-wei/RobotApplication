package com.ubtechinc.alpha2ctrlapp.ui.fragment.user;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseActivity;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.base.BaseFragment;

public class FindPwdByEmailFragment extends BaseFragment implements OnClickListener  {

	private TextView btn_go_to_email;
	private TextView txt_note;
	private String email ;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
//		init(false, getString(R.string.ui_login));
		((BaseActivity)getActivity()).setTopVisible();
		activity.btn_ignore.setVisibility(View.GONE);
		initView();
		initControlListener();
	}
	@Override
	public View onCreateFragmentView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_find_pwd_by_email, container, false);
	}

	
	@Override
	public void initView() {
		email = bundle.getString("email");
		txt_note = (TextView) mContentView.findViewById(R.id.txt_note);
		txt_note.setText(txt_note.getText().toString()
				.replace("?", email));
		btn_go_to_email = (TextView) mContentView.findViewById(R.id.btn_go_to_email);
		int index_1 = txt_note.getText().toString().indexOf(email);
		int index_2 = index_1+email.length();
		SpannableStringBuilder builder = new SpannableStringBuilder(txt_note.getText().toString());
		ForegroundColorSpan greenSpan = new ForegroundColorSpan(this.getResources().getColor(R.color.find_psw_tip_color_red));
		builder.setSpan(greenSpan, index_1-1, index_2+1,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		txt_note.setText(builder);
	}
	@Override
	public void initControlListener() {
		btn_go_to_email.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_go_to_email:
			activity.finish();
			break;
		default:
			break;
		}
		
	}
	
	
}

