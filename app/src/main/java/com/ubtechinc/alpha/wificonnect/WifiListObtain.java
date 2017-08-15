package com.ubtechinc.alpha.wificonnect;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.ubtech.utilcode.utils.WifiControl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @date 2017/6/13
 * @author wzt
 * @Description 获取wifi信号列表
 * @modifier
 *
 * @modify_time
 */

public class WifiListObtain {
    private static final String TAG = "WifiListObtain";

    public static final String LEVEL = "Level";
    public static final String SSID = "SSID";
    public static final String CAPABILITIES = "Capabilities";

    private static WifiListObtain sWifiListObtain;

    private Context mContext;

    private WifiListObtain(Context context) {
        mContext = context.getApplicationContext();
    }

    public static WifiListObtain get(Context context) {
        if(sWifiListObtain == null) {
            synchronized (WifiListObtain.class) {
                if(sWifiListObtain == null)
                    sWifiListObtain = new WifiListObtain(context);
            }
        }

        return sWifiListObtain;
    }

    public String getWifiList() {
        WifiControl wifiControl = WifiControl.get(mContext);
        Log.i(TAG, "Wifi enable(TRUE OR FALSE):  " + wifiControl.isEnable());
        List<ScanResult> wifiResultList;
        if (!wifiControl.isEnable()) {
            Log.i(TAG, "autoConnectWifi modify name by brian.li");
            List<ScanResult> confingList = new ArrayList<ScanResult>();
            wifiControl.openNetCard();
            wifiControl.wifiStartScan();
            // add by zdy 循环查找wifi状态
            // 0正在关闭,1WIFi不可用,2正在打开,3可用,4状态不可zhi
            while (wifiControl.wifiCheckState() != WifiManager.WIFI_STATE_ENABLED) {
                Log.i(TAG, "wifiControl");
                for (int i = 0; i < 15; i++) {//
                    try {
                        Thread.sleep(200);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            confingList.clear();
        }
        wifiResultList = wifiControl.getScanResultForList();

        List<ScanResult> results = scanWifi(wifiResultList);


        String wifiString = getWifiJsonArray(results);

        return wifiString;
    }

    /**
     * 获取信号最强的
     *
     * @param list
     * @return
     */
    public String getConfigListMaxLevel(List<ScanResult> list) {
        List<ScanResult> confingList = new ArrayList<ScanResult>();// 防止传进来的数据被清空，则将数据先拷贝
        confingList.addAll(list);
        int size = confingList.size();

        if (size == 0) {
            return null;
        } else if (size == 1) {
            return confingList.get(0).SSID;
        } else {// 取信号最强的ssid
            int index = 0;
            int max = WifiManager.calculateSignalLevel(
                    confingList.get(0).level, 100);
            for (int i = 1; i < size; i++) {
                int temp = WifiManager.calculateSignalLevel(
                        confingList.get(i).level, 100);
                if (temp > max) {
                    index = i;
                    max = temp;
                }
            }
            return confingList.get(index).SSID;
        }
    }

    public List<ScanResult> scanWifi(List<ScanResult> scanResults) {
        List<ScanResult> requridList = new ArrayList<>();
        boolean isContains;

        for (int i = 0; i < scanResults.size(); i++) {
            String ssid = scanResults.get(i).SSID;
            isContains = false;
            for (int j = 0; j < requridList.size(); j++) {
                if (ssid.equalsIgnoreCase(requridList.get(j).SSID)) {
                    isContains = true;
                    break;
                }
            }
            if (!isContains) {
                requridList.add(scanResults.get(i));
            }
        }
        return requridList;
    }

    private String getWifiJsonArray(List<ScanResult> mScanResultList) {
        JSONArray jsonArray = new JSONArray();
        try {
            for (int i = 0; i < mScanResultList.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                String ssid = mScanResultList.get(i).SSID;
                jsonObject.put(LEVEL, mScanResultList.get(i).level);
                jsonObject.put(SSID, mScanResultList.get(i).SSID);
                jsonObject.put(CAPABILITIES, mScanResultList.get(i).capabilities);
                jsonArray.put(i, jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray.toString();
    }
}
