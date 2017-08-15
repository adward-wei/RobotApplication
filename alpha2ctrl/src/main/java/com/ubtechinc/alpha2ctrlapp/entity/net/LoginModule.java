package com.ubtechinc.alpha2ctrlapp.entity.net;

import android.support.annotation.Keep;

import com.ubtechinc.alpha2ctrlapp.entity.business.user.UserInfo;
import com.ubtechinc.nets.http.Url;

import java.util.List;

/**
 * @ClassName LoginModule
 * @date 6/3/2017
 * @author tanghongyu
 * @Description 登录请求和返回处理
 * @modifier
 * @modify_time
 */
@Keep
public class LoginModule {
    @Url("alpha2-web/user/login")
    @Keep
    public class LoginRequest {

        private String userEmail;
        private String userPhone;
        private String userPassword;

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

        public String getUserPassword() {
            return userPassword;
        }

        public void setUserPassword(String userPassword) {
            this.userPassword = userPassword;
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
        private List<UserInfo> result;

        public List<UserInfo> getResult() {
            return result;
        }

        public void setResult(List<UserInfo> result) {
            this.result = result;
        }
    }

}
