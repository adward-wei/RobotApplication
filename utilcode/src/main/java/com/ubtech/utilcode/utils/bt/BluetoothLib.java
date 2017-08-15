package com.ubtech.utilcode.utils.bt;

import java.util.ArrayList;
import java.util.Set;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;


public class BluetoothLib implements PublicInterface.BlueToothInteracter {

    public static BluetoothLib instance = new BluetoothLib();
    public static BlueToothManager mBlueManager;
    private BluetoothAdapter mBluetoothAdapter;
    private Context context;
    private ArrayList<String> mDevicelist = new ArrayList<String>();
    private ArrayList<String> mPairedDevicelist = new ArrayList<String>();

    private DeviceListener mDeviceListener;

    public static BluetoothLib getInstance() {
        return instance;
    }

    public boolean init(Context context) {
        this.context = context;
        if (mBlueManager == null) {
            mBlueManager = new BlueToothManager(context, this);
        }
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            return false;
        }
        registerReceiver();
        return true;
    }

    public boolean init(Context context, DeviceListener mDeviceListener) {
        this.context = context;
        this.mDeviceListener = mDeviceListener;
        if (mBlueManager == null) {
            mBlueManager = new BlueToothManager(context, this);
        }
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            return false;
        }
        registerReceiver();
        return true;
    }

    public void setDeviceListener(DeviceListener listener) {
        this.mDeviceListener = listener;
    }

    public void startProcess() {
        if (mBlueManager != null) {
            mBlueManager.startProcess();
        }
    }

    public void open() {
        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
        }
    }

    public void close() {
        if (mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.disable();
        }
        this.unRegisterReceiver();
    }

    public boolean isOpen() {
        return mBluetoothAdapter.isEnabled();
    }

    public ArrayList<String> getBondedDevices() {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                mPairedDevicelist.add(device.getName() + "\n" + device.getAddress());
            }
        }
        return mPairedDevicelist;
    }

    public void startScan() {
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        mDevicelist.clear();
        mBluetoothAdapter.startDiscovery();
    }

    public void stopScan() {
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
    }

    BluetoothDevice tmpDevice;

    public void onConnect(String address) {
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        mBlueManager.releaseAllConnected();
        mBlueManager.connectDevice(device);
    }

    public void disConect() {
        mBlueManager.releaseAllConnected();
    }

    public void sendData(String mac, byte cmd, byte[] param, int len) {
        mBlueManager.sendCommand(mac, cmd, param, len);
    }

    public void SetSendXTState(String mac, boolean state) {
        mBlueManager.SetSendXTState(mac, state);
    }

    @Override
    public void onReceiveData(String mac, byte cmd, byte[] param, int len) {
        mDeviceListener.onReceiveData(mac, cmd, param, len);
    }

    @Override
    public void onSendData(String mac, byte[] datas, int nLen) {

    }

    @Override
    public void onConnectState(boolean bsucceed, String mac) {
        if (bsucceed == false) {
            mDeviceListener.onConnectState(bsucceed, mac);
        } else {
            mDeviceListener.onConnectState(bsucceed, mac);
        }
    }

    @Override
    public void onDeviceDisConnected(String mac) {
        mDeviceListener.onDisConnected(mac);
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        context.registerReceiver(mReceiver, filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        context.registerReceiver(mReceiver, filter);

    }

    private void unRegisterReceiver() {
        context.unregisterReceiver(mReceiver);
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed
                // already
                /* if (device.getBondState() != BluetoothDevice.BOND_BONDED) */{
                    // 信号强度。
                    short rssi = intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI);
                    String mac = device.getName() + "\n" + device.getAddress() + "\n" + rssi;

                    if (checkAddress(mac))
                        return;
                    mDevicelist.add(mac);
                    mDeviceListener.notifyCallBack(mac);
                }
                
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

            }
        }
    };

    private boolean checkAddress(String mac) {
        for (int i = 0; i < mDevicelist.size(); i++) {
            if (mDevicelist.get(i).equals(mac))
                return true;
        }
        return false;
    }

    public interface DeviceListener {
        public String notifyCallBack(String mDevice);

        public void onConnectState(boolean bsucceed, String mac);

        public void onReceiveData(String mac, byte cmd, byte[] param, int len);
        
        public void onDisConnected(String mac);
    }

}
