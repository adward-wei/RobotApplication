package com.ubt.alpha2.upgrade.install;

import android.content.Context;

import com.ubt.alpha2.upgrade.UpgradeApplication;
import com.ubt.alpha2.upgrade.bean.PowerInfo;
import com.ubt.alpha2.upgrade.bean.VersionConfigs;
import com.ubt.alpha2.upgrade.utils.Alpha2Property;
import com.ubt.alpha2.upgrade.utils.ApkUtils;
import com.ubt.alpha2.upgrade.utils.ConditionUtil;
import com.ubt.alpha2.upgrade.utils.Constants;
import com.ubt.alpha2.upgrade.utils.LogUtils;
import com.ubt.alpha2.upgrade.utils.PropertyUtils;
import com.ubt.alpha2.upgrade.utils.ServiceUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
/**
 * @author: slive
 * @description:  安装器(静默安装)
 * @create: 2017/7/11
 * @email: slive.shu@ubtrobot.com
 * @modified: slive
 */
public class ApkInstaller {
	//需卸载的apk
	private static List<VersionConfigs.ApkInfo> unInstallApkLists;
	//需安装的apk
	private static List<VersionConfigs.ApkInfo> installApkLists = new ArrayList<>();
	private static int pos;

	public String install(String apkAbsolutePath ) {

		String[] args = { "pm", "install", "-r", apkAbsolutePath };
		String result = "";
		ProcessBuilder processBuilder = new ProcessBuilder(args);
		Process process = null;
		InputStream errIs = null;
		InputStream inIs = null;
		try {
			LogUtils.d("install");
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int read = -1;
			LogUtils.d("processBuilder.start()");
			process = processBuilder.start();
			errIs = process.getErrorStream();
			LogUtils.d("read");
			while ((read = errIs.read()) != -1) {
				baos.write(read);
			}
			LogUtils.d("write");
			baos.write('/' + 'n');
			inIs = process.getInputStream();
			while ((read = inIs.read()) != -1) {
				baos.write(read);
			}
			LogUtils.d("baos.toByteArray()");
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
//			if (process != null) {
//				process.destroy();
//			}
		}

		return result;
	}

	public String install(String apkAbsolutePath, boolean isBuiltIn) {
		ProcessBuilder processBuilder = null;
		LogUtils.d("install: "+apkAbsolutePath+"  isBuiltIn: "+isBuiltIn);
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
//			if (process != null) {
//				process.destroy();
//			}
		}

		return result;
	}

	public static void startInstallBackupApps(VersionConfigs versionConfigs){
		Context context = UpgradeApplication.getContext();
		//检测电量是否充足或者是否在充电
		//(如果因为电量不够而没有升级，则下次轮询时 无论什么情况都应该升级，
		//因为出现备份没删除已经算是特殊异常了）
		if(!ConditionUtil.checkPowerForMainserviceUpgrade()) {
			LogUtils.d("power insufficient");
			return;
		}
		if(PropertyUtils.getSystemProperty(context,Constants.IS_LYNX_INSTALLING).equals("true") ){
			LogUtils.d("all 系统正在升级");
			return;
		}
		PropertyUtils.setSystemProperty(context, Constants.IS_LYNX_INSTALLING,"true");
		PropertyUtils.setSystemProperty(context,Constants.IS_MAINSERVICE_UPDATE,"true");

		unInstallApkLists = new ArrayList<>();
		int size = versionConfigs.apks.size();
		for(int i = 0;i<size;i++){
			if(!versionConfigs.apks.get(i).packagename.equals(ServiceUtils.MAINSERVICE_PACKAGE_NAME)){//不卸载主服务
				if(ApkUtils.isAvilible(context,versionConfigs.apks.get(i).packagename.trim())){ //保证需要卸载的app存在
					unInstallApkLists.add(versionConfigs.apks.get(i));
				}
				installApkLists.add(versionConfigs.apks.get(i));
			}
		}

		if(unInstallApkLists.size() == 0){
			LogUtils.d("unInstallApkLists.size() == 0");
			//卸载的文件一个没有,则准备安装备份文件
			startBackupApkList();
		}else {
			unInstall(unInstallApkLists);
		}
	}

	//卸载
	public static void unInstall(final List<VersionConfigs.ApkInfo> list){
		AppSilence app = new AppSilence();
		app.onUnInstallSync(list.get(pos).packagename.trim(), new AppSilence.SilenceListener() {
			@Override
			public void onSilenceResult(int code) {
				LogUtils.d("安装备份 code="+code);
				if(code == 1){
					LogUtils.d("安装备份 卸载单个成功");
					//检测是否安装的都卸载完毕，没卸载完 则继续卸载
					// 如果卸载完了，则安装备份apk
					if(pos +1 == list.size()){
						LogUtils.d("安装备份 全部卸载成功");
						//准备安装备份文件
						startBackupApkList();
					}else {
						pos++;
						LogUtils.d("安装备份 继续卸载下一个");
						unInstall(list);
					}
				}else if(code == 0) {
					//系统权限没发现卸载不成功的情况，不予考虑
					LogUtils.d("安装备份 卸载失败");
				}
			}
		});
	}


	private static int delPos =0 ;//删除多余的apk的位置

	public static void unInstallDelApk(final ArrayList<String> list){
		delPos = 0;
		unInstallDelApkInner(list);
	}
	//卸载
	private static void unInstallDelApkInner(final ArrayList<String> list){
		AppSilence app = new AppSilence();
		app.onUnInstallSync(list.get(delPos), new AppSilence.SilenceListener() {
			@Override
			public void onSilenceResult(int code) {
				LogUtils.d(" code="+code);
				if(code == 1){
					LogUtils.d("删除多余的apk 卸载单个成功");
					//检测是否安装的都卸载完毕，没卸载完 则继续卸载
					// 如果卸载完了，则安装备份apk
					if(delPos +1 == list.size()){
						LogUtils.d("删除多余的apk 全部卸载成功");
					}else {
						delPos++;
						LogUtils.d("删除多余的apk 继续卸载下一个");
						ApkInstaller.unInstallDelApk(list);
					}
				}else if(code == 0) {
					//系统权限没发现卸载不成功的情况，不予考虑
					LogUtils.d("卸载失败");
				}
			}
		});
	}

	private static void startBackupApkList(){
		BaseInstallManager baseInstallManager = new InstallBackupAppManager();
		baseInstallManager.startInstallBackupApk(installApkLists);
	}
}
