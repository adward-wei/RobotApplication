package com.ubtechinc.alpha.im.msghandler;

import com.ubtechinc.alpha.AlStopPlayAction;
import com.ubtechinc.alpha.AlphaMessageOuterClass;
import com.ubtechinc.alpha.app.AlphaApplication;
import com.ubtechinc.alpha.ops.RobotOpsManager;
import com.ubtechinc.alpha.sdk.motion.MotionRobotApi;
import com.ubtechinc.alpha.serverlibutil.interfaces.StopActonResultListener;
import com.ubtechinc.alpha.sdk.SdkConstants;
import com.ubtechinc.nets.im.service.RobotPhoneCommuniteProxy;

/**
 * @desc : 停止动作执行的消息处理器
 * @author: wzt
 * @time : 2017/6/1
 * @modifier:
 * @modify_time:
 */


public class StopActionMsgHandler  implements IMsgHandler, StopActonResultListener {
    static final String TAG = "StopActionMsgHandler";
    private int responseCmdId;
    private long requestSerial;
    private String peer;
    @Override
    public void handleMsg(int requestCmdId, int responseCmdId, AlphaMessageOuterClass.AlphaMessage request, String peer) {
        long requestSerial = request.getHeader().getSendSerial();
        this.responseCmdId = responseCmdId;
        this.requestSerial = requestSerial;
        this.peer = peer;
        int retCode = MotionRobotApi.get().stopAction(this);
        if(retCode != SdkConstants.ErrorCode.RESULT_SUCCESS) {
            AlStopPlayAction.AlStopPlayActionResponse.Builder builder = AlStopPlayAction.AlStopPlayActionResponse.newBuilder();
            builder.setIsSuccess(false);
            builder.setResultCode(retCode);
            RobotPhoneCommuniteProxy.getInstance().sendResponseMessage(responseCmdId,"1",requestSerial, builder.build() ,peer,null);
        }

    }

    @Override
    public void onStopActionResult(int nErr) {
        AlStopPlayAction.AlStopPlayActionResponse.Builder builder = AlStopPlayAction.AlStopPlayActionResponse.newBuilder();

        RobotOpsManager.get(AlphaApplication.getContext()).stopAction(null);

        if(nErr == SdkConstants.ErrorCode.RESULT_SUCCESS) {
            builder.setIsSuccess(true);
        }else {
            builder.setIsSuccess(false);
            builder.setResultCode(nErr);
        }
        RobotPhoneCommuniteProxy.getInstance().sendResponseMessage(responseCmdId,"1",requestSerial, builder.build() ,peer,null);
    }
}
