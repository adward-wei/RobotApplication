package com.ubtechinc.alpha2ctrlapp.entity.net;

import android.support.annotation.Keep;

import com.ubtechinc.nets.http.Url;

import java.util.Map;

/**
 * @ClassName GetActionListModule
 * @date 6/7/2017
 * @author tanghongyu
 * @Description 获取机器人列表
 * @modifier
 * @modify_time
 */
@Keep
public class GetRobotIMStatusModule {
    @Url("im/isOnline")
    @Keep
    public class Request {
        private String signature;
        private String time;
        private String userId;
        private String channel;
        //账号以逗号隔开
        private String accounts;

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getAccounts() {
            return accounts;
        }

        public void setAccounts(String accounts) {
            this.accounts = accounts;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }
    }

    @Keep
    public class Response {

        private String returnCode;
        private String returnMsg;
        private Map<String, String > returnMap;

        public String getReturnCode() {
            return returnCode;
        }

        public void setReturnCode(String returnCode) {
            this.returnCode = returnCode;
        }

        public String getReturnMsg() {
            return returnMsg;
        }

        public void setReturnMsg(String returnMsg) {
            this.returnMsg = returnMsg;
        }

        public Map<String, String> getReturnMap() {
            return returnMap;
        }

        public void setReturnMap(Map<String, String> returnMap) {
            this.returnMap = returnMap;
        }
    }



}
