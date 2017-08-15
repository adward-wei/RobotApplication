package com.ubtechinc.nets.phonerobotcommunite;

import com.ubtechinc.nets.im.service.RobotPhoneCommuniteProxy;

/**
 * Created by Administrator on 2017/5/25.
 */

public interface ISendMsg {
    void init();
    void sendMsg(final long requestSerialId,String peer, byte[] bodyBytes, final RobotPhoneCommuniteProxy.Callback callback);
}
