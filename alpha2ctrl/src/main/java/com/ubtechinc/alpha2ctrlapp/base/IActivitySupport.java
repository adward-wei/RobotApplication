package com.ubtechinc.alpha2ctrlapp.base;

/**
 * Activity帮助支持类接口.
 * 
 * @author shimiso
 */
public interface IActivitySupport {
	
	
	/**
	 * 
	 * 校验网络-如果没有网络就弹出设置,并返回true.
	 * 
	 * @return
	 * @author shimiso
	 * @update 2012-7-6 上午9:03:56
	 */
	public abstract boolean validateInternet();
//	public abstract Alpha2Application getAplication();
	/**
	 * 
	 * 校验网络-如果没有网络就返回true.
	 * 
	 * @return
	 * @author shimiso
	 * @update 2012-7-6 上午9:05:15
	 */
	public abstract boolean hasInternetConnected();


	/**
	 * 
	 * 判断GPS是否已经开启.
	 * 
	 * @return
	 * @author shimiso
	 * @update 2012-7-6 上午9:04:07
	 */
	public abstract boolean hasLocationGPS();

	/**
	 * 
	 * 判断基站是否已经开启.
	 * 
	 * @return
	 * @author shimiso
	 * @update 2012-7-6 上午9:07:34
	 */
	public abstract boolean hasLocationNetWork();



	/**
	 * 
	 * 发出Notification的method.
	 * 
	 * @param iconId
	 *            图标
	 * @param contentTitle
	 *            标题
	 * @param contentText
	 *            你内容
	 * @param activity
	 * @author shimiso
	 * @update 2012-5-14 下午12:01:55
	 */
	public void setNotiType(int iconId, String contentTitle,
							String contentText, Class activity, String from);
}
