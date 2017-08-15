package com.ubtechinc.nets.im.modules;

import android.support.annotation.Keep;

import com.ubtechinc.nets.http.Url;

/**
 * Created by Administrator on 2017/5/24.
 */
@Keep
public class FindBindModule {
    @Url("robot/findBindStatus")
    @Keep
    public static class Request {

        public String equipmentId;
    }

    @Keep
    public static class Response {
        public String returnCode;
        public String returnMsg;
        public RobotData returnMap;

    }

    @Keep
    public static class RobotData {
        public String accountType;
        public String userSig;
        public String appidAt3rd;
    }

}
