package com.ubtechinc.alpha.im.msghandler;

import com.ubtechinc.nets.im.modules.IMJsonMsg;

/**
 * Created by Administrator on 2017/6/6.
 */

public class NullJsonHandler implements IMJsonMsgHandler {
    @Override
    public void handleMsg(int requestCmdId, int responseCmdId, IMJsonMsg jsonRequest, String peer) {

    }
}
