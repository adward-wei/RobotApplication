package com.ubtechinc.zh_chat.business;

import android.content.Context;

import com.ubtech.iflytekmix.R;
import com.ubtech.utilcode.utils.StringUtils;
import com.ubtechinc.zh_chat.nets.business.AddRecord;
import com.ubtechinc.zh_chat.robot.UBTSemanticRootProxy;
import com.ubtechinc.zh_chat.utils.ActionUtil;

import java.util.ArrayList;
import java.util.Random;

import timber.log.Timber;

/**
 * <pre>
 *   @author: Logic
 *   @email : 2091843903@qq.com
 *   @time  : 2017/3/24
 *   @desc  : Excel中语义解析的机器人动作
 * </pre>
 */
public class LocalRobotActionBusiness extends BaseBusiness {

    public LocalRobotActionBusiness(Context cxt) {
        super(cxt);
    }

    private  ArrayList<String> answerList = new ArrayList<>();
    private String operation;


    private String slot;

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }


    public  void setAnswerList(ArrayList<String> answerList) {
        this.answerList.clear();
        this.answerList.addAll(answerList);
    }

    @Override
    public void start(UBTSemanticRootProxy handle) {
        final String tts = answerList.size() > 0 ?
                answerList.get(new Random(System.currentTimeMillis()).nextInt(answerList.size())) :
                null;
        if (tts != null)
            handle.start_TTS(tts, false);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Timber.w(e.getMessage());
        }
        handle.stop_Grammar();
        final String actionName = ActionUtil.getLocalActionName(operation, slot);
        handle.start_Action(actionName);
        AddRecord.instance().requestAddRecord(Type.ACTION.getValue(), actionName, null, tts, getSpeechResult());
    }

    @Override
    public void clean(UBTSemanticRootProxy handle) {

    }
}
