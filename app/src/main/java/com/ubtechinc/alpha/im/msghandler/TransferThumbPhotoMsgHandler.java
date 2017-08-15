package com.ubtechinc.alpha.im.msghandler;

import com.google.protobuf.ByteString;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha.AlphaMessageOuterClass;
import com.ubtechinc.alpha.CmTransferPhotoInfo;
import com.ubtechinc.alpha.app.AlphaApplication;
import com.ubtechinc.alpha.entity.UploadFileUrl;
import com.ubtechinc.alpha.im.IMHeaderField;
import com.ubtechinc.alpha.im.IMsgResponseCallback;
import com.ubtechinc.alpha.upload.photo.PhotoUploadInHttpImpl;
import com.ubtechinc.nets.im.service.RobotPhoneCommuniteProxy;
import com.ubtechinc.nets.phonerobotcommunite.ProtoBufferDispose;

/**
 * @desc : 传输所有缩略图的消息处理器
 * @author: wzt
 * @time : 2017/6/7
 * @modifier:
 * @modify_time:
 */

public class TransferThumbPhotoMsgHandler implements IMsgHandler, IMsgResponseCallback {
    static final String TAG = "TransferThumbPhotoMsgHandler";

    public static final int CUSTOM_CMD_NORMAL = 0;
    public static final int CUSTOM_CMD_SEND_URL = 1;

    @Override
    public void handleMsg(int requestCmdId, int responseCmdId, AlphaMessageOuterClass.AlphaMessage request, String peer) {

        ByteString requestBody = request.getBodyData();
        byte[] bodyBytes = requestBody.toByteArray();
        long requestSerial = request.getHeader().getSendSerial();
        CmTransferPhotoInfo.CmTransferPhotoInfoRequest transferPhotoInfo = (CmTransferPhotoInfo.CmTransferPhotoInfoRequest) ProtoBufferDispose.unPackData(
                CmTransferPhotoInfo.CmTransferPhotoInfoRequest.class, bodyBytes);
        LogUtils.d("request body : transfer thumb photo type = " + transferPhotoInfo.getType());

        IMHeaderField headerField = new IMHeaderField();
        headerField.requestSerial = requestSerial;
        headerField.responseCmdId = responseCmdId;
        headerField.peer = peer;
        headerField.customCmd = CUSTOM_CMD_SEND_URL;

        PhotoUploadInHttpImpl.getInstance(AlphaApplication.getContext())
                .handleRequestOfSendAllThumbPics(
                        transferPhotoInfo.getDelPicsList(),
                        transferPhotoInfo.getPath(),
                        this,
                        headerField);
    }

    @Override
    public void onResult(IMHeaderField headerField, Object obj) {
        if(headerField.customCmd == CUSTOM_CMD_SEND_URL) {
            UploadFileUrl uploadFileUrl = (UploadFileUrl) obj;
            com.ubtechinc.alpha.UploadFileUrl.UploadFileUrlResponse.Builder builder =  com.ubtechinc.alpha.UploadFileUrl.UploadFileUrlResponse.newBuilder();
            builder.setFileUrl(uploadFileUrl.getFileUrl());

            com.ubtechinc.alpha.UploadFileUrl.UploadFileUrlResponse responseBody = builder.build();
            RobotPhoneCommuniteProxy.getInstance().sendResponseMessage(headerField.responseCmdId,
                    "1",
                    headerField.requestSerial,
                    responseBody,
                    headerField.peer, null);
        }

    }
}
