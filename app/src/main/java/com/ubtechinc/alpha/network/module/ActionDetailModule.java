package com.ubtechinc.alpha.network.module;

import android.support.annotation.Keep;

import com.ubtechinc.nets.http.Url;

import java.util.List;

/**
 * Created by hongjie.xiang on 2017/6/1.
 */

@Keep
public class ActionDetailModule {
    @Keep
    @Url("action/detailById")
    public static class Request{

        private String actionOriginalId ;

        public String getActionOriginalId() {
            return actionOriginalId;
        }

        public void setActionOriginalId(String actionOriginalId) {
            this.actionOriginalId = actionOriginalId;
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
        public Result result;

        public Result getResult() {
            return result;
        }

        public void setResult(Result result) {
            this.result = result;
        }
    }

    @Keep
    public static class Result{
        public String actionBrowseTime ;
        public String actionCollectTime ;
        public String actionDate ;
        public String actionDesciber ;
        public String actionDownloadTime ;
        public String actionFilePath ;
        public String actionHeadUrl ;
        public String actionId ;
        public String actionImagePath ;
        public String actionMd5 ;
        public String actionName ;
        public String actionOriginalId ;
        public String actionPath ;
        public String actionPraiseTime ;
        public String actionResource ;
        public String actionResume ;
        public String actionSize ;
        public String actionSonType ;
        public String actionStatusDetail ;
        public String actionTime ;
        public String actionTitle ;
        public String actionType ;
        public String actionUser ;
        public String actionVideoPath ;
        public String appType ;
        public String effTime ;
        public String expTime ;




        @Override
        public String toString() {
            return "Result{" +
                    "actionBrowseTime='" + actionBrowseTime + '\'' +
                    ", actionCollectTime='" + actionCollectTime + '\'' +
                    ", actionDate='" + actionDate + '\'' +
                    ", actionDesciber='" + actionDesciber + '\'' +
                    ", actionDownloadTime='" + actionDownloadTime + '\'' +
                    ", actionFilePath='" + actionFilePath + '\'' +
                    ", actionHeadUrl='" + actionHeadUrl + '\'' +
                    ", actionId='" + actionId + '\'' +
                    ", actionImagePath='" + actionImagePath + '\'' +
                    ", actionMd5='" + actionMd5 + '\'' +
                    ", actionName='" + actionName + '\'' +
                    ", actionOriginalId='" + actionOriginalId + '\'' +
                    ", actionPath='" + actionPath + '\'' +
                    ", actionPath='" + actionPraiseTime + '\'' +
                    ", actionResource='" + actionResource + '\'' +
                    ", actionResume='" + actionResume + '\'' +
                    ", actionSize='" + actionSize + '\'' +
                    ", actionSonType='" + actionSonType + '\'' +
                    ", actionStatusDetail='" + actionStatusDetail + '\'' +
                    ", actionTime='" + actionTime + '\'' +
                    ", actionTitle='" + actionTitle + '\'' +
                    ", actionType='" + actionType + '\'' +
                    ", actionUser='" + actionUser + '\'' +
                    ", actionVideoPath='" + actionVideoPath + '\'' +
                    ", appType='" + appType + '\'' +
                    ", effTime='" + effTime + '\'' +
                    ", expTime='" + expTime + '\'' +
                    '}';
        }
    }

}
