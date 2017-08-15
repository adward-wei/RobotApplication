package com.ubtechinc.alphatranslation.util;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

/**
 * Socket 客户端程序
 * 
 * @author chenlin
 * 
 */
public class SocketClientHandler {
	private final static String TAG = "SocketClientHandler";
	// 客户端SOCKET
	private Socket client;
	// 调用者CONTEXT
	private Context mContext;
	private OutputStream mOutput;
	private DataInputStream mInputStream;
	/*
	 * Socket要发送的数据
	 */
	private List<BufferData> mListSend;

	private SocketClientRcvDataCallBack mRcvCallBack;
	/*
	 * Socket读线程
	 */
	private ReadThread mReadThread;
	/*
	 * Socket写线程
	 */
	private WriteThread mWriteThread;
	
	public void ReleaseConnection() {

		if (mReadThread != null) {
			mReadThread.ReleaseConntion();
		}

		if (mWriteThread != null) {
			mWriteThread.ReleaseConnection();
		}

		if (client != null) {
			try {
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 发送数据
	 * 
	 * @param bys
	 * @param nLen
	 */
	public void sendData(byte[] bys, int nLen) {
		synchronized (this) {
			BufferData buf = new BufferData();
			buf.bytes = bys;
			buf.nLen = nLen;

			mListSend.add(buf);
		}
	}

	/**
	 * 构造函数
	 * 
	 * @param context
	 * @param socket
	 * @param callbck
	 */
	public SocketClientHandler(Context context, Socket socket,
			SocketClientRcvDataCallBack callback) {
		mContext = context;
		client = socket;

		mRcvCallBack = callback;
		
		mListSend = new ArrayList<BufferData>();
		
		mReadThread = new ReadThread();
		mReadThread.start();
		mWriteThread = new WriteThread();
		mWriteThread.start();
	}

	/**
	 * 客户端接收到数据
	 * 
	 * @author chenlin
	 * 
	 */
	public interface SocketClientRcvDataCallBack {
		void socketOnRcvData(byte[] datas);
	}
	
	private class BufferData {
		byte[] bytes;
		int nLen;
	}
	
	private class WriteThread extends Thread {
		private boolean mExit;

		/**
		 * 释放资源
		 */
		public void ReleaseConnection() {
			this.mExit = true;
			this.interrupt();
			if (mOutput != null) {
				try {
					mOutput.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		public void sendMsgData(byte[] msgData) {
			// TODO Auto-generated method stub
			try {
				mOutput.write(msgData, 0, msgData.length);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		@Override
		public void run() {

			while (mExit == false) {
				try {
					synchronized (this) {
						BufferData toSend = null;
						for (BufferData data : mListSend) {
							toSend = data;
							break;
							// try {
							// //mOutput.write(data.bytes, 0, data.nLen);
							// Log.i("xt", "send data");
							// } catch (IOException e) {
							// // TODO Auto-generated catch block
							// break;
							// }
						}

						if (toSend != null) {
							sendMsgData(toSend.bytes);
							mListSend.remove(toSend);
							continue;
						}

						// mListSend.clear();
					}
				} catch (Exception e) {
					Log.v(TAG, e.toString());
					break;
				}

				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					break;
				}
			}
		}
	}

	private class ReadThread extends Thread {
		// InputStream mInput;
		// byte[] buffer = new byte[1024];

		private boolean mExit;

		/**
		 * 释放资源
		 */
		public void ReleaseConntion() {
			mExit = true;
			this.interrupt();

			if (mInputStream != null) {
				try {
					mInputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		public byte[] readData() throws IOException {
			// TODO Auto-generated method stub
			int len = mInputStream.available();// 如果不超过一定的长度就一次性读取
			if (len == 0)
				return null;
			Log.i(TAG,
					"beging time ="
							+ new SimpleDateFormat("HH:mm:ss")
									.format(new Date()));
			if (len > 1024 * 50) {// 太大的时候分批读取就行了
				len = 1024 * 50;
			}
			byte[] datas = new byte[len];
			mInputStream.read(datas);
			Log.i(TAG,
					"end time ="
							+ new SimpleDateFormat("HH:mm:ss")
									.format(new Date()));
			return datas;
		}

		@Override
		public void run() {

			while (mExit == false) {
				try {
					byte[] params = readData();

					if (params == null) {
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						continue;
					} else {
						if (mRcvCallBack != null) {
							mRcvCallBack.socketOnRcvData(params);
						}
					}

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();

					mExit = true;
					return;
				}
			}
		}
	}
}
