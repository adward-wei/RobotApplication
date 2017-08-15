package com.ubtechinc.alpha2ctrlapp.im;

import com.ubtechinc.alpha.im.IMCmdId;
import com.ubtechinc.alpha.im.ImMsgDispathcer;
import com.ubtechinc.alpha2ctrlapp.im.msghandler.ActionDownloadStatusMsgHandler;
import com.ubtechinc.alpha2ctrlapp.im.msghandler.AppDownloadStatusMsgHandler;
import com.ubtechinc.alpha2ctrlapp.im.msghandler.RobotLoginLogoutMsgHandler;
import com.ubtechinc.nets.im.annotation.IMJsonMsgRelationVector;
import com.ubtechinc.nets.im.annotation.IMMsgRelationVector;
import com.ubtechinc.nets.im.annotation.ImJsonMsgRelation;
import com.ubtechinc.nets.im.annotation.ImMsgRelation;

/**
 * Created by Administrator on 2017/5/26.
 */
@IMJsonMsgRelationVector({@ImJsonMsgRelation(requestCmdId= IMCmdId.IM_OFFLINE_FROM_SERVER_RESPONSE,msgHandleClass=RobotLoginLogoutMsgHandler.class)})
@IMMsgRelationVector({@ImMsgRelation(requestCmdId = IMCmdId.IM_APP_INSTALL_STATE_RESPONSE, msgHandleClass=AppDownloadStatusMsgHandler.class),
        @ImMsgRelation(requestCmdId = IMCmdId.IM_ACTIONFILE_DOWNLOAD_STATE_RESPONSE, msgHandleClass=ActionDownloadStatusMsgHandler.class)

})
public class ImPhoneClientMsgDispatcher extends ImMsgDispathcer{
}

