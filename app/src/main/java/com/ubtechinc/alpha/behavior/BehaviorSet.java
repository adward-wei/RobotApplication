package com.ubtechinc.alpha.behavior;

import android.util.Log;

import com.ubtech.utilcode.utils.thread.HandlerUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @desc :
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/5
 * @modifier:
 * @modify_time:
 */

public class BehaviorSet extends Behavior {
    private BehaviorSet parent;
    private List<Behavior> childs = new ArrayList<>();
    //private ArrayMap<Behavior,BehaviorListener> listeners = new ArrayMap<>(10);
    private AtomicInteger curBehaviorId = new AtomicInteger(1);
    private AtomicInteger count = new AtomicInteger(0);
    private int maxId = 0;

    BehaviorSet getParent() {
        return parent;
    }

     void setParent(BehaviorSet parent) {
        this.parent = parent;
    }

    void addChildBehavior(final Behavior behavior){
        if (behavior == null) return;
        childs.add(behavior);
        if (behavior.getOrder() > maxId)
            maxId = behavior.getOrder();
        behavior.setBehaviorListener(new BehaviorListener() {
            @Override
            public void onCompleted() {
                //表示当前id组执行完毕
                if (count.decrementAndGet() == 0 ){
                    if (curBehaviorId.incrementAndGet() <= maxId) {
                        onStart();
                    }
                }
            }
        });
    }

    @Override
    protected boolean onStart() {
        if (childs.size() == 0) return false;
        final List<Behavior> root =  findBehaviors(curBehaviorId.get());
        int size = root.size();
        if (size == 0){
            if (curBehaviorId.incrementAndGet() <= maxId){
                return onStart();
            }else {
                resultCallback.onBehaviorResult(true);
                return true;
            }
        }else {
            Log.d(TAG, "curBehaviorId = " + curBehaviorId + ", size =" + size);
            count.set(size);
            HandlerUtils.runUITask(new Runnable() {
                @Override
                public void run() {
                    for (Behavior b : root) {
                        b.start();
                    }
                }
            }, 5);
        }
        return true;
    }

    private List<Behavior> findBehaviors(int curBehaviorId) {
        Log.d(TAG, "curBehaviorId = " + curBehaviorId + ", maxId =" + maxId);
        List<Behavior> list = new ArrayList<>();
        Iterator<Behavior> iter = childs.iterator();
        while (iter.hasNext()){
            Behavior next = iter.next();
            if (next.getOrder() == curBehaviorId){
                list.add(next);
            }
        }
        return list;
    }
}
