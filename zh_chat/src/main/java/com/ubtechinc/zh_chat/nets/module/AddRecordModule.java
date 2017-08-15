package com.ubtechinc.zh_chat.nets.module;

import android.support.annotation.Keep;

import com.ubtechinc.nets.http.Url;

/**
 * @desc :
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/6/20
 * @modifier:
 * @modify_time:
 */
@Keep
public final class AddRecordModule {

    @Url("alpha2-web/user/operate/addRecord")
    @Keep
    public static class Request{
        private int labelId;
        private String msgLanguage = "CN";//消息语言（“EN”、“CN” ）
        private String pushContent;//推送内容
        private String pushLinks;//推送链接
        private String robotMsg;//机器人回复消息
        private String userMsg;//用户发送消息

        public void setLabelId(int labelId) {
            this.labelId = labelId;
        }

        public void setPushContent(String pushContent) {
            this.pushContent = pushContent;
        }

        public void setPushLinks(String pushLinks) {
            this.pushLinks = pushLinks;
        }

        public void setRobotMsg(String robotMsg) {
            this.robotMsg = robotMsg;
        }

        public void setUserMsg(String userMsg) {
            this.userMsg = userMsg;
        }
    }
    @Keep
    public static class Response{
        public String resultCode;
        public String msg;
        public boolean success;
    }
}
