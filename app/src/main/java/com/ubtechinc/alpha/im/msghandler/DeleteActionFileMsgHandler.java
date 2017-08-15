package com.ubtechinc.alpha.im.msghandler;

import android.text.TextUtils;

import com.google.protobuf.ByteString;
import com.ubtech.utilcode.utils.FileUtils;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtech.utilcode.utils.StringUtils;
import com.ubtechinc.alpha.AlDeleteActionFile;
import com.ubtechinc.alpha.AlphaMessageOuterClass;
import com.ubtechinc.alpha.download.BusinessConstant;
import com.ubtechinc.alpha.provider.ActionInfoVisitor;
import com.ubtechinc.alpha.utils.Constants;
import com.ubtechinc.nets.im.service.RobotPhoneCommuniteProxy;
import com.ubtechinc.nets.phonerobotcommunite.ProtoBufferDispose;

import java.io.File;

/**
 * @desc : 删除动作文件的消息处理器,可以通过ActionId也可以通过ActionName删除
 * @author: wzt
 * @time : 2017/6/1
 * @modifier:
 * @modify_time:
 */

public class DeleteActionFileMsgHandler implements IMsgHandler {
    static final String TAG = "DeleteActionFileMsgHandler";
    @Override
    public void handleMsg(int requestCmdId, int responseCmdId, AlphaMessageOuterClass.AlphaMessage request, String peer) {

        ByteString requestBody = request.getBodyData();
        byte[] bodyBytes = requestBody.toByteArray();
        long requestSerial = request.getHeader().getSendSerial();
        AlDeleteActionFile.AlDeleteActionFileRequest deleteActionFile  = (AlDeleteActionFile.AlDeleteActionFileRequest) ProtoBufferDispose.unPackData(
                AlDeleteActionFile.AlDeleteActionFileRequest.class, bodyBytes);
        LogUtils.i("request body : fileName = "+deleteActionFile.getFileName());
        String actionID;
        if(StringUtils.isNumeric(deleteActionFile.getFileName())) {
            actionID = deleteActionFile.getFileName();
        }else {
            // 从数据库删除其信息
            ActionInfoVisitor provider =  ActionInfoVisitor.get();
             actionID = provider.getActionId(deleteActionFile.getFileName());
            provider.deleteById(actionID);
        }

        AlDeleteActionFile.AlDeleteActionFileResponse.Builder builder = AlDeleteActionFile.AlDeleteActionFileResponse.newBuilder();
        if(TextUtils.isEmpty(actionID)) {
            builder.setResultCode(BusinessConstant.ERROR_CODE_ACTION_ID_IS_EMPTY);
            builder.setIsSuccess(false);
        }else {
            // 删除其文件
            String file = Constants.ACTION_PATH + File.separator + actionID + ".ubx";

            if(FileUtils.isFileExists(file)) {
                boolean isSuccess =  FileUtils.deleteFile(file);
                builder.setIsSuccess(isSuccess);
                builder.setResultCode(isSuccess ? 0 : BusinessConstant.ERROR_CODE_DELETE_ACTION_FILE_FAIL);
            }else {
                builder.setResultCode(BusinessConstant.ERROR_CODE_ACTION_FILE_NOT_EXIT);
                builder.setIsSuccess(false);
            }
        }

        RobotPhoneCommuniteProxy.getInstance().sendResponseMessage(responseCmdId,"1",requestSerial, builder.build(),peer,null);
    }

}
