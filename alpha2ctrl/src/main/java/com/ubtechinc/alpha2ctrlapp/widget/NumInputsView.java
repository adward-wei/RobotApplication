package com.ubtechinc.alpha2ctrlapp.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.ActivitySupport;
import com.ubtechinc.alpha2ctrlapp.util.Tools;

public class NumInputsView extends LinearLayout {

	private EditIndexView[] edt_nums;
	private int index =0;
	private ActivitySupport mContext;

	public NumInputsView(Context context) {
		super(context);
	}

	public NumInputsView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context).inflate(R.layout.customer_num_input, this,
				true);
		mContext =(ActivitySupport) context;
		edt_nums = new EditIndexView[] {
				(EditIndexView) findViewById(R.id.edt_1),
				(EditIndexView) findViewById(R.id.edt_2),
				(EditIndexView) findViewById(R.id.edt_3),
				(EditIndexView) findViewById(R.id.edt_4),
				(EditIndexView) findViewById(R.id.edt_5),
				(EditIndexView) findViewById(R.id.edt_6),
				(EditIndexView) findViewById(R.id.edt_7),
				(EditIndexView) findViewById(R.id.edt_8),
				(EditIndexView) findViewById(R.id.edt_9),
				(EditIndexView) findViewById(R.id.edt_10),
				(EditIndexView) findViewById(R.id.edt_11),
				(EditIndexView) findViewById(R.id.edt_12),};

		for (int i = 0; i < edt_nums.length - 1; i++){
			edt_nums[i].the_next = edt_nums[i + 1];
			
		}
		for (int i = 0; i < edt_nums.length; i++){
			if(i==0){
				edt_nums[0].the_front =null;
			}else{
				edt_nums[i].the_front =edt_nums[i-1];
			}
//			edt_nums[i].setKeyListener(DialerKeyListener.getInstance());
		}
			

		for (int i = 0; i < edt_nums.length; i++) {

			edt_nums[i]
					.addTextChangedListener(new TextIndexWatcher(edt_nums[i],i));

		}
	}

	public String getText() {
		String txt = "";
		for (int i = 0; i < edt_nums.length; i++) {
			txt += edt_nums[i].getText().toString();
		}
		return txt;
	}
	public void  clear(){
		for (int i = 0; i < edt_nums.length; i++) {
			edt_nums[i].setText("");
		}
		edt_nums[0].requestFocus();
	}
	public void dele(){
		if(edt_nums[index].getText().toString().equals("") && index>0){
			edt_nums[index-1].setText("");
			edt_nums[index-1].requestFocus();
			index--;
		}else{
			edt_nums[index].setText("");
			edt_nums[index].requestFocus();
		}
	
	}

	class TextIndexWatcher implements TextWatcher {

		private EditIndexView mView;
		private int mi ;

		public TextIndexWatcher(EditIndexView view, int i) {
			mView = view;
			mi= i;
		}

		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
			if (arg0.length() == 1&& Tools.isLetterOrDigital(arg0.toString())) {
				mView.setTextColor(getResources().getColor(R.color.white));
				if (mView.the_next != null){
					mView.the_next.requestFocus();
					index =mi+1;
				}else{
					index = mi;
					mView.requestFocus();
					 if(mContext.getCurrentFocus()!=null)  
				        {  
				            ((InputMethodManager)mContext. getSystemService(mContext.INPUT_METHOD_SERVICE))  
				            .hideSoftInputFromWindow(mContext.getCurrentFocus()  
				                    .getWindowToken(),  
				                    InputMethodManager.HIDE_NOT_ALWAYS);   
				        }  
				}
				
			}
//			else if(arg0.length()==0){
//				if(mView.the_front!=null){
//					mView.the_front.requestFocus();
//					index =mi-1;
//				}else{
//					index = mi;
//				}
//				
//			}
			
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			if(arg0.length()==0){
				if(mView.the_front!=null){
					mView.the_front.requestFocus();
				}
			}
		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub

		}

	}

	public EditIndexView[] getEdt_nums() {
		return edt_nums;
	}

	public void setEdt_nums(EditIndexView[] edt_nums) {
		this.edt_nums = edt_nums;
	}

}
