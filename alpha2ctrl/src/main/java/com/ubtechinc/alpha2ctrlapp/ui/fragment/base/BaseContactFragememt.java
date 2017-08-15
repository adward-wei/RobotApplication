package com.ubtechinc.alpha2ctrlapp.ui.fragment.base;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.BaseHandler;
import com.ubtechinc.alpha2ctrlapp.ui.activity.main.MainPageActivity;

/**
 * @ClassName BaseContactFragememt
 * @date 5/15/2017
 * @author tanghongyu
 * @Description
 * @modifier
 * @modify_time
 */
public abstract class BaseContactFragememt<T> extends Fragment {
	protected Activity mActivity;
	protected View mContentView;
	protected Bundle bundle;
	protected TextView title;
	public MainPageActivity mMainPageActivity;
	protected BaseHandler mHandler;
	protected Context mApplication;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMainPageActivity =  (MainPageActivity)getActivity();
		mActivity = getActivity();
		mApplication = getActivity().getApplicationContext();
	}
	public void replaceFragment(String fragmentClassName, Bundle args) {
		Fragment fragment = getFragmentManager().findFragmentByTag(
				fragmentClassName);
		if (fragment == null) {
			fragment = Fragment.instantiate(mActivity, fragmentClassName);
		}
		if(args!=null)
			((BaseContactFragememt) fragment).setBundle(args);
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.layout_fragment_contanier, fragment, fragmentClassName);
		ft.addToBackStack(fragmentClassName);
		slidingKeyBroad();
		ft.commit();
	}
	/**
	 * 收起键盘
	 */
	public void slidingKeyBroad(){
		if(getActivity().getCurrentFocus()!=null)
		{
			((InputMethodManager) getActivity().getSystemService(mActivity.INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(getActivity().getCurrentFocus()
									.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}




	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContentView = onCreateFragmentView(inflater, container,
				savedInstanceState);
		title = (TextView) mContentView.findViewById(R.id.authorize_title);
		return mContentView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		initView();
		super.onActivityCreated(savedInstanceState);
	}
	public abstract void initView();

	@Override
	public void onDestroyView() {
		// 清除所有跟视图相关的资源
		super.onDestroyView();
		mContentView = null;
	}



	/**
	 * 得到参数
	 * 
	 * @return
	 */
	public Bundle getBundle() {
		return bundle;
	}

	/**
	 * 设置参数
	 * 
	 * @param bundle
	 */
	public void setBundle(Bundle bundle) {
		this.bundle = bundle;
	}




	/**
	 * [onCreateFragmentView abstract method, child class must implements the
	 * method]
	 * 
	 * @param inflater
	 * @param container
	 * @param savedInstanceState
	 * @return
	 */
	public abstract View onCreateFragmentView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState);


	public void refreshDownLoadData(){

	}

		




}
