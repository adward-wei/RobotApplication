package com.ubtechinc.alpha2ctrlapp.ui.fragment.base;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseActivity;

/**
 * @ClassName BaseFragment
 * @date 4/13/2017
 * @author tanghongyu
 * @Description Fragment基础初始化类，提供基础数据和基础方法
 * @modifier
 * @modify_time
 */
public abstract class BaseFragment extends Fragment {
	protected Activity mActivity;
	protected View mContentView;
	protected Bundle bundle;
	protected BaseActivity activity;
	protected Alpha2Application mApplication;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContentView = onCreateFragmentView(inflater, container, savedInstanceState);
		
		return mContentView;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		mActivity = getActivity();
		mApplication = (Alpha2Application) mActivity.getApplicationContext();
		activity =(BaseActivity)getActivity();
		activity.btn_ignore.setVisibility(View.GONE);
		super.onActivityCreated(savedInstanceState);
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
	public void onDestroy() {
		super.onDestroy();
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
	public  void clearFragment(String name){
		int backStackCount = getFragmentManager().getBackStackEntryCount();
		for(int i = 0; i < backStackCount-1; i++) {   
		    getFragmentManager().popBackStack();
		}

	}
	
	public abstract void initView();
	public abstract void initControlListener();
	/**
	 * [onCreateFragmentView abstract method, child class must implements the
	 * method]
	 * 
	 * @param inflater
	 * @param container
	 * @param savedInstanceState
	 * @return
	 */
	public abstract View onCreateFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
	
	
	public void replaceFragment(String fragmentClassName, Bundle args) {
		Fragment fragment = getFragmentManager().findFragmentByTag(fragmentClassName);
		if (fragment == null) {
			fragment = Fragment.instantiate(mActivity, fragmentClassName);
		}
		((BaseFragment) fragment).setBundle(args);
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.layout_fragment_contanier, fragment, fragmentClassName);
		ft.addToBackStack(fragmentClassName);

		slidingKeyBroad();
		ft.commit();
	}


	public  void init(boolean setButtonGone, String title){
		if(setButtonGone)
			activity.setBtnbackGone();
		else
			activity.setBtnbackVisible();
		activity.setTitle(title);
	}
}
