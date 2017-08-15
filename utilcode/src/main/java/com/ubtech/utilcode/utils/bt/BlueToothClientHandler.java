package com.ubtech.utilcode.utils.bt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothSocket;
import android.os.SystemClock;
import android.util.Log;


public class BlueToothClientHandler extends Thread implements DeviceProcessThread.DeviceProcessThreadCallBack {
    static private final String TAG = "BlueToothClientHandler";

    private final String mMacAddress;
    private final BluetoothSocket mBlueToothSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private final ClentCallBack mCallBack;

    private boolean mRun = true;
    private ProtocolPacket mPacketRead;

    private long mLastRcvTime;
    private long mLastSendTime;

    private boolean mSendXTFlag = true;

    private DeviceProcessThread mProcessThread;

    public interface ClentCallBack {
        /**
         * 
         * @param mac
         * @param data
         * @param len
         */
        void onReceiveData(String mac, byte cmd, byte[] param, int len);

        void releaseConnection(String mac);
    }

    public String getmMacAddress() {
        return mMacAddress;
    }

    public void sendData(byte[] datas, int nLen) {
        mProcessThread.sendData(datas, nLen);
    }

    public void SetSendXTState(boolean state) {
        mSendXTFlag = state;
    }

    public BlueToothClientHandler(String mac, BluetoothSocket socket, ClentCallBack callback) {
        mMacAddress = mac;
        mBlueToothSocket = socket;
        mCallBack = callback;

        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        mLastRcvTime = SystemClock.uptimeMillis();
        mLastSendTime = mLastRcvTime;

        // mSendMsgList = new ArrayList<SendMsgData>();

        // Get the BluetoothSocket input and output streams
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            Log.e(TAG, "temp sockets not created", e);
        }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;

        mPacketRead = new ProtocolPacket();
        mProcessThread = new DeviceProcessThread(this);
        mProcessThread.start();
    }

    public void sendXT(byte[] data, int nlen) {
        if (SystemClock.uptimeMillis() - mLastSendTime < 3000) {
            return;
        }
        if (!mSendXTFlag) {
            mLastRcvTime = SystemClock.uptimeMillis();
            return;
        }
        onSendData(data, nlen);
    }

    /**
     * 
     * @return
     */
    public boolean tryToReleaseConnection() {
        boolean bTimeOut = SystemClock.uptimeMillis() - mLastRcvTime > 6000;
        if (mRun == false || this.isAlive() == false || (bTimeOut && mSendXTFlag))
            return true;

        return false;
    }

    /**
	 */
    public void releaseConnection() {
        synchronized (this) {
            try {
                mRun = false;
                if (mProcessThread != null)
                    mProcessThread.releaseConnection();

                if (mmInStream != null)
                    mmInStream.close();

                if (mmOutStream != null)
                    mmOutStream.close();

                if (mBlueToothSocket != null)
                    mBlueToothSocket.close();

                if (mCallBack != null) {
                    mCallBack.releaseConnection(mMacAddress);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        byte[] buffer = new byte[1024];
        int bytes;

        while (mRun) {
            try {
                bytes = mmInStream.read(buffer);
                if (bytes > 0) {
                    byte[] pTemp = new byte[bytes];
                    System.arraycopy(buffer, 0, pTemp, 0, bytes);
                    Log.e("DV_READSTATUS", "Receive: param=" + ByteHexHelper.bytesToHexString(pTemp));
                    for (int i = 0; i < bytes; i++) {
                        if (mPacketRead.setData_(buffer[i])) {
                            if (mCallBack != null) {
                                mPacketRead.setmParamLen(mPacketRead.getmParam().length);
                                mCallBack.onReceiveData(mMacAddress, mPacketRead.getmCmd(), mPacketRead.getmParam(), mPacketRead.getmParamLen());
                            }

                            mLastRcvTime = SystemClock.uptimeMillis();

                            mProcessThread.removeFromListByCmdID(mPacketRead.getmCmd());
                        }
                    }

                }

            } catch (IOException e) {
                Log.e(TAG, "disconnected", e);
                // connectionLost();
                // mRun = false;
                break;
            } catch (Exception e) {
                mRun = false;
                break;
            }
        }
    }

    @Override
    synchronized public void onSendData(byte[] data, int len) {
        try {
            Log.e("DV_READSTATUS", "SendData=" + ByteHexHelper.bytesToHexString(data));
            mmOutStream.write(data, 0, len);
            mLastSendTime = SystemClock.uptimeMillis();
        } catch (IOException e) {
            releaseConnection();
        } catch (Exception e) {
            releaseConnection();
        }
    }

}
