package com.ubtechinc.alpha2ctrlapp.util.upload;

/**
 * @desc : 上传url到自家服务器
 * @author: wzt
 * @time : 2017/6/21
 * @modifier:
 * @modify_time:
 */

public class UrlUpload2SelfServer {

    private static UrlUpload2SelfServer sUrlUpload2SelfServer;

    private UrlUpload2SelfServer() {}

    public static UrlUpload2SelfServer get() {
        if(sUrlUpload2SelfServer == null) {
            synchronized (UrlUpload2SelfServer.class) {
                if(sUrlUpload2SelfServer == null)
                    sUrlUpload2SelfServer = new UrlUpload2SelfServer();
            }
        }

        return sUrlUpload2SelfServer;
    }


}
