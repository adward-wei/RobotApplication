package com.ubtechinc.alpha.sockets;

import android.content.Context;

import com.ubtech.utilcode.utils.LogUtils;
import com.ubtech.utilcode.utils.thread.ThreadPool;
import com.ubtechinc.nets.im.business.ReceivePcMessageBussinesss;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 与PC通信的Socket实现类
 */
public class Alpha2SocketService {
    private ServerSocket mServerSocket;
    private final static String TAG = "Alpha2SocketServiceImpl";
    public final static int SERVER_PORT = 6100;
    private boolean isInterrupted;
    private SocketClient socketClient;

    private ReceivePcMessageBussinesss receivePcMessageBussinesss;
    private static Alpha2SocketService sInstance;


    /**
     * 构造器
     */
    public Alpha2SocketService() {
        receivePcMessageBussinesss = ReceivePcMessageBussinesss.getInstance();
        receivePcMessageBussinesss.init();

    }

    public static Alpha2SocketService getInstance(Context context) {
        if (sInstance == null) {
            synchronized (Alpha2SocketService.class) {
                sInstance = new Alpha2SocketService();
            }
        }
        return sInstance;
    }


    /**
     * 释放资源
     */
    public void ReleaseConnection() {
        synchronized (this) {
            isInterrupted = true;
            try {
                mServerSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void receivePcData() {
        ThreadPool.runOnNonUIThread(new Runnable() {
            @Override
            public void run() {
                try {
                    mServerSocket = new ServerSocket(SERVER_PORT);
                    while (!isInterrupted) {
                        Socket client = mServerSocket.accept();
                        socketClient = new SocketClient(client);
                        byte[] data = socketClient.readData();
                        receivePcMessageBussinesss.handleReceivedMessage(data);
                    }
                    mServerSocket.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    LogUtils.v(TAG, "listening socket error");
                    return;
                }
            }
        });
    }


    /**
     * 发送socket消息到PC端
     */
    public void socketSendData(final byte[] data) {
        ThreadPool.runOnNonUIThread(new Runnable() {
            @Override
            public void run() {
                try {
                    socketClient.sendMsgData(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
