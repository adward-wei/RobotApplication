package com.ubtechinc.alpha.affairdispatch;


import android.util.Log;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha.speech.SpeechServiceProxy;
import com.ubtechinc.alpha2services.BuildConfig;

/**
 * @desc : 事务管理器
 * @author: wzt
 * @time : 2017/5/18
 * @modifier:
 * @modify_time:
 */

public class BaseAffairManager implements IAffairStateListener {
    private static final String TAG = "BaseAffairManager";
    private Queue<BaseAffair> mAffairQueue;
    protected BaseAffair mWorkingAffair;
    private AffairExecute mAffairExecute;

    protected BaseAffairManager() {
        mAffairQueue = new PriorityQueue<BaseAffair>(11, OrderIsdn);
        mWorkingAffair = null;
        mAffairExecute = new AffairExecute();
        mAffairExecute.setAffairStateCallback(this);
    }

    public synchronized void processAffair(BaseAffair event) {
        LogUtils.d(TAG,"processAffair--affair :"+event);
        if(event.isNeedCheck() == true) {
            mAffairQueue.add(event);
            processNext();
        } else {
            mAffairExecute.execute(event, 0);
        }
    }

    protected void processNext() {
        Log.d(TAG, " processNext---mAffairQueue.length=" + mAffairQueue.size());
        BaseAffair nextAffair;
        nextAffair = mAffairQueue.peek();
        if(nextAffair != null) {
            if(mWorkingAffair == null) {
                nextAffair = mAffairQueue.poll(); // 从队列头部移除
                mWorkingAffair = nextAffair;
                Log.d(TAG, " mWorkingAffair.getObj=" + mWorkingAffair.getObj());
                mAffairExecute.execute(mWorkingAffair, 0);
            } else {
                LogUtils.d(TAG, " mWorkingAffair = " + mWorkingAffair);

                if(nextAffair.getPriority() > mWorkingAffair.getPriority()) {
                    if(mWorkingAffair.isCanPause()) {
                        mWorkingAffair.pause();
                    } else {
                        mWorkingAffair.stop();
                    }
                    mWorkingAffair = null;
                    processNext(); //执行下一个任务
                }
            }
        }
    }


    private Comparator<BaseAffair> OrderIsdn =  new Comparator<BaseAffair>(){
        public int compare(BaseAffair a1, BaseAffair a2) {
            int priorityA = a1.getPriority();
            int priorityB = a2.getPriority();
            if(priorityB > priorityA) {
                return -1;
            } else if(priorityB < priorityA) {
                return 1;
            } else {
                return 0;
            }

        }
    };

    //上一个任务执行完了
    @Override
    public void onAffairComplete(AbstractAffair event) {
        LogUtils.d(TAG,"onAffairComplete--affair : "+event);
        if (mWorkingAffair == event) {
            mWorkingAffair = null;
            processNext();
        }
    }

    @Override
    public void onAffairTimeOut(AbstractAffair event) {
        //todo:具体实现放在子类中
    }

    /** 待处理的事务的个数
     * @return
     */
    protected int getPendingAffairSize() {
        return mAffairQueue.size();
    }


    /**当前一个任务没有回调，导致Queue发送堵塞时，把mWorkingAffair设置为空，让其重新运行起来
     */
    protected void restartProcess() {
        mAffairQueue.clear();
        mWorkingAffair = null;
    }

    public String getWorkingAffairDesc() {
        if (mWorkingAffair != null) {
            return mWorkingAffair.toString();
        } else {
            return "mWorkingAffair is null";
        }
    }

    public void clearAllAffair() {
        if (mAffairQueue != null) {
            mAffairQueue.clear();
        }
    }
}
