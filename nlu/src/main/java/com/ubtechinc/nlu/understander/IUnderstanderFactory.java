/*
 *
 *  *
 *  *  *
 *  *  * Copyright (c) 2008-2017 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *  *
 *  *
 *
 */
package com.ubtechinc.nlu.understander;

import android.content.Context;

/**
 * @desc: IUnderStander
 * @author: Logic.peng
 * @email : 2091843903@qq.com
 * @time  : 2017/3/22
 * @modifier:
 * @modify_time:
 */

public final class IUnderstanderFactory {

     public static IUnderstander createEmotibot(Context cxt) {
        EmotibotUnderstanderWrapper emotibot = new EmotibotUnderstanderWrapper(cxt);
        EmotibotUnderstanderWrapper.init(cxt);
        return emotibot;
    }

     public static IUnderstander createTunling(Context cxt){
        TunlingUnderstanderWrapper tunling = new TunlingUnderstanderWrapper(cxt);
        TunlingUnderstanderWrapper.init(cxt);
        return tunling;
    }

    public static IUnderstander createLocalUnder(Context cxt){
        LocalUnderstanderWrapper local = new LocalUnderstanderWrapper(cxt);
        LocalUnderstanderWrapper.init(cxt);
        return local;
    }

    public static IUnderstander createUBTServerUnder(Context cxt){
        UBTUnderstanderWrapper ubt = new UBTUnderstanderWrapper(cxt);
        UBTUnderstanderWrapper.init(cxt);
        return ubt;
    }

}
