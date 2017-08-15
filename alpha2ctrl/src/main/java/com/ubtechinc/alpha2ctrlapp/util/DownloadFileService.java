package com.ubtechinc.alpha2ctrlapp.util;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.ubtechinc.alpha2ctrlapp.entity.business.app.ApkDownLoad;

/**
 * @ClassName DownloadFileService
 * @date 2016/10/13
 * @author tanghongyu
 * @Description 文件下载服务
 * @modifier
 * @modify_time
 */
public class DownloadFileService extends Service {


    @Override
    public void onCreate() {
        DownloadFileUtils.getInstance().initBaseData(this, 1);
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {

        return  binder;
    }
    public void startDownload(ApkDownLoad apk) {
        DownloadFileUtils.getInstance().addDownloadFile(apk);
    }
    private BinderImpl binder = new BinderImpl();
    public class BinderImpl extends Binder {

        public DownloadFileService getService() {
            return DownloadFileService.this;
        }
    }


}
