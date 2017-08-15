package com.ubtechinc.alpha.serverlibutil.aidl;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * @desc :
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/6/12
 * @modifier:
 * @modify_time:
 */

public class AlarmInfo implements Parcelable {
    public int id;
    public int state;//0 未结束,1，结束
    public int hh;
    public int mm;
    public int repeat;// 0一次 1每天 2工作日
    public boolean isUseAble;// 是否开启
    public String actionStartName;// 闹钟开始时候执行动作
    public String acitonEndName;// 闹钟结束时候执行动作 0 关机 1保持开机
    public int actionType;// 0 动作表 1录音提醒 2拨打电话
    public int yy;// 年
    public int mo;// 月
    public int day;// 日
    public int date;// 星期
    public int ss;// 秒
    public boolean vibrate;//震动
    public String label;//
    public Uri alert;
    public boolean silent;//静音
    public long dtstart;
    public boolean iscomplete;
    public long dttime;

    public AlarmInfo(){

    }

    protected AlarmInfo(Parcel in) {
        id = in.readInt();
        state = in.readInt();
        hh = in.readInt();
        mm = in.readInt();
        repeat = in.readInt();
        isUseAble = in.readByte() != 0;
        actionStartName = in.readString();
        acitonEndName = in.readString();
        actionType = in.readInt();
        yy = in.readInt();
        mo = in.readInt();
        day = in.readInt();
        date = in.readInt();
        ss = in.readInt();
        vibrate = in.readByte() != 0;
        label = in.readString();
        alert = in.readParcelable(Uri.class.getClassLoader());
        silent = in.readByte() != 0;
        dtstart = in.readLong();
        iscomplete = in.readByte() != 0;
        dttime = in.readLong();
    }

    public static final Creator<AlarmInfo> CREATOR = new Creator<AlarmInfo>() {
        @Override
        public AlarmInfo createFromParcel(Parcel in) {
            return new AlarmInfo(in);
        }

        @Override
        public AlarmInfo[] newArray(int size) {
            return new AlarmInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(state);
        dest.writeInt(hh);
        dest.writeInt(mm);
        dest.writeInt(repeat);
        dest.writeByte((byte) (isUseAble ? 1 : 0));
        dest.writeString(actionStartName);
        dest.writeString(acitonEndName);
        dest.writeInt(actionType);
        dest.writeInt(yy);
        dest.writeInt(mo);
        dest.writeInt(day);
        dest.writeInt(date);
        dest.writeInt(ss);
        dest.writeByte((byte) (vibrate ? 1 : 0));
        dest.writeString(label);
        dest.writeParcelable(alert, flags);
        dest.writeByte((byte) (silent ? 1 : 0));
        dest.writeLong(dtstart);
        dest.writeByte((byte) (iscomplete ? 1 : 0));
        dest.writeLong(dttime);
    }

    @Override
    public String toString() {
        return "AlarmInfo[state=" + state + ",repeat=" + repeat + ",actionEndName=" + acitonEndName +
                ",anctionStartName=" + actionStartName + ",isUserAble=" + isUseAble +
                ",actionType=" + actionType + ",yy=" + (2000 + yy) + ",mo=" + mo +
                "day=" + day + ",date=" + date + ",hh=" + hh + ",mm=" + mm + ",ss=" + ss +
                ",vibrate=" + vibrate + ",label=" + label + "alert=" + alert.toString() +
                ",silent" + silent + ",dtstart=" + dtstart + ",iscommplete=" + iscomplete +
                "dttime=" + dttime + "]";
    }
}
