package com.ubtechinc.alpha.network.business;

import android.support.annotation.Keep;

import com.ubtechinc.alpha.network.module.AddPhotoModule;
import com.ubtechinc.nets.ResponseListener;
import com.ubtechinc.nets.http.HttpProxy;

/**
 * Created by hongjie.xiang on 2017/6/1.
 */
@Keep
public class AddPhoto {
    private static final String TAG = "FindJoke";
    private static AddPhoto sInstance;
    public static AddPhoto getInstance() {
        if (sInstance == null) {
            synchronized (AddPhoto.class) {

                if (sInstance == null) {
                    sInstance = new AddPhoto();
                }
            }
        }
        return sInstance;
    }


    public void requestAddPhoto(String robotSeq, String imageOriginalUrl,String userId,ResponseListener<AddPhotoModule.Response> listener) {
        AddPhotoModule.Request request=new AddPhotoModule.Request();
        request.setRobotSeq(robotSeq);
        request.setImageOriginalUrl(imageOriginalUrl);
        request.setUserId(userId);
        HttpProxy.get().doPost(request, listener);


    }

}
