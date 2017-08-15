package com.ubtechinc.alpha2ctrlapp.entity.net;

import android.support.annotation.Keep;

import com.ubtechinc.nets.http.Url;

/**
 * @ClassName LoginModule
 * @date 6/3/2017
 * @author tanghongyu
 * @Description 获取验证码
 * @modifier
 * @modify_time
 */
@Keep
public class GetRegisterCodeModule {
    @Url("alpha2-web/user/register/verificationCode")
    @Keep
    public class Request {

        //注册帐号 邮箱或手机号码。如果为手机号码需要加上区号如:86
        private String account;
        //类型 1：手机号码，2：邮箱
        private String registerType;

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getRegisterType() {
            return registerType;
        }

        public void setRegisterType(String registerType) {
            this.registerType = registerType;
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
