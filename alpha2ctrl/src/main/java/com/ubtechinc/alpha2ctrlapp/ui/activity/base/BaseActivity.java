package com.ubtechinc.alpha2ctrlapp.ui.activity.base;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.ActivitySupport;

public abstract class BaseActivity extends ActivitySupport {
	private static final String TAG = "BaseActivity";
	public Context mContext;
	protected ImageButton btn_left;
	protected LinearLayout btn_back;
	protected TextView tv_title;
	private ViewFlipper mLayoutBase;
	protected View mContentView;
	protected RelativeLayout topLay;
	public Button btn_ignore ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.layout_base);
		this.mContext = this;
		topLay = (RelativeLayout)findViewById(R.id.top_title);
		mLayoutBase = (ViewFlipper) findViewById(R.id.layout_container);
		btn_back = (LinearLayout)findViewById(R.id.btn_back);
		tv_title = (TextView)findViewById(R.id.title);
		btn_ignore = (Button)findViewById(R.id.btn_ignore);
	}
	@SuppressLint("NewApi")
	public void onBack(View v){
		if (getFragmentManager().getBackStackEntryCount() > 0) {
			getFragmentManager().popBackStack();
		}
	    if(getCurrentFocus()!=null)  
        {  
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
            .hideSoftInputFromWindow(getCurrentFocus()  
                    .getWindowToken(),  
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }  

	    Logger.i( "BaseActivity B");
	}
	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mContentView = null;
		mContext = null;
	}

	@Override
	public void setContentView(View view) {
		if (mLayoutBase.getChildCount() > 1) {
			mLayoutBase.removeViewAt(1);
		}
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1);
		mLayoutBase.addView(view, lp);
		this.mContentView = view;
		// CommonUtils.showTitlePopupWindow(mActivity, mContentView);
	}

	@Override
	public void setContentView(int layoutResID) {
		View view = LayoutInflater.from(this).inflate(layoutResID, null);
		setContentView(view);
	}
	/**
	 * 添加Fragment
	 * 
	 * @param fragmentClassName
	 */
	public void addFragment(String fragmentClassName) {
		addFragment(fragmentClassName, null);
	}

	/**
	 * 添加Fragment
	 * 
	 * @param fragmentClassName
	 * @param args
	 */
	@SuppressLint("NewApi")
	public void addFragment(String fragmentClassName, Bundle args) {
		Fragment fragment = Fragment.instantiate(mContext, fragmentClassName, args);
		getFragmentManager().beginTransaction().add(R.id.layout_fragment_contanier, fragment, fragmentClassName).commit();
	}

	
	/**
	 * 设置标题
	 */
	public void setTitle(int titleId) {
		tv_title.setText(getString(titleId));
	}

	/**
	 * 设置标题
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		tv_title.setText(title);
	}
	
	public void setBtnbackGone(){
		btn_back.setVisibility(View.GONE);
	}
	public void setBtnbackVisible(){
		btn_back.setVisibility(View.VISIBLE);
	}
	
	public void setTopGone(){
		topLay.setVisibility(View.GONE);
	}
	public void setTopVisible(){
		topLay.setVisibility(View.VISIBLE);
	}
	@SuppressLint("NewApi")
	public void back(){
		if (getFragmentManager().getBackStackEntryCount() > 0) {
			getFragmentManager().popBackStack();
		}
		 Logger.i( "BaseActivity b");
	}

	
	@Override
	public void onResume(){
		super.onResume();
	}
}
