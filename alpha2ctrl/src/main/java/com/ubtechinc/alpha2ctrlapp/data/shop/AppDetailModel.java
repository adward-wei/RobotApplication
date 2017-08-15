package com.ubtechinc.alpha2ctrlapp.data.shop;

import android.content.Context;
import android.os.Message;
import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.base.BaseHandler;
import com.ubtechinc.alpha2ctrlapp.constants.NetWorkConstant;
import com.ubtechinc.alpha2ctrlapp.data.user.CollectReponsitory;
import com.ubtechinc.alpha2ctrlapp.data.user.ICollectDataSource;
import com.ubtechinc.alpha2ctrlapp.entity.AppDetail;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.RobotApp;
import com.ubtechinc.alpha2ctrlapp.entity.request.GetShareUrlRequest;
import com.ubtechinc.alpha2ctrlapp.ui.activity.main.MainPageActivity;
import com.ubtechinc.nets.http.ThrowableWrapper;

/**
 * Created by tanghongyu on 2016/10/16.
 */

public class AppDetailModel implements IAppDetailModel {

    private static final String TAG = "AppDetailModel";
    private MyHandler mHandler;
    private int mAppStatus;
    private ICollectListener iCollectListener;
    private IDownloadListener iDownloadListener;
    private ICancelCollectListener iCancelCollectListener;
    private IGetShareUrlListener iGetShareUrlListener;
    private ILostConnectListener iLostConnectListener;
    private Context mContext;
    AppReponsitory appReponsitory;
    public AppDetailModel(Context context) {

        appReponsitory = AppReponsitory.getInstance(AppRemoteDataSource.getInstance(context));
        mContext = context;

    }


    @Override
    public void getAppDetail(Context context, int appId, @NonNull IAppDataSource.LoadAppDetailDataCallback callback) {
        appReponsitory.getAppDetail(appId, callback);

    }




    @Override
    public void unRegisterHandler() {

        Alpha2Application.getInstance().unregisterHandler(mHandler);
    }

    @Override
    public void getAppShareUrl(IGetShareUrlListener listener) {
        this.iGetShareUrlListener = listener;
        GetShareUrlRequest r = new GetShareUrlRequest();
        r.setType("appshare");
        r.setCode("url");
//        mUserAction.setParamerObj(r);
//        mUserAction.doRequest(NetWorkConstant.REQUEST_GET_SHARE_URL, "");
    }

    @Override
    public void addLostConnectListener(ILostConnectListener lostConnectListener) {
        this.iLostConnectListener = lostConnectListener;
    }

    @Override
    public void downloadRobotApp(AppDetail appDetail, MainPageActivity activity, IDownloadListener listener) {

        iDownloadListener = listener;
        Logger.d(TAG, " run downloadRobotApp iDownloadListener = " + iDownloadListener + " thread id = " + Thread.currentThread().getId());
        RobotApp app = new RobotApp();
        app.setName(appDetail.getAppName());
        app.setPackageName(appDetail.getAppPackage());
        app.setUrl(appDetail.getAppPath());
        app.setAppKey(appDetail.getAppKey());
        //发消息到机器人下载应用
        activity.downLoadApp(app, appDetail.getAppIcon(), appDetail.getVersionCode());
    }

    @Override
    public void addCollector(int appId, AppDetail appDetail, ICollectListener listener) {
        iCollectListener = listener;

        CollectReponsitory.get().doCollect(appId, 2, new ICollectDataSource.CollectCallback() {
            @Override
            public void onSuccess() {
                if (iCollectListener != null)
                    iCollectListener.onCollectResult(true);
            }

            @Override
            public void onFail(ThrowableWrapper e) {
                if (iCollectListener != null)
                    iCollectListener.onCollectResult(false);

            }
        });



    }

    @Override
    public void cancleCollect(int appId, ICancelCollectListener listener) {
        iCancelCollectListener = listener;
        CollectReponsitory.get().cancelCollect(appId, 2, new ICollectDataSource.CollectCallback() {
            @Override
            public void onSuccess() {
                if (iCancelCollectListener != null)
                    iCancelCollectListener.onCancelCollectResult(true);

            }

            @Override
            public void onFail(ThrowableWrapper e) {
                if (iCancelCollectListener != null)
                    iCancelCollectListener.onCancelCollectResult(false);

            }
        });

    }


    private class MyHandler extends BaseHandler {

        @Override
        public void handleMessage(Message msg) {
            if (msg == null) {
                Logger.e(TAG, "handleMessage msg is null.");
                return;
            }
            switch (msg.what) {





                case NetWorkConstant.RESPONSE_GET_SHARE_URL_SUCCESS:
                    if (iGetShareUrlListener != null)

                        iGetShareUrlListener.onGetShareUrlResult((String) msg.obj);
                    break;

            }
        }
    }


}
