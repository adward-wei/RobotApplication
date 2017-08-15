package com.ubtechinc.alpha2ctrlapp.ui.activity.robot;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.StringUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseActivity;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.robot.OpenNetLitenerFragment;

import java.util.Timer;

/**
 * @ClassName ConfigureRobotNetworkActivity
 * @date 6/12/2017
 * @author tanghongyu
 * @Description 机器人配网
 * @modifier
 * @modify_time
 */
public class ConfigureRobotNetworkActivity extends BaseActivity {
    protected Context mContext;
    private String TAG="ConfigureRobotNetworkActivity";
    public static Timer timeCheck = null;
    public static String robotSn="";
    public static String  robotMac;
    public static  boolean isFromDevice= false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_common_fragment);
        if(savedInstanceState == null){
            addFragment(OpenNetLitenerFragment.class.getName());
        }
        mContext = this;
        isFromDevice = getIntent().getBooleanExtra("isFromDevice", false);
        robotSn = getIntent().getStringExtra(Constants.ROBOTSN);
        robotMac = getIntent().getStringExtra(Constants.ROBOT_MAC);

        if (!StringUtils.isEmpty(robotMac)) {

            robotMac = robotMac.toUpperCase();
        }
        btn_ignore.setVisibility(View.VISIBLE);
        btn_ignore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//				Constants.NONE_CONNECTED_ROBOT = true;
                if(isFromDevice){
                    ConfigureRobotNetworkActivity.this.finish();
                }else{
                    Intent intent = new Intent(ConfigureRobotNetworkActivity.this, MyDeviceActivity.class);
                    startActivity(intent);
                    ((Alpha2Application)getApplication()).removeActivity();
                }

            }
        });

        //用了太多全局变量，不敢改，只能在这里跳转过去 T_T
        Logger.d(TAG, "robot sn = " + robotSn);
        Intent intent;
        if(robotSn.toUpperCase().contains("UBT")) {//2017年3月之后产的机器(序列号包含UBT)才去支持蓝牙配网
            intent = new Intent(this, BLEConfigureActivity.class);
        } else {
            intent = new Intent(this, NetworkConfigureActivity.class);
        }
        intent.putExtra("robot_sn", robotSn);
        startActivity(intent);
        finish();
    }
    @Override
    public void onBack(View v){
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        }else{
            if(isFromDevice){
                this.finish();
            }else{
                Intent intent = new Intent(this, MyDeviceActivity.class);
                startActivity(intent);
                ((Alpha2Application)getApplication()).removeActivity();
            }

        }
        Logger.i("back", "ConfigureRobotNetworkActivity B");
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        if(timeCheck!=null)
            timeCheck.cancel();
        timeCheck = null;
        robotSn="";
        isFromDevice = false;
    }
}
