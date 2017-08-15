package com.ubtechinc.alpha.network.module;

import android.support.annotation.Keep;

import com.ubtechinc.nets.http.Url;

/**
 * Created by hongjie.xiang on 2017/6/1.
 */
@Keep
public class AddPhotoModule {
    @Url("robot/addPhoto")
    @Keep
    public  static class Request{


        private String robotSeq ;
        private String imageOriginalUrl ;
        private String userId ;

        public String getRobotSeq() {
            return robotSeq;
        }

        public void setRobotSeq(String robotSeq) {
            this.robotSeq = robotSeq;
        }

        public String getImageOriginalUrl() {
            return imageOriginalUrl;
        }

        public void setImageOriginalUrl(String imageOriginalUrl) {
            this.imageOriginalUrl = imageOriginalUrl;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }

    @Keep
    public  class Response{
        public String resultCode;
        public String msg;
        public boolean success;
        @Override
        public String toString() {
            return "Result{" +
                    "resultCode='" + resultCode + '\'' +
                    ", msg='" + msg + '\'' +
                    '}';
        }
    }


}
