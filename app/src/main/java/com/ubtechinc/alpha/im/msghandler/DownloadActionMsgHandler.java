package com.ubtechinc.alpha.im.msghandler;

import com.google.protobuf.ByteString;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha.AlphaMessageOuterClass;
import com.ubtechinc.alpha.CmActionDownloadState;
import com.ubtechinc.alpha.CmDownloadActionFile;
import com.ubtechinc.alpha.app.AlphaApplication;
import com.ubtechinc.alpha.download.ActionFileEntrity;
import com.ubtechinc.alpha.download.BusinessConstant;
import com.ubtechinc.alpha.download.FileDownload;
import com.ubtechinc.alpha.im.IMCmdId;
import com.ubtechinc.alpha.im.IMHeaderField;
import com.ubtechinc.alpha.im.IMsgResponseCallback;
import com.ubtechinc.alpha.im.Robot2PhoneMsgMgr;
import com.ubtechinc.alpha.utils.ActionNameUtil;
import com.ubtechinc.alpha.utils.StorageUtil;
import com.ubtechinc.nets.im.service.RobotPhoneCommuniteProxy;
import com.ubtechinc.nets.phonerobotcommunite.ProtoBufferDispose;


/**
 * @desc : 下载动作文件的消息处理器
 * @author: wzt
 * @time : 2017/6/5
 * @modifier:
 * @modify_time:
 */

public class DownloadActionMsgHandler implements IMsgHandler, IMsgResponseCallback {
    static final String TAG = "DownloadActionMsgHandler";
    @Override
    public void handleMsg(int requestCmdId, int responseCmdId, AlphaMessageOuterClass.AlphaMessage request, String peer) {

        ByteString requestBody = request.getBodyData();
        byte[] bodyBytes = requestBody.toByteArray();
        long requestSerial = request.getHeader().getSendSerial();
        CmDownloadActionFile.CmDownloadActionFileRequest actionFileRequest = ( CmDownloadActionFile.CmDownloadActionFileRequest) ProtoBufferDispose.unPackData(
                CmDownloadActionFile.CmDownloadActionFileRequest.class, bodyBytes);
        LogUtils.i( "request body : download action name = " + actionFileRequest.getActionName()+" ActionOriginalID="+actionFileRequest.getActionOriginalId());
        CmDownloadActionFile.CmDownloadActionFileResponse.Builder builder = CmDownloadActionFile.CmDownloadActionFileResponse.newBuilder();
        if (!StorageUtil.isAvailableExternalMemoryGreaterThan100M()) {



            builder.setResultCode(BusinessConstant.ERROR_CODE_MEMORY_INSUFFICIENT);
            builder.setIsSuccess(false);
            RobotPhoneCommuniteProxy.getInstance().sendResponseMessage(responseCmdId, "1", requestSerial, builder.build(), peer,null);
        } else {

            ActionFileEntrity action = new ActionFileEntrity();
            action.setActionId(actionFileRequest.getActionId());
            action.setActionName(actionFileRequest.getActionName());
            action.setActionType(actionFileRequest.getActionType());
            action.setActionFilePath(actionFileRequest.getActionFilePath());
            action.setActionOriginalId(actionFileRequest.getActionOriginalId());

            IMHeaderField headerField = new IMHeaderField();
            headerField.requestSerial = requestSerial;
            headerField.responseCmdId = responseCmdId;
            headerField.peer = peer;

            FileDownload downLoad = new FileDownload(AlphaApplication.getContext(), FileDownload.DOWNLOAD_TYPE_ACTION_FILE,
                    headerField, this);
            downLoad.download(action);
            ActionNameUtil.insertActionInfo(actionFileRequest.getActionOriginalId());
            builder.setIsSuccess(true);
            RobotPhoneCommuniteProxy.getInstance().sendResponseMessage(responseCmdId, "1", requestSerial, builder.build(), peer,null);

        }
    }

    @Override
    public void onResult(IMHeaderField headerField, Object obj) {
        ActionFileEntrity entrity = (ActionFileEntrity) obj;

        CmActionDownloadState.CmActionDownloadStateResponse.Builder builder = CmActionDownloadState.CmActionDownloadStateResponse.newBuilder();
        builder.setActionId(entrity.getActionId());
        builder.setActionName(entrity.getActionName());
        builder.setActionType(entrity.getActionType());
        builder.setActionOriginalId(entrity.getActionOriginalId());
        builder.setDownloadState(entrity.getDownloadState());
        LogUtils.d(TAG,  "ActionFileDownload callback = " +  entrity.toString());

        Robot2PhoneMsgMgr.getInstance().sendData(IMCmdId.IM_ACTIONFILE_DOWNLOAD_STATE_RESPONSE,
                "1",
                builder.build(),
                headerField.peer, null);
    }
}
