package com.ubtechinc.alpha.network.module;

/**
 * Created by hongjie.xiang on 2017/6/1.
 */

public abstract class BaseModule {
    public static abstract class Response{
        public String resultCode;
        public String msg;
        public boolean success;
    }

    public static class Data<T>{
        public T result;
    }
}
