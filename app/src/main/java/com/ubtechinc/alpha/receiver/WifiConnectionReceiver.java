package com.ubtechinc.alpha.receiver;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import com.ubtechinc.alpha.app.AlphaApplication;
import com.ubtechinc.alpha.robotinfo.RobotState;
import com.ubtechinc.alpha.wificonnect.Alpha2Connection;

/**
 * @date 2017/6/13
 * @author wzt
 * @Description 接收有关wifi连接的广播
 * @modifier
 * @modify_time
 */

public class WifiConnectionReceiver extends BroadcastReceiver {
    private static final String TAG = "WifiConnectionReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Alpha2Connection alpha2Connection = Alpha2Connection.getInstance(AlphaApplication.getContext());
        if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                    BluetoothAdapter.ERROR);
            switch (state) {
                case BluetoothAdapter.STATE_OFF:
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    break;
                case BluetoothAdapter.STATE_ON:
                    alpha2Connection.setBluetoothName(RobotState.get().getSid());
                    alpha2Connection.startBleService();
                    break;
                case BluetoothAdapter.STATE_TURNING_ON:
                    break;
                default:
                    break;
            }

        } else if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {//connected and disconnected
            alpha2Connection.wifiConnectState(intent);
        } else if (action.equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)) {
            alpha2Connection.networkStateChanged(WifiInfo.getDetailedStateOf((SupplicantState) intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE)));
            int errorCode = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, -1);
            String ssidName = intent.getStringExtra(WifiManager.EXTRA_BSSID);
            if (errorCode == WifiManager.ERROR_AUTHENTICATING) {
                if (ssidName != null) {
                    if (!ssidName.equals(alpha2Connection.getReadyConnectSsid())) {
                        return;
                    }
                    if (alpha2Connection.getWifiAccountFailer() != 0) {
                        alpha2Connection.setWifiAccountFailer(alpha2Connection.getWifiAccountFailer() - 1);
                        alpha2Connection.stopNetworkConnection(false);
                        if (alpha2Connection.isConnectingNow()) {
                            alpha2Connection.getmHandler().sendEmptyMessage(alpha2Connection.WIFI_INFORMATION_ERROR);
                        }
                    }
                }
            }

        }
    }
}
