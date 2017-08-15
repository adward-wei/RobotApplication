package com.ubtechinc.alpha2ctrlapp.data.shop;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.common.base.Preconditions;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.data.ErrorParser;
import com.ubtechinc.alpha2ctrlapp.entity.net.GetRecommendModule;
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

public class ADRemoteDataSource implements IADDataSource {

    private static final String TAG = "ADRemoteDataSource";
    private static ADRemoteDataSource INSTANCE;
    Alpha2Application mContext;
    private ADRemoteDataSource(@NonNull Context application) {
        Preconditions.checkNotNull(application);
        this.mContext = (Alpha2Application) application;
    }
    public static ADRemoteDataSource getInstance(@NonNull Context application) {
        Preconditions.checkNotNull(application);
        if (INSTANCE == null) {
            INSTANCE = new ADRemoteDataSource(application);
        }
        return INSTANCE;
    }




    @Override
    public void getAdvertisingPromotion(String language,final  @NonNull ADDataCallback callback) {
        GetRecommendModule.Request request = new GetRecommendModule().new Request();
        request.setSystemLanguage(language);
        HttpProxy.get().doGet(request, new ResponseListener<GetRecommendModule.Response>() {
            @Override
            public void onError(ThrowableWrapper e) {
                callback.onDataNotAvailable(e);
            }

            @Override
            public void onSuccess(GetRecommendModule.Response response) {

                if(ErrorParser.get().isSuccess(response.getResultCode())) {
                    callback.onLoadADData(response.getData().getResult());
                }else {
                    callback.onDataNotAvailable(ErrorParser.get().getThrowableWrapper());
                }
            }
        });

    }
}
