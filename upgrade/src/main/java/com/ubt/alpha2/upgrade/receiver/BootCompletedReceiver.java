package com.ubt.alpha2.upgrade.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.SyncStateContract;

import com.ubt.alpha2.upgrade.utils.Constants;

/**
 * @author: slive
 * @description: this class used to receive BootCompletedReceiver and starts UpgradeService
 * @create: 2017/6/27
 * @email: slive.shu@ubtrobot.com
 * @modified: slive
 */
public class BootCompletedReceiver extends BroadcastReceiver {

    public BootCompletedReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent();
        intent.setAction(Constants.UPGRADE_ACTION);
        intent.setPackage(context.getPackageName());
        context.startService(serviceIntent);
    }
}
