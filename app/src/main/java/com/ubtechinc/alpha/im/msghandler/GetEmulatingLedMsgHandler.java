package com.ubtechinc.alpha.im.msghandler;

import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha.AlphaMessageOuterClass;

/**
 * @desc : 获取第三方app按键信息的消息处理器
 * @author: wzt
 * @time : 2017/6/5
 * @modifier:
 * @modify_time:
 */

public class GetEmulatingLedMsgHandler implements IMsgHandler {
    static final String TAG = "GetEmulatingLedMsgHandler";
    @Override
    public void handleMsg(int requestCmdId, int responseCmdId, AlphaMessageOuterClass.AlphaMessage request, String peer) {

        LogUtils.d(TAG,String.valueOf(requestCmdId));



    }


}
