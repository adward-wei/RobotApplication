package com.ubt.alpha2.upgrade.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.ubt.alpha2.upgrade.bean.ReportDataBean;
import com.ubt.alpha2.upgrade.utils.Alpha2Property;
import com.ubt.alpha2.upgrade.utils.Constants;
import com.ubt.alpha2.upgrade.utils.LogUtils;
import com.ubt.alpha2.upgrade.utils.ServiceUtils;

public class AppReplacedReceiver extends BroadcastReceiver {
    public AppReplacedReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String packageName = intent.getDataString();
        LogUtils.e("AppPackageBroadcast:"+packageName+"  action:"+intent.getAction()+"  package:"+context.getPackageName());
        if(!TextUtils.isEmpty(packageName) && packageName.contains(context.getPackageName())){
            if(intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {
                LogUtils.e("接收到自己被覆盖安装的广播，重新启动服务");
                Alpha2Property.set(context, Constants.IS_LYNX_INSTALLING, "false");
                ServiceUtils.startService(context);
            }else if(intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)){
                LogUtils.e("接收到自己被移除的广播，重新启动服务");
                ReportDataBean.getInstance().setUpgradeOk(true);
                ReportDataBean.getInstance().report();
                Alpha2Property.set(context, Constants.IS_LYNX_INSTALLING, "false");
                ServiceUtils.startService(context);
            }
        }else {
            LogUtils.e("接收到其他应用被安装的广播，重新启动服务");
        }
    }
}
