
package com.ubtechinc.alpha.deskclock;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha.provider.AlarmInfoVisitor;

import java.util.List;


/**
 * Glue class: connects AlarmAlert IntentReceiver to AlarmAlert activity. Passes
 * through Alarm ID.
 */

public class AlarmReceiver extends BroadcastReceiver {
	private static final String TAG =AlarmReceiver.class.getSimpleName();
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		LogUtils.i(TAG, "action:" + action);
		if (DeskclockManager.ALARM_ALERT_ACTION.equals(action)) {
			final int alarmId = intent.getIntExtra("alarm_id",-1);
			final String content = "闹钟响了，"+ intent.getStringExtra("alarm_label");
			LogUtils.i(TAG, "alarm id:" + alarmId + "  alarm lebel:" + content);
			int id = intent.getIntExtra("alarm_id",-1);
			if(id != -1){
				AlarmInfoVisitor.get().updateById(id);
			}
			if (!isServiceWork(context, AlarmService.class.getName())) {
				Intent serviceIntent = new Intent(context, AlarmService.class);
				serviceIntent.putExtra("alarm_label", content);
				context.startService(serviceIntent);
			}
		}
	}

	public boolean isServiceWork(Context mContext, String serviceName) {
		boolean isWork = false;
		ActivityManager myAM = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);
		if (myList.size() <= 0) {
			return false;
		}
		for (int i = 0; i < myList.size(); i++) {
			String mName = myList.get(i).service.getClassName().toString();
			if (mName.equals(serviceName)) {
				isWork = true;
				break;
			}
		}
		return isWork;
	}
}
