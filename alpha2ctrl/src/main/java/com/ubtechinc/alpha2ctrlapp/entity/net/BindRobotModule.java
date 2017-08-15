package com.ubtechinc.alpha2ctrlapp.entity.net;

import android.support.annotation.Keep;

import com.ubtechinc.nets.http.Url;
/**
 * @ClassName BindRobotModule
 * @date 6/7/2017
 * @author tanghongyu
 * @Description 绑定机器人
 * @modifier
 * @modify_time
 */
@Keep
public class BindRobotModule {
    @Url("alpha2-web/relation/registerRobot")
    @Keep
    public class Request {
        //机器人用户名（即机器人序列号）
        private String userName;
        //绑定人id
        private String userOnlyId;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserOnlyId() {
            return userOnlyId;
        }

        public void setUserOnlyId(String userOnlyId) {
            this.userOnlyId = userOnlyId;
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
        private String macAddress;

        public String getMacAddress() {
            return macAddress;
        }

        public void setMacAddress(String macAddress) {
            this.macAddress = macAddress;
        }
    }

    @Keep
    public class Data {
        private Result result;

        public Result getResult() {
            return result;
        }

        public void setResult(Result result) {
            this.result = result;
        }
    }

}
