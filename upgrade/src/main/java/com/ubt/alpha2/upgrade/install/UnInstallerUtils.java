package com.ubt.alpha2.upgrade.install;

import java.io.DataOutputStream;

/**
 * [静默卸载具体实现]
 * 
 * @author zengdengyi
 * @version 1.0
 * @date 2014-11-11
 * 
 **/

public class UnInstallerUtils {

	public void chmodApk(String busybox, String chmod) {
		try {
			Process process = null;
			DataOutputStream os = null;
			process = Runtime.getRuntime().exec/* ("su") */("sh");
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(busybox);
			os.flush();
			os.writeBytes(chmod);
			os.flush();
			os.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public boolean uninstallApk(String uninstallapk) {
		boolean ret = false;
		try {
			Process process = null;
			DataOutputStream os = null;
			process = Runtime.getRuntime().exec/* ("su") */("sh");
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(uninstallapk);
			os.flush();
			os.close();
			int value = process.waitFor();
			if (value == 0) {
				ret = true;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			ret = false;
		}
		return ret;
	}

}
