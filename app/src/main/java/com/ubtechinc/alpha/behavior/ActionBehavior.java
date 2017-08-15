package com.ubtechinc.alpha.behavior;

import android.text.TextUtils;

import com.ubtech.utilcode.utils.thread.HandlerUtils;
import com.ubtechinc.alpha.app.AlphaApplication;
import com.ubtechinc.alpha.ops.RobotOpsManager;
import com.ubtechinc.alpha.ops.action.ActionPlayListener;
import com.ubtechinc.alpha.sdk.SdkConstants;

import timber.log.Timber;

/**
 * @desc :
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/5
 * @modifier:
 * @modify_time:
 */

public class ActionBehavior extends Behavior implements ActionPlayListener {
    private String action;
    private int speed;

    void setAction(String action) {
        this.action = action;
    }

    void setSpeed(int speed) {
        this.speed = speed;
    }

    @Override
    public boolean onStart() {
        if (TextUtils.isEmpty(action)) return false;
        RobotOpsManager.get(AlphaApplication.getContext()).playAction(action, this);
        Timber.d(TAG, "ActionBehavior start: action= %s", action);
        return true;
    }

    @Override
    public void onActionResult(final int nErr) {
        HandlerUtils.runUITask(new Runnable() {
            @Override
            public void run() {
                resultCallback.onBehaviorResult(nErr == SdkConstants.ErrorCode.RESULT_SUCCESS);
            }
        }, 500);//delay让动作做完
    }
}
