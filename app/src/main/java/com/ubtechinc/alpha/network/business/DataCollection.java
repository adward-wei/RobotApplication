package com.ubtechinc.alpha.network.business;

import com.ubtechinc.alpha.network.module.DataCollectionModule;
import com.ubtechinc.nets.ResponseListener;
import com.ubtechinc.nets.http.HttpProxy;

/**
 * Created by hongjie.xiang on 2017/6/1.
 */

public class DataCollection {
    private static final String TAG = "DataCollection";
    private static DataCollection sInstance;
    public static DataCollection getInstance() {
        if (sInstance == null) {
            synchronized (DataCollection.class) {

                if (sInstance == null) {
                    sInstance = new DataCollection();
                }
            }
        }
        return sInstance;
    }

    public void requestDataCollection(String robotSeq, String uploadContext,String languageVersion,String appVersion,ResponseListener<DataCollectionModule.Response> listener) {
        DataCollectionModule.Request request=new DataCollectionModule.Request();
        request.setRobotSeq(robotSeq);
        request.setUploadContext(uploadContext);
        request.setLanguageVersion(languageVersion);
        request.setAppVersion(appVersion);
        HttpProxy.get().doPost(request,listener);
    }

}
