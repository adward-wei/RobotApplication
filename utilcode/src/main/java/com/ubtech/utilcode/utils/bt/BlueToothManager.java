package com.ubtech.utilcode.utils.bt;

import java.util.ArrayList;
import java.util.List;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.SystemClock;


public class BlueToothManager extends Thread implements BlueToothClientHandler.ClentCallBack, BluetoothUtil.BluetoothUtilCallBack {

    private boolean mServices = false;
    private List<BlueToothClientHandler> mListClient;
    private boolean mRun = true;
    private PublicInterface.BlueToothInteracter mBlueToothInteraction;
    private BluetoothUtil mBluetoothUtil;

    public BluetoothUtil getmBluetoothUtil() {
        return mBluetoothUtil;
    }

    private long mLastSendTime;

    public BlueToothManager(Context context, PublicInterface.BlueToothInteracter s) {
        mBlueToothInteraction = s;
        mListClient = new ArrayList<BlueToothClientHandler>();
        mBluetoothUtil = new BluetoothUtil(context, this);

        mLastSendTime = SystemClock.uptimeMillis();
    }

    synchronized public void setBlueToothInteraction(PublicInterface.BlueToothInteracter s) {
        mBlueToothInteraction = s;
    }

    public void startProcess() {
        mRun = true;
        mBluetoothUtil.start(mServices);
        this.start();
    }

    public void connectDevice(BluetoothDevice device) {
        releaseAllConnected();

        mBluetoothUtil.connect(device);
    }

    synchronized public BlueToothClientHandler findClient(String mac) {
        BlueToothClientHandler find = null;
        for (BlueToothClientHandler s : mListClient) {
            if (s.getmMacAddress().equals(mac)) {
                find = s;
                break;
            }
        }

        return find;
    }

    synchronized public void addClientToList(String mac, BluetoothSocket socket) {
        if (findClient(mac) != null)
            return;

        BlueToothClientHandler client = new BlueToothClientHandler(mac, socket, this);
        mListClient.add(client);
        client.start();
    }

    synchronized public void releaseAllConnected() {
        if (null != mListClient) {
            for (BlueToothClientHandler s : mListClient) {
                s.releaseConnection();
            }

            mListClient.clear();
        }

    }

    synchronized public void releaseConnection() {
        mRun = false;
        for (BlueToothClientHandler s : mListClient) {
            s.releaseConnection();
        }

        mListClient.clear();
    }

    synchronized public void sendCommand(String mac, byte cmd, byte[] param, int len) {
        ProtocolPacket packet = new ProtocolPacket();

        packet.setmCmd(cmd);
        packet.setmParam(param);
        packet.setmParamLen(len);

        byte[] rawDatas = packet.packetData();

        BlueToothClientHandler client = findClient(mac);

        if (client != null) {
            client.sendData(rawDatas, rawDatas.length);
        }
    }

    public void SetSendXTState(String mac, boolean state) {
        BlueToothClientHandler client = findClient(mac);
        if (client != null) {
            client.SetSendXTState(state);
        }
    }

    @Override
    public void run() {
        while (mRun) {
            synchronized (this) {
                BlueToothClientHandler remove = null;
                for (BlueToothClientHandler s : mListClient) {
                    if (s.tryToReleaseConnection()) {
                        remove = s;
                        break;
                    } else {
                        ProtocolPacket packet = new ProtocolPacket();

                        packet.setmCmd(ConstValue.DETALDV_XT);
                        packet.setmParam(null);
                        packet.setmParamLen(0);

                        byte[] rawDatas = packet.packetData();
                        s.sendXT(rawDatas, rawDatas.length);
                    }
                }

                if (remove != null) {
                    String mac = remove.getmMacAddress();
                    remove.releaseConnection();
                    mListClient.remove(remove);

                    if (mBlueToothInteraction != null) {
                        mBlueToothInteraction.onDeviceDisConnected(mac);
                    }
                    continue;
                }
            }

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                mRun = false;
                break;
            }
        }
    }

    @Override
    synchronized public void onReceiveData(String mac, byte cmd, byte[] param, int len) {
        if (mBlueToothInteraction != null) {
            mBlueToothInteraction.onReceiveData(mac, cmd, param, len);
        }
    }

    @Override
    synchronized public void releaseConnection(String mac) {
        if (mBlueToothInteraction != null) {
            mBlueToothInteraction.onDeviceDisConnected(mac);
        }
    }

    @Override
    public void onConnectStateChange(String mac, int state) {

    }

    @Override
    synchronized public void onDeviceConnected(String mac, BluetoothSocket socket) {
        addClientToList(mac, socket);
        if (mBlueToothInteraction != null) {
            mBlueToothInteraction.onConnectState(true, mac);
        }
    }

    @Override
    synchronized public void onConnectFailed() {
        if (mBlueToothInteraction != null) {
            mBlueToothInteraction.onConnectState(false, null);
        }
    }
}
