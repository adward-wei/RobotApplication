package com.ubt.alpha2.upgrade.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

/**
 * 广播工具类
 *
 * @author wangzhengtian
 * @Date 2017-03-14
 */

public class BroadcastUtil {

    public static void sendBroadcast2Service(Context context, int cmd, String parameter) {
        Intent intent = new Intent();
        intent.setAction(Constants.UPDATE_ACTION_MASTER);
        intent.putExtra(Constants.GET_COMMAND, cmd);
        if(!TextUtils.isEmpty(parameter)) {
            intent.putExtra(Constants.GET_PARAMETER, parameter);
        }
        context.sendBroadcast(intent);
    }

    public static void sendTTSContent(Context context, String text) {
        int cmd = Constants.UPDATE_CMD_TTSCONTENT;
        sendBroadcast2Service(context, cmd, text);
    }
}
