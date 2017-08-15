package com.ubtechinc.alpha.upload;

/**
 * @desc : 上传接口
 * @author: wzt
 * @time : 2017/6/21
 * @modifier:
 * @modify_time:
 */

public interface IUploader {
    void upload(final UploadCBHandler uploadCBHandler, final IUploadResultListener listener);
}
