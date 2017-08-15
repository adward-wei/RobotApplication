package com.ubtechinc.alpha2ctrlapp.util;

import android.os.Handler;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class PictureAcceptThread extends Thread {

	private int port = 8086;
	private String path;
	boolean isExit;
	private Handler mHandler;
	private ServerSocket ss;

	public PictureAcceptThread(String path, Handler mHandler) {
		this.path = path;
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		this.mHandler = mHandler;
	}

	public void startProcess() {
		Socket s = null;
		try {
			ss = new ServerSocket(port);
			// 选择进行传输的文件
			while (!isInterrupted()) {
				s = ss.accept();
				int bufferSize = 8192;
				byte[] buf = new byte[bufferSize];
				int passedlen = 0;
				long len = 0;
				DataInputStream dis = new DataInputStream(s.getInputStream());
				String name = dis.readUTF();
				String filepath = path + name;
				len = dis.readLong();

				FileOutputStream os = new FileOutputStream(filepath);
				int read = 0;
				while (!isExit && (read = dis.read(buf)) != -1) {
					os.write(buf, 0, read);
				}
				mHandler.obtainMessage(0, name).sendToTarget();
				os.close();
				dis.close();
				s.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stopProcess() {
		try {
			if(ss!=null)
			ss.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.interrupt();
	}

	public void run() {
		startProcess();
	}

}
