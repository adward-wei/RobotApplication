package com.ubtech.utilcode.utils.bt;

public class PublicInterface {

    public interface BlueToothInteracter {

        abstract void onReceiveData(String mac, byte cmd, byte[] param, int len);

        abstract void onSendData(String mac, byte[] datas, int nLen);

        abstract void onConnectState(boolean bsucceed, String mac);

        abstract void onDeviceDisConnected(String mac);
    }
}
