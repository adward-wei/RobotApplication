package com.ubtechinc.nets.phonerobotcommunite;

import com.ubtechinc.alpha.im.ImMsgDispathcer;

/**
 * Created by Administrator on 2017/5/25.
 */

public interface IReceiveMsg {
    void init();
    void setIMsgDispatcher(ImMsgDispathcer msgDispathcer);
}
