package com.ubtech.utilcode.utils.network;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/**
 * 网络帮助模块
 * @author wenbiao.xie
 *
 */
public class NetworkHelper {

	private static String TAG = "NetworkHelper";
	public enum NetworkStatus{
		NetworkNotReachable,
		NetworkReachableViaWWAN,
		NetworkReachableViaWiFi,
	}
	
	public interface NetworkInductor
	{
		void onNetworkChanged(NetworkStatus status);
	}
	
	private static class HelperHolder {
		private static final NetworkHelper helper = new NetworkHelper();
	}
	
	public static NetworkHelper sharedHelper()
	{
		return HelperHolder.helper;
	}
	/*
	private static void load()
	{
		try {
			System.loadLibrary("networkhelper");
		} catch (Exception e) {
			
		}
	}*/
	
	boolean mRegistered = false;
	NetworkStatus mStatus = NetworkStatus.NetworkNotReachable;
	NetworkBroadcastReceiver mReceiver = new NetworkBroadcastReceiver();
	List<WeakReference<NetworkInductor>> mInductors;
	
	private NetworkHelper(){
		//load();		
		mInductors = new LinkedList<WeakReference<NetworkInductor>>();
	}
	
	public void registerNetworkSensor(Context context)
	{
		if (mRegistered)
			return;
		
		mRegistered = true;
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		if (info == null || !info.isAvailable())
		{
			mStatus = NetworkStatus.NetworkNotReachable;
		}
		else if (info.getType() == ConnectivityManager.TYPE_MOBILE)
		{
			mStatus = NetworkStatus.NetworkReachableViaWWAN;
			
		}
		else if (info.getType() == ConnectivityManager.TYPE_WIFI)
		{
			mStatus = NetworkStatus.NetworkReachableViaWiFi;
		}
		
		//native_set_network_status(mStatus.ordinal());
		
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		context.registerReceiver(mReceiver, intentFilter);
	}
	
	public void unregisterNetworkSensor(Context context)
	{
		if (!mRegistered)
			return;
		
		mRegistered = false;
		context.unregisterReceiver(mReceiver);
	}
	
	public NetworkStatus getNetworkStatus() {
		return mStatus;
	}
	
	public boolean isWifiActive() {
		return mStatus.equals(NetworkStatus.NetworkReachableViaWiFi);
	}
	
	public boolean isNetworkAvailable() {
		return !mStatus.equals(NetworkStatus.NetworkNotReachable);
	}
	
	public void addNetworkInductor(NetworkInductor inductor)
	{
		final List<WeakReference<NetworkInductor>> list = new ArrayList<WeakReference<NetworkInductor>>(mInductors);
		for (int i = 0; i < list.size(); i++) {
			WeakReference<NetworkInductor> inductorRef = list.get(i);
			NetworkInductor ind = inductorRef.get();
			if ( ind == inductor)
				return;
			else if (ind == null) {
				mInductors.remove(inductorRef);
			}
		}
		
		mInductors.add(new WeakReference<NetworkInductor>(inductor));
	}
	
	public void removeNetworkInductor(NetworkInductor inductor)
	{
		final List<WeakReference<NetworkInductor>> list = new ArrayList<WeakReference<NetworkInductor>>(mInductors);
		for (int i = 0; i < list.size(); i++) {
			WeakReference<NetworkInductor> inductorRef = list.get(i);
			NetworkInductor ind = inductorRef.get();
			if ( ind == inductor) {
				mInductors.remove(inductorRef);
				return;
			}else if (ind == null) {
				mInductors.remove(inductorRef);
			}
		}
	}
	
	protected void onNetworkChanged() {
		if (mInductors.size() == 0)
			return;
		
		final List<WeakReference<NetworkInductor>> list = new ArrayList<WeakReference<NetworkInductor>>(mInductors);		
		for (int i = 0; i < list.size(); i++) {
			WeakReference<NetworkInductor> inductorRef = list.get(i);
			NetworkInductor inductor = inductorRef.get();
			if (inductor != null) 
				inductor.onNetworkChanged(mStatus);
			else 
				mInductors.remove(inductorRef);
		}
	}
	
	protected class NetworkBroadcastReceiver extends BroadcastReceiver
	{
		@Override
		public void onReceive (Context context, Intent intent)
		{
			
			if (intent == null)
				return;

			String action = intent.getAction();
			if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION))
			{
				ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo info = manager.getActiveNetworkInfo();
				NetworkStatus ns = NetworkStatus.NetworkNotReachable;
				if (info == null || !info.isAvailable())
				{
					ns = NetworkStatus.NetworkNotReachable;
				}
				else if (info.getType() == ConnectivityManager.TYPE_MOBILE)
				{
					ns = NetworkStatus.NetworkReachableViaWWAN;
					
				}
				else if (info.getType() == ConnectivityManager.TYPE_WIFI)
				{
					ns = NetworkStatus.NetworkReachableViaWiFi;
				}
				
				if (!mStatus.equals(ns)) {
					mStatus = ns;
					//native_set_network_status(mStatus.ordinal());				
					onNetworkChanged();
				}
			}
		}
	}
	
	//private native static void native_set_network_status(int status);

}
