package com.ubtechinc.alpha.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha.model.upgrade.UpdateConstant;

public class BroadcastUtils {
    private static final String TAG = "BroadcastUtils";

    public static void sendBroadcast2SystemUpdate(Context mContext, int cmd, String parameter) {
        Intent intent = new Intent();
        intent.setAction(UpdateConstant.UPDATE_ACTION_SERVANT);
        intent.putExtra(UpdateConstant.GET_COMMAND, cmd);
        intent.putExtra(UpdateConstant.GET_PARAMETER, parameter);
        if(!TextUtils.isEmpty(parameter)) {
            LogUtils.i( "parameter=" + parameter);
        }
        mContext.sendBroadcast(intent);
    }
}
