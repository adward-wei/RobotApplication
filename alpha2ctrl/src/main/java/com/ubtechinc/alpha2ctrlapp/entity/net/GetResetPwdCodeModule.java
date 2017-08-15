package com.ubtechinc.alpha2ctrlapp.entity.net;

import com.ubtechinc.nets.http.Url;

/**
 * @ClassName LoginModule
 * @date 6/3/2017
 * @author tanghongyu
 * @Description 获取重置密码验证码
 * @modifier
 * @modify_time
 */
public class GetResetPwdCodeModule {
    @Url("alpha2-web/user/password/verificationCode")
    public class Request {

        //注册帐号 邮箱或手机号码。如果为手机号码需要加上区号如:86
        private String account;
        //类型 1：手机号码，2：邮箱
        private int accountType;

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public int getAccountType() {
            return accountType;
        }

        public void setAccountType(int accountType) {
            this.accountType = accountType;
        }
    }

    public class Response extends BaseResponse {

        private Data data;

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }
    }

    public class Data {

    }

}
