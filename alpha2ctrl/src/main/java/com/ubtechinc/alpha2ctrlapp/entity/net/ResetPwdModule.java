package com.ubtechinc.alpha2ctrlapp.entity.net;

import android.support.annotation.Keep;

import com.ubtechinc.nets.http.Url;

/**
 * @ClassName LoginModule
 * @date 6/3/2017
 * @author tanghongyu
 * @Description 重置密码
 * @modifier
 * @modify_time
 */
@Keep
public class ResetPwdModule {
    @Url("alpha2-web/user/resetPassword")
    @Keep
    public class Request {

        //注册帐号 邮箱或手机号码。如果为手机号码需要加上区号如:86
        private String userId;
        //新密码
        private String userPassword;
        private String uuid;


        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserPassword() {
            return userPassword;
        }

        public void setUserPassword(String userPassword) {
            this.userPassword = userPassword;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        @Override
        public String toString() {
            return "VerifyCodeRequest{" +
                    "userId='" + userId + '\'' +
                    ", userPassword='" + userPassword + '\'' +
                    ", uuid='" + uuid + '\'' +
                    '}';
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
