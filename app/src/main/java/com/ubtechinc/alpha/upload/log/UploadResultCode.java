package com.ubtechinc.alpha.upload.log;

/**
 * 将log文件上传到七牛服务器
 *
 * @author wangzhengtian
 * @Date 2017-02-28
 */

public class UploadResultCode {
    public static final int SUCCESS = 0;
    /** 参数错误 **/
    public static final int ERROR_PARAMETER = 1;
    public static final int ERROR_FAIL = 2;
    /** 被别的代码占用 **/
    public static final int ERROR_OCCUPY = 3;
}
