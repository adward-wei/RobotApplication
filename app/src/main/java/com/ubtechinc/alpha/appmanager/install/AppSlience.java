package com.ubtechinc.alpha.appmanager.install;

import android.util.Log;


import com.ubtech.utilcode.utils.thread.ThreadPool;

import java.io.File;
import java.util.List;

/**
 * [静默安装]
 * 
 * @author zengdengyi
 * @version 1.0
 * @date 2016年5月23日 下午2:17:14
 * 
 **/

public class AppSlience {
	public void onSlience(final String path, final SlienceListener listener) {
		ThreadPool.runOnNonUIThread(new Runnable() {
			@Override
			public void run() {
				InstallerUtils installerUtils = new InstallerUtils();
				String ret = installerUtils.install(path);
				Log.i("AppSlience", "update result=" + ret);
				if (ret.toLowerCase().contains("success")) {
					listener.onSlienceSuccess();
				} else {
					if(ret.toLowerCase().contains("insufficient_storage")) {
						listener.onSlienceFail(2, ret);
					} else {
						listener.onSlienceFail(0, ret);
					}

				}
				deleteFile(path);
			}
		});

	}

	public void onSlience(final String path, final SlienceListener listener, final boolean isBuiltIn,
						  final boolean is2Mic) {

		ThreadPool.runOnNonUIThread(new Runnable() {
			@Override
			public void run() {
				InstallerUtils installerUtils = new InstallerUtils();
				String ret = null;
				if(!is2Mic) {
					ret = installerUtils.install(path, isBuiltIn);
				} else {
					/** 2麦的机器只有内部存储 **/
					ret = installerUtils.install(path);
				}
				Log.i("AppSlience", "update result=" + ret);
				if (ret.toLowerCase().contains("success")) {
					listener.onSlienceSuccess();
				} else {
					if(ret.toLowerCase().contains("insufficient_storage")) {
						listener.onSlienceFail(2, ret);
					} else {
						listener.onSlienceFail(0, ret);
					}

				}
				deleteFile(path);
			}
		});

	}

	/**
	 * 删除单个文件
	 * 
	 * @param sPath
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public void deleteFile(String sPath) {
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
		}
	}

	public void onUnInitall(final String app, final SlienceListener listener) {

		ThreadPool.runOnNonUIThread(new Runnable() {
			@Override
			public void run() {
				String busybox = "mount -o remount rw /data";
				String chmod = "chmod 777 /data/app/" + app + ".apk";
				String uninstallapk = "pm uninstall " + app;
				UnInstallerUtils unInstallerUtils = new UnInstallerUtils();
				unInstallerUtils.chmodApk(busybox, chmod);
				boolean ret = unInstallerUtils.uninstallApk(uninstallapk);
				if (ret == true) {
					listener.onSlienceSuccess();
				} else {
					listener.onSlienceFail(0, "");

				}
			}
		});

	}

	public void bathUninstall(final List<String> apps, final BatchSlienceListener listener) {
		ThreadPool.runOnNonUIThread(new Runnable() {
			@Override
			public void run() {for(String app : apps) {
				String busybox = "mount -o remount rw /data";
				String chmod = "chmod 777 /data/app/" + app + ".apk";
				String uninstallapk = "pm uninstall " + app;
				UnInstallerUtils unInstallerUtils = new UnInstallerUtils();
				unInstallerUtils.chmodApk(busybox, chmod);
				boolean ret = unInstallerUtils.uninstallApk(uninstallapk);
				if (ret == true) {
					listener.onSlienceSuccess(app);
				} else {
					listener.onSlienceFail(app);

				}
			}

				listener.onComplete();


			}
		});
	}

	public interface SlienceListener {

		/**
		 * @author zengdengyi
		 *            app packageName
		 * @param code
		 *            0 faiure 1 success
		 */
		public void onSlienceFail(int code, String msg);
		public void onSlienceSuccess();

	}

	public interface BatchSlienceListener {

		/**
		 * @author zengdengyi
		 *            app packageName
		 */
		public void onSlienceFail(String pkgName);
		public void onSlienceSuccess(String pkgName);
		public void onComplete();
	}
}
