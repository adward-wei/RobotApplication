package com.ubtechinc.alpha.serverlibutil.aidl;
import com.ubtechinc.alpha.serverlibutil.aidl.AlarmInfo;

interface IAlarmListResultListener {
    void onQueryAlarmList(in AlarmInfo[] onAlarmInfo);
}
