package com.ubtechinc.alpha.serverlibutil.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @desc :
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/27
 * @modifier:
 * @modify_time:
 */

public final class MotorAngle implements Parcelable {
    private int mId;
    private int angle;

    public void setId(int mId) {
        this.mId = mId;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public int getId() {
        return mId;
    }

    public int getAngle() {
        return angle;
    }

    public MotorAngle(int mId, int angle) {
        this.mId = mId;
        this.angle = angle;
    }

    public MotorAngle() {
    }

    protected MotorAngle(Parcel in) {
        mId = in.readInt();
        angle = in.readInt();
    }

    public static final Creator<MotorAngle> CREATOR = new Creator<MotorAngle>() {
        @Override
        public MotorAngle createFromParcel(Parcel in) {
            return new MotorAngle(in);
        }

        @Override
        public MotorAngle[] newArray(int size) {
            return new MotorAngle[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeInt(angle);
    }
}
