package com.ubtechinc.alpha2ctrlapp.ui.activity.base;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.protobuf.GeneratedMessageLite;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.ActivitySupport;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.base.BaseHandler;
import com.ubtechinc.alpha2ctrlapp.entity.MessageEntity;
import com.ubtechinc.alpha2ctrlapp.im.Phone2RobotMsgMgr;
import com.ubtechinc.nets.phonerobotcommunite.ICallback;

import java.util.ArrayList;
import java.util.ListIterator;

public abstract class BaseContactActivity<T> extends ActivitySupport  {

	protected Context mContext;
	protected Alpha2Application mApplication;
	public RelativeLayout topLayout;
	public   TextView title;
	protected BaseHandler mHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.layout_base);
		mContext = this;
		mApplication = (Alpha2Application) mContext.getApplicationContext();
		this.topLayout = (RelativeLayout)findViewById(R.id.top_title);
		this.title = (TextView) findViewById(R.id.title);
		this.topLayout.setVisibility(View.GONE);
	}

	/**
	 * 用于向Alpha发送XMPP消息
	 * 
	 * @author zengdengyi
	 * @param obj
	 *            --->命令实体
	 * @param messageType
	 *            -->类型
	 *            --->mac地址
	 * @date 2015-1-16 下午2:35:43
	 */
	public void sendRequest(Object obj, int messageType) {
		MessageEntity message = new MessageEntity();
		message.setObj(obj);

	}

	public void sendRequest(int cmdId,  GeneratedMessageLite requestBody, @NonNull ICallback<T> dataCallback) {
		Phone2RobotMsgMgr.getInstance().sendDataToRobot(cmdId, "1", requestBody, Alpha2Application.getRobotSerialNo(),dataCallback );
	}
	/**
	 * 添加
	 * 
	 * @param fragmentClassName
	 * @param args
	 */
	public void addFragment(String fragmentClassName, Bundle args) {
		Fragment fragment = Fragment.instantiate(mContext, fragmentClassName, args);
		getFragmentManager().beginTransaction().add(R.id.layout_fragment_contanier, fragment, fragmentClassName).commit();
	}
	public void addFragment(String fragmentClassName) {
		addFragment(fragmentClassName, null);
	}
	public void onBack(View v){
		if (getFragmentManager().getBackStackEntryCount() > 0) {
			getFragmentManager().popBackStack();
		}else{
			this.finish();
		}
		/**
		 * 隐藏键盘
		 */
		 if(getCurrentFocus()!=null)  
	        {  
	            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))  
	            .hideSoftInputFromWindow(getCurrentFocus()  
	                    .getWindowToken(),  
	                    InputMethodManager.HIDE_NOT_ALWAYS);   
	       }  
	}
	/**
	 * 处理掉线---逆序
	 *
	 * @author zengdengyi
	 * @param mac
	 * @date 2015-1-19 上午10:06:39
	 */
	public void isCurrentAlpha2MacLostConnection(String mac) {
		if (Alpha2Application.currentAlpha2Mac.equals(mac)) {
			ArrayList<Activity> activitys = Alpha2Application.getActicitys();

			ListIterator<Activity> li = activitys.listIterator();// 获得ListIterator对象
			for (li = activitys.listIterator(); li.hasNext();) {// 将游标定位到列表结尾
				li.next();
			}
			for (; li.hasPrevious();) {// 逆序将列表中的activity finish
				li.previous().finish();
			}
		}
	}
	public void setTitle(String titleStr){
		title.setText(titleStr);
	}
	public  void setTitle(int titleId){
		title.setText(this.getString(titleId));
	}

	/**
	 * 打开动画打开activity
	 */
	protected void openActivity(Intent intent, Context context) {
		context.startActivity(intent);
		overridePendingTransition(R.anim.push_left_in, 0);

	}


}
