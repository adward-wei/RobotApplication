package com.ubtechinc.alpha2ctrlapp.entity.net;

import android.support.annotation.Keep;

import com.ubtechinc.nets.http.Url;

/**
 * @ClassName LoginModule
 * @date 6/3/2017
 * @author tanghongyu
 * @Description 登出请求和返回处理
 * @modifier
 * @modify_time
 */
@Keep
public class LoginoutModule {
    @Url("alpha2-web/user/logout")
    @Keep
    public class Request {

        String authorization;

        public String getAuthorization() {
            return authorization;
        }

        public void setAuthorization(String authorization) {
            this.authorization = authorization;
        }
    }

    @Keep
    public class Response extends BaseResponse {

        private Data data;

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }
    }
    @Keep
    public class Data {

    }

}
