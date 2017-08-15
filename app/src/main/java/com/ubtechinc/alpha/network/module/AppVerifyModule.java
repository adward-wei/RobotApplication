package com.ubtechinc.alpha.network.module;

import android.support.annotation.Keep;

import com.ubtechinc.nets.http.Url;

/**
 * Created by hongjie.xiang on 2017/6/1.
 */
@Keep
public class AppVerifyModule {
    @Url("alpha2-web/app/verify")
    @Keep
    public static class Request{


        private String appKey ;
        private String packageName ;

        public String getAppKey() {
            return appKey;
        }

        public void setAppKey(String appKey) {
            this.appKey = appKey;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }
    }

    @Keep
    public class Response{
        public String resultCode ;
        public String msg;
        public Data data;

    }
    @Keep
    public class Data {
        public String result;

        @Override
        public String toString() {
            return "Result{" +
                    "result='" + result + '\'' +
                    '}';
        }
    }
}
