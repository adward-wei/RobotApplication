package com.ubtechinc.alpha2ctrlapp.data.shop;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.common.base.Preconditions;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.data.ErrorParser;
import com.ubtechinc.alpha2ctrlapp.entity.net.GetActionDetailModule;
import com.ubtechinc.alpha2ctrlapp.entity.net.GetActionListModule;
import com.ubtechinc.alpha2ctrlapp.entity.net.GetLastActionListModule;
import com.ubtechinc.alpha2ctrlapp.entity.net.GetShareUrlModule;
import com.ubtechinc.alpha2ctrlapp.entity.net.PraiseActionModule;
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

public class ActionRemoteDataSource implements IActionDataSource {

    private static final String TAG = "MessageRemoteDataSource";
    private static ActionRemoteDataSource INSTANCE;
    Alpha2Application mContext;
    private ActionRemoteDataSource(@NonNull Context application) {
        Preconditions.checkNotNull(application);
        this.mContext = (Alpha2Application) application;
    }
    public static ActionRemoteDataSource getInstance(@NonNull Context application) {
        Preconditions.checkNotNull(application);
        if (INSTANCE == null) {
            INSTANCE = new ActionRemoteDataSource(application);
        }
        return INSTANCE;
    }


    @Override
    public void getLastActionList(int page, int pageSize, final @NonNull LoadActionCallback callback) {

        GetLastActionListModule.Request request = new GetLastActionListModule().new Request();
        request.setPage(page);
        request.setPagesize(pageSize);
        HttpProxy.get().doGet(request, new ResponseListener<GetActionListModule.Response>() {
            @Override
            public void onError(ThrowableWrapper e) {
                callback.onDataNotAvailable(e);
            }

            @Override
            public void onSuccess(GetActionListModule.Response response) {
                if(ErrorParser.get().isSuccess(response.getResultCode())) {
                    callback.onActionLoaded(response.getData().getResult().getRecords());
                }else {
                    callback.onDataNotAvailable(ErrorParser.get().getThrowableWrapper());
                }

            }
        });

    }

    @Override
    public void searchAction(String actionName, int actionSonType,int page, int pageSize,final  @NonNull LoadActionCallback callback) {

        GetActionListModule.Request request = new GetActionListModule().new Request();
        request.setActionName(actionName);
        request.setActionSonType(actionSonType);
        request.setPage(page);
        request.setPagesize(pageSize);
        HttpProxy.get().doGet(request, new ResponseListener<GetActionListModule.Response>() {
            @Override
            public void onError(ThrowableWrapper e) {
                callback.onDataNotAvailable(e);
            }

            @Override
            public void onSuccess(GetActionListModule.Response response) {


                if(ErrorParser.get().isSuccess(response.getResultCode())) {
                    callback.onActionLoaded(response.getData().getResult().getRecords());
                }else {
                    callback.onDataNotAvailable(ErrorParser.get().getThrowableWrapper());
                }
            }
        });
    }

    @Override
    public void getActionList(int actionSonType,int page, int pageSize,final @NonNull LoadActionCallback callback) {
        GetActionListModule.Request request = new GetActionListModule().new Request();
        request.setActionSonType(actionSonType);
        request.setPage(page);
        request.setPagesize(pageSize);
        HttpProxy.get().doGet(request, new ResponseListener<GetActionListModule.Response>() {
            @Override
            public void onError(ThrowableWrapper e) {
                callback.onDataNotAvailable(e);
            }

            @Override
            public void onSuccess(GetActionListModule.Response response) {
                if(ErrorParser.get().isSuccess(response.getResultCode())) {
                    callback.onActionLoaded(response.getData().getResult().getRecords());
                }else {
                    callback.onDataNotAvailable(ErrorParser.get().getThrowableWrapper());
                }
            }
        });
    }

    @Override
    public void getActionDetail(int actionId, final @NonNull LoadActionDetailCallback callback) {
        GetActionDetailModule.Request request = new GetActionDetailModule().new Request();
        request.setActionId(actionId);
        HttpProxy.get().doGet(request, new ResponseListener<GetActionDetailModule.Response>() {
            @Override
            public void onError(ThrowableWrapper e) {
                callback.onDataNotAvailable(e);
            }

            @Override
            public void onSuccess(GetActionDetailModule.Response response) {
                if(ErrorParser.get().isSuccess(response.getResultCode())) {
                    callback.onActionDetailLoaded(response.getData().getResult());
                }else {
                    callback.onDataNotAvailable(ErrorParser.get().getThrowableWrapper());
                }
            }
        });
    }

    @Override
    public void praiseAction( String praiseObjectId, @NonNull PraiseActionCallback callback) {
        PraiseActionModule.Request request = new PraiseActionModule().new Request();
        request.setAppType(2);
        request.setPraiseObjectId(praiseObjectId);
        request.setPraiseType(1);
    }

    @Override
    public void getShareUrl(String code, String type,final  @NonNull LoadShareUrlCallback callback) {
        GetShareUrlModule.Request request = new GetShareUrlModule().new Request();
        request.setCode(code);
        request.setType(type);
        HttpProxy.get().doGet(request, new ResponseListener<GetShareUrlModule.Response>() {
            @Override
            public void onError(ThrowableWrapper e) {
                callback.onDataNotAvailable(e);
            }

            @Override
            public void onSuccess(GetShareUrlModule.Response response) {
                if(ErrorParser.get().isSuccess(response.getResultCode())) {
                    callback.onShareUrlLoaded(response.getData().getResult());
                }else {
                    callback.onDataNotAvailable(ErrorParser.get().getThrowableWrapper());
                }
            }
        });
    }
}
