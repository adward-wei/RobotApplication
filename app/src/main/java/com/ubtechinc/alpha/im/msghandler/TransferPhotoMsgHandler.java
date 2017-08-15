package com.ubtechinc.alpha.im.msghandler;

import com.google.protobuf.ByteString;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha.AlphaMessageOuterClass;
import com.ubtechinc.alpha.CmTransferPhotoInfo;
import com.ubtechinc.alpha.app.AlphaApplication;
import com.ubtechinc.alpha.entity.TransferPhotoInfo;
import com.ubtechinc.alpha.entity.UploadFileUrl;
import com.ubtechinc.alpha.im.IMHeaderField;
import com.ubtechinc.alpha.im.IMsgResponseCallback;
import com.ubtechinc.alpha.upload.photo.PhotoUploadInHttpImpl;
import com.ubtechinc.alpha.upload.photo.TransferPhotoConstants;
import com.ubtechinc.alpha.upload.photo.TransferPhotoPresenterImpl;
import com.ubtechinc.nets.im.service.RobotPhoneCommuniteProxy;
import com.ubtechinc.nets.phonerobotcommunite.ProtoBufferDispose;

/**
 * @desc : 传输照片的消息处理器
 * @author: wzt
 * @time : 2017/6/6
 * @modifier:
 * @modify_time:
 */

public class TransferPhotoMsgHandler implements IMsgHandler, IMsgResponseCallback {
    static final String TAG = "TransferPhotoMsgHandler";

    public static final int CUSTOM_CMD_NORMAL = 0;
    public static final int CUSTOM_CMD_SEND_URL = 1;

    @Override
    public void handleMsg(int requestCmdId, int responseCmdId, AlphaMessageOuterClass.AlphaMessage request, String peer) {

        ByteString requestBody = request.getBodyData();
        byte[] bodyBytes = requestBody.toByteArray();
        long requestSerial = request.getHeader().getSendSerial();
        CmTransferPhotoInfo.CmTransferPhotoInfoRequest transferPhotoInfo = (CmTransferPhotoInfo.CmTransferPhotoInfoRequest) ProtoBufferDispose.unPackData(
                CmTransferPhotoInfo.CmTransferPhotoInfoRequest.class, bodyBytes);
        LogUtils.d("request body : transfer photo type = " + transferPhotoInfo.getType());

        IMHeaderField headerField = new IMHeaderField();
        headerField.requestSerial = requestSerial;
        headerField.responseCmdId = responseCmdId;
        headerField.peer = peer;

        int type = transferPhotoInfo.getType();
        if(type == TransferPhotoConstants.TYPE_GET_THUMBNAIL_PIC_TOTAL) {
            // 查询相册缩略图的总数
            headerField.customCmd = CUSTOM_CMD_NORMAL;
            TransferPhotoPresenterImpl.getInstance()
                    .handleRequestOfGetTotalThumbnail(
                            this,
                            headerField);
        } else if (type == TransferPhotoConstants.TYPE_PATCH_DEL_PICS) {
            // 批量删除相册图片
            headerField.customCmd = CUSTOM_CMD_NORMAL;
            TransferPhotoPresenterImpl.getInstance()
                    .handleRequestOfPatchDelPic(
                            transferPhotoInfo.getDelPicsList(),
                            this,
                            headerField);
        } else if (type == TransferPhotoConstants.TYPE_GET_PICS_BY_THUMBNAIL) {
            // 根据缩略图查找大图
            headerField.customCmd = CUSTOM_CMD_SEND_URL;
            PhotoUploadInHttpImpl.getInstance(AlphaApplication.getContext())
                    .handleRequestOfGetPicByThumbnail(
                            transferPhotoInfo.getPath(),
                            this,
                            headerField);

        } else if (type == TransferPhotoConstants.TYPE_SEND_ALL_THUMB_PICS) {
             // 发送所有缩略图到手机端
            headerField.customCmd = CUSTOM_CMD_SEND_URL;
            PhotoUploadInHttpImpl.getInstance(AlphaApplication.getContext())
                    .handleRequestOfSendAllThumbPics(
                            transferPhotoInfo.getDelPicsList(),
                            transferPhotoInfo.getPath(),
                            this,
                            headerField);

        }
    }

    @Override
    public void onResult(IMHeaderField headerField, Object obj) {
        if(headerField.customCmd == CUSTOM_CMD_NORMAL) {
            TransferPhotoInfo transferPhotoInfo = (TransferPhotoInfo) obj;
            CmTransferPhotoInfo.CmTransferPhotoInfoResponse.Builder builder = CmTransferPhotoInfo.CmTransferPhotoInfoResponse.newBuilder();
            builder.setType(transferPhotoInfo.getType());
            builder.setAmount(transferPhotoInfo.getAmount());
            builder.addAllDelPics(transferPhotoInfo.getDelPics());

            CmTransferPhotoInfo.CmTransferPhotoInfoResponse responseBody = builder.build();
            RobotPhoneCommuniteProxy.getInstance().sendResponseMessage(headerField.responseCmdId,
                    "1",
                    headerField.requestSerial,
                    responseBody,
                    headerField.peer,null);
        } else if(headerField.customCmd == CUSTOM_CMD_SEND_URL) {
            UploadFileUrl uploadFileUrl = (UploadFileUrl) obj;
            com.ubtechinc.alpha.UploadFileUrl.UploadFileUrlResponse.Builder builder =  com.ubtechinc.alpha.UploadFileUrl.UploadFileUrlResponse.newBuilder();
            builder.setFileUrl(uploadFileUrl.getFileUrl());

            com.ubtechinc.alpha.UploadFileUrl.UploadFileUrlResponse responseBody = builder.build();
            RobotPhoneCommuniteProxy.getInstance().sendResponseMessage(headerField.responseCmdId,
                    "1",
                    headerField.requestSerial,
                    responseBody,
                    headerField.peer,null);
        }

    }
}
