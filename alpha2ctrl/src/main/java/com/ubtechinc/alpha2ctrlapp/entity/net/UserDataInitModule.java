package com.ubtechinc.alpha2ctrlapp.entity.net;

import android.support.annotation.Keep;

import com.ubtechinc.nets.http.Url;

import java.util.List;

/**
 * @ClassName UserDataInitModule
 * @date 6/3/2017
 * @author tanghongyu
 * @Description 用户数据初始化合并請求
 * @modifier
 * @modify_time
 */
@Keep
public class UserDataInitModule {
    @Url("alpha2-web/request/batchDispatcher")
    @Keep
    public class Request {

        private String uri;
        private String requestMethod;
        private String contentType;
        private String parameters;
        private String headers;

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public String getRequestMethod() {
            return requestMethod;
        }

        public void setRequestMethod(String requestMethod) {
            this.requestMethod = requestMethod;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public String getParameters() {
            return parameters;
        }

        public void setParameters(String parameters) {
            this.parameters = parameters;
        }

        public String getHeaders() {
            return headers;
        }

        public void setHeaders(String headers) {
            this.headers = headers;
        }
    }

    @Keep
    public class Result {

        private String result;

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }

    @Keep
    public class Response extends BaseResponse {

        private List<Result> data;

        public List<Result> getData() {
            return data;
        }

        public void setData(List<Result> data) {
            this.data = data;
        }
    }



}
