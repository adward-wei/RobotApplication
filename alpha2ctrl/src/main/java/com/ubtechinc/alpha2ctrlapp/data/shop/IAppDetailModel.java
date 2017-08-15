package com.ubtechinc.alpha2ctrlapp.data.shop;

import android.content.Context;
import android.support.annotation.NonNull;

import com.ubtechinc.alpha2ctrlapp.entity.AppDetail;
import com.ubtechinc.alpha2ctrlapp.ui.activity.main.MainPageActivity;


/**
 * Created by tanghongyu on 2016/10/16.
 */

public interface IAppDetailModel {

    public interface ICancelCollectListener {

        public void onCancelCollectResult(boolean isSuccess);
    }
    public interface ICollectListener {

        public void onCollectResult(boolean isSuccess);
        public void onRepeat();
    }
    public interface IDownloadListener {

        public void onDownloadResult(int code);
    }


    public interface IGetShareUrlListener {

        public void onGetShareUrlResult(String url);
    }



    public interface ILostConnectListener {

        public void onLostConnect(String macAddress);
    }

    public void getAppDetail(Context context, int appId, @NonNull IAppDataSource.LoadAppDetailDataCallback callback);
    public void addCollector(int appId, AppDetail appDetail, ICollectListener listener);
    public void cancleCollect(int appId, ICancelCollectListener listener);
    public void unRegisterHandler();
    public void getAppShareUrl(IGetShareUrlListener listener);
    public void addLostConnectListener(ILostConnectListener lostConnectListener);
    public void downloadRobotApp(AppDetail appDetail, MainPageActivity activity, IDownloadListener listener);

}
