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

package com.ubtechinc.alpha.ops.alarm;

import android.net.Uri;

import com.ubtechinc.alpha.provider.EntityManagerHelper;
import com.ubtechinc.framework.db.annotation.Column;
import com.ubtechinc.framework.db.annotation.GenerationType;
import com.ubtechinc.framework.db.annotation.Id;
import com.ubtechinc.framework.db.annotation.Table;

/**
 * @desc : 闹钟信息实体
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/25
 * @modifier:
 * @modify_time:
 */
@Table(version = EntityManagerHelper.DB_ALARM_INFO_VERSION)
public class AlarmInfo {
    @Id(strategy = GenerationType.AUTO_INCREMENT)
    public int id;
    public int state;//0 未结束,1，结束
    @Column
    public int hh;//hour
    @Column
    public int mm;//minutes
    @Column
    public int repeat;// 0一次 1每天 2工作日 daysofweek
    @Column
    public boolean isUseAble;// 是否开启 enabled
    @Column
    public String actionStartName;// 闹钟开始时候执行动作
    @Column
    public String acitonEndName;// 闹钟结束时候执行动作 0 关机 1保持开机
    @Column
    public int actionType;// 0 动作表 1录音提醒 2拨打电话
    @Column
    public int yy;// 年
    @Column
    public int mo;// 月
    @Column
    public int day;// 日
    @Column
    public int date;// 星期
    @Column
    public int ss;// 秒
    @Column
    public int vibrate;//震动
    @Column
    public String label;
    @Column
    public Uri alert;
    @Column
    public boolean silent;
    @Column
    public String message;// 对应名称
    @Column
    public long dtstart;//运行毫秒值
    @Column
    public long dttime;
    @Column
    public boolean iscomplete;//是否执行完成

    @Override
    public String toString() {
        return "AlarmInfo[state="+state+",repeat="+repeat+",actionEndName="+acitonEndName+
                ",anctionStartName="+actionStartName+",isUserAble="+isUseAble+
                ",actionType="+actionType+",yy="+(2000+yy)+",mo="+mo+
                "day="+day+",date="+date+",hh="+ hh+",mm="+mm+",ss="+ss+
                ",vibrate="+vibrate + ",label=" +label+ "alert="+ alert.toString()+
                ",silent" + silent + ",dtstart=" + dtstart+ ",iscommplete=" + iscomplete+
                "dttime="+ dttime+ "]";
    }
}
