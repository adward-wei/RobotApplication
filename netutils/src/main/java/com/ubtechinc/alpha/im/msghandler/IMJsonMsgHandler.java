package com.ubtechinc.alpha.im.msghandler;

import com.ubtechinc.nets.im.modules.IMJsonMsg;

/**
 * Created by Administrator on 2017/6/6.
 */

public interface IMJsonMsgHandler {
    public void handleMsg(int requestCmdId, int responseCmdId, IMJsonMsg jsonRequest, String peer);
}
