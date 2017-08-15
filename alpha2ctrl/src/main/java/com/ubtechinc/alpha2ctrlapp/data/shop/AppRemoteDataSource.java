package com.ubtechinc.alpha2ctrlapp.data.shop;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.common.base.Preconditions;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.entity.net.BatchGetAppInfoModule;
import com.ubtechinc.alpha2ctrlapp.entity.net.GetAppDetailModule;
import com.ubtechinc.alpha2ctrlapp.entity.net.GetAppListModule;
import com.ubtechinc.alpha2ctrlapp.entity.net.GetRecentAppListModule;
import com.ubtechinc.alpha2ctrlapp.entity.net.SearchAppModule;
import com.ubtechinc.nets.ResponseListener;
import com.ubtechinc.nets.http.HttpProxy;
import com.ubtechinc.nets.http.ThrowableWrapper;

/**
 * @author：tanghongyu
 * @date：4/7/2017 3:36 PM
 * @modifier：tanghongyu
 * @modify_date：4/7/2017 3:36 PM
 * [A brief description]
 * version
 */

public class AppRemoteDataSource implements IAppDataSource {

    private static final String TAG = "MessageRemoteDataSource";
    private static AppRemoteDataSource INSTANCE;
    Alpha2Application mContext;
    private AppRemoteDataSource(@NonNull Context application) {
        Preconditions.checkNotNull(application);
        this.mContext = (Alpha2Application) application;
    }
    public static AppRemoteDataSource getInstance(@NonNull Context application) {
        Preconditions.checkNotNull(application);
        if (INSTANCE == null) {
            INSTANCE = new AppRemoteDataSource(application);
        }
        return INSTANCE;
    }





    @Override
    public void loadAppList(int page, int pageSize, final @NonNull AppListDataCallback callback) {
        GetAppListModule.Request request = new GetAppListModule().new Request();
        request.setPage(page);
        request.setPagesize(pageSize);
        HttpProxy.get().doGet(request, new ResponseListener<GetAppListModule.Response>() {
            @Override
            public void onError(ThrowableWrapper e) {
                callback.onDataNotAvailable(e);
            }

            @Override
            public void onSuccess(GetAppListModule.Response response) {
                callback.onLoadAppList(response.getData().getResult().getRecords());
            }
        });
    }

    @Override
    public void loadRecentAppList(int page, int pageSize, final @NonNull AppListDataCallback callback) {
        GetRecentAppListModule.Request request = new GetRecentAppListModule().new Request();
        request.setPage(page);
        request.setPagesize(pageSize);
        HttpProxy.get().doGet(request, new ResponseListener<GetRecentAppListModule.Response>() {
            @Override
            public void onError(ThrowableWrapper e) {
                callback.onDataNotAvailable(e);
            }

            @Override
            public void onSuccess(GetRecentAppListModule.Response response) {
                callback.onLoadAppList(response.getData().getResult().getRecords());
            }
        });
    }

    @Override
    public void searchApp(String appName, int page, int pageSize,final  @NonNull AppListDataCallback callback) {
        SearchAppModule.Request request = new SearchAppModule().new Request();
        request.setAppName(appName);
        request.setPage(page);
        request.setPageSize(pageSize);
        HttpProxy.get().doGet(request, new ResponseListener<SearchAppModule.Response>() {
            @Override
            public void onError(ThrowableWrapper e) {
                callback.onDataNotAvailable(e);
            }

            @Override
            public void onSuccess(SearchAppModule.Response response) {
                callback.onLoadAppList(response.getData().getResult().getRecords());
            }
        });
    }

    @Override
    public void getAppDetail(int appId, final  @NonNull LoadAppDetailDataCallback callback) {
        GetAppDetailModule.Request request = new GetAppDetailModule().new Request();
        request.setAppId(appId);
        HttpProxy.get().doGet(request, new ResponseListener<GetAppDetailModule.Response>() {
            @Override
            public void onError(ThrowableWrapper e) {
                callback.onDataNotAvailable(e);
            }

            @Override
            public void onSuccess(GetAppDetailModule.Response response) {
                callback.onLoadAppDetail(response.getData().getResult());
            }
        });
    }

    @Override
    public void batchGetAppInfo(String  appKeyList,final  @NonNull AppListDataCallback callback) {
        BatchGetAppInfoModule.Request request = new BatchGetAppInfoModule().new Request();
        request.setAppKey(appKeyList);
        HttpProxy.get().doGet(request, new ResponseListener<BatchGetAppInfoModule.Response>() {
            @Override
            public void onError(ThrowableWrapper e) {
                callback.onDataNotAvailable(e);
            }

            @Override
            public void onSuccess(BatchGetAppInfoModule.Response response) {
                callback.onLoadAppList(response.getData().getResult());
            }
        });
    }
}
