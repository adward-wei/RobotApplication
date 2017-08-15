package com.ubtechinc.zh_chat.ui.camera;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;

public class PictureSendThread extends Thread {
	private String ip;
	private int port;
	private Socket client;
	private String filePath;
	private boolean isExit;

	public PictureSendThread(String ip, int port, String filePath) {
		this.ip = ip;
		this.port = port;
		this.filePath = filePath;
	}

	public void startProcess() {
		try {
			client = new Socket(ip, port);
			DataOutputStream mOutput = new DataOutputStream(
					client.getOutputStream());
			File fi = new File(filePath);
			mOutput.writeUTF(fi.getName());
			mOutput.flush();
			mOutput.writeLong(fi.length());
			mOutput.flush();

			FileInputStream fin = new FileInputStream(filePath);
			int bufferSize = 8192;
			byte[] buf = new byte[bufferSize];
			int read;
			while (!isExit && (read = fin.read(buf)) != -1) {
				mOutput.write(buf, 0, read);
			}
			mOutput.flush();
			fin.close();
			mOutput.close();
			client.close();
		}  catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		startProcess();
	}

}
