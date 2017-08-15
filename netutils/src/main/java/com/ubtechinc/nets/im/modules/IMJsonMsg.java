package com.ubtechinc.nets.im.modules;

/**
 * Created by Administrator on 2017/6/6.
 */

    public class IMJsonMsg {
        public IMJsonHeader header;
        public String bodyData; //json格式


    public static class IMJsonHeader {
        public int commandId;
        public String versionCode;
    }
}
