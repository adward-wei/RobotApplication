package com.ubtech.utilcode.utils.bt;

import java.util.ArrayList;
import java.util.List;

import android.os.SystemClock;

class BUFFER_DATA {
    public byte[] datas;
    public int nLen;
    public long lastSendTime;
    public int resenTime;
}

public class DeviceProcessThread extends Thread {

    public interface DeviceProcessThreadCallBack {
        void onSendData(byte[] data, int len);
    }

    private ProtocolPacket mPacketRead;
    private DeviceProcessThreadCallBack mCallBack;

    private List<BUFFER_DATA> mListReSend;
    private boolean mRun = true;

    public DeviceProcessThread(DeviceProcessThreadCallBack callback) {
        mCallBack = callback;
        mPacketRead = new ProtocolPacket();
        mListReSend = new ArrayList<BUFFER_DATA>();
    }

    public void sendData(byte[] datas, int nLen) {
        synchronized (this) {
            BUFFER_DATA data = new BUFFER_DATA();

            data.datas = datas;
            data.nLen = nLen;
            data.lastSendTime = SystemClock.uptimeMillis();
            data.resenTime = 0;

            mListReSend.add(data);

            if (mCallBack != null) {
                mCallBack.onSendData(datas, nLen);
            }
        }
    }

    public void sendDataNotPutToList(byte[] datas, int nLen) {
        synchronized (this) {
            if (mCallBack != null) {
                mCallBack.onSendData(datas, nLen);
            }
        }
    }

    public void clearDataBuffer() {
        synchronized (this) {
            mListReSend.clear();
            mPacketRead = null;
            mPacketRead = new ProtocolPacket();
        }
    }

    public void releaseConnection() {
        synchronized (this) {
            mRun = false;

            mListReSend.clear();
        }
    }

    public void removeFromListByCmdID(byte cmdID) {
        synchronized (this) {
            ProtocolPacket packet = new ProtocolPacket();
            BUFFER_DATA remove = null;
            for (BUFFER_DATA buffer : mListReSend) {
                packet.setRawData(buffer.datas);

                if (packet.getmCmd() == cmdID) {
                    remove = buffer;
                    break;
                }
            }

            if (remove != null)
                mListReSend.remove(remove);
        }
    }

    @Override
    public void run() {
        while (mRun) {
            synchronized (this) {
                BUFFER_DATA removeData = null;
                for (BUFFER_DATA data : mListReSend) {
                    if (SystemClock.uptimeMillis() - data.lastSendTime >= 500) {

                        if (data.resenTime >= 2) {
                            removeData = data;
                            break;
                        } else {
                            sendDataNotPutToList(data.datas, data.nLen);
                            data.lastSendTime = SystemClock.uptimeMillis();
                            data.resenTime++;
                        }
                    }
                }

                if (removeData != null) {
                    mListReSend.remove(removeData);
                    removeData = null;
                    continue;
                }

            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                mRun = false;
                break;
            }

        }
    }

}
