package com.ubtechinc.alpha2ctrlapp.util.upload;


/**
 * @desc : 上传结果回调
 * @author: wzt
 * @time : 2017/6/21
 * @modifier:
 * @modify_time:
 */

public interface IUploadResultListener {
    void onUploadSuccess(String url, final UploadCBHandler uploadCBHandler);
    void onUploadFail(String respInfo, final UploadCBHandler uploadCBHandler);
}
