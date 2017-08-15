package com.ubtechinc.alpha2ctrlapp.entity.net;

import android.support.annotation.Keep;

import com.ubtechinc.nets.http.Url;

import java.util.List;

/**
 * @ClassName LoginModule
 * @date 6/3/2017
 * @author tanghongyu
 * @Description 验证获取到的验证码
 * @modifier
 * @modify_time
 */
@Keep
public class VerifyResetPwdCodeModule {
    @Url("alpha2-web/user/password/verificationCode")
    @Keep
    public class Request {

        //注册帐号 邮箱或手机号码。如果为手机号码需要加上区号如:86
        private String userEmail;
        private String userPhone;
        private String verificationCode;

        public String getUserEmail() {
            return userEmail;
        }

        public void setUserEmail(String userEmail) {
            this.userEmail = userEmail;
        }

        public String getUserPhone() {
            return userPhone;
        }

        public void setUserPhone(String userPhone) {
            this.userPhone = userPhone;
        }

        public String getVerificationCode() {
            return verificationCode;
        }

        public void setVerificationCode(String verificationCode) {
            this.verificationCode = verificationCode;
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
    public class Result {
        private String uuid;
        private String userId;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        @Override
        public String toString() {
            return "Result{" +
                    "uuid='" + uuid + '\'' +
                    ", userId='" + userId + '\'' +
                    '}';
        }
    }
    @Keep
    public class Data {
        private List<Result> result;

        public List<Result> getResult() {
            return result;
        }

        public void setResult(List<Result> result) {
            this.result = result;
        }
    }

}
