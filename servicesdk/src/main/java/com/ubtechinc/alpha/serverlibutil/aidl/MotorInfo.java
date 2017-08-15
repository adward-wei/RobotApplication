package com.ubtechinc.alpha.serverlibutil.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @desc : 舵机信息实体
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/14
 * @modifier:
 * @modify_time:
 */
public class MotorInfo implements Parcelable {

    private int mId ;//--- 舵机ID
    private int mUpperLimitAngle; //--- 舵机转角上限，单位弧度
    private int mLowerLimitAngle; // -- int 舵机转角下限，单位弧度
    private int mRotatingSpeed;// -- int 舵机转速，单位毫秒
    private int mTorque; // -- float 舵机转矩，单位kg*cm

    public int getId() {
        return mId;
    }

    public int getUpperLimitAngle() {
        return mUpperLimitAngle;
    }

    public int getLowerLimitAngle() {
        return mLowerLimitAngle;
    }

    public int getRotatingSpeed() {
        return mRotatingSpeed;
    }

    public int getTorque() {
        return mTorque;
    }

    public MotorInfo(int id, int upperLimitAngle, int lowerLimitAngle, int speed, int torque){
        this.mId = id;
        this.mUpperLimitAngle = upperLimitAngle;
        this.mLowerLimitAngle = lowerLimitAngle;
        this.mRotatingSpeed = speed;
        this.mTorque = torque;
    }

    protected MotorInfo(Parcel in) {
        mId = in.readInt();
        mUpperLimitAngle = in.readInt();
        mLowerLimitAngle = in.readInt();
        mRotatingSpeed = in.readInt();
        mTorque = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeInt(mUpperLimitAngle);
        dest.writeInt(mLowerLimitAngle);
        dest.writeInt(mRotatingSpeed);
        dest.writeInt(mTorque);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MotorInfo> CREATOR = new Creator<MotorInfo>() {
        @Override
        public MotorInfo createFromParcel(Parcel in) {
            return new MotorInfo(in);
        }

        @Override
        public MotorInfo[] newArray(int size) {
            return new MotorInfo[size];
        }
    };
}
