package com.ubtechinc.zh_chat.business;

import android.content.Context;

import com.ubtech.iflytekmix.R;
import com.ubtechinc.zh_chat.nets.business.AddRecord;
import com.ubtechinc.zh_chat.robot.UBTSemanticRootProxy;
import com.ubtechinc.zh_chat.utils.ActionUtil;

import timber.log.Timber;

/**
 * <pre>
 *   @author: Logic
 *   @email : 2091843903@qq.com
 *   @time  : 2017/3/24
 *   @desc  : 机器人动作行为
 * </pre>
 */
public class RobotActionBusiness extends BaseBusiness {

    private boolean needTTS = true;

    public RobotActionBusiness(Context cxt) {
        super(cxt);
    }

    /**
     * 是否offline
     */
    private boolean isOffline;
    public boolean isOfflineGrammar() {
        return isOffline;
    }

    public void setOfflineGrammar(boolean offline) {
        isOffline = offline;
    }

    public void setNeedTTS(boolean needTTS){
        this.needTTS = needTTS;
    }

    @Override
    public void start(UBTSemanticRootProxy handle) {
        if (needTTS) {
            handle.start_TTS(
                    mContext.getString(R.string.robot_ok_tip), false);
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Timber.w(e.getMessage());
        }
        String actionName;
        if(isOfflineGrammar()) {
            actionName = ActionUtil.getOfflineActionName(operation);
        }else {
            actionName = ActionUtil.getNetActionName(operation, getSlots());
        }

        handle.stop_Grammar();
        handle.start_Action(actionName);
        AddRecord.instance().requestAddRecord(Type.ACTION.getValue(), actionName, null, needTTS ? mContext.getString(R.string.robot_ok_tip) : "", getSpeechResult());
    }

    @Override
    public void clean(UBTSemanticRootProxy handle) {

    }
}
