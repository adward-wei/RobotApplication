package com.ubtechinc.zh_chat.business;

import android.content.Context;

import com.ubtech.iflytekmix.R;
import com.ubtechinc.zh_chat.MixApplication;
import com.ubtechinc.zh_chat.robot.UBTSemanticRootProxy;

/**
 * <pre>
 *   author: Logic
 *   email : 2091843903@qq.com
 *   time  : 2017/3/24
 *   desc  : 自我介绍
 * </pre>
 */

public class SelfBriefBusinesss extends BaseBusiness {

    public SelfBriefBusinesss(Context cxt) {
        super(cxt);
    }

    @Override
    public void start(UBTSemanticRootProxy handle) {
        boolean isFirstRun = MixApplication.getSettingBoolean("isFirstRun", true);
        String text = isFirstRun? mContext.getString(R.string.start_hint1) : mContext.getString(R.string.start_hint2);
        handle.start_TTS(text, false);
//        AddRecord.instance().requestAddRecord(Type.BAIKE.getValue(), text, null, null, getSpeechResult());
        if (isFirstRun)
            MixApplication.setSettingBoolean("isFirstRun", false);
    }

    @Override
    public void clean(UBTSemanticRootProxy handle) {

    }
}
