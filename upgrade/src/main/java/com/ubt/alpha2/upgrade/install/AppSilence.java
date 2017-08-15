package com.ubt.alpha2.upgrade.install;

import com.ubt.alpha2.upgrade.utils.LogUtils;

import java.io.File;

/**
 * Created by ubt on 2017/6/27.
 */

public class AppSilence {

    public void onSilence(final String path, final SilenceListener
                          listener) {
        Thread installThread = new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                ApkInstaller apkInstaller = new ApkInstaller();
                String ret = apkInstaller.install(path);
                LogUtils.d("AppSlience", "update result=" + ret);
                if (ret.toLowerCase().contains("success")) {
                    listener.onSilenceResult(1);
                } else {
                    if(ret.toLowerCase().contains("insufficient_storage")) {
                        listener.onSilenceResult(2);
                    } else {
                        listener.onSilenceResult(0);
                    }

                }
                deleteFile(path);
            }
        });
        installThread.start();
    }

    public void onSilenceSync(final String path, final SilenceListener listener, final boolean isBuiltIn,
                          final boolean is2Mic) {
        ApkInstaller apkInstaller = new ApkInstaller();
        String ret;
        LogUtils.d("onSilenceSync: is2Mic: "+is2Mic);
        if(!is2Mic) {
            ret = apkInstaller.install(path, isBuiltIn);
        } else {
            /** 2麦的机器只有内部存储 **/
            ret = apkInstaller.install(path);
        }
        LogUtils.d("AppSilence", "update result=" + ret);
        if (ret.toLowerCase().contains("success")) {
            listener.onSilenceResult(1);
            deleteFile(path);
        } else {
            if(ret.toLowerCase().contains("insufficient_storage")) {
                listener.onSilenceResult(2);
            } else {
                listener.onSilenceResult(0);
            }
        }
    }

    public void onSilence(final String path, final SilenceListener listener, final boolean isBuiltIn,
                          final boolean is2Mic) {
        Thread installThread = new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                ApkInstaller apkInstaller = new ApkInstaller();
                String ret = null;
                if(!is2Mic) {
                    ret = apkInstaller.install(path, isBuiltIn);
                } else {
                    /** 2麦的机器只有内部存储 **/
                    ret = apkInstaller.install(path);
                }
                LogUtils.d("AppSlience", "update result=" + ret);
                if (ret.toLowerCase().contains("success")) {
                    listener.onSilenceResult(1);
                } else {
                    if(ret.toLowerCase().contains("insufficient_storage")) {
                        listener.onSilenceResult(2);
                    } else {
                        listener.onSilenceResult(0);
                    }

                }
                deleteFile(path);
            }
        });
        installThread.start();
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

    public void onUnInstallSync(final String app, final SilenceListener listener){
        String busybox = "mount -o remount rw /data";
        String chmod = "chmod 777 /data/app/" + app + ".apk";
        String uninstallapk = "pm uninstall " + app;
        UnInstallerUtils unInstallerUtils = new UnInstallerUtils();
        unInstallerUtils.chmodApk(busybox, chmod);
        boolean ret = unInstallerUtils.uninstallApk(uninstallapk);
        if (ret == true) {
            listener.onSilenceResult(1);
        } else {
            listener.onSilenceResult(0);
        }
    }

    public void onUnInstall(final String app, final SilenceListener listener) {
        Thread unInstallThread = new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                String busybox = "mount -o remount rw /data";
                String chmod = "chmod 777 /data/app/" + app + ".apk";
                String uninstallapk = "pm uninstall " + app;
                UnInstallerUtils unInstallerUtils = new UnInstallerUtils();
                unInstallerUtils.chmodApk(busybox, chmod);
                boolean ret = unInstallerUtils.uninstallApk(uninstallapk);
                if (ret == true) {
                    listener.onSilenceResult(1);
                } else {
                    listener.onSilenceResult(0);

                }
            }
        });
        unInstallThread.start();

    }

    public interface SilenceListener {

        /**
         * @author zengdengyi
         * @param appPackageName
         *            app packageName
         * @param code
         *            0 faiure 1 success
         */
        void onSilenceResult(int code);
    }
}
