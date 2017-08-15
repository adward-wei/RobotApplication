package com.ubtechinc.alpha2ctrlapp.third;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.orhanobut.logger.Logger;
import com.ubt.alpha2sdk.aidlclient.RobotInfo;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtech.utilcode.utils.SPUtils;
import com.ubtech.utilcode.utils.StringUtils;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.constants.IntentConstants;
import com.ubtechinc.alpha2ctrlapp.constants.PreferenceConstants;
import com.ubtechinc.alpha2ctrlapp.ui.activity.robot.ConfigureRobotNetworkActivity;
import com.ubtechinc.alpha2ctrlapp.ui.activity.user.AuthorizationActivity;


/**
 * @ClassName ThirdSDKReceiver
 * @date 2016/9/21 
 * @author tanghongyu
 * @Description 第三方App监听
 * @modifier
 * @modify_time
 */
public class ThirdSDKReceiver extends BroadcastReceiver {
    private static final String TAG = "ThirdSDKReceiver";
    @Override
    public void onReceive(Context context, Intent intent){

        switch (intent.getAction()) {
            case IntentConstants.ACTION_THIRD_SDK_VERIFY :
                if(Constants.hasLoginOut) {//没有登录情况下，先存下
                    Logger.d(" onReceive not login save preference");
                    SPUtils.get().put(PreferenceConstants.IS_FROM_THRID_AUTH, true);

                    String appId = intent.getStringExtra(AidlService.AIDL_DATA_APPID);
                    String appKey = intent.getStringExtra(AidlService.AIDL_DATA_APPKEY);
                    SPUtils.get().put(PreferenceConstants.THRID_APP_ID, appId);
                    SPUtils.get().put(PreferenceConstants.THRID_APP_KEY, appKey);

                }else {//已经登录则调到验证页面
                    Logger.d("onReceive user is login start AuthorizationActivity");
                    intent.setClass(context, AuthorizationActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    context.startActivity(intent);
                }
                break;
            case IntentConstants.ACTION_THIRD_SDK_CONFIG_NETWORK :
                RobotInfo robot = (RobotInfo) intent.getSerializableExtra(AidlService.AIDL_DATA_ROBOT);
                if(Constants.hasLoginOut) {//没有登录情况下，先存下
                    Logger.d(" onReceive not login save preference");
                    SPUtils.get().put(PreferenceConstants.IS_FROM_THRID_CONFIG_NETWORK, true);
                    SPUtils.get().putObject(robot);


                }else {//已经登录则跳到配网页面
                    Logger.d("onReceive user is login start ConfigureRobotNetworkActivity");
                    intent.setClass(context, ConfigureRobotNetworkActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);


                    intent.putExtra("isFromDevice", true);
                    intent.putExtra(Constants.ROBOTSN, robot.getEquipmentId());
                    if( !StringUtils.isEmpty(robot.getMacAddress())){
                        intent.putExtra(Constants.ROBOT_MAC,robot.getMacAddress());
                    }
                    context.startActivity(intent);

                }

                break;
            case IntentConstants.ACTION_THIRD_SDK_DOWNLOAD_APP :

                    Logger.d(" onReceive not login save preference");
                SPUtils.get().put(PreferenceConstants.IS_FROM_THRID_DOWNLOAD_APP, true);
                    String appId = intent.getStringExtra(AidlService.AIDL_DATA_APPID);
                SPUtils.get().put(PreferenceConstants.THRID_APP_ID, appId);


                break;
        }


     
    }
}
