package com.ubtechinc.alpha.appmanager.install;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * [静默安装具体的实现]
 * 
 * @author zengdengyi
 * @version 1.0
 * @date 2014-11-11
 * 
 **/

public class InstallerUtils {

	public String install(String apkAbsolutePath ) {
		String[] args = { "pm", "install", "-r", apkAbsolutePath };
		String result = "";
		ProcessBuilder processBuilder = new ProcessBuilder(args);
		Process process = null;
		InputStream errIs = null;
		InputStream inIs = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int read = -1;
			process = processBuilder.start();
			errIs = process.getErrorStream();
			while ((read = errIs.read()) != -1) {
				baos.write(read);
			}
			baos.write('/' + 'n');
			inIs = process.getInputStream();
			while ((read = inIs.read()) != -1) {
				baos.write(read);
			}
			byte[] data = baos.toByteArray();
			result = new String(data);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (errIs != null) {
					errIs.close();
				}
				if (inIs != null) {
					inIs.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (process != null) {
				process.destroy();
			}
		}

		return result;
	}

	public String install(String apkAbsolutePath, boolean isBuiltIn) {
		ProcessBuilder processBuilder = null;
		if(isBuiltIn) {
			String[] args = {"pm", "install", "-r", "-f", apkAbsolutePath};
			processBuilder = new ProcessBuilder(args);
		} else {
			String[] args = {"pm", "install", "-r", "-s", apkAbsolutePath};
			processBuilder = new ProcessBuilder(args);
		}
		String result = "";
		Process process = null;
		InputStream errIs = null;
		InputStream inIs = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int read = -1;
			process = processBuilder.start();
			errIs = process.getErrorStream();
			while ((read = errIs.read()) != -1) {
				baos.write(read);
			}
			baos.write('/' + 'n');
			inIs = process.getInputStream();
			while ((read = inIs.read()) != -1) {
				baos.write(read);
			}
			byte[] data = baos.toByteArray();
			result = new String(data);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (errIs != null) {
					errIs.close();
				}
				if (inIs != null) {
					inIs.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (process != null) {
				process.destroy();
			}
		}

		return result;
	}

}
