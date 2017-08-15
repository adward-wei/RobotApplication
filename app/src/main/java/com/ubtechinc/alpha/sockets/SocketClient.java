package com.ubtechinc.alpha.sockets;

import android.util.Log;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SocketClient implements IClient {
	/*
	 * 客户端SOCKET实例
	 */
	private Socket mClient;
	OutputStream mOutput;
	private DataInputStream mInputStream;

	public SocketClient(Socket client) {
		this.mClient = client;
		try {
			mOutput = mClient.getOutputStream();
			mInputStream = new DataInputStream(mClient.getInputStream());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getClientIp() {
		String ip = mClient.getInetAddress().getHostAddress();
		Log.v("chenlin", ip);

		return ip;
	}

	@Override
	public void close() {
		try {
			mClient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isClosed() {
		return mClient.isClosed();
	}

	@Override
	public boolean isConnected() {
		return mClient.isConnected();
	}

	@Override
	public void setKeepAlive(boolean isAlive) {
		try {
			mClient.setKeepAlive(isAlive);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void releaseWriteConnection() {
		if (mOutput != null) {
			try {
				mOutput.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void releaseReadConnection() {
		if (mInputStream != null) {
			try {
				mInputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void sendMsgData(byte[] msgData) throws IOException {
		mOutput.write(msgData, 0, msgData.length);

	}

	@Override
	public byte[] readData() throws IOException {
		int len = mInputStream.available();// 如果不超过一定的长度就一次性读取
		if (len == 0)
			return null;
		Log.i("readData",
				"beging time ="
						+ new SimpleDateFormat("HH:mm:ss").format(new Date()));
		if (len > 1024 * 50) {// 太大的时候分批读取就行了
			len = 1024 * 50;
		}
		byte[] datas = new byte[len];
		mInputStream.read(datas);
		Log.i("readData",
				"end time ="
						+ new SimpleDateFormat("HH:mm:ss").format(new Date()));
		return datas;
	}
}
