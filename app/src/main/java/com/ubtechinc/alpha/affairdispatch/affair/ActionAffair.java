package com.ubtechinc.alpha.affairdispatch.affair;

import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha.affairdispatch.AffairCallback;
import com.ubtechinc.alpha.affairdispatch.BaseAffair;
import com.ubtechinc.alpha.affairdispatch.constants.AffairType;
import com.ubtechinc.alpha.ops.ActionServiceProxy;
import com.ubtechinc.alpha.ops.action.ActionPlayListener;

/**
 * description: 播放动作的事务实体
 * autour: bob.xu
 * date: 2017/6/30 16:30
 * update: 2017/6/30
 * version: a
*/
public class ActionAffair extends BaseAffair {

    private String actionName;
    private ActionPlayListener listener;
    public static final String TAG = "ActionAffair";

    private ActionAffair(String actionName,ActionPlayListener listener,int priority) {
        super();
        this.actionName = actionName;
        this.listener = listener;
        setPriority(priority);
        setType(AffairType.EVENT_TYPE_ACTION);
    }

    public String getActionName() {
        return actionName;
    }

    public ActionPlayListener getListener() {
        return listener;
    }

    public static class Builder {

        private String actionName;
        private ActionPlayListener listener;
        private int priority;

        public Builder setActionName(String actionName) {
            this.actionName = actionName;
            return this;
        }

        public Builder setListener(ActionPlayListener listener) {
            this.listener = listener;
            return this;
        }

        public Builder setPriority(int priority) {
            this.priority = priority;
            return this;
        }

        public ActionAffair createActionAffair() {
            return new ActionAffair(actionName, listener, priority);
        }
    }

    @Override
    public void execute(final AffairCallback callback) {
        ActionServiceProxy.getInstance().onRealPlayAction(actionName,new ActionPlayListener() {

            @Override
            public void onActionResult(int nErr) {
                LogUtils.d(TAG,"onActionResult---nErr = "+nErr);
                if (listener != null) {
                    listener.onActionResult(nErr);
                }
                if (callback != null) {
                    callback.onComplete();
                }
            }

        });
    }

    @Override
    public long calcuteExecuteTime() {
        return 10*60*1000; //兜底的时间，如果10分钟还没执行完的话，当做出错了。10分钟后事务处理模块能继续执行动作。
    }

    @Override
    public void stop() {
        super.stop();
        ActionServiceProxy.getInstance().stopAction(null);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString()).append(",")
                .append("actionName:").append(getActionName()).append(",");
        return builder.toString();
    }
}
