package com.ubtechinc.zh_chat.nets.module;

import android.support.annotation.Keep;

import com.ubtechinc.nets.http.Url;

import java.util.List;

/**
 * Created by hongjie.xiang on 2017/6/1.
 */
@Keep
public class FindJokeModule {

    @Url("alpha2-web/robot/findJoke")
    @Keep
    public static class Request{
        private String jokeLanguage ;
        private String jokeType ;

        public String getJokeLanguage() {
            return jokeLanguage;
        }

        public void setJokeLanguage(String jokeLanguage) {
            this.jokeLanguage = jokeLanguage;
        }

        public String getJokeType() {
            return jokeType;
        }

        public void setJokeType(String jokeType) {
            this.jokeType = jokeType;
        }
    }

    @Keep
    public static class Response{
        public String resultCode;
        public String msg;
        public boolean success;
        public Data data ;
    }

    @Keep
    public static class Data{
        public List<Result> result;

        public List<Result> getResult() {
            return result;
        }

        public void setResult(List<Result> result) {
            this.result = result;
        }
    }
    @Keep
    public static class Result{
        public String jokeContext ;
        public String jokeId ;
        public String jokeLanguage ;
        public String jokeTitle ;
        public String jokeType ;
        @Override
        public String toString() {
            return "Result{" +
                    "jokeContext='" + jokeContext + '\'' +
                    ", jokeId='" + jokeId + '\'' +
                    ", jokeLanguage='" + jokeLanguage + '\'' +
                    ", jokeTitle='" + jokeTitle + '\'' +
                    ", jokeType='" + jokeType + '\'' +
                    '}';
        }
    }

}
