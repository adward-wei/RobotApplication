package com.ubtechinc.alpha2ctrlapp.entity.net;

import android.support.annotation.Keep;

import com.ubtechinc.nets.http.Url;


/**
 * Created by hongjie.xiang on 2017/6/1.
 */
@Keep
public class GetTokenModule {
    @Url("alpha2-web/qiniu/getToken")
    @Keep
    public static class Request{
     //1：用户图像 2：机器人图像 3：用户相册
        private int fileType;

        public int getFileType() {
            return fileType;
        }

        public void setFileType(int fileType) {
            this.fileType = fileType;
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
    public static class Result{
        public String domain ;
        public String key ;
        public String token ;
        @Override
        public String toString() {
            return "Result{" +
                    "domain='" + domain + '\'' +
                    ", key='" + key + '\'' +
                    ", token='" + token + '\'' +
                    '}';
        }
    }

    @Keep
    public static class Data{
        public Result result;
    }

}
