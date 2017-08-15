package com.ubtechinc.alpha.upload;

import com.ubtechinc.alpha.im.IMHeaderField;
import com.ubtechinc.alpha.im.IMsgResponseCallback;

/**
 * @desc : 上传时带的一些回调句柄参数，方便回调时使用
 * @author: wzt
 * @time : 2017/6/21
 * @modifier:
 * @modify_time:
 */

public class UploadCBHandler {
    public int type;
    public String filePath;
    public IMsgResponseCallback msgResponseCallback;
    public IMHeaderField headerField;
}
