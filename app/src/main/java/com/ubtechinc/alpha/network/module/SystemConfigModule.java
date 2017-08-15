package com.ubtechinc.alpha.network.module;

import android.support.annotation.Keep;

import com.ubtechinc.nets.http.Url;

import java.util.List;

/**
 * Created by hongjie.xiang on 2017/6/1.
 */
@Keep
public class SystemConfigModule{
    @Url("alpha2-web/robot/findUpgrateConfig")
    @Keep
    public static class Request{

        private String apkLanguage ;
        private String robotVersion ;


        public String getEquipmentId() {
            return apkLanguage;
        }

        public void setEquipmentId(String apkLanguage) {
            this.apkLanguage = apkLanguage;
        }

        public String getControlUserId() {
            return robotVersion;
        }

        public void setControlUserId(String robotVersion) {
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
        public String apkName ;
        public String id ;
        public String newVersion ;
        public String packageName ;
        public String robotVersion ;
        public String status ;
        @Override
        public String toString() {
            return "Result{" +
                    "apkLanguage='" + apkLanguage + '\'' +
                    ", apkName='" + apkName + '\'' +
                    ", id='" + id + '\'' +
                    ", newVersion='" + newVersion + '\'' +
                    ", packageName='" + packageName + '\'' +
                    ", robotVersion='" + robotVersion + '\'' +
                    ", status='" + status + '\'' +

                    '}';
        }

    }

}
