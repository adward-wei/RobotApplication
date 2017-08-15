package com.ubtechinc.alpha.affairdispatch;

import com.ubtechinc.alpha.affairdispatch.constants.AffairState;
import com.ubtechinc.alpha.affairdispatch.constants.AffairType;

/**
 * @desc : 抽象事务
 * @author: wzt
 * @time : 2017/5/18
 * @modifier:
 * @modify_time:
 */

public abstract class AbstractAffair {
    private int mPriority;
    private AffairType mType;
    private AffairState mState;
    private int mIndex;
    protected int mID;
    /** 是否可被打断 **/
    protected boolean mCanPause = true;
    /** 是否需要检查 **/
    protected boolean mNeedCheck = true;
    protected boolean mCompatible = false;

    private Object mObj;


    public AbstractAffair() {

    }

    public int getPriority() {
        return mPriority;
    }

    public void setPriority(int mPriority) {
        this.mPriority = mPriority;
    }

    public AffairState getState() {
        return mState;
    }

    public void setState(AffairState mState) {
        this.mState = mState;
    }

    public int getID() {
        return mID;
    }

    public void setID(int mID) {
        this.mID = mID;
    }

    public abstract void start();
    public abstract void pause();
    public abstract void resume();
    public abstract void stop();
    public abstract void execute(AffairCallback callback);
    public abstract long calcuteExecuteTime();
    public int getIndex() {
        return mIndex;
    }

    public void setIndex(int mIndex) {
        this.mIndex = mIndex;
    }

    public boolean isCanPause() {
        return mCanPause;
    }

    public void setCanPause(boolean mCanPause) {
        this.mCanPause = mCanPause;
    }

    public Object getObj() {
        return mObj;
    }

    public void setObj(Object mObj) {
        this.mObj = mObj;
    }

    public boolean isNeedCheck() {
        return mNeedCheck;
    }

    public void setNeedCheck(boolean mNeedCheck) {
        this.mNeedCheck = mNeedCheck;
    }

    public AffairType getType() {
        return mType;
    }

    public void setType(AffairType mType) {
        this.mType = mType;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString()).append(",")
                .append("mType:").append(mType).append(",")
                .append("mPriority:").append(mPriority).append(",")
                .append("mID:").append(mID).append(",");
        return builder.toString();

    }
}
