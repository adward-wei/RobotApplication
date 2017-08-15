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
package com.ubtechinc.nlu.iflytekmix;

import android.content.Context;
import android.text.TextUtils;

 /**
 * @desc: 混合语义解析器
 * @author: Logic.peng
 * @email : 2091843903@qq.com
 * @time  : 2017/3/22
 * @modifier:
 * @modify_time:
 */

public class MixSemanticResolver implements Resolver {
    protected final Context cxt;

    public MixSemanticResolver(Context context){
        this.cxt = context;
    }

    @Override
    public MixSemantic resolve(String originStr) {
        MixSemantic mixSemantic;

        mixSemantic = new OfflineSemanticResolver(cxt).resolve(originStr);
        String speechResult = null;

        if (mixSemantic == null){
            mixSemantic = new NetSemanticResolver(cxt).resolve(originStr);
        }else if (!mixSemantic.semanticValid){
            speechResult = mixSemantic.speechResult;
        }

        if (mixSemantic != null
                && !mixSemantic.semanticValid
                && (!TextUtils.isEmpty(mixSemantic.speechResult)|| !TextUtils.isEmpty(speechResult)))
        {
            speechResult = TextUtils.isEmpty(mixSemantic.speechResult)? speechResult : mixSemantic.speechResult;
            mixSemantic = new ExcelSemanticResolver(cxt,speechResult).resolve(originStr);
        }

        if (mixSemantic == null){
            //科飞直接返回识别文本，非json格式， 直接交给第三方语义分析?
            mixSemantic = new MixSemantic();
            mixSemantic.speechResult = TextUtils.isEmpty(speechResult) ? originStr : speechResult;
            mixSemantic.semanticValid = false;
        }
        return mixSemantic;
    }
}
