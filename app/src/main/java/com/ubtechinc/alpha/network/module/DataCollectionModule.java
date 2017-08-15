package com.ubtechinc.alpha.network.module;


import android.support.annotation.Keep;

import com.ubtechinc.nets.http.Url;

/**
 * Created by hongjie.xiang on 2017/6/1.
 */
@Keep
public class DataCollectionModule {
    @Url("alpha2-web/robot/addDna")
    @Keep
    public static class Request{


        private String robotSeq ;
        private String uploadContext ;
        private String languageVersion ;
        private String appVersion ;

        public String getRobotSeq() {
            return robotSeq;
        }

        public void setRobotSeq(String robotSeq) {
            this.robotSeq = robotSeq;
        }

        public String getUploadContext() {
            return uploadContext;
        }

        public void setUploadContext(String uploadContext) {
            this.uploadContext = uploadContext;
        }

        public String getLanguageVersion() {
            return languageVersion;
        }

        public void setLanguageVersion(String languageVersion) {
            this.languageVersion = languageVersion;
        }

        public String getAppVersion() {
            return appVersion;
        }

        public void setAppVersion(String appVersion) {
            this.appVersion = appVersion;
        }
    }
    @Keep
    public static class Response{
        public String resultCode;
        public String msg;
        @Override
        public String toString() {
            return "Result{" +
                    "resultCode='" + resultCode + '\'' +
                    ", msg='" + msg + '\'' +
                    '}';
        }
    }


}
