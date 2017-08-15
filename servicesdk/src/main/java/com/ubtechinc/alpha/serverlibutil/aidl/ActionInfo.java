/*
 *
 *  *
 *  * Copyright (c) 2008-2016 UBTECH Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBTECH.
 *  *
 *
 */
package com.ubtechinc.alpha.serverlibutil.aidl;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Locale;

/**
 * <pre>
 *   @author: Logic
 *   @email : logic.peng@ubtech.com
 *   @time  : 2017/4/14
 *   @desc  : 动作信息实体
 * </pre>
 */
public class ActionInfo implements Parcelable {
    private String id;
    private String cn_name;// -- String类型，Action名称
    private String en_name;
    private String desc;// -- String 类型，Action文字描述, 比如说是抬左手
    private int time;// -- long, Action时长
    private String type;//类型："1"，"2"，"3"

    protected ActionInfo(Parcel in) {
        id = in.readString();
        cn_name = in.readString();
        en_name = in.readString();
        desc = in.readString();
        time = in.readInt();
        type = in.readString();
    }

    public static final Creator<ActionInfo> CREATOR = new Creator<ActionInfo>() {
        @Override
        public ActionInfo createFromParcel(Parcel in) {
            return new ActionInfo(in);
        }

        @Override
        public ActionInfo[] newArray(int size) {
            return new ActionInfo[size];
        }
    };

    public String getName() {
        if (Locale.getDefault().equals(Locale.CHINESE))
            return cn_name;
        else
            return en_name;
    }

    public String getDesc() {
        return desc;
    }

    public long getDuration() {
        return time;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public int getTime() {
        return time;
    }

    public ActionInfo(String id, String cn_name, String en_name, String desc, int duration, String type){
        this.cn_name = cn_name ==null? "": cn_name;
        this.desc = desc == null? "": desc;
        this.time = duration;
        this.type = type == null? "":type;
        this.id = id == null? "": id;
        this.en_name = en_name == null? "": en_name;
    }

    @Override
    public String toString() {
        return "ActionInfo[name:"+ cn_name +";desc:"+desc+";duration:"+ time +"]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(cn_name);
        dest.writeString(en_name);
        dest.writeString(desc);
        dest.writeInt(time);
        dest.writeString(type);
    }
}
