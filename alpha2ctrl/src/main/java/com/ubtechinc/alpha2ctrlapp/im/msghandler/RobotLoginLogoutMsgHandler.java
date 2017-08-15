package com.ubtechinc.alpha2ctrlapp.im.msghandler;

import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.StringUtils;
import com.ubtechinc.alpha.im.msghandler.IMJsonMsgHandler;
import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.events.RobotIMStateChangeEvent;
import com.ubtechinc.alpha2ctrlapp.im.imjsonmsg.IMJsonLoginStateMsg;
import com.ubtechinc.nets.im.modules.IMJsonMsg;
import com.ubtechinc.nets.utils.JsonUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2017/6/6.
 */

public class RobotLoginLogoutMsgHandler implements IMJsonMsgHandler {
    private String TAG = "RobotLoginLogoutMsgHandler";

    @Override
    public void handleMsg(int requestCmdId, int responseCmdId, IMJsonMsg jsonRequest, String peer) {
        String bodyData = jsonRequest.bodyData;
        IMJsonLoginStateMsg loginStateMsg = JsonUtil.getObject(bodyData,IMJsonLoginStateMsg.class);
        RobotIMStateChangeEvent event = null ;
        if(StringUtils.isEquals(loginStateMsg.state, BusinessConstants.ROBOT_IM_STATE_CHANGE_LOGOUT)) {
            event= new RobotIMStateChangeEvent(loginStateMsg.userId, BusinessConstants.ROBOT_STATE_OFFLINE);
        }else if(StringUtils.isEquals(loginStateMsg.state, BusinessConstants.ROBOT_IM_STATE_CHANGE_LOGIN)) {
            event= new RobotIMStateChangeEvent(loginStateMsg.userId, BusinessConstants.ROBOT_STATE_ONLINE);
        }
        if(event != null) EventBus.getDefault().post(event);
        Logger.t(TAG).d("LoginoutState ------ userId = %s , state = %s",loginStateMsg.userId,loginStateMsg.state);

    }
}
