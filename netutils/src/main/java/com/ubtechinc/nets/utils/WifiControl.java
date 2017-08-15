package com.ubtechinc.nets.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.AuthAlgorithm;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WifiControl {
	private final static String TAG = "WifiControl";
	private StringBuffer mStringBuffer = new StringBuffer();
	private List<ScanResult> listResult;
	private ScanResult mScanResult;
	// 定义WifiManager对象
	private WifiManager mWifiManager;
	// 定义WifiInfo对象
	private WifiInfo mWifiInfo;
	// 网络连接列表
	private List<WifiConfiguration> mWifiConfiguration;
	// 定义一个WifiLock
	WifiLock mWifiLock;
	Context mContext;

	private WifiConfiguration mLastConnectConfigure;

	/**
	 * 构造方法
	 */
	private WifiControl(Context context) {
		mContext = context;
		try {
			mWifiManager = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
		} catch (Exception e) {
			Log.v("chenlin", e.toString());
		}
		mWifiInfo = mWifiManager.getConnectionInfo();
	}
	//使用volatile关键字保其可见性
	volatile private static WifiControl INSTANCE = null;

	public static WifiControl get(Context context) {

			if(INSTANCE != null){//懒汉式

			}else{
				synchronized (WifiControl.class) {
					if(INSTANCE == null){//二次检查
						INSTANCE = new WifiControl(context);
					}
				}
			}

		return INSTANCE;
	}



	public String getNetbroadcastAddr() {
		try {
			if (mWifiManager == null)
				return null;
			DhcpInfo d = mWifiManager.getDhcpInfo();
			if (d == null)
				return null;
			return intToIp(d.ipAddress & d.netmask | (~d.netmask));
		} catch (Exception e) {
			return null;
		}
	}

	private String intToIp(int paramInt) {
		return (paramInt & 0xFF) + "." + (0xFF & paramInt >> 8) + "."
				+ (0xFF & paramInt >> 16) + "." + (0xFF & paramInt >> 24);
	}

	public static boolean hasActiveInternetConnection() {
			try {
				HttpURLConnection urlc = (HttpURLConnection) (new URL("http://cn.bing.com/").openConnection());
				urlc.setRequestProperty("User-Agent", "Test");
				urlc.setRequestProperty("Connection", "close");
				urlc.setConnectTimeout(5000);
				urlc.connect();
				return (urlc.getResponseCode() == 200);
			} catch (IOException e) {
				Log.e(TAG, "Error checking internet connection", e);
			}

		return false;
	}

	/*
	 * @author suncat
	 *
	 * @category 判断是否有外网连接（普通方法不能判断外网的网络是否连接，比如连接上局域网）
	 *
	 * @return
	 */
	public static boolean ping() {
		String result = null;
		try {
			String ip = "www.baidu.com";// ping 的地址，可以换成任何一种可靠的外网
			Process p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + ip);// ping网址3次
			// 读取ping的内容，可以不加
			InputStream input = p.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(input));
			StringBuffer stringBuffer = new StringBuffer();
			String content = "";
			while ((content = in.readLine()) != null) {
				stringBuffer.append(content);
			}
			Log.d("------ping-----",
					"result content : " + stringBuffer.toString());
			// ping的状态
			int status = p.waitFor();
			if (status == 0) {
				result = "success";
				return true;
			} else {
				result = "failed";
			}
		} catch (IOException e) {
			result = "IOException";
		} catch (InterruptedException e) {
			result = "InterruptedException";
		} finally {
			Log.d("----result---", "result = " + result);
		}
		return false;
	}

	/**
	 * 打开Wifi网卡
	 */
	public void openNetCard() {
		if (!mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(true);
		}
	}

	/**
	 * 是否打开WIFI
	 *
	 */
	public boolean isEnable() {
		return mWifiManager.isWifiEnabled();
	}

	/**
	 * 关闭Wifi网卡
	 */
	public void closeNetCard() {
		if (mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(false);
		}
	}

	/**
	 * 检查当前Wifi网卡状态
	 */
	public int checkNetCardState() {
		if (mWifiManager.getWifiState() == 0) {
			Log.i(TAG, "网卡正在关闭");
		} else if (mWifiManager.getWifiState() == 1) {
			Log.i(TAG, "网卡已经关闭");
		} else if (mWifiManager.getWifiState() == 2) {
			Log.i(TAG, "网卡正在打开");
		} else if (mWifiManager.getWifiState() == 3) {
			Log.i(TAG, "网卡已经打开");
		} else {
			Log.i(TAG, "---_---晕......没有获取到状态---_---");
		}

		return mWifiManager.getWifiState();
	}

	/**
	 * 扫描周边网络
	 */
	public void scan() {
		mWifiManager.startScan();
		listResult = mWifiManager.getScanResults();
		if (listResult != null) {
			Log.i(TAG, "当前区域存在无线网络，请查看扫描结果");
		} else {
			Log.i(TAG, "当前区域没有无线网络");
		}
	}

	/**
	 * 得到扫描结果
	 */
	public String getScanResult() {
		// 每次点击扫描之前清空上一次的扫描结果
		if (mStringBuffer != null) {
			mStringBuffer = new StringBuffer();
		}
		// 开始扫描网络
		scan();
		listResult = mWifiManager.getScanResults();
		if (listResult != null) {
			for (int i = 0; i < listResult.size(); i++) {
				mScanResult = listResult.get(i);
				mStringBuffer = mStringBuffer.append("NO.").append(i + 1)
						.append(" :").append(mScanResult.SSID).append("->")
						.append(mScanResult.BSSID).append("->")
						.append(mScanResult.capabilities).append("->")
						.append(mScanResult.frequency).append("->")
						.append(mScanResult.level).append("->")
						.append(mScanResult.describeContents()).append("\n\n");
			}
		}
		Log.i(TAG, mStringBuffer.toString());
		return mStringBuffer.toString();
	}

	/**
	 * 得到扫描结果
	 */
	public List<ScanResult> getScanResultForList() {
		// 开始扫描网络
		scan();
		listResult = mWifiManager.getScanResults();
		return listResult;
	}

	/**
	 * 得到已知连接的列表
	 *
	 * @return list<ScanResult> : ScanResult列表
	 */
	public List<ScanResult> getKnowScanResult() {
		List<ScanResult> listScan = getScanResultForList();
		if (listScan == null)
			return null;

		List<ScanResult> list = new ArrayList<ScanResult>();
		for (int i = 0; i < listScan.size(); i++) {
			ScanResult mScanResult = listScan.get(i);
			if (mScanResult != null && mScanResult.SSID.equals("") == false
					&& mScanResult.SSID != null
					&& IsExsits(mScanResult.SSID) != null) {
				list.add(mScanResult);
			}
		}

		return list;
	}

	/**
	 * 得到未知连接的列表
	 *
	 * @return list<ScanResult> : ScanResult列表
	 */
	public List<ScanResult> getUnKnowScanResult() {
		List<ScanResult> listScan = getScanResultForList();
		if (listScan == null)
			return null;

		List<ScanResult> list = new ArrayList<ScanResult>();
		for (int i = 0; i < listScan.size(); i++) {
			ScanResult mScanResult = listScan.get(i);
			if (mScanResult.SSID != null
					&& mScanResult.SSID.equals("") == false
					&& IsExsits(mScanResult.SSID) == null) {
				list.add(mScanResult);
			}
		}

		return list;
	}

	/**
	 * 连接指定网络
	 */
	public void connect() {
		mWifiInfo = mWifiManager.getConnectionInfo();

	}

	/**
	 * 获取连接网络的信息
	 *
	 */
	public WifiInfo getConnectInfo() {
		mWifiInfo = mWifiManager.getConnectionInfo();
		return mWifiInfo;
	}

	/*
	 *
	 */
	public boolean isCurrentConnectWifi(String strSSID) {
		WifiInfo info = getConnectInfo();
		if ((info != null && info.getSSID() != null && info.getSSID().equals(
				strSSID))
				|| (info != null && info.getSSID() != null && info.getSSID()
						.equals("\"" + strSSID + "\""))) {
			return true;
		}

		return false;
	}

	/**
	 * 断开当前连接的网络
	 */
	public void disconnectWifi() {
		int netId = getNetworkId();
		mWifiManager.disableNetwork(netId);
		mWifiManager.disconnect();
		mWifiInfo = null;
	}

	/**
	 * 检查当前网络状态
	 *
	 * @return String
	 */
	public boolean checkNetWorkState() {
		getConnectInfo();
		if (mWifiInfo != null) {
			Log.i(TAG, "网络正常工作");
			return true;
		} else {
			Log.i(TAG, "网络已断开");
		}

		return false;
	}

	public boolean isWifiConnect() {
		ConnectivityManager connManager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = null;
		try {
			mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		} catch (Exception e) {
			return false;
		}

		return mWifi != null && mWifi.isConnected();

	}

	/**
	 * 得到连接的ID
	 */
	public int getNetworkId() {
		return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
	}

	/**
	 * 得到IP地址
	 */
	public int getIPAddress() {
		return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
	}

	// 锁定WifiLock
	public void acquireWifiLock() {
		mWifiLock.acquire();
	}

	// 解锁WifiLock
	public void releaseWifiLock() {
		// 判断时候锁定
		if (mWifiLock.isHeld()) {
			mWifiLock.acquire();
		}
	}

	// 创建一个WifiLock
	public void creatWifiLock() {
		mWifiLock = mWifiManager.createWifiLock("Test");
	}

	// 得到配置好的网络
	public List<WifiConfiguration> getConfiguration() {
		return mWifiConfiguration = mWifiManager.getConfiguredNetworks();
	}

	// 指定配置好的网络进行连接
	public void connectConfiguration(int index) {
		// 索引大于配置好的网络索引返回
		if (index >= mWifiConfiguration.size()) {
			return;
		}
		// 连接配置好的指定ID的网络
		mWifiManager.enableNetwork(mWifiConfiguration.get(index).networkId,
				true);
		//将配置信息保存到wpa_xxxx.conf文件中
		mWifiManager.saveConfiguration();
	}

	// 得到MAC地址
	public String getMacAddress() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
	}

	// 得到接入点的BSSID
	public String getSSID() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.getSSID();
	}

	// 得到接入点的BSSID
	public String getBSSID() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
	}

	// 得到WifiInfo的所有信息包
	public String getWifiInfo() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.toString();
	}

	/*
	 *
	 */
	public int getWifiSignalLevel(int rssi, int numLevels) {
		if (mWifiInfo != null) {
			return WifiManager.calculateSignalLevel(rssi, numLevels);
		}

		return 0;
	}

	// 添加一个网络并连接
	public int addNetwork(WifiConfiguration wcg) {
		int wcgID = mWifiManager.addNetwork(wcg);
		if (wcgID != -1) {
		List<WifiConfiguration> list =  mWifiManager.getConfiguredNetworks();

			for(int i=0;i<list.size();i++){
	//	for( WifiConfiguration i : list ) {
			if(list.get(i).SSID != null && list.get(i).SSID.equals(wcg.SSID)) {
				Log.d(TAG,"READ WIFI CONFIGURATION FROM CONF FILE WifiControl SSID equal "+list.get(i).SSID);
				mWifiManager.disconnect();
				boolean bRet =mWifiManager.enableNetwork(list.get(i).networkId, true);
				if (bRet == false) {
					Log.d(TAG,"WifiControl "+list.get(i).SSID);
					mWifiManager.removeNetwork(wcg.networkId);
					return -1;
				}
				mWifiManager.reconnect();
				break;
			}
				if(i==(list.size()-1)){
					Log.d(TAG,"NOT READ WIFI CONFIGURATION FROM CONF FILE "+wcg.SSID);
					boolean bRet =mWifiManager.enableNetwork(wcg.networkId, true);
					if (bRet == false) {
						Log.d(TAG,"NOT FROM CONFIG WifiControl "+wcg.SSID);
						mWifiManager.removeNetwork(wcg.networkId);
						return -1;
					}
					mWifiManager.reconnect();
				}
		}
			//将配置信息保存到wpa_xxxx.conf文件中
			mWifiManager.saveConfiguration();
			mLastConnectConfigure = wcg;

		}
		return wcgID;
	}

	public WifiConfiguration getLastConnectConfigure() {
		return mLastConnectConfigure;
	}

	/**
	 * 根据已知SSID连接已保存的网络
	 *
	 * @param strSSID
	 *            : AP的ssid名
	 */
	public void connectKnowAp(String strSSID) {
		getConfiguration();

		if (mWifiConfiguration == null)
			return;

		for (int i = 0; i < mWifiConfiguration.size(); i++) {
			String ssid = "\"" + strSSID + "\"";
			WifiConfiguration wifi = mWifiConfiguration.get(i);
			if (wifi.SSID.equals(ssid)) {
				connectConfiguration(i);
				mLastConnectConfigure = wifi;
				break;
			}
		}
	}

	/**
	 * 从已保存的列表里去除
	 *
	 * @param SSID
	 */
	public void forgetPassword(String SSID) {
		WifiConfiguration tempConfig = this.IsExsits(SSID);

		if (tempConfig != null) {

			mWifiManager.removeNetwork(tempConfig.networkId);

		}
	}

	public void removeNetwork(int netWorkId) {
		mWifiManager.removeNetwork(netWorkId);
	}

	public boolean isSecureWifi(String strSecure) {
		if (strSecure.contains("WEP") || strSecure.contains("WPA"))
			return true;

		return false;
	}

	private static boolean isHex(String key) {
		for (int i = key.length() - 1; i >= 0; i--) {
			final char c = key.charAt(i);
			if (!(c >= '0' && c <= '9' || c >= 'A' && c <= 'F' || c >= 'a'
					&& c <= 'f')) {
				return false;
			}
		}

		return true;
	}

	private boolean isHexWepKey(String wepKey) {
		final int len = wepKey.length();

		// WEP-40, WEP-104, and some vendors using 256-bit WEP (WEP-232?)
		return !(len != 10 && len != 26 && len != 58) && isHex(wepKey);

	}

	// 然后是一个实际应用方法，只验证过没有密码的情况：
	@SuppressLint("NewApi")
	public WifiConfiguration CreateWifiInfo(String SSID, String Password,
											String strSecure) {
		WifiConfiguration config = new WifiConfiguration();
		config.allowedAuthAlgorithms.clear();
		config.allowedGroupCiphers.clear();
		config.allowedKeyManagement.clear();
		config.allowedPairwiseCiphers.clear();
		config.allowedProtocols.clear();
		config.SSID = "\"" + SSID + "\"";

		int Type;

		if (strSecure.contains("WEP")) {
			Type = 2;
		} else if (strSecure.contains("WPA2")) {
			Type = 4;
		} else if (strSecure.contains("WPA")) {
			Type = 3;
		} else {
			Type = 1;
		}

		WifiConfiguration tempConfig = this.IsExsits(SSID);
		if (tempConfig != null) {
			mWifiManager.removeNetwork(tempConfig.networkId);
		}

		if (Type == 1) // WIFICIPHER_NOPASS
		{
			// config.wepKeys[0] = "";

			config.allowedKeyManagement.set(KeyMgmt.NONE);
			// config.wepTxKeyIndex = 0;
		}

		if (Type == 2) // WIFICIPHER_WEP
		{
			// config.preSharedKey = "";

			// config.hiddenSSID = true;
			// config.wepKeys[0] = "\"" + Password + "\"";
			// config.allowedAuthAlgorithms
			// .set(WifiConfiguration.AuthAlgorithm.SHARED);
			// config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			// config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			// config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
			// config.allowedGroupCiphers
			// .set(WifiConfiguration.GroupCipher.WEP104);
			// config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			// config.wepTxKeyIndex = 0;
			//

			if (!TextUtils.isEmpty(Password)) {
				if (isHexWepKey(Password)) {
					config.wepKeys[0] = Password;
				} else {
					config.wepKeys[0] = "\"" + Password + "\"";
				}
			}

			// config.wepKeys[0] = Password;
			config.allowedKeyManagement.set(KeyMgmt.NONE);
			config.allowedAuthAlgorithms.set(AuthAlgorithm.OPEN);
			config.allowedAuthAlgorithms.set(AuthAlgorithm.SHARED);
			config.wepTxKeyIndex = 0;
		}

		if (Type == 3) // WIFICIPHER_WPA
		{
			config.preSharedKey = "\"" + Password + "\"";
			config.hiddenSSID = true;
			config.allowedAuthAlgorithms
					.set(AuthAlgorithm.OPEN);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedKeyManagement.set(KeyMgmt.WPA_PSK);
			config.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.TKIP);
			config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.CCMP);
			config.status = WifiConfiguration.Status.ENABLED;
		}

		if (Type == 4) {
			// WPA2
			config.preSharedKey = "\"" + Password + "\"";
			config.allowedAuthAlgorithms
					.set(AuthAlgorithm.OPEN);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedKeyManagement.set(KeyMgmt.WPA_PSK);
			config.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.TKIP);
			config.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.CCMP);
			config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
			config.status = WifiConfiguration.Status.ENABLED;
		}

		return config;
	}

	public WifiConfiguration IsExsits(String SSID) {
		List<WifiConfiguration> existingConfigs = mWifiManager
				.getConfiguredNetworks();

		if (existingConfigs == null)
			return null;

		for (WifiConfiguration existingConfig : existingConfigs) {
			if (existingConfig.SSID != null
					&& existingConfig.SSID.equals("\"" + SSID + "\"")) {
				return existingConfig;
			}
		}

		return null;
	}

	// add by zdy begin
	/**
	 * 扫描wifi
	 */
	public void wifiStartScan() {
		mWifiManager.startScan();
	}

	/**
	 * 检查WIFI状态
	 * 
	 * @return
	 */
	public int wifiCheckState() {
		return mWifiManager.getWifiState();
	}

	/**
	 * 得到扫描结果
	 */
	public List<ScanResult> getScanResults() {
		listResult = mWifiManager.getScanResults();
		return listResult;
	}
	// add by zdy end

}
