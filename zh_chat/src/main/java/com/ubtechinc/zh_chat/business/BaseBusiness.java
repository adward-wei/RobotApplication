package com.ubtechinc.zh_chat.business;

import android.content.Context;

import com.ubtechinc.nlu.iflytekmix.MixSemantic;
import com.ubtechinc.nlu.iflytekmix.pojos.Slots;
import com.ubtechinc.zh_chat.robot.UBTSemanticRootProxy;

/**
 * <pre>
 *   author: Logic
 *   email : 2091843903@qq.com
 *   time  : 2017/3/24
 *   desc  : alpha支持的行为
 * </pre>
 */
public abstract class BaseBusiness {
    private String url;
    private String content;
    private Slots slots;
    protected String operation;
    protected final Context mContext;
    private MixSemantic semantic;

    public BaseBusiness(Context cxt){
        this.mContext = cxt.getApplicationContext();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Slots getSlots() {
        return slots;
    }

    public void setSlots(Slots slots) {
        this.slots = slots;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public abstract void start(UBTSemanticRootProxy handle);

    public abstract void clean(UBTSemanticRootProxy handle);

    public Context getContext() {
        return mContext;
    }

    public String getSpeechResult() {
        return semantic == null ? null: semantic.getSpeechResult();
    }

    public void setSemantic(MixSemantic semantic) {
        this.semantic = semantic;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    public enum Type {
        CHAT(1), BAIKE(2), CACULATE(3), DATE(4), REMIND(5), CAMERA(6) ,
        WEATHER(7), DANCE(8), ACTION(9),EXCHANGE(10),JOKE(11),MUSIC(12),
        STORY(13),POEM(14),FRTUNE(15),TRANSLATION(16);
        private int value;
        Type(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }
}
