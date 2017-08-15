package com.ubtechinc.alpha.im;

import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha.AlphaMessageOuterClass;
import com.ubtechinc.nets.im.modules.IMJsonMsg;
import com.ubtechinc.nets.im.service.RobotPhoneCommuniteProxy;
import com.ubtechinc.nets.phonerobotcommunite.ProtoBufferDispose;
import com.ubtechinc.nets.utils.JsonUtil;

/**
 * Created by Administrator on 2017/5/24.
 */

public class IMsgHandleEngine {
    private static final String TAG = "IMsgHandleEngine";
    private static IMsgHandleEngine sInstance;

    private ImMsgDispathcer msgDispathcer;

    public static IMsgHandleEngine getInstance() {
        if (sInstance == null) {
            synchronized (ImMsgDispathcer.class) {
                if (sInstance == null) {
                    sInstance = new IMsgHandleEngine();
                }
            }
        }
        return sInstance;
    }
    public void setIMsgDispatcher(ImMsgDispathcer msgDispathcer) {
        this.msgDispathcer = msgDispathcer;
    }



    public void handleProtoBuffMsg(byte[] msgBytes, String peer) {

        AlphaMessageOuterClass.AlphaMessage msgRequest = (AlphaMessageOuterClass.AlphaMessage)ProtoBufferDispose.unPackData(AlphaMessageOuterClass.AlphaMessage.class,msgBytes);
        int cmdId = msgRequest.getHeader().getCommandId();
        long responseId = msgRequest.getHeader().getResponseSerial();
        long requestId = msgRequest.getHeader().getSendSerial();
        LogUtils.d("handleProtoBuffMsg--cmdId = "+cmdId+", requestId = "+requestId+", responseId = "+responseId);
        if (responseId > 0) { //说明该消息是一个Response Msg,这种不由ImMsgDispathcer处理，把Resonse抛给调用方
            RobotPhoneCommuniteProxy.getInstance().dispatchResponse(responseId,msgRequest);
        } else {
            msgDispathcer.dispatchMsg(cmdId, msgRequest,peer);
        }
    }

    public void handleJsonMsg(String jasonStr,String peer) {
        IMJsonMsg jsonRequest = JsonUtil.getObject(jasonStr,IMJsonMsg.class);
        int cmdId = jsonRequest.header.commandId;
        msgDispathcer.dispatchMsg(cmdId,jsonRequest,peer);

    }
}
