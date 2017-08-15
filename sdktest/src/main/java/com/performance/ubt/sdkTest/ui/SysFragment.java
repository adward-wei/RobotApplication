package com.performance.ubt.sdkTest.ui;

import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.performance.ubt.sdkTest.R;
import com.ubtechinc.alpha.sdk.sys.SysApi;
import com.ubtechinc.alpha.serverlibutil.aidl.AlarmInfo;

public class SysFragment extends BaseFragement implements View.OnClickListener {

    private static final String TAG = "SysTest";

    @Override
    protected void initView() {
        //初始化view
        initButton();
        SysApi.get().initializ(mContext);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_sys;
    }

    @Override
    protected void getDataFromServer() {
    }

    /**
     * 初始化button
     */
    private void initButton() {
        Button getSidButton = (Button) mView.findViewById(R.id.getSid);
        getSidButton.setOnClickListener(this);
        Button getMICVersionButton = (Button) mView.findViewById(R.id.getMICVersion);
        getMICVersionButton.setOnClickListener(this);
        Button insertAlarmButton = (Button) mView.findViewById(R.id.insertAlarm);
        insertAlarmButton.setOnClickListener(this);
        Button queryAllAlarmButton = (Button) mView.findViewById(R.id.queryAllAlarm);
        queryAllAlarmButton.setOnClickListener(this);
        Button startAppButton = (Button) mView.findViewById(R.id.startApp);
        startAppButton.setOnClickListener(this);
        Button enterUpgradeModeButton = (Button) mView.findViewById(R.id.enterUpgradeMode);
        enterUpgradeModeButton.setOnClickListener(this);
        Button exitUpgradeModeButton = (Button) mView.findViewById(R.id.exitUpgradeMode);
        exitUpgradeModeButton.setOnClickListener(this);
        Button getChestVersionButton = (Button) mView.findViewById(R.id.getChestVersion);
        getChestVersionButton.setOnClickListener(this);
        Button getHeadVersionButton = (Button) mView.findViewById(R.id.getHeadVersion);
        getHeadVersionButton.setOnClickListener(this);
        Button getBatteryVersionButton = (Button) mView.findViewById(R.id.getBatteryVersion);
        getBatteryVersionButton.setOnClickListener(this);
        Button isPowerChargingButton = (Button) mView.findViewById(R.id.isPowerCharging);
        isPowerChargingButton.setOnClickListener(this);
        Button getPowerValueButton = (Button) mView.findViewById(R.id.getPowerValue);
        getPowerValueButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.getSid:
                getSid();
                break;
            case R.id.getMICVersion:
                getMICVersion();
                break;
            case R.id.insertAlarm:
                insertAlarm();
                break;
            case R.id.queryAllAlarm:
                queryAllAlarm();
                break;
            case R.id.startApp:
                startApp();
                break;
            case R.id.enterUpgradeMode:
                enterUpgradeMode();
                break;
            case R.id.exitUpgradeMode:
                exitUpgradeMode();
                break;
            case R.id.getChestVersion:
                getChestVersion();
                break;
            case R.id.getHeadVersion:
                getHeadVersion();
                break;
            case R.id.getBatteryVersion:
                getBatteryVersion();
                break;
            case R.id.isPowerCharging:
                isPowerCharging();
                break;
            case R.id.getPowerValue:
                getPowerValue();
                break;
            default:
                break;
        }

    }

    private void getSid() {
        String sid = SysApi.get().getSid();
        Log.i(TAG, "getSid return " + sid);
        showToast("Sid:" + sid);
    }

    private void getMICVersion() {
        String micVersion = SysApi.get().getMICVersion();
        Log.i(TAG, "getMICVersion return " + micVersion);
        showToast("MICVersion:" + micVersion);
    }

    //TODO
    private void insertAlarm() {
        AlarmInfo alarmInfo = new AlarmInfo();
        alarmInfo.state = 0;
        alarmInfo.repeat = 1;
        alarmInfo.hh = 17;
        alarmInfo.mm = 00;
        alarmInfo.vibrate = true;
        alarmInfo.label = "sdk接口测试专用";
        alarmInfo.iscomplete = false;
        Uri uri = Uri.parse("");
        alarmInfo.alert = uri;
        SysApi.get().insertAlarm(alarmInfo);
    }

    private void queryAllAlarm() {
        AlarmInfo[] alarmInfos = SysApi.get().queryAllAlarm("");
        Log.i(TAG, "Alarm个数:" + alarmInfos.length);
        for (AlarmInfo alarm : alarmInfos) {
            Log.i(TAG, "alarm.id:" + alarm.id);
            Log.i(TAG, "alarm.state:" + alarm.state);
            Log.i(TAG, "alarm.hh:" + alarm.hh);
            Log.i(TAG, "alarm.mm:" + alarm.mm);
            Log.i(TAG, "alarm.repeat:" + alarm.repeat);
            Log.i(TAG, "alarm.isUseAble:" + alarm.isUseAble);
            Log.i(TAG, "alarm.actionStartName:" + alarm.actionStartName);
            Log.i(TAG, "alarm.actionEndName:" + alarm.acitonEndName);
            Log.i(TAG, "alarm.actionType:" + alarm.actionType);
            Log.i(TAG, "alarm.yy:" + alarm.yy);
            Log.i(TAG, "alarm.mo:" + alarm.mo);
            Log.i(TAG, "alarm.day:" + alarm.day);
            Log.i(TAG, "alarm.date:" + alarm.date);
            Log.i(TAG, "alarm.ss:" + alarm.ss);
            Log.i(TAG, "alarm.vibrate:" + alarm.vibrate);
            Log.i(TAG, "alarm.label:" + alarm.label);
            Log.i(TAG, "alarm.alert:" + alarm.alert.toString());
            Log.i(TAG, "alarm.silent:" + alarm.silent);
            Log.i(TAG, "alarm.dtstart:" + alarm.dtstart);
            Log.i(TAG, "alarm.iscomplete:" + alarm.iscomplete);
            Log.i(TAG, "alarm.dttime:" + alarm.dttime);
        }
    }

    private void startApp() {
        Uri uri = Uri.parse("content://" + "com.yfz.Lesson" + "/people");
        SysApi.get().startApp(uri);
    }

    private void enterUpgradeMode() {
        SysApi.get().enterUpgradeMode();
    }

    private void exitUpgradeMode() {
        SysApi.get().exitUpgradeMode();
    }

    private void getChestVersion() {
        String chestVersion = SysApi.get().getChestVersion();
        Log.i(TAG, "getChestVersion return " + chestVersion);
        showToast("getChestVersion:" + chestVersion);
    }

    private void getHeadVersion() {
        String headVersion = SysApi.get().getHeadVersion();
        Log.i(TAG, "getHeadVersion return " + headVersion);
        showToast("getHeadVersion:" + headVersion);
    }

    private void getBatteryVersion() {
        String batteryVersion = SysApi.get().getBatteryVersion();
        Log.i(TAG, "getBatteryVersion return " + batteryVersion);
        showToast("getBatteryVersion:" + batteryVersion);
    }

    private void isPowerCharging() {
        boolean isPowerCharging = SysApi.get().isPowerCharging();
        Log.i(TAG, "isPowerCharging return " + isPowerCharging);
        showToast("isPowerCharging:" + isPowerCharging);
    }

    private void getPowerValue() {
        int powerValue = SysApi.get().getPowerValue();
        Log.i(TAG, "getPowerValue return " + powerValue);
        showToast("getPowerValue:" + powerValue);
    }


    private void showToast(String message) {
        Toast toast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
