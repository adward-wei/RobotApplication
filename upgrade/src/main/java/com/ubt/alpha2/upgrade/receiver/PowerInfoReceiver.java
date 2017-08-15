package com.ubt.alpha2.upgrade.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.text.TextUtils;

import com.tencent.openqq.protocol.imsdk.msg;
import com.ubt.alpha2.upgrade.utils.LogUtils;

public class PowerInfoReceiver extends BroadcastReceiver {
    public PowerInfoReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtils.d("intent: "+intent);
        String action = intent.getAction();
        LogUtils.d("action: "+action);
        if(TextUtils.isEmpty(action))
            return;
        if(action.equals(Intent.ACTION_POWER_CONNECTED)
                || action.equals(Intent.ACTION_POWER_DISCONNECTED)) {
            int batteryStatus = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharging = batteryStatus == BatteryManager.BATTERY_STATUS_CHARGING;

            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

            float batteryPct = level / (float) scale;
            LogUtils.d("isCharging: " + isCharging + "  batteryPct: " + batteryPct);
        }

        if(action.equals(Intent.ACTION_BATTERY_CHANGED)){
            int batteryStatus = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharging = batteryStatus == BatteryManager.BATTERY_STATUS_CHARGING;

            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

            float batteryPct = level / (float) scale;
            LogUtils.d("isCharging: " + isCharging + "  batteryPct: " + batteryPct);
        }
    }
}
