package com.ubtechinc.alpha.upload;

import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha.network.business.AddPhoto;
import com.ubtechinc.alpha.network.module.AddPhotoModule;
import com.ubtechinc.alpha.robotinfo.RobotState;
import com.ubtechinc.nets.ResponseListener;
import com.ubtechinc.nets.http.ThrowableWrapper;

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

    public void upload(String url) {
        AddPhoto.getInstance().requestAddPhoto(RobotState.get().getSid(),
                url,
                "",
                new ResponseListener<AddPhotoModule.Response>() {
                    @Override
                    public void onError(ThrowableWrapper e) {
                        LogUtils.d("AddPhoto fail");
                        e.printStackTrace();
                    }

                    @Override
                    public void onSuccess(AddPhotoModule.Response response) {
                        LogUtils.d("AddPhoto success");
                    }
                });
    }
}
