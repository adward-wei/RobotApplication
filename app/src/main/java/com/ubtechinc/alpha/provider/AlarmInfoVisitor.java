/*
 *
 *  *
 *  *  *
 *  *  * Copyright (c) 2008-2017 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *  *
 *  *
 *
 */

package com.ubtechinc.alpha.provider;

import android.content.Context;

import com.ubtechinc.alpha.app.AlphaApplication;
import com.ubtechinc.alpha.deskclock.DeskClock;
import com.ubtechinc.alpha.deskclock.DeskclockManager;
import com.ubtechinc.alpha.ops.alarm.AlarmInfo;
import com.ubtechinc.framework.db.EntityManager;
import com.ubtechinc.framework.db.EntityManagerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @desc : 闹钟信息
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/27
 * @modifier:
 * @modify_time:
 */

public class AlarmInfoVisitor extends BaseVisitor<AlarmInfo> {

    public static AlarmInfoVisitor instance;

    public AlarmInfoVisitor(Context context) {
        super(context);
    }

    public static AlarmInfoVisitor get(){
        if(instance ==null ){
            synchronized(AlarmInfoVisitor.class){
                if(instance ==null){
                    instance =new AlarmInfoVisitor(AlphaApplication.getContext());
                }
            }
        }
        return instance;
    }
    @Override
    protected EntityManager<AlarmInfo> entityManagerFactory() {
        dbMananger = EntityManagerFactory.getInstance(context != null? context: AlphaApplication.getContext(),
                EntityManagerHelper.DB_VERSION,
                EntityManagerHelper.DB_ACCOUNT,
                null, null)
                .getEntityManager(AlarmInfo.class, EntityManagerHelper.DB_ALARM_INFO_TABLE);
        return dbMananger;
    }

    @Override
    public void saveOrUpdate(AlarmInfo info) {
        dbMananger.saveOrUpdate(info);
        if(!info.iscomplete){
            DeskclockManager.getDeskclockManager().enableDeskclock(info);
        }
    }

    public void updateById(int id){
        AlarmInfo alarm =dbMananger.findById(id);
        alarm.iscomplete =true;
        dbMananger.saveOrUpdate(alarm);
    }

    public List<DeskClock> getActive(long dttime) {

        List<DeskClock> list = new ArrayList<DeskClock>();
        List<AlarmInfo> alarmInfoList = getAllData();
        for(AlarmInfo item:alarmInfoList) {
            if(item.dttime > dttime || item.date != 0 || item.iscomplete == false) {
                DeskClock bean = new DeskClock();
                bean.setClockID(item.id);
                bean.setHour(item.hh);
                bean.setMinutes(item.mm);
                bean.setEnabled(item.isUseAble);
                bean.setDaysofweek(item.date);
                bean.setVibrate(item.vibrate);
                bean.setMessage(item.label);
                bean.setAlert(item.alert.getPath());
                bean.setDtstart(dtLongToString(item.dtstart));
                list.add(bean);
            }
        }
        return list;
    }

    public List<DeskClock> getHistory() {

        List<DeskClock> list = new ArrayList<DeskClock>();
        List<AlarmInfo> alarmInfoList = getAllData();
        for(AlarmInfo item:alarmInfoList) {
            if( item.iscomplete) {
                DeskClock bean = new DeskClock();
                bean.setClockID(item.id);
                bean.setHour(item.hh);
                bean.setMinutes(item.mm);
                bean.setEnabled(item.isUseAble);
                bean.setDaysofweek(item.date);
                bean.setVibrate(item.vibrate);
                bean.setMessage(item.label);
                bean.setAlert(item.alert.getPath());
                bean.setDtstart(dtLongToString(item.dtstart));
                list.add(bean);
            }
        }
        return list;

    }

    public String dtLongToString(long time) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String dt = df.format(time);
        return dt;
    }

    public EntityManager<AlarmInfo> getEntityManager() {
        return dbMananger;
    }

}
