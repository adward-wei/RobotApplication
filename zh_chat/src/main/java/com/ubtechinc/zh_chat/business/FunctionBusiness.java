package com.ubtechinc.zh_chat.business;

import android.content.Context;
import android.text.TextUtils;

import com.ubtech.utilcode.utils.thread.HandlerUtils;
import com.ubtechinc.zh_chat.robot.UBTSemanticRootProxy;

/**
 * <pre>
 *   @author: Logic
 *   @email : 2091843903@qq.com
 *   @time  : 2017/3/24
 *   @desc  :
 * </pre>
 */
public class FunctionBusiness extends BaseBusiness {

    public FunctionBusiness(Context cxt) {
        super(cxt);
    }

    @Override
    public void start(final UBTSemanticRootProxy handle) {
        if (!TextUtils.isEmpty(operation))
            handle.startLocalFunction(operation);
        HandlerUtils.runUITask(new Runnable() {
            @Override
            public void run() {
                handle.start_Grammar();
            }
        }, 3000);
    }

    @Override
    public void clean(UBTSemanticRootProxy handle) {

    }
}
