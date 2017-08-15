package com.ubtechinc.alpha2ctrlapp.util.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;

import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by nixiaoyan on 2016/8/1.
 */
public class BluetoothManager {
    public static BluetoothManager bluetoothManager;
    private BluetoothSocket  socket;
    private Context context;
    private BluetoothDevice device;
    //该UUID表示串口服务
    public static final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
    public static final String TAG="BlueToothManager";


    public BluetoothManager (Context context,BluetoothDevice device){
        this.context = context;
        this.device = device;

    }
    public   static BluetoothManager getInstance(Context context,BluetoothDevice  device){
        if(bluetoothManager == null){
            bluetoothManager  = new BluetoothManager(context,device);
        }
         return bluetoothManager;

    }

    public void connect(BluetoothDevice device) {
       this.device = device;
        if(device!=null){
            new ConnectThread().start();
        }
    }

    private class ConnectThread extends Thread {
        boolean connecting ;
        boolean connected;
        int connetTime ;
        public void run() {
            connecting = true;
            connected = false;
            initSocket();
            while (!connected && connetTime <= 10) {
                try {
                    socket.connect();
                    connected = true;
                } catch (IOException e1) {
                    connetTime++;
                    connected = false;
                    // 关闭 socket
                    try {
                        socket.close();
                        socket = null;
                    } catch (IOException e2) {
                        //TODO: handle exception
                        Logger.d(TAG, "Socket", e2);
                    }
                } finally {
                    connecting = false;
                }
                //connectDevice();
            }
            // 重置ConnectThread
            //synchronized (BluetoothService.this) {
            //ConnectThread = null;
            //}
        }

        public void cancel() {
            try {
                socket.close();
                socket = null;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                connecting = false;
            }
        }
    }
    /**
     * 取得BluetoothSocket
     */
    private void initSocket() {
        BluetoothSocket temp = null;
        try {
            Method m = device.getClass().getMethod(
                    "createRfcommSocket", new Class[] { int.class });
            temp = (BluetoothSocket) m.invoke(device, 1);//这里端口为1
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        socket = temp;
    }

}
