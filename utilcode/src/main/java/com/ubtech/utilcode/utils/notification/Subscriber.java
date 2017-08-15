package com.ubtech.utilcode.utils.notification;

/**
 * 对象型消息订阅者接口
 * @author wenbiao.xie
 * 
 * @param <T>
 */
public interface Subscriber<T>
{
	/**
      * Handle a published event. 
      *
      * @param event The Object that is being published.
    **/
	public void onEvent(T event);
}
