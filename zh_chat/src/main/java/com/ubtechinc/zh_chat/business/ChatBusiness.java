package com.ubtechinc.zh_chat.business;

import android.content.Context;
import android.text.TextUtils;

import com.ubtechinc.zh_chat.nets.business.AddRecord;
import com.ubtechinc.zh_chat.robot.UBTSemanticRootProxy;

import java.util.ArrayList;
import java.util.Random;

/**
 * <pre>
 *   author: Logic
 *   email : 2091843903@qq.com
 *   time  : 2017/3/24
 *   desc  : 闲聊
 * </pre>
 */
public class ChatBusiness extends BaseBusiness {

    private ArrayList<String> anserTextList = new ArrayList<>();// 聊天内容

    public void setAnswerText(String anserText) {
        anserTextList.add(anserText);
    }

    public ChatBusiness(Context cxt) {
        super(cxt);
    }

    @Override
    public void start(UBTSemanticRootProxy handle) {
        if(anserTextList.size() <= 0 ) return;
        String tts = anserTextList.get(new Random(System.currentTimeMillis()).nextInt(anserTextList.size()));
        handle.start_TTS(tts, false);
        if (!TextUtils.isEmpty(getSpeechResult()))
            AddRecord.instance().requestAddRecord(Type.CHAT.getValue(), tts, null, null, getSpeechResult());
    }

    @Override
    public void clean(UBTSemanticRootProxy handle) {
        anserTextList.clear();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ChatBusiness[");
        for (int i = 0, size = anserTextList.size() ; i < size ; i ++){
            sb.append(anserTextList.get(i));
            if (i != (size-1)){
                sb.append("\n");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
