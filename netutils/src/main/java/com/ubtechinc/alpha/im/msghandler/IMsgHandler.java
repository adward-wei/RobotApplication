package com.ubtechinc.alpha.im.msghandler;

import com.ubtechinc.alpha.AlphaMessageOuterClass;

/**
 * Created by Administrator on 2017/5/25.
 */

public interface IMsgHandler {
    void handleMsg(int requestCmdId, int responseCmdId, AlphaMessageOuterClass.AlphaMessage request, String peer);
}
