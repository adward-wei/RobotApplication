package com.ubtechinc.zh_chat.nets.business;

import com.ubtechinc.nets.ResponseListener;
import com.ubtechinc.nets.http.HttpProxy;
import com.ubtechinc.zh_chat.nets.module.FindJokeModule;

/**
 * Created by hongjie.xiang on 2017/6/1.
 */

public class FindJoke {
    private static FindJoke sInstance;
    public static FindJoke instance() {
        if (sInstance == null) {
            synchronized (FindJoke.class) {

                if (sInstance == null) {
                    sInstance = new FindJoke();
                }
            }
        }
        return sInstance;
    }

    private FindJoke() {
    }

    public void requestFindJoke(String jokeLanguage, String jokeType, ResponseListener<FindJokeModule.Response> listener) {
        FindJokeModule.Request request=new FindJokeModule.Request();
        request.setJokeLanguage(jokeLanguage);
        request.setJokeType(jokeType);
        HttpProxy.get().doPost(request, listener);
    }

}
