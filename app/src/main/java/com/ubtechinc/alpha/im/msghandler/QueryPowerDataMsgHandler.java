package com.ubtechinc.alpha.im.msghandler;

import com.ubtechinc.alpha.AlphaMessageOuterClass;
import com.ubtechinc.alpha.CmQueryPowerData;
import com.ubtechinc.alpha.robotinfo.RobotState;
import com.ubtechinc.nets.im.service.RobotPhoneCommuniteProxy;

/**
 * Created by Administrator on 2017/6/6 0006.
 */

public class QueryPowerDataMsgHandler implements IMsgHandler{
    private static final String TAG="QueryPowerDataMsgHandler";
    @Override
    public void handleMsg(int requestCmdId, int responseCmdId, AlphaMessageOuterClass.AlphaMessage request, String peer) {
           final long requestSerial = request.getHeader().getSendSerial();
            int power  =RobotState.get().getPowerValue();
            CmQueryPowerData.CmQueryPowerDataResponse dataResponse =CmQueryPowerData.CmQueryPowerDataResponse.newBuilder().setPowerValue(power).build();
            RobotPhoneCommuniteProxy.getInstance().sendResponseMessage(responseCmdId,"1",requestSerial,dataResponse,peer,null);

    }
}
