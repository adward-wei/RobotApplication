package com.ubtechinc.alphatranslation.util;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import android.content.Context;
import android.util.Log;

import com.ubtechinc.alphatranslation.util.SocketClientHandler.SocketClientRcvDataCallBack;

/**
 * Socket服务类
 * 
 * @author chenlin
 * 
 */
public class SockServiceHandler extends Thread implements
		SocketClientRcvDataCallBack {
	private final String TAG = "SockServiceHandler";
	// context
	private Context mContext;
	// SOCKET服务接口
	private ServerSocket mServerSocket;
	// SOCKET端口
	private int mPort;
	// 停止标志
	private boolean mbStop;
	
	private SocketServiceCallBack mCallBack;

	private SocketClientHandler mSocketClientHandler;

	public interface SocketServiceCallBack {
		void onRcvData(byte[] datas);
	}

	/**
	 * 释放资源
	 */
	public void releaseConnection() {
		
		try {
			mbStop = true;
			mServerSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (mSocketClientHandler != null) {
			mSocketClientHandler.ReleaseConnection();
			mSocketClientHandler = null;
		}
	}

	/**
	 * 构造函数
	 * 
	 * @param context
	 * @param nPort
	 */
	public SockServiceHandler(Context context, int nPort, SocketServiceCallBack callback) {
		mContext = context;
		mPort = nPort;
		mCallBack = callback;

		mbStop = false;
		
		this.start();
	}

	@Override
	public void run() {
		try {
			mServerSocket = new ServerSocket(mPort);

			while (!mbStop) {
				Socket client = mServerSocket.accept();

				if (mSocketClientHandler != null) {
					mSocketClientHandler.ReleaseConnection();
				}

				mSocketClientHandler = new SocketClientHandler(mContext,
						client, this);
				Log.v(TAG, "client socket rcv");
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return;
		}
	}

	@Override
	public void socketOnRcvData(byte[] datas) {
		if (mCallBack != null) {
			mCallBack.onRcvData(datas);
		}
	}
}
