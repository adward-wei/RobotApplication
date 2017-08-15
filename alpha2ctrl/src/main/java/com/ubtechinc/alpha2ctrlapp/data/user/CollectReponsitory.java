package com.ubtechinc.alpha2ctrlapp.data.user;


import android.support.annotation.NonNull;

import com.ubtechinc.alpha2ctrlapp.data.ErrorParser;
import com.ubtechinc.alpha2ctrlapp.entity.net.AddCollectionModule;
import com.ubtechinc.alpha2ctrlapp.entity.net.CancelCollectModule;
import com.ubtechinc.alpha2ctrlapp.entity.net.DeleteCollectModule;
import com.ubtechinc.alpha2ctrlapp.entity.net.GetCollectListModule;
import com.ubtechinc.nets.ResponseListener;
import com.ubtechinc.nets.http.HttpProxy;
import com.ubtechinc.nets.http.ThrowableWrapper;

/**
 * @ClassName CollectReponsitory
 * @date 6/6/2017
 * @author tanghongyu
 * @Description 收藏相关管理类
 * @modifier
 * @modify_time
 */
public class CollectReponsitory implements ICollectDataSource{

    volatile private static CollectReponsitory INSTANCE = null;
    private CollectReponsitory(){}

    public static CollectReponsitory get() {
        try {
            if(INSTANCE != null){//懒汉式

            }else{
                //创建实例之前可能会有一些准备性的耗时工作
                Thread.sleep(300);
                synchronized (CollectReponsitory.class) {
                    if(INSTANCE == null){//二次检查
                        INSTANCE = new CollectReponsitory();
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return INSTANCE;
    }



    public static void destroyInstance() {
        INSTANCE = null;
    }



    @Override
    public void getCollectionList(int appType,final  @NonNull LoadCollectionCallback callback) {
        GetCollectListModule.Request request = new GetCollectListModule().new Request();
        request.setAppType(appType);
        HttpProxy.get().doGet(request, new ResponseListener<GetCollectListModule.Response>() {
            @Override
            public void onError(ThrowableWrapper e) {
                callback.onFail(e);
            }

            @Override
            public void onSuccess(GetCollectListModule.Response response) {

                if(ErrorParser.get().isSuccess(response.getResultCode())) {
                    callback.onSuccess(response.getData().getResult());
                }else {
                    callback.onFail(ErrorParser.get().getThrowableWrapper());
                }

            }
        });
    }

    @Override
    public void doCollect(int collectRelationId, int collectType,final @NonNull CollectCallback callback) {
        AddCollectionModule.Request request = new AddCollectionModule().new Request();
        request.setCollectRelationId(collectRelationId);
        request.setCollectType(collectType);
        HttpProxy.get().doPost(request, new ResponseListener<AddCollectionModule.Response>() {
            @Override
            public void onError(ThrowableWrapper e) {
                callback.onFail(e);
            }

            @Override
            public void onSuccess(AddCollectionModule.Response request) {
                if(ErrorParser.get().isSuccess(request.getResultCode())) {
                    callback.onSuccess();
                }else {
                    callback.onFail(ErrorParser.get().getThrowableWrapper());
                }

            }
        });
    }

    @Override
    public void cancelCollect(int collectRelationId, int collectType,final  @NonNull CollectCallback callback) {
        CancelCollectModule.Request request = new CancelCollectModule().new Request();
        request.setCollectType(collectType);
        request.setCollectRelationId(collectRelationId);
        HttpProxy.get().doPost(request, new ResponseListener<CancelCollectModule.Response>() {
            @Override
            public void onError(ThrowableWrapper e) {
                callback.onFail(e);
            }

            @Override
            public void onSuccess(CancelCollectModule.Response response) {
                if(ErrorParser.get().isSuccess(response.getResultCode())) {
                    callback.onSuccess();
                }else {
                    callback.onFail(ErrorParser.get().getThrowableWrapper());
                }

            }
        });
    }

    @Override
    public void deleteCollect(int collectId, final @NonNull CollectCallback callback) {
        DeleteCollectModule.Request request = new DeleteCollectModule().new Request();
        request.setCollectId(collectId);
        HttpProxy.get().doPost(request, new ResponseListener<DeleteCollectModule.Response>() {
            @Override
            public void onError(ThrowableWrapper e) {
                callback.onFail(e);
            }

            @Override
            public void onSuccess(DeleteCollectModule.Response response) {

                if(ErrorParser.get().isSuccess(response.getResultCode())) {
                    callback.onSuccess();
                }else {
                    callback.onFail(ErrorParser.get().getThrowableWrapper());
                }

            }
        });
    }
}
