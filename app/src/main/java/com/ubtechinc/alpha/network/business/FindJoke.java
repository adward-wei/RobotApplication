package com.ubtechinc.alpha.network.business;

import android.content.Context;

import com.ubtechinc.alpha.network.module.FindJokeModule;
import com.ubtechinc.nets.ResponseListener;
import com.ubtechinc.nets.http.HttpProxy;

/**
 * Created by hongjie.xiang on 2017/6/1.
 */

public class FindJoke {
    private static final String TAG = "FindJoke";
    private Context context;
    private static FindJoke sInstance;
    public static FindJoke getInstance() {
        if (sInstance == null) {
            synchronized (FindJoke.class) {

                if (sInstance == null) {
                    sInstance = new FindJoke();
                }
            }
        }
        return sInstance;
    }


    public void requestFindJoke(String jokeLanguage, String jokeType, ResponseListener<FindJokeModule.Response> listener) {
        FindJokeModule.Request request=new FindJokeModule.Request();
        request.setJokeLanguage(jokeLanguage);
        request.setJokeType(jokeType);
        HttpProxy.get().doPost(request, listener);


    }

}
