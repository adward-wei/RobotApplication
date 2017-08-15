package com.ubtechinc.alpha.im.msghandler;

import com.google.protobuf.ByteString;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtech.utilcode.utils.StringUtils;
import com.ubtechinc.alpha.AlphaMessageOuterClass;
import com.ubtechinc.alpha.CmGetActionList;
import com.ubtechinc.alpha.CmNewActionInfo;
import com.ubtechinc.alpha.ops.action.Action;
import com.ubtechinc.alpha.provider.ActionInfoVisitor;
import com.ubtechinc.nets.im.service.RobotPhoneCommuniteProxy;
import com.ubtechinc.nets.phonerobotcommunite.ProtoBufferDispose;

import java.util.List;

/**
 * @desc : 边充边玩设置与查询的消息处理器
 * @author: wzt
 * @time : 2017/6/6
 * @modifier:
 * @modify_time:
 */

public class GetActionlistMsgHandler implements IMsgHandler {
    static final String TAG = "GetActionlistMsgHandler";

    @Override
    public void handleMsg(int requestCmdId, int responseCmdId, AlphaMessageOuterClass.AlphaMessage request, String peer) {

        ByteString requestBody = request.getBodyData();
        byte[] bodyBytes = requestBody.toByteArray();
        long requestSerial = request.getHeader().getSendSerial();
        CmGetActionList.CmGetActionListRequest actionListRequest = (CmGetActionList.CmGetActionListRequest) ProtoBufferDispose.unPackData(
                CmGetActionList.CmGetActionListRequest.class, bodyBytes);
        LogUtils.d("request body : get action list  languageType = " + actionListRequest.getLanguageType());
        CmGetActionList.CmGetActionListResponse.Builder builder = CmGetActionList.CmGetActionListResponse.newBuilder();

        ActionInfoVisitor provider = ActionInfoVisitor.get();
        List<Action> actionList = provider.getAllData();
        for(Action item:actionList) {
            CmNewActionInfo.CmNewActionInfoResponse.Builder actionBuilder = CmNewActionInfo.CmNewActionInfoResponse.newBuilder();
            actionBuilder.setActionId(item.Id);
            actionBuilder.setActionType(Integer.parseInt(item.type));
            if(StringUtils.isEquals("EN", actionListRequest.getLanguageType())) {
                actionBuilder.setActionName(item.en_name == null? "": item.en_name);
            } else if(StringUtils.isEquals("CN", actionListRequest.getLanguageType())
                    || StringUtils.isEquals("HK", actionListRequest.getLanguageType())) {
                actionBuilder.setActionName(item.cn_name == null? "": item.cn_name);
            } else {
                actionBuilder.setActionName(item.en_name == null? "": item.en_name);
            }
            CmNewActionInfo.CmNewActionInfoResponse actionItem = actionBuilder.build();
            LogUtils.d("addActionList actionItem = " + actionItem.getActionId());
            builder.addActionList(actionItem);
        }

        LogUtils.d("response GetActionlist size = " + builder.getActionListCount());
        RobotPhoneCommuniteProxy.getInstance().sendResponseMessage(responseCmdId,
                "1",
                requestSerial,
                builder.build(),
                peer,null);
    }
}
