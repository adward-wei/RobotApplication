package com.ubtechinc.alpha.im.msghandler;


import com.ubtech.utilcode.utils.notification.NotificationCenter;
import com.ubtechinc.alpha.AlphaMessageOuterClass;
import com.ubtechinc.alpha.event.QueryChargePlayEvent;

/**
 * @ClassName GetChargePlayMsgHandler
 * @date 6/19/2017
 * @author tanghongyu
 * @Description 获取边冲边玩状态
 * @modifier
 * @modify_time
 */
public class GetChargePlayMsgHandler implements IMsgHandler {
    static final String TAG = "SetChargePlayMsgHandler";

    @Override
    public void handleMsg(int requestCmdId, int responseCmdId, AlphaMessageOuterClass.AlphaMessage request, String peer) {

        long requestSerial = request.getHeader().getSendSerial();

        QueryChargePlayEvent event = new QueryChargePlayEvent();
        event.requestSerial = requestSerial;
        event.responseCmdID = responseCmdId;
        event.peer = peer;
        NotificationCenter.defaultCenter().publish(event);
    }
}
