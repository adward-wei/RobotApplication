package com.ubtechinc.alpha.network.module;

import android.support.annotation.Keep;

import com.ubtechinc.nets.http.Url;

import java.util.List;


/**
 * Created by hongjie.xiang on 2017/6/1.
 */
@Keep
public class AppDetailModule  {
    @Url("alpha2-web/robot/findUpgrateDetail")
    @Keep
    public static class Request{


        private String packageName ;
        private String fromVersion ;
        private String toVersion ;
        private String apkLanguage ;
        private String robotVersion ;
        public String getToVersion() {
            return toVersion;
        }

        public void setToVersion(String toVersion) {
            this.toVersion = toVersion;
        }
        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public String getFromVersion() {
            return fromVersion;
        }

        public void setFromVersion(String fromVersion) {
            this.fromVersion = fromVersion;
        }

        public String getApkLanguage() {
            return apkLanguage;
        }

        public void setApkLanguage(String apkLanguage) {
            this.apkLanguage = apkLanguage;
        }

        public String getRobotVersion() {
            return robotVersion;
        }

        public void setRobotVersion(String robotVersion) {
            this.robotVersion = robotVersion;
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
        public String apkLanguage ;
        public String apkMd5 ;
        public String apkUrl ;
        public String apkVersion ;
        public String extendInfo ;
        public String fromVersion ;
        public String id ;
        public String packageName ;
        public String patchMd5 ;
        public String patchUrl ;
        public String robotVersion ;
        public String toVersion ;
        @Override
        public String toString() {
            return "Result{" +
                    "apkLanguage='" + apkLanguage + '\'' +
                    ", apkMd5='" + apkMd5 + '\'' +
                    ", apkUrl='" + apkUrl + '\'' +
                    ", apkVersion='" + apkVersion + '\'' +
                    ", apkUrl='" + apkUrl + '\'' +
                    ", extendInfo='" + extendInfo + '\'' +
                    ", fromVersion='" + fromVersion + '\'' +
                    ", id='" + id + '\'' +
                    ", packageName='" + packageName + '\'' +
                    ", patchMd5='" + patchMd5 + '\'' +
                    ", patchUrl='" + patchUrl + '\'' +
                    ", robotVersion='" + robotVersion + '\'' +
                    ", toVersion='" + toVersion + '\'' +
                    '}';
        }

    }

}
