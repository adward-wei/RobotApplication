package com.ubtechinc.alpha.wificonnect;

import android.content.Context;
import android.os.Build;

import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha.wificonnect.bleperipheral.UBTBluetoothService;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @date 2017/6/15
 * @author wzt
 * @Description 联网过程中跟蓝牙的通信
 * @modifier
 * @modify_time
 */

public class BleCommunicate implements IBluetoothDataInteract {
    private static final String TAG = "BleCommunicate";

    // 与蓝牙服务通信的命令号
    private static int COMMAND_NETWORK_CONFIGURATION = 1;
    private static int COMMAND_WIFILIST_INFORMATION = 2;
    private static int COMMAND_EXIT_CONNECTION_MODE = 5;


    // 与蓝牙服务通信有关的关键字
    private static String JSON_COMMAND = "command";
    private static String JSON_SSID = "ssid";
    private static String JSON_PWD = "passwd";
    private static String JSON_SECURE = "cap";  //WEP,WPA,WPA2
    private static final String WIFILIST = "APList";
    private static final String RESPONSECODE = "responseCode";
    private static final String CLIENTTAG = "client";
    private static final String VERSIONNAME = "versionName";
    private static final String VERSIONCODE = "versionCode";

    private static BleCommunicate sBleCommunicate;

    // 给蓝牙响应的信息
    private String sendBackString;

    // 蓝牙服务
    UBTBluetoothService ubtBluetoothService;

    private BleCommunicate() {}

    public static BleCommunicate get() {

        if(sBleCommunicate == null) {
            synchronized (BleCommunicate.class) {
                if(sBleCommunicate == null)
                    sBleCommunicate = new BleCommunicate();
            }
        }

        return sBleCommunicate;
    }

    // 启动蓝牙服务
    public void startBleService(Context context) {
        ubtBluetoothService = UBTBluetoothService.getBluetoothIntance(context);
        ubtBluetoothService.setBluetoothInteract(BleCommunicate.this);
        ubtBluetoothService.enableRobotBluetooth();
        ubtBluetoothService.beginBoradCastReciver();
    }

    // 停止蓝牙服务
    public void stopBleService() {
        if (ubtBluetoothService != null)
            ubtBluetoothService.disableRobotBluetooth();
    }

    @Override
    public String responseData() {
        LogUtils.d(TAG, "responseData  :" + sendBackString);
        return sendBackString;
    }

    @Override
    public void receverData(String result) {
        sendBackString = "";
        bleMessageParse(result);
    }

    // 解析来自蓝牙的数据包
    private void bleMessageParse(String s) {
        if (s == null) {
            return;
        }
        LogUtils.d(TAG, "before command string: " + s);

        try {
            JSONObject infoContent = new JSONObject(s);
            int commandType = infoContent.getInt(JSON_COMMAND);
            LogUtils.d(TAG, "after command string: " + s);

            if (commandType == COMMAND_NETWORK_CONFIGURATION) {
                Alpha2Connection.getInstance(null).setType_connection(Alpha2Connection.BLUETOOTH_TYPE);
                ConnectionTimer.get().stopConnectionHintTimer();
                String ssid = infoContent.getString(JSON_SSID);
                String pwd = infoContent.getString(JSON_PWD);
                String secure = infoContent.getString(JSON_SECURE);
                LogUtils.d(TAG, "ssid: " + ssid + "  pwd :" + pwd + "   cap" + secure);
                Alpha2Connection.getInstance(null).connectedBegin(ssid, pwd, secure, Alpha2Connection.BLUETOOTH_TYPE);
            } else if (commandType == COMMAND_WIFILIST_INFORMATION) {
                getRobotWifiList();
            } else if (commandType == COMMAND_EXIT_CONNECTION_MODE) {
                exitConnectionMode();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getRobotWifiList() throws JSONException {
        JSONObject reply = new JSONObject();
        reply.put(JSON_COMMAND, COMMAND_WIFILIST_INFORMATION + "");
        reply.put(WIFILIST, Alpha2Connection.getInstance(null).getWifiString());

        LogUtils.d(TAG, "reply  " + reply.toString() + " wifiString :" + Alpha2Connection.getInstance(null).getWifiString());
        try {
            Thread.sleep(600);
        } catch (Exception e) {
            e.printStackTrace();
        }

        sendBackString = reply.toString();
    }

    // 通过蓝牙，告知客户端当前网络的连接状态
    public void sendRobotConnectionStatus() {
        try {
            JSONObject reply = new JSONObject();
            reply.put(JSON_COMMAND, COMMAND_NETWORK_CONFIGURATION + "");
            reply.put(RESPONSECODE, COMMAND_NETWORK_CONFIGURATION + "");
            reply.put(CLIENTTAG, 4);
            reply.put(VERSIONNAME, "1");
            reply.put(VERSIONCODE, "1");
            LogUtils.d(TAG, "reply  " + reply.toString() + "connection result:" + Alpha2Connection.getInstance(null).getWifiString());
            try {
                Thread.sleep(600);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (ubtBluetoothService != null) {
                ubtBluetoothService.sendBleData(reply.toString(), false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 通过蓝牙，告知客户端退出联网操作
    public void exitConnectionMode() throws JSONException {
        JSONObject reply = new JSONObject();
        reply.put(JSON_COMMAND, COMMAND_EXIT_CONNECTION_MODE + "");
        LogUtils.d(TAG, "reply  " + reply.toString());
        if (ubtBluetoothService != null) {
            ubtBluetoothService.sendBleData(reply.toString(), true);
        }
        Alpha2Connection.getInstance(null).stopNetworkConnection(false);
    }

}
