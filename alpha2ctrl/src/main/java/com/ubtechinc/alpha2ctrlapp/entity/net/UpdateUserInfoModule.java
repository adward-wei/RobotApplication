package com.ubtechinc.alpha2ctrlapp.entity.net;

import com.ubtechinc.nets.http.Url;

/**
 * @ClassName UpdateUserInfoModule
 * @date 6/3/2017
 * @author tanghongyu
 * @Description 重置密码
 * @modifier
 * @modify_time
 */
public class UpdateUserInfoModule {
    @Url("alpha2-web/user/update")
    public class Request {

        //注册帐号 邮箱或手机号码。如果为手机号码需要加上区号如:86
        private String countryCode;
        private String nickName;
        //新密码
        private String userBirthday;
        private String userImage;

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getUserBirthday() {
            return userBirthday;
        }

        public void setUserBirthday(String userBirthday) {
            this.userBirthday = userBirthday;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public String getUserImage() {
            return userImage;
        }

        public void setUserImage(String userImage) {
            this.userImage = userImage;
        }

        @Override
        public String toString() {
            return "Request{" +
                    "nickName='" + nickName + '\'' +
                    ", userBirthday='" + userBirthday + '\'' +
                    ", userImage='" + userImage + '\'' +
                    '}';
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
