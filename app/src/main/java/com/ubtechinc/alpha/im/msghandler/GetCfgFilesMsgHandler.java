package com.ubtechinc.alpha.im.msghandler;

import com.ubtechinc.alpha.AlphaMessageOuterClass;
import com.ubtechinc.alpha.CmPlayActionFileOuterClass;
import com.ubtechinc.alpha.CmrGetActionFileList;
import com.ubtechinc.alpha.model.StaticValue;
import com.ubtechinc.alpha.utils.Constants;
import com.ubtechinc.nets.im.service.RobotPhoneCommuniteProxy;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2017/5/29.
 */

public class GetCfgFilesMsgHandler implements IMsgHandler {
    static final String TAG = "GetCfgFilesMsgHandler";
    @Override
    public void handleMsg(int requestCmdId, int responseCmdId, AlphaMessageOuterClass.AlphaMessage request, String peer) {

        long requestSerial = request.getHeader().getSendSerial();
        List<String> fileList = getActionFileList();
        CmrGetActionFileList.CmrGetActionFileListResponse responseBody = null;
        CmrGetActionFileList.CmrGetActionFileListResponse.Builder builder = CmrGetActionFileList.CmrGetActionFileListResponse.newBuilder();
        if (fileList != null && fileList.size() > 0) {
            for (String fileStr : fileList) {
                builder.addActionFileList(CmPlayActionFileOuterClass.CmPlayActionFile.newBuilder().setFilename(fileStr).build());
            }
        }
        responseBody = builder.build();
        RobotPhoneCommuniteProxy.getInstance().sendResponseMessage(responseCmdId,"1",requestSerial,responseBody,peer,null);
    }

    public List<String> getActionFileList() {
        List<String> fileList = new ArrayList<String>();
        File path = new File(Constants.ACTION_PATH);
        File[] files = path.listFiles();
        if (null == files) {
            return null;
        }
        for(int i=0; i<files.length; i++){
            if (files[i].isDirectory())
                continue;

            String fileExt = getExtensionName(files[i].getName());
            if (fileExt.equals("ubx")){
                String strFileName = getFileNameNoEx(files[i].getName());
                fileList.add(strFileName);
            }
        }
        Collections.sort(fileList);
        return fileList;
    }

    private String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    private String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }
}
