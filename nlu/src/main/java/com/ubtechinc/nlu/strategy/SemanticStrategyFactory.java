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
package com.ubtechinc.nlu.strategy;

import android.content.Context;

/**
 * @desc: 语义分析策略工厂
 * @author: Logic.peng
 * @email : 2091843903@qq.com
 * @time  : 2017/3/22
 * @modifier:
 * @modify_time:
 */

public final class SemanticStrategyFactory {

    public static SpeechGrammarStrategy createIUESemanticStrategy(Context cxt){
        return new IUESemanticStrategy(cxt);
    }

    public static SpeechGrammarStrategy createDefaultStrategy(Context cxt){
        return new DefaultSemanticStrategy(cxt);
    }

    public static SpeechGrammarStrategy createIESemanticStrategy(Context cxt){
        return new IESemanticStrategy(cxt);
    }

    public static SpeechGrammarStrategy createIUTSemanticStrategy(Context cxt) {
        return new IUTSemanticStrategy(cxt);
    }

    public static SpeechGrammarStrategy createEISemanticStategy(Context cxt){
        return new EISemanticStrategy(cxt);
    }
}
