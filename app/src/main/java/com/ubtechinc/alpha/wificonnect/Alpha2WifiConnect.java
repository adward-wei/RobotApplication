package com.ubtechinc.alpha.wificonnect;

/**
 * Created by ubt on 2016/9/22.
 */

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

import com.ubtech.utilcode.utils.LogUtils;

import java.util.List;

public class Alpha2WifiConnect {

    private String TAG="Alpha2WifiConnect";

    WifiManager wifiManager;

    //定义几种加密方式，一种是WEP，一种是WPA，还有没有密码的情况
    public enum WifiCipherType
    {
        WIFICIPHER_WEP,WIFICIPHER_WPA, WIFICIPHER_NOPASS, WIFICIPHER_INVALID
    }

    //构造函数
    public Alpha2WifiConnect(WifiManager wifiManager)
    {
        this.wifiManager = wifiManager;
    }

    //打开wifi功能
    private boolean OpenWifi()
    {
        boolean bRet = true;
        if (!wifiManager.isWifiEnabled())
        {
            bRet = wifiManager.setWifiEnabled(true);
        }
        return bRet;
    }

    //提供一个外部接口，传入要连接的无线网
    public boolean Connect(String SSID, String Password, String Type)
    {
        int networkId;
        if(!this.OpenWifi())
        {
            LogUtils.i("fail to open wifi");
            return false;
        }

        //开启wifi功能需要一段时间(我在手机上测试一般需要1-3秒左右)，所以要等到wifi
        //状态变成WIFI_STATE_ENABLED的时候才能执行下面的语句
        while(wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING )
        {
            try{
                //为了避免程序一直while循环，让它睡个100毫秒在检测……
                Thread.currentThread();
                Thread.sleep(100);
            }
            catch(InterruptedException ie){
                ie.printStackTrace();
            }
        }
        LogUtils.i("begin to connect");
        WifiConfiguration wifiConfig = this.CreateWifiInfo(SSID, Password, Type);
        disableNetworkConfig();

        if ((networkId =  wifiManager.addNetwork(wifiConfig)) == -1) {
            Log.d("TAG", "Failed to add network configuration!");
            return false;
        }

        // Disconnect from current WiFi connection
        if (!disconnectFromWifi()) {
            Log.d("TAG", "Failed to disconnect from network!");
            return false;
        }

        // Enable network to be connected
        if (! wifiManager.enableNetwork(networkId, true)) {
            Log.d("TAG", "Failed to enable network!");
            return false;
        }

        // Connect to network
        if (! wifiManager.reconnect()) {
            Log.d("TAG", "Failed to connect!");
            return false;
        }
        //BRIAN ADD THE FOLLOWING SENTENCE TO SOLVE THE WIFI CONFIGURATION CAN'T SAVE BEGINNING
         wifiManager.saveConfiguration();
        //BRIAN ADD THE FOLLOWING SENTENCE TO SOLVE THE WIFI CONFIGURATION CAN'T SAVE ENDING
        return true;
}

    /**
     * Function to disconnect from the currently connected WiFi AP.
     * @return true  if disconnection succeeded
     * 				 false if disconnection failed
     */
    public boolean disconnectFromWifi() {
        return (wifiManager.disconnect());
    }

    //查看以前是否也配置过这个网络
    private WifiConfiguration IsExsits(String SSID)
    {
        List<WifiConfiguration> existingConfigs = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration existingConfig : existingConfigs)
        {
            if (existingConfig.SSID.equals("\""+SSID+"\""))
            {
                return existingConfig;
            }
        }
        return null;
    }

    public void disableNetworkConfig(){
        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for (int i = 0; i < list.size(); i++) {
            wifiManager.disableNetwork(list.get(i).networkId);
        }
    }
    public void enableNetworkConfig(){
        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for (int i = 0; i < list.size(); i++) {
            wifiManager.enableNetwork(list.get(i).networkId,true);
        }
    }

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
        LogUtils.i("createWifiInfo ==>secure type "+Type);


        if (Type == 1) // WIFICIPHER_NOPASS
        {
            // config.wepKeys[0] = "";

            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            // config.wepTxKeyIndex = 0;
        }


        if (Type == 2) // WIFICIPHER_WEP
        {

            if (!TextUtils.isEmpty(Password)) {
                if (isHexWepKey(Password)) {
                    config.wepKeys[0] = Password;
                } else {
                    config.wepKeys[0] = "\"" + Password + "\"";
                }
            }

            // config.wepKeys[0] = Password;
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.wepTxKeyIndex = 0;
        }


        if (Type == 3) // WIFICIPHER_WPA
        {
            config.preSharedKey = "\"" + Password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
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
                    .set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.CCMP);
            config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            config.status = WifiConfiguration.Status.ENABLED;
        }

        return config;
    }

    private boolean isHexWepKey(String wepKey) {
        final int len = wepKey.length();

        // WEP-40, WEP-104, and some vendors using 256-bit WEP (WEP-232?)
        return !(len != 10 && len != 26 && len != 58) &&  isHex(wepKey);

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
}