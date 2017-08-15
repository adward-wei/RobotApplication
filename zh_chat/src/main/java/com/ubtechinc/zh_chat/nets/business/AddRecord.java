package com.ubtechinc.zh_chat.nets.business;

import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.nets.ResponseListener;
import com.ubtechinc.nets.http.HttpProxy;
import com.ubtechinc.nets.http.ThrowableWrapper;
import com.ubtechinc.zh_chat.nets.module.AddRecordModule;

/**
 * @desc :
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/6/20
 * @modifier:
 * @modify_time:
 */

public final class AddRecord {
    private static AddRecord sInstance;
    public static AddRecord instance() {
        if (sInstance == null) {
            synchronized (AddRecord.class) {
                if (sInstance == null) {
                    sInstance = new AddRecord();
                }
            }
        }
        return sInstance;
    }

    private AddRecord() {
    }

    public void requestAddRecord(int labelId, String pushContent, String pushLinks,
                                 String robotMsg, String userMsg) {
        AddRecordModule.Request request=new AddRecordModule.Request();
        request.setLabelId(labelId);
        request.setPushContent(pushContent);
        request.setPushLinks(pushLinks);
        request.setRobotMsg(robotMsg);
        request.setUserMsg(userMsg);
        HttpProxy.get().doPost(request, new ResponseListener<AddRecordModule.Response>() {
            @Override
            public void onError(ThrowableWrapper e) {
                LogUtils.W("add record error = %s", e.getMessage());
            }

            @Override
            public void onSuccess(AddRecordModule.Response response) {
                LogUtils.D("add record result= %s", response.success);
            }
        });
    }
}
