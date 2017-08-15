package com.ubtechinc.zh_chat.business;

import android.content.Context;

/**
 * <pre>
 *   author: Logic
 *   email : 2091843903@qq.com
 *   time  : 2017/3/24
 *   desc  : 笑话，故事
 * </pre>
 */
public class FunPlayBusiness extends PlayBusiness {
    public FunPlayBusiness(Context cxt) {
        super(cxt);
    }

    @Override
    protected Type getType() {
        return Type.STORY;
    }
}
