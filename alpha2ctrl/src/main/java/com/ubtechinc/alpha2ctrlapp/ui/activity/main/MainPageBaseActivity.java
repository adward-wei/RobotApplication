package com.ubtechinc.alpha2ctrlapp.ui.activity.main;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseContactActivity;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.base.BaseContactFragememt;

/**
 * 主页基础类
 */
public abstract class MainPageBaseActivity extends BaseContactActivity {
	//业务内容
	private ViewFlipper mLayoutBase;
	protected View mContentView;
	private FragmentTransaction ft;
	public BaseContactFragememt currentFragment;
	public boolean isMainFrag = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.mian_top_title);
		mLayoutBase = (ViewFlipper) super.findViewById(R.id.layout_container);
	}

	/**
	 * 点击返回键，把当前Fragment从栈中弹出
	 * @param v
     */
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
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}


	@Override
	public void setContentView(View view) {
		if (mLayoutBase.getChildCount() > 1) {
			mLayoutBase.removeViewAt(1);
		}
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1);
		mLayoutBase.addView(view, lp);
		this.mContentView = view;
		
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
	public void addFragment(String fragmentClassName, Bundle args) {
		Fragment fragment = Fragment.instantiate(mContext, fragmentClassName, args);
		getFragmentManager().beginTransaction().add(R.id.layout_fragment_contanier, fragment, fragmentClassName).commit();
	}

	/**
	 * 切换页签
	 * @param contentFragment 内容页
	 * @param needBack
	 * @param args
     */
	 public void onChangeFragment(Fragment contentFragment,boolean needBack,Bundle args) {
		
		    if(currentFragment!=null&&contentFragment.getClass().getName().equals(currentFragment.getClass().getName())){
		    	return;
		    }
		    isMainFrag = false;	
		    Fragment fragment = getFragmentManager().findFragmentByTag(contentFragment.getClass().getName());
			if (fragment == null) {
				fragment = Fragment.instantiate(mContext, contentFragment.getClass().getName());
			}
			((BaseContactFragememt) fragment).setBundle(args);
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.replace(R.id.layout_fragment_contanier, fragment, contentFragment.getClass().getName());
			ft.addToBackStack(contentFragment.getClass().getName());
			 if(getCurrentFocus()!=null)  
		        {  
		            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))  
		            .hideSoftInputFromWindow(getCurrentFocus()  
		                    .getWindowToken(),  
		                    InputMethodManager.HIDE_NOT_ALWAYS);   
		        }  
			ft.commit();
			
			
		}

	@Override
	public void onResume(){
		super.onResume();
	}
}
